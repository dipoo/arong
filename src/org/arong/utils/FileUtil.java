package org.arong.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.servlet.http.HttpServlet;

public class FileUtil {
	/**
	 * 将一个源文件（夹）复制到另一个文件（夹）
	 * @param sourceFilePath
	 *            文件复制源
	 * @param targetFilePath
	 *            文件复制目标端
	 * @throws IOException 
	 */
	public static void copyDirectoryWithinFile(String sourceFilePath, String targetFilePath) throws IOException {
		// 获取源文件
		File sourceFile = new File(sourceFilePath);
		File[] sourceFiles = null;
		// 如果源文件是一个目录
		if (sourceFile.isDirectory()) {
			targetFilePath += "\\" + sourceFile.getName();
			// 目标文件（目录）
			File targetFile = new File(targetFilePath);
			// 创建目录
			targetFile.mkdirs();
			sourceFiles = sourceFile.listFiles();
		} else if (sourceFile.isFile()) {
			sourceFiles = new File[] { sourceFile };
		}
		// 循环文件
		for (int i = 0; i < sourceFiles.length; i++) {
			if (sourceFiles[i].isDirectory()) {
				String newSourceFilePath = sourceFilePath + "\\"
						+ sourceFiles[i].getName();
				// 递归
				copyFile(newSourceFilePath, targetFilePath);
			} else {
				try {
					FileInputStream fis = new FileInputStream(sourceFiles[i]);
					BufferedInputStream bis = new BufferedInputStream(fis);
					FileOutputStream fos = new FileOutputStream(targetFilePath
							+ "\\" + sourceFiles[i].getName());
					BufferedOutputStream bos = new BufferedOutputStream(fos);
					byte[] buff = new byte[1024];
					int len = 0;
					while ((len = bis.read()) != -1) {
						bos.write(buff, 0, len);
					}
					bos.flush();
					bos.close();
					fos.close();
					bis.close();
					fis.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 通过字节流的方式复制文件
	 * @param srcPath
	 * @param deskPath
	 * @throws IOException
	 */
	public static void copyFile(String srcPath, String deskPath) throws IOException{
		copyByte(new File(srcPath), new File(deskPath));
	}
	/**
	 * 字节文件复制
	 * @param srcFile 源文件
	 * @param deskFile 目标文件
	 * @throws IOException
	 */
	public static void copyByte(File srcFile, File deskFile) throws IOException {
		FileInputStream fis = new FileInputStream(srcFile);
		BufferedInputStream bis = new BufferedInputStream(fis);
		FileOutputStream fos = new FileOutputStream(deskFile);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		byte[] b = new byte[1024];
		int len = 0;
		while (-1 != (len = bis.read(b))) {
			bos.write(b, 0, len);
			bos.flush();
		}
		bis.close();
		fis.close();
		bos.close();
		bis.close();
	}

	/**
	 * 文本文件复制
	 * 
	 * @param srcFile
	 *            源文件
	 * @param deskFile
	 *            目标文件
	 * @throws IOException
	 */
	public static void copyChar(File srcFile, File deskFile) throws IOException {
		FileInputStream fis = new FileInputStream(srcFile);
		InputStreamReader isReader = new InputStreamReader(fis, "UTF-8");
		FileOutputStream fos = new FileOutputStream(deskFile);
		OutputStreamWriter osWriter = new OutputStreamWriter(fos, "UTF-8");
		char[] c = new char[1024];
		int len = 0;
		while ((len = isReader.read(c)) != -1) {
			osWriter.write(c, 0, len);
			osWriter.flush();
		}
		isReader.close();
		fis.close();
		osWriter.close();
		fos.close();
	}

	/**
	 * 获取java工程项根路径的文件对象。 在真实的开发环境中，此方法不推荐使用，因为一个项目的发布，工程根目录下的文件 很可能会被丢弃。
	 * 
	 * @param filename
	 *            文件名
	 * @return File
	 * @throws IOException
	 */
	public static File getJavaRootFile(String filename) throws IOException {
		File file = new File(filename);
		return file;
	}

	/**
	 * 获取class所在同目录的文件对象。 如果文件在上一层目录，则filename为../filename，以此类推。
	 * 
	 * @param clazz
	 *            调用此方法的类的字节码对象
	 * @param filename
	 *            文件名
	 * @return File
	 * @throws IOException
	 */
	public static File getCurrentClassFile(Class<?> clazz, String filename)
			throws IOException {
		File file = new File(clazz.getResource(filename).getPath());
		return file;
	}

	/**
	 * 获得web项目根目录下的文件对象。 如果为子目录，则filename为childpath/filename，以此类推。
	 * 
	 * @param filename
	 *            文件名
	 * @return File
	 * @throws IOException
	 */
	public static File getWebRootFile(String filename) throws IOException {
		File file = new File("/" + filename);
		return file;
	}

	/**
	 * 获取Web项目的WEB-INF目录下的文件对象。
	 * 
	 * @param filename
	 *            文件名
	 * @return
	 * @throws IOException
	 */
	public static File getWebInfFile(String filename) throws IOException {
		return getWebRootFile("WEB-INT/" + filename);
	}

	/**
	 * 获取Web项目的WEB-INF目录下的文件对象。
	 * 
	 * @param clazz
	 *            调用此方法的类的字节码对象
	 * @param filename
	 *            文件名
	 * @return File
	 * @throws IOException
	 */
	public static File getWebInfFile(Class<?> clazz, String filename)
			throws IOException {
		String path = clazz.getResource("/").getPath();
		File file = new File(path.substring(0, path.length() - 8) + filename);
		return file;
	}

	/**
	 * servlet环境获取Web项目的WEB-INF目录下的文件对象。
	 * 
	 * @param servlet
	 *            HttpServlet对象，在servlet环境中可以传this。
	 * @param filename
	 *            文件名
	 * @return File
	 * @throws IOException
	 */
	public static File getWebInfFile(HttpServlet servlet, String filename)
			throws IOException {
		File file = new File(servlet.getServletContext().getRealPath(
				"/WEB-INF/" + filename));
		return file;
	}
}