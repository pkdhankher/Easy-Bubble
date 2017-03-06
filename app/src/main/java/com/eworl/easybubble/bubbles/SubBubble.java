package com.eworl.easybubble.bubbles;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.eworl.easybubble.R;
import com.eworl.easybubble.db.program;
import com.eworl.easybubble.eventBus.MasterBubbleInLeft;
import com.eworl.easybubble.eventBus.MasterBubbleInRight;
import com.eworl.easybubble.eventBus.RotateSubBubbleEvent;
import com.eworl.easybubble.eventBus.StaticSubBubbleCoordinatesEvent;
import com.eworl.easybubble.utils.Coordinate;
import com.eworl.easybubble.utils.ValueGenerator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 3/2/17.
 */

public class SubBubble {
    private FrameLayout fmContentView, fmSubBubbleView;
    private ImageView ivIcon;
    private Context context;
    private Drawable iconId;
    private Coordinate coordinates;
    private float pointerDownY, pointerDownX;
    private ValueGenerator valueGenerator;
    private MasterBubble masterBubble2nd;
    private long startTime, endTime;
    private int radius;
    private float diffY;
    private FrameLayout.LayoutParams fmContentViewParams;
    private boolean masterBubbleInRight = false;
    private List<program> log_list;
    private MasterBubble masterBubble;

    public SubBubble(Context context, List<program> log_list,MasterBubble masterBubble) {
        this.context = context;
        this.log_list = log_list;
        this.masterBubble = masterBubble;
        masterBubble2nd = new MasterBubble(context, log_list);
        valueGenerator = masterBubble2nd.getValueGenerator();
        radius = valueGenerator.getRadius();
        EventBus.getDefault().register(this);

//        fmContentViewParams = (FrameLayout.LayoutParams) fmContentView.getLayoutParams();
        intializeViews();
        setListeners();

    }


    private void intializeViews() {
        fmContentView = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.layout_sub_bubble, null);
        fmSubBubbleView = (FrameLayout) fmContentView.findViewById(R.id.fmSubBubbleView);
        ivIcon = (ImageView) fmContentView.findViewById(R.id.ivIcon);
    }

    private void setListeners() {
        fmSubBubbleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        performeActionDown(motionEvent);
                        break;
                    case MotionEvent.ACTION_UP:
                        performeActionUp(motionEvent);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        performeActionMove(motionEvent);
                        break;
                }
                return false;
            }
        });

//        closeMasterBubble();
//        performAction();
    }

    private void performeActionMove(MotionEvent motionEvent) {
        float x = motionEvent.getRawX();
        float y = motionEvent.getRawY();
        diffY = pointerDownY - y;
        if (masterBubbleInRight) {
            diffY = -(pointerDownY - y);
        }

        rotateSubBubble();
        Log.d(TAG, "diffX: " + diffY / 10);

    }


    private void performeActionUp(MotionEvent motionEvent) {
        endTime = System.currentTimeMillis();

        if (endTime - startTime < 100) {
            fmSubBubbleViewOnClick(motionEvent);
        }
//       staticSubBubbleCoordinates();
    }

    private void staticSubBubbleCoordinates() {
        EventBus.getDefault().post(new StaticSubBubbleCoordinatesEvent());
    }

    private void fmSubBubbleViewOnClick(MotionEvent motionEvent) {

        performAction(motionEvent);
    }

    private void performeActionDown(MotionEvent motionEvent) {
        pointerDownX = motionEvent.getRawX();
        pointerDownY = motionEvent.getRawY();
        startTime = System.currentTimeMillis();

    }

    private void rotateSubBubble() {
        EventBus.getDefault().post(new RotateSubBubbleEvent(context, diffY));

    }

    private void performAction(MotionEvent motionEvent) {
        ArrayList<SubBubble> subBubbleList =  masterBubble.getSubBubbleList();
        subBubbleList.size();

        Log.d(TAG, "SubBubbleList size: " + subBubbleList.size());
        Toast.makeText(context, "Action Performed", Toast.LENGTH_SHORT).show();

    }


    public void setIcon(Drawable iconId) {
        this.iconId = iconId;
        ivIcon.setImageDrawable(iconId);
    }

    public View getView() {
        return fmContentView;
    }

    public FrameLayout getLayout() {
        return fmContentView;
    }


    public ViewGroup.LayoutParams getParams() {

        return fmContentViewParams;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MasterBubbleInRight event) {
        masterBubbleInRight = true;

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MasterBubbleInLeft event) {
        masterBubbleInRight = false;

    }

    public Coordinate getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate coordinates) {
        fmContentView.setX((float) coordinates.getX());
        fmContentView.setY((float) coordinates.getY());
        this.coordinates = coordinates;
    }


}
