package com.example.mettre.myaprojectandroid.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mettre.myaprojectandroid.R;
import com.example.mettre.myaprojectandroid.app.MyApplication;
import com.example.mettre.myaprojectandroid.base.BaseMainFragment;
import com.example.mettre.myaprojectandroid.bean.UserBean;
import com.example.mettre.myaprojectandroid.constant.CommonConstant;
import com.example.mettre.myaprojectandroid.event.StartBrotherEvent;
import com.example.mettre.myaprojectandroid.http.HttpMethods;
import com.example.mettre.myaprojectandroid.subscribers.ProgressSubscriber;
import com.example.mettre.myaprojectandroid.subscribers.SubscriberOnNextListener;
import com.example.mettre.myaprojectandroid.utils.LoginUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Mettre on 2018/8/14.
 */

public class RightFragment extends BaseMainFragment implements View.OnClickListener {

    private TextView address_text, out_text;
    private LinearLayout information_linearLayout;
    private RefreshLayout refreshLayout;

    private ImageView icon_img;
    private TextView nice_name;
    private TextView nice_name_line;
    private TextView login_btn;

    /**
     * 获取个人信息
     */
    private UserBean userBean;
    private SubscriberOnNextListener getUserInfoNext;

    public static RightFragment newInstance() {
        return new RightFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_right, container, false);
        initView(view);
        setRefresh(view);
        initListener();
        return view;
    }


    /**
     * 添加刷新
     */
    private void setRefresh(View view) {
        refreshLayout = view.findViewById(R.id.refreshLayout);
        if (TextUtils.isEmpty(MyApplication.getInstances().getToken())) {
            refreshLayout.setEnableRefresh(false);
        } else {
            refreshLayout.setEnableRefresh(true);
        }
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getUserInfoNext();
            }
        });
    }

    public void initView(View view) {
        address_text = view.findViewById(R.id.address_text);
        out_text = view.findViewById(R.id.out_text);
        icon_img = view.findViewById(R.id.icon_img);
        nice_name = view.findViewById(R.id.nice_name);
        nice_name_line = view.findViewById(R.id.nice_name_line);
        login_btn = view.findViewById(R.id.login_btn);

        information_linearLayout = view.findViewById(R.id.information_linearLayout);
        if (TextUtils.isEmpty(MyApplication.getInstances().getToken())) {
            login_btn.setVisibility(View.VISIBLE);
            nice_name.setVisibility(View.GONE);
            nice_name_line.setVisibility(View.GONE);
            icon_img.setImageResource(R.drawable.icon_head2);
            return;
        } else {
            login_btn.setVisibility(View.GONE);
            nice_name.setVisibility(View.VISIBLE);
            nice_name_line.setVisibility(View.VISIBLE);
            getUserInfoNext();
        }
    }

    public void initListener() {
        address_text.setOnClickListener(this);
        out_text.setOnClickListener(this);
        information_linearLayout.setOnClickListener(this);
    }


    private void signOutUI() {
        userBean = null;
        nice_name.setText("");
        login_btn.setVisibility(View.VISIBLE);
        nice_name.setVisibility(View.GONE);
        nice_name_line.setVisibility(View.GONE);
        icon_img.setImageResource(R.drawable.icon_head2);
        refreshLayout.setEnableRefresh(false);
    }

    private void loginUI() {
        login_btn.setVisibility(View.GONE);
        nice_name.setVisibility(View.VISIBLE);
        nice_name_line.setVisibility(View.VISIBLE);
        refreshLayout.setEnableRefresh(true);
        getUserInfoNext();
    }

    @Subscribe
    public void startBrother(StartBrotherEvent event) {
        if (event.EventType == CommonConstant.USER_INFOR) {
            loginUI();
        }
    }

    /**
     * 获取个人信息
     */
    private void getUserInfoNext() {

        getUserInfoNext = new SubscriberOnNextListener<UserBean>() {

            @Override
            public void onNext(UserBean userBean1) {
                userBean = userBean1;
                nice_name.setText(userBean.getName());
            }

            @Override
            public void onCompleted() {
                refreshLayout.finishRefresh(10);
            }

            @Override
            public void onError() {
                refreshLayout.finishRefresh(10);
            }

            @Override
            public void onSocketTimeout() {
                refreshLayout.finishRefresh(10);
            }

            @Override
            public void onConnectException() {
                refreshLayout.finishRefresh(10);
            }
        };
        HttpMethods.getInstance().getUserInfo(new ProgressSubscriber(getUserInfoNext, _mActivity));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.address_text:
                EventBus.getDefault().post(new StartBrotherEvent(AddressListFragment.newInstance(false)));
                break;
            case R.id.out_text:
                LoginUtils.getInstance().signOutRemoveToken();
                EventBus.getDefault().post(new StartBrotherEvent(LoginFragment.newInstance()));
                signOutUI();
                break;
            case R.id.information_linearLayout:
                if (!TextUtils.isEmpty(MyApplication.getInstances().getToken()) && userBean != null) {
                    EventBus.getDefault().post(new StartBrotherEvent(InformationFragment.newInstance(userBean)));
                } else {
                    EventBus.getDefault().post(new StartBrotherEvent(LoginFragment.newInstance()));
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
