package com.example.mettre.myaprojectandroid.dialog;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.mettre.myaprojectandroid.R;
import com.example.mettre.myaprojectandroid.bean.City;
import com.yiguo.adressselectorlib.AddressSelector;
import com.yiguo.adressselectorlib.OnItemClickListener;

import java.util.ArrayList;

import me.yokeyword.fragmentation.SupportActivity;


/**
 * Created by app on 2017/11/5.
 */

public class BottomDialog extends PopupWindow {

    private View view;

    private AddressSelector addressSelector;

    public BottomDialog(int TabAmount, FragmentActivity _mActivity, ArrayList<City> cities, OnItemClickListener onItemClickListener, AddressSelector.OnTabSelectedListener onTabSelectedListener) {

        this.view = LayoutInflater.from(_mActivity).inflate(R.layout.bottom_address, null);

        addressSelector = (AddressSelector) view.findViewById(R.id.address);

        addressSelector.setTabAmount(TabAmount);
        addressSelector.setCities(cities);
        addressSelector.setListTextSize(14);

        addressSelector.setOnItemClickListener(onItemClickListener);

        addressSelector.setOnTabSelectedListener(onTabSelectedListener);

        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.main_pay).getTop();

                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });


    /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        DisplayMetrics dm = new DisplayMetrics();
        _mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        this.setHeight(dm.heightPixels / 2);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从右向左
        this.setAnimationStyle(R.style.take_photo_anim_bottom);
    }
}
