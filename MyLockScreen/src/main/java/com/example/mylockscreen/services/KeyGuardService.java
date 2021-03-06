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
import com.example.mylockscreen.activities.LockScreenActivity;
import com.example.mylockscreen.utils.Constants;
import com.example.mylockscreen.utils.Preferences;

/**
 * Created by haihong.xiahh on 13-7-3.
 */
public class KeyGuardService extends Service {
    public static final String ACTION_ON = "com.secretlisa.beidanci.SWITCH_ON";
    public static final String ACTION_STOP = "com.secretlisa.beidanci.SWITCH_OFF";
    public static final String TAG = "KeyGuardService";
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
                    KeyGuardService.this.phoneState = false;
                    return;
                case 2:
                    KeyGuardService.this.phoneState = true;
                    return;
                case 1:
            }
            KeyGuardService.this.phoneState = true;
        }
    };
    BroadcastReceiver mMasterResetReciever = new BroadcastReceiver()
    {
        public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
        {
            String action = paramAnonymousIntent.getAction();
            Log.d(TAG, Constants.TAG + "service receive " + action);
            if ((action.equals("android.intent.action.SCREEN_OFF")) && (Preferences.getUserPrefBoolean(paramAnonymousContext, "app_on", true)))
            {
                Log.d(TAG, Constants.TAG + "service start activity");
                //KeyGuardService.this.log.i("android.intent.action.SCREEN_OFF");
                //if ((Utils.checkDbExist(paramAnonymousContext)) && (!KeyGuardService.this.phoneState))
                if (true)
                {
                    //KeyGuardService.this.keyguardLock.disableKeyguard();
                    Intent localIntent = new Intent(paramAnonymousContext, LockScreenActivity.class);
                    localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    KeyGuardService.this.startActivity(localIntent);
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
        Log.d(TAG, Constants.TAG + "service create");
        super.onCreate();
        //MessageManager.getInstance().initialize(getApplicationContext());
        this.keyguardLock = ((KeyguardManager)getSystemService("keyguard")).newKeyguardLock("com.secretlisa.beidanci");
        //this.log.i("======OnCreate=====");
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        localIntentFilter.addAction("android.intent.action.SCREEN_ON");
        localIntentFilter.addAction("android.intent.action.PACKAGE_RESTARTED");
        localIntentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(this.mMasterResetReciever, localIntentFilter);
        ((TelephonyManager)getSystemService("phone")).listen(this.listener, 32);
    }

    public void onDestroy()
    {
        super.onDestroy();
        //this.log.i("======onDestroy=====");
        Log.d(TAG, Constants.TAG + "service destroy");
        unregisterReceiver(this.mMasterResetReciever);

    }

    public void onStart(Intent paramIntent, int paramInt)
    {
        super.onStart(paramIntent, paramInt);
        Log.d(TAG, Constants.TAG + "service start");
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
