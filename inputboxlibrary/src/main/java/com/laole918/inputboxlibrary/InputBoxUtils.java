package com.laole918.inputboxlibrary;

import android.app.Activity;
import android.view.View;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by laole918 on 2016/1/9.
 */
public class InputBoxUtils {

    private static Map<Activity, InputBox> weakInputBoxs = new WeakHashMap<>();

    public static void register(Activity context) {
        if(!weakInputBoxs.containsKey(context)) {
            InputBox box = new InputBox();
            box.onCreate(context);
            weakInputBoxs.put(context, box);
        }
    }

    public static void unregister(Activity context) {
        if(weakInputBoxs.containsKey(context)) {
            InputBox box = weakInputBoxs.get(context);
            box.onDestroy();
            weakInputBoxs.remove(context);
        }
    }

    public static void show(Activity context, View anchor, int limit) {
        if(weakInputBoxs.containsKey(context)) {
            InputBox box = weakInputBoxs.get(context);
            box.show(anchor, limit);
        }
    }

    public static InputBoxView getInputBoxView(Activity context) {
        if(weakInputBoxs.containsKey(context)) {
            InputBox box = weakInputBoxs.get(context);
            return box.getInputBoxView();
        }
        return null;
    }

    public static boolean isShowing(Activity context) {
        if(weakInputBoxs.containsKey(context)) {
            InputBox box = weakInputBoxs.get(context);
            return box.isShowing();
        }
        return false;
    }

    public static void dismiss(Activity context) {
        if(weakInputBoxs.containsKey(context)) {
            InputBox box = weakInputBoxs.get(context);
            box.dismiss();
        }
    }

}
