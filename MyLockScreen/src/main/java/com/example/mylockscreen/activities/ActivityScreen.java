package com.example.mylockscreen.activities;

/**
 * Created by haihong.xiahh on 13-7-3.
 */
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;
import com.example.mylockscreen.R;

public class ActivityScreen extends Activity
{
    private final String TAG = "ActivityScreen";
    TimeChangeReceiver mTimeChangeReceiver;
    private void initViews(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "xhh: activity screen lock view create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        initViews();
        this.mTimeChangeReceiver = new TimeChangeReceiver();
    }
    public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
    {
        return false;
    }

    protected void onDestroy()
    {
        Log.d(TAG, "xhh: activity screen lock view destroy");
        super.onDestroy();
/*        if ((this.bitmap != null) && (!this.bitmap.isRecycled()))
            this.bitmap.recycle();*/
/*        if (!((ApplicationBeidanci)getApplicationContext()).activityOn)
        {
            MobclickAgent.onKillProcess(this);
            Database.getInstance(this).close();
            Process.killProcess(Process.myPid());
        }*/
    }

    protected void onNewIntent(Intent paramIntent)
    {
        super.onNewIntent(paramIntent);
        //this.log.d("=========onNewIntent========");
    }

    protected void onPause()
    {
        Log.d(TAG, "xhh: activity screen lock view pause");
        super.onPause();
        try
        {
/*            if (this.flagRigister)
            {
                this.mTimeChangeReceiver.unregister();
                this.flagRigister = false;
            }*/
            return;
        }
        catch (Exception localException)
        {
            //MobclickAgent.reportError(this, "defined error myself:\n" + Utils.getErrorString(localException));
        }
    }

    protected void onResume()
    {
        super.onResume();
/*        Utils.checkDailyWord(this);
        changeTime();
        this.mTimeChangeReceiver.register();
        this.flagRigister = true;
        String str = getIntent().getAction();
        this.mScreenView.refresh();
        if (str != null)
            this.mScreenView.postDelayed(new Runnable()
            {
                public void run()
                {
                    ActivityScreen.this.mScreenView.mDotSwitchView.play();
                }
            }
                    , 500L);*/
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent)
    {
        //this.mScreenView.onTouch(this.mScreenView, paramMotionEvent);
        return super.onTouchEvent(paramMotionEvent);
    }

    protected void onUserLeaveHint()
    {
        //this.log.d("onUserLeaveHint");
        super.onUserLeaveHint();
        finish();
    }

    class TimeChangeReceiver extends BroadcastReceiver
    {
        TimeChangeReceiver()
        {
        }

        public void onReceive(Context paramContext, Intent paramIntent)
        {
            String str = paramIntent.getAction();
            //ActivityScreen.this.log.d(str);
            ActivityScreen.this.changeTime();
        }

        public void register()
        {
            IntentFilter localIntentFilter = new IntentFilter();
            localIntentFilter.addAction("android.intent.action.TIME_SET");
            localIntentFilter.addAction("android.intent.action.TIME_TICK");
            ActivityScreen.this.registerReceiver(this, localIntentFilter);
        }

        public void unregister()
        {
            ActivityScreen.this.unregisterReceiver(this);
        }
    }
    private void changeTime()
    {
/*        long l = Utils.getNowTime();
        this.txtTime.setText(Utils.formatTime("HH:mm", l));
        String[] arrayOfString = Utils.formatTime("MM-dd", l).split("-");
        int i = Integer.valueOf(arrayOfString[0]).intValue();
        int j = Integer.valueOf(arrayOfString[1]).intValue();
        this.txtDate.setText(i + "月" + j + "日 " + Utils.getWeekDay(l));*/
    }
}