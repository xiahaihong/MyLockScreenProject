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
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.mylockscreen.R;
import com.example.mylockscreen.adapters.LockScreenPageAdapter;
import com.example.mylockscreen.widgets.ChannelHorizontalScrollView;
import com.example.mylockscreen.widgets.SliderRelativeLayout;

import java.util.ArrayList;

public class LockScreenActivity extends Activity
{
    private ViewPager mViewPager;
    private ChannelHorizontalScrollView mScrollView;
    private ImageView mScrollLeft,mScrollRight;
    private LinearLayout mLinearLayout;
    private ViewGroup mTabBarLayout;
    private ArrayList<View> mViewPagerViews;
    private ArrayList<String> mViewData;
    private LockScreenPageAdapter mAdapter;

    private final String TAG = "LockScreenActivity";
    TimeChangeReceiver mTimeChangeReceiver;
    public static int MSG_LOCK_SUCESS = 1;
    private SliderRelativeLayout sliderLayout = null;

    private ImageView imgView_getup_arrow; // 动画图片
    private AnimationDrawable animArrowDrawable = null;
    private final static int PADDING = 30;

    private int mPreSelectItem = 0;
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
        setContentView(R.layout.lock_screen_activity);
        initViews();
        this.mTimeChangeReceiver = new TimeChangeReceiver();
        sliderLayout.setMainHandler(mHandler);


        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mScrollView=(ChannelHorizontalScrollView)findViewById(R.id.h_scroll_view);
        mScrollLeft = (ImageView) findViewById(R.id.scroll_left);
        mScrollRight = (ImageView) findViewById(R.id.scroll_right);
        mTabBarLayout = (ViewGroup) findViewById(R.id.tab_bar_layout);
        addViewPagerView();
    }

    void addViewPagerView(){
        mViewPagerViews = new ArrayList<View>();
        mViewData = new ArrayList<String>();
        mViewData.add("1");
        mViewData.add("2");
        mViewData.add("3");
        for (int i = 0; i < mViewData.size(); i++){
            View v = getLayoutInflater().inflate(R.layout.item_view, null);
            mViewPager.addView(v);
            mViewPagerViews.add(v);
        }
        initTitleTab();
        setViewPagerAdapter();
    }
    private void initTitleTab() {
        if (mLinearLayout != null) {
            mLinearLayout.removeAllViews();
        }else{
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT);
            mLinearLayout=new LinearLayout(this);
            mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            mLinearLayout.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
            mLinearLayout.setLayoutParams(params);
            mScrollView.addView(mLinearLayout);

            mScrollView.setOnScrollListener(new ChannelHorizontalScrollView.OnChannelScrollListener() {
                @Override
                public void onLeft() {
                    mScrollLeft.setVisibility(View.GONE);
                    mScrollRight.setVisibility(View.VISIBLE);
                }
                @Override
                public void onRight() {
                    mScrollLeft.setVisibility(View.VISIBLE);
                    mScrollRight.setVisibility(View.GONE);
                }
                @Override
                public void onScroll() {
                    mScrollLeft.setVisibility(View.VISIBLE);
                    mScrollRight.setVisibility(View.VISIBLE);
                }
            });
        }
        for (int i = 0; i < mViewData.size(); i++) {
            String title = mViewData.get(i);
            addCategoryView(i, title);
        }
    }
    private void setViewPagerAdapter() {
        mAdapter = new LockScreenPageAdapter(this, mViewPagerViews);
        mViewPager.setAdapter(mAdapter);
        moveTitleLabel(0);
        mViewPager.setCurrentItem(0);
        mViewPager.setOnPageChangeListener(new ItemOnPageChangeListener());
    }

    private class ItemOnPageChangeListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageSelected(int i) {
            moveTitleLabel(i);
        }

        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    private void moveTitleLabel(int position) {
        int mScreenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int mScreenHeight = getWindowManager().getDefaultDisplay().getHeight();

        int visiableWidth = 0;

        int scrollViewWidth = 0;

        mLinearLayout.measure(mLinearLayout.getMeasuredWidth(), -1);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                mLinearLayout.getMeasuredWidth(), -1);
        params.gravity = Gravity.CENTER_VERTICAL;
        mLinearLayout.setLayoutParams(params);
        for (int i = 0; i < mLinearLayout.getChildCount(); i++) {
            TextView itemView = (TextView) mLinearLayout.getChildAt(i);
            int width = itemView.getMeasuredWidth();
            if (i < position) {
                visiableWidth += width;
            }
            scrollViewWidth += width;

            if (i == mLinearLayout.getChildCount() - 1) {
                break;
            }
            if (position != i) {
                itemView.setTextColor(getResources().getColor(
                        R.color.channel_news_title_no_press));
            } else {
                itemView.setTextColor(getResources().getColor(
                        R.color.channel_news_title_press));
            }
        }

        int titleWidth = mLinearLayout.getChildAt(position).getMeasuredWidth();
        int nextTitleWidth = 0;
        if (position > 0) {

            nextTitleWidth = position == mLinearLayout.getChildCount() - 1 ? 0
                    : mLinearLayout.getChildAt(position - 1).getMeasuredWidth();
        }
        final int move = visiableWidth - (mScreenWidth - titleWidth) / 2;
        if (mPreSelectItem < position) {
            if ((visiableWidth + titleWidth + nextTitleWidth) >= (mScreenWidth / 2)) {
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        mScrollView.setScrollX(move);
                    }
                });

            }
        } else {
            if ((scrollViewWidth - visiableWidth) >= (mScreenWidth / 2)) {
                mScrollView.setScrollX(move);
            }
        }
        mPreSelectItem = position;
    }


    private void addCategoryView(int i, String title) {
        TextView titleItem = new TextView(this);
        titleItem.setLayoutParams(new LinearLayout.LayoutParams(
                -1, -1));
        titleItem.setText(title);
        titleItem.setTextSize(18);
        titleItem.setTag(mViewData.get(i));

        if (i == 0) {
            titleItem.setTextColor(getResources().getColor(
                    R.color.channel_news_title_press));
        } else {
            titleItem.setTextColor(getResources().getColor(
                    R.color.channel_news_title_no_press));
        }
        titleItem.setPadding(PADDING, 0, PADDING, 0);
        titleItem.setGravity(Gravity.CENTER);
        titleItem.setOnClickListener(new ItemTitleOnClickListener());
        mLinearLayout.addView(titleItem);
    }

    private class ItemTitleOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
                if (null != v.getTag()) {// tab中的分类
                    for (int i = 0; i < mLinearLayout.getChildCount(); i++) {
                        TextView child = (TextView) mLinearLayout.getChildAt(i);
                        if (child == v) {
                            child.setTextColor(getResources().getColor(
                                    R.color.channel_news_title_press));
                            mViewPager.setCurrentItem(i);
                        } else {
                            child.setTextColor(getResources().getColor(
                                    R.color.channel_news_title_no_press));
                        }
                    }
                }


        }
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