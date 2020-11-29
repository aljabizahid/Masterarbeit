package com.aljabi.masterarbeit.imageprocessing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.awt.Color;

public class ImageProcessor {
    public BufferedImage loadImage(String filepath) throws IOException {
        File source = new File(filepath);
        BufferedImage image = ImageIO.read(source);
        BufferedImage outimage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = outimage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return image;
    }

    public BufferedImage duplicateBufferedImage(BufferedImage image) {
        BufferedImage imageCopy = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        imageCopy.setData(image.getData());
        return imageCopy;
    }

    public void saveImage(String filepath, BufferedImage image) throws IOException {
        /*String type = new String(filepath.substring(filepath.lastIndexOf('.')+1,filepath.length()).toUpperCase());
        if (!type.equals("BMP") &&
                !type.equals("JPG") &&
                !type.equals("JPEG") &&
                !type.equals("PNG")
                ) throw new IOException("Unsupported file format");*/
        File destination = new File(filepath);
        ImageIO.write(image, "png", destination);
    }

    public BufferedImage verticalEdgeDetector(BufferedImage source) {
        BufferedImage destination = duplicateBufferedImage(source);
        float data1[] = {
                -1, 0, 1,
                -2, 0, 2,
                -1, 0, 1,
        };

        float data2[] = {
                1, 0, -1,
                2, 0, -2,
                1, 0, -1,
        };
        new ConvolveOp(new Kernel(3, 3, data1), ConvolveOp.EDGE_NO_OP, null).filter(destination, source);
        return source;
    }

    public BufferedImage horizontalEdgeDetector(BufferedImage source) {
        BufferedImage destination = duplicateBufferedImage(source);
        float data1[] = {
                -1, -2, -1,
                0, 0, 0,
                1, 2, 1,
        };

        float data2[] = {
                1, 2, 1,
                0, 0, 0,
                -1, -2, -1,
        };
        new ConvolveOp(new Kernel(3, 3, data1), ConvolveOp.EDGE_NO_OP, null).filter(destination, source);
        return source;
    }

    public BufferedImage getGrayImage(BufferedImage color_image) {
        int W = color_image.getWidth();
        int H = color_image.getHeight();
        BufferedImage GrayImage = new BufferedImage(color_image.getWidth(), color_image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int i = 1; i < W - 1; i++) {
            for (int j = 1; j < H - 1; j++) {
                int rgb = color_image.getRGB(i - 1, j - 1);
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = (rgb) & 0xff;
                //from https://en.wikipedia.org/wiki/Grayscale, calculating luminance
                int gray = (int) (0.2126 * r + 0.7152 * g + 0.0722 * b);
                //int gray = (r + g + b) / 3;
                int edgeColor = 0xff000000 | (gray << 16) | (gray << 8) | gray;
                GrayImage.setRGB(i, j, edgeColor);
            }
        }
        return GrayImage;
    }

    public float getBrightness(BufferedImage image, int x, int y) {
        int r = image.getRaster().getSample(x, y, 0);
        int g = image.getRaster().getSample(x, y, 1);
        int b = image.getRaster().getSample(x, y, 2);
        float[] hsb = Color.RGBtoHSB(r, g, b, null);
        return hsb[2];
    }

    public float[] histogram(BufferedImage bi) {
        float[] Brightniss_y = new float[bi.getHeight()];
        for (int y = 0; y < bi.getHeight(); y++) {
            float counter = 0;
            for (int x = 0; x < bi.getWidth(); x++) {
                counter += getBrightness(bi, x, y);
            }
            Brightniss_y[y] = counter;
        }
        return Brightniss_y;
    }

    public void ImageToHistogram (BufferedImage image) {

        public static double[][] convertPictureToArray (BufferedImage image)
        {
            int width = image.getWidth();
            int height = image.getHeight();
            double[][] pixels = new double[width][height];
            for (int col = 0; col < width; col++) {
                for (int row = 0; row < height; row++) {
                    int color = image.getRGB(col, row);
                    pixels[col][row] = getIntensity(color);
                }
            }
            return pixels;
        }
        public void drawHistogram(double[][] a)
        {
            int m = a.length;
            int n = a[0].length;
            int pixels = m*n;
            int[] histogram = new int[256];
            StdDraw.setPenColor(StdDraw.BOOK_BLUE);
            for (int i = 0; i < m; i++)
            {
                for (int j = 0; j < n; j++)
                {
                    int intensity = (int) a[i][j];
                    histogram[intensity]++;
                }
            }
            StdDraw.setXscale(0,256);
            StdDraw.setYscale(0,pixels/20); // obtained "20" by experimentation
            StdDraw.enableDoubleBuffering();
            for (int i = 0; i < 256; i++)
            {
                StdDraw.filledRectangle(i+0.5,histogram[i]/2,0.5,histogram[i]/2);
            }
            StdDraw.show();
        }
    }
    public double getIntensity(int color) // this method produces monochrome luminance
        {
            int r = (color >> 16) & 0xff;
            int g = (color >> 8) & 0xff;
            int b = (color) & 0xff;
            return 0.299*r + 0.587*g + 0.114*b;
        }
    }
}
