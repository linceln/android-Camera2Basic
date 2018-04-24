package com.example.android.camera2basic.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.view.TextureView;

import com.example.android.camera2basic.util.FileUtils;
import com.example.android.camera2basic.util.PhotoUtils;

import java.io.File;
import java.util.HashMap;

/**
 * 使用 Camera2 API 获取前后摄像头照片并合成
 */
public class ImageComposite {

    private static final String FRONT = "front";

    private static final String BACK = "back";

    /**
     * 单例
     */
    private static ImageComposite mInstance = new ImageComposite();

    /**
     * 保存拍摄的背景图和前景图
     */
    private HashMap<String, File> mFiles = new HashMap<>();

    /**
     * 前置摄像头
     */
    private Camera2Helper mFrontHelper;

    /**
     * 后置摄像头
     */
    private Camera2Helper mBackHelper;

    /**
     * 前景照片 x 方向坐标偏移量
     */
    private float mOffsetX;

    /**
     * 前景照片 y 方向坐标偏移量
     */
    private float mOffsetY;

    /**
     * 前景照片显示宽度
     */
    private int mWidth;

    /**
     * 前景照片显示高度
     */
    private int mHeight;

    /**
     * 合成完成回调
     */
    private OnComposeCallback mOnComposeCallback;

    private ImageComposite() {
    }

    public static ImageComposite getInstance() {
        return mInstance;
    }

    /**
     * 合成照片
     */
    public void compose(OnComposeCallback callback) {
        if (mBackHelper == null || mFrontHelper == null) {
            throw new NullPointerException("Not initialized");
        }
        this.mOnComposeCallback = callback;
        this.mFiles.clear();
        this.mBackHelper.setOnCaptureCompleteListener(new Callback(BACK));
        this.mFrontHelper.setOnCaptureCompleteListener(new Callback(FRONT));
        this.mBackHelper.takePicture();
        this.mFrontHelper.takePicture();
    }

    /**
     * 初始化，设置 TextureView 监听
     *
     * @param activity {@link android.app.Activity}
     * @param bg       {@link TextureView}背景
     * @param fg       {@link TextureView}前景
     */
    public void init(final FragmentActivity activity, TextureView bg, final TextureView fg) {
        if (bg == null || fg == null) {
            throw new NullPointerException("背景或前景不能为空");
        }
        fg.post(new Runnable() {
            @Override
            public void run() {
                mOffsetX = fg.getX();
                mOffsetY = fg.getY();
                mWidth = fg.getWidth();
                mHeight = fg.getHeight();
            }
        });
        bg.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                mBackHelper = Camera2Helper.newInstance(activity, activity.getLifecycle());
                mBackHelper.openBackCamera(surface, width, height);
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });

        fg.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                mFrontHelper = Camera2Helper.newInstance(activity, activity.getLifecycle());
                mFrontHelper.openFrontCamera(surface, width, height);
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
    }

    private class Callback implements Camera2Helper.onCaptureCompleteListener {

        private String mOrientation;

        Callback(String orientation) {
            this.mOrientation = orientation;
        }

        @Override
        public void onCaptureComplete(File file) {
            mFiles.put(mOrientation, file);
            if (mFiles.size() >= 2) {
                // 取出刚拍出的前景背景图
                Bitmap bitmapFront = BitmapFactory.decodeFile(mFiles.get(FRONT).getAbsolutePath());
                Bitmap bitmapBack = BitmapFactory.decodeFile(mFiles.get(BACK).getAbsolutePath());
                if (bitmapFront == null || bitmapBack == null) {
                    return;
                }
                if (bitmapFront.getWidth() > mWidth || bitmapFront.getHeight() > mHeight) {
                    // TODO 裁剪前景照片条件判断
                    // 裁剪前景照片
                    int cropped = (mWidth > mHeight) ? mHeight : mWidth;
                    Bitmap bitmap = bitmapFront;
                    bitmapFront = Bitmap.createBitmap(cropped, cropped, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmapFront);
                    canvas.drawBitmap(bitmap, 0, 0, null);
                    canvas.save();
                    canvas.restore();
                }
                // 合成
                // 1. 背景图画在下层，前景图画在上层
                // 2. 保存到系统图库
                Bitmap bitmap = PhotoUtils.compose(bitmapBack, bitmapFront, mOffsetX, mOffsetY);
                String path = FileUtils.saveBitmap(bitmap);
                // TODO 通知系统图库刷新照片
                if (mOnComposeCallback != null) {
                    mOnComposeCallback.onCompleted(path);
                }
            }
        }
    }

    public interface OnComposeCallback {
        /**
         * 合成成功回调
         *
         * @param path 合成图片的路径
         */
        void onCompleted(String path);
    }
}