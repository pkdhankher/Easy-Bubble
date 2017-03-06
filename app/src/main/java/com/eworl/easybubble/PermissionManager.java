package com.eworl.easybubble;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

/**
 * Created by root on 3/2/17.
 */

public class PermissionManager {

    public static void checkForOverlayPermission(Activity activity) {
        if (android.os.Build.VERSION.SDK_INT >= 23) { //Android M Or Over
            if (!Settings.canDrawOverlays(activity)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName()));
                activity.startActivityForResult(intent, 1);
            }
        }
    }
}
