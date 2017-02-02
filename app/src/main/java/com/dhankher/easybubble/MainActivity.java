package com.dhankher.easybubble;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import com.goldducks.splashAnimations.SplashScreen;

public class MainActivity extends AppCompatActivity {

    GenerateViews adapter;
    AddingViewClass addingViewClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_main);
     //   SplashScreen.show(this,SplashScreen.TERMINAL_ANIMATION);
        if (android.os.Build.VERSION.SDK_INT >= 23) {   //Android M Or Over
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1);
            }
        }

        startService(new Intent(MainActivity.this, BubbleService.class));
        adapter = new GenerateViews(this);
        addingViewClass=  new AddingViewClass(this,adapter);

          finish();


    }
}
