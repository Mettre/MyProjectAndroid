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
import com.example.mettre.myaprojectandroid.http.HttpMethods;
import com.example.mettre.myaprojectandroid.http.HttpResult3;
import com.example.mettre.myaprojectandroid.subscribers.ProgressSubscriber;
import com.example.mettre.myaprojectandroid.subscribers.SubscriberOnNextListener;
import com.example.mettre.myaprojectandroid.utils.CountdownControl;
import com.example.mettre.myaprojectandroid.utils.SharedPrefsUtil;
import com.example.mettre.myaprojectandroid.utils.ToastUtils;

import com.cazaea.sweetalert.SweetAlertDialog;
import rx.Subscriber;

import static com.example.mettre.myaprojectandroid.utils.LoginUtils.isMobileNO;

/**
 * Created by app on 2017/7/26.
 * 忘记密码
 */
public class ForgetPasswordFragment extends BaseMainFragment implements View.OnClickListener {

    private EditText verificationCodeNum, newPassword;
    private TextView verificationCode, phoneNumText, submit_btn;
    private Toolbar mToolbar;
    private SubscriberOnNextListener sendPhoneNumberByResetPasswordNext;
    private Subscriber<HttpResult3> subscriber;

    private SubscriberOnNextListener resetPasswordNext;
    private Subscriber<HttpResult3> subscriber2;

    public static ForgetPasswordFragment newInstance() {
        ForgetPasswordFragment fragment = new ForgetPasswordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forget_password, container, false);
        initView(view);
        onListener();
        return view;
    }

    private void onListener() {
        submit_btn.setOnClickListener(this);
        verificationCode.setOnClickListener(this);
    }


    protected void initView(View view) {

        mToolbar = view.findViewById(R.id.toolbar);
        submit_btn = view.findViewById(R.id.submit_btn);
        phoneNumText = view.findViewById(R.id.phone_num_text);
        verificationCodeNum = view.findViewById(R.id.verification_code_num);
        verificationCode = view.findViewById(R.id.verification_code);
        newPassword = view.findViewById(R.id.new_password);

        mToolbar.setTitleTextColor(getResources().getColor(R.color.oil));
        initToolbarNav(mToolbar);
        mToolbar.setTitle("修改密码");

        String phoneNum = SharedPrefsUtil.getValue(_mActivity, "username", "");
        phoneNumText.setText(phoneNum);

    }

    /**
     * 发送重置密码验证码
     */
    private void sendPhoneNumberByResetPassword(final TextView phone) {

        sendPhoneNumberByResetPasswordNext = new SubscriberOnNextListener<HttpResult3>() {

            @Override
            public void onNext(HttpResult3 response) {
                new SweetAlertDialog(_mActivity, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("")
                        .setContentText("验证码已发送到手机")
                        .setConfirmText("关闭")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();
                CountdownControl.changeBtnGetCode(verificationCode, _mActivity);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError() {

            }

            @Override
            public void onSocketTimeout() {
                ToastUtils.showCenterToast("连接服务器超时", 200);
            }

            @Override
            public void onConnectException() {
                ToastUtils.showCenterToast("服务器连接失败", 200);
            }
        };
        subscriber = new ProgressSubscriber(sendPhoneNumberByResetPasswordNext, _mActivity);
        HttpMethods.getInstance().sendVerificationCode(subscriber, phone.getText().toString(),1);
    }

    /**
     * 重置密码
     */
    private void forgetPassword() {

        resetPasswordNext = new SubscriberOnNextListener<HttpResult3>() {

            @Override
            public void onNext(HttpResult3 response) {
                new SweetAlertDialog(_mActivity, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("")
                        .setContentText("密码重置成功!")
                        .setConfirmText("关闭")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();

                                pop();

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
                ToastUtils.showCenterToast("连接服务器超时", 200);
            }

            @Override
            public void onConnectException() {
                ToastUtils.showCenterToast("服务器连接失败", 200);
            }
        };
        subscriber2 = new ProgressSubscriber(resetPasswordNext, _mActivity);
        HttpMethods.getInstance().forgetPassword(subscriber2, phoneNumText.getText().toString(), newPassword.getText().toString(), verificationCodeNum.getText().toString());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_btn:
                hideSoftInput();
                forgetPassword();

                break;
            case R.id.verification_code:
                if (TextUtils.isEmpty(phoneNumText.getText().toString())) {
                    ToastUtils.showShortToastSafe("手机号码不能为空！");
                    return;
                } else if (!isMobileNO(phoneNumText.getText().toString())) {
                    ToastUtils.showShortToastSafe("请输入正确的手机号码！");
                    return;
                }
                sendPhoneNumberByResetPassword(phoneNumText);
                break;
        }
    }
}
