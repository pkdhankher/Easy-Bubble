package com.dhankher.easybubble;

import android.content.Context;
import android.util.Log;
import android.view.WindowManager;
import android.widget.FrameLayout;

import static com.dhankher.easybubble.BubbleService.screenWidth_percent;
import static com.dhankher.easybubble.BubbleService.screenheight_percent;

/**
 * Created by hippo on 31/1/17.
 */

public class GenerateViews extends Adapter {
    public static final String TAG = GenerateViews.class.getCanonicalName();
    BubbleService bubbleService;
    float x1, x2, x3, x4;
    float y1, y2, y3, y4;
    float sin45 = (float) Math.sin(Math.toRadians(45));
    float cos45 = (float) Math.cos(Math.toRadians(45));
    float sin0 = (float) Math.sin(Math.toRadians(0));
    float cos0 = (float) Math.cos(Math.toRadians(0));

    public GenerateViews(Context context) {
        super(context);
    }

    @Override
    public void onBindView(FrameLayout layout, WindowManager.LayoutParams layoutParams, int position, int pointerX, int pointerY) {
        switch (position) {
            case 0:


                layoutParams.x = (int) ( 60+ (150 * sin0));
             //   Log.d(TAG, "bblparams"+(bubbleService.bubbleParams.x ));
                x1 = (150 * sin0);
              Log.d(TAG, "x1: " +x1 );
                layoutParams.y = (int) ( 600+(150 * cos0));
                y1 = (150 * cos0);
                Log.d(TAG, "y1: " + y1);
                bubbleService.windowManager.addView(layout, layoutParams);
                break;

            case 1:
                layoutParams.x = (int) (60+ ((x1 * cos45) + (y1 * sin45)));
                x2 = ((x1 * cos45) + (y1 * sin45));
                layoutParams.y = (int) ((int) 600+((y1 * cos45) - (x1 * sin45)));
                y2 = ((y1 * cos45) - (x1 * sin45));
                Log.d(TAG, "y2: " + y2);
                Log.d(TAG, "x2: " +x2 );
                bubbleService.windowManager.addView(layout, layoutParams);
                break;
            case 2:
                layoutParams.x = (int) ((x2 * cos45) + (y2 * sin45));
                x3 = ((x2 * cos45) + (y2 * sin45));
                layoutParams.y = (int) ((y2 * cos45) - (x2 * sin45));
                y3 = ((y2 * cos45) - (x2 * sin45));
                Log.d(TAG, "y3: " + y3);
                Log.d(TAG, "x3: " +x3 );
                bubbleService.windowManager.addView(layout, layoutParams);
                break;
            case 3:
                layoutParams.x = (int) ((x3 * cos45) + (y3 * sin45));
                x4 = ((x3 * cos45) + (y3 * sin45));
                layoutParams.y = (int) ((y3 * cos45) - (x3 * sin45));
                y4 = ((y3 * cos45) - (x3 * sin45));
                Log.d(TAG, "y4: " + y4);
                Log.d(TAG, "x4: " +x4 );
                bubbleService.windowManager.addView(layout, layoutParams);
                break;
            case 4:
                layoutParams.x = (int) ((x3 * cos45) + (y3 * sin45));
                layoutParams.y = (int) ((y3 * cos45) - (x3 * sin45));
                bubbleService.windowManager.addView(layout, layoutParams);
                break;

        }

    }

    @Override
    public void onUnbind(FrameLayout frameLayout) {

    }
}
