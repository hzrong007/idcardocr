### 项目参考：[idCardCv](https://gitee.com/nbsl/idCardCv.git)

1. 安装、配置openvc环境
> [下载地址](https://nchc.dl.sourceforge.net/project/opencvlibrary/4.2.0/opencv-4.2.0-vc14_vc15.exe) opencv-4.2.0-vc14_vc15.exe

> 安装目录：D:\tools\support\opencv2
> 环境变量配置

```
path中追加：D:\tools\support\opencv\build\x64\vc15\bin
```



2. idea搭建openvc java环境    [参考该链接](https://jingyan.baidu.com/album/0202781151ec471bcc9ce5ca.html)
3. VM options配置：

```
-Djava.library.path=D:\tools\support\opencv\build\java\x64
```

4. 运行IdCardOcrUtil的main方法