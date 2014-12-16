package cn.kenshin.image.watermark;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import net.coobird.thumbnailator.Thumbnails;

/**
 * 补充Thumbnails不能进行打多个水印的不足
 * 同时利用Thumbnails进行了打斜的水印
 */
public class MultiWaterMarker {

    private int gridWidth = 500;
    private int gridHeight = 300;

    private float opacity = 0.3f;// 不透明度
    private int rotation = 330;// 旋转度

    /**
     * 按栅格打印多个水印
     *
     * @param gridWidth  单元格宽
     * @param gridHeight 单元格高
     */
    public MultiWaterMarker(int gridWidth, int gridHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
    }

    /**
     * @param targetImg 目标图片地址
     * @param pressImg  水印图片地址
     * @param opacity   不透明度
     * @throws java.io.IOException
     */
    public void waterMark(String targetImg, String pressImg, float opacity) throws IOException {
        this.opacity = opacity;
        waterMark(targetImg, pressImg);
    }

    /**
     * @param targetImg 目标图片地址
     * @param pressImg  水印图片地址
     * @param rotation  水印图片旋转度
     * @param opacity   不透明度
     * @throws java.io.IOException
     */
    public void waterMark(String targetImg, String pressImg, int rotation, float opacity) throws IOException {
        this.opacity = opacity;
        this.rotation = rotation;
        waterMark(targetImg, pressImg);
    }

    /**
     * 把图片印刷到图片上
     *
     * @param targetImg 目标图片地址
     * @param pressImg  水印图片地址
     * @throws java.io.IOException
     */
    public void waterMark(String targetImg, String pressImg) throws IOException {
        BufferedImage waterMark = Thumbnails.of(pressImg).size(gridWidth, gridHeight).rotate(rotation)
                .asBufferedImage();

        Image imgSrc = ImageIO.read(new File(targetImg));
        int width = imgSrc.getWidth(null);
        int height = imgSrc.getHeight(null);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.drawImage(ImageIO.read(new File(targetImg)), 0, 0, width, height, null);

        for (int i = 0; i <= height / gridHeight; i++) {
            for (int j = 0; j <= width / gridWidth; j++) {
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, opacity));
                g.drawImage(waterMark, j * gridWidth, i * gridHeight, waterMark.getWidth(), waterMark.getHeight(), null);
            }
        }
        // 水印文件结束
        g.dispose();
        FileOutputStream out = new FileOutputStream(targetImg);
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        encoder.encode(image);
        out.close();
    }
}
