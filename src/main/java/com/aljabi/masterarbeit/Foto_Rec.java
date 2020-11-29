package com.aljabi.masterarbeit;

import com.aljabi.masterarbeit.imageprocessing.ImageProcessor;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Foto_Rec {
    private BufferedImage image;
    private static ImageProcessor imgProcessor;

    public static void main(String args[]) throws IOException {

        StdDraw stdDraw;
        System.out.println("Started");
        imgProcessor = new ImageProcessor();
        /*System.out.println("Enter the file name :");
        Scanner ne1 = new Scanner(System.in);
        String filename = ne1.nextLine();*/
        String filename = "BMW_1.jpeg";
        String new_filename = "Test.png";
        BufferedImage orgImage = imgProcessor.loadImage(filename);
        BufferedImage copyImage = imgProcessor.duplicateBufferedImage(orgImage);
        imgProcessor.saveImage(new_filename, copyImage);
        BufferedImage GrayImage=imgProcessor.getGrayImage(copyImage);
        imgProcessor.saveImage("GrayFoto.png",GrayImage);
        BufferedImage VerticalEdges_Image=imgProcessor.verticalEdgeDetector(GrayImage);
        imgProcessor.saveImage("VerticalEdges.png",VerticalEdges_Image);
        BufferedImage HorizontalEdges_Image=imgProcessor.horizontalEdgeDetector(GrayImage);
        imgProcessor.saveImage("Horizontal_Edges.png",HorizontalEdges_Image);
        float [] H_y=imgProcessor.histogram(VerticalEdges_Image);
        System.out.println(H_y);
        //double[][] a = imgProcessor.(picture);
          //  drawHistogram(a);
        System.out.println("finished");
    }

  
   /*public float[][] bufferedImageToArray(BufferedImage image, int w, int h) {
        float[][] array = new float[w][h];
        for (int x=0; x<w; x++) {
            for (int y=0; y<h; y++) {
                array[x][y] = Photo.getBrightness(image,x,y);
            }
        }
        return array;
    }

    public float[][] bufferedImageToArrayWithBounds(BufferedImage image, int w, int h) {
        float[][] array = new float[w+2][h+2];

        for (int x=0; x<w; x++) {
            for (int y=0; y<h; y++) {
                array[x+1][y+1] = Photo.getBrightness(image,x,y);
            }
        }
        // vynulovat hrany :
        for (int x=0; x<w+2; x++) {
            array[x][0] = 1;
            array[x][h+1] = 1;
        }
        for (int y=0; y<h+2; y++) {
            array[0][y] = 1;
            array[w+1][y] = 1;
        }
        return array;
    }    
    
    static public BufferedImage arrayToBufferedImage(float[][] array, int w, int h) {
        BufferedImage bi = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
        for (int x=0; x<w; x++) {
            for (int y=0; y<h; y++) {
                Photo.setBrightness(bi,x,y,array[x][y]);
            }
        }
        return bi;
    }    

    static public BufferedImage createBlankBi(BufferedImage image) {
        BufferedImage imageCopy = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_RGB);
        return imageCopy;
    }
  
    public BufferedImage createBlankBi(int width, int height) {
        BufferedImage imageCopy = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        return imageCopy;
    }
    
    public BufferedImage sumBi(BufferedImage bi1, BufferedImage bi2) { //used by edgeDetectors
        BufferedImage out = new BufferedImage(Math.min(bi1.getWidth(), bi2.getWidth()),
                Math.min(bi1.getHeight(), bi2.getHeight()),
                BufferedImage.TYPE_INT_RGB);
        
        for (int x=0; x<out.getWidth(); x++)
            for (int y=0; y<out.getHeight(); y++) {
            this.setBrightness(out,x,y, (float)Math.min(1.0, this.getBrightness(bi1,x,y) + this.getBrightness(bi2,x,y) ) );
            }
        return out;
    }
    
    public void plainThresholding(Statistics stat) {
        int w = this.getWidth();
        int h = this.getHeight();
        for (int x=0; x<w; x++) {
            for (int y=0;y<h; y++) {
                this.setBrightness(x,y,stat.thresholdBrightness(this.getBrightness(x,y),1.0f));
            }
        }
    }
    
    /**ADAPTIVE THRESHOLDING CEZ GETNEIGHBORHOOD - deprecated
    public void adaptiveThresholding() { // jedine pouzitie tejto funkcie by malo byt v konstruktore znacky 
        Statistics stat = new Statistics(this);
        int radius = Intelligence.configurator.getIntProperty("photo_adaptivethresholdingradius");
        if (radius == 0) {
            plainThresholding(stat);
            return;
        }
        int w = this.getWidth();
        int h = this.getHeight();

        float[][] sourceArray = this.bufferedImageToArray(this.image,w,h);
        float[][] destinationArray = this.bufferedImageToArray(this.image,w,h);

        int count;
        float neighborhood;
        
        for (int x=0; x<w; x++) {
            for (int y=0; y<h; y++) {
                // compute neighborhood
                count = 0;
                neighborhood = 0;
                for (int ix = x-radius; ix <=x+radius; ix++) {
                    for (int iy = y-radius; iy <=y+radius; iy++) {
                        if (ix >= 0 && iy >=0 && ix < w && iy < h) {
                            neighborhood += sourceArray[ix][iy];
                            count++;
                        } 
                        /********/
//                        else {
//                            neighborhood += stat.average;
//                            count++;
//                        }
    /********
     }
     }
     neighborhood /= count;
     //
     if (destinationArray[x][y] < neighborhood) {
     destinationArray[x][y] = 0f;
     }  else {
     destinationArray[x][y] = 1f;
     }
     }
     }
     this.image = arrayToBufferedImage(destinationArray,w,h);
     }

     public HoughTransformation getHoughTransformation() {
     HoughTransformation hough = new HoughTransformation(this.getWidth(), this.getHeight());
     for (int x=0; x<this.getWidth();x++) {
     for (int y=0; y<this.getHeight(); y++) {
     hough.addLine(x,y,this.getBrightness(x,y));
     }
     }
     return hough;
     } */
}
