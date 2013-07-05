package com.example.mylockscreen.widgets;

/**
 * Created by haihong.xiahh on 13-7-5.
 */

import android.widget.HorizontalScrollView;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

public class ChannelHorizontalScrollView extends HorizontalScrollView {

    private OnChannelScrollListener onScrollListener;
    private Handler mHandler;
    private View mView;
    private final static int SCROLL_LISTENER = 1;
    private int mScrollX;

    public ChannelHorizontalScrollView(Context context, AttributeSet attrs,
                                       int defStyle) {
        super(context, attrs, defStyle);

    }

    public void setOnScrollListener(OnChannelScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;

    }

    public ChannelHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == SCROLL_LISTENER) {

                    if (mView.getMeasuredWidth()-25 < mScrollX + getWidth()
                            && mScrollX != 0) {
                        onScrollListener.onRight();
                    } else if (mScrollX < 25) {
                        onScrollListener.onLeft();
                    } else{
                        onScrollListener.onScroll();
                    }

                }

            }
        };

        setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                mView = getChildAt(0);
                if (MotionEvent.ACTION_DOWN != event.getAction()) {
                    mScrollX = getScrollX();
                    // 惯性滑动
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int tempScrollX = getScrollX();
                            if(mScrollX!=tempScrollX);
                            mScrollX=tempScrollX;
                            mHandler.sendEmptyMessage(SCROLL_LISTENER);
                        }
                    }, 500);
                }
                return false;
            }
        });
    }

    @Override
    public void setScrollX(int value) {
        super.setScrollX(value);

        if(value<=0)
            onScrollListener.onLeft();
        else if(getChildAt(0).getMeasuredWidth()<=value+getWidth())
            onScrollListener.onRight();
        else
            onScrollListener.onScroll();

    }

    public interface OnChannelScrollListener {

        public void onLeft();

        public void onRight();

        public void onScroll();

    }

}
