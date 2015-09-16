package core.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import core.io.file.BinaryFile;

public class Zip {

	protected ZipEntry newEntry(String name) {
		return new ZipEntry(name);
	}

	protected ZipOutputStream newOutputStream(OutputStream output) throws IOException {
		return new ZipOutputStream(output);
	}

	public byte[] compress(BinaryFile directory, boolean recursive, String regex) throws IOException {
		if (directory == null) {
			throw new NullPointerException();
		}
		String directoryName = directory.toString().replace('\\', '/');
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		ZipOutputStream output = newOutputStream(bytes);
		try {
			BinaryFile[] files = (BinaryFile[]) directory.listFiles(true, regex);
			for (int i = 0; i < files.length; i++) {
				String filename = files[i].toString().replace('\\', '/');

				byte[] data = files[i].readToByteArray();
				ZipEntry entry = newEntry(filename);
				output.putNextEntry(entry);
				output.write(data);
				output.closeEntry();
			}
		} finally {
			output.close();
		}
		return bytes.toByteArray();
	}

}
