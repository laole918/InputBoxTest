package com.laole918.inputboxtest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.readystatesoftware.systembartint.SystemBarTintManager;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Button btnNormal, btnListView, btnScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(false);
        tintManager.setNavigationBarTintEnabled(true);
//
//        tintManager.setTintColor(Color.parseColor("#999000FF"));
        tintManager.setNavigationBarTintColor(Color.parseColor("#ffFF4081"));

        btnNormal = (Button) findViewById(R.id.btn_normal);
        btnListView = (Button) findViewById(R.id.btn_listview);
        btnScrollView = (Button) findViewById(R.id.btn_scrollview);
        btnNormal.setOnClickListener(this);
        btnListView.setOnClickListener(this);
        btnScrollView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(btnNormal)) {
            Intent i = new Intent();
            i.setClass(this, NormalActivity.class);
            startActivity(i);
        } else if(v.equals(btnListView)) {
            Intent i = new Intent();
            i.setClass(this, ListViewActivity.class);
            startActivity(i);
        } else if(v.equals(btnScrollView)) {
            Intent i = new Intent();
            i.setClass(this, ScrollViewActivity.class);
            startActivity(i);
        }

    }
}
