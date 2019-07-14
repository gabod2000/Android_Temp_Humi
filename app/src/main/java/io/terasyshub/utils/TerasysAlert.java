package io.terasyshub.utils;

import android.app.Activity;

import com.andrognito.flashbar.Flashbar;
import com.andrognito.flashbar.anim.FlashAnim;

import io.terasyshub.R;

public class TerasysAlert {
    public static void show(String message, boolean status, Activity activity){
        new Flashbar.Builder(activity)
                .gravity(Flashbar.Gravity.TOP)
                .backgroundColorRes(status? R.color.positive: R.color.negative)
                .message(message)
                .enterAnimation(FlashAnim.with(activity)
                        .animateBar()
                        .duration(750)
                        .alpha()
                        .overshoot())
                .exitAnimation(FlashAnim.with(activity)
                        .animateBar()
                        .duration(400)
                        .accelerateDecelerate())
                .duration(3000)
                .build().show();
    }
}
