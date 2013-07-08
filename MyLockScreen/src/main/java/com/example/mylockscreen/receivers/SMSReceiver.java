package com.example.mylockscreen.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
import com.example.mylockscreen.controllers.Controllers;
import com.example.mylockscreen.utils.Constants;

import java.util.ArrayList;

/**
 * Created by haihong.xiahh on 13-7-5.
 */
public class SMSReceiver extends BroadcastReceiver {
    static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SMSReceiver";
    private static final String SEPARATOR = "======= SMS_REPARATOR_FOR_MY_LOCK_SCREEN =======\n";
    public static final int NOTIFICATION_ID_RECEIVED = 0x1221;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        ArrayList<String> list = new ArrayList<String>();
        String smsStr = "";
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d(TAG, Constants.TAG + "boot received");
            // Intent in = new Intent(context, SMSNotifyActivity.class); //
            // 这是你的activity
            // in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // context.startActivity(in);
        }
        if (intent.getAction().equals(SMSReceiver.ACTION)) {
            Log.d(TAG, Constants.TAG + "sms received");
            Bundle bundle = intent.getExtras();
            StringBuilder sb = new StringBuilder();
            if (bundle != null) {
                Object messages[] = (Object[]) bundle.get("pdus");
                SmsMessage[] smsMessage = new SmsMessage[messages.length];

                for (int n = 0; n < messages.length; n++) {
                    smsMessage[n] = SmsMessage
                            .createFromPdu((byte[]) messages[n]);
                    sb.append("From:");
                    sb.append(smsMessage[n].getDisplayOriginatingAddress());
                    sb.append("\n");
                    sb.append(smsMessage[n].getDisplayMessageBody());
                    list.add(sb.toString());
                    smsStr += sb + SEPARATOR;
                }
            }
            Log.d(SMSReceiver.TAG, Constants.TAG + "[SMSApp] onReceiveIntent0: " + sb);
            //abortBroadcast();
            Controllers.getInstance().getFileService().writeStorage("sms", smsStr);

/*            Intent in = new Intent(context, SMSNotifyActivity.class);
            Bundle bundle2 = new Bundle();
            bundle2.putStringArrayList("message", list);
            in.putExtras(bundle2);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(in);*/
            Log.d(SMSReceiver.TAG, Constants.TAG + "[SMSApp] onReceiveIntent0over: ");
        }
    }
}
