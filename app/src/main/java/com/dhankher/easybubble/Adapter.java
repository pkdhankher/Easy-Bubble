package com.dhankher.easybubble;

import android.content.Context;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * Created by hippo on 31/1/17.
 */

public abstract class Adapter {
    private Context context;

    public Adapter(Context context) {
        this.context = context;
    }

    public abstract void onBindView(FrameLayout frameLayout, WindowManager.LayoutParams subLayoutParams, int position, int pointerX, int pointerY);

    public abstract void onUnbind(FrameLayout frameLayout);

    public Context getContext() {
        return context;
    }
}