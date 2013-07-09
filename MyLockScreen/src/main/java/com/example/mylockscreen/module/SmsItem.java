package com.example.mylockscreen.module;

/**
 * Created by haihong.xiahh on 13-7-9.
 */
public class SmsItem {
    String mThreadId;
    String mAddress;
    String mDate;
    String mBody;

    public String getmThreadId() {
        return mThreadId;
    }

    public void setmThreadId(String mThreadId) {
        this.mThreadId = mThreadId;
    }

    public SmsItem(){
        mAddress = "";
        mDate = "";
        mBody = "";
    }

    public SmsItem(String address, String date, String body, String threadId){
        mAddress = address;
        mDate = date;
        mBody = body;
        mThreadId = threadId;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmBody() {
        return mBody;
    }

    public void setmBody(String mBody) {
        this.mBody = mBody;
    }

}
