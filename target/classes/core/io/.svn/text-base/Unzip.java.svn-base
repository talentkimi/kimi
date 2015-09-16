package core.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import core.io.file.BinaryFile;
import core.io.file.Directory;

/**
 * ZIP Compression.
 */
public class Unzip extends BinaryFile {

	/** The length. */
	private int length = -1;

	/**
	 * Set this as verbose.
	 */
	public void setVerbose() {
		length = 0;
	}

	/**
	 * Creates a new zip file.
	 * @param filename the filename.
	 */
	public Unzip(String filename, boolean verbose) {
		super(filename);
	}

	/**
	 * Unzip this file to the given directory.
	 * @param directory the directory.
	 */
	public void unzipTo(Directory directory) throws IOException {
		ZipFile zipFile = new ZipFile(this);
		Enumeration entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			if (!entry.isDirectory()) {
				String name = entry.getName().replace('\\', '/');
				File entryFile = new File(directory, name);
				File entryFileDirectory = entryFile.getParentFile();
				entryFileDirectory.mkdirs();
				StreamInput input = new StreamInput(zipFile.getInputStream(entry));
				try {
					StreamOutput output = new StreamOutput(entryFile);
					try {
						input.readTo(output);
					} finally {
						output.close();
					}
				} finally {
					input.close();
				}
				entryFile.setLastModified(entry.getTime());
			}
		}
		zipFile.close();
	}

	/**
	 * Zip the given file to this.
	 * @param fileToZip the file to zip.
	 * @param overwrite true to overwrite this file if it exists.
	 */
	public void zipFrom(BinaryFile fileToZip, boolean overwrite) throws IOException {
		zipFrom(fileToZip, overwrite, null);
	}

	/**
	 * Zip the given file to this.
	 * @param fileToZip the file to zip.
	 * @param overwrite true to overwrite this file if it exists.
	 */
	public void zipFrom(BinaryFile fileToZip, boolean overwrite, String excludePath) throws IOException {
		if (exists() && !overwrite) {
			throw new IOException("zip file already exists: '" + this + "'");
		}
		StreamOutput output = new StreamOutput(this);
		try {
			ZipOutputStream zip = new ZipOutputStream(output);
			try {
				File parentFile = fileToZip.getParentFile();
				String parent = "";
				if (parentFile != null) {
					parent = parentFile.toString().replace('\\', '/') + "/";
				}
				zip.setLevel(9);
				zip.setMethod(ZipOutputStream.DEFLATED);
				zip(zip, fileToZip, parent, excludePath);
			} finally {
				zip.close();
			}
			if (length > 0) {
				System.out.println();
			}
		} catch (FileNotFoundException e) {
			throw e;
		} finally {
			output.close();
		}
	}

	/**
	 * Zip this file.
	 * @param zip the zip stream.
	 * @param fileToZip the file to zip.
	 */
	private void zip(ZipOutputStream zip, BinaryFile fileToZip, String parent, String excludePath) throws IOException {
		if (length >= 0) {
			System.out.print('.');
			length++;
			if (length == 80) {
				System.out.println();
				length = 0;
			}
		}
		String name = fileToZip.toString().replace('\\', '/');
		if (parent != null && name.startsWith(parent)) {
			name = name.substring(parent.length(), name.length());
		}
		if (excludePath != null && name.startsWith(excludePath)) {
			name = name.substring(excludePath.length(), name.length());
		}
		ZipEntry entry = new ZipEntry(name);
		if (fileToZip.isDirectory()) {
			BinaryFile[] filesToZip = (BinaryFile[]) fileToZip.listFiles(false);
			for (int i = 0; i < filesToZip.length; i++) {
				zip(zip, filesToZip[i], parent, excludePath);
			}
		} else {
			zip.putNextEntry(entry);
			byte[] data = fileToZip.readToByteArray();
			zip.write(data, 0, data.length);
			zip.closeEntry();
		}
	}

}
