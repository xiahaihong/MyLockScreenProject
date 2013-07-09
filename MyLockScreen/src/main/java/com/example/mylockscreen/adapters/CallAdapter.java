package com.example.mylockscreen.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.mylockscreen.R;
import com.example.mylockscreen.module.CallHolder;
import com.example.mylockscreen.module.CallItem;
import com.example.mylockscreen.module.SmsHolder;
import com.example.mylockscreen.module.SmsItem;
import com.example.mylockscreen.utils.Constants;

import java.util.ArrayList;

/**
 * Created by haihong.xiahh on 13-7-9.
 */
public class CallAdapter extends BaseAdapter{
    Context mContext;
    ArrayList<CallItem> mPhoneList;
    LayoutInflater mInflater;
    static final String TAG = "CallAdapter";

    public CallAdapter(Context context, ArrayList<CallItem> smsItemArrayList){
        mContext = context;
        mPhoneList = smsItemArrayList;
        mInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<CallItem> itemArrayList){
        mPhoneList = itemArrayList;
    }

    @Override
    public int getCount() {
        return null == mPhoneList ? 0 : mPhoneList.size();
    }

    @Override
    public Object getItem(int i) {
        return null == mPhoneList ? null : mPhoneList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Log.d(TAG, Constants.TAG + "position " + i);
        CallHolder holder;
        if (view == null){
            Log.d(TAG, Constants.TAG + "create view first time");
            holder = new CallHolder();
            view = mInflater.inflate(R.layout.item_call, null);
            holder.mAddressView = (TextView) view.findViewById(R.id.call_address);
            holder.mDateView = (TextView) view.findViewById(R.id.call_date);
            holder.mFromLayout = (RelativeLayout) view.findViewById(R.id.call_from_layout);
            view.setTag(holder);
        } else {
            Log.d(TAG, Constants.TAG + "view already used");
            holder = (CallHolder)view.getTag();
        }
        holder.mAddressView.setText(mPhoneList.get(i).getmAddress());
        holder.mDateView.setText(mPhoneList.get(i).getmDate());
        bindViewClickListener(holder);
        return view;
    }

    public void bindViewClickListener(CallHolder holder){
        SmsClickListener listener = new SmsClickListener();
        holder.mAddressView.setOnClickListener(listener);
        holder.mDateView.setOnClickListener(listener);
        holder.mFromLayout.setOnClickListener(listener);
    }

    public class SmsClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL_BUTTON);
            mContext.startActivity(intent);
        }
    }
}

