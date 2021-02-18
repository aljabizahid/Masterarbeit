package com.aljabi.masterarbeit.Hough;

import java.awt.Image;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import org.opencv.core.MatOfPoint;
import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.awt.geom.AffineTransform;
import javax.imageio.ImageIO;
import javax.swing.*;

import static javafx.application.Application.launch;

public class Deteection_Correction {
    private static final String OUTPUT_PATH = "output_path/";

    public BufferedImage Shear(BufferedImage img){
        AffineTransform tx=new AffineTransform();
       /* tx.translate(img.getHeight()/2, img.getWidth()/2);
        tx.shear(-0.5 , 0);
        tx.translate(-img.getWidth()/2,-img.getHeight()/2);*/
        tx.rotate(Math.PI/12);
        tx.translate(-img.getWidth()/2,-img.getHeight()/2);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage newImage= new BufferedImage(img.getHeight(),img.getWidth(), BufferedImage.TYPE_4BYTE_ABGR);
        op.filter(img, newImage);
        return newImage;
//        JOptionPane.showMessageDialog(null,new JLabel(new ImageIcon(newImage)));
    }

    public void start(Mat src) throws IOException {
        //Loading the OpenCV core library
           /* System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
            String file ="D:\\Images\\road4.jpg";
            Mat src = Imgcodecs.imread(file);*/
        //Converting the image to Gray
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_RGBA2GRAY);
        //Detecting the edges
        Mat edges = new Mat();
        Imgproc.Canny(gray, edges, 60, 60 * 3, 3, false);
        // Changing the color of the canny
        Mat cannyColor = new Mat();
        Imgproc.cvtColor(edges, cannyColor, Imgproc.COLOR_GRAY2BGR);
        //Detecting the hough lines from (canny)
        Mat lines = new Mat();
        Imgproc.HoughLines(edges, lines, 1, Math.PI / 180, 300);
        for (int i = 0; i < lines.rows(); i++) {
            double[] data = lines.get(i, 0);
            double rho = data[0];
            double theta = data[1];
            double a = Math.cos(theta);
            double b = Math.sin(theta);
            double x0 = a * rho;
            double y0 = b * rho;
            //Drawing lines on the image
            Point pt1 = new Point();
            Point pt2 = new Point();
            pt1.x = Math.round(x0 + 1000 * (-b));
            pt1.y = Math.round(y0 + 1000 * (a));
            pt2.x = Math.round(x0 - 1000 * (-b));
            pt2.y = Math.round(y0 - 1000 * (a));
            Imgproc.line(cannyColor, pt1, pt2, new Scalar(0, 0, 255), 3);
        }
        BufferedImage img = Mat2BufferedImage(cannyColor);
        saveImage("Hough.png", img);
        //Converting matrix to JavaFX writable image
//            Image img = HighGui.toBufferedImage(cannyColor);

//            WritableImage writableImage= SwingFXUtils.toFXImage((BufferedImage) img, null);
//            //Setting the image view
//            ImageView imageView = new ImageView(writableImage);
//            imageView.setX(10);
//            imageView.setY(10);
//            imageView.setFitWidth(575);
//            imageView.setPreserveRatio(true);
//            //Setting the Scene object
//            Group root = new Group(imageView);
//            Scene scene = new Scene(root, 595, 400);
//            stage.setTitle("Hough Line Transform");
//            stage.setScene(scene);
//            stage.show();
    }

    public void run_Hough(Mat src) throws IOException {
        // Declare the output variables
        Mat dst = new Mat(), cdst = new Mat(), cdstP;
//        String default_file = "../../../../data/sudoku.png";
//        String filename = ((args.length > 0) ? args[0] : default_file);
        // Load an image
//        Mat src = Imgcodecs.imread(filename, Imgcodecs.IMREAD_GRAYSCALE);
        // Check if image is loaded fine
        if (src.empty()) {
            System.out.println("Error opening image!");
//            System.out.println("Program Arguments: [image_name -- default "
//                    + default_file +"] \n");
            System.exit(-1);
        }
        // Edge detection
        Imgproc.Canny(src, dst, 50, 150, 3, false);
        // Copy edges to the images that will display the results in BGR
        Imgproc.cvtColor(dst, cdst, Imgproc.COLOR_GRAY2BGR);
        cdstP = cdst.clone();
        // Standard Hough Line Transform
        Mat lines = new Mat(); // will hold the results of the detection
        Imgproc.HoughLines(dst, lines, 1, Math.PI / 180, 150); // runs the actual detection
        // Draw the lines
        for (int x = 0; x < lines.rows(); x++) {
            double rho = lines.get(x, 0)[0],
                    theta = lines.get(x, 0)[1];
            double a = Math.cos(theta), b = Math.sin(theta);
            double x0 = a * rho, y0 = b * rho;
            Point pt1 = new Point(Math.round(x0 + 1000 * (-b)), Math.round(y0 + 1000 * (a)));
            Point pt2 = new Point(Math.round(x0 - 1000 * (-b)), Math.round(y0 - 1000 * (a)));
            Imgproc.line(cdst, pt1, pt2, new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
        }
        // Probabilistic Line Transform
        Mat linesP = new Mat(); // will hold the results of the detection
        Imgproc.HoughLinesP(dst, linesP, 1, Math.PI / 180, 200, 100, 50); // runs the actual detection
        // Draw the lines
        for (int x = 0; x < linesP.rows(); x++) {
            double[] l = linesP.get(x, 0);
            Imgproc.line(cdstP, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
        }
        // Show results
        BufferedImage img_cdst = Mat2BufferedImage(cdst);
        saveImage("cdst.png", img_cdst);
        BufferedImage img_cdstP = Mat2BufferedImage(cdstP);
        saveImage("cdstP.png", img_cdstP);
    }

    public void Rect_Detection(Mat src) throws IOException {
        Mat dst = new Mat();
        Mat Contours_img=new Mat(src.size(), src.type());
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        int threshold = 100;
        Imgproc.Canny(src, dst, threshold, threshold * 3);
        Imgproc.findContours(dst, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        MatOfPoint2f matOfPoint2f = new MatOfPoint2f();
        MatOfPoint2f approxCurve = new MatOfPoint2f();

        for (int idx = 0; idx >= 0; idx = (int) hierarchy.get(0, idx)[0]) {
            MatOfPoint contour = ((List<MatOfPoint>) contours).get(idx);
            Rect rect = Imgproc.boundingRect(contour);
            double contourArea = Imgproc.contourArea(contour);
            matOfPoint2f.fromList(contour.toList());
            Imgproc.approxPolyDP(matOfPoint2f, approxCurve, Imgproc.arcLength(matOfPoint2f, true) * 0.02, true);
            long total = approxCurve.total();
            if (total == 3) { // is triangle
                // do things for triangle
            }
            if (total >= 4 && total <= 6) {
                List<Double> cos = new ArrayList<>();
                Point[] points = approxCurve.toArray();
                for (int j = 2; j < total + 1; j++) {
                    cos.add(angle(points[(int) (j % total)], points[j - 2], points[j - 1]));
                }
                Collections.sort(cos);
                Double minCos = cos.get(0);
                Double maxCos = cos.get(cos.size() - 1);
                boolean isRect = total == 4 && minCos >= -0.1 && maxCos <= 0.3;
                boolean isPolygon = (total == 5 && minCos >= -0.34 && maxCos <= -0.27) || (total == 6 && minCos >= -0.55 && maxCos <= -0.45);
                if (isRect) {
                    double ratio = Math.abs(1 - (double) rect.width / rect.height);
                    drawText(rect.tl(), ratio <= 0.02 ? "SQU" : "RECT", dst);
                    Imgproc.drawContours(Contours_img, contours, idx, new Scalar(0, 0, 255));
                    BufferedImage Rect_img = Mat2BufferedImage(Contours_img);
                    saveImage("Rect_img.png", Rect_img);
                }
                if (isPolygon) {
                    drawText(rect.tl(), "Polygon", src);
                    BufferedImage Rect_img = Mat2BufferedImage(src);
                    saveImage("Polygon_img.png", Rect_img);
                }
            }
        }
    }

    private double angle(Point pt1, Point pt2, Point pt0) {
        double dx1 = pt1.x - pt0.x;
        double dy1 = pt1.y - pt0.y;
        double dx2 = pt2.x - pt0.x;
        double dy2 = pt2.y - pt0.y;
        return (dx1 * dx2 + dy1 * dy2) / Math.sqrt((dx1 * dx1 + dy1 * dy1) * (dx2 * dx2 + dy2 * dy2) + 1e-10);
    }

    private void drawText(Point ofs, String text, Mat colorImage) {
        Imgproc.putText(colorImage, text, ofs, Core.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255, 255, 25));
    }

    public BufferedImage Mat2BufferedImage(Mat matrix) throws IOException {
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(".jpg", matrix, mob);
        return ImageIO.read(new ByteArrayInputStream(mob.toArray()));
    }

    public void saveImage(String filepath, BufferedImage image) throws IOException {
        File destination = new File(OUTPUT_PATH + filepath);
        ImageIO.write(image, "png", destination);
    }
}
