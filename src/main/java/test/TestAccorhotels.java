package test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import core.http.exception.HttpException;
import core.io.file.TextFile;
import core.xml.Xml;
import engine.WebSpider;


public class TestAccorhotels extends WebSpider{

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		new TestAccorhotels().method();

	}

	private void method() throws Exception {
//		newHttpGetRequest("http://repos.accorhotels.com/ota/content.xml");
		FileInputStream fis=new FileInputStream("C:\\Documents and Settings\\User\\Desktop\\content.xml");
	    byte[] b=new byte[fis.available()];
		fis.read(b);
	    Xml offers=toXml(new String(b));
		System.err.println(offers.getChild("hotels").children());
		int count=offers.getChild("hotels").children("hotel");
		TextFile mapping = new TextFile("java/data/nearfinder/accorhotels.nfd");
		for(int i=0;i<count;i++){
			try{
			Xml hotel=offers.getChild("hotels").getChild("hotel",i);
			String hotelCode=hotel.getChild("hotelCode").getValue().toString();
			String latitude=hotel.getChild("latitude").getValue().toString();
			String longitude=hotel.getChild("longitude").getValue().toString();
			mapping.write(hotelCode + "\t" + latitude+ "," + longitude + "\r\n",true); 
			}catch (Exception e) {
				continue;
			}
		}
		System.err.println("end");
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
