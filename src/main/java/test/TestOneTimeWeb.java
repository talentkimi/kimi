package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

import core.http.exception.HttpException;
import core.io.StreamReader;
import core.io.file.TextFile;
import core.net.IpAddress;
import core.net.ftp.Ftp;
import core.text.Charsets;
import core.util.UtilDate;
import core.xml.Xml;
import core.xml.XmlNode;
import engine.WebSpider;

public class TestOneTimeWeb extends WebSpider {

	/**
	 * @param args
	 * @throws Throwable
	 */

	private String Token = null;
	private static UtilDate TokenTime = new UtilDate(
			new UtilDate().getTimeInMillis() - 55 * 60 * 1000);

	public static void main(String[] args) throws Throwable {
		// new TestOneTimeWeb().one();
		// new TestOneTimeWeb().two();
		// new TestOneTimeWeb().three();
		// new TestOneTimeWeb().four();
		// new TestOneTimeWeb().five();
		// new TestOneTimeWeb().six();
		// new TestOneTimeWeb().seven();
		// new TestOneTimeWeb().eight();
		// new TestOneTimeWeb().nine();
		// new TestOneTimeWeb().ten();
		// new TestOneTimeWeb().eleven();
		// new TestOneTimeWeb().twelve();
		// new TestOneTimeWeb().thirteen();
		// new TestOneTimeWeb().fourteen();
		// new TestOneTimeWeb().fifteen();
		// new TestOneTimeWeb().sixteen();
		// new TestOneTimeWeb().seventeen();
		// new TestOneTimeWeb().eighteen();
		// new TestOneTimeWeb().ninteen();
		// new TestOneTimeWeb().twenty();
		// new TestOneTimeWeb().twentyone();
		// new TestOneTimeWeb().twentytwo();
		// System.err.println(getWeek("01/12/2010"));
		// System.err.println("------");
		// new TestOneTimeWeb().twentythree();
		// new TestOneTimeWeb().twentyfour();
		// new TestOneTimeWeb().twentyfive();
		// new TestOneTimeWeb().twentysix();
		// new TestOneTimeWeb().twentyseven();
		// new TestOneTimeWeb().twentyeight();
		// new TestOneTimeWeb().twentyningth();
		// new TestOneTimeWeb().thirty();
		// new TestOneTimeWeb().thirtyone();
		// new TestOneTimeWeb().thirtytwo();
		// new TestOneTimeWeb().thirtythree();
		// new TestOneTimeWeb().thirtyfour();
		// new TestOneTimeWeb().extractExcel();
		// new TestOneTimeWeb().thirtyfive();
		// new TestOneTimeWeb().thirtysix();
		// new TestOneTimeWeb().extractExcel2();
		// new TestOneTimeWeb().extractExcel3();
		// new TestOneTimeWeb().extractExcel4();
		// new TestOneTimeWeb().makeBookingRequest();
		// new TestOneTimeWeb().qq();
		// new TestOneTimeWeb().travelcube();
		// new TestOneTimeWeb().travelcube2();
		// new TestOneTimeWeb().travelcube3();
		// new TestOneTimeWeb().travelcube4();
		// new TestOneTimeWeb().apollo();
		// new TestOneTimeWeb().venere();
		// new TestOneTimeWeb().vene2(new TestOneTimeWeb().vene());
		new TestOneTimeWeb().geo();
		// new TestOneTimeWeb().qqw();
	}

	private void qqw() throws Exception {
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
				reader = new BufferedReader(new InputStreamReader(zipInput,
						Charsets.UTF_8));
				String line = null;
				while ((line = reader.readLine()) != null) {
					System.err.println(line);
				}
			}

		}
		ftp.logout();

	}

	private void geo() throws Exception {
		newHttpGetRequest("http://114-svc.elong.com/xml/hotellist.xml");
		Xml hotels = downloadXml("Hotels");
		List<String> hotelList = new ArrayList<String>();
		for (int m = 0; m < 1000; m++) {
			String hotelId = hotels.getChild("HotelInfoForIndex", m)
					.getChild("Hotel_id").getValue().toString();
			hotelList.add(hotelId);
		}
		FileWriter correct = new FileWriter("C:\\Users\\kimi\\geo.txt");
		PrintWriter out = new PrintWriter(new BufferedWriter(correct));
		// for (int i = 0; i < correctRecords.size(); i++) {
		// out.println(correctRecords.get(i));
		// }

		for (int n = 0; n < hotelList.size(); n++) {
			String hotelCode = hotelList.get(n).toString();
			try {
				newHttpGetRequest("http://114-svc.elong.com/xml/v1.1/perhotelen/"
						+ hotelCode + ".xml");
				Xml hotel = downloadXml(600, hotelCode);
				String hotelId = hotel.getChild("q1:id").getValue().toString();
				String lat = hotel.getChild("q1:lat").getValue().toString();
				String lon = hotel.getChild("q1:lon").getValue().toString();
				out.println(hotelId + "\t" + lat + "," + lon);
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		out.close();
	}

	private void vene2(String token) throws Exception {
		XmlNode soap = new XmlNode("soap:Envelope");
		soap.newAttribute("xmlns:xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		soap.newAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
		soap.newAttribute("xmlns:soap",
				"http://schemas.xmlsoap.org/soap/envelope/");
		Xml soapBody = soap.newChild("soap:Body");
		Xml getHotelList = soapBody.newChild("GetHotelList");
		getHotelList.newAttribute("xmlns", "http://elong.com/NorthBoundAPI/");
		Xml hotelRequest = getHotelList.newChild("GetHotelListRequest");
		Xml requestHead = hotelRequest.newChild("RequestHead");
		requestHead.newChild("LoginToken", token);
		requestHead.newChild("Language", "EN");
		requestHead.newChild("GUID");
		requestHead.newChild("Version");
		requestHead.newChild("TestMode", "0");

		Xml hotelCondition = hotelRequest.newChild("GetHotelCondition");
		hotelCondition.newChild("CheckInDate", "2012-03-04T00:00:00");
		hotelCondition.newChild("CheckOutDate", "2012-03-05T00:00:00");
		hotelCondition.newChild("HotelId", "00101011");
		// hotelCondition.newChild("RoomTypeID");
		// hotelCondition.newChild("RatePlanID");

		newHttpPostRequest("http://211.151.230.198/NorthBoundService/V1.1/NorthBoundAPIService.asmx");
		addHttpHeader("Content-Type", "text/xml; charset=utf-8");
		addHttpHeader("SOAPAction",
				"http://elong.com/NorthBoundAPI/GetHotelList");
		System.err.println(soap);
		setXmlContent(soap.toString().replace("\r\n", ""));
		Xml loginResponse = downloadXml("getHotel");
		System.err.println(loginResponse);

	}

	private static void updateTokenTime() {
		TokenTime = new UtilDate();
	}

	private static Xml generateLogonRequest() {
		String USERNAME = "paa_finn";
		String PASSWORD = "ngWQgAH$}K";
		Xml logonRequest = new XmlNode("soapenv:Envelope");
		logonRequest.newAttribute("xmlns:soapenv",
				"http://schemas.xmlsoap.org/soap/envelope/");
		logonRequest.newAttribute("xmlns:xsd",
				"http://www.w3.org/2001/XMLSchema");
		logonRequest.newAttribute("xmlns:xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		Xml body = logonRequest.newChild("soapenv:Body");
		Xml logonNode = body.newChild("Logon");
		logonNode.newAttribute("xmlns", "https://secure.mytravel.se/LogonWS");
		logonNode.newChild("username", USERNAME);
		logonNode.newChild("password", PASSWORD);

		return logonRequest;
	}

	private synchronized String getToken() throws Exception {
		if ((new UtilDate()).getTimeInMillis() - TokenTime.getTimeInMillis() > 49 * 60 * 1000) {
			Xml LogonRequest = generateLogonRequest();

			getCookieList().clear();
			// Send the request
			newHttpPostRequest("https://secure.mytravel.se/LogonWS/LogonService.asmx");
			setHttpVersion();
			addHttpHeader("SOAPAction",
					"https://secure.mytravel.se/LogonWS/Logon");
			setXmlContent(LogonRequest);

			boolean isDownloadSuccess = false;
			int numberOfRetry = 0;
			Xml getTokenResponse = null;
			do
				try {
					getTokenResponse = downloadXml("VingSEToken.xml");
					Token = getTokenResponse.getChild("soap:Body")
							.getChild("LogonResponse").getChild("LogonResult")
							.getValue().toString();
					isDownloadSuccess = true;
				} catch (RuntimeException e) {
					numberOfRetry++;
				} catch (Exception e) {
					numberOfRetry++;
					sleep(10);
				}
			while (!isDownloadSuccess && numberOfRetry < 4);

			if (Token == null || isDownloadSuccess == false)
				return "";
			updateTokenTime();
		}
		return Token;
	}

	private String vene() throws Exception {
		XmlNode soap = new XmlNode("soap:Envelope");
		soap.newAttribute("xmlns:xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		soap.newAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
		soap.newAttribute("xmlns:soap",
				"http://schemas.xmlsoap.org/soap/envelope/");
		Xml soapBody = soap.newChild("soap:Body");
		Xml login = soapBody.newChild("Login");
		login.newAttribute("xmlns", "http://elong.com/NorthBoundAPI/");
		Xml loginRequest = login.newChild("loginRequest");
		loginRequest.newChild("UserName", "wutest11");
		loginRequest.newChild("Password", "wutest11");
		newHttpPostRequest("http://211.151.230.198/NorthBoundService/V1.1/NorthBoundAPIService.asmx");
		addHttpHeader("Content-Type", "text/xml; charset=utf-8");
		addHttpHeader("SOAPAction", "http://elong.com/NorthBoundAPI/Login");
		setXmlContent(soap.toString().replace("\r\n", ""));
		// System.err.println(soap);
		Xml loginResponse = downloadXml("Login");
		System.err.println(loginResponse);
		String loginToken = loginResponse.getChild("soap:Body")
				.getChild("LoginResponse").getChild("LoginResult")
				.getChild("LoginToken").getValue().toString();
		System.err.println(loginToken);
		return loginToken;

	}

	private void travelcube4() throws Exception {

		Xml request = new XmlNode("Request");
		Xml source = request.newChild("Source");
		Xml requestorId = source.newChild("RequestorID");
		requestorId.newAttribute("Client", "381");
		requestorId.newAttribute("EMailAddress", "XML@TRAVELFUSION.COM");
		requestorId.newAttribute("Password", "PASS");
		Xml requestor = source.newChild("RequestorPreferences");
		requestor.newAttribute("Language", "en");
		requestor.newAttribute("Currency", "GBP");
		requestor.newAttribute("Country", "GB");
		requestor.newChild("RequestMode", "SYNCHRONOUS");
		requestor.newChild("ResponseURL", "");

		Xml requestDetails = request.newChild("RequestDetails");
		Xml booking = requestDetails.newChild("AddBookingRequest");
		booking.newAttribute("Currency", "GBP");
		booking.newChild("BookingName", "");
		booking.newChild("BookingReference", "");
		booking.newChild("AgentReference", "");
		booking.newChild("BookingDepartureDate", "");
		Xml paxNames = booking.newChild("PaxNames");
		Xml paxName = paxNames.newChild("PaxName", "Mr Fang");
		paxName.newAttribute("PaxId", "1");
		Xml bookingItems = booking.newChild("BookingItems");
		Xml bookingItem = bookingItems.newChild("BookingItem");
		bookingItem.newAttribute("ItemType", "hotel");
		bookingItem.newChild("ItemReference", "");
		Xml itemCity = bookingItem.newChild("ItemCity");
		itemCity.newAttribute("Code", "AMS");
		Xml item = bookingItem.newChild("Item");
		item.newAttribute("Code", "NAD");

		System.err.println(request);
		newHttpPostRequest("https://interface.demo.octopustravel.com/rbsukapi/RequestListenerServlet");
		addHttpHeader("SOAPAction",
				"https://interface.demo.octopustravel.com/rbsukapi/RequestListenerServlet");
		addHttpHeader("Content-Type", "text/xml; charset=utf-8");
		setXmlContent("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r"
				+ request.toString());
		System.err.println(downloadXml("bookingresponse"));

	}

	private void travelcube3() throws Exception {

		Xml request = new XmlNode("Request");
		Xml source = request.newChild("Source");
		Xml requestorId = source.newChild("RequestorID");
		requestorId.newAttribute("Client", "381");
		requestorId.newAttribute("EMailAddress", "XML@TRAVELFUSION.COM");
		requestorId.newAttribute("Password", "PASS");
		Xml requestor = source.newChild("RequestorPreferences");
		requestor.newAttribute("Language", "en");
		// requestor.newAttribute("Currency","GBP");
		// requestor.newAttribute("Country","GB");
		requestor.newChild("RequestMode", "SYNCHRONOUS");

		Xml requestDetails = request.newChild("RequestDetails");
		Xml price = requestDetails.newChild("HotelPriceBreakdownRequest");
		price.newChild("City", "AMS");
		price.newChild("Item", "KRA");
		Xml period = price.newChild("PeriodOfStay");
		period.newChild("CheckInDate", "2011-12-12");
		period.newChild("Duration", "4");

		Xml rooms = price.newChild("Rooms");
		Xml room = rooms.newChild("Room");
		room.newAttribute("Code", "DB");
		room.newAttribute("NumberOfRooms", "1");
		Xml beds = room.newChild("ExtraBeds");
		beds.newChild("Age", "5");
		// Xml star=searchHotel.newChild("StarRating","3");
		// star.newAttribute("MinimumRating","true");
		// searchHotel.newChild("LocationCode","G1");
		// Xml facilityCodes=searchHotel.newChild("FacilityCodes");
		// facilityCodes.newChild("FacilityCode","*SO");
		// searchHotel.newChild("OrderBy","pricelowtohigh");
		// searchHotel.newChild("NumberOfReturnedItems","10");
		System.err.println(request);
		newHttpPostRequest("https://interface.demo.octopustravel.com/rbsukapi/RequestListenerServlet");
		addHttpHeader("SOAPAction",
				"https://interface.demo.octopustravel.com/rbsukapi/RequestListenerServlet");
		addHttpHeader("Content-Type", "text/xml; charset=utf-8");
		setXmlContent("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r"
				+ request.toString());
		System.err.println(downloadXml("pricebreakdown"));

	}

	private void venere() throws Exception {

		newHttpGetRequest("https://b2b-uat.venere.com/xhi-1.0/resources/properties_catalog.resource");
		addHttpGetField("org", "Travelfusion");
		addHttpGetField("user", "user_res");
		addHttpGetField("psw", "7g5T9y6E4r2N6n3w");
		byte[] content = downloadHttpToByteArray("File").toByteArray();
		ByteArrayInputStream is = new ByteArrayInputStream(content);

		// InputStream is =
		// download("https://b2b-uat.venere.com/xhi-1.0/resources/properties_catalog.resource?org=Travelfusion&user=user_res&psw=7g5T9y6E4r2N6n3w");
		// InputStreamReader in = new InputStreamReader(is, Charsets.UTF_8);

		GZIPInputStream zis = new GZIPInputStream(is);
		InputStreamReader isr = new InputStreamReader(zis, UTF_8);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		StringBuffer region = new StringBuffer();
		while ((line = br.readLine()) != null) {
			System.err.println(line);
		}

	}

	private void travelcube2() throws Exception {
		Xml request = new XmlNode("Request");
		Xml source = request.newChild("Source");
		Xml requestorId = source.newChild("RequestorID");
		requestorId.newAttribute("Client", "381");
		requestorId.newAttribute("EMailAddress", "XML@TRAVELFUSION.COM");
		requestorId.newAttribute("Password", "PASS");
		Xml requestor = source.newChild("RequestorPreferences");
		requestor.newAttribute("Language", "en");
		requestor.newAttribute("Currency", "GBP");
		requestor.newAttribute("Country", "GB");
		requestor.newChild("RequestMode", "SYNCHRONOUS");

		Xml requestDetails = request.newChild("RequestDetails");
		Xml searchHotel = requestDetails.newChild("SearchHotelPriceRequest");
		Xml itemDestination = searchHotel.newChild("ItemDestination");
		itemDestination.newAttribute("DestinationType", "city");
		itemDestination.newAttribute("DestinationCode", "AMS");
		searchHotel.newChild("ImmediateConfirmationOnly");
		Xml period = searchHotel.newChild("PeriodOfStay");
		period.newChild("CheckInDate", "2011-12-12");
		period.newChild("Duration", "4");
		searchHotel.newChild("IncludeRecommended");
		Xml rooms = searchHotel.newChild("Rooms");
		Xml room = rooms.newChild("Room");
		room.newAttribute("Code", "DB");
		room.newAttribute("NumberOfRooms", "1");
		Xml beds = room.newChild("ExtraBeds");
		beds.newChild("Age", "5");
		// Xml star=searchHotel.newChild("StarRating","3");
		// star.newAttribute("MinimumRating","true");
		// searchHotel.newChild("LocationCode","G1");
		// Xml facilityCodes=searchHotel.newChild("FacilityCodes");
		// facilityCodes.newChild("FacilityCode","*SO");
		// searchHotel.newChild("OrderBy","pricelowtohigh");
		// searchHotel.newChild("NumberOfReturnedItems","10");
		System.err.println(request);
		newHttpPostRequest("https://interface.demo.octopustravel.com/rbsukapi/RequestListenerServlet");
		addHttpHeader("SOAPAction",
				"https://interface.demo.octopustravel.com/rbsukapi/RequestListenerServlet");
		addHttpHeader("Content-Type", "text/xml; charset=utf-8");
		setXmlContent("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r"
				+ request.toString());
		System.err.println(downloadXml("offer"));
	}

	public String formatStars(String stars) {
		stars = stars.substring(stars.indexOf("-"), stars.length()).replaceAll(
				"-", "");
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
		if (distanceInMeter.contains("Ca"))
			distanceInMeter = distanceInMeter.replace("Ca ", "");
		if (distanceInMeter.contains("ca"))
			distanceInMeter = distanceInMeter.replace("ca ", "");
		if (distanceInMeter.contains("km") && distanceInMeter.contains(",")) {
			distanceInMeter = distanceInMeter.replace(" km", "");
			if (distanceInMeter.contains("-")) {
				distanceInMeter = distanceInMeter.substring(distanceInMeter
						.indexOf("-") + 1);
			}
			try {
				distanceInMeter = String
						.valueOf(
								(Double.valueOf(distanceInMeter.replace(",",
										".")) * 1000)).replace(".0", "");
			} catch (Exception e) {
			}
		} else if (distanceInMeter.contains("km")
				&& !distanceInMeter.contains(",")) {
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

	private void apollo() throws Exception {
		Xml requestNode = new XmlNode("OTA_VehAvailRateRQ");
		requestNode.newAttribute("xmlns",
				"http://www.opentravel.org/OTA/2003/05");
		requestNode.newAttribute("xmlns:xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		requestNode.newAttribute("xsi:schemaLocation",
				"http://www.opentravel.org/OTA/2003/05 OTA_VehAvailRateRQ.xsd");
		requestNode.newAttribute("Target", "Test");
		requestNode.newAttribute("Version", "1.005");
		Xml requestLogin = requestNode.newChild("POS");
		Xml login = requestLogin.newChild("Source");
		login.newAttribute("ISOCurrency", "NOK");
		Xml loginValue = login.newChild("RequestorID");
		loginValue.newAttribute("Type", "16");
		loginValue.newAttribute("ID", "360367");
		loginValue.newAttribute("ID_Context", "CARTRAWLER");
		Xml availRQ = requestNode.newChild("VehAvailRQCore");
		availRQ.newAttribute("Status", "Available");
		Xml VehRentalCore = availRQ.newChild("VehRentalCore");
		VehRentalCore.newAttribute("PickUpDateTime", "2011-12-03T12:00:00");
		VehRentalCore.newAttribute("ReturnDateTime", "2011-12-05T12:00:00");
		Xml pickUpLocation = VehRentalCore.newChild("PickUpLocation");
		pickUpLocation.newAttribute("CodeContext", "IATA");
		pickUpLocation.newAttribute("LocationCode", "LGW");
		Xml returnLocation = VehRentalCore.newChild("ReturnLocation");
		returnLocation.newAttribute("CodeContext", "IATA");
		returnLocation.newAttribute("LocationCode", "LGW");
		Xml driverType = availRQ.newChild("DriverType");
		driverType.newAttribute("Age", "30");
		Xml vehAvailRQInfo = requestNode.newChild("VehAvailRQInfo");
		Xml customer = vehAvailRQInfo.newChild("Customer");
		Xml citizenCountryName = customer.newChild("Primary").newChild(
				"CitizenCountryName");
		citizenCountryName.newAttribute("Code", "NO");
		Xml TPA_Extensions = vehAvailRQInfo.newChild("TPA_Extensions");
		TPA_Extensions.newChild("ConsumerIP", IpAddress.getLocalAddress());
		newHttpPostRequest("http://otatest.cartrawler.com:20002/cartrawlerota");
		setXmlContent("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ requestNode.toString().replace("\r\n", ""));
		Xml carsAvails = downloadXml("CarsAvails");

	}

	private void travelcube() throws Exception {
		Xml request = new XmlNode("Request");
		Xml source = request.newChild("Source");
		Xml requestorId = source.newChild("RequestorID");
		requestorId.newAttribute("Client", "381");
		requestorId.newAttribute("EMailAddress", "XML@TRAVELFUSION.COM");
		requestorId.newAttribute("Password", "PASS");
		Xml requestor = source.newChild("RequestorPreferences");
		requestor.newAttribute("Language", "en");
		requestor.newChild("RequestMode", "SYNCHRONOUS");
		Xml requestDetails = request.newChild("RequestDetails");
		Xml itemRequest = requestDetails
				.newChild("ItemInformationDownloadRequest");
		itemRequest.newAttribute("ItemType", "hotel");

		// newHttpPostRequest("https://interface.demo.octopustravel.com/rbsukapi/RequestListenerServlet");
		// addHttpHeader("SOAPAction",
		// "https://interface.demo.octopustravel.com/rbsukapi/RequestListenerServlet");
		// setXmlContent("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r" +
		// request.toString());
		// System.err.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r" +
		// request.toString());

		// HttpURLConnection connection = (HttpURLConnection) new
		// URL("https://interface.demo.octopustravel.com/rbsukapi/RequestListenerServlet").openConnection();
		// connection.setRequestMethod("POST");
		// connection.setDoInput(true);
		// connection.setDoOutput(true);
		// connection.setUseCaches(true);
		// connection.setAllowUserInteraction(false);
		// connection.setRequestProperty("X-TFRequest", "true");
		// connection.setRequestProperty("Content-Type",
		// "application/download");
		// connection.getOutputStream().write(("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r"
		// + request.toString()).getBytes());
		// connection.getOutputStream().flush();
		// setHttpHeader("Content-Type", "application/download");
		// InputStream offer = connection.getInputStream();

		// GZIPInputStream gis = new GZIPInputStream(offer);
		// BufferedReader reader = new BufferedReader(new InputStreamReader(gis,
		// Charsets.UTF_8));
		// System.err.println(reader.readLine());

		newHttpPostRequest("https://interface.demo.octopustravel.com/rbsukapi/RequestListenerServlet");
		addHttpHeader("SOAPAction",
				"https://interface.demo.octopustravel.com/rbsukapi/RequestListenerServlet");
		addHttpHeader("Content-Type", "application/download");
		setXmlContent("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r"
				+ request.toString());
		// System.err.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r" +
		// request.toString());
		// newHttpGetRequest("https://interface.demo.octopustravel.com/rbsukapi/RequestListenerServlet?"+"<?xml version=\"1.0\" encoding=\"utf-8\"?>\r"
		// + request.toString());
		// setHttpHeader("Content-Encoding", "gzip");
		byte[] content = downloadHttpToByteArray("File").toByteArray();
		ByteArrayInputStream is = new ByteArrayInputStream(content);
		GZIPInputStream zis = new GZIPInputStream(is);
		InputStreamReader isr = new InputStreamReader(zis, UTF_8);
		BufferedReader br = new BufferedReader(isr);
		System.err.println(br.readLine());
		System.err.println(br.readLine());
		System.err.println(br.readLine());
		System.err.println(br.readLine());
	}

	private void qq() throws Exception {
		List<List> list = new ArrayList();
		List<Double> room1 = new ArrayList();
		room1.add(1d);
		room1.add(2d);
		List<Double> room2 = new ArrayList();
		room2.add(3d);
		room2.add(4d);
		room2.add(5d);
		// List <Double>room3 = new ArrayList();
		// room3.add(6d);
		// room3.add(7d);
		// room3.add(8d);
		list.add(room1);
		list.add(room2);
		// list.add(room3);

		List<String> price = new ArrayList();
		List<String> number = new ArrayList();

		for (int i = 0; i < list.size(); i++) {
			List room = list.get(i);
			if (i == 0) {
				for (int j = 0; j < room.size(); j++) {
					number.add(j + ",");
					price.add(room.get(j) + ",");
				}
			} else {
				List<String> var1 = number;
				List<String> var2 = price;
				number = new ArrayList();
				price = new ArrayList();
				for (int j = 0; j < room.size(); j++) {
					for (int k = 0; k < var1.size(); k++) {
						number.add(var1.get(k) + j + ",");
					}
					for (int k = 0; k < var2.size(); k++) {
						price.add(var2.get(k) + room.get(j) + ",");
					}
				}
			}
		}

		System.err.println("number=========");
		for (int i = 0; i < number.size(); i++) {
			System.err.println(number.get(i));
		}

		System.err.println("price=========");
		for (int i = 0; i < price.size(); i++) {
			System.err.println(price.get(i));
		}

	}

	private void makeBookingRequest() throws Exception {
		XmlNode soapEnvelope = new XmlNode("soapenv:Envelope");
		soapEnvelope.newAttribute("xmlns:soapenv",
				"http://schemas.xmlsoap.org/soap/envelope/");
		soapEnvelope.newAttribute("xmlns:ser",
				"http://www.laterooms.com/services");
		soapEnvelope.newChild("soapenv:Header");
		Xml body = soapEnvelope.newChild("soapenv:Body");
		Xml makeBooking = body.newChild("ser:makeBooking");
		Xml b = makeBooking.newChild("ser:b");
		Xml partner = b.newChild("ser:Partner");
		partner.newChild("ser:Id", "11247");
		partner.newChild("ser:Username", "Travelfusion");
		partner.newChild("ser:Password", "Xi46Ftk9MJ");

		Xml rooms = b.newChild("ser:Rooms");
		Xml room = rooms.newChild("ser:room");
		room.newChild("ser:Id", "1234");
		room.newChild("ser:GuestTitle", "Mr");
		room.newChild("ser:GuestInitials", "J");
		room.newChild("ser:GuestSurname", "Smith");
		room.newChild("ser:Adults", "1");
		room.newChild("ser:Children", "1");
		room.newChild("ser:BedType", "d");
		room.newChild("ser:Smoking", "true");
		room.newChild("ser:Cost", "99");

		b.newChild("ser:HotelId", "152584");
		b.newChild("ser:ArrivalDate", "20/11/2011");
		b.newChild("ser:Nights", "2");
		b.newChild("ser:Currency", "GBP");

		Xml guest = b.newChild("ser:Guest");
		guest.newChild("ser:Title", "Mr");
		guest.newChild("ser:Initials", "J");
		guest.newChild("ser:Surname", "Smith");
		guest.newChild("ser:Email", "jsmith@domain.com");
		guest.newChild("ser:Postcode", "XY441FF");
		guest.newChild("ser:Tel", "01241231234");
		guest.newChild("ser:CountryOfResidence", "1");
		guest.newChild("ser:SendEmailConfirmation", "true");

		Xml booker = b.newChild("ser:Booker");
		booker.newChild("ser:Title", "Mr");
		booker.newChild("ser:Initials", "J");
		booker.newChild("ser:Surname", "Smith");
		booker.newChild("ser:Email", "jsmith@domain.com");
		booker.newChild("ser:Postcode", "XY441FF");
		booker.newChild("ser:Tel", "01241231234");
		booker.newChild("ser:CountryOfResidence", "1");
		booker.newChild("ser:SendEmailConfirmation", "true");

		Xml paymentCard = b.newChild("ser:PaymentCard");
		paymentCard.newChild("ser:CardHolderName", "MR J SMITH");
		paymentCard.newChild("ser:CardType", "1");
		paymentCard.newChild("ser:CardNumber", "4444333322221111");
		paymentCard.newChild("ser:ExpiryDate", "05/07");
		paymentCard.newChild("ser:IssueNumberOrStartDate", "2");
		paymentCard.newChild("ser:SecurityCode", "184");

		b.newChild("ser:DoNotBook", "true");
		// b.newChild("ser:ReservationType", "1");
		newHttpPostRequest("http://xmlbooking.sandbox.laterooms.com?wsdl");
		addHttpHeader("Content-Type", "text/xml; charset=utf-8");
		addHttpHeader("SOAPAction",
				"http://www.laterooms.com/services/makeBooking");
		setXmlContent(soapEnvelope.toString().replace("\r\n", ""));
		System.err.println(downloadXml("Search"));
	}

	private void extractExcel4() throws Exception {
		newHttpGetRequest("http://www.csa.cz/en/portal/homepage/cz_homepage.htm");
		downloadHtmlFormList("home");
		newHttpGetRequest("http://www.csa.cz/en/external_shared/ext_link_booking.htm");
		addHttpGetField("password", "1");
		addHttpGetField("PRICER_PREF", "SCP2");
		addHttpGetField("AIRLINES", "ok");
		addHttpGetField("cabinPreference", "");
		addHttpGetField("next", "1");
		addHttpGetField("ID_LOCATION", "CZ");
		addHttpGetField("JOURNEY_TYPE", "RT");
		addHttpGetField("DEP_0", "Oslo, Gardermoen, Norway (OSL)");
		addHttpGetField("ARR_0", "Ostrava, Mosnov, Czech Republic (OSR)");
		addHttpGetField("DEP_1", "");
		addHttpGetField("ARR_1", "");
		addHttpGetField("DAY_0", "7");
		addHttpGetField("MONTH_SEL_0", "12/2011");
		addHttpGetField("DAY_1", "15");
		addHttpGetField("MONTH_SEL_1", "12/2011");
		addHttpGetField("ADTCOUNT", "1");
		addHttpGetField("YTHCOUNT", "0");
		addHttpGetField("CHDCOUNT", "0");
		addHttpGetField("INFCOUNT", "0");
		downloadHtmlFormList("results");
		selectText("meta http-equiv=\"Refresh\"", "</");
		extractText(null, "content=\"0;");
		String url = getUrl(extractText("URL=", "\""));
		deselectText();
		String sid = url.substring(url.indexOf("=") + 1);
		System.err.println(sid);
		// newHttpGetRequest("http://secure.csa.cz/en/ibs/ajaxSectorItineraryOffer.php?sector=0&fareOfferData=0&sid="+sid+"&userFareOfferData=0"+"&fareOfferData=0"+"&fareId1=0"+"&fareId0=0");
		// downloadHttp("results2");
		newHttpGetRequest("http://secure.csa.cz/en/ibs/ajaxSectorCalendarOffer.php?sector=0&fareOfferData=0&sid="
				+ sid + "&userFareOfferData=0");
		Xml results2 = downloadXml("results2");
		String description2 = results2.getChild("page").getChild("description")
				.toString();
		System.err.println(description2);
		selectText(description2);
		String value1 = "";
		while (selectText("requestCalendarOfferData", ";", OPTIONAL)) {
			String offer = extractText("'", "'");
			if (offer.contains("2011-12-07"))
				value1 = offer;
			deselectText();
		}
		deselectText();
		System.err.println(value1);

		String value2 = "";
		newHttpGetRequest("http://secure.csa.cz/en/ibs/ajaxSectorCalendarOffer.php?sector=1&fareOfferData="
				+ value1 + "&sid=" + sid + "&userFareOfferData=" + value1);
		Xml results3 = downloadXml("results3");
		String description3 = results3.getChild("page").getChild("description")
				.toString();
		System.err.println(description3);
		selectText(description3);
		while (selectText("name=\"calendarDay1\" value=", ">", OPTIONAL)) {
			String offer = extractText("\"", "\"");
			if (offer.contains("2011-12-15"))
				value2 = offer;
			deselectText();
		}
		deselectText();
		newHttpGetRequest("http://secure.csa.cz/en/ibs/ajaxSectorItineraryOffer.php?fareId1="
				+ value2 + "&sector=0" + "&fareId0=" + value1 + "&sid=" + sid);
		Xml finalOffer = downloadXml("finalOffer");
		// System.err.println(finalOffer);
		String outwardOffer = finalOffer.getChild("page")
				.getChild("description").toString();
		selectText(outwardOffer);
		String itineraryId = "";
		String flag = "<input type=";
		try {
			while (selectText("\"radio\" name=\"flightId_0\"", flag, OPTIONAL)) {

				selectText("<strong>", "</strong>");
				String price = extractText().replaceAll("?", "").replaceAll(
						"&nbsp", "");
				System.err.println(price);
				deselectText();

				while (selectText("switchDetailFlightInfo",
						"javascript:dummy()", OPTIONAL)) {
					if (extractText().contains("<h3>")) {
						selectText("<h3>", "</h3>");
						String flightNo = extractText().replaceAll("?", "")
								.replaceAll("&nbsp", "");
						;
						deselectText();

						selectText("<td>", "</td>");
						String departure = extractText("(", ")");
						deselectText();

						selectText("<td>", "</td>");
						String departureTime = extractText(null, ",");
						deselectText();

						selectText("<td>", "</td>");
						String arrival = extractText("(", ")");
						deselectText();

						selectText("<td>", "</td>");
						String arrivalTime = extractText(null, ",");
						deselectText();

						System.err.println(flightNo + "  " + departure + "  "
								+ departureTime + "  " + arrival + "  "
								+ arrivalTime);
					}
					deselectText();
				}
				selectText("requestItineraryOfferData", "pngfix");
				if (extractText().contains("checked")) {
					itineraryId = extractText("'", "'");
					System.err.println(itineraryId);
				}
				deselectText();

				deselectText();

				if (containsText("\"radio\" name=\"flightId_0\"", flag, NO_MOVE
						| OPTIONAL)) {

				} else {
					flag = null;
				}

			}
		} catch (Exception e) {
		}
		newHttpGetRequest("http://secure.csa.cz/en/ibs/ajaxSectorItineraryOffer.php?fareId1="
				+ value2
				+ "&itineraryId0="
				+ itineraryId
				+ "&sector=1"
				+ "&fareId0=" + value1 + "&sid=" + sid);
		Xml finalOffer2 = downloadXml("finalOffer");
		// System.err.println(finalOffer);
		String retwardOffer = finalOffer2.getChild("page")
				.getChild("description").toString();
		selectText(retwardOffer);
		// System.err.println(retwardOffer);
		flag = "<input type=";
		try {
			while (selectText("\"radio\" name=\"flightId_1\"", flag, OPTIONAL)) {
				selectText("<strong>", "</strong>");
				String price = extractText().replaceAll("?", "").replaceAll(
						"&nbsp", "");
				System.err.println(price);
				deselectText();

				while (selectText("switchDetailFlightInfo",
						"javascript:dummy()", OPTIONAL)) {
					if (extractText().contains("<h3>")) {
						selectText("<h3>", "</h3>");
						String flightNo = extractText().replaceAll("?", "")
								.replaceAll("&nbsp", "");
						;
						deselectText();

						selectText("<td>", "</td>");
						String departure = extractText("(", ")");
						deselectText();

						selectText("<td>", "</td>");
						String departureTime = extractText(null, ",");
						deselectText();

						selectText("<td>", "</td>");
						String arrival = extractText("(", ")");
						deselectText();

						selectText("<td>", "</td>");
						String arrivalTime = extractText(null, ",");
						deselectText();

						System.err.println(flightNo + "  " + departure + "  "
								+ departureTime + "  " + arrival + "  "
								+ arrivalTime);
					}
					deselectText();
				}
				deselectText();
				if (containsText("\"radio\" name=\"flightId_1\"", flag, NO_MOVE
						| OPTIONAL)) {

				} else {
					flag = null;
				}

			}
		} catch (Exception e) {
		}
		deselectText();

	}

	// private void extractExcel2() throws Exception{
	// try {
	// Workbook book = Workbook.getWorkbook(new
	// File("C:\\Users\\kimi\\LongLat.xls"));
	// Sheet sheet = book.getSheet(0);
	// List<String> correctRecords = new ArrayList();
	// List<String> incorrectRecords = new ArrayList();
	// Map<String,String> codeMap = new HashMap<String,String>();
	// for (int i = 1; i < sheet.getRows(); i++) {
	// StringBuffer sb = new StringBuffer();
	// String line = sheet.getCell(0, i).getContents().trim().replace("||", "");
	// try{
	// String[] split = Text.split(line, "|");
	// if(codeMap.containsKey(split[5])){
	// String str=codeMap.get(split[5]).toString();
	// codeMap.put(split[5], str+","+split[0]);
	// }else{
	// codeMap.put(split[5], split[0]);
	// }
	//
	//
	// //
	// System.err.println("regionIdMap.put(\""+split[0]+"\",\""+split[5]+"\")");
	// }catch (Exception e) {
	// continue;
	// }
	// }
	// Iterator<String> iter=codeMap.keySet().iterator();
	// while(iter.hasNext())
	// {
	// System.err.println("regionIdMap.put(\""+iter.next()+"\",\""+codeMap.get(iter.next())+"\")");
	// }
	//
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//

	// }
	private void thirtysix() throws Exception {

		newHttpGetRequest("http://distribution-xml.booking.com/xml/bookings.getHotelFacilities");
		addHttpHeader("Authorization", "Basic ZmlubnJlaXNlYWc6czllQTY=");
		addHttpGetField("hotel_ids", "16800");
		Xml hotelFacilitiesResults = null;
		hotelFacilitiesResults = downloadXml("HotelFacilityResults");

		// newHttpGetRequest("http://distribution-xml.booking.com/xml/bookings.getHotels");
		// setHttpCharset(UTF_8);
		//
		// addHttpHeader("Authorization", "Basic ZmlubnJlaXNlYWc6czllQTY=");
		// addHttpGetField("countrycodes", "cn");
		// addHttpGetField("offset", "2000");

		// Xml hotels=downloadXml("gethotels"+"ad");
		// System.err.println(hotels.children("result"));
		// boolean flag=true;
		// int i=0;
		// while(flag){
		//
		// Xml hotels3=runCity(i);
		// System.err.println(hotels3.children("result"));
		// if(hotels3.children("result")<1000)
		// flag=false;
		// else{
		// i=i+1001;
		// }
		// }
		// newHttpGetRequest("http://distribution-xml.booking.com/xml/bookings.getCountries");
		// setHttpCharset(UTF_8);
		// addHttpHeader("Authorization", "Basic ZmlubnJlaXNlYWc6czllQTY=");
		// addHttpGetField("rows", "1000");
		// addHttpGetField("languagecode", "en");
		// addHttpGetField("offset", "0");
		//
		// Xml country=downloadXml("getCountries"+"0");
		// System.err.println(country.children("result"));
		//
		// newHttpGetRequest("http://distribution-xml.booking.com/xml/bookings.getCountries");
		// setHttpCharset(UTF_8);
		// addHttpHeader("Authorization", "Basic ZmlubnJlaXNlYWc6czllQTY=");
		// addHttpGetField("rows", "1000");
		// addHttpGetField("languagecode", "en");
		// addHttpGetField("offset", "1000");
		// Xml country2=downloadXml("getCountries"+"1000");
		// System.err.println(country2.children("result"));

		// newHttpGetRequest("http://distribution-xml.booking.com/xml/bookings.getCities");
		// setHttpCharset(UTF_8);
		//
		// addHttpHeader("Authorization", "Basic ZmlubnJlaXNlYWc6czllQTY=");
		// Xml city=downloadXml("getCities");
		// System.err.println(city.children("result"));

		// newHttpGetRequest("http://distribution-xml.booking.com/xml/bookings.getHotels");
		// setHttpCharset(UTF_8);

		// addHttpHeader("Authorization", "Basic ZmlubnJlaXNlYWc6czllQTY=");
		// addHttpGetField("city_id", "-3794558");
		// addHttpGetField("hotel_ids", "88735");
		// Xml hotels=downloadXml("getHotels"+"88735");
		// System.err.println(hotels.children("result"));

		// addHttpGetField("offset", "2000");
		// newHttpGetRequest("http://distribution-xml.booking.com/xml/bookings.getHotelPhotos");
		// setHttpCharset(UTF_8);
		//
		// addHttpHeader("Authorization", "Basic ZmlubnJlaXNlYWc6czllQTY=");
		// addHttpGetField("hotel_ids", "16848");
		// downloadXml("getHotelPhotos");
		//
		// newHttpGetRequest("http://distribution-xml.booking.com/xml/bookings.getHotelDescriptionTranslations");
		// setHttpCharset(UTF_8);
		//
		// addHttpHeader("Authorization", "Basic ZmlubnJlaXNlYWc6czllQTY=");
		// addHttpGetField("hotel_ids", "16848");
		// downloadXml("getHotelDescription");

	}

	private Xml runCity(int index) throws Exception {
		newHttpGetRequest("http://distribution-xml.booking.com/xml/bookings.getHotels");
		setHttpCharset(UTF_8);

		addHttpHeader("Authorization", "Basic ZmlubnJlaXNlYWc6czllQTY=");
		addHttpGetField("countrycodes", "cn");
		addHttpGetField("rows", "1000");
		addHttpGetField("offset", index);
		return downloadXml("gethotels" + "cn" + index);
	}

	private void thirtyfive() throws IOException, HttpException,
			InterruptedException {
		XmlNode soap = new XmlNode("soapenv:Envelope");
		soap.newAttribute("xmlns:soapenv",
				"http://schemas.xmlsoap.org/soap/envelope/");
		soap.newAttribute("xmlns:ivec", "http://ivectorbookingxml/");
		soap.newChild("soapenv:Header");
		Xml soapBody = soap.newChild("soapenv:Body");
		Xml details = soapBody.newChild("ivec:PropertyDetails");
		Xml detailsRequest = details.newChild("ivec:PropertyDetailsRequest");
		Xml login = detailsRequest.newChild("ivec:LoginDetails");
		login.newChild("ivec:Login", "swedentestxml");
		login.newChild("ivec:Password", "test");
		login.newChild("ivec:AgentReference", "1234");
		detailsRequest.newChild("ivec:PropertyID", "4836");
		newHttpPostRequest("http://xml.lowcostgroup.com/soap/book.asmx");
		addHttpHeader("Content-Type", "text/xml; charset=utf-8");
		addHttpHeader("SOAPAction", "http://ivectorbookingxml/PropertyDetails");
		setXmlContent(soap.toString().replace("\r\n", ""));
		Xml hotelInfo = downloadXml("hotelInfo");
		System.err.println(hotelInfo);
	}

	private String getOperator(String operator) throws Exception {
		if (operator.equals("TurkishAirlines") || operator.equals("THY")) {
			return "Turkish Airlines";
		}
		if ("THY*".equals(operator)) {
			return "AnadoluJet";
		}
		if ("Anadolu Jet*".equals(operator)) {
			return "AnadoluJet";
		}
		if ("Saga Airlines Aircraft".equals(operator)) {
			return "Saga Airlines";
		}
		if ("AJET".equals(operator)) {
			return "AnadoluJet";
		}
		return operator;
	}

	private void thirtyfour() throws Throwable {

		ArrayList<String> coordsList = new ArrayList<String>();
		cacheHotels(coordsList);
		StringBuilder nfdFile = new StringBuilder();
		for (int index = 0; index < coordsList.size(); index++)
			nfdFile.append(coordsList.get(index)).append(TextFile.newLine);
		System.err.println(nfdFile.toString());
	}

	private void cacheHotels(ArrayList<String> coordsList) throws Throwable {
		String[] countries = cacheCountries();
		for (int i = 0; i < countries.length; i++) {
			String countryCode = countries[i];
			String[] cities = cacheCities(countryCode);
			for (int n = 0; n < cities.length; i++) {
				String cityCode = cities[n];
				cacheHotels(cityCode, coordsList);

			}
		}
	}

	private String[] cacheCountries() throws Throwable {
		newHttpGetRequest(" http://stageapi.scandichotels.com/XMLAPI/V1/countries");
		addHttpGetField("language", "no");
		addHttpHeader("APIKey", "8ca6b565-3428-4a22-86a8-6980e5400211");
		Xml countriesXml = downloadXml("countries");
		int size = countriesXml.getChild("Countries").children();
		String[] countries = new String[size];
		int i = 0;
		for (Xml iCountry : countriesXml.getChild("Countries").childIterator(
				"Country")) {
			String countryCode = iCountry.getChild("CountryId").getValue()
					.toString();
			String countryName = iCountry.getChild("CountryName").getValue()
					.toString();
			String Continent = iCountry.getChild("Continent").getValue()
					.toString();
			// put(countryCode, countryName + "|" + Continent);
			countries[i] = countryCode;
			i++;
		}
		return countries;
	}

	private String[] cacheCities(String countryCode) throws Throwable {
		newHttpGetRequest(" http://stageapi.scandichotels.com/XMLAPI/V1/cities");
		addHttpGetField("country", countryCode);
		addHttpGetField("language", "no");
		addHttpHeader("APIKey", "8ca6b565-3428-4a22-86a8-6980e5400211");
		Xml citiesXml = downloadXml("cities");
		int size = citiesXml.getChild("MarketingCities").children();
		String[] cities = new String[size];
		int i = 0;
		for (Xml iCity : citiesXml.getChild("MarketingCities").childIterator(
				"MarketingCity")) {
			String cityCode = iCity.getChild("MarketingCityId").getValue()
					.toString();
			String cityName = iCity.getChild("MarketingCityName").getValue()
					.toString();
			// put(cityCode, cityName);
			cities[i] = cityCode;
			i++;
		}
		return cities;
	}

	private void cacheHotels(String cityCode, ArrayList<String> coordsList)
			throws Throwable {
		newHttpGetRequest(" http://stageapi.scandichotels.com/XMLAPI/V1/hotels");
		addHttpGetField("city", cityCode);
		addHttpGetField("language", "no");
		addHttpHeader("APIKey", "8ca6b565-3428-4a22-86a8-6980e5400211");
		Xml hotels = downloadXml("Hotels");
		for (Xml iHotel : hotels.getChild("Hotels").childIterator("Hotel")) {
			String hotelCode = iHotel.getChild("HotelId").getValue().toString();
			newHttpGetRequest(" http://stageapi.scandichotels.com/XMLAPI/V1/hotel");
			addHttpGetField("id", hotelCode);
			addHttpGetField("language", "no");
			addHttpHeader("APIKey", "8ca6b565-3428-4a22-86a8-6980e5400211");
			Xml hotel = downloadXml("Hotels" + hotelCode);
			try {
				String lon = hotel.getChild("Hotel")
						.getChild("HotelCoordinates").getChild("Longitude")
						.getValue().toString();
				String lat = hotel.getChild("Hotel")
						.getChild("HotelCoordinates").getChild("Latitude")
						.getValue().toString();
				// put(hotelCode,hotel.toString());

				coordsList.add(hotelCode + "\t" + lat + "," + lon);
			} catch (Exception e) {
				continue;
			}

		}
	}

	private void extractExcel() {
		// Map<String,String> map=new HashMap<String, String>();
		// map.put("AFGHANISTAN","AF");
		//
		// try {
		// Workbook book = Workbook.getWorkbook(new
		// File("C:\\Users\\kimi\\Master Catalogue_13_12_11 Finn.xls"));
		// Sheet sheet = book.getSheet(0);
		// List<String> correctRecords = new ArrayList();
		// List<String> incorrectRecords = new ArrayList();
		//
		// for (int i = 1; i < sheet.getRows(); i++) {
		// StringBuffer sb = new StringBuffer();
		// String geoname_id = sheet.getCell(2, i).getContents().trim();
		// String country=sheet.getCell(5, i).getContents().trim();
		// String country_code = sheet.getCell(6, i).getContents().trim();
		// // System.err.println(country+"  "+country_code);
		// if(country_code.equals("")||country_code==null)
		// {
		// try{
		// country_code=map.get(country.toUpperCase()).toString();
		// }catch (Exception e) {
		// System.err.println(country);
		// }
		// }else {
		//
		// }
		// String latitude = sheet.getCell(11, i).getContents().trim();
		// String lognitude = sheet.getCell(12, i).getContents().trim();
		//
		// if(latitude.contains(","))
		// latitude=latitude.replace(",", ".");
		// if(lognitude.contains(","))
		// lognitude=lognitude.replace(",", ".");
		//
		// try {
		// Double.parseDouble(latitude);
		// Double.parseDouble(lognitude);
		// sb.append(geoname_id).append("_").append(country_code).append("\t").append(latitude).append(",").append(lognitude);
		// correctRecords.add(sb.toString());
		// } catch (Exception e) {
		// e.printStackTrace();
		// sb.append("row:").append(i).append("\t").append(geoname_id).append("\t").append(latitude).append("\t").append(lognitude);
		// incorrectRecords.add(sb.toString());
		// }
		// }
		//
		// FileWriter correct = new FileWriter("C:\\Users\\kimi\\correct.txt");
		// PrintWriter out = new PrintWriter(new BufferedWriter(correct));
		// for (int i = 0; i < correctRecords.size(); i++) {
		// out.println(correctRecords.get(i));
		// }
		// out.close();
		//
		// FileWriter incorrect = new
		// FileWriter("C:\\Users\\kimi\\incorrect.txt");
		// PrintWriter out2 = new PrintWriter(new BufferedWriter(incorrect));
		// for (int i = 0; i < incorrectRecords.size(); i++) {
		// out2.println(incorrectRecords.get(i));
		// }
		// out2.close();
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	private void thirtythree() throws Exception {
		Xml root = new XmlNode("GetOffer");
		root.newAttribute("Version", "1.0");
		Xml products = root.newChild("Products");
		products.newChild("Product", "hotel");

		Xml search = root.newChild("SearchCriteria");
		search.newChild("StartDate", "2011-10-01");
		search.newChild("EndDate", "2011-10-03");
		Xml origin = search.newChild("OriginLocation");
		origin.newAttribute("LocationCode", "CPH");
		origin.newAttribute("CodeContext", "IATA");
		Xml destination = search.newChild("DestinationLocation");
		destination.newAttribute("LocationCode", "CPH");
		destination.newAttribute("CodeContext", "IATA");
		Xml passenger = search.newChild("PassengerInfo");
		passenger.newAttribute("Adults", "1");
		// passenger.newAttribute("Children", "1");
		// passenger.newAttribute("ChildrenAges", "5");
		Xml requester = search.newChild("Requester");
		requester.newAttribute("UserName", "destination");
		requester.newAttribute("PassWord", "ns94Ueff");
		requester.newAttribute("Site", "resfeber.se");
		newHttpPostRequest("http://www.travelocitynordic.com/services/valid_offers/get_products.cgi");
		setHttpCharset(UTF_8);
		setXmlContent(root);
		Xml information = downloadXml("reisefeber.xml");
		// System.err.println(information);

	}

	private void thirtytwo() throws Exception {
		XmlNode soap = new XmlNode("soapenv:Envelope");
		soap.newAttribute("xmlns:soapenv",
				"http://schemas.xmlsoap.org/soap/envelope/");
		soap.newAttribute("xmlns:xhi", "http://www.venere.com/XHI");
		Xml soapHeader = soap.newChild("soapenv:Header");
		Xml auth = soapHeader.newChild("xhi:Authentication");
		auth.newChild("xhi:UserOrgID", "Travelfusion");
		auth.newChild("xhi:UserID", "user_srv");
		auth.newChild("xhi:UserPSW", "Jk7hT5rT65HuIyt");

		Xml soapBody = soap.newChild("soapenv:Body");
		Xml hotelAvail = soapBody.newChild("xhi:XHI_HotelAvailRQ");
		hotelAvail.newAttribute("start", "2011-11-08");
		hotelAvail.newAttribute("end", "2011-11-10");
		hotelAvail.newAttribute("numGuests", "2");
		hotelAvail.newAttribute("numRooms", "1");
		hotelAvail.newAttribute("avoidCache", "false");
		hotelAvail.newAttribute("msgEchoToken", "test");

		Xml query = hotelAvail.newChild("xhi:AvailQueryByProperty");
		query.newAttribute("bookingMethods", "IB");
		query.newAttribute("propertyIDs", "220429");

		Xml format = hotelAvail.newChild("xhi:AvailResultFormat");
		format.newAttribute("orderBy", "category");
		format.newAttribute("showPropertyDetails", "true");
		format.newAttribute("showDailyRates", "true");
		format.newAttribute("showRoomCancellationPolicies", "true");
		format.newAttribute("langID", "fr");

		newHttpPostRequest("https://b2b-uat.venere.com/xhi-1.0/services/XHI_HotelAvail.soap");
		addHttpHeader("Content-Type", "text/xml; charset=utf-8");
		addHttpHeader("SOAPAction",
				"https://b2b-uat.venere.com/xhi-1.0/services/XHI_HotelAvail.soap");
		setXmlContent(soap.toString().replace("\r\n", ""));
		Xml a = downloadXml("test");
		System.err.println(a);
	}

	private void thirtyone() throws Exception {
		XmlNode soap = new XmlNode("soapenv:Envelope");
		soap.newAttribute("xmlns:soapenv",
				"http://schemas.xmlsoap.org/soap/envelope/");
		soap.newAttribute("xmlns:xhi", "http://www.venere.com/XHI");
		Xml soapHeader = soap.newChild("soapenv:Header");
		Xml auth = soapHeader.newChild("xhi:Authentication");
		auth.newChild("xhi:UserOrgID", "Travelfusion");
		auth.newChild("xhi:UserID", "user_srv");
		auth.newChild("xhi:UserPSW", "Jk7hT5rT65HuIyt");
		Xml soapBody = soap.newChild("soapenv:Body");
		Xml ping = soapBody.newChild("xhi:XHI_PingRQ");
		ping.newAttribute("msgEchoToken", "REQ.001");
		ping.newAttribute("echoData", "Hello, world!");
		newHttpPostRequest("https://b2b-uat.venere.com/xhi-1.0/services/XHI_Ping.soap");
		addHttpHeader("Content-Type", "text/xml; charset=utf-8");
		addHttpHeader("SOAPAction",
				"https://b2b-uat.venere.com/xhi-1.0/services/XHI_Ping.soap");
		setXmlContent(soap.toString().replace("\r\n", ""));
		downloadXml("test");
	}

	private void thirty() throws Exception {
		XmlNode soap = new XmlNode("soap:Envelope");
		soap.newAttribute("xmlns:xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		soap.newAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
		soap.newAttribute("xmlns:soap",
				"http://schemas.xmlsoap.org/soap/envelope/");
		Xml soapBody = soap.newChild("soap:Body");
		Xml login = soapBody.newChild("GetListing");
		login.newAttribute("xmlns", "http://ferieboliger.no/API/");
		login.newChild("partnerId", "1");
		login.newChild("listingId", "34");

		newHttpPostRequest("http://hhforyou.com/API/Commands.asmx");
		addHttpHeader("Content-Type", "text/xml; charset=utf-8");
		addHttpHeader("SOAPAction", "http://ferieboliger.no/API/GetListing");
		setXmlContent(soap.toString().replace("\r\n", ""));
		downloadXml("getListing");

		// XmlNode soap2 = new XmlNode("soap:Envelope");
		// soap2.newAttribute("xmlns:xsi",
		// "http://www.w3.org/2001/XMLSchema-instance");
		// soap2.newAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
		// soap2.newAttribute("xmlns:soap",
		// "http://schemas.xmlsoap.org/soap/envelope/");
		// Xml soapBody2 = soap2.newChild("soap:Body");
		// Xml search = soapBody2.newChild("GetListingsWithModificationDates");
		// search.newAttribute("xmlns", "http://ferieboliger.no/API/");
		// search.newChild("partnerId","1");
		// newHttpPostRequest("http://hhforyou.com/API/Commands.asmx");
		// addHttpHeader("Content-Type", "text/xml; charset=utf-8");
		// addHttpHeader("SOAPAction",
		// "http://ferieboliger.no/API/GetListingsWithModificationDates");
		// setXmlContent(soap2.toString().replace("\r\n", ""));
		// downloadXml("getSearch");
		//
		XmlNode soap3 = new XmlNode("soap:Envelope");
		soap3.newAttribute("xmlns:xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		soap3.newAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
		soap3.newAttribute("xmlns:soap",
				"http://schemas.xmlsoap.org/soap/envelope/");
		Xml soapBody3 = soap3.newChild("soap:Body");
		Xml prices = soapBody3.newChild("GetListingBooking");
		prices.newAttribute("xmlns", "http://ferieboliger.no/API/");
		prices.newChild("partnerId", "1");
		prices.newChild("listingId", "34");

		newHttpPostRequest("http://hhforyou.com/API/Commands.asmx");
		addHttpHeader("Content-Type", "text/xml; charset=utf-8");
		addHttpHeader("SOAPAction",
				"http://ferieboliger.no/API/GetListingBooking");
		setXmlContent(soap3.toString().replace("\r\n", ""));
		downloadXml("GetListingBooking");

		XmlNode soap4 = new XmlNode("soap:Envelope");
		soap4.newAttribute("xmlns:xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		soap4.newAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
		soap4.newAttribute("xmlns:soap",
				"http://schemas.xmlsoap.org/soap/envelope/");
		Xml soapBody4 = soap4.newChild("soap:Body");
		Xml prices2 = soapBody4.newChild("GetListingPrices");
		prices2.newAttribute("xmlns", "http://ferieboliger.no/API/");
		prices2.newChild("partnerId", "1");
		prices2.newChild("listingId", "34");

		newHttpPostRequest("http://hhforyou.com/API/Commands.asmx");
		addHttpHeader("Content-Type", "text/xml; charset=utf-8");
		addHttpHeader("SOAPAction",
				"http://ferieboliger.no/API/GetListingPrices");
		setXmlContent(soap4.toString().replace("\r\n", ""));
		downloadXml("GetListingPrices");

	}

	private void twentyningth() throws Exception {

		Xml bookingDetail = new XmlNode("BookingDetail");
		Xml partner = bookingDetail.newChild("Partner");
		partner.newChild("id", "11247");
		partner.newChild("Username", "naama@travelfusion.com");
		partner.newChild("Password", "laterooms");
		Xml guest = bookingDetail.newChild("Guest");
		guest.newChild("Title", "Mr");
		guest.newChild("Initials", "J");
		guest.newChild("Surname", "Smith");// O
		guest.newChild("Email", "jsmith@domain.com");
		guest.newChild("Postcode", "XY441FF");
		guest.newChild("Tel", "01241231234");
		guest.newChild("CountryOfResidence", "1");
		guest.newChild("SendEmailConfirmation", "true");
		Xml booker = bookingDetail.newChild("Booker");
		booker.newChild("Title", "Mr");
		booker.newChild("Initials", "J");
		booker.newChild("Surname", "Smith");// O
		booker.newChild("Email", "jsmith@domain.com");
		booker.newChild("Postcode", "XY441FF");
		booker.newChild("Tel", "01241231234");
		booker.newChild("CountryOfResidence", "1");
		booker.newChild("SendEmailConfirmation", "true");

		Xml rooms = bookingDetail.newChild("Rooms");
		Xml room = rooms.newChild("room");
		room.newChild("Id", "1234");
		room.newChild("GuestTitle", "Mr");
		room.newChild("GuestInitials", "J");
		room.newChild("GuestSurname", "Smith");
		room.newChild("Adults", "1");
		room.newChild("Children", "1");
		room.newChild("BedType", "d");
		room.newChild("Smoking", "true");
		room.newChild("Cost", "99");
		Xml paymentCard = bookingDetail.newChild("PaymentCard");
		paymentCard.newChild("CardHolderName", "MR J SMITH");
		paymentCard.newChild("CardType", "1");
		paymentCard.newChild("CardNumber", "4444333322221111");
		paymentCard.newChild("ExpiryDate", "05/07");
		paymentCard.newChild("IssueNumberOrStartDate", "2");
		paymentCard.newChild("SecurityCode", "184");
		bookingDetail.newChild("HotelId", "152584");
		bookingDetail.newChild("ArrivalDate", "20/11/2011");
		bookingDetail.newChild("Nights", "2");
		bookingDetail.newChild("Currency", "GBP");
		bookingDetail.newChild("DoNotBook", "true");
		bookingDetail.newChild("SpecialRequests", "");
		bookingDetail.newChild("ReservationType", "1");
		newHttpPostRequest("http://xmlbookingwrapper.sandbox.laterooms.com");
		setXmlContent("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r"
				+ bookingDetail.toString());
		System.err.println(downloadXml("booking"));

	}

	private void twentyeight() throws Exception {
		XmlNode soap = new XmlNode("soap:Envelope");
		soap.newAttribute("xmlns:xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		soap.newAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
		soap.newAttribute("xmlns:soap",
				"http://schemas.xmlsoap.org/soap/envelope/");
		Xml soapBody = soap.newChild("soap:Body");
		Xml login = soapBody.newChild("GetListing");
		login.newAttribute("xmlns", "http://ferieboliger.no/API/");
		login.newChild("listingId", "");
		login.newChild("partnerId", "");
		newHttpPostRequest("http://ferieboliger.no/API/Commands.asmx");
		addHttpHeader("Content-Type", "text/xml; charset=utf-8");
		addHttpHeader("SOAPAction", "http://ferieboliger.no/API/GetListing");
		setXmlContent(soap.toString().replace("\r\n", ""));
		downloadXml("getListing");
	}

	private void twentyseven() throws Exception {
		long time = 1309907940000L;
		Date date = new Date(time);
		String returnArrivalDate = new UtilDate("dd-MMM-yyyy HH:mm:ss",
				date.toLocaleString()).toString("dd/MM/yyyy-HH:mm");
		System.err.println(returnArrivalDate);
		returnArrivalDate = new UtilDate("dd-MMM-yyyy HH:mm:ss",
				"06-Jul-2011 07:19:00").toString("dd/MM/yyyy-HH:mm");
		System.err.println(returnArrivalDate);

	}

	private void twentysix() throws Exception {
		Xml bookingDetail = new XmlNode("BookingDetail");
		Xml partner = bookingDetail.newChild("Partner");
		partner.newChild("id", "11247");
		partner.newChild("Username", "naama@travelfusion.com");
		partner.newChild("Password", "Xi46Ftk9MJ");
		Xml guest = bookingDetail.newChild("Guest");
		guest.newChild("Title", "Mr");
		guest.newChild("Initials", "J");
		guest.newChild("Surname", "Smith");// O
		guest.newChild("Email", "jsmith@domain.com");
		guest.newChild("Postcode", "XY441FF");
		guest.newChild("Tel", "01241231234");
		guest.newChild("CountryOfResidence", "1");
		guest.newChild("SendEmailConfirmation", "true");
		Xml booker = bookingDetail.newChild("Booker");
		booker.newChild("Title", "Mr");
		booker.newChild("Initials", "J");
		booker.newChild("Surname", "Smith");// O
		booker.newChild("Email", "jsmith@domain.com");
		booker.newChild("Postcode", "XY441FF");
		booker.newChild("Tel", "01241231234");
		booker.newChild("CountryOfResidence", "1");
		booker.newChild("SendEmailConfirmation", "true");

		Xml rooms = bookingDetail.newChild("Rooms");
		Xml room = rooms.newChild("room");
		room.newChild("Id", "1234");
		room.newChild("GuestTitle", "Mr");
		room.newChild("GuestInitials", "J");
		room.newChild("GuestSurname", "Smith");
		room.newChild("Adults", "1");
		room.newChild("Children", "1");
		room.newChild("BedType", "d");
		room.newChild("Smoking", "true");
		room.newChild("Cost", "99");
		Xml paymentCard = bookingDetail.newChild("PaymentCard");
		paymentCard.newChild("CardHolderName", "MR J SMITH");
		paymentCard.newChild("CardType", "1");
		paymentCard.newChild("CardNumber", "4444333322221111");
		paymentCard.newChild("ExpiryDate", "05/07");
		paymentCard.newChild("IssueNumberOrStartDate", "2");
		paymentCard.newChild("SecurityCode", "184");
		bookingDetail.newChild("HotelId", "987654");
		bookingDetail.newChild("ArrivalDate", "20/11/2011");
		bookingDetail.newChild("Nights", "2");
		bookingDetail.newChild("Currency", "GBP");
		bookingDetail.newChild("DoNotBook", "true");
		bookingDetail.newChild("SpecialRequests", "");
		bookingDetail.newChild("ReservationType", "1");
		newHttpPostRequest("http://xmlbookingwrapper.sandbox.laterooms.com");
		setXmlContent("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r"
				+ bookingDetail.toString());
		System.err.println(downloadXml("booking").toString());
	}

	private void twentyfive() throws Exception {
	}

	private void twentyfour() throws Exception {
	}

	private void one() throws Exception {
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
