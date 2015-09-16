package test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import core.text.Charsets;
import core.util.UtilList;
import core.util.UtilMap;
import core.util.time.Milliseconds;
import core.util.time.Weeks;
import core.xml.Xml;
import engine.WebSpider;

public class Apollo extends WebSpider{

	// private static final String DEVELOPER_EMAIL = "car.hotel.prepackaged@travelfusion.com";
	private static final String URL_PREFIX = "http://www.apollo.no";

	protected Milliseconds getDatabaseTimeout() {
		return new Weeks(4);
	}
	public void method() throws Exception{
		FileInputStream is = new FileInputStream("E://apollo.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(is, Charsets.UTF_8));
		String line;
		int i=0;
		int j=0;
//		while((line=br.readLine())!=null){
//			String hotelID=line.split("\t")[0];
//			String hotelPageUrl=line.split("\t")[1];
			setHttpCharset(UTF_8);
			newHttpGetRequest("http://www.apollo.no/NO/hvor-vil-du-reise/Afrika/Egypt/SharmelSheik/Nabq/Accomodations/Pages/ParkInnSharmelSheikresort.aspx?hBookingBtn=true");
			downloadHttp(60,"Apollo_hotel.html");
			
			try {
				extractText(null, "<div id=\"fragment-1");
				if (extractText().contains("image-rotator")) {
					selectText(null, "</div>");
					while (selectText("<img src=\"", "\"", OPTIONAL)) {
						String image = extractText();
						deselectText();
					}
					deselectText();
				}
				extractText(null, " <div class=\"rating\">");
				String stars = extractText("<div class=\"", "\"");
			} catch (Exception e) {
				i++;
				//System.err.println("No Hotel"+hotelID);
			}

				j++;
				System.err.println("OK");
//			}
			
		System.err.println(i);
		System.err.println(j);
			
		}

	
public static void main(String[] args) throws Exception {
	new Apollo().method();
}
	public String getValue(String key, UtilMap params) throws Throwable {
		// read all the hotelID and URL to cache first time.
		String cache_param_key = "hotelUrl";
		String hotelID = key;
		String hotelPageUrl = (String) params.get(cache_param_key);
		Xml hotelDetailsXmlResponse = null;
		try {
//			hotelDetailsXmlResponse = getHotelInfo(hotelID, hotelPageUrl);
		} catch (Exception e) {
			// Email.send(DEVELOPER_EMAIL, "Prepackage Cache@" +
			// getSupplierName().getCaseSensitive() + " - CacheHotel Debug", new
			// UtilDate().toString("yyyy/MM/dd - HH:mm:ss") + "\n"
			// + IpAddress.getLocalAddress() + "\n" + Text.getStackTrace(e) + "\n" + hotelID + "\n"
			// + hotelPageUrl, true);
		}
		if (hotelDetailsXmlResponse == null) {
			return "No Hotel";
		}
		return hotelDetailsXmlResponse.toString();
	}
/*
	public Xml getHotelInfo(String hotelID, String hotelPageUrl) throws Exception {
		UtilList miscInfoList = new UtilList();
		UtilList imageInfoList = new UtilList();
		HotelDetailsResult hotelDetailsResult = new HotelDetailsResult();
		setHttpCharset(UTF_8);
		newHttpGetRequest(hotelPageUrl);
		downloadHttp("Apollo_hotel.html");
		try {
			extractText(null, "<div id=\"fragment-1");
		} catch (Exception e) {
			return null;
		}
		if (extractText().contains("image-rotator")) {
			selectText(null, "</div>");
			while (selectText("<img src=\"", "\"", OPTIONAL)) {
				String image = extractText();
				imageInfoList.add(image);
				deselectText();
			}
			deselectText();
		}
		extractText(null, " <div class=\"rating\">");
		String stars = extractText("<div class=\"", "\"");
		stars = formatStars(stars);
		String type = "hotel";
		if (!(extractText().contains("<h1>"))) return null;
		String hotelName = extractText("<h1>", "</h1>").replace("&amp;", "&");;
		StringBuilder description = new StringBuilder();
		String address = "";
		selectText("<div id=\"spacer\"></div>", "</div>");
		if (extractText().contains("<p class=\"intro\">")) {
			while (selectText("<strong>", "/p>", OPTIONAL)) {
				String title = extractText(null, "</strong>");
				String content = "";
				if (extractText().contains("<span class=\"rtc_normal\">")) {
					content = extractText("<span class=\"rtc_normal\">", "</span>");
				} else if (!extractText().contains("<span")) {
					if (extractText().contains("<p>"))
						content = extractText("<p>", "<");
					else
						content = extractText(null, "<");
				} else {
					try {
						content = extractText("<span>", "</span>");
					} catch (Exception e) {
						content = extractText(">", "</span>");
					}
				}
				description.append(title);
				description.append("\n");
				description.append(content.replaceAll("&nbsp; ", ""));
				description.append("\n");
				deselectText();
			}
		} else {
			description.append("");
		}
		deselectText();
		if (!extractText("<div id=\"fragment-2\">", "<div class=\"content-cell\">", NO_MOVE).contains("<h3")) {
			return null;
		}
		selectText("<div id=\"fragment-2\">", "<h3");
		while (selectText("<strong>", "/p>", OPTIONAL)) {
			String name = extractText(null, "</strong>");
			String value = extractText("<p>", "<");
			String miscInfo = name + "|" + value;
			miscInfoList.add(miscInfo);
			deselectText();
		}
		deselectText();
		selectText("</h3>", "<div id=\"fragment-3\">");
		while (selectText("<div", "</div>", OPTIONAL)) {
			String name = extractText("<h4>", "</h4>");
			String value = extractText("<p>", "</p>");
			String miscInfo = name + "|" + value;
			miscInfoList.add(miscInfo);
			deselectText();
		}
		for (int i = 0; i < miscInfoList.size(); i++) {
			if (miscInfoList.get(i).toString().split("[|]").length > 1) {
				String name = miscInfoList.get(i).toString().split("[|]")[0];
				String value = miscInfoList.get(i).toString().split("[|]")[1];
				boolean addDistance = false;
				if (name.equals("Adresse")) {
					address = value;
					if (address.contains("Tel:")) {
						address = address.substring(0, address.indexOf("Tel:"));
					}
				} else {
					if (name.equals("Andre fasiliteter")) {
						if (value != null && value.split(",").length > 0) {
							StringBuilder otherFacilty = new StringBuilder();
							for (int j = 0; j < value.split(",").length; j++) {
								boolean addFacility = false;
								if (selectFacility(value.split(",")[j]) != null) {
									addFacility = true;
								}
								if (addFacility) {
									HotelSupplierFacility facility = new HotelSupplierFacility();
									facility.set(selectFacility(value.split(",")[j]), "available", selectFacility(value.split(",")[j]));
									hotelDetailsResult.getFacilityList().add(facility);
								} else {
									otherFacilty.append(value.split(",")[j]);
									otherFacilty.append(", ");
								}
							}
							if (otherFacilty.length() > 0) {
								SupplierMiscellaneousInfo info = new SupplierMiscellaneousInfo();
								info.set(name, otherFacilty.substring(0, otherFacilty.length() - 2));
								hotelDetailsResult.getMiscellaneousInfoList().add(info);
							}
						}
					}

					if (selectDistance(name, value) != null) {
						addDistance = true;
					}
					if (addDistance) {
						SupplierMiscellaneousInfo info = new SupplierMiscellaneousInfo();
						info.set(selectDistance(name, value).split("[|]")[0], selectDistance(name, value).split("[|]")[1]);
						hotelDetailsResult.getMiscellaneousInfoList().add(info);
					} else if (!name.equals("Andre fasiliteter")) {
						SupplierMiscellaneousInfo info = new SupplierMiscellaneousInfo();
						info.set(name, value);
						hotelDetailsResult.getMiscellaneousInfoList().add(info);
					}
				}
			}
		}
		for (int i = 0; i < imageInfoList.size(); i++) {
			String url = URL_PREFIX + imageInfoList.get(i).toString();
			SupplierImage image = new SupplierImage();
			image.getUrl().set(url);
			hotelDetailsResult.getImageList().add(image);
		}
		try {
			hotelDetailsResult.setStars(stars);
		} catch (Exception e) {
		}
		hotelDetailsResult.setType(type);
		hotelDetailsResult.setHotelName(hotelName);
		hotelDetailsResult.setDescription(description.toString());
		hotelDetailsResult.setAddress(address);
		return hotelDetailsResult.getHotelDetailsResult().toXml();

	}

	public String formatStars(String stars) {
		stars = stars.substring(stars.indexOf("-"), stars.length()).replaceAll("-", "");
		if (stars.contains("half")) {
			stars = stars.substring(0, stars.indexOf("h")) + ".5";
		}
		return stars;
	}

	public String selectDistance(String name, String value) {
		String type = "";
		String distanceInMeter = "0";
		if (name.equals("Avstand til strand")) {
			type = "ToBeach";
			distanceInMeter = value;

		} else if (name.equals("Avstand til sentrum")) {
			type = "ToLocalCentre";
			distanceInMeter = value;
		} else {
			return null;
		}
		if (distanceInMeter.contains("Ca")) distanceInMeter = distanceInMeter.replace("Ca ", "");
		if (distanceInMeter.contains("ca")) distanceInMeter = distanceInMeter.replace("ca ", "");
		if (distanceInMeter.contains("km") && distanceInMeter.contains(",")) {
			distanceInMeter = distanceInMeter.replace(" km", "");
			if (distanceInMeter.contains("-")) {
				distanceInMeter = distanceInMeter.substring(distanceInMeter.indexOf("-") + 1);
			}
			distanceInMeter = String.valueOf((Double.valueOf(distanceInMeter.replace(",", ".")) * 1000)).replace(".0", "");
		} else if (distanceInMeter.contains("km") && !distanceInMeter.contains(",")) {
			distanceInMeter = distanceInMeter.replace(" km", "") + "000";
		} else if (distanceInMeter.contains("meter")) {
			distanceInMeter = distanceInMeter.replace(" meter", "");
		} else if (distanceInMeter.endsWith(" m")) {
			distanceInMeter = distanceInMeter.replace(" m", "");
		} else {
			distanceInMeter = "0";
		}
		return type + "|" + distanceInMeter;
	}

	public String selectFacility(String value) {
		String facility = null;
		if (value.equals(" Bar")) {
			facility = BAR;
		} else if (value.equals(" Basseng")) {
			facility = POOL;
		} else if (value.equals(" Barnebasseng")) {
			facility = CHILDREN_POOL;
		} else if (value.equals(" Restaurant")) {
			facility = RESTAURANT;
		} else if (value.equals(" Luftkondisjonering")) {
			facility = AIR_CONDITIONING;
		} else if (value.equals(" Heis")) {
			facility = LIFT;
		} else if (value.equals(" Outdoor Parking")) {
			facility = PARKING;
		} else if (value.equals(" Internett")) {
			facility = INTERNET;
		}
		return facility;
	}
	
	*/
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
