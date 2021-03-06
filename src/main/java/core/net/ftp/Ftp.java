package core.net.ftp;

import java.io.IOException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.io.StreamReader;
import core.io.file.BinaryFile;
import core.os.unix.UnixFileList;

public class Ftp implements FtpCommands {
	
	private static final Logger log = LoggerFactory.getLogger(Ftp.class);
	
	private FtpMode mode = FtpMode.PASSIVE;
	private FtpControlConnection control;
	private FtpDataConnection connection;

	public FtpControlConnection getControl() {
		return control;
	}

	public void setMode(FtpMode mode) {
		if (mode == null) {
			throw new NullPointerException();
		}
		this.mode = mode;
	}

	public FtpMode getMode() {
		return mode;
	}

	public synchronized void connect(String host, int port) throws IOException {
		control = new FtpControlConnection(host, port);
	}

	public synchronized void disconnect() throws IOException {
		if (control != null) {
			control.close();
		}
	}

	public synchronized boolean login(String username, String password) throws IOException {
		if (username == null || password == null) {
			throw new NullPointerException();
		}
		if (username.length() == 0) {
			throw new IllegalArgumentException("username is empty");
		}
		if (password.length() == 0) {
			throw new IllegalArgumentException("password is empty");
		}
		FtpCode code = getControl().execute(USERNAME + ' ' + username);
		if (code.isPositiveCompletion()) {
			return true; // Password not required
		}
		if (!code.isPositiveIntermediate()) {
			return false; // Username not recognised/allowed
		}
		code = getControl().execute(PASSWORD + ' ' + password);
		return code.isPositiveCompletion();
	}

	public synchronized boolean logout() throws IOException {
		return getControl().execute(QUIT).isPositiveCompletion();
	}

	public synchronized boolean changeDirectory(String directory) throws IOException {
		return getControl().execute(CHANGE_DIRECTORY + ' ' + directory).isPositiveCompletion();
	}

	public synchronized boolean makeDirectory(String directory) throws IOException {
		return getControl().execute(CREATE_DIRECTORY + ' ' + directory).isPositiveCompletion();
	}

	public synchronized boolean removeDirectory(String directory) throws IOException {
		return getControl().execute(DELETE_DIRECTORY + ' ' + directory).isPositiveCompletion();
	}

	public synchronized boolean parentDirectory() throws IOException {
		return getControl().execute(CHANGE_DIRECTORY_UP).isPositiveCompletion();
	}

	public synchronized boolean deleteFile(String fileName) throws IOException {
		return getControl().execute(DELETE_FILE + ' ' + fileName).isPositiveCompletion();
	}

	public synchronized boolean renameFile(String oldName, String newName) throws IOException {
		if (getControl().execute(RENAME_FROM + ' ' + oldName).isPositiveIntermediate()) {
			return getControl().execute(RENAME_TO + ' ' + newName).isPositiveCompletion();
		}
		return false;
	}

	public synchronized UnixFileList listFiles() throws IOException {
		FtpDataConnection dataConnection = FtpDataConnection.connect(this, getMode());
		try {
			if (getControl().execute(LIST_DIRECTORY).isPositivePreliminary()) {
				byte[] data = dataConnection.download();
				String text = new String(data);
				if (text.toLowerCase().contains("permission denied")) {
					throw new IOException("Permission denied");
				}
				UnixFileList fileList = UnixFileList.parseUnixFileList(text);
				getControl().response().getCode().isPositiveCompletion();
				return fileList;
			}
		} finally {
			dataConnection.close();
		}
		return null;
	}

	public synchronized byte[] downloadFile(String filename) throws IOException {
		FtpDataConnection dataConnection = FtpDataConnection.connect(this, getMode());
		try {
			if (getControl().execute(DOWNLOAD_FILE + ' ' + filename).isPositivePreliminary()) {
				byte[] data = dataConnection.download();
				getControl().response().getCode().isPositiveCompletion();
				return data;
			}
		} finally {
			dataConnection.close();
		}
		return null;
	}
	
	public synchronized StreamReader downloadPartFile(String filename) throws IOException {
		 connection = FtpDataConnection.connect(this, getMode());		
		if (getControl().execute(DOWNLOAD_FILE + ' ' + filename).isPositivePreliminary()) {
			return connection.downloadReader();
		}
		return null;
	}
	
	public synchronized void closeConnection() throws IOException{
		connection.close();
	}
	
	public synchronized void uploadFile(String filename, byte[] data) throws IOException {
		FtpDataConnection dataConnection = FtpDataConnection.connect(this, getMode());
		try {
			if (getControl().execute(UPLOAD_FILE + ' ' + filename).isPositivePreliminary()) {
				dataConnection.upload(data);
				getControl().response().getCode().isPositiveCompletion();
			}
		} finally {
			dataConnection.close();
		}
	}
	
	public synchronized int size(String filename) throws IOException {
		FtpDataConnection dataConnection = FtpDataConnection.connect(this, getMode());
		try {
			getControl().request(SIZE + ' ' + filename);
			FtpResponse response = getControl().response();
			return Integer.valueOf(response.getLastLine().split(" ")[1]);
		} finally {
			dataConnection.close();
		}
	}
}
