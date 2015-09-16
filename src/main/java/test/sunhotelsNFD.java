package test;

import java.io.IOException;
import java.util.HashSet;

import core.http.exception.HttpException;
import core.io.file.TextFile;
import core.xml.Xml;
import engine.WebSpider;


public class sunhotelsNFD extends WebSpider{
	public static void main(String[] args) {
		try {
			new sunhotelsNFD().createNFD();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static final String[] supportedLanguage = {"SV", "NO", "EN"};
	private void createNFD() throws Exception {
		newHttpGetRequest("http://xml.sunhotels.net/xml_destinations.asp?username=finn&password=norge&lang=en&destinationcode=&xmlversion=11&sortby=dest");
		Xml cityCodeResults = downloadXml("citycode");
		TextFile mapping = new TextFile("E://nfd//sunhotels.nfd");
		mapping.delete();
		for (int i = 0; i < cityCodeResults.children("Destination"); i++) {
			final Xml cityCodeResult = cityCodeResults.getChild("Destination", i);
			String cityCode = cityCodeResult.getChild("DestinationCode").getValue().toString().trim();
			for (String language : supportedLanguage) {
				try {
					newHttpGetRequest("http://xml.sunhotels.net/xml_rooms_search.asp?username=finn&password=norge&xmlversion=11&lang=" + language.toLowerCase() + "&destination=" + cityCode + "&coords=1");
					Xml hotelInfoResults = downloadXml("hotelInfoResults");
					for (int j = 0; j < hotelInfoResults.children("hotel"); j++) {
						final Xml hotelInfoResult = hotelInfoResults.getChild("hotel", j);
						String hotel_id = hotelInfoResult.getChild("hotel.id").getValue().toString();
						String lat = hotelInfoResult.getChild("coordinates").getChild("latitude").getValue().toString();
						String lon = hotelInfoResult.getChild("coordinates").getChild("longitude").getValue().toString();
						System.err.println(hotel_id+"--"+lat+"--"+lon);
						//mapping.write(hotel_id + "\t" + lat + "," +lon+ "\r\n", true);
					}
				}catch(Exception e){
					continue;
				}
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
