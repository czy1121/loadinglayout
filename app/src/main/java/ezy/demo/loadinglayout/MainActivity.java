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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ezy.ui.layout.LoadingLayout;


public class MainActivity extends AppCompatActivity implements View.OnClickListener  {


    LoadingLayout vLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vLoading = LoadingLayout.wrap(this);
        vLoading.setRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "retry" , Toast.LENGTH_LONG).show();
            }
        });
        vLoading.showContent();

        findViewById(R.id.list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), ListActivity.class));
            }
        });
        findViewById(R.id.another).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), LoadingActivity.class));
            }
        });

     }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_empty:
            vLoading.showEmpty();
            return true;
        case R.id.action_loading:
            vLoading.showLoading();
            return true;
        case R.id.action_content:
            vLoading.showContent();
            return true;
        case R.id.action_error:
            vLoading.showError();
            return true;
        }
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.loading, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
    }
}
