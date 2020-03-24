package com.rw;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import static org.opencv.highgui.HighGui.waitKey;

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
        waitKey(0);
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

        waitKey(0);
        HighGui.destroyAllWindows();
    }

    /**
     * 掩码
     */
    @Test
    public void testMatMask() {
        String[] args = new String[]{getFile("/xx4.jpg")};
        new MatMaskOperationsRun().run(args);
    }

    /**
     * 混合两个图像
     * -- addWeighted(加权混合)，要求两个图象的大小和类型必须完全相同，该处理的原理为遍历两个图像相对应的各个像素点，将对应的颜色值乘以相应的权重再相加；
     */
    @Test
    public void testAddWeight() {
        double alpha = 0.3;
        // 俩个图片长宽不一致, throw Exception
//        Mat src1 = Imgcodecs.imread(getFile("/lena.jpg"));
//        Mat src2 = Imgcodecs.imread(getFile("/xx4.jpg"));
        
        Mat src1 = Imgcodecs.imread(getFile("/linux.jpg"));
        Mat src2 = Imgcodecs.imread(getFile("/windows.jpg"));

        double beta = (1.0 - alpha);
        Mat dst = new Mat();
        Core.addWeighted(src1, alpha, src2, beta, 0, dst);
        HighGui.imshow("Linear Blend", dst);
        waitKey(0);
    }
}
