package core.mime;

import java.io.IOException;

import core.io.file.BinaryFile;

/**
 * A MIME Attachment
 */
public final class MimeAttachment {

	/** The filename. */
	private final String filename;
	/** The content type. */
	private final String contentType;
	/** The data. */
	private final byte[] data;

	/**
	 * Returns the filename.
	 * @return the filename.
	 */
	public final String getFilename() {
		return filename;
	}

	/**
	 * Returns the content type.
	 * @return the content type.
	 */
	public final String getContentType() {
		return contentType;
	}

	/**
	 * Returns the data.
	 * @return the data.
	 */
	public final byte[] getData() {
		return data;
	}

	/**
	 * Creates a new Attachment.
	 * @param filename the filename.
	 * @param contentType the content type.
	 * @param data the data.
	 */
	public MimeAttachment(String filename, String contentType, byte[] data) {
		this.filename = filename;
		this.contentType = contentType;
		this.data = data;
	}

	/**
	 * Creates a new Attachment.
	 * @param filename the filename.
	 * @param contentType the content type.
	 * @param data the data.
	 */
	public MimeAttachment(String filename, String contentType, String data) {
		this(filename, contentType, data.getBytes());
	}

	/**
	 * Creates a new Attachment.
	 * @param file the file.
	 * @param contentType the content type.
	 */
	public MimeAttachment(BinaryFile file, String contentType) throws IOException {
		this(file.getName(), contentType, file.readToByteArray());
	}

	/**
	 * Creates a new Attachment.
	 * @param filename the filename.
	 * @param contentType the content type.
	 */
	public MimeAttachment(String filename, String contentType) throws IOException {
		this(new BinaryFile(filename), contentType);
	}

}