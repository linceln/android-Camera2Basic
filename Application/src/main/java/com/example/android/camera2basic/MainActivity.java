package com.example.android.camera2basic;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.android.camera2basic.core.ImageComposite;
import com.example.android.camera2basic.view.AutoFitTextureView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AutoFitTextureView mTextureBackground;

    private AutoFitTextureView mTextureForeground;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextureBackground = findViewById(R.id.textureBackground);
        mTextureForeground = findViewById(R.id.textureForeground);
        findViewById(R.id.capture).setOnClickListener(this);
        mTextureForeground.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.capture:
                ImageComposite.getInstance().compose(new ImageComposite.OnComposeCallback() {
                    @Override
                    public void onCompleted(String path) {
                        Toast.makeText(MainActivity.this, "Saved: " + path, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.textureForeground:
                // TODO 点击前后切换
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        if (mTextureBackground.isAvailable()) {
            ImageComposite.getInstance().openCamera(this,
                    mTextureBackground.getSurfaceTexture(),
                    mTextureBackground.getWidth(),
                    mTextureBackground.getHeight(),
                    mTextureForeground.getSurfaceTexture(),
                    mTextureForeground.getWidth(),
                    mTextureForeground.getHeight());
        } else {
            ImageComposite.getInstance().init(this, mTextureBackground, mTextureForeground);
        }
    }
}