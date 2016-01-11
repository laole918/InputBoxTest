package com.laole918.inputboxlibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by laole918 on 2016/1/10.
 */
public class InputBoxView extends LinearLayout {

    private EditText editText;
    private Button btnSend;

    public InputBoxView(Context context) {
        super(context);
        initViews();
    }

    private void initViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_inputbox_view, this, true);
        editText = (EditText) findViewById(R.id.edit_text);
        btnSend = (Button) findViewById(R.id.btn_send);
    }

    public EditText getEditText() {
        return editText;
    }

    public Button getSendButton() {
        return btnSend;
    }

}
