package com.eworl.easybubble.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eworl.easybubble.R;
import com.eworl.easybubble.RecyclerViewListeners.RvItemDragListener;

/**
 * Created by Dhankher on 3/1/2017.
 */

public class RvHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "RecyclerViewHolder";
    public TextView appName;
    public ImageView appIcon;
    private Context context;
    private View itemView;
    public FrameLayout flRecycleViewItem;
    private RvItemDragListener rvItemDragListener;

    public RvHolder(View itemView) {
        super(itemView);

        this.itemView = itemView;
        appName = (TextView) itemView.findViewById(R.id.country_name);
        appIcon = (ImageView) itemView.findViewById(R.id.appIcon);
        flRecycleViewItem = (FrameLayout) itemView.findViewById(R.id.flRecycleItem);
    }

    private void itemViewonClick() {
        Toast.makeText(context, "onclick", Toast.LENGTH_SHORT).show();
//        clickListener = new AllitemViewTouchListener(context, itemList, this, mainActivity);
//        itemView.setOnTouchListener(clickListener);
    }

}