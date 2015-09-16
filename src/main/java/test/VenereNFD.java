package test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import core.http.exception.HttpException;
import core.io.file.TextFile;
import core.lang.LatLon;
import core.net.IpAddress;
import core.text.Charsets;
import core.text.Text;
import core.util.UtilDate;
import core.xml.Xml;
import engine.WebSpider;


public class VenereNFD extends WebSpider{

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		System.err.println("start");
		new VenereNFD().generate();
		System.err.println("end");
		
	}
  public void generate() throws Exception{
			TextFile mapping = new TextFile("E://nfd2//venere.nfd");
			mapping.delete();
//			setHttpCharset(Charsets.UTF_8);
//			newHttpGetRequest("http://www.venere.com/feeds/xml/catalog_en.xml.gz");
//			setHttpHeader("Accept-Encoding", "gzip");
			
			FileInputStream is=new FileInputStream("E://catalog_en(2).xml//catalog_en(2).xml");
			BufferedReader br = new BufferedReader(new InputStreamReader(is, Charsets.UTF_8));		
			String line;
			StringBuilder sb = new StringBuilder();
			String hotelDetails = "";
			while ((line = br.readLine()) != null) {
				if (line.endsWith("</p>")) {
					hotelDetails = sb.append(line).toString();
					sb.delete(0, sb.length());	
					try {
						Xml hotelInfo = toXml(hotelDetails);
						String id = hotelInfo.getAttribute("id").getValue().toString();
						   String lat= hotelInfo.getChild("lat").getValue().toString();
						   String lon=hotelInfo.getChild("lon").getValue().toString();
//			              System.err.println(id+"---"+lat+"---"+lon);
			           mapping.write(id + "\t" + lat + "," +lon+ "\r\n", true); 
					} catch (Exception e) {
						continue;
					}
				} else {
					sb = sb.append(line);
				}
			}


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
