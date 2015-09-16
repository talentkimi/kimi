package logging.log4j;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import logging.TpLogIndexEntry;
import logging.TpLogRecord;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

import core.text.DateUtil;
import core.util.UtilDate;
import core.xml.Xml;
import core.xml.XmlNode;

/**
 * This log records is wrapper for log4j event. 
 * @author Dimitrijs
 */
public class Log4jLogRecord extends TpLogRecord implements Externalizable {
	private static final long serialVersionUID = 2L;
	public static final String TYPE = "log4j";
	
	private String name;
	private String message;
	private String level;
	private String category;
	private String[] throwable;
	private String threadName;
	private String className;
	private String fileName;
	private String lineNumber;
	private String methodName;
	private int uniqueNumber;
	private String taskName;
	
	public Log4jLogRecord() {
	}

	
	public Log4jLogRecord(long time) {
		super(time);
	}

	public Log4jLogRecord(String name, LoggingEvent event, String taskName) {
		super(event.getTimeStamp());
		this.name = name;
		this.taskName = taskName;
		message = event.getRenderedMessage();
		level = event.getLevel().toString();
		category = event.getLoggerName();
		throwable = event.getThrowableStrRep();
		threadName = event.getThreadName();
		final LocationInfo li = event.getLocationInformation();
		className = li.getClassName();
		fileName = li.getFileName();
		lineNumber = li.getLineNumber();
		methodName = li.getMethodName();
//		uniqueNumber = TripPlannerServerConfig.getUniqueNumber();
	}

	@Override
	public TpLogIndexEntry[] createIndexEntries() {
		return new TpLogIndexEntry[] {new Log4jTimeLogIndexEntry(this)};
	}

	@Override
	public String getLoggerId() {
		return TYPE + "." + name;
	}

	@Override
	public void maskPrivate() {
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		super.readExternal(in);
		final int ver = in.readInt(); // version
		switch (ver) {
			case 2:
				taskName = (String) in.readObject();
				name = (String) in.readObject();
				message = (String) in.readObject();
				level = (String) in.readObject();
				category = (String) in.readObject();
				throwable = (String[]) in.readObject();
				threadName = (String) in.readObject();
				className = (String) in.readObject();
				fileName = (String) in.readObject();
				lineNumber = (String) in.readObject();
				methodName = (String) in.readObject();
				uniqueNumber = in.readInt();
				break;
			default:
				throw new IllegalStateException("Unsupported version " + ver);	
		}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);
		out.writeInt(2); // version
		out.writeObject(taskName);
		out.writeObject(name);
		out.writeObject(message);
		out.writeObject(level);
		out.writeObject(category);
		out.writeObject(throwable);
		out.writeObject(threadName);
		out.writeObject(className);
		out.writeObject(fileName);
		out.writeObject(lineNumber);
		out.writeObject(methodName);
		out.writeInt(getUniqueNumber());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String[] getThrowable() {
		return throwable;
	}

	public void setThrowable(String[] throwable) {
		this.throwable = throwable;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public int getUniqueNumber() {
		if (uniqueNumber == 0) {
//			uniqueNumber = TripPlannerServerConfig.getUniqueNumber();
		}
		return uniqueNumber;
	}

	public void setUniqueNumber(int uniqueNumber) {
		this.uniqueNumber = uniqueNumber;
	}

	public String getTaskName() {
		return taskName;
	}


	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}


	@Override
	public Xml toXml() {
		final DateFormat fmt = DateUtil.getDateFormat(UtilDate.FORMAT_PATTERN_MILLIS);
		final Xml recXml = new XmlNode("Log4jLogRecord");

		recXml.addChild(new XmlNode("HostId", getUniqueNumber()));
		recXml.addChild(new XmlNode("Name", name));
		recXml.addChild(new XmlNode("Time", fmt.format(new Date(getTime()))));
		recXml.addChild(new XmlNode("Level", level));
		recXml.addChild(new XmlNode("Category", category));
		recXml.addChild(new XmlNode("Thread", threadName));
		recXml.addChild(new XmlNode("Class", className));
		recXml.addChild(new XmlNode("Method", methodName));
		recXml.addChild(new XmlNode("File", fileName));
		recXml.addChild(new XmlNode("Line", lineNumber));
		recXml.addChild(new XmlNode("Message", message));
		recXml.addChild(new XmlNode("TaskName", taskName));

		if (throwable != null && throwable.length > 0) {
			final Xml throwableXml = new XmlNode("StackTrace");
			recXml.addChild(throwableXml);
			for (String t : throwable) {
				throwableXml.addChild(new XmlNode("Element", t));
			}
		}

		return recXml;
	}
	
	public static Log4jLogRecord fromXml(Xml xml) {
		final DateFormat fmt = DateUtil.getDateFormat(UtilDate.FORMAT_PATTERN_MILLIS);
		Date time;
		try {
			time = fmt.parse(xml.getChild("Time").getValue().toString());
		} catch (ParseException ex) {
			time = new Date();
		}
		final Log4jLogRecord result = new Log4jLogRecord(time.getTime()); 
		result.uniqueNumber = Integer.parseInt(xml.getChild("HostId").getValue().toString());
		result.name = xml.getChild("Name").getValue().toString();
		result.level = xml.getChild("Level").getValue().toString();
		result.category = xml.getChild("Category").getValue().toString();
		result.threadName = xml.getChild("Thread").getValue().toString();
		result.className = xml.getChild("Class").getValue().toString();
		result.methodName = xml.getChild("Method").getValue().toString();
		result.fileName = xml.getChild("File").getValue().toString();
		result.lineNumber = xml.getChild("Line").getValue().toString();
		
		Xml msg = xml.getChild("Message", true);
		if (msg != null) {
			result.message = msg.getValue().toString();
		}
		
		Xml task = xml.getChild("TaskName", true);
		if (task != null) {
			result.taskName = task.getValue().toString();
		}
		
		Xml stack = xml.getChild("StackTrace", true);
		if (stack != null) {
			List<String> elements = new LinkedList<String>();
			for (Xml child : stack.childIterator("Element")) {
				elements.add(child.getValue().toString());
			}
			result.throwable = elements.toArray(new String[elements.size()]);
		}
		return result;
	}
}
