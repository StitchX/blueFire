package com.blue.crm.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @author fcq
 * @version v2.0.2.crm
 * @description 图片工具类
 * @date 2022/3/16 14:49
 */
public class ImageUtil {

    //水印透明度
    private static final float alpha = 0.9f;
    //水印文字字体
    private static final Font font = new Font("宋体", Font.BOLD, 500);
    //水印文字颜色
    private static final Color color = Color.RED;

    public static void addWatermark(InputStream inputStream, String content, OutputStream outputStream) throws IOException {
        BufferedImage srcImage = ImageIO.read(inputStream);
        int height = srcImage.getHeight();
        int width = srcImage.getWidth();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.drawImage(srcImage, 0, 0, width, height, null);
        graphics.setFont(font);
        graphics.setColor(color);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
        FontMetrics fontMetrics = graphics.getFontMetrics(font);
        int stringWidth = fontMetrics.stringWidth(content);
        int stringHeight = fontMetrics.getHeight();

//        graphics.translate(-width / 5, height / 4);
        graphics.rotate(30 * Math.PI / 180);

        int x = (width - stringWidth) / 2;
        int y = (height - stringHeight) / 2;
        graphics.drawString(content, 0, 0);

        graphics.dispose();

        ImageIO.write(bufferedImage, "png", outputStream);
    }

    public static void main(String[] args) throws IOException {
        File file = new File("D:/Data/桌面/营业执照1.png");
        FileInputStream fileInputStream = new FileInputStream(file);
        File out = new File("D:/Data/桌面/营业执照2.png");
        FileOutputStream fileOutputStream = new FileOutputStream(out);
        addWatermark(fileInputStream, "蓝色火焰", fileOutputStream);
        System.out.println("添加水印完成");
    }
}
