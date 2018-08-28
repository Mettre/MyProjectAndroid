package com.example.mettre.myaprojectandroid.utils;

import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by app on 2017/12/11.
 * 验证码倒计时控件
 */

public class CountdownControl {

    /**
     * 验证码60秒重新获取
     *
     * @param verificationCode
     * @param _mActivity
     */
    public static void changeBtnGetCode(final TextView verificationCode, final FragmentActivity _mActivity) {

        final boolean[] tag = {true};
        final int[] i = {60};
        Thread thread = null;

        thread = new Thread() {
            @Override
            public void run() {
                if (tag[0]) {
                    while (i[0] > 0) {
                        i[0]--;
                        if (_mActivity == null) {
                            break;
                        }
                        _mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //mBtnCode.setText("获取验证码(" + i + ")");
                                String str = String.format("重新获取(%s)", i[0]);
                                verificationCode.setText(str);
                                verificationCode.setClickable(false);
                                verificationCode.setEnabled(false);
                            }
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    tag[0] = false;
                }
                i[0] = 60;
                tag[0] = true;
                if (_mActivity != null) {
                    _mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            verificationCode.setText("获取验证码");
                            verificationCode.setClickable(true);
                            verificationCode.setEnabled(true);
                        }
                    });
                }
            }
        };
        thread.start();
    }
}
