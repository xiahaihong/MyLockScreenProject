package com.example.mylockscreen.activities;

/**
 * Created by haihong.xiahh on 13-7-3.
 */
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import com.example.mylockscreen.R;
import com.example.mylockscreen.widgets.SliderRelativeLayout;

public class LockScreenActivity extends Activity
{
    private final String TAG = "LockScreenActivity";
    TimeChangeReceiver mTimeChangeReceiver;
    public static int MSG_LOCK_SUCESS = 1;
    private SliderRelativeLayout sliderLayout = null;

    private ImageView imgView_getup_arrow; // 动画图片
    private AnimationDrawable animArrowDrawable = null;

    private Context mContext = null ;
    private void initViews(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        sliderLayout = (SliderRelativeLayout)findViewById(R.id.slider_layout);
        imgView_getup_arrow = (ImageView)findViewById(R.id.getup_arrow);
        animArrowDrawable = (AnimationDrawable) imgView_getup_arrow.getBackground() ;
    }

    private Handler mHandler =new Handler (){

        public void handleMessage(Message msg){

            Log.i(TAG, "handleMessage :  #### " );

            if(MSG_LOCK_SUCESS == msg.what)
                finish(); // 锁屏成功时，结束我们的Activity界面
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = LockScreenActivity.this;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Log.d(TAG, "xhh: activity screen lock view create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        initViews();
        this.mTimeChangeReceiver = new TimeChangeReceiver();
        sliderLayout.setMainHandler(mHandler);
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

    private Runnable AnimationDrawableTask = new Runnable(){

        public void run(){
            animArrowDrawable.start();
            mHandler.postDelayed(AnimationDrawableTask, 300);
        }
    };
    protected void onPause()
    {
        Log.d(TAG, "xhh: activity screen lock view pause");
        super.onPause();
        animArrowDrawable.stop();
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
        mHandler.postDelayed(AnimationDrawableTask, 300);
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
                    LockScreenActivity.this.mScreenView.mDotSwitchView.play();
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
            //LockScreenActivity.this.log.d(str);
            LockScreenActivity.this.changeTime();
        }

        public void register()
        {
            IntentFilter localIntentFilter = new IntentFilter();
            localIntentFilter.addAction("android.intent.action.TIME_SET");
            localIntentFilter.addAction("android.intent.action.TIME_TICK");
            LockScreenActivity.this.registerReceiver(this, localIntentFilter);
        }

        public void unregister()
        {
            LockScreenActivity.this.unregisterReceiver(this);
        }
    }
    private void changeTime()
    {
    }

/*
    public void onAttachedToWindow() {
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        super.onAttachedToWindow();
    }


    public boolean onKeyDown(int keyCode ,KeyEvent event){

        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK)
            return true ;
        else
            return super.onKeyDown(keyCode, event);

    }*/
}