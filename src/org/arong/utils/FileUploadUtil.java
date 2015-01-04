package org.arong.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.arong.utils.WebUtil;


public class FileUploadUtil {
	private static String filePath = null;

	private static Random ran = new Random();

	public static final String TEMP_PATH = "/WEN-INF/temp";
	public static final String UPLOAD_PATH = "/WEN-INF/bookpics";

	/**
	 * 得到文件夹下所有文件，并将这些文件名称封装到Map集合中<br>
	 * 参数代表的是一个目录 <br>
	 * 返加值中格式是 key-uuid<br>
	 * 名称 value--真实名称
	 * 
	 * @param dir
	 * @return
	 */
	public static Map<String, String> getAllFileName(String dir) {
		Map<String, String> map = new HashMap<String, String>();
		File file = new File(dir);
		getAllFileName(file, map);
		return map;
	}

	/**
	 * 递归找出文件夹下的所有文件并封装到map中
	 * 
	 * @param file
	 * @param map
	 */
	private static void getAllFileName(File file, Map<String, String> map) {
		File[] fs = file.listFiles();
		for (File f : fs) {
			if (f.isDirectory()) {
				getAllFileName(f, map);// 递归调用
			} else {
				String name = f.getName(); // uuid名称
				String realName = name.substring(name.indexOf("_") + 1);
				map.put(name, realName);
			}
		}
	}

	/**
	 * 根据文件夹的名称得到文件夹在服务器上的路径
	 * 
	 * @param dirPath
	 * @param request
	 * @return
	 */
	public static String getFileServerPath(String dirPath,
			HttpServletRequest request) {
		return request.getSession().getServletContext().getRealPath(dirPath);
	}

	/**
	 * 得到一个文件的真实名字
	 * 
	 * @param filename
	 * @return
	 */
	public static String getFileRealName(String filename) {
		return filename.substring(filename.lastIndexOf("\\") + 1);
	}

	/**
	 * 得到一个随机的名字的文件名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getRandomFilename(String filename) {
		return UUID.randomUUID().toString() + "_" + filename;
	}

	/**
	 * 得到随机目录
	 * 
	 * @param uploadDir
	 * @return
	 */
	public static String getRandomDir(String uploadDir) {

		String firstDir = String.valueOf((char) (ran.nextInt(6) + 97));
		String secondDir = String.valueOf((char) (ran.nextInt(6) + 97));
		File file = new File(uploadDir + "\\" + firstDir + "\\" + secondDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getPath();
	}

	/**
	 * 完成文件复制
	 * 
	 * @param item
	 * @param uploadDir
	 * @param fileRealname
	 */
	public static void copy(FileItem item, String uploadDir, String fileRealname) {
		InputStream is = null;
		OutputStream os = null;
		uploadDir = getRandomDir(uploadDir);
		try {
			is = item.getInputStream();
			os = new FileOutputStream(uploadDir + "\\" + fileRealname);
			byte[] b = new byte[1024 * 10];
			int len = 0;
			while ((len = is.read(b)) != -1) {
				os.write(b, 0, len);
				os.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("复制文件失败");
		} finally {
			try {
				if (is != null)
					is.close();
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				throw new RuntimeException("关闭流失败");
			}
		}
	}

	/**
	 * 删除临时文件
	 * 
	 * @param tempDir
	 */
	public static void deleteTemp(String tempDir) {
		File file = new File(tempDir);
		File[] fs = file.listFiles();
		for (File f : fs) {
			f.delete();
		}
	}

	/**
	 * 通过uuid文件名查找上传目录中的文件并返回绝对路径
	 * 
	 * @param dir
	 * @param uuidName
	 * @return
	 */
	public static String getFilePath(String dir, String uuidName) {
		File file = new File(dir);
		if (file.isDirectory()) {
			File[] fs = file.listFiles();
			for (File f : fs) {
				filePath = getFilePath(f.getAbsolutePath(), uuidName);
			}
		} else {
			if (file.getName().equals(uuidName)) {
				filePath = file.getAbsolutePath();
			}
		}
		return filePath;
	}

	/**
	 * 保存文件并返回文件名
	 * 
	 * @param req
	 * @param clazz
	 * @param book
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object[] storeAndReturn(HttpServletRequest req,
			Class<?> clazz) {
		Object[] objects = new Object[] { "default.png", null };
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(1024 * 10 * 10);
		factory.setRepository(new File(getFileServerPath("/WEB-INF/temp", req)));
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> items = null;
		upload.setHeaderEncoding("utf-8");
		try {
			items = upload.parseRequest(req);
			Object obj = clazz.newInstance();
			for (FileItem fileItem : items) {
				if (fileItem.isFormField()) {
					try {
						String name = fileItem.getFieldName();
						String value = fileItem.getString("UTF-8");
						System.out.println(name + ":" + value);
						WebUtil.setProperty(obj, name, new String[] { value });
						System.out.println(obj);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				} else {
					String dir = getRandomDir(getFileServerPath(
							"/WEB-INF/bookpics", req));
					String fileRealname = getRandomFilename(getFileRealName(fileItem
							.getName()));
					store(fileItem, dir, fileRealname);
					objects[0] = dir.substring(dir.indexOf("WEB-INF") + 8, dir.length()) + "/" + fileRealname;
				}
			}
			objects[1] = obj;
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return objects;
	}

	/**
	 * 完成文件保存
	 * 
	 * @param item
	 * @param uploadDir
	 * @param fileRealname
	 */
	public static void store(FileItem item, String uploadDir,
			String fileRealname) {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = item.getInputStream();
			os = new FileOutputStream(uploadDir + "\\" + fileRealname);
			byte[] b = new byte[1024 * 10];
			int len = 0;
			while ((len = is.read(b)) != -1) {
				os.write(b, 0, len);
				os.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("复制文件失败");
		} finally {
			try {
				if (is != null)
					is.close();
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				throw new RuntimeException("关闭流失败");
			}
		}
	}
}