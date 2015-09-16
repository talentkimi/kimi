package core.mime;

import java.util.ArrayList;
import java.util.regex.Pattern;

import core.text.Charsets;
import core.util.FieldList;

/**
 * A MIME Message.
 */
public final class MimeMessage {

	/** The address pattern. */
	private static final Pattern ADDRESS_PATTERN = Pattern.compile(".+@.+");
	/** The email newbase64. */
	private static final String NEW_LINE = "\r\n";
	/** The multi-part boundary. */
	private static final String BOUNDARY = "Multi-Part-Separator-String";
	/** The character set. */
	private static final String CHARSET = Charsets.US_ASCII;
	/** The MIME encoder. */
	private static final Base64Encoder ENCODER = new Base64Encoder();

	/** The sender. */
	private final String sender;
	/** The TO recipients. */
	private final ArrayList<String> toRecipients = new ArrayList<String>();
	/** The CC recipients. */
	private final ArrayList<String> ccRecipients = new ArrayList<String>();
	/** The BCC recipients. */
	private final ArrayList<String> bccRecipients = new ArrayList<String>();
	/** The attachments. */
	private final ArrayList<MimeAttachment> attachments = new ArrayList<MimeAttachment>();
	/** The subject. */
	private final String subject;
	/** The body. */
	private final String body;
	/** The content type. */
	private final String contentType;
	/** The content charset */
	private String contentCharset = CHARSET;

	/**
	 * set content charset
	 * @param charset
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public final void setContentCharset(String charset) throws NullPointerException, IllegalArgumentException {
		if (charset == null) {
			throw new NullPointerException();
		}
		boolean found = false;
		FieldList fieldList = new FieldList(Charsets.class);
		for (int x = 0; x < fieldList.size(); x++) {
			Object c = fieldList.getValue(x);
			if ((c instanceof String) && charset.equals((String) c)) {
				found = true;
				break;
			}
		}
		if (!found) {
			throw new IllegalArgumentException("Unrecognized content charset \"" + charset + "\"");
		}
		contentCharset = new String(charset);
	}

	/**
	 * get content charset
	 * @return the content charset
	 */
	public final String getContentCharset() {
		return contentCharset;
	}

	/**
	 * Returns the sender.
	 * @return the sender.
	 */
	public final String getSender() {
		return sender;
	}

	/**
	 * get content type
	 * @return content type
	 */
	public final String getContentType() {
		return contentType;
	}

	/**
	 * Returns the number of recipients.
	 * @return the number of recipients.
	 */
	public final int recipients() {
		return toRecipients.size();
	}

	/**
	 * Returns the number of recipients.
	 * @return the number of recipients.
	 */
	public final int ccRecipients() {
		return ccRecipients.size();
	}

	/**
	 * Returns the number of recipients.
	 * @return the number of recipients.
	 */
	public final int bccRecipients() {
		return bccRecipients.size();
	}

	/**
	 * Returns the recipient.
	 * @return the recipient.
	 */
	public final String getRecipient(int index) {
		return toRecipients.get(index);
	}

	/**
	 * Returns the recipient.
	 * @return the recipient.
	 */
	public final String getCcRecipient(int index) {
		return ccRecipients.get(index);
	}

	/**
	 * Returns the recipient.
	 * @return the recipient.
	 */
	public final String getBccRecipient(int index) {
		return bccRecipients.get(index);
	}

	/**
	 * Returns the recipient.
	 * @return the recipient.
	 */
	public final String getRecipients() {
		return getRecipients(toRecipients);
	}

	/**
	 * Returns the recipient.
	 * @return the recipient.
	 */
	public final String getCcRecipients() {
		return getRecipients(ccRecipients);
	}

	/**
	 * Returns the recipient.
	 * @return the recipient.
	 */
	public final String getBccRecipients() {
		return getRecipients(bccRecipients);
	}

	/**
	 * Returns the recipient.
	 * @return the recipient.
	 */
	private static final String getRecipients(ArrayList<String> list) {
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			if (i > 0) {
				buffer.append(',').append(' ');
			}
			buffer.append(list.get(i));
		}
		return buffer.toString();
	}

	/**
	 * Adds a recipient.
	 * @param recipients the recipient to add.
	 */
	public static final void addRecipients(ArrayList<String> list, String recipients) throws InvalidAddressException {
		if (recipients == null) {
			throw new NullPointerException();
		}
		int index = 0;
		for (int i = 0; i < recipients.length(); i++) {
			char c = recipients.charAt(i);
			if (c == ',' || c == ';') {
				String recipient = recipients.substring(index, i).trim();
				if (recipient.length() > 0) {
					addRecipient(list, recipient);
				}
				index = i + 1;
			}
		}
		if (index < recipients.length()) {
			String recipient = recipients.substring(index).trim();
			if (recipient.length() > 0) {
				addRecipient(list, recipient);
			}
		}
	}

	/**
	 * Adds a recipient.
	 * @param recipient the recipient to add.
	 */
	public static final void addRecipients(ArrayList<String> list, String[] recipients) throws InvalidAddressException {
		if (recipients == null) {
			throw new NullPointerException();
		}
		if (recipients.length == 0) {
			throw new IllegalArgumentException("recipients is empty");
		}
		for (int i = 0; i < recipients.length; i++) {
			String recipient = recipients[i];
			addRecipient(list, recipient);
		}
	}

	private static final void addRecipient(ArrayList<String> list, String recipient) throws InvalidAddressException {
		if (recipient == null) {
			throw new NullPointerException();
		}
		if (!isValidAddress(recipient)) {
			throw new InvalidAddressException(recipient);
		}
		list.add(recipient);
	}

	public final void addRecipients(String[] recipients) throws InvalidAddressException {
		addRecipients(toRecipients, recipients);
	}

	public final void addRecipients(String recipients) throws InvalidAddressException {
		addRecipients(toRecipients, recipients);
	}

	public final void addBccRecipients(String[] recipients) throws InvalidAddressException {
		addRecipients(bccRecipients, recipients);
	}

	public final void addBccRecipients(String recipients) throws InvalidAddressException {
		addRecipients(bccRecipients, recipients);
	}

	public final void addCcRecipients(String[] recipients) throws InvalidAddressException {
		addRecipients(ccRecipients, recipients);
	}

	public final void addCcRecipients(String recipients) throws InvalidAddressException {
		addRecipients(ccRecipients, recipients);
	}

	/**
	 * Returns the number of attachments.
	 * @return the number of attachments.
	 */
	public final int attachments() {
		return attachments.size();
	}

	/**
	 * Returns the attachment at the given index.
	 * @param index the index.
	 * @return the attachment.
	 */
	public final MimeAttachment getAttachment(int index) {
		return attachments.get(index);
	}

	/**
	 * Attach an attachment.
	 * @param attachment the attachment to add.
	 */
	public final void attach(MimeAttachment attachment) throws InvalidAddressException {
		if (attachment == null) {
			throw new NullPointerException();
		}
		this.attachments.add(attachment);
	}

	/**
	 * Returns the subject.
	 * @return the subject.
	 */
	public final String getSubject() {
		return subject;
	}

	/**
	 * Returns the body.
	 * @return the body.
	 */
	public final String getBody() {
		return body;
	}

	/**
	 * Returns true if the given email address is valid.
	 * @param address the address.
	 * @return true if the given email address is valid.
	 */
	public static final boolean isValidAddress(String address) {
		if (address == null) {
			throw new NullPointerException();
		}
		if (address.indexOf(',') != -1) {
			return false;
		}
		if (address.indexOf(';') != -1) {
			return false;
		}
		return ADDRESS_PATTERN.matcher(address).matches();
	}

	/**
	 * Creates a new mime message.
	 * @param sender the sender.
	 * @param recipient the recipient.
	 * @param subject the subject.
	 * @param body the body.
	 */
	public MimeMessage(String sender, String recipient, String subject, String body, String contentType, boolean isBlindCopy) throws InvalidAddressException {
		if (sender == null || recipient == null || subject == null || body == null || contentType == null) {
			throw new NullPointerException();
		}
		if (!isValidAddress(sender)) {
			throw new InvalidAddressException(sender);
		}
		if (isBlindCopy) {
			addBccRecipients(recipient);
		} else {
			addRecipients(recipient);
		}
		this.sender = sender;
		this.subject = subject;
		this.body = body;
		this.contentType = contentType;
	}

	/**
	 * Creates a new mime message.
	 * @param sender the sender.
	 * @param subject the subject.
	 * @param body the body.
	 */
	public MimeMessage(String sender, String recipient, String subject, String body, String contentType) throws InvalidAddressException {
		this(sender, recipient, subject, body, contentType, false);
	}

	/**
	 * Returns this email as a string.
	 * @return this email as a string.
	 */
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("MIME-Version: 1.0").append(NEW_LINE);
		buffer.append("Subject: ").append(getSubject()).append(NEW_LINE);
		if (recipients() == 0 && ccRecipients() == 0) {
			buffer.append("To: ").append(getSender()).append(NEW_LINE);
		} else {
			buffer.append("To: ").append(getRecipients()).append(NEW_LINE);
		}
		if (ccRecipients() > 0) {
			buffer.append("Cc: ").append(getCcRecipients()).append(NEW_LINE);
		}
		buffer.append("From: ").append(getSender()).append(NEW_LINE);
		buffer.append("Return-Path: ").append(getSender()).append(NEW_LINE);

		// Text only
		if (attachments() == 0) {
			buffer.append("Content-Type: " + contentType + "; charset=\"").append(contentCharset).append("\"").append(NEW_LINE);
			buffer.append(NEW_LINE);
			buffer.append(getBody()).append(NEW_LINE);
		}

		// Text with attachments
		else {
			buffer.append("Content-Type: multipart/mixed; BOUNDARY=\"").append(BOUNDARY).append("\"").append(NEW_LINE);
			buffer.append(NEW_LINE);

			// Body
			buffer.append("--").append(BOUNDARY).append(NEW_LINE);
			buffer.append("Content-Type: " + contentType + "; charset=\"").append(contentCharset).append("\"").append(NEW_LINE);
			buffer.append(NEW_LINE);
			buffer.append(getBody()).append(NEW_LINE);

			// Attachments
			for (int i = 0; i < attachments(); i++) {
				MimeAttachment attachment = getAttachment(i);
				buffer.append("--").append(BOUNDARY).append(NEW_LINE);
				buffer.append("Content-Type:").append(attachment.getContentType()).append("; name=\"").append(attachment.getFilename()).append("\"").append(NEW_LINE);
				buffer.append("Content-Disposition: attachment; filename=\"").append(attachment.getFilename()).append("\"").append(NEW_LINE);
				buffer.append("Content-Transfer-Encoding: base64").append(NEW_LINE);
				buffer.append(NEW_LINE);
				appendAttachmentData(buffer, attachment, 76);
			}

			// Final Boundary
			buffer.append("--").append(BOUNDARY).append("--");
		}
		return buffer.toString();
	}

	/**
	 * Appends the data of the given attachment to the string buffer.
	 * @param buffer the string buffer.
	 * @param attachment the attachment.
	 * @param maxLength the maximum length of a line.
	 */
	private void appendAttachmentData(StringBuilder buffer, MimeAttachment attachment, int maxLength) {
		byte[] base64 = ENCODER.encode(attachment.getData());
		int index = 0;
		while (index + maxLength < base64.length) {
			for (int i = index; i < index + maxLength; i++) {
				buffer.append((char) base64[i]);
			}
			buffer.append(NEW_LINE);
			index += maxLength;
		}
		for (int i = index; i < base64.length; i++) {
			buffer.append((char) base64[i]);
		}
		buffer.append(NEW_LINE);
	}
}
