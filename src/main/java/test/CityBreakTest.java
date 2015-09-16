package test;

import java.io.IOException;

import core.http.exception.HttpException;
import core.net.IpAddress;

import core.xml.Xml;
import core.xml.XmlNode;
import engine.WebSpider;

public class CityBreakTest extends WebSpider {
	private static final String organizationUrl = "http://api.citybreak.com/OnlineOrganization.asmx";
	private String xmlServer="http://test.xftserver.awp.advences.com";
	@Override
	public String getSpiderName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void runTask() throws Throwable {
		// TODO Auto-generated method stub

	}

	private void login(String clientSessionId) throws Throwable {
		XmlNode soap = new XmlNode("soap:Envelope");
		soap.newAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		soap.newAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
		soap.newAttribute("xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/");
		Xml soapBody = soap.newChild("soap:Body");
		Xml login = soapBody.newChild("LoginOnline");
		login.newAttribute("xmlns", "http://api.citybreak.com/");
		login.newChild("userName", "Visitse.se Finn.no_online");
		login.newChild("password", "20#/sj");
		login.newChild("clientSessionId", clientSessionId);
		login.newChild("clientIpAddress", IpAddress.getLocalAddress());
		login.newChild("identifier", "1440836719");

		Xml ccc = login.newChild("ccc");
		ccc.newChild("LanguageId", "5");
		ccc.newChild("Currency", "SEK");

		newHttpPostRequest("http://api.citybreak.com/LoginService.asmx");
		setHttpHeader("Cookie", "ASP.NET_SessionId=" + clientSessionId);
		addHttpHeader("Content-Type", "text/xml; charset=utf-8");
		addHttpHeader("SOAPAction", "http://api.citybreak.com/LoginOnline");
		setXmlContent(soap, true);
	
		downloadXml("login");
	}

	private String getClientSessionId() throws Throwable {
		XmlNode soap = new XmlNode("soap:Envelope");
		soap.newAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		soap.newAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
		soap.newAttribute("xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/");
		Xml soapBody = soap.newChild("soap:Body");
		Xml login = soapBody.newChild("LoginOnline");
		login.newAttribute("xmlns", "http://api.citybreak.com/");
		login.newChild("userName", "Visitse.se Finn.no_online");
		login.newChild("password", "20#/sj");
		login.newChild("clientSessionId");
		login.newChild("clientIpAddress", IpAddress.getLocalAddress());
		login.newChild("identifier", "1440836719");
//		Xml ccc = login.newChild("ccc");
//	    ccc.newChild("LanguageId", "1");
//		ccc.newChild("Currency", "SEK");
		newHttpPostRequest("http://api.citybreak.com/LoginService.asmx");
		addHttpHeader("Content-Type", "text/xml; charset=utf-8");
		addHttpHeader("SOAPAction", "http://api.citybreak.com/LoginOnline");
		setXmlContent(soap, true);
		downloadXml("GetClientSessionId");
		String clientSessionId = getCookieList().getCookie(0).getValue();
		return clientSessionId;
	}

private void getCity() throws Throwable{
	XmlNode soap = new XmlNode("soap:Envelope");
	soap.newAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
	soap.newAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
	soap.newAttribute("xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/");
	Xml soapBody = soap.newChild("soap:Body");
	Xml login = soapBody.newChild("ExportProducttypeGroup");
	login.newAttribute("xmlns", "http://api.citybreak.com/");
	login.newChild("producttypeGroupType", "11");
	newHttpPostRequest("http://api.citybreak.com/Salespoint.asmx");
	addHttpHeader("Content-Type", "text/xml; charset=utf-8");
	addHttpHeader("SOAPAction", "http://api.citybreak.com/ExportProducttypeGroup");
	setXmlContent(soap, true);
	downloadXml("city");
}
	private void getFail(String clientSessionId) throws Throwable {
		XmlNode soap = new XmlNode("soap:Envelope");
		soap.newAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		soap.newAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
		soap.newAttribute("xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/");
		Xml soapBody = soap.newChild("soap:Body");
		Xml login = soapBody.newChild("GetFailReason");
		login.newAttribute("xmlns", "http://api.citybreak.com/");
		newHttpPostRequest("http://api.citybreak.com/LoginService.asmx");
		setHttpHeader("Cookie", "ASP.NET_SessionId=" + clientSessionId);
		addHttpHeader("Content-Type", "text/xml; charset=utf-8");
		addHttpHeader("SOAPAction", "http://api.citybreak.com/GetFailReason");
		setXmlContent(soap, true);
		downloadXml("Fail");
	}

	private Xml exportProducttypeGroupGeo(String id) throws Throwable {
		XmlNode soap = new XmlNode("soap:Envelope");
		soap.newAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		soap.newAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
		soap.newAttribute("xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/");
		Xml soapBody = soap.newChild("soap:Body");
		Xml exportProducttypeGroupGeo = soapBody.newChild("ExportProducttypeGroupGeo");
		exportProducttypeGroupGeo.newAttribute("xmlns", "http://api.citybreak.com/");
		exportProducttypeGroupGeo.newChild("geoid", id);
		newHttpPostRequest("http://api.citybreak.com/Salespoint.asmx");
		setHttpHeader("Cookie", "ASP.NET_SessionId=" + id);
		addHttpHeader("Content-Type", "text/xml; charset=utf-8");
		addHttpHeader("SOAPAction", "http://api.citybreak.com/ExportProducttypeGroupGeo");
		setXmlContent(soap.toString().replace("\r\n", ""));
		Xml producttypeGroupGeo = downloadXml(120, "ProducttypeGroupGeo");
		return producttypeGroupGeo;
	}
	private Xml getProductGroupAttributes(String clientSessionId) throws Throwable {
		XmlNode soap = new XmlNode("soap:Envelope");
		soap.newAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		soap.newAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
		soap.newAttribute("xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/");
		Xml soapBody = soap.newChild("soap:Body");
		Xml getProductGroupAttributes = soapBody.newChild("GetProductGroupAttributes");
		getProductGroupAttributes.newAttribute("xmlns", "http://api.citybreak.com/");
		getProductGroupAttributes.newChild("groupType", 11);
		getProductGroupAttributes.newChild("descriptionId", 2814);
		getProductGroupAttributes.newChild("showActiveAttributes", true);
		newHttpPostRequest(organizationUrl);
		setHttpHeader("Cookie", "ASP.NET_SessionId=" + clientSessionId);
		addHttpHeader("Content-Type", "text/xml; charset=utf-8");
		addHttpHeader("SOAPAction", "http://api.citybreak.com/GetProductGroupAttributes");
		setXmlContent(soap.toString().replace("\r\n", ""));
		Xml productGroupAttributes = downloadXml("ProductGroupAttributes");
		return productGroupAttributes;
	}
	/**
	 * @param args
	 * @throws Throwable
	 */
	public static void main(String[] args) throws Throwable {
		String ss = new CityBreakTest().getClientSessionId();
		new CityBreakTest().login(ss);
		new CityBreakTest().exportProducttypeGroupGeo("5213");
//	    new CityBreakTest().loginaa();
		
     
	}

	private void loginaa() throws Exception{
		Xml request= new XmlNode("Requester");
		
		Xml header = request.newChild("soapenv:Header");
//		Xml header= new XmlNode("soapenv:Header");
		Xml userName = header.newChild("username", "OpodoTest");
		userName.newAttribute("soapenv:actor", "http://schemas.xmlsoap.org/soap/actor/next");
		userName.newAttribute("soapenv:mustUnderstand", "1");
		userName.newAttribute("xmlns:soapenc", "http://schemas.xmlsoap.org/soap/encoding/");
		userName.newAttribute("xsi:type", "soapenc:string");
		Xml password = header.newChild("password", "OpodoTest");
		password.newAttribute("soapenv:actor", "http://schemas.xmlsoap.org/soap/actor/next");
		password.newAttribute("soapenv:mustUnderstand", "1");
		password.newAttribute("xmlns:soapenc", "http://schemas.xmlsoap.org/soap/encoding/");
		password.newAttribute("xsi:type", "soapenc:string");
		newHttpPostRequest(xmlServer);
		addHttpHeader("Content-Type", "text/xml; charset=utf-8");
		addHttpHeader("SOAPAction", "http://schemas.xmlsoap.org/soap/");
		setXmlContent(request,true);
		String aa=downloadXml("login").toString();
		System.err.println(aa);
	}

}
