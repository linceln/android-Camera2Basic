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
     * @param background 背景照片
     * @param foreground 前景照片
     * @param left       前景照片左边坐标
     * @param top        前景照片上边坐标
     * @return 合成后的照片
     */
    public static Bitmap compose(Bitmap background, Bitmap foreground, int left, int top) {
        Bitmap bitmap = Bitmap.createBitmap(background.getWidth(), background.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.save();
        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawBitmap(foreground, left, top, null);
        canvas.restore();
        return bitmap;
    }
}
