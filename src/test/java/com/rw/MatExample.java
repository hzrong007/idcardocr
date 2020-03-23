package com.rw;

import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * OpenCV 图像加载和显示
 *
 * @author rongwei
 * @since 2020年3月23日21:56:00
 */
public class MatExample extends BaseEample {
    @Test
    public void testImShow() {
        String file = getFile("/xx4.jpg");
        Mat rgbMat = Imgcodecs.imread(file);
        HighGui.namedWindow("RongWeiDisplay");
        HighGui.imshow("RongWeiDisplay", rgbMat);
        HighGui.waitKey(0);
        HighGui.destroyWindow("RongWeiDisplay");
    }

    /**
     * 彩色置灰
     */
    @Test
    public void testCvtColor() {
        String file = getFile("/xx4.jpg");
        Mat rgbMat = Imgcodecs.imread(file);
        Mat grayMat = new Mat();
        Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_BGR2GRAY);
        Imgcodecs.imwrite(mkdirFile("/11.jpg", "/gray"), grayMat);

        HighGui.namedWindow("RongWeiColorDisplay");
        HighGui.namedWindow("RongWeiGrayDisplay");
        HighGui.imshow("RongWeiColorDisplay", rgbMat);
        HighGui.imshow("RongWeiGrayDisplay", grayMat);

        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}
