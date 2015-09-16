package test;

import core.xml.Xml;
import core.xml.XmlNode;
import engine.WebSpider;

public class SinaStock extends  WebSpider{

	/**
	 * @param args
	 * @throws Throwable 
	 */
	public static void main(String[] args) throws Throwable {
		new SinaStock().getStock();

	}

	private void getStock() throws Throwable {
		XmlNode soap = new XmlNode("soap:Envelope");
		soap.newAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		soap.newAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
		soap.newAttribute("xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/");
		Xml soapBody = soap.newChild("soap:Body");
		Xml stockInfo = soapBody.newChild("getStockInfoByCode");
		stockInfo.newAttribute("xmlns", "http://WebXml.com.cn/");
        stockInfo.newChild("theStockCode", "sh603997");
		newHttpPostRequest("http://webservice.webxml.com.cn/WebServices/ChinaStockWebService.asmx?op=getStockInfoByCode");
		addHttpHeader("Content-Type", "text/xml; charset=utf-8");
		addHttpHeader("SOAPAction", "http://WebXml.com.cn/getStockInfoByCode");
		setXmlContent(soap, true);
	
		Xml stock= downloadXml("stock");
		System.err.println(stock.toString());
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
