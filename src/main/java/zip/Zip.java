package zip;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;
import org.apache.tools.zip.ZipEntry;

/**
 * ZIP操作工具类
 * @说明 注意引入ant.jar文件
 * @author cuisuqiang
 * @version 1.0
 * @since
 */
public class Zip {
	
	/**
	 * 压缩文件列表到某ZIP文件
	 * @param zipFilename 要压缩到的ZIP文件
	 * @param paths 文件列表，多参数
	 * @throws Exception
	 */
	public static void compress(String zipFilename, String... paths)
			throws Exception {
		compress(new FileOutputStream(zipFilename), paths);
	}

	/**
	 * 压缩文件列表到输出流
	 * @param os 要压缩到的流
	 * @param paths 文件列表，多参数
	 * @throws Exception
	 */
	public static void compress(OutputStream os, String... paths)
			throws Exception {
		ZipOutputStream zos = new ZipOutputStream(os);
		for (String path : paths) {
			if (path.equals(""))
				continue;
			java.io.File file = new java.io.File(path);
			if (file.exists()) {
				if (file.isDirectory()) {
					zipDirectory(zos, file.getPath(), file.getName()
							+ File.separator);
				} else {
					zipFile(zos, file.getPath(), "");
				}
			}
		}
		zos.close();
	}
	
	private static void zipDirectory(ZipOutputStream zos, String dirName,
			String basePath) throws Exception {
		File dir = new File(dirName);
		if (dir.exists()) {
			File files[] = dir.listFiles();
			if (files.length > 0) {
				for (File file : files) {
					if (file.isDirectory()) {
						zipDirectory(zos, file.getPath(), basePath
								+ file.getName().substring(
										file.getName().lastIndexOf(
												File.separator) + 1)
								+ File.separator);
					} else
						zipFile(zos, file.getPath(), basePath);
				}
			} else {
				ZipEntry ze = new ZipEntry(basePath);
				zos.putNextEntry(ze);
			}
		}
	}

	private static void zipFile(ZipOutputStream zos, String filename,
			String basePath) throws Exception {
		File file = new File(filename);
		if (file.exists()) {
			FileInputStream fis = new FileInputStream(filename);
			ZipEntry ze = new ZipEntry(basePath + file.getName());
			zos.putNextEntry(ze);
			byte[] buffer = new byte[8192];
			int count = 0;
			while ((count = fis.read(buffer)) > 0) {
				zos.write(buffer, 0, count);
			}
			fis.close();
		}
	}
}