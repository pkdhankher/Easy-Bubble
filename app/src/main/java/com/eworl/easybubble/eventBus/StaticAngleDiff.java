package com.eworl.easybubble.eventBus;

import android.content.Context;

/**
 * Created by Dhankher on 2/14/2017.
 */
public class StaticAngleDiff {
    Double angle;
    Context context;
    public StaticAngleDiff(Context context, double angle) {
        this.context = context;
        this.angle = angle;
        angleDiff();

    }

    public double angleDiff() {
        return angle;
    }

}
