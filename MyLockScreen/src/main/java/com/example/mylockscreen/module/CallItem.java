package com.example.mylockscreen.module;

/**
 * Created by haihong.xiahh on 13-7-9.
 */
public class CallItem {

    String mAddress;
    String mDate;

    public CallItem(){
        mAddress = "";
        mDate = "";
    }

    public CallItem(String address, String date){
        mAddress = address;
        mDate = date;
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

}
