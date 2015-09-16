package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.zip.ZipInputStream;

import core.io.file.TextFile;
import core.lang.LatLon;
import core.text.Charsets;
import core.xml.Xml;
import engine.WebSpider;


public class LateRoomsNFD extends WebSpider{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.err.println("start");
     new LateRoomsNFD().generate();		
 	System.err.println("end");
    
	}
	public void generate(){
			try {
				TextFile mapping = new TextFile("E://nfd2//laterooms.nfd");
				mapping.delete();
				ZipInputStream zipInput = new ZipInputStream(download("http://xmlfeed.laterooms.com/staticdata/hotels_standard.zip"));
				zipInput.getNextEntry();
				BufferedReader br = new BufferedReader(new InputStreamReader(zipInput, Charsets.UTF_8));
				String line;
				String hotelCode = "";
				String hotelDetails = "";
				StringBuilder sb = new StringBuilder();
				while ((line = br.readLine()) != null) {
					if (line.endsWith("</hotel>")) {

						hotelDetails = sb.append(line).toString().replace("<hotels>", "");
						hotelCode = hotelDetails.split("</hotel_ref>")[0].replaceAll("<hotel>    <hotel_ref>", "").trim();
						sb.delete(0, sb.length());
						
						try {
							Xml hotelInfo = toXml(hotelDetails.substring(1));
								String lon=hotelInfo.getChild("geo_code").getChild("long").getValue().toString();
								String lat=hotelInfo.getChild("geo_code").getChild("lat").getValue().toString();
								//System.err.println(hotelCode+"---"+lat+"---"+lon);
								mapping.write(hotelCode + "\t" + lat + "," +lon+ "\r\n", true);
	
						          
						} catch (Exception e) {
							continue;
						}
					} else {
						sb = sb.append(line);
					}
				}
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
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
