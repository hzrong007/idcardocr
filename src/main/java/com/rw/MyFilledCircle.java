package com.rw;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class MyFilledCircle {
    private final static Integer TICKET = -1;
    private final static Integer LINE_TYPE = 8;

    public static void circle(Mat mat, Point center, int w) {
        Imgproc.circle(mat, center, w / 10, new Scalar(0, 0, 225), TICKET, LINE_TYPE);
    }
}
