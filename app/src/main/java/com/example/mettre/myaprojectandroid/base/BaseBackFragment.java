package com.example.mettre.myaprojectandroid.base;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.mettre.myaprojectandroid.R;

/**
 * Created by Mettre on 2018/8/14.
 */

public class BaseBackFragment extends MySupportFragment {

    protected void initToolbarNav(Toolbar toolbar) {

        toolbar.setNavigationIcon(R.drawable.back_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });

    }
}