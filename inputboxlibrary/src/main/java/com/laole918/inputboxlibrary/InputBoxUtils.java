package com.laole918.inputboxlibrary;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * Created by laole918 on 2016/1/9.
 */
public class InputBoxUtils {

    private static final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    );

    private static Activity mContext;
    private static InputMethodManager imm;
    private static ViewGroup decorView;//activity的根View
    private static ViewGroup rootView;// mSharedView 的 根View
    private static ViewGroup anchorOnView;
    private static InputBoxView mInputBoxView;

    private static int anchorY = 0;
    private static int mKeyBoardHeight = 0;
    private static int mVisibleHeight = 0;
    private static boolean mIsKeyboardShow = false;
    private static boolean mInputBoxViewShow = false;

    public static void register(Activity context) {
        if (context == null) return;
        if (mContext != null && mContext == context) {
            return;
        }
        if (mContext != null && mContext != context) {
            dismiss();
        }
        mContext = context;
        initViews();
        initInputBoxView();
        init();
    }

    private static void init() {
        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }

    private static void initViews() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        decorView = (ViewGroup) mContext.getWindow().getDecorView();
        rootView = (ViewGroup) layoutInflater.inflate(R.layout.layout_inputbox_container, null, false);
        rootView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ));
        rootView.setClickable(true);
    }

    private static void initInputBoxView() {
        mInputBoxView = new InputBoxView(mContext);
        params.gravity = Gravity.BOTTOM;
        mInputBoxView.setLayoutParams(params);
        mInputBoxView.getEditText().setOnTouchListener(mOnTouchListener);
    }

    public static InputBoxView getInputBoxView() {
        return mInputBoxView;
    }

    public static void unregister() {
        if (mContext == null) return;
        decorView.getViewTreeObserver().removeGlobalOnLayoutListener(mOnGlobalLayoutListener);
        mContext = null;
    }

    public static void show() {
        if (mContext == null) {
            throw new IllegalArgumentException("mContext=null没有初始化，请调用register方法");
        }
        mInputBoxViewShow = true;
        setCancelable(true);
        if (!isShowing()) {
            onAttached();
        }
        showSoftInput();
    }

    public static void showAnchor(View anchor, ListView listView) {
        anchorOnView = listView;
        showAnchor(anchor);
    }

    public static void showAnchor(View anchor, ScrollView scrollView) {
        anchorOnView = scrollView;
        showAnchor(anchor);
    }

    private static void showAnchor(View anchor) {
        Rect r = new Rect();
        int[] location = new int[2];
        anchor.getLocalVisibleRect(r);
        anchor.getLocationInWindow(location);
        anchorY = location[1] + r.bottom;
        show();
    }

    private static void onAttached() {
        decorView.addView(rootView);
        if (mInputBoxView.getParent() != null)
            ((ViewGroup) mInputBoxView.getParent()).removeView(mInputBoxView);
        rootView.addView(mInputBoxView);
    }

    public static boolean isShowing() {
        return rootView.getParent() != null;
    }

    public static void dismiss() {
        mInputBoxViewShow = false;
        hideSoftInput();
        rootView.removeView(mInputBoxView);
        decorView.removeView(rootView);
    }

    private static void setCancelable(boolean isCancelable) {
        if (isCancelable) {
            rootView.setOnTouchListener(onCancelableTouchListener);
        } else {
            rootView.setOnTouchListener(null);
        }
    }

    private static void onKeyboardShow() {
        params.gravity = Gravity.BOTTOM;
        params.bottomMargin = mKeyBoardHeight;
        mInputBoxView.setLayoutParams(params);
        mInputBoxView.post(new Runnable() {
            @Override
            public void run() {
                mInputBoxView.requestFocus();
            }
        });
        if(anchorOnView instanceof ListView) {
            ListView listView = (ListView) anchorOnView;
            listView.smoothScrollBy(anchorY - mVisibleHeight + mInputBoxView.getHeight(), 0);
        } else if(anchorOnView instanceof ScrollView) {
            ScrollView scrollView = (ScrollView) anchorOnView;
            scrollView.smoothScrollBy(0, anchorY - mVisibleHeight + mInputBoxView.getHeight());
        }
    }

    private static void onKeyboardHide() {
        params.gravity = Gravity.BOTTOM;
        params.bottomMargin = 0;
        mInputBoxView.setLayoutParams(params);
        if(anchorOnView != null) {
            anchorOnView = null;
            anchorY = 0;
        }
    }

    private static void onClickEditText() {
        params.gravity = Gravity.BOTTOM;
        params.bottomMargin = mKeyBoardHeight;
        mInputBoxView.setLayoutParams(params);
        showSoftInput();
    }

    private static void showSoftInput() {
        if (!mIsKeyboardShow) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private static void hideSoftInput() {
        if (mIsKeyboardShow) {
            if (mContext.getCurrentFocus() != null && mContext.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(mContext.getCurrentFocus().getWindowToken(), 0);
            } else {
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private static void getKeyboardHeight() {
        Rect r = new Rect();
        decorView.getGlobalVisibleRect(r);
        int screenHeight = r.bottom;
        decorView.getWindowVisibleDisplayFrame(r);
        int visibleHeight = r.bottom;
        if (mVisibleHeight == 0) {
            mVisibleHeight = visibleHeight;
            return;
        }
        if (mVisibleHeight == visibleHeight) {
            return;
        }
        mVisibleHeight = visibleHeight;
        if (mVisibleHeight < screenHeight) {
            mIsKeyboardShow = true;
            mKeyBoardHeight = screenHeight - mVisibleHeight;
            if(mInputBoxViewShow) {
                onKeyboardShow();
            }
        } else {
            mIsKeyboardShow = false;
            mVisibleHeight = 0;
            if(mInputBoxViewShow) {
                onKeyboardHide();
            }
        }
    }

    private static final View.OnTouchListener onCancelableTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                dismiss();
                if (!isShowing()) {
                    setCancelable(false);
                }
            }
            return false;
        }
    };

    private static final ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            getKeyboardHeight();
        }
    };

    private static View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            if (action == MotionEvent.ACTION_UP) {
                onClickEditText();
            }
            return true;
        }
    };

}
