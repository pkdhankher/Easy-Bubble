package com.eworl.easybubble.activities;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import com.eworl.easybubble.db.Program;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static android.content.ContentValues.TAG;

/**
 * Created by Dhankher on 3/3/2017.
 */
public class FetchingAppsTask implements Runnable {
    CallBack callBack;
    Context context;
    List<Program> allItems;

    public FetchingAppsTask(CallBack callBack, List<Program> allItems, Context context) {
        this.callBack = callBack;
        this.allItems = allItems;
        this.context = context;
    }

    @Override
    public void run() {

        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> apps = packageManager.getInstalledApplications(0);
        List<ApplicationInfo> appInfoList = new ArrayList();
        for (ApplicationInfo info : apps) {
            if (packageManager.getLaunchIntentForPackage(info.packageName) != null) {
                appInfoList.add(info);

            }
        }
        Collections.sort(appInfoList, new ApplicationInfo.DisplayNameComparator(packageManager));
        Log.d(TAG, "getInstalledApplication: " + appInfoList);
        int listCount = appInfoList.size();
        Log.d(TAG, "count: " + listCount);
        allItems = new ArrayList<Program>();
        for (int i = 0; i < listCount; i++) {
            String appName = (String) packageManager.getApplicationLabel(appInfoList.get(i));
            Drawable icon = packageManager.getApplicationIcon(appInfoList.get(i));

            Bitmap img = ((BitmapDrawable) icon).getBitmap();
            Log.d(TAG, "bitmap: " + img);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] imageInByte = stream.toByteArray();
            String appIcon = Base64.encodeToString(imageInByte, Base64.DEFAULT);

            String packageName = appInfoList.get(i).packageName;
            allItems.add(new Program((long) i, appName, appIcon, packageName, false));
            Log.d(TAG, "packageName: " + packageName);

        }
        callBack.onWorkComplited(allItems);

    }
}
