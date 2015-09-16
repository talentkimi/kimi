package test;
//http://feeds.booking.com/partner/2rlk4CyAXnlkqtN9xCkQ.tsv
//http://feeds.booking.com/partner/ZTRwaPYQtcWQ3Cjy0HMjA.tsv
//http://feeds.booking.com/partner/8QuA6Ejy6TrE6H6gOeWmQ.tsv
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import core.http.HttpCookieList;
import core.io.file.TextFile;
import core.lang.LatLon;
import core.text.Charsets;
import core.text.Text;
import engine.WebSpider;


public class BookingdotcomNFD extends WebSpider{

	public static void main(String[] args) throws Exception {
		System.err.println("start");
		new BookingdotcomNFD().request();
		System.err.println("end");

	}
	public void request() throws Exception{
		
//		String source1 = null;
//		String source2 = null;
//		String source3 = null;
//		try{
//		newHttpGetRequest("https://admin.bookings.org/partner/");
//		downloadHtmlFormList("Login.html");
//
//		newHttpFormRequest("myform");
//		addHttpFormField("loginname", "destinationpunktse");
//		addHttpFormField("password", "dest1nat1on99");
//		addRemainingHttpFormFields();
//		downloadHttp("Partner_Center.html");
//
//		HttpCookieList cookies = getCookieList();
//		selectText("/partner/dump.html?affiliate_id=;partner_id=412852;ses=", "\">Downloads</a>");
//		String temp = extractText();
//		deselectText();
//		String sessionId = temp;
//
//		newHttpGetRequest("https://admin.bookings.org/partner/hoteldump.html?partner_id=412852;ses=" + sessionId);
//		for (int i = 0; i < cookies.size(); i++) {
//			addHttpHeader(cookies.getCookie(i).getName(), cookies.getCookie(i).getValue());
//		}
//		downloadHttp("download.html");
//
//		newHttpGetRequest("https://admin.bookings.org/partner/hoteldump.html?affiliate_id=;partner_id=412852;ses=" + sessionId);
//		downloadHttp("hotel_dump.html");
//
//		selectText("<li>tab delimited text format in utf8 encoding. <a href=\"", "\">");
//		source1 = extractText();
//System.err.println(source1);
//		deselectText();
//		selectText("<li>tab delimited text format in utf8 encoding. <a href=\"", "\">");
//		source2 = extractText();
//System.err.println(source2);
//		deselectText();
//		selectText("<li>tab delimited text format in utf8 encoding. <a href=\"", "\">");
//		source3 = extractText();
//System.err.println(source3);
//		deselectAllText();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		source1 = source1.replace("http", "https");
//		source2 = source2.replace("http", "https");
//		source3 = source3.replace("http", "https");
//		
		System.err.println("start1");
		BufferedReader br=null;
	//	InputStream is=null ;
		String hotelSourceInfo = null;
			TextFile mapping = new TextFile("E://nfd2//bookingdotcom.nfd");
			mapping.delete();
			setHttpCharset(Charsets.UTF_8);
		//	is = download("https://feeds.booking.com/partner/2rlk4CyAXnlkqtN9xCkQ.tsv");
			FileInputStream is=new FileInputStream("E://bookingdotcom//2E6WdcCJUnDOFtvi5hetw.tsv");
			br = new BufferedReader(new InputStreamReader(is, Charsets.UTF_8));
			br.readLine();
			while ((hotelSourceInfo = br.readLine()) != null) {
					String[] hotelInfo = Text.split(hotelSourceInfo, "\t");
					try{
					//String hotel_id = hotelInfo[0];
					//String lon = hotelInfo[14];
					//String lat = hotelInfo[15];
//					System.err.println(hotelInfo[0]+"--"+hotelInfo[15] +"--"+hotelInfo[14]);
					mapping.write(hotelInfo[0] + "\t" + hotelInfo[15] + "," + hotelInfo[14] + "\r\n", true);
					}catch(Exception e){
						continue;
					}
				}
		br.close();
		is.close();
		System.err.println("end1");
		sleep(10);
		System.err.println("start2");
		//InputStream is2 = download("https://feeds.booking.com/partner/ZTRwaPYQtcWQ3Cjy0HMjA.tsv");
		FileInputStream is2=new FileInputStream("E://bookingdotcom//4PLf4NcK2N9yFYpClV5g.tsv");
		
		br = new BufferedReader(new InputStreamReader(is2, Charsets.UTF_8));
		br.readLine();
		while ((hotelSourceInfo = br.readLine()) != null) {
				String[] hotelInfo = Text.split(hotelSourceInfo, "\t");
				try{
					mapping.write(hotelInfo[0] + "\t" + hotelInfo[15] + "," + hotelInfo[14] + "\r\n", true);
					}catch(Exception e){
					continue;
				}
			}
		br.close();
		is2.close();
		System.err.println("end2");
		sleep(10);
		System.err.println("start3");
		//InputStream is3 = download("https://feeds.booking.com/partner/8QuA6Ejy6TrE6H6gOeWmQ.tsv");
		FileInputStream is3=new FileInputStream("E://bookingdotcom//CVVa5re6W41tvcSxqbtIQ.tsv");
		br = new BufferedReader(new InputStreamReader(is3, Charsets.UTF_8));
		br.readLine();
		while ((hotelSourceInfo = br.readLine()) != null) {
				String[] hotelInfo = Text.split(hotelSourceInfo, "\t");
				try{
					mapping.write(hotelInfo[0] + "\t" + hotelInfo[15] + "," + hotelInfo[14] + "\r\n", true);
					}catch(Exception e){
					continue;
				}
			}
		br.close();
		is3.close();
		System.err.println("end3");
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
