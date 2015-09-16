package test.ftptest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.ZipInputStream;

import core.io.StreamReader;
import core.net.ftp.Ftp;
import core.text.Charsets;
import engine.WebSpider;

public class FTPTest extends WebSpider{

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		FTPTest f=new FTPTest();
		f.getFTP();
		
	}

	private void getFTP() throws Exception {
		
		String USERNAME = "finn";
		String PASSWORD = "fin@g6kYY23";
		String FTP_URL = "xml.vacasol.dk";
		String SERVER_FILE_PATH = "Catalog-da.xml.zip";
		Ftp ftp = new Ftp();

		ftp.connect(FTP_URL, 21);
		if (ftp.login(USERNAME, PASSWORD)) {
			addBytesReceived(ftp.size(SERVER_FILE_PATH));
			StreamReader sr = ftp.downloadPartFile(SERVER_FILE_PATH);
			sr.setCharset(Charsets.UTF_8);
			ZipInputStream zipInput = new ZipInputStream(sr);
			String entryName = zipInput.getNextEntry().getName();
			if (entryName.equals("Catalog-da.xml")) {
				BufferedReader reader = null;
				reader = new BufferedReader(new InputStreamReader(zipInput, Charsets.UTF_8));
                String line = null;
				while((line=reader.readLine())!=null){
					System.err.println(line);
				}
			}

		}
		ftp.logout();
		
	}

	@Override
	public String getSpiderName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void runTask() throws Throwable {
		// TODO Auto-generated method stub
		
	}

}
