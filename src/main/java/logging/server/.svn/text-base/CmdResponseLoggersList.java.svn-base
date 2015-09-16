package logging.server;

import java.util.LinkedList;
import java.util.List;

/**
 * Response from the logging server with loggers list.
 * @author Dimitrijs
 */
public class CmdResponseLoggersList extends CmdResponseOk {
	private static final long serialVersionUID = 1L;

	private final List<String> result;

	public CmdResponseLoggersList() {
		result = new LinkedList<String>();
	}
	
	public CmdResponseLoggersList(List<String> result) {
		this.result = result;
	}
	
	public List<String> getResult() {
		return result;
	}
}
