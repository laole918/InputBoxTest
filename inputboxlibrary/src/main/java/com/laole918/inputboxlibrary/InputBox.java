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
public class InputBox {

    private static final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    );

    private Activity mContext;
    private InputMethodManager imm;
    private ViewGroup decorView;//activity的根View
    private ViewGroup rootView;// mSharedView 的 根View
    private ViewGroup anchorOnView;
    private InputBoxView mInputBoxView;

    private int anchorY = 0;
    private int mKeyBoardHeight = 0;
    private int mVisibleHeight = 0;
    private boolean mIsKeyboardShow = false;
    private boolean mInputBoxViewShow = false;

    public void onCreate(Activity context) {
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

    private void init() {
        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }

    private void initViews() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        decorView = (ViewGroup) mContext.getWindow().getDecorView();
        rootView = (ViewGroup) layoutInflater.inflate(R.layout.layout_inputbox_container, null, false);
        rootView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ));
        rootView.setClickable(true);
    }

    private void initInputBoxView() {
        mInputBoxView = new InputBoxView(mContext);
        params.gravity = Gravity.BOTTOM;
        mInputBoxView.setLayoutParams(params);
        mInputBoxView.setOnTouchEditTextListener(mOnTouchListener);
    }

    public InputBoxView getInputBoxView() {
        return mInputBoxView;
    }

    public void onDestroy() {
        if (mContext == null) return;
        if(isShowing()) {
            dismiss();
        }
        decorView.getViewTreeObserver().removeGlobalOnLayoutListener(mOnGlobalLayoutListener);
        mInputBoxView = null;
        anchorOnView = null;
        rootView = null;
        decorView = null;
        imm = null;
        mContext = null;
    }

    public void show() {
        show(null, 0);
    }

    public void show(View anchor) {
        show(anchor, 0);
    }

    public void show(int limit) {
        show(null, limit);
    }

    public void show(View anchor, int limit) {
        if (mContext == null) {
            throw new IllegalArgumentException("mContext=null没有初始化，请调用register方法");
        }
        mInputBoxViewShow = true;
        setCancelable(true);
        if (!isShowing()) {
            onAttached();
        }
        if(anchor != null) {
            initAnchorY(anchor);
            initAnchorOnView(anchor);
        }
        if(limit > 0) {
            mInputBoxView.setTextLimit(limit);
            disableClipOnParents(mInputBoxView);
        }
        showSoftInput();
    }

    private void initAnchorY(View anchor) {
        Rect r = new Rect();
        int[] location = new int[2];
        anchor.getLocalVisibleRect(r);
        anchor.getLocationInWindow(location);
        anchorY = location[1] + r.bottom;
    }

    private void initAnchorOnView(View anchor) {
        if(anchor.getParent() == null) {
            return;
        }
        if(anchor instanceof ListView || anchor instanceof ScrollView) {
            anchorOnView = (ViewGroup) anchor;
            return;
        }
        if (anchor.getParent() instanceof View) {
            initAnchorOnView((View) anchor.getParent());
        }
    }

    private void onAttached() {
        decorView.addView(rootView);
        if (mInputBoxView.getParent() != null)
            ((ViewGroup) mInputBoxView.getParent()).removeView(mInputBoxView);
        rootView.addView(mInputBoxView);
    }

    public void disableClipOnParents(View v) {
        if (v.getParent() == null) {
            return;
        }
        if (v instanceof ViewGroup) {
            ((ViewGroup) v).setClipChildren(false);
        }
        if (v.getParent() instanceof View) {
            disableClipOnParents((View) v.getParent());
        }
    }

    public boolean isShowing() {
        return rootView.getParent() != null;
    }

    public void dismiss() {
        mInputBoxViewShow = false;
        hideSoftInput();
        rootView.removeView(mInputBoxView);
        decorView.removeView(rootView);
    }

    private void setCancelable(boolean isCancelable) {
        if (isCancelable) {
            rootView.setOnTouchListener(onCancelableTouchListener);
        } else {
            rootView.setOnTouchListener(null);
        }
    }

    private void onKeyboardShow() {
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

    private void onKeyboardHide() {
        params.gravity = Gravity.BOTTOM;
        params.bottomMargin = 0;
        mInputBoxView.setLayoutParams(params);
        if(anchorOnView != null) {
            anchorOnView = null;
            anchorY = 0;
        }
    }

    private void onClickEditText() {
        params.gravity = Gravity.BOTTOM;
        params.bottomMargin = mKeyBoardHeight;
        mInputBoxView.setLayoutParams(params);
        showSoftInput();
    }

    private void showSoftInput() {
        if (!mIsKeyboardShow) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void hideSoftInput() {
        if (mIsKeyboardShow) {
            if (mContext.getCurrentFocus() != null && mContext.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(mContext.getCurrentFocus().getWindowToken(), 0);
            } else {
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private void getKeyboardHeight() {
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

    private final View.OnTouchListener onCancelableTouchListener = new View.OnTouchListener() {
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

    private final ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            getKeyboardHeight();
        }
    };

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            if (action == MotionEvent.ACTION_UP && !mIsKeyboardShow) {
                onClickEditText();
                return true;
            }
            return !mIsKeyboardShow;
        }
    };

}
