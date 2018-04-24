package com.example.android.camera2basic.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 2018/4/22
 */
public class FileUtils {

    /**
     * 保存照片到默认照片目录
     *
     * @param bitmap {@link Bitmap} 合成后的 Bitmap
     * @return {@link String} 保存的路径
     */
    public static String saveBitmap(Bitmap bitmap) {
        String picturesDir = getPicturesDir();
        File file = new File(picturesDir, System.currentTimeMillis() + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file.getAbsolutePath();
    }

    /**
     * 保存照片到应用缓存目录
     *
     * @param data 二进制图片数据
     */
    public static String savePicture(Context context, byte[] data) {
        File file = new File(getCacheDir(context), System.currentTimeMillis() + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.flush();
            fos.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file.getAbsolutePath();
    }

    /**
     * 获取应用的缓存目录
     *
     * @param context {@link Context}上下文
     * @return {@link String} 缓存目录路径
     */
    private static String getCacheDir(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File cacheDir = context.getExternalCacheDir();
            if (cacheDir != null && (cacheDir.exists() || cacheDir.mkdirs())) {
                return cacheDir.getAbsolutePath();
            }
        }
        return context.getCacheDir().getAbsolutePath();
    }

    /**
     * 获取系统默认图片目录
     *
     * @return 目录的路径
     */
    private static String getPicturesDir() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File externalPicturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (externalPicturesDir != null && (externalPicturesDir.exists() || externalPicturesDir.mkdirs())) {
                return externalPicturesDir.getAbsolutePath();
            }
        }
        return null;
    }
}