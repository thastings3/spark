package com.gatech.spark.helper;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * Date: 3/25/13
 * Time: 8:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomViewPager extends ViewPager{

    public CustomViewPager(Context context){
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev){
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev){
        return false;
    }
}