package com.eworl.easybubble.Adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eworl.easybubble.ViewHolder.ViewClickListener;
import com.eworl.easybubble.activities.MainActivity;
import com.eworl.easybubble.db.DaoSession;
import com.eworl.easybubble.db.program;
import com.eworl.easybubble.db.programDao;
import com.eworl.easybubble.utils.ItemObject;
import com.eworl.easybubble.R;
import com.eworl.easybubble.ViewHolder.RecyclerViewHolders;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {

    private static final String TAG = "RecyclerViewAdapter";
    private List<ItemObject> itemList;
    private Context context;
    private List<program> log_list;
    private MainActivity mainActivity;

    public RecyclerViewAdapter(Context context, List<ItemObject> itemList, List<program> log_list, MainActivity mainActivity) {
        this.itemList = itemList;
        this.context = context;
        this.log_list = log_list;
        this.mainActivity = mainActivity;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_list_layout, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView, context, itemList, mainActivity, log_list);
        return rcv;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {


        Log.d(TAG, "positionss: " + position);
        for (int i = 0; i < itemList.size(); i++) {
            Log.d(TAG, "boolean: " + itemList.get(i).isClicked());
        }

        if ((itemList.get(position).isClicked()) == true) {
            holder.appName.setText(itemList.get(position).getAppName());
            holder.appIcon.setImageDrawable(itemList.get(position).getAppIcon());
            holder.addIcon.setImageResource(R.drawable.red_square);
            holder.plusIcon.setImageResource(R.drawable.cross);
        } else {
            holder.appName.setText(itemList.get(position).getAppName());
            holder.appIcon.setImageDrawable(itemList.get(position).getAppIcon());
            holder.addIcon.setImageResource(itemList.get(position).getGreenIcon());
            holder.plusIcon.setImageResource(itemList.get(position).getPlusIcon());
        }
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
