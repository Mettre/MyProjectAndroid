package com.example.mettre.myaprojectandroid.subscribers;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.mettre.myaprojectandroid.app.MyApplication;
import com.example.mettre.myaprojectandroid.event.StartBrotherEvent;
import com.example.mettre.myaprojectandroid.fragment.LoginFragment;
import com.example.mettre.myaprojectandroid.progress.ProgressCancelListener;
import com.example.mettre.myaprojectandroid.progress.ProgressDialogHandler;
import com.example.mettre.myaprojectandroid.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import com.cazaea.sweetalert.SweetAlertDialog;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import rx.Subscriber;

/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 * Created by liukun on 16/3/10.
 */
public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private SubscriberOnNextListener mSubscriberOnNextListener;
    private ProgressDialogHandler mProgressDialogHandler;

    private SweetAlertDialog sweetAlertDialog;
    private FragmentActivity context;

    public ProgressSubscriber(SubscriberOnNextListener mSubscriberOnNextListener, FragmentActivity context) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.context = context;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }

    /**
     * 用于请求时不显示加载dialog
     *
     * @param mSubscriberOnNextListener
     * @param context
     */
    public ProgressSubscriber(SubscriberOnNextListener mSubscriberOnNextListener, FragmentActivity context, boolean isShow) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.context = context;
    }

    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        showProgressDialog();
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
        dismissProgressDialog();
        mSubscriberOnNextListener.onCompleted();
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();
        String message = "";
        if (e instanceof SocketTimeoutException) {
            message = "连接服务器超时";
            mSubscriberOnNextListener.onSocketTimeout();
        } else if (e instanceof ConnectException) {
            message = "服务器连接失败";
            mSubscriberOnNextListener.onConnectException();
        } else {
            mSubscriberOnNextListener.onError();
            message = e.getMessage();
        }
        if ("未登录".equals(message)) {
            EventBus.getDefault().post(new StartBrotherEvent(LoginFragment.newInstance()));
        }
        if (sweetAlertDialog == null) {
            sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
            sweetAlertDialog.setTitleText("异常")
                    .setContentText(message)
                    .setConfirmText("关闭")
                    .show();
        } else if (!sweetAlertDialog.isShowing()) {
            sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
            sweetAlertDialog.setTitleText("异常")
                    .setContentText(message)
                    .setConfirmText("关闭")
                    .show();

        }
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener != null) {
            try {
                mSubscriberOnNextListener.onNext(t);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}