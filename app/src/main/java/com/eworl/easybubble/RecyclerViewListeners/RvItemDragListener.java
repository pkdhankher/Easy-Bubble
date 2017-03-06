package com.eworl.easybubble.RecyclerViewListeners;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.eworl.easybubble.Adapter.RvAdapter;
import com.eworl.easybubble.R;
import com.eworl.easybubble.activities.MainActivity;
import com.eworl.easybubble.db.Program;

import java.util.List;

public class RvItemDragListener implements View.OnDragListener {
    private static final String TAG = "DragListener";
    private boolean isDropped = false;
    private Listener mListener;
    private  Context context;
    private MainActivity mainActivity;
    private Program sourceListItem;

    public RvItemDragListener(Listener listener, Context context, MainActivity mainActivity) {
        this.mListener = listener;
        this.context = context;
        this.mainActivity = mainActivity;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        switch (dragEvent.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                break;
            case DragEvent.ACTION_DROP:
                isDropped = true;
                int positionTarget = -1;

                View viewSource = (View) dragEvent.getLocalState();
                Log.d(TAG, "onDrag: "+view);
                if (view.getId() == R.id.flRecycleItem || view.getId() == R.id.TVAllItemListEmpty
                        || view.getId() == R.id.TVSelectedItemListEmpty) {
                    RecyclerView target;

                    if (view.getId() == R.id.TVSelectedItemListEmpty) {
                        target = (RecyclerView) view.getRootView()
                                .findViewById(R.id.rvSelectedAppList);
                    } else if (view.getId() == R.id.TVAllItemListEmpty) {
                        target = (RecyclerView) view.getRootView()
                                .findViewById(R.id.rvAppList);
                    } else {
                        target = (RecyclerView) view.getParent();
                        positionTarget = (int) view.getTag();
                    }

                    RecyclerView source = (RecyclerView) viewSource.getParent();
                    RvAdapter adapterSource = (RvAdapter) source.getAdapter();
                    int positionSource = (int) viewSource.getTag();

                    sourceListItem = adapterSource.getList().get(positionSource);
                    Log.d(TAG, "item App Name: "+sourceListItem.getAppName());
// here i have to check item already exist into selected items list or not
                    List<Program> listSource = adapterSource.getList();

                    listSource.remove(positionSource);
                    adapterSource.updateList(listSource);
                    adapterSource.notifyDataSetChanged();

                    RvAdapter adapterTarget = (RvAdapter) target.getAdapter();
                    List<Program> customListTarget = adapterTarget.getList();
                    if (positionTarget >= 0) {
                        customListTarget.add(positionTarget, sourceListItem);
                    } else {
                        Log.d(TAG, "customListTarget.size(): "+customListTarget.size());
                        customListTarget.add(sourceListItem);
                    }
                    adapterTarget.updateList(customListTarget);
                    adapterTarget.notifyDataSetChanged();
                    Log.d(TAG, "customListTarget.size(): "+customListTarget.size());

                  if(source.getId()== mainActivity.getRvAllAppsId() && target.getId() == mainActivity.getrvSelectedAppsId()){
                      try {
                          mainActivity.getProgramDaoInstance().insert(new Program(null, sourceListItem.getAppName(), sourceListItem.getAppIcon(), sourceListItem.getPackageName(), true));
                      }catch (Exception E){
                          E.printStackTrace();
                      }
                          Toast.makeText(context, sourceListItem.getAppName()+" added to list", Toast.LENGTH_SHORT).show();
                  }
                    if(source.getId()== mainActivity.getrvSelectedAppsId()  && target.getId() == mainActivity.getRvAllAppsId()){
                        mainActivity.getProgramDaoInstance().delete(new Program(sourceListItem.getId(),sourceListItem.getAppName(),sourceListItem.getAppIcon(),sourceListItem.getPackageName(),true));
                        Toast.makeText(context, sourceListItem.getAppName()+" removed from list", Toast.LENGTH_SHORT).show();
                    }

                    view.setVisibility(View.VISIBLE);

                    if (source.getId() == R.id.rvAppList
                            && adapterSource.getItemCount() < 1) {
                        mListener.setEmptyListBottom(true);
                    }

                    if (view.getId() == R.id.TVAllItemListEmpty) {
                        mListener.setEmptyListBottom(false);
                    }

                    if (source.getId() == R.id.rvSelectedAppList
                            && adapterSource.getItemCount() < 1) {
                        mListener.setEmptyListTop(true);
                    }

                    if (view.getId() == R.id.TVSelectedItemListEmpty) {
                        mListener.setEmptyListTop(false);
                    }
                }
                break;
        }

        if (!isDropped) {
            ((View) dragEvent.getLocalState()).setVisibility(View.VISIBLE);
        }

        return true;
    }
}