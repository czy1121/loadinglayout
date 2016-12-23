/*
 * Copyright 2016 czy1121
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ezy.demo.loadinglayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import ezy.ui.layout.LoadingLayout;


public class LoadingActivity extends AppCompatActivity implements View.OnClickListener  {


    LoadingLayout vLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        vLoading = (LoadingLayout) findViewById(R.id.loading);
        vLoading.setRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "retry" , Toast.LENGTH_LONG).show();
            }
        });
        vLoading.showContent();


        findViewById(R.id.action_content).setOnClickListener(this);
        findViewById(R.id.action_empty).setOnClickListener(this);
        findViewById(R.id.action_loading).setOnClickListener(this);
        findViewById(R.id.action_error).setOnClickListener(this);

     }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.action_empty:
            vLoading.showEmpty();
            break;
        case R.id.action_loading:
            vLoading.showLoading();
            break;
        case R.id.action_content:
            vLoading.showContent();
            break;
        case R.id.action_error:
            vLoading.showError();
            break;
        }
    }
}
