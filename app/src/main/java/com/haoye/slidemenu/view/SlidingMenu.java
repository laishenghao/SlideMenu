package com.haoye.slidemenu.view;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * Created by Haoye on 2016/4/17.
 * Copyright © 2016 Haoye All Rights Reserved
 */
public class SlidingMenu extends HorizontalScrollView {
    private LinearLayout mWrapper;
    private ViewGroup    mMenu;
    private ViewGroup    mContent;
    private int          mScreenWidth;
    private int          mMenuRightPadding = 80;//dp
    private int          mMenuWidth;
    private boolean      hasMeasured = false;
    private boolean      hasOpened   = false;

    /**
     * 未使用自定义属性时调用
     * @param context
     * @param attrs
     */
    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
//        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics metrics = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(metrics);
//        mScreenWidth = metrics.widthPixels;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mScreenWidth = metrics.widthPixels;
        mMenuWidth = mScreenWidth * 2 / 3; //mScreenWidth - mMenuRightPadding;
        mMenuRightPadding = (int)(metrics.density * 80);
    }

    /**
     * 设置自己及子控件的宽和高
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!hasMeasured) {
            mWrapper = (LinearLayout)getChildAt(0);
            mMenu    = (ViewGroup)mWrapper.getChildAt(0);
            mContent = (ViewGroup)mWrapper.getChildAt(1);
            mMenu.getLayoutParams().width = mMenuWidth;
            mContent.getLayoutParams().width = mScreenWidth;
            hasMeasured = true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //---通过设置偏移量，将menu隐藏
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (changed) {
            this.scrollTo(mMenuWidth, 0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
        case MotionEvent.ACTION_UP:
            // scrollX 是隐藏在左边的宽度
            if (getScrollX() >= mMenuWidth/2) {
                smoothScrollTo(mMenuWidth, 0);
                hasOpened = false;
            }
            else {
                smoothScrollTo(0, 0);
                hasOpened = true;
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float scale = l * 1.0f / mMenuWidth;// 1 ~ 0
        //---调用属性动画，设置TranslationX
        mMenu.setTranslationX(l);//
        if (scale > 0.8f) {
            mContent.setScaleX(scale);
            mContent.setScaleY(scale);
        }
        if (scale > 0.5f) {
            mMenu.setScaleX(1.5f - scale);
            mMenu.setPivotX(0);
        }
    }

    public void openMenu() {
        if (hasOpened){
            return;
        }
        smoothScrollTo(0, 0);
        hasOpened = true;
    }

    public void closeMenu() {
        if (!hasOpened) {
            return;
        }
        smoothScrollTo(mMenuWidth, 0);
        hasOpened = false;
    }

    public void toggle() {
        if (hasOpened) {
            closeMenu();
        }
        else {
            openMenu();
        }
    }
}
