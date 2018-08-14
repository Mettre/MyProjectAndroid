package com.example.mettre.myaprojectandroid.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.EventLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mettre.myaprojectandroid.R;
import com.example.mettre.myaprojectandroid.base.BaseMainFragment;
import com.example.mettre.myaprojectandroid.event.StartBrotherEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Mettre on 2018/8/14.
 */

public class RightFragment extends BaseMainFragment implements View.OnClickListener {

    private TextView address_text, out_text;

    public static RightFragment newInstance() {
        return new RightFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_right, container, false);
        initView(view);
        initListener();
        return view;
    }

    public void initView(View view) {
        address_text = view.findViewById(R.id.address_text);
        out_text = view.findViewById(R.id.out_text);
    }

    public void initListener() {
        address_text.setOnClickListener(this);
        out_text.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.address_text:
                EventBus.getDefault().post(new StartBrotherEvent(MyAddressFragment.newInstance()));
                break;
            case R.id.out_text:
                EventBus.getDefault().post(new StartBrotherEvent(MyAddressFragment.newInstance()));
                break;
        }
    }
}
