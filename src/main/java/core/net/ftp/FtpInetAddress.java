package core.net.ftp;

import java.net.InetAddress;

import java.net.UnknownHostException;

import core.text.Text;

public class FtpInetAddress {

	public static final FtpInetAddress parseFtpInetAddress(String address) throws UnknownHostException {
		String[] parts = Text.split(address, ",");
		byte[] bytes = new byte[4];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(parts[i]);
		}
		int port0 = Integer.parseInt(parts[4]);
		int port1 = Integer.parseInt(parts[5]);
		int port = (port0 << 8) + port1;
		return new FtpInetAddress(InetAddress.getByAddress(bytes), port);
	}

	private final InetAddress address;
	private final int port;

	public InetAddress getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public String toString() {
		byte[] bytes = address.getAddress();
		int address0 = (bytes[0] >= 0 ? bytes[0] : bytes[0] + 256);
		int address1 = (bytes[1] >= 0 ? bytes[1] : bytes[1] + 256);
		int address2 = (bytes[2] >= 0 ? bytes[2] : bytes[2] + 256);
		int address3 = (bytes[3] >= 0 ? bytes[3] : bytes[3] + 256);
		int port0 = ((port & 0xff00) >> 8);
		int port1 = (port & 0x00ff);
		return address0 + "," + address1 + "," + address2 + "," + address3 + "," + port0 + "," + port1;
	}

	public FtpInetAddress(InetAddress address, int port) {
		this.address = address;
		this.port = port;
	}
}
