package com.example.mylockscreen.module;

/**
 * Created by haihong.xiahh on 13-7-9.
 */
public class SmsItem {

    String mAddress;
    String mDate;
    String mBody;
    public SmsItem(){
        mAddress = "";
        mDate = "";
        mBody = "";
    }

    public SmsItem(String address, String date, String body){
        mAddress = address;
        mDate = date;
        mBody = body;
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
