package test;



import java.lang.reflect.Field;

import core.util.UtilDate;

import engine.WebSpider;


public class TestCurrency extends WebSpider{

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		new TestCurrency().method();

	}

	private  void method() throws Exception {
	  setHttpCharset("UTF-8");
      newHttpGetRequest("http://www.boc.cn/sourcedb/whpj/");
      downloadHttp("currency.htm");
      selectText("<td align=\"center\" valign=\"top\">", "<td height=\"30\" align=\"center\">");
	  while (selectText("<tr align=\"center\">", "</tr>", OPTIONAL | INCLUDE_DELIMITERS)) {
			selectText("<td", "</td>");
			String country = extractText(">",null);
			deselectText();
			selectText("<td", "</td>");
			String currency1 = extractText(">",null);
			deselectText();
			selectText("<td", "</td>");
			String currency2=extractText(">", null);
			deselectText();
			selectText("<td", "</td>");
			String currency3=extractText(">", null);
			deselectText();
			selectText("<td", "</td>");
			String currency4=extractText(">", null);
			deselectText();
			selectText("<td", "</td>");
			String currency5=extractText(">", null);
			deselectText();
			selectText("<td", "</td>");
			String time=extractText(">", null);
			deselectText();
			System.err.println(country+"||"+currency1+"||"+currency2+"||"+currency3+"||"+currency4+"||"+currency5+"||"+time);
			deselectText();
		}
	  deselectText();
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
