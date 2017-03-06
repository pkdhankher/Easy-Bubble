package com.eworl.easybubble.Adapter;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.eworl.easybubble.R;
import com.eworl.easybubble.RecyclerViewListeners.Listener;
import com.eworl.easybubble.RecyclerViewListeners.RvItemDragListener;
import com.eworl.easybubble.ViewHolder.RvHolder;
import com.eworl.easybubble.activities.MainActivity;
import com.eworl.easybubble.db.Program;

import java.util.List;

/**
 * Created by Dhankher on 3/2/2017.
 */

public class RvAdapter extends RecyclerView.Adapter<RvHolder> {

    private static final String TAG = "RvAdapter";
    private List<Program> rvItemList;
    private Context context;
    private MainActivity mainActivity;
    private Listener mListener;

    public RvAdapter(Context context, List<Program> rvItemList, MainActivity mainActivity, Listener listener) {

        this.rvItemList = rvItemList;
        this.context = context;
        this.mainActivity = mainActivity;
        this.mListener = listener;
    }

    @Override
    public RvHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_list_layout, null);
        RvHolder rcv = new RvHolder(layoutView);
        return rcv;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(RvHolder holder, int position) {

        holder.appName.setText(rvItemList.get(position).getAppName());

        String img = rvItemList.get(position).getAppIcon();
        byte[] bitmapdata = Base64.decode(img, Base64.DEFAULT);
        Bitmap appIcon = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);

        holder.appIcon.setImageBitmap(appIcon);

        holder.flRecycleViewItem.setTag(position);
        holder.flRecycleViewItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    view.startDragAndDrop(data, shadowBuilder, view, 0);
                } else {
                    view.startDrag(data, shadowBuilder, view, 0);
                }
                return true;
//                return false;
            }
        });

        holder.flRecycleViewItem.setOnDragListener(new RvItemDragListener(mListener, context, mainActivity));
    }

    @Override
    public int getItemCount() {
        return this.rvItemList.size();
    }

    public RvItemDragListener getDragInstance() {
        if (mListener != null) {
            return new RvItemDragListener(mListener, context, mainActivity);
        } else {
            Log.e("ListAdapter", "Listener wasn't initialized!");
            return null;
        }
    }


    public List<Program> getList() {
        return rvItemList;
    }

    public void updateList(List<Program> list) {
        rvItemList = list;
    }

}