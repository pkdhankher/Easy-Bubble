package com.eworl.easybubble;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by root on 3/2/17.
 */

public class ViewManager {
    private WindowManager windowManager;
    private Context context;
    private Display display;
    private Point size = new Point();
    private int screenHeight, screenWidth;
    private static ViewManager runningInstance;

    public ViewManager(Context context) {
             if (runningInstance != null)
            return;

        runningInstance = this;
        this.context = context;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        calculateScreenDimentions();
    }

    private void calculateScreenDimentions() {
        display = windowManager.getDefaultDisplay();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }


    public void addView(View view, WindowManager.LayoutParams layoutParams) {
        windowManager.addView(view, layoutParams);
    }

    public void removeView(View view) {
        windowManager.removeView(view);
    }

    public void updateViewLayout(View view, WindowManager.LayoutParams layoutParams) {

        windowManager.updateViewLayout(view, layoutParams);
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int screenHeightPercentage() {
        int screenHeightPercent = getScreenHeight() / 100;
        return screenHeightPercent;
    }

    public int screenWidthPercentage() {
        int screenWidthPercent = getScreenWidth() / 100;
        return screenWidthPercent;
    }

//    public int getStatusBarHeight(){
//        int statusBarHeight = 0;
//        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
//        }
//        return statusBarHeight;
//    }

    public static ViewManager init(Context context) {
        if (runningInstance != null)
            return runningInstance;

        return new ViewManager(context);

    }

    public static ViewManager getRunningInstance(){
        return runningInstance;
    }

}
