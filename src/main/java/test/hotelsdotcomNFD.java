package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.ZipInputStream;

import core.io.file.TextFile;
import core.text.Charsets;
import engine.WebSpider;


public class hotelsdotcomNFD extends WebSpider{


	public static void main(String[] args) throws Exception {
		System.err.print("start");
  		new hotelsdotcomNFD().generate();
       System.err.print("end");
	}
	public void generate() throws Exception{
			// http://lowcostold/xmlspec/xmldemo/Hotel_All_Active.zip

			ZipInputStream zipInput = new ZipInputStream(download("http://www.ian.com/affiliatecenter/include/Hotel_All_Active.zip"));

			zipInput.getNextEntry();
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(zipInput, Charsets.ISO_8859_1));
			} catch (Exception e) {
			}
			TextFile mapping = new TextFile("E://nfd2//hotelsdotcom.nfd");
			mapping.delete();
			String line;
			String str3=reader.readLine();
//			String splitStr[]=str3.split("\\|");
//			for(int i=0;i<splitStr.length;i++){
//				System.err.println(splitStr[i]+"---"+i);
//			}
			while ((line = reader.readLine()) != null)
				try {
					String[] str= line.split("\\|");
				  //  System.err.println(hotelId+"--"+lat+"--"+lon);
				    if(str[11].startsWith("-.")||str[10] .startsWith("-.")||str[11].startsWith(".")||str[10] .startsWith(".")){
				    	
				    }else{
				    mapping.write(str[0] + "\t" + str[11] + "," + str[10] + "\r\n", true);
				    }
				} catch (Exception e) {
					e.printStackTrace();
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
