package com.rw;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class MyLine {
    private final static Integer TICKET = 2;
    private final static Integer LINE_TYPE = 8;

    public static void line(Mat mat, Point start, Point end) {
        Imgproc.line(mat, start, end, new Scalar(255, 0, 0), TICKET, LINE_TYPE);
    }
}
