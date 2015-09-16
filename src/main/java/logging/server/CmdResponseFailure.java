package logging.server;

public class CmdResponseFailure extends AbstractCmdResponse {
	private static final long serialVersionUID = 1L;
	private Throwable exception;
	private String message;
	
	public CmdResponseFailure() {
	}

	public CmdResponseFailure(String message) {
		this.message = message;
	}

	public CmdResponseFailure(Throwable exception) {
		this.exception = exception;
	}
	
	public CmdResponseFailure(String message, Throwable exception) {
		this.message = message;
		this.exception = exception;
	}

	public Throwable getException() {
		return exception;
	}
	
	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "Failure" + super.toString() + " [exception=" + exception + ", message=" + message + "]";
	}
}
