package com.example.android.camera2basic.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * 2018/4/22
 */
public class PhotoUtils {

    /**
     * 合成照片
     *
     * @param background {@link Bitmap} 背景照片
     * @param foreground {@link Bitmap} 前景照片
     * @param left       float 前景照片左边坐标
     * @param top        float 前景照片上边坐标
     * @return {@link Bitmap} 合成后的照片
     */
    public static Bitmap compose(Bitmap background, Bitmap foreground, float left, float top) {
        int width = background.getWidth();
        int height = background.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.save();
        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawBitmap(foreground, left, top, null);
        canvas.restore();
        return bitmap;
    }
}
