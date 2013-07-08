package com.example.mylockscreen.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.CheckBox;

import android.widget.CompoundButton;
import android.widget.ImageView;
import com.example.mylockscreen.R;
import com.example.mylockscreen.services.KeyGuardService;
import com.example.mylockscreen.utils.Constants;
import com.example.mylockscreen.utils.Preferences;
import com.example.mylockscreen.widgets.SliderRelativeLayout;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    CheckBox lockBtn;

    private SliderRelativeLayout sliderLayout = null;

    private ImageView imgView_getup_arrow; // 动画图片
    private AnimationDrawable animArrowDrawable = null;

    private Context mContext = null ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, Constants.TAG + "main activity create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lockBtn = (CheckBox) this.findViewById(R.id.lockBtn);
        lockBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Preferences.setUserPrefBoolean(MainActivity.this, "app_on", b);
                Intent localIntent = new Intent(MainActivity.this, KeyGuardService.class);
                Log.d(TAG, Constants.TAG + "checked changed");
                if (b)
                    localIntent.putExtra("action", "com.secretlisa.beidanci.SWITCH_ON");
                //while (true)
                if (true)
                {
                    Log.d(TAG, Constants.TAG + "start service");
                    MainActivity.this.startService(localIntent);
/*                    if (!b){
                        localIntent.putExtra("action", "com.secretlisa.beidanci.SWITCH_OFF");
                        break;
                    }*/
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, Constants.TAG + "main activity pause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, Constants.TAG + "main activity destroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
