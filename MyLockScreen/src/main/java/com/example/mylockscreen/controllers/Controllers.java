package com.example.mylockscreen.controllers;

import android.app.Application;
import android.content.Context;
import com.example.mylockscreen.module.FileService;

/**
 * Created by haihong.xiahh on 13-7-5.
 */
public class Controllers {
    private Context mContext;
    private Application mApplication;
    private static final class ControllersHolder {
        private final static Controllers INSTANCE = new Controllers();

        private ControllersHolder() {
        }
    }

    public static Controllers getInstance() {
        return ControllersHolder.INSTANCE;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void setApplication(Application mApplication) {
        this.mApplication = mApplication;
    }

    public Application getApplication() {
        return mApplication;
    }
    public FileService getFileService() {
        return new FileService();
    }
}
