package test;



import java.io.FileOutputStream;

import zip.Zip;





public class TestCompress {
	public static void main(String[] args) {
		// 要压缩的文件列表
		String path01 = "C:\\a";
		String path02 = "C:\\test.txt";
		try {
			FileOutputStream os = new FileOutputStream("C:\\a.zip"); // 输出的ZIP文件流
			Zip.compress(os, path01, path02);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}