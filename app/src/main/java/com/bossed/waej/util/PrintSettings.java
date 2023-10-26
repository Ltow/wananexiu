package com.bossed.waej.util;

import android.graphics.Bitmap;

import java.io.IOException;
import java.io.OutputStream;

public class PrintSettings {

    private OutputStream outputStream;

    public PrintSettings(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * 初始化打印缓冲
     *
     * @throws IOException
     */
    public void initPrinter() throws IOException {
        byte[] b = new byte[2];
        b[0] = 0x1B;
        b[1] = 0x40;
        outputStream.write(b);
        outputStream.flush();
    }

    /**
     * 打印文本数据
     *
     * @param text
     * @throws IOException
     */
    public void printText(String text) throws IOException {
        outputStream.write(text.getBytes("GBK"));
        outputStream.flush();
    }

    /**
     * 初始化打印缓冲
     *
     * @throws IOException
     */
    public void printToSpaceLines(int lines, String text) throws IOException {
        byte[] b = new byte[2];
        b[0] = 0x1B;
        b[1] = 0x4A;
        outputStream.write(b);
        outputStream.write(lines);
        printText(text);
        outputStream.flush();
    }

    /**
     * 设置文本对齐方式
     *
     * @param align 打印位置 0：居左(默认) 1：居中 2：居右
     * @throws IOException
     */
    public void setAlignPosition(int align) throws IOException {
        outputStream.write(0x1B);
        outputStream.write(0x61);
        outputStream.write(align);
        outputStream.flush();
    }

    /**
     * 设置行间距
     *
     * @param gap 最大256
     * @throws IOException
     */
    public void setLineGap(int gap) throws IOException {
        outputStream.write(0x1B);
        outputStream.write(0x33);
        outputStream.write(gap);
        outputStream.flush();
    }

    /**
     * 设置左内边距
     *
     * @param paddingLeft [0,47]&&(paddingLeft+paddingRight)也在此范围内
     * @throws IOException
     */
    public void setPaddingLeft(int paddingLeft) throws IOException {
        outputStream.write(0x1B);
        outputStream.write(0x6C);
        outputStream.write(paddingLeft);
        outputStream.flush();
    }

    /**
     * 设置右内边距
     *
     * @param paddingRight [0,47]&&(paddingLeft+paddingRight)也在此范围内
     * @throws IOException
     */
    public void setPaddingRight(int paddingRight) throws IOException {
        outputStream.write(0x1B);
        outputStream.write(0x51);
        outputStream.write(paddingRight);
        outputStream.flush();
    }

    /**
     * 设置字体样式
     *
     * @param fontStyle 1 加粗 2 倍高 3 加粗倍高 4 倍宽 5 倍高倍宽 6 加粗倍高倍宽
     * @throws IOException
     */
    public void setFontStyle(int fontStyle) throws IOException {

        outputStream.write(0x1B);
        outputStream.write(0x21);
        outputStream.write(fontStyle);
        outputStream.flush();
    }

    /**
     * 设置字体样式
     *
     * @param fontStyle 1 加粗 2 倍高 3 加粗倍高 4 倍宽 5 倍高倍宽 6 加粗倍高倍宽
     * @throws IOException
     */
    public void setBigSize(int fontStyle) throws IOException {
        outputStream.write(0x1C);
        outputStream.write(0x57);
        outputStream.write(fontStyle);
        outputStream.flush();
    }


    /**
     * 设置字母下划线
     *
     * @param n 0解除设置 1设置细下划线 2设置粗下划线
     * @throws IOException
     */
    public void setUnderline(int n) throws IOException {

        outputStream.write(0x1B);
        outputStream.write(0x2D);
        outputStream.write(n);
        outputStream.flush();
    }

    /**
     * 设置粗体
     *
     * @param n 0解除设置 1设置
     * @throws IOException
     */
    public void setBold(int n) throws IOException {

        outputStream.write(0x1B);
        outputStream.write(0x45);
        outputStream.write(n);
        outputStream.flush();
    }


    /**
     * 设置颠倒打印模式
     *
     * @param n 00解除设置 01设置
     * @throws IOException
     */
    public void setUpsideDown(int n) throws IOException {

        outputStream.write(0x1B);
        outputStream.write(0x7B);
        outputStream.write(n);
        outputStream.flush();
    }

    /**
     * 设置反白打印
     *
     * @param n 00解除设置 01设置
     * @throws IOException
     */
    public void setTurnWhite(int n) throws IOException {

        outputStream.write(0x1D);
        outputStream.write(0x42);
        outputStream.write(n);
        outputStream.flush();
    }

    /**
     * 设定打印内容的灰度，分8个等级1 ~ 8，“1”为最浅，“8”为最深，用于解决不同热敏纸颜色深浅不一的问题
     *
     * @param n [1,8],默认4
     * @throws IOException
     */
    public void setGrayScale(int n) throws IOException {

        outputStream.write(0x1B);
        outputStream.write(0x6D);
        outputStream.write(n);
        outputStream.flush();
    }


    /**
     * 设置字体
     *
     * @param n 0 中文： 24 × 24 ，外文： 12 × 24; 1 中文： 16 × 16 ，外文： 8 × 16; 2 中文：
     *          12 × 12 ，外文： 6 × 12
     * @throws IOException
     */
    public void setFont(int n) throws IOException {

        outputStream.write(0x1B);
        outputStream.write(0x4D);
        outputStream.write(n);
        outputStream.flush();
    }

    /**
     * 设置顺时针旋转90°
     *
     * @param n 00解除设置 01设置
     * @throws IOException
     */
    public void setClockwise90degrees(int n) throws IOException {

        outputStream.write(0x1B);
        outputStream.write(0x56);
        outputStream.write(n);
        outputStream.flush();
    }

    /**
     * 绝对打印位置
     *
     * @throws IOException
     */
    public void printLocation(int light, int weight) throws IOException {
        outputStream.write(0x1B);
        outputStream.write(0x24);
        outputStream.write(light);
        outputStream.write(weight);
        outputStream.flush();
    }


    /**
     * 换行
     *
     * @throws IOException
     */
    public void nextLine() throws IOException {
        outputStream.write("\r\n".getBytes("GBK"));
        outputStream.flush();
    }


    /**
     * 制表符
     *
     * @param length
     * @throws IOException
     */
    public void printTab(int length) throws IOException {
        for (int i = 0; i < length; i++) {
            outputStream.write("\t".getBytes("GBK"));
        }
        outputStream.flush();
    }

    /**
     * 到下一个制表位
     *
     * @throws IOException
     */
    public void printHT() throws IOException {
        outputStream.write(0x09);
        outputStream.flush();
    }

    /**
     * 打印空白（一个汉字的位置）
     *
     * @param length 需要打印空白的长度,
     * @throws IOException
     */
    public void printWordTab(int length) throws IOException {
        for (int i = 0; i < length; i++) {
            outputStream.write("  ".getBytes("GBK"));
        }
        outputStream.flush();
    }

    /**
     * 制表
     *
     * @throws IOException
     */
    public void printTable(byte[] b) throws IOException {

        outputStream.write(0x1B);
        outputStream.write(0x44);
        outputStream.write(b);
        outputStream.flush();
    }

    /**
     * 是指条形码字符位置
     *
     * @param n
     * @throws IOException
     */

    public void setBarCodeNum(int n) throws IOException {

        outputStream.write(0x1D);
        outputStream.write(0x48);
        outputStream.write(n);

        outputStream.flush();
    }

    /**
     * 条形码长度
     *
     * @param n
     * @throws IOException
     */
    public void setBarCodeHeight(int n) throws IOException {

        outputStream.write(0x1D);
        outputStream.write(0x68);
        outputStream.write(n);

        outputStream.flush();
    }

    /**
     * 条形码宽度
     *
     * @param n
     * @throws IOException
     */
    public void setBarCodeWidth(int n) throws IOException {

        outputStream.write(0x1D);
        outputStream.write(0x77);
        outputStream.write(n);

        outputStream.flush();
    }

    /**
     * 条形码字体
     *
     * @param n
     * @throws IOException
     */
    public void setBarCodeFont(int n) throws IOException {

        outputStream.write(0x1D);
        outputStream.write(0x66);
        outputStream.write(n);

        outputStream.flush();
    }

    /**
     * @param m 编码系统
     * @param b 条形码字符串
     * @throws IOException
     */
    public void setB(int m, String b) throws IOException {

        outputStream.write(0x1D);
        outputStream.write(0x6B);
        outputStream.write(m);
        outputStream.write(b.getBytes("GBK"));

        outputStream.flush();
    }

    /**
     * 图片转比特数组
     *
     * @param bmp
     * @return
     */
    public void draw2PxPoint(Bitmap bmp) throws IOException {
        int size = bmp.getWidth() * bmp.getHeight() / 8 + 1000;
        byte[] data = new byte[size];
        int k = 0;
        data[k++] = 0x1B;
        data[k++] = 0x33;
        data[k++] = 0x00;
        for (int j = 0; j < bmp.getHeight() / 24f; j++) {
            data[k++] = 0x1B;
            data[k++] = 0x2A;
            data[k++] = 33;
            data[k++] = (byte) (bmp.getWidth() % 256);
            data[k++] = (byte) (bmp.getWidth() / 256);
            for (int i = 0; i < bmp.getWidth(); i++) {
                for (int m = 0; m < 3; m++) {
                    for (int n = 0; n < 8; n++) {
                        byte b = px2Byte(i, j * 24 + m * 8 + n, bmp);
                        data[k] += data[k] + b;
                    }
                    k++;
                }
            }
            data[k++] = 10;
        }
        outputStream.write(data);
        outputStream.flush();
    }

    /**
     * 图片转比特数组
     *
     * @param x
     * @param y
     * @param bit
     * @return
     */
    public static byte px2Byte(int x, int y, Bitmap bit) {
        if (x < bit.getWidth() && y < bit.getHeight()) {
            byte b;
            int pixel = bit.getPixel(x, y);
            int alpha = 0xFF << 24;
            alpha = ((pixel & 0xFF000000) >> 24);
            int red = (pixel & 0x00ff0000) >> 16;
            int green = (pixel & 0x0000ff00) >> 8;
            int blue = pixel & 0x000000ff;
            int gray = RGB2Gray(alpha, red, green, blue);
            if (gray < 128) {
                b = 1;
            } else {
                b = 0;
            }
            return b;
        }
        return 0;
    }

    /**
     * 图片转比特数组
     *
     * @param r
     * @param g
     * @param b
     * @return
     */
    private static int RGB2Gray(int a, int r, int g, int b) {
        if (r >= 255) {
            r = 255;
        } else {
            r = 0;
        }
        if (b >= 255) {
            b = 255;
        } else {
            b = 0;
        }
        if (g >= 255) {
            g = 255;
        } else {
            g = 0;
        }
        int gray = (int) (0.29900 * r + 0.58700 * g + 0.11400 * b); // �Ҷ�ת����ʽ
        return gray;
    }

}
