package com.laole918.inputboxlibrary;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by laole918 on 2016/1/10.
 */
public class InputBoxView extends LinearLayout implements TextWatcher, View.OnClickListener {

    private EditText editText;
    private Button btnSend;
    private TextView tipsText;

    private View.OnClickListener mOnClickSendListener;

    private int textLimit;
    private boolean isOutOfLimit = false;

    public InputBoxView(Context context) {
        super(context);
        initViews();
    }

    private void initViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_inputbox_view, this, true);
        editText = (EditText) findViewById(R.id.edit_text);
        btnSend = (Button) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);
        tipsText = (TextView) findViewById(R.id.tips_text);
        tipsText.setVisibility(View.GONE);
    }

    public void setTextLimit(int textLimit) {
        if(textLimit > 0) {
            SpannableString ss = new SpannableString(0 + "/" + textLimit);
            tipsText.setText(ss);
            tipsText.setVisibility(View.VISIBLE);
            editText.addTextChangedListener(this);
        }
        this.textLimit = textLimit;
    }

    void setOnTouchEditTextListener(View.OnTouchListener l) {
        editText.setOnTouchListener(l);
    }

    public String getEditText() {
        return editText.getText().toString();
    }

    public void setEditText(String text) {
        editText.setText(text);
    }

    public void setEditHint(String text) {
        if(text.length() > 10) {
            text = text.substring(0, 10) + "...";
        }
        editText.setHint(text);
    }

    public void setOnClickSendListener(View.OnClickListener l) {
        mOnClickSendListener = l;
    }

    public boolean IsOutOfLimit() {
        return isOutOfLimit;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        int length = editable.length();
        String text = length + "/" + textLimit;
        SpannableString ss = new SpannableString(text);
        if(length > textLimit) {
            int end = text.indexOf("/");
            ForegroundColorSpan fcs = new ForegroundColorSpan(Color.RED);
            ss.setSpan(fcs, 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            isOutOfLimit = true;
        } else {
            isOutOfLimit = false;
        }
        tipsText.setText(ss);
    }

    @Override
    public void onClick(View view) {
        if(view.equals(btnSend)) {
            if(isOutOfLimit) {
                Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                shake.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        animation.setAnimationListener(null);
                        tipsText.startAnimation(animation);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                editText.startAnimation(shake);
                return;
            }
            if (mOnClickSendListener != null) {
                mOnClickSendListener.onClick(view);
            }
        }
    }
}
