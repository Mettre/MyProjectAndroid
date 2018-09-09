package com.example.mettre.myaprojectandroid.base;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mettre.myaprojectandroid.R;


/**
 * Created by YoKeyword on 16/2/3.
 */
public class BaseMainFragment extends MySupportFragment implements View.OnClickListener{

    public int page = 1;
    public int pageSize = 10;

    private long DELAY_TIME = 900;
    private static long lastClickTime;

    private LinearLayout mainLoadLayout;
    private LinearLayout emptyView;
    private TextView tv_empty_title;
    private TextView tv_empty_content;
    private LinearLayout loadingView;
    private LinearLayout connectionFailedView;
    private TextView reconnectText;

    protected OnFragmentOpenDrawerListener mOpenDraweListener;

    protected void initToolbarNav(Toolbar toolbar) {
        initToolbarNav(toolbar, false);
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

    protected void initClickListener(View mContentView, int... ids) {
        for (int id : ids) {
            mContentView.findViewById(id).setOnClickListener(this);
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

    /**
     * loading view
     */
    public void initLoadView(Boolean type,View view) {

        mainLoadLayout = view.findViewById(R.id.main_load_layout);
        emptyView = view.findViewById(R.id.empty_view);
        tv_empty_title = view.findViewById(R.id.tv_empty_title);
        tv_empty_content = view.findViewById(R.id.tv_empty_content);
        loadingView = view.findViewById(R.id.loading_view);
        connectionFailedView = view.findViewById(R.id.connection_failed_view);
        reconnectText = view.findViewById(R.id.reconnect_text);
        connectionFailedView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        if (type) {
            loadingView.setVisibility(View.VISIBLE);
            mainLoadLayout.setVisibility(View.VISIBLE);
        } else {
            loadingView.setVisibility(View.GONE);
            mainLoadLayout.setVisibility(View.GONE);
        }
    }

    public void connectionFailed(final onReconnectInface onReconnectInface, final boolean type) {
        mainLoadLayout.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        connectionFailedView.setVisibility(View.VISIBLE);
        reconnectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectionFailedView.setVisibility(View.GONE);
                if (type) {
                    loadingView.setVisibility(View.VISIBLE);
                }
                onReconnectInface.onReconnect();
            }
        });
    }

    public void hasDate() {
        mainLoadLayout.setVisibility(View.GONE);
    }


    public void LoadEmpty() {
        connectionFailedView.setVisibility(View.GONE);
        mainLoadLayout.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
        tv_empty_title.setText("暂无数据");
        tv_empty_content.setText("去别处逛逛吧");
    }

    public void LoadEmpty(String emptyTitle, String emptyContent) {
        mainLoadLayout.setVisibility(View.VISIBLE);
        connectionFailedView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        tv_empty_title.setText(emptyTitle);
        tv_empty_content.setText(emptyContent);
        loadingView.setVisibility(View.GONE);
    }


    public void LoadError() {
        connectionFailedView.setVisibility(View.GONE);
        mainLoadLayout.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.VISIBLE);
        tv_empty_title.setText("数据加载失败");
        tv_empty_content.setText("请稍后重试");
        loadingView.setVisibility(View.GONE);
    }

    public void LoadLoading() {
        emptyView.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        // 判断当前点击事件与前一次点击事件时间间隔是否小于阙值
        if (isFastDoubleClick()) {
            return;
        }
    }

    private boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < DELAY_TIME) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public interface OnFragmentOpenDrawerListener {
        void onOpenDrawer();
    }
    public interface onReconnectInface {
        void onReconnect();
    }

}