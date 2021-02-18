package com.aljabi.masterarbeit.imageprocessing;

import edu.princeton.cs.introcs.StdDraw;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import sun.security.provider.ConfigFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.lang.Math;

public class ImageProcessor<voids> {
    private static final String OUTPUT_PATH = "output_path/";
    private static final String INPUT_PATH = "input_path/";

    public BufferedImage loadImage(String filepath) throws IOException {
        File source = new File(INPUT_PATH + filepath);
        BufferedImage image = ImageIO.read(source);
        BufferedImage outimage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = outimage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return image;
    }

    public BufferedImage duplicateBufferedImage(BufferedImage image, boolean Adaptive) {
        if (Adaptive) {
            BufferedImage imageCopy = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            imageCopy.setData(image.getData());
            return imageCopy;
        } else {
            BufferedImage imageCopy = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            imageCopy.setData(image.getData());
            return imageCopy;
        }
    }

    public void saveImage(String filepath, BufferedImage image) throws IOException {

        File destination = new File(OUTPUT_PATH + filepath);
        ImageIO.write(image, "png", destination);
    }

    public int[][] FotoToArray(BufferedImage img) {
        int[][] img_pixels = new int[img.getWidth()][img.getHeight()];
        System.out.println(img.getWidth());
        System.out.println(img.getHeight());
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                int rgb = img.getRGB(i, j);
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = (rgb) & 0xff;
                img_pixels[i][j] = (r + g + b) / 3;
                //System.out.println(img_pixels[i][j]);
                if (img_pixels[i][j] > 220) {
                    img_pixels[i][j] = 1;
                } else {
                    img_pixels[i][j] = 0;

                }
                //
            }

        }
        return img_pixels;
    }

    public Mat BinaryFoto(Mat img, boolean Ad) {
        Mat Out = new Mat();
        if (Ad) {
            Imgproc.adaptiveThreshold(img, Out, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 15, -2);
        } else {
            Imgproc.threshold(img, Out, 180, 500, Imgproc.THRESH_BINARY);
        }
        return Out;

    }




    public BufferedImage getGrayImage(BufferedImage color_image, boolean Ad) {
        int W = color_image.getWidth();
        int H = color_image.getHeight();
        if (Ad) {
            BufferedImage GrayImage = new BufferedImage(color_image.getWidth(), color_image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
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

        } else {
            BufferedImage GrayImage = new BufferedImage(color_image.getWidth(), color_image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
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
    }
    public BufferedImage MedianFilter(BufferedImage img) {
        BufferedImage Image = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        Color[] pixel = new Color[9];
        int[] R = new int[9];
        int[] B = new int[9];
        int[] G = new int[9];
        for (int i = 1; i < img.getWidth() - 1; i++) {
            for (int j = 1; j < img.getHeight() - 1; j++) {
                /*pixel[0] = new Color(img.getRGB(i - 2, j - 2));
                pixel[1] = new Color(img.getRGB(i - 1, j - 2));
                pixel[2] = new Color(img.getRGB(i, j - 2));
                pixel[3] = new Color(img.getRGB(i + 1, j - 2));
                pixel[4] = new Color(img.getRGB(i + 2, j - 2));
                pixel[5] = new Color(img.getRGB(i - 2, j - 1));
                pixel[6] = new Color(img.getRGB(i - 1, j - 1));
                pixel[7] = new Color(img.getRGB(i, j - 1));
                pixel[8] = new Color(img.getRGB(i + 1, j - 1));
                pixel[9] = new Color(img.getRGB(i + 2, j - 1));
                pixel[10] = new Color(img.getRGB(i - 2, j));
                pixel[11] = new Color(img.getRGB(i - 1, j));
                pixel[12] = new Color(img.getRGB(i, j));
                pixel[13] = new Color(img.getRGB(i + 1, j));
                pixel[14] = new Color(img.getRGB(i + 2, j));
                pixel[15] = new Color(img.getRGB(i - 2, j + 1));
                pixel[16] = new Color(img.getRGB(i - 1, j + 1));
                pixel[17] = new Color(img.getRGB(i, j - 1));
                pixel[18] = new Color(img.getRGB(i + 1, j + 1));
                pixel[19] = new Color(img.getRGB(i + 2, j + 1));
                pixel[20] = new Color(img.getRGB(i - 2, j + 2));
                pixel[21] = new Color(img.getRGB(i - 1, j + 2));
                pixel[22] = new Color(img.getRGB(i, j + 2));
                pixel[23] = new Color(img.getRGB(i + 1, j + 2));
                pixel[24] = new Color(img.getRGB(i + 2, j + 2));*/
                pixel[0] = new Color(img.getRGB(i - 1, j - 1));
                pixel[1] = new Color(img.getRGB(i, j - 1));
                pixel[2] = new Color(img.getRGB(i + 1, j - 1));
                pixel[3] = new Color(img.getRGB(i - 1, j));
                pixel[4] = new Color(img.getRGB(i, j));
                pixel[5] = new Color(img.getRGB(i + 1, j));
                pixel[6] = new Color(img.getRGB(i - 1, j + 1));
                pixel[7] = new Color(img.getRGB(i, j + 1));
                pixel[8] = new Color(img.getRGB(i + 1, j + 1));
                for (int k = 0; k < 9; k++) {
                    R[k] = pixel[k].getRed();
                    B[k] = pixel[k].getBlue();
                    G[k] = pixel[k].getGreen();
                }
                Arrays.sort(R);
                Arrays.sort(G);
                Arrays.sort(B);
                Image.setRGB(i, j, new Color(R[4], G[4], B[4]).getRGB());
            }
        }
        return Image;
    }
    public  BufferedImage Mat2BufferedImage(Mat matrix)throws IOException {
        MatOfByte mob=new MatOfByte();
        Imgcodecs.imencode(".jpg", matrix, mob);
        return ImageIO.read(new ByteArrayInputStream(mob.toArray()));
    }
    public  Mat BufferedImage2Mat(BufferedImage image) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", byteArrayOutputStream);
        byteArrayOutputStream.flush();
        return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
    }



}
