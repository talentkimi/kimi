package core.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.io.file.TextFile;
import core.text.Text;
import core.util.bytes.Bytes;
import core.util.bytes.Gigabytes;
import core.util.bytes.Kilobytes;
import core.util.bytes.Megabytes;
import core.util.time.Days;
import core.util.time.Hours;
import core.util.time.Milliseconds;
import core.util.time.Minutes;
import core.util.time.Seconds;
import core.xml.Xml;
import core.xml.XmlException;

/**
 * A Configuration File.
 */
public final class ConfigClassFile extends TextFile {

	private static final Logger log = LoggerFactory.getLogger(ConfigClassFile.class);


	/** The config xml. */
	private Xml configXml = null;
	/** The directory. */
	private final String directory;
	/** The class list. */
	private final UtilList classList = new UtilList();

	/**
	 * Returns the number of config classes.
	 * @return the number of config classes.
	 */
	public final int configs() {
		return classList.size();
	}

	/**
	 * Returns the config class at the given index.
	 * @param index the index.
	 * @return the config class.
	 */
	public final Class getConfig(int index) {
		return (Class) classList.get(index);
	}

	/**
	 * Returns the config xml.
	 * @return the config xml.
	 */
	public final Xml getXml() {
		return configXml;
	}

	/**
	 * Returns the xml as a string.
	 * @return the xml as a string.
	 */
	public String toString() {
		return Xml.WRITER.write(configXml);
	}

	/**
	 * Creates a new config file.
	 * @param filename the filename.
	 */
	public ConfigClassFile(String directory, boolean debug) throws IOException, IllegalAccessException, ParseException {
		super(directory);
		if (directory == null) {
			throw new NullPointerException();
		}
		this.directory = directory;
		loadXml(debug);
	}

	/**
	 * Load the xml from the file.
	 */
	public final synchronized void loadXml(boolean debug) throws IOException, IllegalAccessException, ParseException {
		this.configXml = new ConfigClassDirectory(debug).loadFromDirectory(this);
		for (int i = 0; i < classList.size(); i++) {
			Class theClass = (Class) classList.get(i);
			setConfig(theClass);
		}
	}

	/**
	 * Add the given class (that implements Config) to this file.
	 * @param configClass the config class.
	 */
	public final synchronized void addConfig(Class configClass) throws Exception {
		if (!configClass.getName().endsWith("Config")) {
			throw new IllegalArgumentException("Config class name must end with 'Config': \"" + configClass + "\"");
		}
		if (classList.contains(configClass)) {
			throw new IllegalArgumentException("Attempt to add class again: \"" + configClass + "\"");
		}
		classList.add(configClass);
		setConfig(configClass);
	}

	/**
	 * Sets the config for the given class.
	 * @param theClass the class.
	 */
	private void setConfig(Class theClass) throws IllegalAccessException, ParseException {
		// Class name
		String className = Text.getSimpleName(theClass);
		className = className.substring(0, className.indexOf("Config"));
		int count = configXml.children(className);
		if (count == 0) {
			throw new XmlException("Config group not found: <" + className + ">");
		}
//		if (count == 0) {
//			if (log.isDebugEnabled()) log.debug ("[WARNING]   Config group not found: <" + className + "> (using default values)");
//			return;
//		}
		if (count > 1) {
			throw new XmlException("Duplicate config group: <" + className + ">");
		}
		Xml branch = configXml.getChild(className);

		// Find all setX(String s) methods
		Field[] fields = theClass.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			Class fieldClass = field.getType();
			String fieldName = field.getName();
			if(fieldName.equals("serialVersionUID") || (fieldName.indexOf("_tc") >= 0)){
				// ignore serialisation id and terracotta auto-inserted fields
				continue;
			}
			count = branch.children(fieldName);
			if (count == 0) {
				throw new XmlException("Config data not found: <" + className + "><" + fieldName + ">");
			}
//			if (count == 0) {
//				if (log.isDebugEnabled()) log.debug ("[WARNING]   Config data not found: <" + className + "><" + fieldName + "> (using default value)");
//				continue;
//			}
			if (count > 1) {
				throw new XmlException("Duplicate config data: <" + className + "><" + fieldName + ">");
			}
			Xml leaf = branch.getChild(fieldName);
			if (fieldClass.equals(Xml.class)) {
				field.set(null, leaf);
			} else {

				String value = (String) leaf.getValue();

				// Basic Types
				if (fieldClass.equals(String.class)) {
					field.set(null, value);
				} else if (fieldClass.equals(boolean.class)) {
					field.setBoolean(null, parseBoolean(className, fieldName, value));
				} else if (fieldClass.equals(byte.class)) {
					field.setByte(null, parseByte(className, fieldName, value));
				} else if (fieldClass.equals(short.class)) {
					field.setShort(null, parseShort(className, fieldName, value));
				} else if (fieldClass.equals(int.class)) {
					field.setInt(null, parseInt(className, fieldName, value));
				} else if (fieldClass.equals(long.class)) {
					field.setLong(null, parseLong(className, fieldName, value));
				} else if (fieldClass.equals(float.class)) {
					field.setFloat(null, parseFloat(className, fieldName, value));
				} else if (fieldClass.equals(double.class)) {
					field.setDouble(null, parseDouble(className, fieldName, value));
				} else if (fieldClass.equals(char.class)) {
					field.setChar(null, parseChar(className, fieldName, value));
				} else if (fieldClass.equals(Milliseconds.class)) {
					field.set(null, parseMilliseconds(className, fieldName, value));
				} else if (fieldClass.equals(Seconds.class)) {
					field.set(null, parseSeconds(className, fieldName, value));
				} else if (fieldClass.equals(Minutes.class)) {
					field.set(null, parseMinutes(className, fieldName, value));
				} else if (fieldClass.equals(Hours.class)) {
					field.set(null, parseHours(className, fieldName, value));
				} else if (fieldClass.equals(Days.class)) {
					field.set(null, parseDays(className, fieldName, value));
				} else if (fieldClass.equals(Percent.class)) {
					field.set(null, parsePercent(className, fieldName, value));
				} else if (fieldClass.equals(Bytes.class)) {
					field.set(null, parseBytes(className, fieldName, value));
				} else if (fieldClass.equals(Kilobytes.class)) {
					field.set(null, parseKilobytes(className, fieldName, value));
				} else if (fieldClass.equals(Megabytes.class)) {
					field.set(null, parseMegabytes(className, fieldName, value));
				} else if (fieldClass.equals(Gigabytes.class)) {
					field.set(null, parseGigabytes(className, fieldName, value));
				} else if (fieldClass.equals(UtilDate.class)) {
					field.set(null, parseUtilDate(className, fieldName, value));
				} else if (fieldClass.equals(Class.class)) {
					field.set(null, parseClass(className, fieldName, value));
				} else if (fieldClass.equals(String[].class)) {
					field.set(null, parseStringArray(className, fieldName, leaf));
				} else {
					field.set(null, parseConfigClass(className, fieldName, leaf, fieldClass));
				}
			}
		}
	}

	private Object parseStringArray(String className, String fieldName, Xml leaf) {
		try {
			int length = leaf.children();
			if (length == 0) {
				throw new XmlException("<" + leaf.getName() + "> has no children");
			}
			String[] array = new String[length];
			for (int i = 0; i < array.length; i++) {
				array[i] = leaf.getChild(i).getValue().toString();
				if (array[i] == null) {
					throw new NullPointerException();
				}
			}
			return array;
		} catch (XmlException xe) {
			throw xe;
		} catch (Exception e) {
			if (log.isDebugEnabled()) log.debug ("[Error]", e);
			throw new XmlException("Class not supported: <" + className + "><" + fieldName + ">");
		}
	}

	private Object parseConfigClass(String className, String fieldName, Xml leaf, Class fieldClass) {
		try {
			ConfigClass configClass = (ConfigClass) fieldClass.newInstance();
			configClass.parseFrom(leaf);
			return configClass;
		} catch (XmlException xe) {
			throw xe;
		} catch (Exception e) {
			if (log.isDebugEnabled()) log.debug ("[Error]", e);
			throw new XmlException("Class not supported: <" + className + "><" + fieldName + "> \"" + fieldClass + "\"");
		}
	}

	/**
	 * Parse a boolean from the given value.
	 * @param className the class name.
	 * @param fieldName the field name.
	 * @param value the value to parse from.
	 * @return the boolean.
	 */
	private final boolean parseBoolean(String className, String fieldName, String value) {
		if (value != null) {
			if (value.equalsIgnoreCase("true")) {
				return true;
			}
			if (value.equalsIgnoreCase("false")) {
				return false;
			}
		}
		throw new XmlException("Config data not a boolean: <" + className + "><" + fieldName + "> \"" + value + "\"");
	}

	/**
	 * Parse an integer from the given value.
	 * @param className the class name.
	 * @param fieldName the field name.
	 * @param value the value to parse from.
	 * @return the integer.
	 */
	private final int parseInt(String className, String fieldName, String value) {
		try {
			return Integer.parseInt(value);
		} catch (RuntimeException re) {
			throw new XmlException("Config data not an integer: <" + className + "><" + fieldName + "> \"" + value + "\"");
		}
	}

	/**
	 * Parse a long from the given value.
	 * @param className the class name.
	 * @param fieldName the field name.
	 * @param value the value to parse from.
	 * @return the long.
	 */
	private final long parseLong(String className, String fieldName, String value) {
		try {
			return Long.parseLong(value);
		} catch (RuntimeException re) {
			throw new XmlException("Config data not a long: <" + className + "><" + fieldName + "> \"" + value + "\"");
		}
	}

	/**
	 * Parse a short from the given value.
	 * @param className the class name.
	 * @param fieldName the field name.
	 * @param value the value to parse from.
	 * @return the short.
	 */
	private final short parseShort(String className, String fieldName, String value) {
		try {
			return Short.parseShort(value);
		} catch (RuntimeException re) {
			throw new XmlException("Config data not a short: <" + className + "><" + fieldName + "> \"" + value + "\"");
		}
	}

	/**
	 * Parse a byte from the given value.
	 * @param className the class name.
	 * @param fieldName the field name.
	 * @param value the value to parse from.
	 * @return the byte.
	 */
	private final byte parseByte(String className, String fieldName, String value) {
		try {
			return Byte.parseByte(value);
		} catch (RuntimeException re) {
			throw new XmlException("Config data not a byte: <" + className + "><" + fieldName + "> \"" + value + "\"");
		}
	}

	/**
	 * Parse a float from the given value.
	 * @param className the class name.
	 * @param fieldName the field name.
	 * @param value the value to parse from.
	 * @return the float.
	 */
	private final float parseFloat(String className, String fieldName, String value) {
		try {
			return Float.parseFloat(value);
		} catch (RuntimeException re) {
			throw new XmlException("Config data not a float: <" + className + "><" + fieldName + "> \"" + value + "\"");
		}
	}

	/**
	 * Parse a double from the given value.
	 * @param className the class name.
	 * @param fieldName the field name.
	 * @param value the value to parse from.
	 * @return the double.
	 */
	private final double parseDouble(String className, String fieldName, String value) {
		try {
			return Double.parseDouble(value);
		} catch (RuntimeException re) {
			throw new XmlException("Config data not a double: <" + className + "><" + fieldName + "> \"" + value + "\"");
		}
	}

	/**
	 * Parse a char from the given value.
	 * @param className the class name.
	 * @param fieldName the field name.
	 * @param value the value to parse from.
	 * @return the char.
	 */
	private final char parseChar(String className, String fieldName, String value) {
		if (value == null || value.length() != 1) {
			throw new XmlException("Config data not a char: <" + className + "><" + fieldName + "> \"" + value + "\"");
		}
		return value.charAt(0);
	}

	/**
	 * Parse a char from the given value.
	 * @param className the class name.
	 * @param fieldName the field name.
	 * @param value the value to parse from.
	 * @return the char.
	 */
	private final Milliseconds parseMilliseconds(String className, String fieldName, String value) {
		return new Milliseconds(parseLong(className, fieldName, value));
	}

	/**
	 * Parse seconds from the given value.
	 * @param className the class name.
	 * @param fieldName the field name.
	 * @param value the value to parse from.
	 * @return the seconds.
	 */
	private final Seconds parseSeconds(String className, String fieldName, String value) {
		return new Seconds(parseLong(className, fieldName, value));
	}

	/**
	 * Parse minutes from the given value.
	 * @param className the class name.
	 * @param fieldName the field name.
	 * @param value the value to parse from.
	 * @return the minutes.
	 */
	private final Minutes parseMinutes(String className, String fieldName, String value) {
		return new Minutes(parseLong(className, fieldName, value));
	}

	/**
	 * Parse hours from the given value.
	 * @param className the class name.
	 * @param fieldName the field name.
	 * @param value the value to parse from.
	 * @return the hours.
	 */
	private final Hours parseHours(String className, String fieldName, String value) {
		return new Hours(parseLong(className, fieldName, value));
	}

	/**
	 * Parse days from the given value.
	 * @param className the class name.
	 * @param fieldName the field name.
	 * @param value the value to parse from.
	 * @return the days.
	 */
	private final Days parseDays(String className, String fieldName, String value) {
		return new Days(parseLong(className, fieldName, value));
	}

	/**
	 * Parse a percent from the given value.
	 * @param className the class name.
	 * @param fieldName the field name.
	 * @param value the value to parse from.
	 * @return the percent.
	 */
	private final Percent parsePercent(String className, String fieldName, String value) {
		return new Percent(parseInt(className, fieldName, value));
	}

	/**
	 * Parse a megabytes from the given value.
	 * @param className the class name.
	 * @param fieldName the field name.
	 * @param value the value to parse from.
	 * @return the megabytes.
	 */
	private final Bytes parseBytes(String className, String fieldName, String value) {
		return new Bytes(parseLong(className, fieldName, value));
	}

	/**
	 * Parse a megabytes from the given value.
	 * @param className the class name.
	 * @param fieldName the field name.
	 * @param value the value to parse from.
	 * @return the megabytes.
	 */
	private final Kilobytes parseKilobytes(String className, String fieldName, String value) {
		return new Kilobytes(parseLong(className, fieldName, value));
	}

	/**
	 * Parse a megabytes from the given value.
	 * @param className the class name.
	 * @param fieldName the field name.
	 * @param value the value to parse from.
	 * @return the megabytes.
	 */
	private final Megabytes parseMegabytes(String className, String fieldName, String value) {
		return new Megabytes(parseLong(className, fieldName, value));
	}

	/**
	 * Parse a megabytes from the given value.
	 * @param className the class name.
	 * @param fieldName the field name.
	 * @param value the value to parse from.
	 * @return the megabytes.
	 */
	private final Gigabytes parseGigabytes(String className, String fieldName, String value) {
		return new Gigabytes(parseLong(className, fieldName, value));
	}

	/**
	 * Parse a date from the given value.
	 * @param className the class name.
	 * @param fieldName the field name.
	 * @param value the value to parse from.
	 * @return the date.
	 */
	private final UtilDate parseUtilDate(String className, String fieldName, String value) throws ParseException {
		return new UtilDate(value);
	}

	/**
	 * Parse a class from the given value.
	 * @param className the class name.
	 * @param fieldName the field name.
	 * @param value the value to parse from.
	 * @return the class.
	 */
	private final Class parseClass(String className, String fieldName, String value) {
		try {
			Class theClass = Class.forName(value);
			theClass.newInstance();
			return theClass;
		} catch (Exception re) {
			throw new XmlException("Config data not a double: <" + className + "><" + fieldName + "> \"" + value + "\"");
		}
	}

}
