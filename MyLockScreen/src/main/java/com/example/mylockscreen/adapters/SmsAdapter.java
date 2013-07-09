package com.example.mylockscreen.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.mylockscreen.R;
import com.example.mylockscreen.module.SmsHolder;
import com.example.mylockscreen.module.SmsItem;
import com.example.mylockscreen.utils.Constants;

import java.util.ArrayList;

/**
 * Created by haihong.xiahh on 13-7-9.
 */
public class SmsAdapter extends BaseAdapter{
    Context mContext;
    ArrayList<SmsItem> mSmsList;
    LayoutInflater mInflater;
    static final String TAG = "SmsAdapter";

    public SmsAdapter(Context context, ArrayList<SmsItem> smsItemArrayList){
        mContext = context;
        mSmsList = smsItemArrayList;
        mInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<SmsItem> itemArrayList){
        mSmsList = itemArrayList;
    }

    @Override
    public int getCount() {
        return null == mSmsList ? 0 : mSmsList.size();
    }

    @Override
    public Object getItem(int i) {
        return null == mSmsList ? null : mSmsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Log.d(TAG, Constants.TAG + "position " + i);
        SmsHolder holder;
        if (view == null){
            Log.d(TAG, Constants.TAG + "create view first time");
            holder = new SmsHolder();
            view = mInflater.inflate(R.layout.item_sms, null);
            holder.mAddressView = (TextView) view.findViewById(R.id.sms_address);
            holder.mDateView = (TextView) view.findViewById(R.id.sms_date);
            holder.mBodyView = (TextView) view.findViewById(R.id.sms_body);
            holder.mFromLayout = (RelativeLayout) view.findViewById(R.id.sms_from_layout);
            view.setTag(holder);
        } else {
            Log.d(TAG, Constants.TAG + "view already used");
            holder = (SmsHolder)view.getTag();
        }
        holder.mAddressView.setText(mSmsList.get(i).getmAddress());
        holder.mDateView.setText(mSmsList.get(i).getmDate());
        holder.mBodyView.setText(mSmsList.get(i).getmBody());
        bindViewClickListener(holder);
        return view;
    }

    public void bindViewClickListener(SmsHolder holder){
        SmsClickListener listener = new SmsClickListener();
        holder.mBodyView.setOnClickListener(listener);
        holder.mAddressView.setOnClickListener(listener);
        holder.mDateView.setOnClickListener(listener);
        holder.mFromLayout.setOnClickListener(listener);
    }

    public class SmsClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setData(Uri.parse("content://mms-sms"));
            mContext.startActivity(intent);
        }
    }
}

