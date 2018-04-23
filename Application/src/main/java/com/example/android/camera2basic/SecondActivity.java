package com.example.android.camera2basic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.android.camera2basic.core.ImageCompositeHelper;
import com.example.android.camera2basic.util.FileUtils;
import com.example.android.camera2basic.util.PhotoUtils;

import java.io.File;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        final AutoFitTextureView container = findViewById(R.id.container);
        final AutoFitTextureView container1 = findViewById(R.id.container1);
        ImageCompositeHelper.getInstance().init(this, container, container1);
        findViewById(R.id.capture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageCompositeHelper.getInstance().compose(new ImageCompositeHelper.OnComposeCompleteListener() {
                    @Override
                    public void onCompose(File bg, File fg) {
                        Bitmap bgBitmap = BitmapFactory.decodeFile(bg.getAbsolutePath());
                        Bitmap fgBitmap = BitmapFactory.decodeFile(fg.getAbsolutePath());
                        if (bgBitmap == null || fgBitmap == null) {
                            return;
                        }

                        Bitmap bitmap;
                        if (bgBitmap.getWidth() > fgBitmap.getHeight()) {
                            bitmap = PhotoUtils.compose(bgBitmap, fgBitmap, 0, 0);
                        } else {
                            bitmap = PhotoUtils.compose(fgBitmap, bgBitmap, 0, 0);
                        }
                        FileUtils.savePicture(bitmap);
                    }
                });
            }
        });
    }
}