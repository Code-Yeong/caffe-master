package com.vector.caffe.util;

import android.content.Context;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**
 * Created by guo on 17-6-12.
 */

public class FunUtils {
    public static void showToast(View view, String msg) {
        Snackbar.make(view, msg, Toast.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    public static void startVibrate(Context cxt) {
        Vibrator vibrator = (Vibrator) cxt.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(20);
    }
}
