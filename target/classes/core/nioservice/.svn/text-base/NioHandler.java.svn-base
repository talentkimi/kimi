package core.nioservice;

public interface NioHandler extends Runnable {

	public void processData(byte[] data, int count);
	public void send();
	public void setException(Exception e);
	public Exception getException();
	public void connectionEstablished();
	public void writeFinished();
	public void terminate();
}
