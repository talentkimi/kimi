package core.nioservice;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class HandlerFutureTask extends FutureTask<NioHandler> {

	private final NioHandler handler;

	public HandlerFutureTask(NioHandler handler) {
		super(handler, null);
		this.handler = handler;
	}

	protected void done() {
		try {
			// Get result returned by call(), or cause deferred
			// exception to be thrown. The result is in the handler
			// instance stored above, so we ignore it
			get();

			// TODO: May extend NioHandler to add methods for handling
			// these exceptions. This method is still running in the
			// worker thread
		} catch (ExecutionException e) {
			//handler.terminate();
			// log
		} catch (InterruptedException e) {
			Thread.interrupted();
			// log handler interrupted
		}
	}
}
