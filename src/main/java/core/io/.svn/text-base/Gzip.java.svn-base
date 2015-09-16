package core.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * GZIP Compression.
 */
public class Gzip extends Compressor {

	/**
	 * Returns a new output stream.
	 * @param output the output.
	 * @return a new output stream.
	 */
	protected DeflaterOutputStream newOutputStream(OutputStream output) throws IOException {
		return new GZIPOutputStream(output);
	}

	/**
	 * Returns a new input stream.
	 * @param input the input.
	 * @return a new input stream.
	 */
	protected InflaterInputStream newInputStream(InputStream input) throws IOException {
		return new GZIPInputStream(input);
	}
}