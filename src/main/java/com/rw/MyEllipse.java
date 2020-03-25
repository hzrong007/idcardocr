package com.rw;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class MyEllipse {
    private final static Integer TICKET = 2;
    private final static Integer LINE_TYPE = 8;

    public static void ellipse(Mat mat, double angle, double w) {
        Imgproc.ellipse(mat, new Point(w / 2.0, w / 2.0),
                new Size(w / 4.0, w / 16.0),
                angle,
                0,
                360,
                new Scalar(255, 0, 0),
                TICKET,
                LINE_TYPE);
    }
}
