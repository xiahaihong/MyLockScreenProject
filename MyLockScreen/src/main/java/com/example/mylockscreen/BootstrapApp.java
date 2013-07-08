package com.example.mylockscreen;

import android.app.Application;
import com.example.mylockscreen.controllers.Controllers;

/**
 * Created by haihong.xiahh on 13-7-5.
 */
public class BootstrapApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Controllers.getInstance().setContext(getBaseContext());
        Controllers.getInstance().setApplication(this);
    }
}
