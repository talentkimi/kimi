package logging.server;

public abstract class AbstractCmdResponse implements CmdResponse {
	private static final long serialVersionUID = 1L;

	private long processingTime;
	
	@Override
	public void setProcessingTime(long nano) {
		this.processingTime = nano;
	}

	public long getProcessingTime() {
		return processingTime;
	}
	
	public String toString() {
		return String.format("[%.2fms]", (processingTime / 1000000.0));
	}
}
