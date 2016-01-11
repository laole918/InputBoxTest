package com.laole918.inputboxtest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.laole918.inputboxlibrary.InputBoxUtils;

public class NormalActivity extends BaseActivity implements View.OnClickListener {

    private Button show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
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
