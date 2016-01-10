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
    private static InputBoxView mInputBoxView;

    private static int mInputBoxViewHeight = 0;
    private static int mKeyBoardHeight = 0;
    private static int mVisibleHeight = 0;
    private static boolean mIsKeyboardShow = false;
//    private static boolean mIsInputBoxShow = false;

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
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(listener);
    }

    private static void initViews() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        decorView = (ViewGroup) mContext.getWindow().getDecorView();
        rootView = (ViewGroup) layoutInflater.inflate(R.layout.layout_inputbox_container, null, false);
        rootView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ));
    }

    private static void initInputBoxView() {
        mInputBoxView = new InputBoxView(mContext);
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        mInputBoxView.measure(w, h);
        mInputBoxViewHeight = mInputBoxView.getMeasuredHeight();
//        params.gravity = Gravity.BOTTOM;
        mInputBoxView.setLayoutParams(params);
    }

    public static void unregister() {
        if (mContext == null) return;
        decorView.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        mContext = null;
    }

    public static void show() {
        if (mContext == null) {
            throw new IllegalArgumentException("mContext=null没有初始化");
        }
        if (!isShowing()) {
            onAttached();
            showSoftInput();
//            mInputBoxView.show();
        }
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
        if (mIsKeyboardShow) {
//            mInputBoxView.dismiss();
//            hideSoftInput();
            return;
        }
        rootView.removeView(mInputBoxView);
        decorView.removeView(rootView);
    }

    public static void setCancelable(boolean isCancelable) {
        if (isCancelable) {
            rootView.setOnTouchListener(onCancelableTouchListener);
        } else {
            rootView.setOnTouchListener(null);
        }
    }

    private static void onKeyboardShow() {
        params.gravity = Gravity.TOP;
        params.topMargin = mKeyBoardHeight;
        mInputBoxView.setLayoutParams(params);
        System.out.println("onKeyboardShow" + params.topMargin);
    }

    private static void onKeyboardHide() {
        params.gravity = Gravity.BOTTOM;
        params.topMargin = 0;
//        mInputBoxView.setLayoutParams(params);
//        System.out.println("onKeyboardHide");
    }

    private static void showSoftInput() {
        imm.showSoftInput(decorView, InputMethodManager.SHOW_FORCED);
    }

    private static void hideSoftInput() {
        imm.hideSoftInputFromWindow(decorView.getWindowToken(), 0);
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
            onKeyboardShow();
        } else {
            mIsKeyboardShow = false;
//            mVisibleHeight = 0;
            mKeyBoardHeight = 0;
            onKeyboardHide();
        }
    }

    /**
     * Called when the user touch on black overlay in order to dismiss the dialog
     */
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

    private static final ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            getKeyboardHeight();
        }
    };

}
