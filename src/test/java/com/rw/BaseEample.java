package com.rw;

import com.rw.utils.ocr.IdCardOcrUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.opencv.core.Core;

import java.io.File;
import java.net.URL;

/**
 * 初始化基础类
 *
 * @author rongwei
 * @since 2020年3月23日21:58:15
 */
public class BaseEample {
    public static String rootPath;

    @Before
    public void before() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        URL resource = IdCardOcrUtil.class.getClassLoader().getResource("");
        rootPath = StringUtils.strip(resource.getFile(), "/");
    }

    public String getFile(String fileName) {
        File file = new File(rootPath + fileName);
        return file.getPath();
    }

    public String mkdirFile(String fileName, String... dir) {
        String join = StringUtils.join(dir);
        File parentDir = new File(rootPath + join);
        if (!parentDir.exists()) {
            parentDir.mkdir();
        }
        File file = new File(parentDir, fileName);
        return file.getPath();
    }
}
