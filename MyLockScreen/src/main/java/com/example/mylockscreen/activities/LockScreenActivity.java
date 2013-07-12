package com.example.mylockscreen.activities;

/**
 * Created by haihong.xiahh on 13-7-3.
 */
import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.example.mylockscreen.R;
import com.example.mylockscreen.adapters.CallAdapter;
import com.example.mylockscreen.adapters.LockScreenPageAdapter;
import com.example.mylockscreen.adapters.SmsAdapter;
import com.example.mylockscreen.controllers.Controllers;
import com.example.mylockscreen.module.CallItem;
import com.example.mylockscreen.module.SmsItem;
import com.example.mylockscreen.utils.Constants;
import com.example.mylockscreen.widgets.ChannelHorizontalScrollView;
import com.example.mylockscreen.widgets.SliderRelativeLayout;
import com.example.mylockscreen.widgets.WidgetLayout;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LockScreenActivity extends Activity implements View.OnClickListener
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
    private String SEPARATOR = "==== separator_for_lock_screen ===== \n";
    ArrayList<SmsItem> mSmsList = new ArrayList<SmsItem>();
    ArrayList<CallItem> mCallList = new ArrayList<CallItem>();
    SmsAdapter mSmsAdapter;
    CallAdapter mCallAdapter;


    private AppWidgetHost mAppWidgetHost;
    private AppWidgetManager mAppWidgetManager;
    private WidgetLayout mWidgetLayout;
    private static final int APPWIDGET_HOST_ID = 0x100;
    private static final int REQUEST_PICK_APPWIDGET = 2;
    private static final int REQUEST_CREATE_APPWIDGET = 1;
    private static final String EXTRA_CUSTOM_WIDGET = "custom_widget";

    private LinearLayout mTimeLayout;
    private TextView mTimeView;
    private TextView mDayView;

    private boolean flagRigister = false;

    private void initViews(){
        // add flags to show before keyguard
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        // layout init for slider
        sliderLayout = (SliderRelativeLayout)findViewById(R.id.slider_layout);
        imgView_getup_arrow = (ImageView)findViewById(R.id.getup_arrow);
        animArrowDrawable = (AnimationDrawable) imgView_getup_arrow.getBackground() ;
    }

    private Handler mHandler =new Handler (){

        public void handleMessage(Message msg){

            Log.d(TAG, Constants.TAG + "handleMessage :  #### " );
            if(MSG_LOCK_SUCESS == msg.what)
                Log.d(TAG, Constants.TAG + "finish");
                finish(); // 锁屏成功时，结束我们的Activity界面
        }
    };

    private String getDayInfo(){
        String dayInfo;
        Time time = new Time();
        time.setToNow();
        int year = time.year;
        int month = time.month + 1;
        int day = time.monthDay;
        int weekday = time.weekDay;
        dayInfo = String.valueOf(year) + "年" + String.valueOf(month) + "月" + String.valueOf(day) + "日 "
                + "星期" + String.valueOf(weekday);
        return dayInfo;
    }

    private String getTimeInfo(){
        String timeInfo;
        Time time = new Time();
        time.setToNow();
        int hour = time.hour;
        int minute = time.minute;
        timeInfo = String.valueOf(hour) + ":" + String.valueOf(minute);
        return timeInfo;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = LockScreenActivity.this;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER);
        Log.d(TAG, Constants.TAG + "activity screen lock view create");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_screen_activity);

        initViews();

        this.mTimeChangeReceiver = new TimeChangeReceiver();

        mTimeLayout = (LinearLayout) findViewById(R.id.time_layout);
        mTimeView = (TextView) findViewById(R.id.time_textview);
        mDayView = (TextView) findViewById(R.id.day_textview);
        String timeInfo = getTimeInfo();
        String dayInfo = getDayInfo();
        mTimeView.setText(timeInfo);
        mDayView.setText(dayInfo);

        sliderLayout.setMainHandler(mHandler);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mScrollView=(ChannelHorizontalScrollView)findViewById(R.id.h_scroll_view);
        mScrollLeft = (ImageView) findViewById(R.id.scroll_left);
        mScrollRight = (ImageView) findViewById(R.id.scroll_right);
        mTabBarLayout = (ViewGroup) findViewById(R.id.tab_bar_layout);

        addViewPagerView();
    }

    String getSMSFromLocal(){
        String sms = Controllers.getInstance().getFileService().readStorage("sms");
        return sms;
    }
    private ArrayList<SmsItem> getNewSmsCount() {
        ArrayList<SmsItem> smsList = new ArrayList<SmsItem>();
        String result = "";
        Cursor cur = getContentResolver().query(Uri.parse("content://sms"), null,
                "type = 1 and read = 0", null, null);

        try{
            for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                String address = cur.getString(cur.getColumnIndex("address"));
                String person = cur.getString(cur.getColumnIndex("person"));
                String date = cur.getString(cur.getColumnIndex("date"));
                String date_sent = cur.getString(cur.getColumnIndex("date_sent"));
                String subject = cur.getString(cur.getColumnIndex("subject"));
                String body = cur.getString(cur.getColumnIndex("body"));
                String seen = cur.getString(cur.getColumnIndex("seen"));
                String threadId = cur.getString(cur.getColumnIndex("thread_id"));
                if (person != null && ! "".equals(person)){
                    address = person;
                }
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String dateFormatted = formatter.format(Long.parseLong(date));
                date = dateFormatted;
                smsList.add(new SmsItem(address, date, body, threadId));
                result += address + person + date + date_sent + subject + body + seen + SEPARATOR;
            }
        }finally{
            if(null != cur){
                cur.close();
            }
        }
        return smsList;
    }

    private ArrayList<CallItem> readMissCall() {
        String result = "";
        ArrayList<CallItem> callList = new ArrayList<CallItem>();
/*
        Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, new String[] {
                CallLog.Calls.TYPE
        }, " type=? and new=?", new String[] {
                CallLog.Calls.MISSED_TYPE + "", "1"
        }, "date desc");
*/

        Cursor cur = getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                " type=? ", new String[] {
                CallLog.Calls.MISSED_TYPE + ""
        }, "date desc");

        try{
            for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                String name = cur.getString(cur.getColumnIndex("name"));
                String number = cur.getString(cur.getColumnIndex("formatted_number"));
                String date = cur.getString(cur.getColumnIndex("date"));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = formatter.format(Long.parseLong(date));
                if (name != null && !"".equals(name)){
                    number = name;
                }
                callList.add(new CallItem(number, date));
                result += name + number + date + SEPARATOR;
            }
        }finally{
            if(null != cur){
                cur.close();
            }
        }
        //Log.d(TAG, Constants.TAG + result);
        return callList;
    }

    void  getSystemNotificationFromService(){
        String service = Context.NOTIFICATION_SERVICE;
        NotificationManager notificationManager = (NotificationManager) getSystemService(service);
    }

    String getSystemNotification(){
        String[] commands = {"/system/bin/dumpsys notification"};
        Process process = null;
        DataOutputStream dataOutputStream = null;
        List<String> lineList = new ArrayList<String>();
        try {
            process = Runtime.getRuntime().exec("sh");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            int length = commands.length;
            for (int i = 0; i < length; i++) {
                Log.d(TAG, "commands[" + i + "]:" + commands[i]);
                dataOutputStream.writeBytes(commands[i] + "\n");
            }
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();

            process.waitFor();
            //process = Runtime.getRuntime().exec(commands);

            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";

            final StringBuilder log = new StringBuilder();
            String separator = System.getProperty("line.separator");
            Pattern pattern = Pattern.compile("pkg=[^\\s]+");
            while ((line = reader.readLine()) != null) {
                if(line != null && line.trim().startsWith("NotificationRecord")){
                    Matcher matcher = pattern.matcher(line);
                    if(matcher.find()){
                        lineList.add(matcher.group());
                    }else{
                        Log.e(TAG, Constants.TAG + "what's this?!");
                    }
                }

                log.append(line);
                log.append(separator);
            }
            Log.d(TAG, Constants.TAG + "log:" + log.toString());

            int size = lineList.size();
            for (int i = 0; i < size; i++) {
                Log.d(TAG, Constants.TAG + "app:" + lineList.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, Constants.TAG + "copy fail", e);
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, Constants.TAG + "finish");
    return Arrays.toString(lineList.toArray());
    }


    void addViewPagerView(){
        int tabCount = 4;
        mViewPagerViews = new ArrayList<View>();
        mViewData = new ArrayList<String>();
        for (int i = 0; i < tabCount; i++){
            mViewData.add("Tab " + String.valueOf(i));
        }
        for (int i = 0; i < mViewData.size(); i++){
            View v = getLayoutInflater().inflate(R.layout.item_view, null);
            TextView textView = (TextView) v.findViewById(R.id.item_text);
            ListView listView = (ListView) v.findViewById(R.id.item_listview);
            String text = "Content " + i;

            if (i == 0){
                // for sms
                listView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
                mSmsList = getNewSmsCount();
                mSmsAdapter = new SmsAdapter(LockScreenActivity.this, mSmsList);
                listView.setAdapter(mSmsAdapter);
            } else if (i == 1){
                // for call
                listView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
                mCallList = readMissCall();
                mCallAdapter = new CallAdapter(LockScreenActivity.this, mCallList);
                listView.setAdapter(mCallAdapter);
            } else if (i == 2){
                // for system notification
                text = "sys notification : \n" + getSystemNotification();
                text += "\n\nrecent app:\n" + getAPPInfo();
            }

            if ("".equals(text)){
                text = "Content " + i;
            } else {
                v.setBackgroundColor(R.color.channel_news_title_no_press);
                textView.setTextSize(20);
            }

            if (i == mViewData.size() -1){
                // for add view
                v.setBackgroundResource(R.drawable.add);
                mAppWidgetManager = AppWidgetManager.getInstance(mContext);
                mAppWidgetHost = new AppWidgetHost(mContext, APPWIDGET_HOST_ID);
                mAppWidgetHost.startListening();
                v.setOnClickListener(this);
            } else {
                v.getBackground().setAlpha(0);
                textView.setText(text);
            }
            v.setTag(mViewData.get(i));
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
        mAdapter = new LockScreenPageAdapter(mViewPagerViews);
        mViewPager.setAdapter(mAdapter);
        moveTitleLabel(0);
        mViewPager.setCurrentItem(0);
        mViewPager.setOnPageChangeListener(new ItemOnPageChangeListener());
    }

    @Override
    public void onClick(View view) {
        Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetHost.allocateAppWidgetId());
        // start the pick activity
        this.startActivityForResult(pickIntent, REQUEST_PICK_APPWIDGET);
        Log.d(TAG, Constants.TAG + "add click");
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

/*            if (i == mLinearLayout.getChildCount() - 1) {
                break;
            }*/
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, Constants.TAG + "on activity result");
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PICK_APPWIDGET:
                    addAppWidget(data);
                    break;
                case REQUEST_CREATE_APPWIDGET:
                    completeAddAppWidget(data);
                    break;
            }
        } else if (requestCode == REQUEST_PICK_APPWIDGET
                && resultCode == RESULT_CANCELED && data != null) {
            int appWidgetId = data.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
            if (appWidgetId != -1) {
                mAppWidgetHost.deleteAppWidgetId(appWidgetId);
            }
        }
    }
    private void addAppWidget(Intent data) {
        Log.d(TAG, Constants.TAG + "add app widget");
        int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,-1);
        String customWidget = data.getStringExtra(EXTRA_CUSTOM_WIDGET);
        if ("search_widget".equals(customWidget)) {
            mAppWidgetHost.deleteAppWidgetId(appWidgetId);
        } else {
            AppWidgetProviderInfo appWidget = mAppWidgetManager
                    .getAppWidgetInfo(appWidgetId);

            Log.d("addAppWidget", "configure:" + appWidget.configure);
            if (appWidget.configure != null) {
                // 弹出配置界面
                Intent intent = new Intent(
                        AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
                intent.setComponent(appWidget.configure);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                        appWidgetId);

                startActivityForResult(intent, REQUEST_CREATE_APPWIDGET);
            } else {
                onActivityResult(REQUEST_CREATE_APPWIDGET, Activity.RESULT_OK,
                        data);
            }
        }
    }

    /**
     * 添加widget
     *
     * @param data
     */
    private void completeAddAppWidget(Intent data) {
        Log.d(TAG, Constants.TAG + "complete add app widget");
        Bundle extras = data.getExtras();
        int appWidgetId = extras
                .getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);

        AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager
                .getAppWidgetInfo(appWidgetId);

        View hostView = mAppWidgetHost.createView(this, appWidgetId,
                appWidgetInfo);
        mWidgetLayout = new WidgetLayout(mContext);
        mWidgetLayout.addInScreen(hostView, appWidgetInfo.minWidth,
                appWidgetInfo.minHeight);
        View v = (View) mWidgetLayout;

        // add view
        mViewPagerViews.add(v);
        int index = mViewPagerViews.size() -1 ;
        mViewData.add("Tab " + String.valueOf(index));
        addCategoryView(index, "Tab " + index);
        mAdapter.setmViewList(mViewPagerViews);
        mAdapter.notifyDataSetChanged();
    }


    private void addCategoryView(int i, String title) {
        TextView titleItem = new TextView(this);
        titleItem.setLayoutParams(new LinearLayout.LayoutParams(
                -1, -1));
        titleItem.setText(title);
        titleItem.setTextSize(18);
        titleItem.setTag(title);

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
        try {
            mAppWidgetHost.stopListening();
        } catch (NullPointerException ex) {
            Log.i(TAG, "problem while stopping AppWidgetHost during Launcher destruction", ex);
        }
        Log.d(TAG, Constants.TAG + "activity screen lock view destroy");
        super.onDestroy();
    }

/*
    protected void onNewIntent(Intent paramIntent)
    {
        super.onNewIntent(paramIntent);
        Log.d(TAG, Constants.TAG + "new intent");
    }
*/

    private Runnable AnimationDrawableTask = new Runnable(){

        public void run(){
            animArrowDrawable.start();
            mHandler.postDelayed(AnimationDrawableTask, 300);
        }
    };
    protected void onPause()
    {
        Log.d(TAG, Constants.TAG + "activity screen lock view pause");
        super.onPause();
        animArrowDrawable.stop();
        try
        {
            if (this.flagRigister)
            {
                this.mTimeChangeReceiver.unregister();
                this.flagRigister = false;
            }
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
        mSmsList = getNewSmsCount();
        mCallList = readMissCall();
        if (mSmsAdapter != null){
            mSmsAdapter.setData(mSmsList);
            mSmsAdapter.notifyDataSetChanged();
        }
        if (mCallAdapter != null){
            mCallAdapter.setData(mCallList);
            mCallAdapter.notifyDataSetChanged();
        }
        mHandler.postDelayed(AnimationDrawableTask, 300);
        changeTime();
        this.mTimeChangeReceiver.register();
        this.flagRigister = true;
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent)
    {
        //this.mScreenView.onTouch(this.mScreenView, paramMotionEvent);
        return super.onTouchEvent(paramMotionEvent);
    }

/*    protected void onUserLeaveHint()
    {
        //this.log.d("onUserLeaveHint");
        Log.d(TAG, Constants.TAG + "user leave finish");
        super.onUserLeaveHint();
        finish();
    }*/

    class TimeChangeReceiver extends BroadcastReceiver
    {
        TimeChangeReceiver()
        {
        }

        public void onReceive(Context paramContext, Intent paramIntent)
        {
            String str = paramIntent.getAction();
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
        String timeInfo = getTimeInfo();
        String dayInfo = getDayInfo();
        mDayView.setText(dayInfo);
        mTimeView.setText(timeInfo);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, Constants.TAG + "back pressed");
    }

    private String getAPPInfo(){
        String appInfo = "";
        final ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RecentTaskInfo> recentTasks = am.getRecentTasks(5, ActivityManager.RECENT_IGNORE_UNAVAILABLE);
        for (ActivityManager.RecentTaskInfo taskInfo : recentTasks){
            appInfo +=  taskInfo.baseIntent.toString() + "\n";
        }
        return appInfo;
    }
}