package com.laole918.inputboxlibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by laole918 on 2016/1/10.
 */
public class InputBoxView extends LinearLayout {

    private InputMethodManager imm;

    private EditText editText;
    private Button btnSend;

    public InputBoxView(Context context) {
        super(context);
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        initViews();
    }

    private void initViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_inputbox_view, this, true);
        editText = (EditText) findViewById(R.id.edit_text);
        btnSend = (Button) findViewById(R.id.btn_send);
    }

    public void dismiss() {
        editText.clearFocus();
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public void show() {
        editText.setFocusable(true);
        editText.requestFocus();
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }
}
