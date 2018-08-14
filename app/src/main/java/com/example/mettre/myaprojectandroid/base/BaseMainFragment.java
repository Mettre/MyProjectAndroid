package com.example.mettre.myaprojectandroid.base;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.mettre.myaprojectandroid.R;


/**
 * Created by YoKeyword on 16/2/3.
 */
public class BaseMainFragment extends MySupportFragment {

    public int page = 1;
    public int pageSize = 10;

    protected OnFragmentOpenDrawerListener mOpenDraweListener;

    protected void initToolbarNav(Toolbar toolbar) {
        initToolbarNav(toolbar,false);
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

    public interface OnFragmentOpenDrawerListener {
        void onOpenDrawer();
    }
}