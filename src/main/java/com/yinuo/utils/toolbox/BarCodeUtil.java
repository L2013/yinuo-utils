package com.yinuo.utils.toolbox;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * @author chenglu
 * 2019年6月28日
 * 条形码工具类
 */
public class BarCodeUtil {
    private static final String CHARSET = "utf-8";
    private static final String FORMAT = "PNG";

    private static final int HEIGHT = 100;
    private static final char DOT = '.';

    public static void encode(String content, String destPath, String fileName) throws Exception {
        String formatFileName = fileName.substring(0, StrUtil.contains(fileName, DOT) ? fileName.indexOf(".") : fileName.length()) + "."
                + FORMAT.toLowerCase();
        String fullPath = destPath + File.separator + formatFileName;
        File file = new File(fullPath);
        FileOutputStream output = new FileOutputStream(file);
        encode(content, output);
    }

    public static void encode(String content, OutputStream output) throws Exception {
        ImageIO.write(createImage(content), FORMAT, output);
    }

    public static String decode(String fullPath) throws Exception {
        return decode(new File(fullPath));
    }

    public static String decode(File file) throws Exception {
        BufferedImage image = ImageIO.read(file);
        Assert.notNull(image);

        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Map<DecodeHintType, Object> hints = getDecodeHintType();
        Result result = new MultiFormatReader().decode(bitmap, hints);
        return result.getText();
    }

    /**
     * 根据文本生成条形码
     *
     * @param content
     * @return
     * @throws Exception
     */
    private static BufferedImage createImage(String content) throws Exception {
        Map<EncodeHintType, Object> hintType = getEncodeHintType();
        // 使用code_128格式进行编码生成100*25的条形码
        MultiFormatWriter writer = new MultiFormatWriter();
        //为了无边距，需设置宽度为条码自动生成规则的宽度
        int width = new Code128Writer().encode(content).length;
        //条码放大倍数
        int codeMultiples = 1;
        //获取条码内容的宽，不含两边距，当EncodeHintType.MARGIN为0时即为条码宽度
        int codeWidth = width * codeMultiples;
        // 生成位图矩阵BitMatrix
        BitMatrix matrix = writer.encode(content, BarcodeFormat.CODE_128, codeWidth, HEIGHT, hintType);
        return MatrixToImageWriter.toBufferedImage(matrix);
    }

    /**
     * 设置条形码的生码格式参数
     *
     * @return
     */
    public static Map<EncodeHintType, Object> getEncodeHintType() {
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.MARGIN, 0);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        return hints;
    }

    /**
     * 设置条形码解码格式参数
     *
     * @return
     */
    public static Map<DecodeHintType, Object> getDecodeHintType() {
        Map<DecodeHintType, Object> hints = new HashMap<>();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        return hints;
    }

    public static void main(String[] args) throws Exception {
        String content = "3086049002846739";
        BarCodeUtil.encode(content, "e://", "barCodeTest.png");

        content = BarCodeUtil.decode("e://barCodeTest.png");
        System.out.println(content);
    }
}
