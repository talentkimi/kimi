package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.zip.ZipInputStream;

import core.io.file.TextFile;
import core.text.Charsets;
import engine.WebSpider;
public class HotelProntoNFD extends WebSpider{

	public static void main(String[] ags) {
		try{
			System.err.print("start");
	        new HotelProntoNFD().createNFD();
	        System.err.print("end");
	        
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void createNFD() throws Exception {
		// http://www.hotelpronto2.com/downloads/Hotels.zip
		ZipInputStream zipInput = new ZipInputStream(download("http://www.hotelpronto2.com/downloads/Hotels.zip"));
		zipInput.getNextEntry();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(zipInput, Charsets.UTF_16LE));
		} catch (Exception e) {
		}
		String line;
		String str=reader.readLine();
//		String splitStr[]=str.split("\",\"");
//		for(int i=0;i<splitStr.length;i++){
//			System.err.println(splitStr[i]+"---"+i);
//		}
		//lat 23 lon 22
		TextFile mapping = new TextFile("E://nfd2//hotelpronto.nfd");
		mapping.delete();
		while ((line = reader.readLine()) != null) {
			line = new String(line.getBytes(Charsets.UTF_8), Charsets.UTF_8);
			String[] hotel = line.split("\",\"");
		//	String id=hotel[0];
		 //  String lat=hotel[22];
		  //  String lon=hotel[21];
		   // System.err.println(hotel[0].substring(1)+"---"+hotel[23]+"---"+hotel[22]);
			mapping.write(hotel[0].substring(1) + "\t" + hotel[23] + "," + hotel[22] + "\r\n", true);
			}
		reader.close();
		zipInput.close();
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
