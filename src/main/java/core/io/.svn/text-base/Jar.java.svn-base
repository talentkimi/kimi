package core.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Jar extends Zip {

	protected ZipEntry newEntry(String name) {
		return new JarEntry(name);
	}

	protected ZipOutputStream newOutputStream(OutputStream output) throws IOException {
		return new JarOutputStream(output);
	}

}
