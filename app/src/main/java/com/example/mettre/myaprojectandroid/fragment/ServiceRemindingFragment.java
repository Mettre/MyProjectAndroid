package com.example.mettre.myaprojectandroid.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.mettre.myaprojectandroid.http.HttpMethods;
import com.example.mettre.myaprojectandroid.http.HttpResult3;
import com.example.mettre.myaprojectandroid.subscribers.ProgressSubscriber;
import com.example.mettre.myaprojectandroid.subscribers.SubscriberOnNextListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by app on 2018/1/30.
 * 公告列表
 */

public class ServiceRemindingFragment extends BaseMainFragment {

    /**
     * 公告列表
     */
    private Toolbar mToolbar;
    private RefreshLayout refreshLayout;
    private SubscriberOnNextListener getBacklogListNext;
    private Subscriber<HttpResult3> subscriber;
    private RecyclerView recyclerView;
    private ArrayList<BacklogResponse.ListBean> backLoglist;
    private MyAdapter myAdapter;

    public static ServiceRemindingFragment newInstance() {
        ServiceRemindingFragment fragment = new ServiceRemindingFragment();
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
                getAddressList();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                getAddressList();
            }
        });
    }


    private void initView(View view) {

        setRefresh();
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

    /**
     * 添加刷新
     */
    private void setRefresh(View view) {
        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                getBacklogListInfo();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                getBacklogListInfo();
            }
        });
    }

    private void setCache() {
        hasDate();
        myAdapter.setNewData(backLoglist);
        if (backLoglist.size() >= 10) {
            refreshLayout.setEnableLoadmore(true);
        } else {
            refreshLayout.setEnableLoadmore(false);
        }
    }

    private void getCache() {
        backLoglist = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        myAdapter = new MyAdapter(R.layout.fragment_service_reminding, backLoglist);
        recyclerView.setAdapter(myAdapter);
        if (backLoglist == null || backLoglist.size() == 0) {
            getBacklogListInfo();
        } else {
            hasDate();
            page = 1;
            getBacklogListInfo();
        }

    }


    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getBacklogListInfo();
        }
    }

    /**
     * 我的公告
     */
    private void getBacklogListInfo() {

        getBacklogListNext = new SubscriberOnNextListener<List<HttpResult3>>() {

            @Override
            public void onNext(List<HttpResult3> response) {
                if (page == 1) {
                    if (response.getList() != null && response.getList().size() > 0) {
                        hasDate();
                        backLoglist = response.getList();
                        setCache();
                    } else {
                        LoadEmpty("您还没有待办事项", "");
                    }

                } else {
                    myAdapter.addData(response.getList());
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
                        getBacklogListInfo();
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
                        getBacklogListInfo();
                    }
                }, true);
            }
        };
        subscriber = new ProgressSubscriber(getBacklogListNext, _mActivity, false);
        HttpMethods.getInstance().AddAddressRequest(subscriber, page, pageSize);
    }

    private void myAdapterListener(MyAdapter myAdapter) {
        myAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!StringUtils.isEmpty(backLoglist.get(position).getPendingType())) {
                    switch (backLoglist.get(position).getPendingType()) {
                        case "ADD_FAMILY":
                            startForResult(AuditPropertyEntryFragment.newInstance(backLoglist.get(position), position), 1000);
                            break;
                    }
                    BacklogResponse.ListBean listBean = backLoglist.get(position);
                    listBean.setPendingStatus("CHECKED");
                    backLoglist.set(position, listBean);
                    adapter.setNewData(backLoglist);
                }
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
    class MyAdapter extends BaseQuickAdapter<BacklogResponse.ListBean, BaseViewHolder> {

        public MyAdapter(Integer viewId, ArrayList<BacklogResponse.ListBean> data) {
            super(viewId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, BacklogResponse.ListBean item) {
            helper.setText(R.id.service_title, item.getTitle());
            helper.setText(R.id.service_time, TimeUtils.stringReplaceT(item.getCreatedDate()));
            helper.setText(R.id.service_content, item.getContent());
            helper.setVisible(R.id.badge_red, "CREATE".equals(item.getPendingStatus()));
        }
    }
}
