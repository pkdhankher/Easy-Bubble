package com.eworl.easybubble.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.eworl.easybubble.LayoutParamGenerator;
import com.eworl.easybubble.activities.MainActivity;
import com.eworl.easybubble.eventBus.BubbleServiceIsRunning;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Dhankher on 3/6/2017.
 */

public class BubbleService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

//        viewManager.addView(masterBubble.getView(), LayoutParamGenerator.getNewLayoutParams());
//        EventBus.getDefault().post(new BubbleServiceIsRunning());
    }
}
