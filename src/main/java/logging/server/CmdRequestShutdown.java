package logging.server;

public class CmdRequestShutdown extends CmdRequest {
	private static final long serialVersionUID = 1L;

	private String shutdownPassword;
	
	public CmdRequestShutdown(String host, int port) {
		super(host, port);
	}

	public String getShutdownPassword() {
		return shutdownPassword;
	}

	public void setShutdownPassword(String shutdownPassword) {
		this.shutdownPassword = shutdownPassword;
	}

	@Override
	public CmdResponse process() {
		LoggingServer.shutdown(shutdownPassword);
		return null;
	}

}
