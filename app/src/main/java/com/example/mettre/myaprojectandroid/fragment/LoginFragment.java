package com.example.mettre.myaprojectandroid.fragment;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mettre.myaprojectandroid.R;
import com.example.mettre.myaprojectandroid.base.BaseMainFragment;
import com.example.mettre.myaprojectandroid.bean.LoginBean;
import com.example.mettre.myaprojectandroid.constant.CommonConstant;
import com.example.mettre.myaprojectandroid.event.StartBrotherEvent;
import com.example.mettre.myaprojectandroid.http.HttpMethods3;
import com.example.mettre.myaprojectandroid.http.HttpResult3;
import com.example.mettre.myaprojectandroid.subscribers.ProgressSubscriber;
import com.example.mettre.myaprojectandroid.subscribers.SubscriberOnNextListener;
import com.example.mettre.myaprojectandroid.utils.LoginUtils;
import com.example.mettre.myaprojectandroid.utils.SharedPrefsUtil;
import com.example.mettre.myaprojectandroid.view.ClearEditText;

import org.greenrobot.eventbus.EventBus;

import rx.Subscriber;

/**
 * Created by Mettre on 2018/8/15.
 * 登录
 */

public class LoginFragment extends BaseMainFragment implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextView login_btn;
    private TextView register_btn;
    private TextView forgot_password_btn;
    private SubscriberOnNextListener getOnLoginListNext;
    private Subscriber<HttpResult3> subscriber2;

    private ClearEditText phoneEdt;
    private TextView user_protocol;
    private EditText passwordEdt;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initView(view);
        inListener();
        return view;
    }

    private void inListener() {
        login_btn.setOnClickListener(this);
        register_btn.setOnClickListener(this);
        forgot_password_btn.setOnClickListener(this);
    }

    private void initView(View view) {
        mToolbar = view.findViewById(R.id.toolbar);
        login_btn = view.findViewById(R.id.login_btn);
        register_btn = view.findViewById(R.id.register_btn);
        forgot_password_btn = view.findViewById(R.id.forgot_password_btn);

        phoneEdt = view.findViewById(R.id.phone_num);
        passwordEdt = view.findViewById(R.id.password);
        user_protocol = view.findViewById(R.id.user_protocol);

        mToolbar.setTitleTextColor(getResources().getColor(R.color.oil));
        initToolbarNav(mToolbar);
        mToolbar.setTitle("登录");
        String phone = SharedPrefsUtil.getValue(_mActivity, "phone", "");
        phoneEdt.setText(phone);
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && requestCode == 2000) {
            String phone = data.getString("phone");
            String password = data.getString("password");
            phoneEdt.setText(phone);
            passwordEdt.setText(password);
        }
    }

    /**
     * 登录
     */
    private void loginOnLine(final String phone, String password) {
        getOnLoginListNext = new SubscriberOnNextListener<LoginBean>() {

            @Override
            public void onNext(LoginBean loginBean) {
                LoginUtils.getInstance().loginSaveToken(phone, loginBean.getAccess_token());
                EventBus.getDefault().post(new StartBrotherEvent(CommonConstant.USER_INFOR));
                pop();

            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError() {

            }

            @Override
            public void onSocketTimeout() {
            }

            @Override
            public void onConnectException() {
            }
        };
        subscriber2 = new ProgressSubscriber(getOnLoginListNext, _mActivity);
        HttpMethods3.getInstance().getLoginOnlineInfo(subscriber2, phone, password);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                hideSoftInput();
                if (LoginUtils.getInstance().loginCheck(phoneEdt, passwordEdt)) {
                    loginOnLine(phoneEdt.getText().toString(), passwordEdt.getText().toString());
                }
                break;
            case R.id.forgot_password_btn:
                start(ForgetPasswordFragment.newInstance());
                break;
            case R.id.register_btn:
                startForResult(RegisterFragment.newInstance(), 2000);
                break;
            case R.id.wx_login_btn:
//                WxPayUtils.wxLogin(_mActivity);
                break;
        }
    }


}
