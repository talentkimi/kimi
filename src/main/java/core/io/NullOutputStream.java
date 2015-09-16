package core.io;

import java.io.IOException;
import java.io.OutputStream;

public class NullOutputStream extends OutputStream {

	public static final NullOutputStream INSTANCE = new NullOutputStream();

	public void write(byte[] b, int off, int len) {
	}

	public void write(int b) {
	}

	public void write(byte[] b) throws IOException {
	}
	
	public void flush() throws IOException {
	}
	
	public void close() throws IOException {
	}
}
