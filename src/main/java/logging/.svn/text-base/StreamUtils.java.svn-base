package logging;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import core.util.ClosableHandler;

/**
 * Some utils to manipulate serialization or extarnalization.
 * @author Dimitrijs
 *
 */
public class StreamUtils {
	
	
	public static byte[] convertToArray(Object obj, boolean compress) throws IOException {
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		ObjectOutputStream objOut = null;
		ZipOutputStream zipOut = null;
		try{
			if (compress) {
				zipOut = new ZipOutputStream(buf);
				zipOut.putNextEntry(new ZipEntry("x"));
				objOut = new ObjectOutputStream(zipOut);
			} else {
				objOut = new ObjectOutputStream(buf);
			}
			objOut.writeObject(obj);
			objOut.flush();
			objOut.close();
			return buf.toByteArray();
		} finally {
			ClosableHandler.closeSafely(objOut);
			ClosableHandler.closeSafely(zipOut);
		}
		
	}
	
	public static Object convertFromArray(byte[] buf, boolean compressed) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bufIn = new ByteArrayInputStream(buf);
		ObjectInputStream objIn = null;
		ZipInputStream zipIn = null; 
		try{
			if (compressed) {
				zipIn = new ZipInputStream(bufIn);
				zipIn.getNextEntry();
				objIn = new ObjectInputStream(zipIn);
			} else {
				objIn = new ObjectInputStream(bufIn);
			}
			return objIn.readObject();
		} finally {
			ClosableHandler.closeSafely(objIn);
			ClosableHandler.closeSafely(zipIn);
		}
		
	}
	
	public static int writeObject(OutputStream out, Object obj) throws IOException {
		return writeObject(out, obj, true);
	}
	
	public static int writeObject(OutputStream out, Object obj, boolean compress) throws IOException {
		final byte[] buf = convertToArray(obj, compress);
		final int size = buf.length;
		
		out.write(size);
		out.write(size >> 8);
		out.write(size >> 16);
		out.write(size >> 24);
		out.write((byte) (compress ? 1 : 0));
		out.write(buf);
		
		return size + 5;
	}
	
	public static Object readObject(InputStream in) throws IOException, ClassNotFoundException {
		final int b1 = in.read();
		final int b2 = in.read();
		final int b3 = in.read();
		final int b4 = in.read();
		final int size = (b1 & 0xFF) | ((b2 & 0xFF) << 8)  | ((b3 & 0xFF) << 16) | ((b4 & 0xFF) << 24);
		final byte[] buf = new byte[size];
		
		final int compressed = in.read();
		
		if (compressed != 0 && compressed != 1) {
			throw new IOException("Illegal compression flag");
		}
		
		int ofs = 0;
		while (ofs != buf.length) {
			ofs += in.read(buf, ofs, buf.length-ofs);
		}
		
		return convertFromArray(buf, compressed == 1);
	}

	public static int writeObject(RandomAccessFile file, Object obj, boolean compress) throws IOException {
		final byte[] buf = convertToArray(obj, compress);
		file.writeInt(buf.length);
		file.writeBoolean(compress);
		file.write(buf);
		return buf.length + 5;
	}
	
	public static Object readObject(RandomAccessFile file) throws IOException, ClassNotFoundException {
		final int size = file.readInt();
		final boolean compressed = file.readBoolean();
		final byte[] buf = new byte[size];
		file.readFully(buf);
		return convertFromArray(buf, compressed);
	}
	
	public static int writeObject(Socket socket, Object obj) throws IOException {
		final OutputStream out = socket.getOutputStream();
		final int result = writeObject(out, obj, true);
		out.flush();
		return result;
	}
	
	public static Object readObject(Socket socket) throws IOException, ClassNotFoundException {
		final InputStream in = socket.getInputStream();
		return readObject(in);
	}
	
	public static void writeString(ObjectOutput out, String str) throws IOException {
		if (str == null)  {
			out.writeInt(-1);
		} else {
			final byte[] b = str.getBytes("UTF-8");
			out.writeInt(b.length);
			out.write(b);
		}
	}

	public static String readString(ObjectInput in) throws IOException {
		final int len = in.readInt();
		if (len == -1) {
			return null;
		} else {
			final byte[] b = new byte[len];
			in.readFully(b);
			return new String(b, "UTF-8");
		}
	}

}
