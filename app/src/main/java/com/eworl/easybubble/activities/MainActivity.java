package com.eworl.easybubble.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.eworl.easybubble.Adapter.RvAdapter;
import com.eworl.easybubble.RecyclerViewListeners.Listener;
import com.eworl.easybubble.Service.BubbleService;
import com.eworl.easybubble.db.DaoMaster;
import com.eworl.easybubble.db.DaoSession;
import com.eworl.easybubble.db.Program;
import com.eworl.easybubble.db.ProgramDao;
import com.eworl.easybubble.eventBus.BubbleServiceIsRunning;
import com.eworl.easybubble.LayoutParamGenerator;
import com.eworl.easybubble.PermissionManager;
import com.eworl.easybubble.R;
import com.eworl.easybubble.ViewManager;
import com.eworl.easybubble.bubbles.MasterBubble;
import com.eworl.easybubble.bubbles.SubBubble;
import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity implements Listener, CallBack {

    private MasterBubble masterBubble;
    private Button startServiceButton;
    private ViewManager viewManager;
    private GridLayoutManager glmAllApps;
    private GridLayoutManager glmSelectedApps;
    private List<Program> allItems;
    private List<Program> rowListItem;
    private final String DB_NAME = "logs-db";
    private boolean onClick = false;
    public TextView textEmptyListTop, textEmptyListBottom;
    public RecyclerView rvSelectedApps, rvAllApps;
    private ProgramDao programDao_object;
    private List<Program> log_list;
    public ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        textEmptyListTop = (TextView) findViewById(R.id.TVSelectedItemListEmpty);
        textEmptyListBottom = (TextView) findViewById(R.id.TVAllItemListEmpty);
        startServiceButton = (Button) findViewById(R.id.button);

        new Thread(new FetchingAppsTask(this, allItems, this)).start();

        progress = new ProgressDialog(this);
        progress.setMessage("Uploading ☺");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

    }

    public void startServive(View view) {
        if (onClick) {
            stopService();
            onClick = false;
        } else {
            loadActivity(allItems);
            viewManager.addView(masterBubble.getView(), LayoutParamGenerator.getNewLayoutParams());
            EventBus.getDefault().post(new BubbleServiceIsRunning());
//            Intent intent = new Intent(this, BubbleService.class);
//            startService(intent);
            onClick = true;
        }
    }


    public List<Program> getAllItemList() {

        return allItems;
    }

    public ProgramDao setupDb() {
        DaoMaster.DevOpenHelper masterHelper = new DaoMaster.DevOpenHelper(this, DB_NAME, null); //create database db file if not exist
        SQLiteDatabase db = masterHelper.getWritableDatabase();  //get the created database db file
        DaoMaster master = new DaoMaster(db);//create masterDao
        DaoSession masterSession = master.newSession(); //Creates Session session

        return masterSession.getProgramDao();
    }

    public ProgramDao getProgramDaoInstance() {
        if (programDao_object != null) {
            return programDao_object;
        } else {
            return setupDb();
        }
    }

    private void loadActivity(List<Program> allItems) {
        Log.d(TAG, "allItems size: " + allItems.size());
        programDao_object = setupDb();
        log_list = programDao_object.queryBuilder().orderDesc(ProgramDao.Properties.Id).build().list();
        Log.d(TAG, "log_list size: " + log_list.size());


        rowListItem = allItems;
        for (int i = 0; i < 5; i++) {
            Log.d(TAG, "rowListItems Apps Names: " + allItems.get(i).getAppName());
        }


//default bubbles for initial list -----
        int backIcon = R.drawable.back;
        int homeIcon = R.drawable.home;
        int lockIcon = R.drawable.locked;

        insertIntoLog_List("back", backIcon,"com.back");
        insertIntoLog_List("home", homeIcon,"com.home");
        insertIntoLog_List("lock", lockIcon,"com.lock");
        Log.d(TAG, "log_list size: " + log_list.size());

//already Added bubbles ----
        for (int i = 0; i < rowListItem.size(); i++) {
            String pak = rowListItem.get(i).getPackageName();
            for (int j = 0; j < log_list.size(); j++) {
                String pak1 = log_list.get(j).getPackageName();
                if (pak.equals(pak1)) {
                    rowListItem.get(i).setIsSelected(true);
                }
            }
        }

//removing added apps from All Apps list -----
        Log.d(TAG, "loglistsizeeee: "+log_list.size());
        for (int i = 0; i < rowListItem.size(); i++) {

            boolean isSelected = rowListItem.get(i).getIsSelected();
            Log.d(TAG, "isSelected: " + rowListItem.get(i).getAppName() + " : " + isSelected);
            if (isSelected) {
                rowListItem.remove(i);
                i--;
            }
        }
        Log.d(TAG, "allItems size: " + allItems.size());


        glmAllApps = new GridLayoutManager(this, 4);
        glmSelectedApps = new GridLayoutManager(this, 4);

        rvSelectedApps = (RecyclerView) findViewById(R.id.rvSelectedAppList);
        rvSelectedApps.setLayoutManager(glmSelectedApps);
        RvAdapter rvAdapter1 = new RvAdapter(MainActivity.this, log_list, this, this);
        rvSelectedApps.setAdapter(rvAdapter1);


        rvAllApps = (RecyclerView) findViewById(R.id.rvAppList);
        rvAllApps.setHasFixedSize(true);
        rvAllApps.setLayoutManager(glmAllApps);
        RvAdapter rvAdapter2 = new RvAdapter(MainActivity.this, rowListItem, this, this);
        rvAllApps.setAdapter(rvAdapter2);

        textEmptyListTop.setOnDragListener(rvAdapter1.getDragInstance());
        textEmptyListTop.setVisibility(View.GONE);
        textEmptyListBottom.setOnDragListener(rvAdapter2.getDragInstance());
        textEmptyListBottom.setVisibility(View.GONE);

        PermissionManager.checkForOverlayPermission(this);

        viewManager = ViewManager.init(this);

        masterBubble = new MasterBubble(this, log_list);


        for (int i = 0; i < log_list.size(); i++) {
            SubBubble subBubble = new SubBubble(this, log_list, masterBubble);
            String img = log_list.get(i).getAppIcon();
            Log.d(TAG, "img:" + img);
            byte[] bitmapdata = Base64.decode(img, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
            Drawable icon = new BitmapDrawable(getResources(), bitmap);
            Log.d(TAG, "onCreate: " + icon);
            subBubble.setIcon(icon);
            subBubble.setId(i);
            masterBubble.addSubBubble(subBubble);
        }


    }

    @Override
    public void setEmptyListTop(boolean visibility) {
        textEmptyListTop.setVisibility(visibility ? View.VISIBLE : View.GONE);
        rvSelectedApps.setVisibility(visibility ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setEmptyListBottom(boolean visibility) {
        textEmptyListBottom.setVisibility(visibility ? View.VISIBLE : View.GONE);
        rvAllApps.setVisibility(visibility ? View.GONE : View.VISIBLE);
    }


    public void stopService() {
        viewManager.removeView(masterBubble.getView());
    }

    public void reStartService() {
        loadActivity(allItems);
        viewManager.addView(masterBubble.getView(), LayoutParamGenerator.getNewLayoutParams());
    }

    public int getRvAllAppsId() {
        return rvAllApps.getId();
    }

    public int getrvSelectedAppsId() {
        return rvSelectedApps.getId();
    }

    public List<Program> getLog_List() {
        return log_list;
    }

    private void insertIntoLog_List(String name, int icon,String packageName) {

        Bitmap img = BitmapFactory.decodeResource(getResources(), icon);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        String image = Base64.encodeToString(imageInByte, Base64.DEFAULT);

        try {
            programDao_object.insert(new Program(null, name, image, packageName, true));

        } catch (Exception e) {
            Log.e(TAG, "item name" + e.toString());
        }
    }


    @Override
    public void onWorkComplited(final List<Program> allItems) {
       handler.postDelayed(runnable,500);
        this.allItems = allItems;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadActivity(allItems);
            }

        });


    }
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            progress.dismiss();
        }
    };


}




