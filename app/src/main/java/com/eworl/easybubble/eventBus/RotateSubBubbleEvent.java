package com.eworl.easybubble.eventBus;

import android.content.Context;
import android.util.Log;

/**
 * Created by Dhankher on 2/13/2017.
 */
public class RotateSubBubbleEvent {
    private static final String TAG = "RotateSubBubbleEvent";
    float diffY;
    Context context;

    public RotateSubBubbleEvent(Context context,float diffY) {
        this.context = context;
        this.diffY = diffY;
        diffY();

    }

    public float diffY() {
        return  diffY;
    }
}
