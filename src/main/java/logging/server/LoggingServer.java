package logging.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import logging.StreamUtils;
import logging.TpLoggerManager;
import logging.config.LoggingConfig;
import logging.filelog.LogDatabase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class LoggingServer implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(LoggingServer.class); 
	private static final ExecutorService executor = Executors.newCachedThreadPool();
	private static final CmdResponseOk RESPONSE_OK = new CmdResponseOk();
	private static volatile boolean shutdown = false; 
	private final Socket socket;
	private static boolean init;
	private static String config = "java/config/";
	
	private static void init() throws Throwable {
		if (init) {
			return;
		}
//		TripPlannerMain.startupConfig(config);
//		TripPlannerMain.startupThreadPools();
//		TripPlannerMain.startupDatabase();
//		TripPlannerMain.startupSystemVariableManager();
//		TripPlannerMain.startupLogging();
		init = true;
	}

	public static void main(String[] args) {
		try {
			 
			final Iterator<String> iargs = Arrays.asList(args).iterator();
			
			while (iargs.hasNext()) {
				final String cmd = iargs.next();
				if (cmd.equals("-c")) {
					config = iargs.next();
				} else if (cmd.equals("start")) {
					init();
					start(iargs);
					break;
				} else if (cmd.equals("stop")) {
					init();
					stop(iargs);
					break;
				}
			}
			
		} catch (Throwable ex) {
            logger.error("LoggingServer exception", ex);
//    		TripPlanner.getTripPlanner().getSecurityManager().allowExit(Thread.currentThread());
            System.exit(1);
		}
//		TripPlanner.getTripPlanner().getSecurityManager().allowExit(Thread.currentThread());
        System.exit(0);
	}
	
	private static void start(Iterator<String> iargs) throws IOException {
		ServerSocket serverSocket = null;
		try {
			final int port = LoggingConfig.getServerPort();
			serverSocket = new ServerSocket(port);
	        serverSocket.setSoTimeout(1000);
//	        TripPlannerMain.BOOT_LOG.info("Logging Server is started up");
	        while (!shutdown) {
	        	// Waiting for connection
		    	try {
			    	final Socket socket = serverSocket.accept();
			    	final LoggingServer server = new LoggingServer(socket);
			    	executor.execute(server);
		    	} catch (SocketTimeoutException ex) {
		    		// just ignore. TimeOutException is thrown for shutdown checking
		    	}
	        }
		} finally {
		    executor.shutdown();
		    try {
		    	executor.awaitTermination(10, TimeUnit.SECONDS);
		    } catch (InterruptedException ex) {
		    	// ignore
		    }
		    if (serverSocket != null) {
		    	serverSocket.close();
		    }
		    try {
		    	TpLoggerManager.shutdown();
		    } catch (InterruptedException ex) {
		    	// ignore
		    }
		    LogDatabase.shutdown();
		}
	}
	
	private static void stop(Iterator<String> iargs) throws IOException {
		final String password = LoggingConfig.getShutdownPassword();
		final CmdRequestShutdown cmd = new CmdRequestShutdown(LoggingConfig.getServerHost(), LoggingConfig.getServerPort());
		cmd.setShutdownPassword(password);
		final CmdResponse resp = cmd.send();
		if (resp instanceof CmdResponseFailure) {
			System.err.println(resp);
		}
	}

	public static void shutdown(String password) {
		if (!LoggingConfig.getShutdownPassword().equals(password)) {
			throw new IllegalArgumentException("Shutdown password doesn't match");
		}
		
		shutdown = true;
	}

	private LoggingServer(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			final CmdRequest request = (CmdRequest) StreamUtils.readObject(socket);
			
			CmdResponse response = null;

			try {
				response = request.process();
				if (response == null) {
					response = RESPONSE_OK;
				}
			} catch (Throwable t) {
				response = new CmdResponseFailure(t);
			}

			StreamUtils.writeObject(socket, response);
			
		} catch (Throwable ex) {
			logger.warn("Server exception", ex);
		} finally {
			try {
				socket.close();
			} catch (IOException ex) {
				logger.warn("Closing connection exception", ex);
			}
		}
	}
}
