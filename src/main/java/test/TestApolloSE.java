package test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import core.text.Charsets;
import core.text.Text;
import core.util.UtilList;
import core.util.UtilMap;


public class TestApolloSE {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		new TestApolloSE().method();

	}

	private void method() throws Exception {
		FileInputStream fis=new FileInputStream("D:\\APOSEK.txt\\APOSEK.txt");
		BufferedReader reader = null; reader = new BufferedReader(new InputStreamReader(fis, Charsets.UTF_8));
		cacheData(reader);
		
	}
	private void cacheData(BufferedReader reader) throws Exception {
		// write one result to cache
		reader.readLine();
		String line;
		Set set=new HashSet();
		try {
			while ((line=reader.readLine())!= null) {
				String[] oneResult = line.split(";");
				// OSLZAK_7_05 or OSLZAK_14_08 //
				String tempKey = oneResult[5].trim() +"\t"+ oneResult[11].trim()+"\t"+oneResult[15].trim(); 
			    set.add(tempKey);
			}
			Iterator iter=set.iterator();
			while (iter.hasNext()) {
				System.err.println(iter.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
