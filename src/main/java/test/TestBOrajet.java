package test;

import java.io.IOException;

import core.http.exception.HttpException;
import core.util.UtilMap;
import engine.WebSpider;


public class TestBOrajet extends WebSpider{
public static void main(String[] args) throws Exception {
	new TestBOrajet().method();
}

private void method() throws Exception {
	newHttpGetRequest("https://www.onurair.com.tr/secure/menu.aspx");
	downloadHtmlFormList("homepage");
	
	
}


private String getAirportCodeById(String id) {
	UtilMap airportCode = new UtilMap();
	airportCode.put("9", "AYT");
	airportCode.put("5", "ESB");
	airportCode.put("60", "GZP");
	airportCode.put("1", "SAW");
	airportCode.put("3", "ADB");
	airportCode.put("35", "BXN");
	airportCode.put("4", "ADA");
	airportCode.put("20", "MQM");
	airportCode.put("14", "NAV");
	airportCode.put("15", "TZX");
	airportCode.put("6", "DIY");
	airportCode.put("10", "SXZ");
	airportCode.put("17", "TJK");
	airportCode.put("23", "EDO");
	airportCode.put("13", "VAN");
	airportCode.put("12", "ONQ");
	airportCode.put("22", "SZF");
	
	return airportCode.get(id).toString();
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
