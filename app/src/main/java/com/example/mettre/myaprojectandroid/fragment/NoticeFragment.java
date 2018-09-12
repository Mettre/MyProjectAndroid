package com.example.mettre.myaprojectandroid.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mettre.myaprojectandroid.R;
import com.example.mettre.myaprojectandroid.base.BaseMainFragment;
import com.example.mettre.myaprojectandroid.bean.NoticeBean;
import com.example.mettre.myaprojectandroid.event.StartBrotherEvent;
import com.example.mettre.myaprojectandroid.http.HttpMethods;
import com.example.mettre.myaprojectandroid.http.HttpResult3;
import com.example.mettre.myaprojectandroid.http.HttpResult5;
import com.example.mettre.myaprojectandroid.subscribers.ProgressSubscriber;
import com.example.mettre.myaprojectandroid.subscribers.SubscriberOnNextListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by app on 2018/1/30.
 * 公告列表
 */

public class NoticeFragment extends BaseMainFragment {

    /**
     * 公告列表
     */
    private Toolbar mToolbar;
    private RefreshLayout refreshLayout;
    private SubscriberOnNextListener getBacklogListNext;
    private Subscriber<HttpResult5> subscriber;
    private RecyclerView recyclerView;
    private List<NoticeBean> noticeBeans;
    private MyAdapter myAdapter;

    public static NoticeFragment newInstance() {
        NoticeFragment fragment = new NoticeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_tracks, container, false);
        initLoadView(true, view);
        initView(view);
        return view;
    }

    /**
     * 添加刷新
     */
    private void setRefresh() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                getNoticeListInfo();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                getNoticeListInfo();
            }
        });
    }


    private void initView(View view) {

        mToolbar = view.findViewById(R.id.toolbar);
        recyclerView = view.findViewById(R.id.recyclerView);
        refreshLayout = view.findViewById(R.id.refreshLayout);

        mToolbar.setTitleTextColor(getResources().getColor(R.color.oil));
        initToolbarNav(mToolbar);
        mToolbar.setTitle("我的公告");
        getCache();
        setRefresh();
        myAdapterListener(myAdapter);
    }

    private void setCache() {
        hasDate();
        myAdapter.setNewData(noticeBeans);
        if (noticeBeans.size() >= 10) {
            refreshLayout.setEnableLoadmore(true);
        } else {
            refreshLayout.setEnableLoadmore(false);
        }
    }

    private void getCache() {
        noticeBeans = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        myAdapter = new MyAdapter(R.layout.fragment_service_reminding, noticeBeans);
        recyclerView.setAdapter(myAdapter);
        if (noticeBeans == null || noticeBeans.size() == 0) {
            getNoticeListInfo();
        } else {
            hasDate();
            page = 1;
            getNoticeListInfo();
        }

    }


    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getNoticeListInfo();
        }
    }

    /**
     * 我的公告
     */
    private void getNoticeListInfo() {

        getBacklogListNext = new SubscriberOnNextListener<List<NoticeBean>>() {

            @Override
            public void onNext(List<NoticeBean> response) {
                if (page == 1) {
                    if (response != null && response.size() > 0) {
                        hasDate();
                        noticeBeans = response;
                        setCache();
                    } else {
                        LoadEmpty("您还没有待办事项", "");
                    }

                } else {
                    myAdapter.addData(response);
                    page++;
                }
            }

            @Override
            public void onCompleted() {
                if (page == 1) {
                    refreshLayout.finishRefresh(100);
                } else {
                    refreshLayout.finishLoadmore(100);
                }
            }

            @Override
            public void onError() {
                if (page == 1) {
                    LoadError();
                    refreshLayout.finishRefresh(100);
                } else {
                    refreshLayout.finishLoadmore(100);
                }
            }

            @Override
            public void onSocketTimeout() {
                if (page == 1) {
                    refreshLayout.finishRefresh(100);
                } else {
                    refreshLayout.finishLoadmore(100);
                }
                connectionFailed(new onReconnectInface() {

                    @Override
                    public void onReconnect() {
                        getNoticeListInfo();
                    }
                }, true);
            }

            @Override
            public void onConnectException() {
                if (page == 1) {
                    refreshLayout.finishRefresh(100);
                } else {
                    refreshLayout.finishLoadmore(100);
                }
                connectionFailed(new onReconnectInface() {

                    @Override
                    public void onReconnect() {
                        getNoticeListInfo();
                    }
                }, true);
            }
        };
        subscriber = new ProgressSubscriber(getBacklogListNext, _mActivity, false);
        HttpMethods.getInstance().getNoticeList(subscriber, page, pageSize);
    }

    private void myAdapterListener(MyAdapter myAdapter) {
        myAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                start(WebFragment.newInstance(noticeBeans.get(position).getNoticeLink(), "公告详情"));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (subscriber != null && (!subscriber.isUnsubscribed())) {
            subscriber.unsubscribe();
        }
    }

    /**
     * 公告
     */
    class MyAdapter extends BaseQuickAdapter<NoticeBean, BaseViewHolder> {

        public MyAdapter(Integer viewId, List<NoticeBean> data) {
            super(viewId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, NoticeBean item) {
            helper.setText(R.id.service_title, item.getNoticeName());
            helper.setText(R.id.service_time, "" + item.getCreationTime());
            helper.setText(R.id.service_content, item.getNoticeDescribe());
        }
    }
}
