/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.camera2basic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class CameraActivity extends AppCompatActivity {

    private Camera2BasicFragment back;
    private Camera2BasicFragment front;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (null == savedInstanceState) {
            back = Camera2BasicFragment.newInstance("0");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, back)
                    .commit();
            front = Camera2BasicFragment.newInstance("1");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container1, front)
                    .commit();
        }

        findViewById(R.id.capture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CameraActivity.this, SecondActivity.class));
//                back.takePicture();
//                front.takePicture();
            }
        });
    }
}
