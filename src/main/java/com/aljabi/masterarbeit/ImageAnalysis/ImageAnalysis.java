package com.aljabi.masterarbeit.ImageAnalysis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class ImageAnalysis {



    public BufferedImage verticalEdgeDetector(BufferedImage source, boolean Adaptive) {
        BufferedImage destination = new BufferedImage(source.getWidth(),source.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
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
        new ConvolveOp(new Kernel(3, 3, data2), ConvolveOp.EDGE_NO_OP, null).filter(source, destination);
        return destination;
    }

    public BufferedImage horizontalEdgeDetector(BufferedImage source, boolean Adaptive) {
        BufferedImage destination = new BufferedImage(source.getWidth(),source.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
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
        new ConvolveOp(new Kernel(3, 3, data1), ConvolveOp.EDGE_NO_OP, null).filter(source, destination);
        return destination;
    }

}
