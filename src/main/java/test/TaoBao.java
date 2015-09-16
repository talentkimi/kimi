package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import core.io.file.TextFile;
import core.text.Charsets;
import core.text.Text;

import engine.WebSpider;

public class TaoBao extends WebSpider {

	@Override
	public String getSpiderName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void runTask() throws Throwable {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) throws Throwable {
		  new TaoBao().downloadFile();

	}
	
	private void downloadFile() throws Throwable {
		List<String> teamList= new ArrayList<String>();
		String WEB_PAGE = "http://caipiao.taobao.com/lottery/order/lottery_jczq_spf.htm?spm=0.0.0.0.rNfWQ5";
	    URL url = new URL(WEB_PAGE);  
	    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();  
	    InputStreamReader input = new InputStreamReader(httpConn  
	            .getInputStream(), "GBK");  
	    BufferedReader bufReader = new BufferedReader(input);  
	    String line = "";  
	    StringBuilder contentBuf = new StringBuilder();  
	    while ((line = bufReader.readLine()) != null) {  
	        contentBuf.append(line);  
	    }  
	    selectText(contentBuf.toString());
	    while (selectText("<tbody", "</tbody>", OPTIONAL | INCLUDE_DELIMITERS)) {
			
//	    selectText("<tbody", "</tbody>");
		while (selectText("<tr class=\"ballLine\"", "</tr>", OPTIONAL | INCLUDE_DELIMITERS)) {
			selectText("<td class=\"league\"><span", "</td>");
			String league = extractText(">","</span>");
			deselectText();
			selectText("<span><a", "</a>");
			String homeTeam=extractText(">", null).trim();
			deselectText();
			selectText("<td class", "</td>");
			String adjust=extractText(">", null);
			deselectText();
			selectText("<span><a", "</a>");
			String awayTeam=extractText(">", null).trim();
			deselectText();
			teamList.add(league+"\t"+homeTeam+"\t"+adjust+"\t"+awayTeam);
//			System.err.println(league+"\t"+homeTeam+"\t"+adjust+"\t"+awayTeam);
			deselectText();
		}
		deselectText();
	    }
		deselectText();
		
		writeToFile(teamList);
		
	}

	private void writeToFile(List<String> teamList) throws Exception, IOException {
		WritableWorkbook  workbook = Workbook.createWorkbook(new FileOutputStream(new File("C:\\Users\\kimi\\Desktop\\lottery.xls")));
		WritableSheet ws = workbook.createSheet("sheet 1", 0);
		
		
		for(int i=0;i<teamList.size();i++)
		{
			String[] teamInfo = Text.split(teamList.get(i), "\t");
			putRow(ws, i, teamInfo);
		}
		 workbook.write();
         workbook.close();
	}

	private void putRow(WritableSheet ws, int i, String[] teamInfo) throws Exception {
		 for(int j=0; j<teamInfo.length; j++) {//写一行
	            Label cell = new Label(j, i, ""+teamInfo[j]);
	            ws.addCell(cell);
	        }
	}
}
