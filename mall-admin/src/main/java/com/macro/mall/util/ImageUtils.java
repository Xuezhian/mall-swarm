package com.macro.mall.util;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;


import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * 图片处理封装类
 */
public class ImageUtils {
    private static Logger logger = LoggerFactory.getLogger(ImageUtils.class);
    private static BufferedImage picCodeBi = new BufferedImage(100, 30, BufferedImage.TYPE_3BYTE_BGR);
//	private static Graphics2D picCodeG2d = picCodeBi.createGraphics();

    /**
     * 倍率缩小图片
     *
     * @param path     -图片路径
     * @param toPath   -新路径
     * @param toWidth  -缩小宽度比例
     * @param toHeight -缩小高度比例
     */
    public static void MultipleSmile(String path, String toPath, int toWidth, int toHeight) throws IOException {

        FileOutputStream out = null;

        File file = new File(path);

        BufferedImage src = ImageIO.read(file);

        int width = src.getWidth();

        int height = src.getHeight();

        BufferedImage tag = new BufferedImage(width / toWidth, height / toHeight, BufferedImage.TYPE_INT_RGB);

        tag.getGraphics().drawImage(src, 0, 0, width / toWidth, height / toHeight, null);

        out = new FileOutputStream(toPath);

        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);

        encoder.encode(tag);
    }

    /**
     * 倍率放大图片
     *
     * @param path     -图片路径
     * @param toPath   -新路径
     * @param toWidth  -放大宽度倍数
     * @param toHeight -放大高度倍数
     */
    public static void MultipleBig(String path, String toPath, int toWidth, int toHeight) throws IOException {

        FileOutputStream out = null;

        File file = new File(path);

        BufferedImage src = ImageIO.read(file);

        int width = src.getWidth();

        int height = src.getHeight();

        BufferedImage tag = new BufferedImage(width * toWidth, height * toHeight, BufferedImage.TYPE_INT_RGB);

        tag.getGraphics().drawImage(src, 0, 0, width * toWidth, height * toHeight, null);

        out = new FileOutputStream(toPath);

        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);

        encoder.encode(tag);
    }

    /**
     * 重置图片大小
     *
     * @param path     -原图路径
     * @param toPath   -新路径
     * @param toWidth  -重新定义宽度
     * @param toHeight -重新定义高度
     */
    public static void reset(String path, String toPath, int toWidth, int toHeight) throws IOException {

        FileOutputStream out = null;

        File file = new File(path);

        BufferedImage src = ImageIO.read(file);

        BufferedImage tag = new BufferedImage(toWidth, toHeight, BufferedImage.TYPE_INT_RGB);

        tag.getGraphics().drawImage(src, 0, 0, toWidth, toHeight, null);

        out = new FileOutputStream(toPath);

        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);

        encoder.encode(tag);

    }

    /**
     * 图片灰化
     *
     * @param path    -源图路径
     * @param newPath -新路径
     * @param format  -格式
     */
    public static void gray(String path, String newPath, String format) throws IOException {

        File file = new File(path);

        BufferedImage bi = ImageIO.read(file);

        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);

        ColorConvertOp op = new ColorConvertOp(cs, null);

        bi = op.filter(bi, null);

        ImageIO.write(bi, format, new File(newPath));
    }

    /**
     * 添加文字水印
     *
     * @param path      -源图路径
     * @param newPath   -新路径
     * @param alpha     -透明度0-1之间
     * @param font      -字体
     * @param fontStyle -字体格式(粗体)
     * @param fontSize  -字体大小
     * @param color     -字体颜色
     * @param word      -水印文字
     * @param x         -文字x轴起始位置
     * @param y         -文字Y轴起始位置
     * @param format    -图片格式
     */
    
    public static void wordWaterMark(String path, String newPath, float alpha, String font, int fontStyle,

                                     int fontSize, Color color, String word, int x, int y, String format) throws IOException {

        FileOutputStream fos = null;

        BufferedImage bi = ImageIO.read(new File(path));

        Graphics2D g2d = bi.createGraphics();

        g2d.drawImage(bi, 0, 0, bi.getWidth(), bi.getHeight(), null, null);

        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);

        g2d.setComposite(ac);

        g2d.setFont(new Font(font, fontStyle, fontSize));

        g2d.setColor(color);

        g2d.drawString(word, x, y);

        g2d.dispose();

        fos = new FileOutputStream(newPath);

        ImageIO.write(bi, format, fos);

    }

    /**
     * 添加图片水印
     *
     * @param path        -源图路径
     * @param imagePath   -水印图片路径
     * @param newPath     -新路径
     * @param alpha       透明度0-1之间
     * @param x           -水印图片x轴起始位置
     * @param y           -水印图片Y轴起始位置
     * @param width       -水印图片宽
     * @param height      -水印图片高
     * @param imageFormat -格式
     */
    public static void imageWaterMark(String path, String imagePath, String newPath,

                                      float alpha, int x, int y, int width, int height, String imageFormat) throws IOException {

        FileOutputStream fos = null;

        BufferedImage bi = ImageIO.read(new File(path));

        Graphics2D g2d = bi.createGraphics();

        g2d.drawImage(bi, 0, 0, bi.getWidth(), bi.getHeight(), null, null);

        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);

        g2d.setComposite(ac);

        BufferedImage bi2 = ImageIO.read(new File(imagePath));

        g2d.drawImage(bi2, x, y, width, height, null, null);

        g2d.dispose();

        fos = new FileOutputStream(newPath);

        ImageIO.write(bi, imageFormat, fos);
    }

    /**
     * 按指定大小和位置裁剪图片
     *
     * @param path    -源图路径
     * @param newPath -新图路径
     * @param x       -裁剪x轴开始坐标
     * @param y       -y轴开始坐标
     * @param width   -裁剪宽度
     * @param height  -高度
     * @param format  -新图片格式
     */
    public static void clip(String path, String newPath, int x, int y, int width, int height, String format) throws IOException {

        BufferedImage bi = ImageIO.read(new File(path));

        BufferedImage bi2 = bi.getSubimage(x, y, width, height);

        ImageIO.write(bi2, format, new File(newPath));
    }

    /**
     * 图片旋转
     *
     * @param path    -源图路径
     * @param newpath -新图路径
     * @param format  -图片格式（jpg,png..）
     * @param degree  -旋转度数（90右转,180，-90左转）
     */
    public static void rotate(String path, String newpath, String format, int degree) throws IOException {

        BufferedImage bi = ImageIO.read(new File(path));

        int type = bi.getColorModel().getTransparency();

        int width = bi.getWidth();

        int height = bi.getHeight();

        BufferedImage bi2 = new BufferedImage(bi.getHeight(), bi.getWidth(), type);

        Graphics2D gi = bi.createGraphics();

        if (degree == 90) {

            gi.translate(width + height - width, 0);

            gi.rotate(Math.toRadians(90));

        } else if (degree == 180) {

            gi.translate(width, height);

            gi.rotate(Math.toRadians(180));

        } else if (degree == -90) {

            gi.translate(0, width);

            gi.rotate(Math.toRadians(-90));

        }

        gi.drawImage(bi, 0, 0, null);

        gi.dispose();

        ImageIO.write(bi2, format, new File(newpath));

    }

    /**
     * 图形验证码
     *
     * @param ops -输出流
     * @return 返回验证码中校验字符串
     */
    public static String picCode(OutputStream ops) throws IOException {
        char[] CHARS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                'M', 'N', 'O', 'P', 'Q', 'R',
                'S', 'T', 'U', 'V', 'W', 'X',
                'Y', 'Z'};
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            sb.append(CHARS[random.nextInt(CHARS.length)] + " ");
        }
        Color backGroundColor = new Color(109, 190, 230);
        Color foreGroundColor = new Color(0, 0, 0);
        Graphics2D picCodeG2d = picCodeBi.createGraphics();
        picCodeG2d.setColor(backGroundColor);
        picCodeG2d.fillRect(0, 0, 100, 30);
        picCodeG2d.setFont(new Font("宋体", Font.BOLD, 20));
        picCodeG2d.setColor(foreGroundColor);
        picCodeG2d.drawString(sb.toString(), 10, 20);
        for (int i = 0; i < 4; i++) {
            int x1 = random.nextInt(picCodeBi.getWidth());
            int y1 = random.nextInt(picCodeBi.getHeight());
            int x2 = random.nextInt(picCodeBi.getWidth());
            int y2 = random.nextInt(picCodeBi.getHeight());
            picCodeG2d.drawLine(x1, y1, x2, y2);
        }
        picCodeG2d.drawImage(picCodeBi, 0, 0, 0, 0, null);
        picCodeG2d.dispose();
        ImageIO.write(picCodeBi, "jpg", ops);
        ops.close();
        String code = sb.toString().replace(" ", "");
        sb.setLength(0);
        return code;
    }

    /**
     * 合并两张图片
     *
     * @param path1   -源图1
     * @param path2   -源图2
     * @param newPath -新路径
     * @param x       -path1的x起点
     * @param y       -path1的y起点
     * @param format  -格式
     */
    public static void mergeImage(String path1, String path2, String newPath, int x, int y, String format) throws IOException {

        BufferedImage bi = ImageIO.read(new File(path1));

        BufferedImage bi2 = ImageIO.read(new File(path2));

        Graphics2D g = bi.createGraphics();

        g.drawImage(bi2, null, x, y);

        g.dispose();

        ImageIO.write(bi, format, new File(newPath));

    }

    /**
     * 图片切片
     *
     * @param path    -源图路径
     * @param rows    -x方向切片数量
     * @param columns -Y向切片数量
     * @return 切片路径list
     */
    public static List<String> silces(String path, int rows, int columns) throws IOException {

        BufferedImage bi = ImageIO.read(new File(path));

        int width = bi.getWidth() / rows;

        int height = bi.getHeight() / columns;

        int k = 1;

        int index = path.indexOf(".");

        String newPath = path.substring(0, index);

        List<String> list = new ArrayList<String>();

        for (int i = 0; i < rows; i++) {

            for (int j = 0; j < columns; j++) {

                String newPath2 = newPath + k + ".jpg";

                BufferedImage bi2 = bi.getSubimage(rows * width, height * columns, width, height);

                ImageIO.read(new File(newPath2));

                list.add(newPath2);

            }

        }

        return list;
    }
}
