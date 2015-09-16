package zip;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 解压缩测试
 * @说明 
 * @author cuisuqiang
 * @version 1.0
 * @since
 */
public class ExtractZip {
	
	@SuppressWarnings( { "unchecked", "static-access" })
	public static void main(String[] args) {
		ExtractZip z = new ExtractZip();
		ArrayList<String> a = z.Ectract("C:\\a.zip", "C:\\tmp\\"); // 返回解压缩出来的文件列表
		for(String s : a){
			System.out.println(s);
		}
	}

	/**
	 * 解压缩
	 * @param sZipPathFile 要解压的文件
	 * @param sDestPath 解压到某文件夹
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList Ectract(String sZipPathFile, String sDestPath) {
		ArrayList<String> allFileName = new ArrayList<String>();
		try {
			// 先指定压缩档的位置和档名，建立FileInputStream对象
			FileInputStream fins = new FileInputStream(sZipPathFile);
			// 将fins传入ZipInputStream中
			ZipInputStream zins = new ZipInputStream(fins);
			ZipEntry ze = null;
			byte[] ch = new byte[256];
			while ((ze = zins.getNextEntry()) != null) {
				File zfile = new File(sDestPath + ze.getName());
				File fpath = new File(zfile.getParentFile().getPath());
				if (ze.isDirectory()) {
					if (!zfile.exists())
						zfile.mkdirs();
					zins.closeEntry();
				} else {
					if (!fpath.exists())
						fpath.mkdirs();
					FileOutputStream fouts = new FileOutputStream(zfile);
					int i;
					allFileName.add(zfile.getAbsolutePath());
					while ((i = zins.read(ch)) != -1)
						fouts.write(ch, 0, i);
					zins.closeEntry();
					fouts.close();
				}
			}
			fins.close();
			zins.close();
		} catch (Exception e) {
			System.err.println("Extract error:" + e.getMessage());
		}
		return allFileName;
	}
}