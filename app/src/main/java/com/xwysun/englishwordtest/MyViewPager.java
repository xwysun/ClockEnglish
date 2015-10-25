package com.xwysun.englishwordtest;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

/**
 * Created by xwysun on 2015/10/25.
 */
public class MyViewPager extends ViewPager {
    public MyViewPager(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean res = super.onInterceptTouchEvent(ev);
        float preX= (float) 1.0;
        if(ev.getAction() == MotionEvent.ACTION_DOWN) {
            preX = ev.getX();
        } else {
            if( Math.abs(ev.getX() - preX)> 4 ) {
                return true;
            } else {
                preX = ev.getX();
            }
        }
        return res;
    }

}
