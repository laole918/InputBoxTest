package com.laole918.inputboxtest;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.laole918.inputboxlibrary.InputBoxUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

public class ScrollViewActivity extends BaseActivity implements View.OnClickListener {

    private ScrollView scrollView;
    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollview);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);

        tintManager.setTintColor(Color.parseColor("#999000FF"));
        tintManager.setNavigationBarTintColor(Color.parseColor("#ffFF4081"));

        scrollView = (ScrollView) findViewById(R.id.scrollview);
        container = (LinearLayout) findViewById(R.id.container);
        List<String> data = getData();
        for (String text :data) {
            TextView tv = new TextView(this);
            tv.setOnClickListener(this);
            tv.setText(text);
            container.addView(tv);
        }

        InputBoxUtils.register(this);
    }

    private List<String> getData() {
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 100; i ++) {
            data.add("你点我啊：" + i);
        }
        return data;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        InputBoxUtils.unregister();
    }

    @Override
    public void onBackPressed() {
        if(InputBoxUtils.isShowing()) {
            InputBoxUtils.dismiss();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        InputBoxUtils.showAnchor(view, scrollView);
    }
}
