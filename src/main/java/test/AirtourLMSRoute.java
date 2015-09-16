package test;

import java.util.HashSet;
import java.util.Iterator;

import core.http.exception.HttpException;
import core.xml.Xml;
import engine.WebSpider;


public class AirtourLMSRoute extends WebSpider{


	private void downloadFile() throws Throwable {
//		String WEB_PAGE = "http://www.airtours.se/lmp_destinationse.txt";
//		newHttpGetRequest(WEB_PAGE);
//		downloadHttp(60, "lmp_destinationse.txt");
//		HashSet<String> sets=new HashSet<String>();
//		while (selectText("", "\n", OPTIONAL | INCLUDE_DELIMITERS)) {
//			String temp = extractText();
//			String oneOffer[] = temp.split("\t");
//			sets.add(oneOffer[1].replaceAll("\"", ""));
//			deselectText();
//		}
//		deselectText();
//
//		Iterator iter=sets.iterator();
//		while(iter.hasNext()){
//			System.err.println(iter.next());
//		}
//		
		String urlString = "https://kyfw.12306.cn/otn/dynamicJs/ljbisyr";
		newHttpGetRequest(urlString);
		downloadHttp(60, "rail");
	}
	
	public static void main(String[] args) throws Throwable {
		  new AirtourLMSRoute().downloadFile();

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
