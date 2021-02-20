package com.aljabi.masterarbeit.Bandclipping;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.sun.j3d.utils.scenegraph.io.state.javax.media.j3d.LightState;
import edu.princeton.cs.introcs.StdDraw;
import edu.princeton.cs.introcs.StdStats;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;


public class Clipping {
    private int imageWidth;
    private int imageHeight;

    public Clipping(int imageWidth, int imageHeight) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        //StdDraw.setCanvasSize(imageWidth, imageHeight);
    }

    public int[] Vertical_projection(int[][] pixels, String filename) throws IOException {
        int cols = pixels.length;
        int rows = pixels[0].length;
        //System.out.println(rows);
        int[] histogramm_y = new int[rows];
        int histogrammsumme = 0;
        for (int y = 0; y < rows; y++) {

            for (int x = 0; x < cols; x++) {
                histogramm_y[y] += pixels[x][y];


            }
            histogrammsumme += histogramm_y[y];
            System.out.printf("Zeilnummer%s=%s\n ", y, histogramm_y[y]);
            //System.out.printf("Summe=%s\n ", histogrammsumme);

        }
        System.out.printf("Summe=%s\n ", histogrammsumme);
        System.out.printf("Mittelwert=%s\n ", histogrammsumme / pixels[0].length);

        drawHistogram(histogramm_y, "y", filename);
        int [] G= Spitzen(histogramm_y);
        return G;

    }

    public int[] Spitzen(int[] Zeile) {
        int Spitze1 = 0;
        int Zeiger1 = 0;
        int Spitze2 = 0;
        int Zeiger2 = 0;
        int Spitze3 = 0;
        int Zeiger3 = 0;
        int rows = Zeile.length;
        for (int y = 0; y < rows; y++) {
            if (Zeile[y] > Spitze1) {
                Spitze1 = Zeile[y];
                Zeiger1 = y;
            }
        }
        int git = rows /10;
        int UnterGrenze1 = Zeiger1 - git;
        int UeberGrenze1 = Zeiger1 + git;
        int[] gerenze = new int[2];
        gerenze[0] = UnterGrenze1;
        gerenze[1] = UeberGrenze1;
        System.out.printf("Spitze1=%s\nZeiger1=%s\n", Spitze1, Zeiger1);
        for (int y = 0; y < rows; y++) {
            if (!(y > UnterGrenze1 & y < UeberGrenze1)) {
                if (Zeile[y] > Spitze2) {
                    Spitze2 = Zeile[y];
                    Zeiger2 = y;
                }

            }
        }
        int UnterGrenze2 = Zeiger2 - git;
        int UeberGrenze2 = Zeiger2 + git;
        System.out.printf("Spitze2=%s\nZeiger2=%s\n", Spitze2, Zeiger2);
        for (int y = 0; y < rows; y++) {
            if (!((y > UnterGrenze1 & y < UeberGrenze1) || (y > UnterGrenze2 & y < UeberGrenze2))) {
                if (Zeile[y] > Spitze3) {
                    Spitze3 = Zeile[y];
                    Zeiger3 = y;
                }

            }

        }
        System.out.printf("Spitze3=%s\nZeiger3=%s\n", Spitze3, Zeiger3);
        return gerenze;
    }

    public BufferedImage Bildclliping(BufferedImage img, int unter, int ueber) {
        BufferedImage Image = new BufferedImage(img.getWidth(), ueber - unter, BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < img.getWidth(); i++) {
            for (int k = unter; k < ueber; k++) {
                int rgb = img.getRGB(i, k);
                Image.setRGB(i, k-unter, rgb);
            }

        }
        return Image;
    }

    public void Horizontal_projection(int[][] pixels, String filename) throws IOException {
        int cols = pixels.length;
        int rows = pixels[0].length;
        int[] histogramm_x = new int[cols];
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                histogramm_x[x] += pixels[x][y];
            }
            //System.out.println(histogramm_x[x]);
        }
        drawHistogram(histogramm_x, "x", filename);
    }

    public void drawHistogramx(int[] a, String R, String filename) throws IOException {
        Histogram histogram = new Histogram(a.length);
        for (int i = 0; i < a.length; i++) {
            histogram.addDataPoint(a[i]);
        }
        //StdDraw.setCanvasSize(a.length, a.length);
        histogram.draw();
    }
    public void drawHistogram(int[] a, String R, String filename) throws IOException {
        double[] x = new double[a.length];
        double[] y = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            y[i] = (double) a[i];
        }
        for (int i = 0; i < a.length; i++) {
            x[i] = i;
        }
        File file = new File(filename);
        BufferedImage img = ImageIO.read(file);
//        StdDraw.setCanvasSize(700, 700);
//        StdDraw.picture(img.getWidth()/2,img.getHeight()/2,filename);


        if (R == "y") {
            List list = Arrays.asList(y);
            StdDraw.setYscale(-1, 200);  // to leave a little border
            StdStats.plotBars(y);
        }
        else{
            List list = Arrays.asList(y);
            StdDraw.setYscale(-1, 200);  // to leave a little border
            StdStats.plotBars(y);
        }


//        if (R == "y") {
//            StdDraw.setXscale(100, 0);
//            StdDraw.setYscale(a.length, 0);
//            StdDraw.setPenColor(StdDraw.BOOK_RED);
//            StdDraw.polygon(y, x);
//        } else if (R == "x") {
//            StdDraw.setXscale(0, a.length);
//            StdDraw.setYscale(0, 40);
//            StdDraw.setPenColor(StdDraw.BOOK_RED);
//            StdDraw.polygon(x, y);
//        }
//        if (R == "y") {
//            StdDraw.setXscale(100, 0);
//            StdDraw.setYscale(a.length, 0);
//            StdDraw.setPenColor(StdDraw.BOOK_RED);
//            //StdDraw.line(x, 0,x,y);
//            StdDraw.polygon(y, x);
//        } else if (R == "x") {
//            StdDraw.setXscale(0, a.length);
//            StdDraw.setYscale(0, 40);
//            StdDraw.setPenColor(StdDraw.BOOK_RED);
//            StdDraw.polygon(x, y);
//        }
        StdDraw.show();
    }
    public int[] Ableitung(int[] Spalten){
        int[] Anderung=new int[Spalten.length];
        String filename;
        Anderung[0]=0;
        int y;
        int Zaehler=0;

        int FlankenAbstand=Spalten.length/8;
        for (int x=1;x<Spalten.length;x++){
            y=Spalten[x]-Spalten[x-1];
            if(!(y>-2&y<2)) {
                Anderung[x] = y;
                Zaehler ++;
                System.out.printf("Zahler%s=%s\n",Zaehler,x);
            }else {
                Anderung[x]=0;
            }
        }

        int[] Flanke= new int[Zaehler+1];
        int F=0;
        for (int x=0;x<Spalten.length;x++){
            if (Anderung[x]!=0){
                F=F+1;
                Flanke[F]=x;
                System.out.printf("Flanke%s=%s\n",F,Flanke[F]);

            }
        }
        int Zeiger=1;
        int GN=0;
        int[] Gruppe=new int[30];
        for (int x=1;x<Flanke.length-1;x++){
            if( Flanke[x+1]-Flanke[x]<FlankenAbstand){
                Zeiger=Zeiger+1;
                //System.out.printf("Zeiger%s",Zeiger);
            }
            else {

                Gruppe[GN]=Zeiger;
                System.out.printf("Gr%s=%s\n",GN,Zeiger);
                GN=GN+1;
                Zeiger=0;
            }
        }

        return Anderung;
    }
}


