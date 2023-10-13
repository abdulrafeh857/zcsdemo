package com.zcs.zcssdkdemo.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class BitmapUtils {

    public static Bitmap scaleBitmap(Bitmap bitmap, float scale) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static byte rgb2Gray(int r, int g, int b, boolean colored) {
        return rgb2Gray(r, g, b, colored, false);
    }

    public static byte rgb2Gray(int r, int g, int b) {
        return rgb2Gray(r, g, b, false, false);
    }

    public static byte rgb2Gray(int r, int g, int b, boolean colored, boolean reverse) {
        int color = colored ? 140: 95;
        double gray = 0.29900 * r + 0.58700 * g + 0.11400 * b;
        return (reverse ? ((int) gray > color)
                : ((int) gray < color)) ? (byte) 1 : (byte) 0;
    }
}
