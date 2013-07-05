package com.example.mylockscreen.services;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.example.mylockscreen.activities.ActivityScreen;
import com.example.mylockscreen.utils.Preferences;

/**
 * Created by haihong.xiahh on 13-7-3.
 */
public class ServiceScreen extends Service {
    public static final String ACTION_ON = "com.secretlisa.beidanci.SWITCH_ON";
    public static final String ACTION_STOP = "com.secretlisa.beidanci.SWITCH_OFF";
    public static final String TAG = "ServiceScreen";
    public KeyguardManager.KeyguardLock keyguardLock;
    PhoneStateListener listener = new PhoneStateListener()
    {
        public void onCallStateChanged(int paramAnonymousInt, String paramAnonymousString)
        {
            super.onCallStateChanged(paramAnonymousInt, paramAnonymousString);
            switch (paramAnonymousInt)
            {
                default:
                    return;
                case 0:
                    ServiceScreen.this.phoneState = false;
                    return;
                case 2:
                    ServiceScreen.this.phoneState = true;
                    return;
                case 1:
            }
            ServiceScreen.this.phoneState = true;
        }
    };
    BroadcastReceiver mMasterResetReciever = new BroadcastReceiver()
    {
        public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
        {
            Log.d(TAG, "xhh:service receive");
            if ((paramAnonymousIntent.getAction().equals("android.intent.action.SCREEN_OFF")) && (Preferences.getUserPrefBoolean(paramAnonymousContext, "app_on", true)))
            {
                Log.d(TAG, "xhh service start activity");
                //ServiceScreen.this.log.i("android.intent.action.SCREEN_OFF");
                //if ((Utils.checkDbExist(paramAnonymousContext)) && (!ServiceScreen.this.phoneState))
                if (true)
                {
                    //ServiceScreen.this.keyguardLock.disableKeyguard();
                    Intent localIntent = new Intent(paramAnonymousContext, ActivityScreen.class);
                    localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ServiceScreen.this.startActivity(localIntent);
                }
            }
        }
    };
    public boolean phoneState = false;

    public IBinder onBind(Intent paramIntent)
    {
        return null;
    }

    public void onCreate()
    {
        Log.d(TAG, "xhh: service create");
        super.onCreate();
        //MessageManager.getInstance().initialize(getApplicationContext());
        this.keyguardLock = ((KeyguardManager)getSystemService("keyguard")).newKeyguardLock("com.secretlisa.beidanci");
        //this.log.i("======OnCreate=====");
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        localIntentFilter.addAction("android.intent.action.SCREEN_ON");
        localIntentFilter.addAction("android.intent.action.PACKAGE_RESTARTED");
        localIntentFilter.setPriority(1000);
        registerReceiver(this.mMasterResetReciever, localIntentFilter);
        ((TelephonyManager)getSystemService("phone")).listen(this.listener, 32);
    }

    public void onDestroy()
    {
        super.onDestroy();
        //this.log.i("======onDestroy=====");
        Log.d(TAG, "xhh: service destroy");
        unregisterReceiver(this.mMasterResetReciever);

    }

    public void onStart(Intent paramIntent, int paramInt)
    {
        super.onStart(paramIntent, paramInt);
        Log.d(TAG, "xhh: service start");
        //Utils.checkDbExist(this);
        if (paramIntent != null)
            switchApp(paramIntent.getStringExtra("action"));
    }

    public void switchApp(String paramString)
    {
        if (paramString == null)
            return;
        do
        {
            //return;
            if (paramString.equals("com.secretlisa.beidanci.SWITCH_ON"))
            {
                Preferences.setUserPrefBoolean(this, "app_on", true);
                //this.keyguardLock.disableKeyguard();
                return;
            }
        }while (!paramString.equals("com.secretlisa.beidanci.SWITCH_OFF"));
        Preferences.setUserPrefBoolean(this, "app_on", false);
        this.keyguardLock.reenableKeyguard();
    }
}
