package com.aljabi.masterarbeit.skew;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;


/*
Text Skew Angle detection on scanned document images by J.J. Hull
REF: Hull, J.J. "Document image skew detection: survey and annotated bibliography," In J.J. Hull, S.L. Taylor (eds.), Document Analysis Systems II, World Scientific Publishing Co., 1997, pp. 40-64.
*/

    public class HullSkewDetect {

        static final String SOURCE_PATH="Input_PATH";
        static final String DEST_PATH="Output_PATH";
        static final String OPENCV_PATH="OPENCV_PATH";

        static final Scalar COLOR_GREEN = new Scalar(0, 255, 0);
        static final Scalar COLOR_RED = new Scalar(0, 0, 255);
       /* public  Mat bufferedImageToMat(BufferedImage img) {
            Mat mat = new Mat(img.getHeight(), img.getWidth(), CvType.CV_8UC3);
            byte[] data = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
            mat.put(0, 0, data);
            return mat;
        }*/

        public  Mat RotateImage(Mat rotImg,double theta)
        {
            Mat rotatedImage = new Mat();
            try{
                double angleToRot=theta;


                if(angleToRot>=92 && angleToRot<=93)
                {
                    Core.transpose(rotImg, rotatedImage);
                }
                else
                {
                    org.opencv.core.Point center = new org.opencv.core.Point(rotImg.cols()/2, rotImg.rows()/2);
                    Mat rotImage = Imgproc.getRotationMatrix2D(center, angleToRot, 1.0);

                    Imgproc.warpAffine(rotImg, rotatedImage, rotImage, rotImg.size());
                }
            }
            catch(Exception e){}
            return rotatedImage;

        }



        public int skewDetectPixelRotation(Mat mat) {
            int[] projections = null;
            int[] angle_measure=new int[181];

            for(int theta=0;theta<=180;theta=theta+5)
            {
                projections=new int[mat.rows()];
                for(int i=0;i<mat.rows();i++)
                {
                    double[] pixVal;
                    for(int j=0;j<mat.cols();j++)
                    {
                        pixVal= mat.get(i, j);
                        if(pixVal[0]==0)//black pixel
                        {
                            int new_row=rotate(i,j,theta,mat);
                            if(new_row>=0 && new_row<mat.rows())
                                projections[new_row]++;
                        }
                    }
                }
                Mat tempMat=mat.clone();
                for(int r=0;r<mat.rows();r++)
                {
                    DrawProjection(r,projections[r],tempMat);
                }
//                Highgui.imwrite(DEST_PATH+"/out_"+theta+".jpg",tempMat);
                angle_measure[theta]=criterion_func(projections);

            }
            int angle=0;
            int val=0;
            for(int i=0;i<angle_measure.length;i++)
            {
                if(val<angle_measure[i])
                {
                    val=angle_measure[i];
                    angle=i;
                }
            }
            return angle;
        }

        private static int criterion_func(int[] projections) {
            int max=0;
            //use below code for image rotation
            //for(int i=0;i<projections.length-1;i++)
            //result+=Math.pow((projections[i+1]-projections[i]), 2);
            for(int i=0;i<projections.length;i++)
            {
                if(max<projections[i])
                {
                    max=projections[i];
                }
            }

            return max;
        }


        //Rotation about the center of the image
        private static int rotate(double y1, double x1, int theta,Mat mat) {
            int x0=mat.cols()/2;
            int y0=mat.rows()/2;

            int new_col=(int) ((x1-x0)*Math.cos(Math.toRadians(theta))-(y1-y0)*Math.sin(Math.toRadians(theta))+x0);
            int new_row=(int) ((x1-x0)*Math.sin(Math.toRadians(theta))+(y1-y0)*Math.cos(Math.toRadians(theta))+y0);

            return new_row;

        }

        private static void DrawProjection(int rownum,int projCount,Mat image) {
            final Point pt1 = new Point(0, -1);
            final Point pt2 = new Point();
            pt1.y = rownum;
            pt2.x = projCount;
            pt2.y = rownum;
           // Core.line(image, pt1, pt2, COLOR_GREEN);
        }


        public static void appendLog(String destPath,String text)
        {
            File logFile = new File(destPath+"/Anglelog.txt");
            if (!logFile.exists())
            {
                try
                {
                    logFile.createNewFile();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            try
            {
                //BufferedWriter for performance, true to set append to file flag
                BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
                buf.append(text);
                buf.newLine();
                buf.close();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        /**
         * OpenCV-4.0.0 Hough Transform - Line Detection
         * @paraments:
         * @return:void
         * @date: January 18, 2019 9:18:08 AM
         */
        public  void houghLines(Mat src) {
            Mat gary=new Mat();
            Mat lines=new Mat();
            //1. Edge processing
            Imgproc.Canny(src, gary, 100,200);
            //2. Hough transform - line detection
            Imgproc.HoughLinesP(gary,lines, 1,Imgproc.HOUGH_GRADIENT/180.0, 1, 0, 0);
            double[] date;
            for(int i=0,len=lines.rows();i<len;i++) {
                date=lines.get(i, 0).clone();
 //              Imgproc.line(src,new Point((int)date[0],(int)date[1]), new Point((int)date[2],(int)date[3]) ,new Scalar(0, 255, 0) , 2, Imgproc.LINE_AA);
            }
            
//            HighGui.imshow("White Snake", src);
//            HighGui.waitKey(0);
        }


    }

