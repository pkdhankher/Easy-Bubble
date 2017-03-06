package com.eworl.easybubble.utils;

import android.graphics.drawable.Drawable;

/**
 * Created by Dhankher on 2/21/2017.
 */

public class SelectedItemObject {

    String appName;
    int appIcon;
    public  SelectedItemObject(String appName,int appIcon){
        this.appName = appName;
        this.appIcon = appIcon;
    }

    public int getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(int appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
