package com.laole918.inputboxtest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Button btnNormal, btnListView, btnScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
