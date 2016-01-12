package com.laole918.inputboxtest;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.laole918.inputboxlibrary.InputBoxUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class NormalActivity extends BaseActivity implements View.OnClickListener {

    private Button show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);

        tintManager.setTintColor(Color.parseColor("#999000FF"));
        tintManager.setNavigationBarTintColor(Color.parseColor("#ffFF4081"));

        show = (Button) findViewById(R.id.show);
        show.setOnClickListener(this);

        InputBoxUtils.register(this);
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
    public void onClick(View v) {
        if(v.equals(show)) {
            InputBoxUtils.show();
        }
    }
}
