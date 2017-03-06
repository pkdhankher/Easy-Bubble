package com.eworl.easybubble.bubbles;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import com.eworl.easybubble.ViewManager;
import com.eworl.easybubble.eventBus.MasterBubbleInLeft;
import com.eworl.easybubble.eventBus.MasterBubbleInRight;
import com.eworl.easybubble.utils.ValueGenerator;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by root on 3/2/17.
 */

public class MasterBubbleTouchListener implements View.OnTouchListener {
    private static final String TAG = MasterBubbleTouchListener.class.getCanonicalName();
    private MasterBubble masterBubble;
    private View fmContentViewLayout;
    private ViewManager viewManager = ViewManager.getRunningInstance();
    private long startTime, endTime;
    private final static int ANIMATION_DURATION = 300;
    private final static float BUBBLE_CLOSE_SIZE = 1f;
    private final static float BUBBLE_OPEN_SIZE = .8f;
    private static final int STATUS_BAR_HEIGHT = 48;
    private static final int TEMP_RADIUS = 50;
    //    private final WindowManager.LayoutParams fmContentViewParams;
    private float pointerX, pointerY;
    private int radius, screenWidth, screenHeight;
    private int latestPointerX, latestPointerY;
    private Context context;
    private FrameLayout fmOpenView, fmCloseView,flSubBubbleContainer;
    private WindowManager.LayoutParams fmContentViewParams;
    private Boolean isOpen = false;
    private boolean isAnimationOngoing = false;


    public MasterBubbleTouchListener(MasterBubble masterBubble, FrameLayout fmOpenView, FrameLayout fmCloseView, FrameLayout flSubBubbleContainer) {
        this.masterBubble = masterBubble;
        fmContentViewLayout = masterBubble.getView();
        this.context = masterBubble.getContext();
        this.fmCloseView = fmCloseView;
        this.fmOpenView = fmOpenView;
        this.flSubBubbleContainer = flSubBubbleContainer;
//        fmContentViewParams = (WindowManager.LayoutParams) fmContentViewLayout.getLayoutParams();
        ValueGenerator valueGenerator = masterBubble.getValueGenerator();
        radius = valueGenerator.getRadius();
        Log.d(TAG, "masterBubbleRadius: " + radius);
        ViewManager viewManager = ViewManager.getRunningInstance();
        screenWidth = viewManager.getScreenWidth();
        screenHeight = viewManager.getScreenHeight();


    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                performeActionDown(motionEvent);
                break;
            case MotionEvent.ACTION_MOVE:
                performeActionMove(motionEvent);
                break;
            case MotionEvent.ACTION_UP:
                setLatestPointerX(motionEvent);
                setLatestPointerY(motionEvent);
                performeActionUp(motionEvent);

                break;
        }
        return false;
    }

    private void setLatestPointerY(MotionEvent motionEvent) {
        latestPointerY = (int) motionEvent.getRawY();
    }

    private void setLatestPointerX(MotionEvent motievent) {
        latestPointerX = (int) motievent.getRawX();
    }

    public int getLatestPointerX() {
        return latestPointerX;
    }

    public int getLatestPointerY() {
        return latestPointerY;
    }

    private void performeActionMove(MotionEvent motionEvent) {
        if (isOpen) {
            close();
        }

        pointerX = motionEvent.getRawX();
        pointerY = motionEvent.getRawY();
        fmContentViewParams = (WindowManager.LayoutParams) fmContentViewLayout.getLayoutParams();
        fmContentViewParams.x = (int) pointerX - 50 - radius;
        fmContentViewParams.y = (int) pointerY - STATUS_BAR_HEIGHT - TEMP_RADIUS - radius - 10;
        viewManager.updateViewLayout(fmContentViewLayout, fmContentViewParams);
    }


    private void performeActionDown(MotionEvent motionEvent) {
        startTime = System.currentTimeMillis();
    }

    private void performeActionUp(MotionEvent motionEvent) {

        endTime = System.currentTimeMillis();
        if ((endTime - startTime) < 200) {
            masterBubbleClickListener();

        }

        final WindowManager.LayoutParams fmContentViewParams = (WindowManager.LayoutParams) fmContentViewLayout.getLayoutParams();
        if (pointerX < (screenWidth / 2)) {
            masterBubbleInLeft();
            ObjectAnimator objectAnimator = new ObjectAnimator();
            objectAnimator.setDuration(500);
            float init = pointerX - radius - TEMP_RADIUS;
            objectAnimator.setFloatValues(init, 0 - radius);
            objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    float current = (int) value;
                    fmContentViewParams.x = (int) current;
                    fmContentViewParams.y = (int) (pointerY - (STATUS_BAR_HEIGHT + TEMP_RADIUS) - radius);
                    if(pointerY<(2*TEMP_RADIUS)){
                        fmContentViewParams.y = (int) (pointerY - (STATUS_BAR_HEIGHT + TEMP_RADIUS) - radius+(2*TEMP_RADIUS));
                    }

                    if(pointerY>(screenHeight-TEMP_RADIUS)){
                        fmContentViewParams.y = (int) (pointerY - (STATUS_BAR_HEIGHT + TEMP_RADIUS) - radius-TEMP_RADIUS);
                    }
                    viewManager.updateViewLayout(fmContentViewLayout, fmContentViewParams);
                }
            });
            objectAnimator.start();
        } else {
            masterBubbleInRight();
           ObjectAnimator objectAnimator = new ObjectAnimator();
            objectAnimator.setDuration(500);
            float initial = pointerX - radius - TEMP_RADIUS;
            float finalV = (float) (screenWidth - ((2 * TEMP_RADIUS)) - radius);
            objectAnimator.setFloatValues(initial, finalV);
//            objectAnimator.setInterpolator(new OvershootInterpolator());
            objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    float current = (int) value;
                    fmContentViewParams.x = (int) current;
                    fmContentViewParams.y = (int) (pointerY - (STATUS_BAR_HEIGHT + TEMP_RADIUS) - radius);
                    if (pointerY < (2 * TEMP_RADIUS)) {
                        fmContentViewParams.y = (int) (pointerY - (STATUS_BAR_HEIGHT + TEMP_RADIUS) - radius + (2 * TEMP_RADIUS));
                    }
                    if (pointerY > (screenHeight - TEMP_RADIUS)) {
                        fmContentViewParams.y = (int) (pointerY - (STATUS_BAR_HEIGHT + TEMP_RADIUS) - radius - TEMP_RADIUS);
                    }
                    viewManager.updateViewLayout(fmContentViewLayout, fmContentViewParams);

                }
            });
            objectAnimator.start();


        }
    }

    private void masterBubbleInRight() {
        EventBus.getDefault().post(new MasterBubbleInRight());
    }
    private void masterBubbleInLeft() {
        EventBus.getDefault().post(new MasterBubbleInLeft());
    }

    private void masterBubbleClickListener() {

        toggle();
    }
    public void toggle() {
        if (isAnimationOngoing) return;

        if (isOpen)
            close();
        else
            open();
    }

    void close() {
        fmOpenView.clearAnimation();
        fmCloseView.clearAnimation();

        flSubBubbleContainer.setVisibility(View.INVISIBLE);
        isAnimationOngoing = true;
        fmCloseView.animate()
                .setDuration(ANIMATION_DURATION)
                .scaleX(BUBBLE_CLOSE_SIZE)
                .scaleY(BUBBLE_CLOSE_SIZE)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        fmOpenView.setVisibility(View.VISIBLE);
                        isAnimationOngoing = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .rotation(0);
        isOpen = false;
    }

    void open() {
        fmOpenView.clearAnimation();
        fmCloseView.clearAnimation();

        flSubBubbleContainer.setVisibility(View.VISIBLE);
        fmOpenView.setVisibility(View.VISIBLE);
        isAnimationOngoing = true;
        fmCloseView.animate().setDuration(ANIMATION_DURATION)
                .setInterpolator(new OvershootInterpolator())
                .scaleX(BUBBLE_OPEN_SIZE)
                .scaleY(BUBBLE_OPEN_SIZE)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        isAnimationOngoing = false;
                    }
                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }
                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                }).rotation(45);
        isOpen = true;
    }

}
