package test;

import core.io.file.TextFile;
import core.net.IpAddress;
import core.xml.Xml;
import core.xml.XmlNode;
import engine.WebSpider;


public class GoteborgNFD extends WebSpider{
	private static String clientSessionId = null;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	try {
		System.err.println("start");
		new GoteborgNFD().generate();
		System.err.println("end");
	} catch (Throwable e) {
		e.printStackTrace();
	}

	}
	public void generate() throws Throwable {
		 TextFile mapping = new TextFile("E://nfd2//goteborg.nfd");
		 mapping.delete();
		Xml producttyGroupGeo = exportProducttypeGroupGeo("3635");
		Xml exportProducttypeGroupGeoResult = producttyGroupGeo.getChild("soap:Body").getChild("ExportProducttypeGroupGeoResponse").getChild("ExportProducttypeGroupGeoResult");
		for (int j = 0; j < exportProducttypeGroupGeoResult.children("XmlExportProducttypeGroup"); j++) {
			Xml xmlExportProducttypeGroup = exportProducttypeGroupGeoResult.getChild(j);
			String hotelId = xmlExportProducttypeGroup.getChild("ProducttypeGroupId").getValue().toString();
			 if
			 (xmlExportProducttypeGroup.getChild("AddressList").containsChild("XmlExportAddress"))
			 {
			 String lat =
			 xmlExportProducttypeGroup.getChild("AddressList").getChild("XmlExportAddress").getChild("Latitude").getValue().toString().replace(",",
			 ".");
			 String lon =
			 xmlExportProducttypeGroup.getChild("AddressList").getChild("XmlExportAddress").getChild("Longitude").getValue().toString().replace(",",
			 ".");
			 String coordinate = lat + "," + lon;
			 if (!coordinate.equals("NaN,NaN")) {
				// System.err.println(hotelId+"---"+lat+"---"+lon);
				 mapping.write(hotelId + "\t" + coordinate + "\r\n", true);
			 }
			 }
		
		}
	}
	private Xml exportProducttypeGroupGeo(String id) throws Throwable {
		getClientSessionId();
		login();
		XmlNode soap = new XmlNode("soap:Envelope");
		soap.newAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		soap.newAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
		soap.newAttribute("xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/");
		Xml soapBody = soap.newChild("soap:Body");
		Xml exportProducttypeGroupGeo = soapBody.newChild("ExportProducttypeGroupGeo");
		exportProducttypeGroupGeo.newAttribute("xmlns", "http://api.citybreak.com/");
		exportProducttypeGroupGeo.newChild("geoid", id);
		newHttpPostRequest("http://api.citybreak.com/Salespoint.asmx");
		setHttpHeader("Cookie", "ASP.NET_SessionId=" + clientSessionId);
		addHttpHeader("Content-Type", "text/xml; charset=utf-8");
		addHttpHeader("SOAPAction", "http://api.citybreak.com/ExportProducttypeGroupGeo");
		setXmlContent(soap.toString().replace("\r\n", ""));
		Xml producttypeGroupGeo = downloadXml("ProducttypeGroupGeo");
		return producttypeGroupGeo;
	}
	private void login() throws Throwable {
		XmlNode soap = new XmlNode("soap:Envelope");
		soap.newAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		soap.newAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
		soap.newAttribute("xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/");
		Xml soapBody = soap.newChild("soap:Body");
		Xml login = soapBody.newChild("LoginOnline");
		login.newAttribute("xmlns", "http://api.citybreak.com/");
		login.newChild("userName", "Goteborg Finn.no_online");
		login.newChild("password", "k0/6&4c)");
		login.newChild("clientSessionId", clientSessionId);
		login.newChild("clientIpAddress", IpAddress.getLocalAddress());
		login.newChild("identifier", "1572956584");
		Xml ccc = login.newChild("ccc");
		ccc.newChild("LanguageId", "5");
		ccc.newChild("Currency","SEK");
		newHttpPostRequest("http://api.citybreak.com/LoginService.asmx");
		setHttpHeader("Cookie", "ASP.NET_SessionId=" + clientSessionId);
		addHttpHeader("Content-Type", "text/xml; charset=utf-8");
		addHttpHeader("SOAPAction", "http://api.citybreak.com/LoginOnline");
		setXmlContent(soap.toString().replace("\r\n", ""));
	
		downloadXml("loginCacheAll");
	}

	private String getClientSessionId() throws Throwable {
		XmlNode soap = new XmlNode("soap:Envelope");
		soap.newAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		soap.newAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
		soap.newAttribute("xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/");
		Xml soapBody = soap.newChild("soap:Body");
		Xml login = soapBody.newChild("LoginOnline");
		login.newAttribute("xmlns", "http://api.citybreak.com/");
		login.newChild("userName", "Goteborg Finn.no_online");
		login.newChild("password", "k0/6&4c)");
		login.newChild("clientSessionId");
		login.newChild("clientIpAddress", IpAddress.getLocalAddress());
		login.newChild("identifier", "1572956584");
		Xml ccc = login.newChild("ccc");
		ccc.newChild("LanguageId", "5");
		ccc.newChild("Currency", "SEK");
		newHttpPostRequest("http://api.citybreak.com/LoginService.asmx");
		addHttpHeader("Content-Type", "text/xml; charset=utf-8");
		addHttpHeader("SOAPAction", "http://api.citybreak.com/LoginOnline");
		setXmlContent(soap.toString().replace("\r\n", ""));
		downloadXml("GetClientSessionIdCacheAll");
		clientSessionId = getCookieList().getCookie(0).getValue();
		return clientSessionId;
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
