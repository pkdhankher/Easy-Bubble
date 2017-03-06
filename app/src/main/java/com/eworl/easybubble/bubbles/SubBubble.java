package com.eworl.easybubble.bubbles;

import android.app.ActivityManager;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import com.eworl.easybubble.R;
import com.eworl.easybubble.db.Program;
import com.eworl.easybubble.eventBus.MasterBubbleInLeft;
import com.eworl.easybubble.eventBus.MasterBubbleInRight;
import com.eworl.easybubble.eventBus.RotateSubBubbleEvent;
import com.eworl.easybubble.eventBus.PointerYDiffEvent;
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
    private int id;
    private Coordinate coordinates;
    private float pointerDownY, pointerDownX,pointerUpY;
    private ValueGenerator valueGenerator;
    private MasterBubble masterBubble2nd;
    private long startTime, endTime;
    private int radius;
    private float diffY,pointerYdiff,moveY;
    private FrameLayout.LayoutParams fmContentViewParams;
    private boolean masterBubbleInRight = false;
    private List<Program> log_list;
    private MasterBubble masterBubble;
    private DevicePolicyManager mDPM;
    private ComponentName mDeviceAdminSample;

    public SubBubble(Context context, List<Program> log_list, MasterBubble masterBubble) {
        this.context = context;
        this.log_list = log_list;
        this.masterBubble = masterBubble;
        masterBubble2nd = new MasterBubble(context, log_list);
        valueGenerator = masterBubble2nd.getValueGenerator();
        radius = valueGenerator.getRadius();
        EventBus.getDefault().register(this);

//        mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
//        mDeviceAdminSample = new ComponentName(Potter.this,SubBubble.class);

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

         moveY = motionEvent.getRawY();
        diffY = pointerDownY - moveY;
        if (masterBubbleInRight) {
            diffY = -(pointerDownY - moveY);
        }

        rotateSubBubble();


    }


    private void performeActionUp(MotionEvent motionEvent) {
        endTime = System.currentTimeMillis();


//
//        pointerUpY = pointerDownY - moveY;
//        if (masterBubbleInRight) {
//            pointerUpY = -(pointerDownY - moveY);
//        }
        pointerYDiff();

        if (endTime - startTime < 200) {
            fmSubBubbleViewOnClick(motionEvent);
        }
    }


    private void pointerYDiff() {
        EventBus.getDefault().post(new PointerYDiffEvent(diffY));
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

        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (log_list.get(this.getId()).getPackageName().equals("com.home")) {

            startMain.addCategory(Intent.CATEGORY_HOME);
            context.startActivity(startMain);

        }else if(log_list.get(this.getId()).getPackageName().equals("com.lock")){

            DevicePolicyManager    dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            try {
                dpm.lockNow();

            } catch (SecurityException e) {
                e.printStackTrace();
                Intent it = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                it.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, new ComponentName(context, DeviceAdminReceiver.class));
//                startActivityForResult(it, 0);
            }

        }else if(log_list.get(this.getId()).getPackageName().equals("com.back")){

            startMain.addCategory(Intent.CATEGORY_HOME);
            context.startActivity(startMain);

        }else {
            Intent i;
            PackageManager manager = context.getPackageManager();
            try {
                i = manager.getLaunchIntentForPackage(log_list.get(this.getId()).getPackageName());

                if (i == null)
                    throw new PackageManager.NameNotFoundException();
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                context.startActivity(i);
            } catch (PackageManager.NameNotFoundException e) {

            }
        }
        Toast.makeText(context,log_list.get(this.getId()).getAppName()+" clicked",Toast.LENGTH_SHORT).show();
        masterBubble.toggle();
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


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
