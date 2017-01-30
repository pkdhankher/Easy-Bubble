package com.dhankher.easybubble;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by Dhankher on 1/25/2017.
 */

public class BubbleService extends Service {

    WindowManager windowManager;
    WindowManager.LayoutParams bubbleParams, removeParams, sub_bubbles_layoutParams1,sub_bubbles_layoutParams2;
    LayoutInflater layoutInflater;
    FrameLayout bubbleLayout, removeLayout,subBubblesLayout, imgframe,sub_bubbles_layout1,sub_bubbles_layout2;
    int screenWidth, screenHeight, statusBarHeight;
    int pointerX, pointerY, radius;
    ImageView removeImage;
    ImageView iv_innerCircle, iv_innerPlus,iv_bubble;
    ImageView iv_1,iv_2,iv_3,iv_4,iv_5;
    ObjectAnimator objectAnimatorTowardsRemoveView, objectAnimatorAwayFromRemoveView;
    private boolean isNearToRemoveView = false;
    int removeViewX = 295, removeViewY = 1068;
    private boolean isBubbleLeft = false;
    private final static int ANIMATION_DURATION_FAST = 100;
    private final static int ANIMATION_DURATION_MEDIUM = 300;
    private final static int ANIMATION_DURATION_SLOW = 500;
    private boolean isBubbleMove = false;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        statusBarHeight = getStatusBarHeight();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;


        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        bubbleLayout = (FrameLayout) layoutInflater.inflate(R.layout.main_bubble, null);
        iv_innerCircle = (ImageView) bubbleLayout.findViewById(R.id.iv_InnerCircle);
        iv_innerPlus = (ImageView) bubbleLayout.findViewById(R.id.iv_InnerPlus);
        iv_bubble=(ImageView) bubbleLayout.findViewById(R.id.iv_bubble);
        bubbleParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        bubbleParams.gravity = Gravity.LEFT|Gravity.TOP ;
        windowManager.addView(bubbleLayout, bubbleParams);

        sub_bubbles_layout1 = (FrameLayout) layoutInflater.inflate(R.layout.sub_bubbles_layout1,null);
        iv_1 = (ImageView) sub_bubbles_layout1.findViewById(R.id.iv_1);
        sub_bubbles_layoutParams1 = new WindowManager.LayoutParams(
          WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT
        );
        sub_bubbles_layoutParams1.gravity = Gravity.TOP|Gravity.RIGHT;
        windowManager.addView(sub_bubbles_layout1,sub_bubbles_layoutParams1);

        sub_bubbles_layout2 = (FrameLayout) layoutInflater.inflate(R.layout.sub_bubbles_layout2,null);
        iv_2 = (ImageView) sub_bubbles_layout2.findViewById(R.id.iv_2);
        sub_bubbles_layoutParams2 = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT
        );
        sub_bubbles_layoutParams2.gravity = Gravity.BOTTOM|Gravity.RIGHT;
        windowManager.addView(sub_bubbles_layout2,sub_bubbles_layoutParams2);

//        iv_3 = (ImageView) subBubblesLayout.findViewById(R.id.iv_3);
//        iv_4 = (ImageView) subBubblesLayout.findViewById(R.id.iv_4);
//        iv_5 = (ImageView) subBubblesLayout.findViewById(R.id.iv_5);



        removeLayout = (FrameLayout) layoutInflater.inflate(R.layout.removeview, null);
        imgframe = (FrameLayout) removeLayout.findViewById(R.id.imgframe);
        removeImage = (ImageView) removeLayout.findViewById(R.id.removeimg);
        removeParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        removeParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        windowManager.addView(removeLayout, removeParams);

        bubbleLayout.setOnTouchListener(new View.OnTouchListener() {
            long startTime, endTime;
            boolean isLongClick = false;
            Handler handler_longclick = new Handler();
            Runnable runnable_longClick = new Runnable() {
                @Override
                public void run() {
                    isLongClick = true;
                    removeLayout.setVisibility(View.VISIBLE);


                }
            };

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startTime = System.currentTimeMillis();
                        handler_longclick.postDelayed(runnable_longClick, 600);


                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (isLongClick) {
                            pointerX = (int) motionEvent.getRawX() - radius;
                            pointerY = (int) motionEvent.getRawY() - statusBarHeight - radius;
                            bubbleParams.x = pointerX;
                            bubbleParams.y = pointerY;
                            if ((pointerX > (removeViewX - 120) && pointerX < (removeViewX + 120 + (radius * 2))) && (pointerY > (removeViewY - 120) && pointerY < (removeViewY + 120))) {

                                if (isNearToRemoveView == false) {

                                    final int initialX = bubbleParams.x;
                                    final int initialY = bubbleParams.y;
                                    final int finalX = removeViewX;
                                    final int finalY = removeViewY;
                                    final float diffX = finalX - initialX;
                                    final float diffY = finalY - initialY;
                                    objectAnimatorTowardsRemoveView = new ObjectAnimator();
                                    objectAnimatorTowardsRemoveView.setDuration(100);
                                    objectAnimatorTowardsRemoveView.setFloatValues(0, 100);
                                    objectAnimatorTowardsRemoveView.setInterpolator(new OvershootInterpolator());
                                    objectAnimatorTowardsRemoveView.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                        @Override
                                        public void onAnimationUpdate(ValueAnimator animation) {
                                            float value = (float) animation.getAnimatedValue();
                                            float currentX = initialX + (diffX * value / 100);
                                            float currentY = initialY + (diffY * value / 100);
                                            bubbleParams.x = (int) currentX;
                                            bubbleParams.y = (int) currentY;
                                            windowManager.updateViewLayout(bubbleLayout, bubbleParams);
                                        }
                                    });
                                    objectAnimatorTowardsRemoveView.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            removeImage.animate().scaleX(1.3f).scaleY(1.3f).setDuration(5);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {

                                        }
                                    });
                                    objectAnimatorTowardsRemoveView.start();
                                    isNearToRemoveView = true;
                                }

                            } else {
                                if (isNearToRemoveView == true) {
                                    final int initialX = removeViewX;
                                    final int initialY = removeViewY;

                                    final int finalX = bubbleParams.x;
                                    final int finalY = bubbleParams.y;

                                    final float diffX = finalX - initialX;
                                    final float diffY = finalY - initialY;

                                    objectAnimatorAwayFromRemoveView = new ObjectAnimator();
                                    objectAnimatorAwayFromRemoveView.setDuration(100);
                                    objectAnimatorAwayFromRemoveView.setFloatValues(0, 100);
                                    objectAnimatorAwayFromRemoveView.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                        @Override
                                        public void onAnimationUpdate(ValueAnimator animation) {
                                            float value = (float) animation.getAnimatedValue();
                                            float currentX = initialX + (diffX * value / 100);
                                            float currentY = initialY + (diffY * value / 100);
                                            bubbleParams.x = (int) currentX;
                                            bubbleParams.y = (int) currentY;
                                            windowManager.updateViewLayout(bubbleLayout, bubbleParams);
                                        }
                                    });
                                    removeImage.animate().scaleX(1f).scaleY(1f).setDuration(5);
                                    objectAnimatorAwayFromRemoveView.start();
                                    isNearToRemoveView = false;
                                } else {
                                    windowManager.updateViewLayout(bubbleLayout, bubbleParams);
                                }
                            }
                            isBubbleMove = true;
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        isLongClick = false;
                        removeLayout.setVisibility(View.GONE);
                        handler_longclick.removeCallbacks(runnable_longClick);
                        endTime = System.currentTimeMillis();
                        isBubbleLeft = (pointerX < (screenWidth / 2)) ? true : false;
                        if (isBubbleMove) {
                            if (isBubbleLeft) {
                                ObjectAnimator objectAnimator = new ObjectAnimator();
                                objectAnimator.setDuration(ANIMATION_DURATION_MEDIUM);
                                objectAnimator.setFloatValues(pointerX, 0);
                                objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                        float value = (float) valueAnimator.getAnimatedValue();
                                        bubbleParams.x = (int) value;
                                        bubbleParams.y = pointerY;
                                        windowManager.updateViewLayout(bubbleLayout, bubbleParams);
                                        isBubbleMove = false;
                                    }
                                });
                                objectAnimator.start();

                            } else {
                                ObjectAnimator objectAnimator = new ObjectAnimator();
                                objectAnimator.setDuration(ANIMATION_DURATION_MEDIUM);
                                objectAnimator.setFloatValues(pointerX, screenWidth - 2 * radius);
                                objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                        float value = (float) valueAnimator.getAnimatedValue();
                                        bubbleParams.x = (int) value;
                                        bubbleParams.y = pointerY;
                                        windowManager.updateViewLayout(bubbleLayout, bubbleParams);
                                        isBubbleMove = false;
                                    }
                                });
                                objectAnimator.start();
                            }
                        }

                        if (endTime - startTime < 100) {
                            bubbleLayout_click();
                        }
                        break;
                }

                return true;
            }
        });

        bubbleLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    bubbleLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    bubbleLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                radius = bubbleLayout.getWidth() / 2;

            }
        });

    }
boolean isClicked = false;
    private void bubbleLayout_click() {
        if(isClicked) {
            iv_bubble.animate().scaleX(1f).scaleY(1f).setDuration(ANIMATION_DURATION_MEDIUM).setInterpolator(new OvershootInterpolator());
            iv_innerCircle.setVisibility(View.VISIBLE);
            iv_innerPlus.animate().setDuration(ANIMATION_DURATION_SLOW).rotation(-135);
            iv_innerPlus.setVisibility(View.GONE);
        isClicked = false;
        }else {
            iv_bubble.animate().scaleX(.85f).scaleY(.85f).setDuration(ANIMATION_DURATION_MEDIUM).setInterpolator(new OvershootInterpolator());
            iv_innerCircle.setVisibility(View.GONE);
            iv_innerPlus.setVisibility(View.VISIBLE);
            iv_innerPlus.animate().setDuration(ANIMATION_DURATION_SLOW).rotation(135);
            isClicked = true;
        }

        sub_bubbles_layout1.setVisibility(View.VISIBLE);
        sub_bubbles_layout2.setVisibility(View.VISIBLE);
    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
