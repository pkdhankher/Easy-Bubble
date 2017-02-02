package com.dhankher.easybubble;

import android.content.Context;
import android.util.EventLog;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import static com.dhankher.easybubble.BubbleService.screenWidth_percent;
import static com.dhankher.easybubble.BubbleService.screenheight_percent;

/**
 * Created by hippo on 31/1/17.
 */

public class GenerateViews extends Adapter implements GestureDetector.OnGestureListener,Updatable {
    public static final String TAG = GenerateViews.class.getCanonicalName();
    BubbleService bubbleService;
    float x1, x2, x3, x4, x5, x6;
    float y1, y2, y3, y4, y5, y6;
    GestureDetector gestureDetector;
    int coordinateX = 60;
    int coordinateY = 600;
    FrameLayout.OnTouchListener gestureListener;
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
                layoutParams.x = (int) (coordinateX + (250 * sin0));
                //   Log.d(TAG, "bblparams"+(bubbleService.bubbleParams.x ));
                x1 = (250 * sin0);
                Log.d(TAG, "x1: " + x1);
                layoutParams.y = (int) (coordinateY + (250 * cos0));
                y1 = (250 * cos0);
                Log.d(TAG, "y1: " + y1);
                bubbleService.windowManager.addView(layout, layoutParams);
                break;


            case 1:
                layoutParams.x = (int) (coordinateX + ((x1 * cos45) + (y1 * sin45)));
                x2 = ((x1 * cos45) + (y1 * sin45));
                layoutParams.y = (int) (coordinateY + ((y1 * cos45) - (x1 * sin45)));
                y2 = ((y1 * cos45) - (x1 * sin45));
                Log.d(TAG, "y2: " + y2);
                Log.d(TAG, "x2: " + x2);
                bubbleService.windowManager.addView(layout, layoutParams);
                break;
            case 2:
                layoutParams.x = (int) (coordinateX + ((x2 * cos45) + (y2 * sin45)));
                x3 = ((x2 * cos45) + (y2 * sin45));
                layoutParams.y = (int) (coordinateY + ((y2 * cos45) - (x2 * sin45)));
                y3 = ((y2 * cos45) - (x2 * sin45));
                Log.d(TAG, "y3: " + y3);
                Log.d(TAG, "x3: " + x3);
                bubbleService.windowManager.addView(layout, layoutParams);
                break;
            case 3:
                layoutParams.x = (int) (coordinateX + ((x3 * cos45) + (y3 * sin45)));
                x4 = ((x3 * cos45) + (y3 * sin45));
                layoutParams.y = (int) (coordinateY + ((y3 * cos45) - (x3 * sin45)));
                y4 = ((y3 * cos45) - (x3 * sin45));
                Log.d(TAG, "y4: " + y4);
                Log.d(TAG, "x4: " + x4);
                bubbleService.windowManager.addView(layout, layoutParams);
                break;
            case 4:
                layoutParams.x = (int) (coordinateX + ((x4 * cos45) + (y4 * sin45)));
                x5 = ((x4 * cos45) + (y4 * sin45));
                layoutParams.y = (int) (coordinateY + ((y4 * cos45) - (x4 * sin45)));
                y5 = ((y4 * cos45) - (x4 * sin45));
                bubbleService.windowManager.addView(layout, layoutParams);
                break;

            case 5:
                layoutParams.x = (int) (coordinateX + ((x5 * cos45) + (y5 * sin45)));
                x6 = ((x5 * cos45) + (y5 * sin45));
                layoutParams.y = (int) (coordinateY + ((y5 * cos45) - (x5 * sin45)));
                y6 = ((y5 * cos45) - (x5 * sin45));
                bubbleService.windowManager.addView(layout, layoutParams);
                break;

            case 6:
                layoutParams.x = (int) (coordinateX + ((x6 * cos45) + (y6 * sin45)));
                layoutParams.y = (int) (coordinateY + ((y6 * cos45) - (x6 * sin45)));
                bubbleService.windowManager.addView(layout, layoutParams);
                break;
        }
       gestureDetector = new GestureDetector(this);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);

                return false;
            }
        });

    }

    @Override
    public void onUnbind(FrameLayout frameLayout) {

    }


    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        Toast.makeText(getContext(),"scrol",Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onScroll: "+motionEvent.getRawX());
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onCoordinateUpdate(int coordinateX, int coordinateY) {
        Log.d(TAG, "onCoordinateUpdate: "+coordinateX);

    }
}
