package com.example.mettre.myaprojectandroid.fragment;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mettre.myaprojectandroid.R;
import com.example.mettre.myaprojectandroid.base.BaseMainFragment;
import com.example.mettre.myaprojectandroid.bean.CaptchaBean;
import com.example.mettre.myaprojectandroid.http.HttpMethods3;
import com.example.mettre.myaprojectandroid.http.HttpResult3;
import com.example.mettre.myaprojectandroid.subscribers.ProgressSubscriber;
import com.example.mettre.myaprojectandroid.subscribers.SubscriberOnNextListener;
import com.example.mettre.myaprojectandroid.utils.CountdownControl;
import com.example.mettre.myaprojectandroid.utils.LoginUtils;
import com.example.mettre.myaprojectandroid.utils.ToastUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;

import static com.example.mettre.myaprojectandroid.utils.LoginUtils.isMobileNO;

/**
 * Created by app on 2017/7/26.
 * 注册
 */

public class RegisterFragment extends BaseMainFragment implements View.OnClickListener {

    private Toolbar mToolbar;
    private EditText phone, verificationCodeNum, password;
    private TextView verificationCode, registerBtn;

    private SubscriberOnNextListener sendPhoneNumberByRegisterNext;
    private Subscriber<HttpResult3> subscriber;
    private SubscriberOnNextListener registerNext;
    private Subscriber<HttpResult3> subscriber2;

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        initView(view);
        onListener();
        return view;
    }

    private void onListener() {
        registerBtn.setOnClickListener(this);
        verificationCode.setOnClickListener(this);
    }


    protected void initView(View view) {
        mToolbar = view.findViewById(R.id.toolbar);
        phone = view.findViewById(R.id.phone_num);
        password = view.findViewById(R.id.password);
        verificationCodeNum = view.findViewById(R.id.verification_code_num);
        verificationCode = view.findViewById(R.id.verification_code);
        registerBtn = view.findViewById(R.id.register_btn);

        mToolbar.setTitleTextColor(getResources().getColor(R.color.oil));
        initToolbarNav(mToolbar);
        mToolbar.setTitle("注册");

    }

    /**
     * 注册
     */
    private void register() {

        registerNext = new SubscriberOnNextListener<HttpResult3>() {

            @Override
            public void onNext(HttpResult3 response) {

                Bundle bundle = new Bundle();
                bundle.putString("phone", phone.getText().toString());
                bundle.putString("password", password.getText().toString());
                setFragmentResult(RESULT_OK, bundle);
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
        subscriber2 = new ProgressSubscriber(registerNext, _mActivity);
        HttpMethods3.getInstance().register(subscriber2, phone.getText().toString(), password.getText().toString(), verificationCodeNum.getText().toString());
    }


    /**
     * 发送注册验证码
     */
    private void sendPhoneNumberByRegister() {

        sendPhoneNumberByRegisterNext = new SubscriberOnNextListener<CaptchaBean>() {

            @Override
            public void onNext(CaptchaBean captchaBean) {
                CountdownControl.changeBtnGetCode(verificationCode, _mActivity);

                new SweetAlertDialog(_mActivity, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("验证码已发送到手机")
                        .setContentText(captchaBean.getCode())
                        .setConfirmText("关闭")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();
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
        subscriber = new ProgressSubscriber(sendPhoneNumberByRegisterNext, _mActivity);
        HttpMethods3.getInstance().sendVerificationCode(subscriber, phone.getText().toString(), 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_btn:
                if (LoginUtils.getInstance().RegisterVerification(phone, verificationCodeNum, password)) {
                    register();
                }
                break;
            case R.id.verification_code:
                if (TextUtils.isEmpty(phone.getText().toString())) {
                    ToastUtils.showShortToastSafe("手机号码不能为空！");
                    return;
                } else if (!isMobileNO(phone.getText().toString())) {
                    ToastUtils.showShortToastSafe("请输入正确的手机号码！");
                    return;
                }
//                getVerifyCode();
                sendPhoneNumberByRegister();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
