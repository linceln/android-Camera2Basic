package com.example.android.camera2basic.core;

import android.graphics.SurfaceTexture;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.view.TextureView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageCompositeHelper {

    private static ImageCompositeHelper mInstance = new ImageCompositeHelper();

    private List<File> mFiles = new ArrayList<>();

    private Camera2Helper mFrontHelper;

    private Camera2Helper mBackHelper;

    private OnComposeCompleteListener mOnComposeCompleteListener;

    private ImageCompositeHelper() {
    }

    public static ImageCompositeHelper getInstance() {
        return mInstance;
    }

    public void compose(OnComposeCompleteListener listener) {
        if (mBackHelper == null || mFrontHelper == null) {
            throw new NullPointerException("Not initialized");
        }
        this.mOnComposeCompleteListener = listener;
        mFiles.clear();
        mBackHelper.setOnCaptureCompleteListener(new Callback());
        mFrontHelper.setOnCaptureCompleteListener(new Callback());
        mBackHelper.takePicture();
        mFrontHelper.takePicture();
    }

    public void init(final FragmentActivity activity, TextureView bg, TextureView fg) {

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
                return false;
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
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
    }

    public interface OnComposeCompleteListener {
        void onCompose(File bg, File fg);
    }

    public class Callback implements Camera2Helper.onCaptureCompleteListener {

        @Override
        public void onCaptureComplete(File file) {
            mFiles.add(file);
            if (mFiles.size() >= 2) {
                mOnComposeCompleteListener.onCompose(mFiles.get(0), mFiles.get(1));
            }
        }
    }
}
