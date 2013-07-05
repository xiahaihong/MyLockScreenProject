package com.example.mylockscreen.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haihong.xiahh on 13-7-5.
 */
public class LockScreenPageAdapter extends PagerAdapter {

    private List<View> mViewList;

    public LockScreenPageAdapter(List<View> list){
        if (list != null){
            mViewList = list;
        } else {
            mViewList = new ArrayList<View>();
        }
    }


    // 销毁position位置的界面
    @Override
    public void destroyItem(View view, int position, Object obj) {
        ((ViewPager) view).removeView(mViewList.get(position));
    }

    // 获取当前窗体界面数
    @Override
    public int getCount() {
        return mViewList.size();
    }

    // 初始化position位置的界面
    @Override
    public Object instantiateItem(View view, int position) {
        View tab = mViewList.get(position);
        ((ViewPager) view).addView(tab, 0);
        return tab;
    }

    // 判断View和对象是否为同一个View
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {

    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {

    }

    @Override
    public void finishUpdate(View arg0) {

    }

}
