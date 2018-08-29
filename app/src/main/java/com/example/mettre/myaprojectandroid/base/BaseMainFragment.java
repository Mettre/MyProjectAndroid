package com.example.mettre.myaprojectandroid.base;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.mettre.myaprojectandroid.R;


/**
 * Created by YoKeyword on 16/2/3.
 */
public class BaseMainFragment extends MySupportFragment implements View.OnClickListener{

    public int page = 1;
    public int pageSize = 10;

    private long DELAY_TIME = 900;
    private static long lastClickTime;

    protected OnFragmentOpenDrawerListener mOpenDraweListener;

    protected void initToolbarNav(Toolbar toolbar) {
        initToolbarNav(toolbar, false);
    }

    protected void initToolbarNav(Toolbar toolbar, boolean isHome) {
        if (!isHome) {
            toolbar.setNavigationIcon(R.drawable.back_icon);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pop();
//                    if (mOpenDraweListener != null) {
//                        mOpenDraweListener.onOpenDrawer();
//                    }
                }
            });
        }
    }

    protected void initClickListener(View mContentView, int... ids) {
        for (int id : ids) {
            mContentView.findViewById(id).setOnClickListener(this);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentOpenDrawerListener) {
            mOpenDraweListener = (OnFragmentOpenDrawerListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentOpenDrawerListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOpenDraweListener = null;
    }

    @Override
    public void onClick(View v) {
        // 判断当前点击事件与前一次点击事件时间间隔是否小于阙值
        if (isFastDoubleClick()) {
            return;
        }
    }

    private boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < DELAY_TIME) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public interface OnFragmentOpenDrawerListener {
        void onOpenDrawer();
    }
}