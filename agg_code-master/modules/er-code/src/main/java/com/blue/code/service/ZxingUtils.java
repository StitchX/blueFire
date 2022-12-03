package com.blue.code.service;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ZxingUtils {


    public static BufferedImage enQRCode(String contents, int width, int height) throws WriterException {
        //定义二维码参数
        final Map<EncodeHintType, Object> hints = new HashMap<>(8) {
            {
                //编码
                put(EncodeHintType.CHARACTER_SET, "UTF-8");
                //容错级别
                put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
                //边距
                put(EncodeHintType.MARGIN, 0);
            }
        };
        return enQRCode(contents, width, height, hints);
    }


    /**
     * 生成二维码
     *
     * @param contents 二维码内容
     * @param width    图片宽度
     * @param height   图片高度
     * @param hints    二维码相关参数
     * @return BufferedImage对象
     * @throws WriterException 编码时出错
     */
    public static BufferedImage enQRCode(String contents, int width, int height, Map<EncodeHintType, Object> hints) throws WriterException {
//        String uuid = UUID.randomUUID().toString().replace("-", "");
        //本地完整路径
//        String pathname = path + "/" + uuid + "." + format;
        //生成二维码
        BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, width, height, hints);
//        Path file = new File(pathname).toPath();
        //将二维码保存到路径下
//        MatrixToImageWriter.writeToPa(bitMatrix, format, file);
//        return pathname;
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }


    /**
     * 将图片绘制在背景图上
     *
     * @param backgroundPath  背景图路径
     * @param zxingImage      图片
     * @param supervisionName 监管局名称
     * @param merchantName    店铺名称
     * @param y               图片在背景图上绘制的y轴起点
     * @return BufferedImage  合并后的图片
     */
    public static BufferedImage drawImage(String backgroundPath, BufferedImage zxingImage, String supervisionName, String merchantName, int y) throws IOException {
        //读取背景图的图片流
        BufferedImage backgroundImage;
        //Try-with-resources 资源自动关闭,会自动调用close()方法关闭资源,只限于实现Closeable或AutoCloseable接口的类
        //System.out.println(backgroundPath);
        //System.out.println(URLUtil.decode(backgroundPath));
        //System.out.println(new File(backgroundPath));
        try (InputStream imagein = new FileInputStream(backgroundPath)) {
            backgroundImage = ImageIO.read(imagein);
        }


        return drawImage(backgroundImage, zxingImage, supervisionName, merchantName, y);

    }


    /**
     * 将图片绘制在背景图上
     *
     * @param backgroundImage 背景图
     * @param zxingImage      二维码
     * @param supervisionName 监管局名称
     * @param merchantName    店铺名称
     * @param y               二维码图片在背景图上绘制的y轴起点
     * @return BufferedImage  合并后的图片
     * @throws IOException 文件写入错误
     */
    public static BufferedImage drawImage(BufferedImage backgroundImage, BufferedImage zxingImage, String supervisionName, String merchantName, int y) throws IOException {
        Objects.requireNonNull(backgroundImage, ">>>>>背景图不可为空");
        Objects.requireNonNull(zxingImage, ">>>>>二维码不可为空");
        //二维码宽度+x不可以超过背景图的宽度,长度同理
        if ((zxingImage.getWidth()) > backgroundImage.getWidth() || (zxingImage.getHeight() + y) > backgroundImage.getHeight()) {
            throw new IOException(">>>>>二维码宽度+x不可以超过背景图的宽度,长度同理");
        }


        //合并图片
        Graphics2D g = backgroundImage.createGraphics();
        int x = (backgroundImage.getWidth() - zxingImage.getWidth()) / 2; //二维码居中左偏移量

        g.drawImage(zxingImage, x, y,
                zxingImage.getWidth(), zxingImage.getHeight(), null);

        Font font = new Font("微软雅黑", Font.PLAIN, 40);
        int supervisionLeftOffset = getStringLeftOffset(backgroundImage, supervisionName, 40);
        if (supervisionLeftOffset <= 0) {
            throw new IOException(">>>>>监管局名称文字宽度不可以超过背景图的宽度");
        }

        g.setFont(font);
        g.drawString(supervisionName, supervisionLeftOffset, 160);


        int leftOffset = getStringLeftOffset(backgroundImage, merchantName, 40);

        if (leftOffset <= 0) {
            throw new IOException(">>>>>商户名称文字宽度不可以超过背景图的宽度");
        }


        g.setColor(Color.BLACK);
        g.drawString(merchantName, leftOffset, 920);

        //g.drawString(merchantName,200,800);
        return backgroundImage;
    }


    /**
     * 空码二维码拼接
     *
     * @param backgroundPath 背景图url
     * @param zxingImage      二维码
     * @param merchantName    店铺名称
     * @param y               二维码图片在背景图上绘制的y轴起点
     * @return BufferedImage  合并后的图片
     * @throws IOException 文件写入错误
     */
    public static BufferedImage drawImageEmpty(String backgroundPath, BufferedImage zxingImage, String merchantName, int y) throws IOException {
        //读取背景图的图片流
        BufferedImage backgroundImage;
        //Try-with-resources 资源自动关闭,会自动调用close()方法关闭资源,只限于实现Closeable或AutoCloseable接口的类
        //System.out.println(backgroundPath);
        //System.out.println(URLUtil.decode(backgroundPath));
        //System.out.println(new File(backgroundPath));
        try (InputStream imagein = new FileInputStream(backgroundPath)) {
            backgroundImage = ImageIO.read(imagein);
        }



        Objects.requireNonNull(backgroundImage, ">>>>>背景图不可为空");
        Objects.requireNonNull(zxingImage, ">>>>>二维码不可为空");
        //二维码宽度+x不可以超过背景图的宽度,长度同理
        if ((zxingImage.getWidth()) > backgroundImage.getWidth() || (zxingImage.getHeight() + y) > backgroundImage.getHeight()) {
            throw new IOException(">>>>>二维码宽度+x不可以超过背景图的宽度,长度同理");
        }


        //合并图片
        Graphics2D g = backgroundImage.createGraphics();
        int x = (backgroundImage.getWidth() - zxingImage.getWidth()) / 2; //二维码居中左偏移量

        g.drawImage(zxingImage, x, y,
                zxingImage.getWidth(), zxingImage.getHeight(), null);




        //int leftOffset = getStringLeftOffset(backgroundImage, merchantName, 40);
        int leftOffset = (backgroundImage.getWidth() - merchantName.length()*20)/2;

        if (leftOffset <= 0) {
            throw new IOException(">>>>>商户名称文字宽度不可以超过背景图的宽度");
        }

        Font font = new Font("微软雅黑", Font.PLAIN, 40);
        g.setFont(font);
        g.setColor(Color.GRAY);
        g.drawString(merchantName, leftOffset, 1020);

        //g.drawString(merchantName,200,800);
        return backgroundImage;
    }

    /**
     * 获取文字拼接图片的左偏移量
     *
     * @param backgroundImage 背景图片
     * @param text            需要添加的文字
     * @param size            字体大小
     * @return x坐标偏移量
     */
    private static int getStringLeftOffset(BufferedImage backgroundImage, String text, int size) {
        int width = backgroundImage.getWidth();

        return (width - text.length() * size) / 2;
    }


    public static InputStream bufferedImageToInputStream(BufferedImage backgroundImage) throws IOException {
        return bufferedImageToInputStream(backgroundImage, "png");
    }

    /**
     * backgroundImage 转换为输入流
     *
     * @param backgroundImage 图片
     * @param format          文件格式
     * @return InputStream  输入流
     * @throws IOException 文件写入异常
     */
    public static InputStream bufferedImageToInputStream(BufferedImage backgroundImage, String format) throws IOException {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        try (
                ImageOutputStream
                        imOut = ImageIO.createImageOutputStream(bs)) {
            ImageIO.write(backgroundImage, format, imOut);
            return new ByteArrayInputStream(bs.toByteArray());
        }
    }
}