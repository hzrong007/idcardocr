package com.rw.utils.ocr;

import com.rw.CharSvm;
import com.rw.utils.CoreFunc;
import org.apache.commons.lang3.StringUtils;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.ml.SVM;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class IdCardOcrUtil {
    public static String rootPath;
    public static CharSvm svmTrain;
    public static String svmXml = "/model/svm.xml";

    static {
        URL resource = IdCardOcrUtil.class.getClassLoader().getResource("");
        rootPath = StringUtils.strip(resource.getFile(), "/");
    }

    public static void main(String[] args) throws Exception {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String text = IdCardOcrUtil.getIdCardCode(rootPath + "/test/xx4.jpg");
        System.out.println(text);
    }

    public static String getIdCardCode(String imagePath) {
        // 原图
        Mat rgbMat = Imgcodecs.imread(imagePath);
        Rect rect = detectTextArea(rgbMat);
        String text = getCharText(rgbMat, rect);
        return text;
    }

    private static String getCharText(Mat srcMat, Rect rect) {
        if (svmTrain == null) {
            svmTrain = new CharSvm();
            SVM svm = SVM.load(rootPath + svmXml);
            svmTrain.setSvm(svm);
        }
        // 身份证位置
        Mat effective = new Mat();
        Mat charsGrayMat = new Mat();
        Mat hierarchy = new Mat();
        srcMat.copyTo(effective);

        Mat charsMat = new Mat(effective, rect);
        // 灰度化
        Imgproc.cvtColor(charsMat, charsGrayMat, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(charsGrayMat, charsGrayMat, new Size(3, 3), 0, 0, Core.BORDER_DEFAULT);
        //减50是去掉文字周边燥点 阴影覆盖;
        double thresholdValue = CoreFunc.otsu(charsGrayMat) - 25;

        Imgproc.threshold(charsGrayMat, charsGrayMat, thresholdValue, 255, Imgproc.THRESH_BINARY_INV);
        Imgproc.medianBlur(charsGrayMat, charsGrayMat, 3);

        Imgcodecs.imwrite("temp/charsMat.jpg", charsGrayMat);

        List<MatOfPoint> charContours = new ArrayList<MatOfPoint>(10);
        Imgproc.findContours(charsGrayMat, charContours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

        Vector<Rect> vecRect = new Vector<Rect>();

        for (int k = 0; k < charContours.size(); k++) {
            Rect mr = Imgproc.boundingRect(charContours.get(k));
            if (verifySizes(mr)) {
                vecRect.add(mr);
            }
        }

        Vector<Rect> sortedRect = CoreFunc.SortRect(vecRect);
        int x = 0;
        StringBuffer idcar = new StringBuffer();
        for (Rect rectSor : sortedRect) {
            Mat specMat = new Mat(charsGrayMat, rectSor);
            specMat = preprocessChar(specMat);
            Imgcodecs.imwrite("temp/debug_specMat" + x + ".jpg", specMat);
            x++;
            String charText = svmTrain.svmFind(specMat);
            idcar.append(charText);
        }
        return idcar.toString();

    }

    private static Rect detectTextArea(Mat srcMat) {
        // 灰度图
        Mat grayMat = new Mat();
        // 灰度化
        Imgproc.cvtColor(srcMat, grayMat, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(grayMat, grayMat, new Size(3, 3), 0, 0, Core.BORDER_DEFAULT);
        grayMat = CoreFunc.Sobel(grayMat);
        Imgcodecs.imwrite("temp/Sobel.jpg", grayMat);

        Imgproc.threshold(grayMat, grayMat, 0, 255, Imgproc.THRESH_OTSU + Imgproc.THRESH_BINARY);
        Imgproc.medianBlur(grayMat, grayMat, 3);

        Imgcodecs.imwrite("temp/grayMat.jpg", grayMat);

        // 使用闭操作。对图像进行闭操作以后，可以看到车牌区域被连接成一个矩形装的区域。
        Rect rect = null;
        for (int step = 20; step < 60; ) {
            //System.out.println(step);
            Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(step, 1));
            Imgproc.morphologyEx(grayMat, grayMat, Imgproc.MORPH_CLOSE, element);

            Imgcodecs.imwrite("temp/MORPH_CLOSE.jpg", grayMat);

            /**
             * 轮廓提取()
             */
            List<MatOfPoint> contoursList = new ArrayList<MatOfPoint>(2);
            Mat hierarchy = new Mat();
            Imgproc.findContours(grayMat, contoursList, hierarchy,
                    Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

            for (int i = 0; i < contoursList.size(); i++) {
                Rect tempRect = Imgproc.boundingRect(contoursList.get(i));
                if (grayMat.height() / tempRect.height < 5) {
                    continue;
                }
                int r = tempRect.width / tempRect.height;
                if (r < 1) {
                    r = tempRect.height / tempRect.width;
                }
                if (tempRect.width > 10 && tempRect.height > 10 &&
                        grayMat.width() != tempRect.width && r > 10 && r < 20) {
                    if (rect == null) {
                        rect = tempRect;
                        continue;
                    }

                    if (tempRect.y > rect.y) {
                        rect = tempRect;
                    }
                }
            }
            if (rect != null) {
                Imgcodecs.imwrite("temp/rectMat.jpg", new Mat(grayMat, rect));
                return rect;
            }
            step = step + 5;
        }
        return rect;

    }

    /**
     * 字符预处理: 统一每个字符的大小
     *
     * @param in
     * @return
     */
    private static Mat preprocessChar(Mat in) {
        int h = in.rows();
        int w = in.cols();
        int charSize = 20;
        Mat transformMat = Mat.eye(2, 3, CvType.CV_32F);
        int m = Math.max(w, h);
        transformMat.put(0, 2, ((m - w) / 2f));
        transformMat.put(1, 2, ((m - h) / 2f));

        Mat warpImage = new Mat(m, m, in.type());

        Imgproc.warpAffine(in, warpImage, transformMat, warpImage.size(), Imgproc.INTER_LINEAR, Core.BORDER_CONSTANT,
                new Scalar(0));

        Mat out = new Mat();
        Imgproc.resize(warpImage, out, new Size(charSize, charSize));

        return out;
    }

    private static boolean verifySizes(Rect mr) {
        if (mr.size().height < 5 || mr.size().width < 5) {
            return false;
        }
        return true;
    }
}
