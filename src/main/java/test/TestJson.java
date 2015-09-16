package test;

import json.JSONToys;
import core.xml.Xml;
import engine.WebSpider;



public class TestJson extends WebSpider{

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
        new TestJson().extractAirportMap();

	}

	public void extractAirportMap() throws Exception {
		newHttpGetRequest("http://www.bmibaby.com/mvc/Search/Metadata/Flight/");
		downloadHttp(30, "airportmap");
		selectText("{\"routes\":", "};", INCLUDE_DELIMITERS);
		StringBuffer buffer = new StringBuffer().append(extractText());
			buffer.deleteCharAt(buffer.length() - 1);
			Xml json = JSONToys.toXml(buffer);
			System.err.println(json);
		deselectText();
		Xml routes = json.getChild("routes");
		for (int o = 0; o < routes.children(); o++) {
			Xml origin = routes.getChild(o);
			for (int d = 0; d < origin.children(); d++) {
				Xml destination = origin.getChild(d);
				if (destination.getName().length() != 3) {
					continue;
				}
				String carrier = destination.getChild("carrier").getValue().toString().toUpperCase();
				if (carrier.equals("WW")) {
					//file.add(origin.getName(), destination.getName());
				}
			}
		}
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
