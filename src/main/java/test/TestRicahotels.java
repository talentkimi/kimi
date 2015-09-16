package test;

import java.io.IOException;


import core.http.exception.HttpException;
import core.net.IpAddress;

import core.xml.Xml;
import core.xml.XmlNode;
import engine.WebSpider;

public class TestRicahotels extends WebSpider {
	private static final String organizationUrl = "http://api.citybreak.com/OnlineOrganization.asmx";
	@Override
	public String getSpiderName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void runTask() throws Throwable {
		// TODO Auto-generated method stub

	}

	private void login() throws Throwable {
		XmlNode soap = new XmlNode("soap:Envelope");
		soap.newAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		soap.newAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
		soap.newAttribute("xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/");
		Xml soapBody = soap.newChild("soap:Body");
		Xml login = soapBody.newChild("GetHotels");
		login.newAttribute("xmlns", "http://www.rica.no/");
      
		newHttpPostRequest("http://www.rica.no/TravelFusionAPI/GetStaticHotelInformation.asmx?op=GetHotels");
		//setHttpHeader("Cookie", "ASP.NET_SessionId=" + clientSessionId);
		addHttpHeader("Content-Type", "text/xml; charset=utf-8");
		addHttpHeader("SOAPAction", "http://www.rica.no/GetHotels");
		setXmlContent(soap, true);
	
		downloadXml("hotels");
	}
	private void getHotel() throws Throwable {
		XmlNode soap = new XmlNode("soap:Envelope");
		soap.newAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		soap.newAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
		soap.newAttribute("xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/");
		Xml soapBody = soap.newChild("soap:Body");
		Xml login = soapBody.newChild("GetHotel");
		login.newAttribute("xmlns", "http://www.rica.no/");
        login.newChild("hotelId","6948");
		newHttpPostRequest("http://www.rica.no/TravelFusionAPI/GetStaticHotelInformation.asmx?op=GetHotel");
		//setHttpHeader("Cookie", "ASP.NET_SessionId=" + clientSessionId);
		addHttpHeader("Content-Type", "text/xml; charset=utf-8");
		addHttpHeader("SOAPAction", "http://www.rica.no/GetHotel");
		setXmlContent(soap, true);
		downloadXml("hotel");
	}
	/**
	 * @param args
	 * @throws Throwable
	 */
	
	private void getRoom() throws Throwable {
		XmlNode soap = new XmlNode("soap:Envelope");
		soap.newAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		soap.newAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
		soap.newAttribute("xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/");
		Xml soapBody = soap.newChild("soap:Body");
		Xml login = soapBody.newChild("GetData");
		login.newAttribute("xmlns", "http://www.rica.no/");
        login.newChild("hotelPageId","6835");
        login.newChild("checkInDate","2010-08-11");
        login.newChild("checkOutDate","2010-08-13");
        
        login.newChild("numberOfAdultsRoom1","1");
        login.newChild("numberOfChildrenRoom1","0");
        login.newChild("numberOfAdultsRoom2","0");
        login.newChild("numberOfChildrenRoom2","0");
        login.newChild("numberOfAdultsRoom3","0");
        login.newChild("numberOfChildrenRoom3","0");
        login.newChild("numberOfAdultsRoom4","0");
        login.newChild("numberOfChildrenRoom4","0");
        login.newChild("rateCode","");
        
		newHttpPostRequest("http://www.rica.no/TravelFusionAPI/GetDynamicPriceData.asmx?op=GetData");
		//setHttpHeader("Cookie", "ASP.NET_SessionId=" + clientSessionId);
		addHttpHeader("Content-Type", "text/xml; charset=utf-8");
		addHttpHeader("SOAPAction", "http://www.rica.no/GetData");
		setXmlContent(soap, true);
	
		downloadXml("room");
	}
	public static void main(String[] args) throws Throwable {
		
//		new TestRicahotels().login();
//		new TestRicahotels().getHotel();
		new TestRicahotels().getRoom();
		
	    
		
     
	}

}
