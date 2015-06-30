package org.arong.utils.image;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.ImageReader;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * 二维码工具类
 */
public class QrCodeUtil {
    static String format = "jpg"; // 图像类型
    static Integer width = 200;
    static Integer height = 200;

    /**
     * 生成二维码图片
     * @param content 文本内容
     * @return
     * @throws WriterException
     */
    public static BufferedImage genQrCode(String content) throws WriterException {
        return genQrCode(content, null, null);
    }

    /**
     *生成二维码图片
     * @param content 文本内容
     * @param width 矩阵宽
     * @param height 矩阵高
     * @return
     * @throws WriterException
     */
    public static BufferedImage genQrCode(String content, Integer width, Integer height) throws WriterException {
        if (width == null) {
            width = QrCodeUtil.width; // 图像宽度
        }
        if (height == null) {
            height = QrCodeUtil.height; // 图像高度
        }

        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix =
            new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints); // 生成矩阵
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    /**
     * 生成二维码图片并保存为文件
     * @param content 文本内容
     * @param output 目标文件
     * @throws WriterException
     * @throws IOException
     */
    public static void genQrCodeToFile(String content, File output) throws WriterException, IOException {
        genQrCodeToFile(content, output, null, null, null);
    }

    /**
     *生成二维码图片并保存为文件
     * @param content 文本内容
     * @param output 目标文件
     * @param format 图片格式
     * @throws WriterException
     * @throws IOException
     */
    public static void genQrCodeToFile(String content, File output, String format) throws WriterException, IOException {
        genQrCodeToFile(content, output, format, null, null);
    }

    /**
     * 生成二维码图片并保存为文件
     * @param content 文本内容
     * @param output 目标文件
     * @param width 宽
     * @param height 高
     * @throws WriterException
     * @throws IOException
     */
    public static void genQrCodeToFile(String content, File output, Integer width,
                                       Integer height) throws WriterException, IOException {
        genQrCodeToFile(content, output, null, width, height);
    }

    /**
     *生成二维码图片并保存为文件
     * @param content 文本内容
     * @param output 目标文件
     * @param format 图片格式
     * @param width 宽
     * @param height 高
     * @throws WriterException
     * @throws IOException
     */
    public static void genQrCodeToFile(String content, File output, String format, Integer width,
                                       Integer height) throws WriterException, IOException {

        if (width == null) {
            width = QrCodeUtil.width; // 图像宽度
        }
        if (height == null) {
            height = QrCodeUtil.height; // 图像高度
        }
        if (format == null) {
            format = QrCodeUtil.format; //文件格式
        }
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix =
            new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints); // 生成矩阵
        MatrixToImageWriter.writeToFile(bitMatrix, format, output);
    }
    
    /**
     * 加载二维码图片得到Result对象
     * @param filename 文件路径
     * @return
     * @throws IOException
     */
    public static Result loadResult(String filename) throws IOException {
        return loadResult(new File(filename));
    }
    
    /**
     * 加载二维码图片得到Result对象
     * @param file 二维码文件
     * @return
     * @throws IOException
     */
    public static Result loadResult(File file) throws IOException {
        BufferedImage bi = ImageReader.readImage(file);
        return loadResult(bi);
    }
    
    /**
     * 加载二维码图片得到Result对象
     * @param bufferedImage 二维码图片
     * @return
     */
    public static Result loadResult(BufferedImage bufferedImage){
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        Result result = null;
        try {
            result = new MultiFormatReader().decode(bitmap, hints);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * 加载二维码图片，得到内容
     * @param filename 图片路径
     * @return
     * @throws IOException
     */
    public static String loadContent(String filename) throws IOException {
        return loadContent(new File(filename));
    }
    
    /**
     * 加载二维码图片，得到内容
     * @param file 二维码图片文件
     * @return
     * @throws IOException
     */
    public static String loadContent(File file) throws IOException {
        BufferedImage bi = ImageReader.readImage(file);
        return loadContent(bi);
    }
    
    /**
     * 加载二维码图片，得到内容
     * @param bufferedImage 二维码图片
     * @return
     */
    public static String loadContent(BufferedImage bufferedImage) {
        return loadResult(bufferedImage).getText();
    }
}
