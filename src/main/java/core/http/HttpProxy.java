package core.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import core.http.request.HttpRequest;
import core.http.response.HttpResponse;
import core.io.Gzip;
import core.io.StreamOutput;
import core.net.IpAddress;
import core.net.NetOutputFlusher;
import core.net.NetServerSocket;
import core.net.NetSocket;
import core.net.NetSocketHandler;
import core.net.NetSocketPipeThread;
import core.net.NetUrl;
import core.text.Text;
import core.util.Random;
import core.util.time.Milliseconds;

public class HttpProxy extends NetServerSocket {

	private static final Logger log = LoggerFactory.getLogger(HttpProxy.class);
	private static final Executor EXECUTOR = Executors.newCachedThreadPool();
	private final String localhost;

	/**
	 * Creates a new handler.
	 * @return a new handler.
	 */
	protected NetSocketHandler newHandler() {
		return new HttpProxyHandler(localhost);
	}

	/**
	 * Creates a new HttpProxy.
	 * @param port the port.
	 * @param connections the connections.
	 */
	public HttpProxy(String localhost, int port, int connections) throws IOException {
		super(port, connections);
		this.localhost = localhost;
	}

	/**
	 * Creates a new HttpProxy.
	 * @param port the port.
	 * @param connections the connections.
	 */
	public HttpProxy(int port, int connections) throws IOException {
		this(null, port, connections);
	}

	public static void main(String[] args) {
		try {
			if (args.length == 0) {
//				TripPlannerMain.startupConfig();
//				NetUrl[] proxyList = AdminAssistantConfig.getServerList().getByHostAddress(IpAddress.getLocalAddress().toString()).getProxyUrls();
				NetUrl[] proxyList = null;
				args = new String[proxyList.length];
				for (int i = 0; i < proxyList.length; i++) {
					args[i] = proxyList[i].toString();
				}
			}
			for (int i = 0; i < args.length; i++) {
				String host = null;
				int port = 0;
				int connectionThreads = 100;
				try {
					port = Integer.parseInt(args[i]);
				} catch (NumberFormatException nfe) {
					int portIndex = args[i].indexOf(':');
					port = Integer.parseInt(args[i].substring(portIndex + 1));
					host = args[i].substring(0, portIndex);
				}
				HttpProxy proxy = new HttpProxy(host, port, connectionThreads);
				new Thread(proxy).start();
				if (log.isDebugEnabled()) log.debug ("[Http Proxy] listening on port " + port);
				if (log.isDebugEnabled()) log.debug ("[Http Proxy] Using "+connectionThreads+" connection threads");
			}
			if(args.length == 0){
				if (log.isDebugEnabled()) log.debug ("Nothing to do - Exiting.");
			}
		} catch (Throwable t) {
			if (log.isDebugEnabled()) log.debug ("java core.http.HttpProxy port connections", t);
		}
	}
	
	@Override
	protected Executor getExecutor() {
		return EXECUTOR;
	}
	
	static class Flusher extends NetOutputFlusher {
		public Flusher(StreamOutput output, Milliseconds frequency) {
			super(output, frequency);
		}
		@Override
		protected Executor getExecutor() {
			return HttpProxy.EXECUTOR;
		}
	}
	
	static class PipeThread extends NetSocketPipeThread {
		public PipeThread(NetSocket from, NetSocket to) throws IOException {
			super(from, to);
		}
		@Override
		protected Executor getExecutor() {
			return HttpProxy.EXECUTOR;
		}
	}

	@Override
	public void runTask() throws Throwable {
		// TODO Auto-generated method stub
		
	}
}

class HttpProxyHandler extends NetSocketHandler {
	
	private static final Logger log = LoggerFactory.getLogger(HttpProxyHandler.class);
	//private StringBuilder buffer = new StringBuilder(); 
	private final String localhost;
	
	private SocketAddress getSocketAddress() {
		if (localhost == null) {
			return null;
		}
		while (true) {
			try {
				SocketAddress address = new InetSocketAddress(localhost, Random.getRandom().nextInt(2000, 4999));
				Socket socket = new Socket(Proxy.NO_PROXY);
				try {
					socket.bind(address);
					return address;
				} finally {
					socket.close();
				}
			} catch (IOException e) {
				if (log.isErrorEnabled()) log.error("Problem connecting to the proxy",e);
			}
		}
	}

	/**
	 * Handle the socket.
	 */
	protected void handleSocket() throws IOException {
		long start = System.currentTimeMillis();
		NetSocket serverSocket = null;
		NetSocket clientSocket = null;
		NetUrl url = null;
		int minGzipFilesize = 5000; //TODO: Need to tweak
		
		try {
			debug("[New Connection]");
			clientSocket = getSocket();

			// Client HTTP Request
			HttpRequest clientRequest = new HttpRequest();
			long clientReadStart = System.currentTimeMillis();
			clientRequest.readFrom(clientSocket.getReader(), "HttpProxy - clientRequest");
			long clientReadFinish = System.currentTimeMillis();
			debug("[Http Request] " + (clientReadFinish - clientReadStart) + " millis");
			debug(clientRequest);

			// Close proxy to only TFRequest.
			if (clientRequest.getHeaderList().getHeader("X-TFRequest") == null || !clientRequest.getHeaderList().contains("X-TFRequest")) {
				if (log.isDebugEnabled()) log.debug ("NON TFRequest Ip from: ", clientSocket.getSocket().getInetAddress().getHostAddress());
				clientSocket.getWriter().write(new HttpResponse(HttpResponse.CODE_403, "These are not the droids you're looking for.").toString(false));
				clientSocket.getWriter().flush();
				clientSocket.close();
				return;
			} else {
				clientRequest.getHeaderList().removeHeader("X-TFRequest");
			}
			boolean isEncrypted = clientRequest.getMethod().equals(HttpRequest.METHOD_CONNECT);
			boolean clientRequestGzip = false;
			// URL
			url = clientRequest.getUrl().getNetUrl();
			debug("[Url] " + url);

			if (url.isAbsolute()) url.setAbsolute(false);

			// Connect
			debug("[Connecting to " + url.getHost() + ":" + url.getPort() + "]");
			long serverConnectStart = System.currentTimeMillis();
			serverSocket = new NetSocket(getSocketAddress(), url.getHost(), url.getPort(), false, 10000);
			long serverConnectFinish = System.currentTimeMillis();
			debug("[Connected to " + url.getHost() + ":" + url.getPort() + "] " + (serverConnectFinish - serverConnectStart) + " millis");

	
			if(clientRequest.getHeaderList().contains("Connection")){
				clientRequest.getHeaderList().removeHeader("Connection");
			}
			clientRequest.getHeaderList().add(new HttpHeader("Connection","Close"));
			
			if(clientRequest.getHeaderList().contains("Accept-Encoding") && clientRequest.getHeaderList().getHeader("Accept-Encoding").getValue().contains("gzip")){
				clientRequestGzip = true;
			}

			if (isEncrypted) {
				// Secure HTTP
				HttpResponse serverResponse = new HttpResponse(HttpResponse.CODE_200, "Connection Established");
				serverResponse.addHeader("Proxy-agent", "Apache/2.0.40 (Red Hat Linux)");
				serverResponse.writeTo(clientSocket.getWriter());
				startTunnel(clientSocket, serverSocket, url);
			} else {
				//	Standard HTTP
				long clientWriteStart = System.currentTimeMillis();
				sendRequest(clientRequest, serverSocket, url);
				long clientWriteFinish = System.currentTimeMillis();
				
				HttpResponse response = new HttpResponse();
				response.readFrom(serverSocket.getReader(),false, "HttpProxy - response");
				
				HttpHeaderList headers = response.getHeaderList();
	
				debug("[Http Response] "+ (clientWriteFinish-clientWriteStart)+ " milis");
				debug(response);
	
				boolean supportsGzip = (headers.contains("Content-Encoding") && headers.getHeader("Content-Encoding").getValue().contains("gzip")); 
				boolean supportedType = headers.contains("Content-Type") && supportedType(headers.getHeader("Content-Type").getValue());
				boolean httpStatus = response.getCode().equals(HttpResponse.CODE_200); // No point in compressing redirects, errors, etc
				boolean bigEnough = true;
				
				if(headers.contains("Content-Length") && (Integer.parseInt(headers.getHeader("Content-Length").getValue()) < minGzipFilesize)){
					bigEnough =  false;
				}
				
				boolean enableCompression = !isEncrypted && !supportsGzip && supportedType && bigEnough && httpStatus && clientRequestGzip; 
				
				debug("[Gzip] Client requested compression: "+clientRequestGzip);
				debug("[Gzip] Encrypted Connection: "+isEncrypted);
				debug("[Gzip] Server Supports Gzip: "+supportsGzip);
				debug("[Gzip] Is a supported filetype: "+supportedType);
				debug("[Gzip] File bigger then " + minGzipFilesize + " bytes: "+bigEnough);
				debug("[Gzip] enableCompression: "+enableCompression);
				
				if(enableCompression) {
					debug("[Gzip] Doesn't support gzip, compress in memory.");
					response.readContent(serverSocket.getReader());
					
					long beforeSize = response.getContent().length();
					long startCompression = System.currentTimeMillis();
					byte[] compressedContent = new Gzip().compress(response.getContent().toByteArray()); 
					long endCompression = System.currentTimeMillis();
					long afterSize = compressedContent.length;
					debug("[Gzip Finished] "+(endCompression-startCompression)+" milis");
					response.setContent(compressedContent,headers.getHeader("Content-Type").getValue());
					response.setHeader("Content-Encoding", "gzip");
					response.setHeader("Content-Length", afterSize);
					if(response.getHeaderList().contains("Transfer-Encoding")) response.getHeaderList().removeHeader("Transfer-Encoding");
					
					debug("[Gzip] Size Before gzip: " + beforeSize+" Bytes");
					debug("[Gzip] Size After gzip: " + afterSize + " Bytes");
					response.setHeader("Connection", "close");
					response.addHeader("X-GzipBeforeSize", beforeSize);
					response.addHeader("X-GzipAfterSize", afterSize);
					response.addHeader("X-GzipTimeMillis", (endCompression-startCompression));
					
					response.writeTo(clientSocket.getWriter());
					clientSocket.getWriter().flush();
				} else {
					if(supportsGzip) { 
						debug("[Gzip] Supports gzip, piping data.");
					} else {
						debug("[Gzip] Doesn't support gzip but skipping compression.");
					}
					clientSocket.getWriter().write(response.toString()); // Write headers to client stream
					startTunnel(clientSocket, serverSocket, url);
				}
			}
		} catch (Throwable t) {
			debug("[Unknown Error]");
			debug(Text.getStackTrace(t));
			
		} finally {
			if (serverSocket != null) serverSocket.close();
			if(clientSocket != null ) clientSocket .close();
			if(url != null ) url = null;
		
			long finish = System.currentTimeMillis();
			debug("[Connection closed] " + (finish - start) + " millis");
		}
	}

	HttpProxyHandler(String localhost) {
		this.localhost = localhost;
	}
	
	/**
	 *  Checks to see if the content type is compressable.
	 * @param contentType 
	 * @return
	 */
	private boolean supportedType(String contentType){
		if(contentType.startsWith("text/")) return true;
		if(contentType.equalsIgnoreCase("application/x-javascript")) return true;
		
		return false;
	}
	protected void sendRequest(HttpRequest request, NetSocket target, NetUrl url) throws IOException {
		debug("[Sending HTTP to " + url.getHost() + ":" + url.getPort() + "]");
		long serverWriteStart = System.currentTimeMillis();
		request.writeTo(target.getWriter());
		long serverWriteFinish = System.currentTimeMillis();
		debug("[Sent HTTP to " + url.getHost() + ":" + url.getPort() + "] " + (serverWriteFinish - serverWriteStart) + " millis");
	}
	
	public final void debug(Object text) {
		if (log.isDebugEnabled()) log.debug ("[Connection "+getConnectionNumber()+"]" + String.valueOf(text));
	}
	
	protected void startTunnel(NetSocket clientSocket, NetSocket serverSocket, NetUrl url) throws IOException,InterruptedException {
		debug("[Started Tunnel to " + url.getHost() + ":" + url.getPort() + "]");
		long tunnelStart = System.currentTimeMillis();
		HttpProxy.PipeThread tunnel1 = new HttpProxy.PipeThread(clientSocket, serverSocket);
		HttpProxy.PipeThread tunnel2 = new HttpProxy.PipeThread(serverSocket, clientSocket);
		
		HttpProxy.Flusher flusher1 = new HttpProxy.Flusher(clientSocket.getWriter(), new Milliseconds(200));
		HttpProxy.Flusher flusher2 = new HttpProxy.Flusher(serverSocket.getWriter(), new Milliseconds(200));
		tunnel1.start();
		tunnel2.start();
		flusher1.start();
		flusher2.start();
		while (!tunnel1.hasFinished() && !tunnel2.hasFinished()) {
			Thread.sleep(200);
		}
		if (tunnel1.hasCrashed()) {
			debug(tunnel1.getThrowable());
		}
		if (tunnel2.hasCrashed()) {
			debug(tunnel2.getThrowable());
		}
		long tunnelFinish = System.currentTimeMillis();
		debug("Proxy -> Client: " + tunnel1.getBytes()+" bytes");
		debug("Server -> Proxy: " + tunnel2.getBytes()+" bytes");
		debug("[Finished Tunnel to " + url.getHost() + ":" + url.getPort() + "] " + (tunnelFinish-tunnelStart) + " millis");
	}
}