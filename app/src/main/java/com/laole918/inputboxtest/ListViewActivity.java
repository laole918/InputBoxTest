package com.laole918.inputboxtest;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.laole918.inputboxlibrary.InputBoxUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);

        tintManager.setTintColor(Color.parseColor("#999000FF"));
        tintManager.setNavigationBarTintColor(Color.parseColor("#ffFF4081"));

        listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, getData());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        InputBoxUtils.showAnchor(view, listView);
    }
}
