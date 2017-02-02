package com.dhankher.easybubble;

import android.content.Context;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * Created by hippo on 31/1/17.
 */

 public  class AddingViewClass {
    private Context context;
    private static Adapter adapter;

    public AddingViewClass(MainActivity mCcontext, GenerateViews myAdapter) {
        this.context = mCcontext;
        this.adapter = myAdapter;

    }

    public static void addView(FrameLayout layout, WindowManager.LayoutParams layoutParams, int position, int pointerX, int pointerY) {
        adapter.onBindView(layout,layoutParams,position,pointerX,pointerY);
    }


}
