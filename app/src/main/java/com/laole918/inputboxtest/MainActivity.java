package com.laole918.inputboxtest;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.laole918.inputboxlibrary.InputBoxUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button2, button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatusAndNavigation(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);

        tintManager.setTintColor(Color.parseColor("#999000FF"));
        tintManager.setNavigationBarTintColor(Color.parseColor("#ffFF4081"));

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

    @TargetApi(19)
    private void setTranslucentStatusAndNavigation(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        bits |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(button2)) {
            InputBoxUtils.show();
            InputBoxUtils.setCancelable(true);
        } else if(v.equals(button3)) {
            InputBoxUtils.dismiss();
        }
    }
}
