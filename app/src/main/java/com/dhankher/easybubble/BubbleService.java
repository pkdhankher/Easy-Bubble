package com.dhankher.easybubble;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
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

import static android.content.ContentValues.TAG;

/**
 * Created by Dhankher on 1/25/2017.
 */

public class BubbleService extends Service {

    public static WindowManager windowManager;
    WindowManager.LayoutParams bubbleParams, removeParams, sub_bubbles_layoutParams1, sub_bubbles_layoutParams2, sub_bubbles_layoutParams3, sub_bubbles_layoutParams4, sub_bubbles_layoutParams5;
    LayoutInflater layoutInflater;
    FrameLayout bubbleLayout, removeLayout, subBubblesLayout, imgframe, sub_bubbles_layout1, sub_bubbles_layout2, sub_bubbles_layout3, sub_bubbles_layout4, sub_bubbles_layout5;
    int screenWidth, screenHeight, statusBarHeight;
    int pointerX, pointerY, radius;
    ImageView removeImage;
    ImageView iv_innerCircle, iv_innerPlus, iv_bubble;
    ImageView iv_1, iv_2, iv_3, iv_4, iv_5;
    ObjectAnimator objectAnimatorTowardsRemoveView, objectAnimatorAwayFromRemoveView;
    private boolean isNearToRemoveView = false;
    int removeViewX = 303, removeViewY = 1076;
    private boolean isBubbleLeft = false;
    private final static int ANIMATION_DURATION_FAST = 100;
    private final static int ANIMATION_DURATION_MEDIUM = 300;
    private final static int ANIMATION_DURATION_SLOW = 500;
    private boolean isBubbleMove = false;
    String bubblePosition;
    public static int screenheight_percent = 0;
    public static int screenWidth_percent = 0;
    Context context;
    static BubbleService bubbleService;
    AddingViewClass addingViewClass;
    WindowManager.LayoutParams currentVIew, previousView;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        bubbleService = this;
        statusBarHeight = getStatusBarHeight();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenWidth_percent = screenWidth / 100;
        Log.d(TAG, "screenWidth " + screenWidth);
        screenHeight = size.y;
        screenheight_percent = screenHeight / 100;
        Log.d(TAG, "screenheight " + screenHeight);

        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);


        sub_bubbles_layout5 = (FrameLayout) layoutInflater.inflate(R.layout.sub_bubbles_layout5, null);
        iv_5 = (ImageView) sub_bubbles_layout5.findViewById(R.id.iv_5);
        sub_bubbles_layoutParams5 = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT
        );
        sub_bubbles_layoutParams5.gravity = Gravity.TOP | Gravity.LEFT;
        windowManager.addView(sub_bubbles_layout5, sub_bubbles_layoutParams5);

        sub_bubbles_layout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < 7; i++) {
                    addView(i);
                }
            }
        });

//

        bubbleLayout = (FrameLayout) layoutInflater.inflate(R.layout.main_bubble, null);
        iv_innerCircle = (ImageView) bubbleLayout.findViewById(R.id.iv_InnerCircle);
        iv_innerPlus = (ImageView) bubbleLayout.findViewById(R.id.iv_InnerPlus);
        iv_bubble = (ImageView) bubbleLayout.findViewById(R.id.iv_bubble);
        bubbleParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        bubbleParams.gravity = Gravity.LEFT | Gravity.TOP;
        windowManager.addView(bubbleLayout, bubbleParams);


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
                        handler_longclick.postDelayed(runnable_longClick, 100);


                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (isLongClick) {

                            iv_innerPlus.setVisibility(View.GONE);
//                            sub_bubbles_layout1.setVisibility(View.GONE);
//                            sub_bubbles_layout2.setVisibility(View.GONE);
//                            sub_bubbles_layout3.setVisibility(View.GONE);
//                            sub_bubbles_layout4.setVisibility(View.GONE);
                            sub_bubbles_layout5.setVisibility(View.GONE);
                            isClicked = false;
                            isClicked = false;
                            iv_bubble.animate().scaleX(1f).scaleY(1f).setDuration(ANIMATION_DURATION_MEDIUM).setInterpolator(new OvershootInterpolator());
                            iv_innerCircle.setVisibility(View.VISIBLE);


                            pointerX = (int) motionEvent.getRawX() - radius;
                            pointerY = (int) motionEvent.getRawY() - statusBarHeight - radius;
                            bubbleParams.x = pointerX;
                            Log.d(TAG, "onTouchX: " + bubbleParams.x);
                            bubbleParams.y = pointerY;
                            Log.d(TAG, "onTouchY: " + bubbleParams.y);
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

                        if ((pointerX > (removeViewX - 120) && pointerX < (removeViewX + 120 + (radius * 2))) && (pointerY > (removeViewY - 120) && pointerY < (removeViewY + 120))) {
                            windowManager.removeViewImmediate(bubbleLayout);
                            windowManager.removeViewImmediate(removeLayout);
//                            windowManager.removeViewImmediate(sub_bubbles_layout1);
//                            windowManager.removeViewImmediate(sub_bubbles_layout2);
//                            windowManager.removeViewImmediate(sub_bubbles_layout3);
//                            windowManager.removeViewImmediate(sub_bubbles_layout4);
                            windowManager.removeViewImmediate(sub_bubbles_layout5);
                            stopSelf();
                        }

                        removeLayout.setVisibility(View.GONE);
                        handler_longclick.removeCallbacks(runnable_longClick);
                        endTime = System.currentTimeMillis();
                        isBubbleLeft = (pointerX < (screenWidth / 2)) ? true : false;
                        if (isBubbleMove) {
                            if (isBubbleLeft) {
                                bubblePosition = "left";
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
                                bubblePosition = "right";
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


                        if ((endTime - startTime) < 200) {
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

    private void addView(int position) {

        sub_bubbles_layout4 = (FrameLayout) layoutInflater.inflate(R.layout.sub_bubbles_layout4, null);
        iv_4 = (ImageView) sub_bubbles_layout4.findViewById(R.id.iv_4);
        sub_bubbles_layoutParams4 = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT
        );
        sub_bubbles_layoutParams4.gravity = Gravity.TOP | Gravity.LEFT;

        addingViewClass.addView(sub_bubbles_layout4, sub_bubbles_layoutParams4, position, pointerX, pointerY);
    }

    private boolean isClicked = false;


    private void bubbleLayout_click() {


        if (isClicked) {

            iv_bubble.animate().scaleX(1f).scaleY(1f).setDuration(ANIMATION_DURATION_MEDIUM).setInterpolator(new OvershootInterpolator());
            iv_innerCircle.setVisibility(View.VISIBLE);
            iv_innerPlus.animate().setDuration(ANIMATION_DURATION_SLOW).rotation(-45);
            iv_innerPlus.setVisibility(View.GONE);
            if (isBubbleLeft) {
                collapseAllLeft();

            } else if (!isBubbleLeft) {
                collapseAllRight();

            }

            isClicked = false;

        } else {
            iv_bubble.animate().scaleX(.85f).scaleY(.85f).setDuration(ANIMATION_DURATION_MEDIUM).setInterpolator(new OvershootInterpolator());
            iv_innerCircle.setVisibility(View.GONE);
            iv_innerPlus.setVisibility(View.VISIBLE);
            iv_innerPlus.animate().setDuration(ANIMATION_DURATION_SLOW).rotation(45);
            ObjectAnimator objectAnimator = new ObjectAnimator();

            // bubble is in the left assumed here
            if (isBubbleLeft) {
                if (pointerY < (25 * screenheight_percent)) {
                    objectAnimator.setIntValues(pointerY, 22 * screenheight_percent);
                    objectAnimator.setDuration(ANIMATION_DURATION_MEDIUM);

                    objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            int value = (int) valueAnimator.getAnimatedValue();
                            bubbleParams.x = 0;
                            bubbleParams.y = value;
                            windowManager.updateViewLayout(bubbleLayout, bubbleParams);

                        }
                    });

                    objectAnimator.start();

                    pointerY = 22 * screenheight_percent;

                } else if (pointerY > (75 * screenheight_percent)) {
                    objectAnimator.setIntValues(pointerY, 68 * screenheight_percent);
                    objectAnimator.setDuration(ANIMATION_DURATION_MEDIUM);

                    objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            int value = (int) valueAnimator.getAnimatedValue();
                            bubbleParams.x = 0;
                            bubbleParams.y = value;
                            windowManager.updateViewLayout(bubbleLayout, bubbleParams);
                        }
                    });

                    objectAnimator.start();
                    pointerY = 68 * screenheight_percent;
                } else {
                    expandAllLeft();
                }
                objectAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        expandAllLeft();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });

            } else if (!isBubbleLeft) {
                if (pointerY < (25 * screenheight_percent)) {
                    objectAnimator.setIntValues(pointerY, 22 * screenheight_percent);
                    objectAnimator.setDuration(ANIMATION_DURATION_MEDIUM);

                    objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            int value = (int) valueAnimator.getAnimatedValue();
                            bubbleParams.x = screenWidth - (2 * radius);
                            bubbleParams.y = value;
                            windowManager.updateViewLayout(bubbleLayout, bubbleParams);

                        }
                    });

                    objectAnimator.start();
                    pointerY = 22 * screenheight_percent;

                } else if (pointerY > (78 * screenheight_percent)) {
                    windowManager.updateViewLayout(bubbleLayout, bubbleParams);
                    objectAnimator.setIntValues(pointerY, 68 * screenheight_percent);
                    objectAnimator.setDuration(ANIMATION_DURATION_MEDIUM);

                    objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            int value = (int) valueAnimator.getAnimatedValue();
                            bubbleParams.x = screenWidth - (2 * radius);
                            bubbleParams.y = value;
                            windowManager.updateViewLayout(bubbleLayout, bubbleParams);

                        }
                    });

                    objectAnimator.start();
                    pointerY = 68 * screenheight_percent;
                } else {
                    expandAllRight();


                }
                objectAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        expandAllRight();

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
            }
            isClicked = true;
        }


    }

    private void expandAllRight() {
//        expand(screenWidth - (2 * radius), pointerY, (screenWidth - (2 * screenWidth_percent) - (2 * radius)), pointerY - (18 * screenheight_percent), sub_bubbles_layout1, sub_bubbles_layoutParams1);
//
//        expand(screenWidth - (2 * radius), pointerY, screenWidth - (2 * radius) - (23 * (screenWidth_percent)), pointerY - (13 * (screenheight_percent)), sub_bubbles_layout2, sub_bubbles_layoutParams2);
//
//        expand(screenWidth - (2 * radius), pointerY, screenWidth - (2 * radius) - (30 * (screenWidth_percent)), pointerY, sub_bubbles_layout3, sub_bubbles_layoutParams3);
//
//        expand(screenWidth - (2 * radius), pointerY, screenWidth - (2 * radius) - (23 * (screenWidth_percent)), pointerY + (13 * (screenheight_percent)), sub_bubbles_layout4, sub_bubbles_layoutParams4);

        expand(screenWidth - (2 * radius), pointerY, (screenWidth - (2 * screenWidth_percent) - (2 * radius)), pointerY + (18 * screenheight_percent), sub_bubbles_layout5, sub_bubbles_layoutParams5);
    }

    private void expandAllLeft() {
//        expand(0, pointerY, (2 * (screenWidth_percent)), pointerY - (18 * (screenheight_percent)), sub_bubbles_layout1, sub_bubbles_layoutParams1);
//
//        expand(0, pointerY, (23 * (screenWidth_percent)), pointerY - (13 * (screenheight_percent)), sub_bubbles_layout2, sub_bubbles_layoutParams2);
//
//        expand(0, pointerY, (30 * (screenWidth_percent)), pointerY, sub_bubbles_layout3, sub_bubbles_layoutParams3);
//
//        expand(0, pointerY, (23 * (screenWidth_percent)), pointerY + (13 * (screenheight_percent)), sub_bubbles_layout4, sub_bubbles_layoutParams4);

        expand(0, pointerY, (2 * (screenWidth_percent)), pointerY + (18 * (screenheight_percent)), sub_bubbles_layout5, sub_bubbles_layoutParams5);
    }

    private void collapseAllRight() {
//        collapse((screenWidth - (2 * screenWidth_percent) - (2 * radius)), pointerY - (18 * screenheight_percent), screenWidth - (2 * radius), pointerY, sub_bubbles_layout1, sub_bubbles_layoutParams1);
//
//        collapse(screenWidth - (2 * radius) - (23 * (screenWidth_percent)), pointerY - (13 * (screenheight_percent)), screenWidth - (2 * radius), pointerY, sub_bubbles_layout2, sub_bubbles_layoutParams2);
//
//        collapse(screenWidth - (2 * radius) - (30 * (screenWidth_percent)), pointerY, screenWidth - (2 * radius), pointerY, sub_bubbles_layout3, sub_bubbles_layoutParams3);
//
//        collapse(screenWidth - (2 * radius) - (23 * (screenWidth_percent)), pointerY + (13 * (screenheight_percent)), screenWidth - (2 * radius), pointerY, sub_bubbles_layout4, sub_bubbles_layoutParams4);

        collapse((screenWidth - (2 * screenWidth_percent) - (2 * radius)), pointerY + (18 * screenheight_percent), screenWidth - (2 * radius), pointerY, sub_bubbles_layout5, sub_bubbles_layoutParams5);
    }

    private void collapseAllLeft() {
//        collapse(2 * screenWidth_percent, pointerY - 18 * screenheight_percent, 0, pointerY, sub_bubbles_layout1, sub_bubbles_layoutParams1);
//
//        collapse(23 * screenWidth_percent, pointerY - 13 * screenheight_percent, 0, pointerY, sub_bubbles_layout2, sub_bubbles_layoutParams2);
//
//        collapse(30 * screenWidth_percent, pointerY, 0, pointerY, sub_bubbles_layout3, sub_bubbles_layoutParams3);
//
//        collapse(23 * screenWidth_percent, pointerY + 13 * screenheight_percent, 0, pointerY, sub_bubbles_layout4, sub_bubbles_layoutParams4);

        collapse(2 * screenWidth_percent, pointerY + 18 * screenheight_percent, 0, pointerY, sub_bubbles_layout5, sub_bubbles_layoutParams5);
    }


    private void expand(final int initialValueX, final int initialValueY, int finalValueX, int finalValueY, final FrameLayout subLayout, final WindowManager.LayoutParams subLayoutParams) {
        final int diffX = finalValueX - initialValueX;
        final int diffY = finalValueY - initialValueY;
        ObjectAnimator objectAnimator = new ObjectAnimator();
        objectAnimator.setDuration(ANIMATION_DURATION_MEDIUM);
        objectAnimator.setFloatValues(0, 100);
        objectAnimator.setInterpolator(new OvershootInterpolator());
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();

                float currentX = initialValueX + (diffX * value / 100);
                float currentY = initialValueY + (diffY * value / 100);
                subLayoutParams.x = (int) currentX;
                subLayoutParams.y = (int) currentY;
                windowManager.updateViewLayout(subLayout, subLayoutParams);
            }
        });
        objectAnimator.start();
        subLayout.setVisibility(View.VISIBLE);
        ///////////////////////////////////////////////////////////////
        int finalXofBubbleLayout = bubbleParams.x;
        int finalYofBubbleLayout = bubbleParams.y;

    }


    private void collapse(final int initialValueX, final int initialValueY, int finalValueX, int finalValueY, final FrameLayout subLayout, final WindowManager.LayoutParams subLayoutParams) {
        final int diffX = initialValueX - finalValueX;
        final int diffY = initialValueY - finalValueY;

        ObjectAnimator objectAnimator = new ObjectAnimator();
        objectAnimator.setDuration(ANIMATION_DURATION_MEDIUM);
        objectAnimator.setFloatValues(0, 100);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();

                float currentX = initialValueX - (diffX * value / 100);
                float currentY = initialValueY - (diffY * value / 100);
                subLayoutParams.x = (int) currentX;
                subLayoutParams.y = (int) currentY;
                windowManager.updateViewLayout(subLayout, subLayoutParams);

            }
        });
        objectAnimator.start();
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                subLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static BubbleService getInstance() {
        return bubbleService;
    }
}
