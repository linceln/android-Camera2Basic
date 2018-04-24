package com.example.android.camera2basic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.android.camera2basic.core.ImageComposite;
import com.example.android.camera2basic.view.AutoFitTextureView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AutoFitTextureView textureBackground = findViewById(R.id.textureBackground);
        AutoFitTextureView textureForeground = findViewById(R.id.textureForeground);
        findViewById(R.id.capture).setOnClickListener(this);
        ImageComposite.getInstance().init(this, textureBackground, textureForeground);
    }

    @Override
    public void onClick(View v) {
        ImageComposite.getInstance().compose(new ImageComposite.OnComposeCallback() {
            @Override
            public void onCompleted(String path) {
                Toast.makeText(MainActivity.this, "Saved: " + path, Toast.LENGTH_SHORT).show();
            }
        });
    }
}