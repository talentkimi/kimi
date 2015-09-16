package core.mime;

import java.io.IOException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.net.NetSocket;
import core.text.Charsets;

/**
 * An SMTP (Simple Message Transport Protocol) client.
 */
public final class SmtpClient {

	private static final Logger log = LoggerFactory.getLogger(SmtpClient.class);


	/** Debug enabled. */
	private boolean debug = false;

	/** The host. */
	private final String host;
	/** The port. */
	private final int port;
	/** The timeout. */
	private int timeout = 60000;
	/** The charset. */
	private String charset = Charsets.UTF_8;
	/** The username. */
	private String username = null;
	/** The username. */
	private String password = null;
	/** Use SSL. */
	private boolean useSSL = false;

	/**
	 * Set the login.
	 * @param username the username.
	 * @param password the password.
	 */
	public void setLogin(String username, String password) {
		if (username == null || password == null) {
			throw new NullPointerException();
		}
		this.username = username;
		this.password = password;
	}

	/**
	 * Creates a new email sender.
	 * @param host the host.
	 */
	public SmtpClient(String host, int port) {
		this(host, port, false);
	}

	/**
	 * Creates a new email sender.
	 * @param host the host.
	 */
	public SmtpClient(String host, int port, boolean useSSL) {
		if (host == null) {
			throw new NullPointerException();
		}
		if (port < 1) {
			throw new IllegalArgumentException("port=" + port);
		}
		this.host = host;
		this.port = port;
		this.useSSL = useSSL;
	}

	/**
	 * Send the given message.
	 * @param message the message.
	 */
	public final void send(MimeMessage message) throws IOException {
		if (message == null) {
			throw new NullPointerException();
		}

		// Connect to mail server
		NetSocket socket = new NetSocket(host, port, useSSL, timeout);
		try {
			socket.getWriter().setCharset(Charsets.US_ASCII);

			Base64Encoder encoder = new Base64Encoder();

			// Send
			readln(socket, "220"); // Hello
			if (username != null && password != null) {
				writeln(socket, "EHLO travelfusion.com", true);
				writeln(socket, "AUTH LOGIN", true);
				readln(socket, "250", "334");
				writeln(socket, encoder.encode(username), true);
				readln(socket, "334");
				writeln(socket, encoder.encode(password), true);
				readln(socket, "235");
			}
			writeln(socket, "MAIL FROM: <" + message.getSender() + ">", true); // Sender
			readln(socket, "250"); // Ok
			for (int i = 0; i < message.recipients(); i++) {
				writeln(socket, "RCPT TO: <" + message.getRecipient(i) + ">", true); // Recipient
				readln(socket, "250"); // Ok
			}
			for (int i = 0; i < message.ccRecipients(); i++) {
				writeln(socket, "RCPT TO: <" + message.getCcRecipient(i) + ">", true); // Recipient
				readln(socket, "250"); // Ok
			}
			for (int i = 0; i < message.bccRecipients(); i++) {
				writeln(socket, "RCPT TO: <" + message.getBccRecipient(i) + ">", true); // Recipient
				readln(socket, "250"); // Ok
			}
			writeln(socket, "DATA", true); // DATA begins
			readln(socket, "354"); // Ready for data

			// Data
			String data = message.toString();
			writeln(socket, data);

			// Finished
			writeln(socket, ".", true); // DATA ends
			readln(socket, "250");
			writeln(socket, "QUIT", true); // Close connection
		} finally {
			socket.close();
		}
	}

	/**
	 * Write the given line to the socket without flushing.
	 * @param socket the socket.
	 * @param line the line.
	 */
	final void writeln(NetSocket socket, String line) throws IOException {
		writeln(socket, line, false);
	}

	/**
	 * Write the given line to the socket.
	 * @param socket the socket.
	 * @param line the line.
	 * @param flush true to flush.
	 */
	final void writeln(NetSocket socket, String line, boolean flush) throws IOException {
		if (debug) {
			if (log.isDebugEnabled()) log.debug (String.valueOf(line));
		}
		socket.getWriter().write(line);
		socket.getWriter().write("\r\n");
		if (flush) {
			socket.getWriter().flush();
		}
	}

	/**
	 * Read a line from the given socket.
	 * @param socket the socket.
	 * @param code the expected code.
	 * @return the line.
	 * @throws IOException if an unexpected code was returned.
	 */
	final String readln(NetSocket socket, String code) throws IOException {
		String line = socket.getReader().readLine();
		if (!line.startsWith(code)) {
			throw new IOException(line);
		}
		if (debug) {
			if (log.isDebugEnabled()) log.debug (String.valueOf(line));
		}
		return line;
	}

	/**
	 * Read a line from the given socket.
	 * @param socket the socket.
	 * @param code1 the expected code (any number of times).
	 * @param code2 the expected end code.
	 * @return the line.
	 * @throws IOException if an unexpected code was returned.
	 */
	final String readln(NetSocket socket, String code1, String code2) throws IOException {
		String line = socket.getReader().readLine();
		while (line.startsWith(code1)) {
			line = socket.getReader().readLine();
		}
		if (!line.startsWith(code2)) {
			throw new IOException(line);
		}
		if (debug) {
			if (log.isDebugEnabled()) log.debug (String.valueOf(line));
		}
		return line;
	}

	public static void main(String[] args) {
		SmtpClient client = new SmtpClient("smtp.googlemail.com", 465);
		client.setLogin("c5@travelfusion.com", "v0owx1ruuolm");
		try {
			MimeMessage message = new MimeMessage("server@travelfusion.com", "craig@chatspike.net", "Test", "<b>You are screwed matt!</b>", MimeTypes.TEXT_HTML);
			if (log.isDebugEnabled()) log.debug ("Sending");
			client.send(message);
			if (log.isDebugEnabled()) log.debug ("Sent!");
		} catch (Exception e) {
			if (log.isDebugEnabled()) log.debug ("[Exception]", e);
		}
	}

}
