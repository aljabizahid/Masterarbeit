package com.aljabi.masterarbeit;

import com.aljabi.masterarbeit.imageprocessing.ImageProcessor;
import com.aljabi.masterarbeit.skew.HullSkewDetect;
import com.aljabi.masterarbeit.Hough.Deteection_Correction;
import com.aljabi.masterarbeit.Bandclipping.Clipping;
import com.aljabi.masterarbeit.ImageAnalysis.ImageAnalysis;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.opencv.core.Mat;
import org.opencv.core.Core;

public class Main {
    static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
    private BufferedImage image;
    private static ImageProcessor imgProcessor;
    private static ImageAnalysis imageAnalysis;
    private static HullSkewDetect skewDetCorr;
    private static Deteection_Correction Hogh_Transform;
    private static Clipping clipping;
    public static void main(String args[]) throws IOException {

        System.out.println(Core.VERSION);
        System.out.println("Started");
        imgProcessor = new ImageProcessor();
        skewDetCorr = new HullSkewDetect();
        Hogh_Transform = new Deteection_Correction();
        imageAnalysis=new ImageAnalysis();


        String filename = "Test_20.jpg";
        boolean Adaptive= true;
        // String new_filename = "Test.png";
        BufferedImage orgImage = imgProcessor.loadImage(filename);
        clipping = new Clipping(orgImage.getWidth(), orgImage.getHeight());

        BufferedImage copyImage = imgProcessor.duplicateBufferedImage(orgImage,Adaptive);
        // imgProcessor.saveImage(new_filename, copyImage);
        BufferedImage GrayImage = imgProcessor.getGrayImage(copyImage,Adaptive);
        imgProcessor.saveImage("GrayFoto.png", GrayImage);
        Mat BinaryMat =imgProcessor.BinaryFoto(imgProcessor.BufferedImage2Mat(GrayImage),Adaptive);
        BufferedImage BinaryBild = imgProcessor.Mat2BufferedImage(BinaryMat);
        imgProcessor.saveImage("Binarybild.png",BinaryBild);
        int Opt = 1; // 0: without Median filter   1: with median filter
        if (Opt == 0) {
            // without Medianfilter
            Mat img = imgProcessor.BufferedImage2Mat(GrayImage);
            Mat rotatedimg = skewDetCorr.RotateImage(img,-1);
            BufferedImage rotatedImage = imgProcessor.Mat2BufferedImage(rotatedimg);
            BufferedImage VerticalEdges_Image = imageAnalysis.verticalEdgeDetector(GrayImage,Adaptive);
            imgProcessor.saveImage("VerticalEdges.png", VerticalEdges_Image);
            BufferedImage HorizontalEdges_Image = imageAnalysis.horizontalEdgeDetector(GrayImage,Adaptive);
            imgProcessor.saveImage("Horizontal_Edges.png", HorizontalEdges_Image);
            int[][] Pixels_Gewichtet_V = imgProcessor.FotoToArray(VerticalEdges_Image);
            int[][] Pixels_Gewichtet_H = imgProcessor.FotoToArray(HorizontalEdges_Image);
           int[] G= clipping.Vertical_projection(Pixels_Gewichtet_V,"output_path/Binarybild.png");
            clipping.Horizontal_projection(Pixels_Gewichtet_H,"output_path/Binarybild.png");
            Mat Houghimg = imgProcessor.BufferedImage2Mat (VerticalEdges_Image);
            Hogh_Transform.start(Houghimg);
        } else if (Opt == 1) {

            BufferedImage img = GrayImage;
            BufferedImage VerticalEdges_Image_med = imageAnalysis.verticalEdgeDetector(img,Adaptive);
            imgProcessor.saveImage("VerticalEdges_med.png", VerticalEdges_Image_med);
            Mat BinaryMat_V =imgProcessor.BinaryFoto(imgProcessor.BufferedImage2Mat(VerticalEdges_Image_med),Adaptive);
            BufferedImage BinaryBild_V= imgProcessor.Mat2BufferedImage(BinaryMat_V);
            BufferedImage Median_Filter = imgProcessor.MedianFilter(BinaryBild_V);
            imgProcessor.saveImage("Median_Filter.png", Median_Filter);
            imgProcessor.saveImage("Binarybild_V.png",BinaryBild_V);
            int[][] Pixels_Gewichtet_V_med = imgProcessor.FotoToArray(BinaryBild_V);

            int[] G=clipping.Vertical_projection(Pixels_Gewichtet_V_med,"output_path/Binarybild.png");
            BufferedImage Band_1=clipping.Bildclliping(GrayImage,G[0],G[1]);
            imgProcessor.saveImage("Band_1.png",Band_1);
            BufferedImage HorizontalEdges_Image_med = imageAnalysis.horizontalEdgeDetector(Band_1,Adaptive);
            imgProcessor.saveImage("HorizontalEdges_med.png", HorizontalEdges_Image_med);
            Mat BinaryMat_H =imgProcessor.BinaryFoto(imgProcessor.BufferedImage2Mat(HorizontalEdges_Image_med),Adaptive);
            BufferedImage BinaryBild_H = imgProcessor.Mat2BufferedImage(BinaryMat_H);
            imgProcessor.saveImage("Binarybild_H.png",BinaryBild_H);
            int[][] Pixels_Gewichtet_H_med = imgProcessor.FotoToArray(BinaryBild_H);
            clipping.Horizontal_projection(Pixels_Gewichtet_H_med,"output_path/Binarybild.png");

        }
        else {
            System.out.println("Zahid");
        }
        System.out.println("finished");

    }



}
