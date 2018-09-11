package com.example.mettre.myaprojectandroid.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.example.mettre.myaprojectandroid.R;
import com.example.mettre.myaprojectandroid.adapter.OrderAdapter;
import com.example.mettre.myaprojectandroid.base.BaseMainFragment;
import com.example.mettre.myaprojectandroid.bean.OrderListBean;
import com.example.mettre.myaprojectandroid.http.HttpMethods;
import com.example.mettre.myaprojectandroid.http.HttpResult3;
import com.example.mettre.myaprojectandroid.http.HttpResult5;
import com.example.mettre.myaprojectandroid.subscribers.ProgressSubscriber;
import com.example.mettre.myaprojectandroid.subscribers.SubscriberOnNextListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by app on 2017/7/18.
 * 全部订单
 */

public class AllOrderFragment extends BaseMainFragment implements OrderAdapter.OrderClickListener {

    private int orderStatus = 0;
    private RefreshLayout refreshLayout;
    private ExpandableListView exListView;
    private OrderAdapter orderAdapter;

    /**
     * 代付款订单
     */
    private List<OrderListBean> list;
    private SubscriberOnNextListener getWaitPayOrderListNext;
    private Subscriber<HttpResult5> subscriber;

    /**
     * 取消订单
     */
    private SubscriberOnNextListener cancellationOrderNext;
    private Subscriber<HttpResult3> subscriber2;


    public static AllOrderFragment newInstance() {
        AllOrderFragment fragment = new AllOrderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_order, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        initLoadView(true, view);
        exListView = view.findViewById(R.id.exListView);
        list = new ArrayList<>();
        getOrderList(orderStatus);
        setRefresh(view);
        exListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                EventBus.getDefault().post(new StartBrotherEvent(ShopInfoFragment.newInstance(list.get(groupPosition).getBrandId())));
                return true;
            }
        });
    }

    private void initEvents() {
        orderAdapter = new OrderAdapter(list, _mActivity, this);
        exListView.setAdapter(orderAdapter);

        for (int i = 0; i < orderAdapter.getGroupCount(); i++) {
            exListView.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
        }
    }

    /**
     * 全部展开
     */
    private void expanedAll() {
        orderAdapter.notifyDataSetChanged();
        for (int i = 0; i < list.size(); i++) {
            exListView.expandGroup(i);
        }
    }

    /**
     * 订单列表
     */
    private void getOrderList(final int orderStatus) {

        getWaitPayOrderListNext = new SubscriberOnNextListener<List<OrderListBean>>() {

            @Override
            public void onNext(List<OrderListBean> response) {
                if (page == 1) {
                    list = response;
                    if (list == null || list.size() == 0) {
                        LoadEmpty("没有相关订单哦", "");
                    } else {
                        hasDate();
                        initEvents();
                    }
                } else {
                    list.addAll(response);
                    expanedAll();
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
                    page--;
                    refreshLayout.finishLoadmore(100);
                }
            }

            @Override
            public void onSocketTimeout() {
                if (page == 1) {
                    refreshLayout.finishRefresh(100);
                } else {
                    page--;
                    refreshLayout.finishLoadmore(100);
                }
                connectionFailed(new onReconnectInface() {

                    @Override
                    public void onReconnect() {
                        page = 1;
                        getOrderList(orderStatus);
                    }
                }, true);
            }

            @Override
            public void onConnectException() {
                if (page == 1) {
                    refreshLayout.finishRefresh(100);
                } else {
                    page--;
                    refreshLayout.finishLoadmore(100);
                }
                connectionFailed(new onReconnectInface() {

                    @Override
                    public void onReconnect() {
                        page = 1;
                        getOrderList(orderStatus);
                    }
                }, true);
            }
        };
        subscriber = new ProgressSubscriber(getWaitPayOrderListNext, _mActivity, false);
        HttpMethods.getInstance().getOrderList(subscriber, page, pageSize, orderStatus);
    }

    /**
     * 取消订单
     */
    private void CancellationOrder(Long orderId) {

        cancellationOrderNext = new SubscriberOnNextListener<HttpResult3>() {

            @Override
            public void onNext(HttpResult3 response) {
                refreshLayout.autoRefresh();
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
        subscriber2 = new ProgressSubscriber(cancellationOrderNext, _mActivity);
        HttpMethods.getInstance().cancelOrder(subscriber2, orderId);
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
                getOrderList(orderStatus);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                getOrderList(orderStatus);
            }
        });
    }

    @Override
    public void onBtnClick(final int position, String str) {
        switch (str) {
            case "付款":

                break;
            case "提醒发货":
                new SweetAlertDialog(_mActivity, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("")
                        .setContentText("已向买家提醒发货，卖家会尽快发货")
                        .setConfirmText("关闭")
                        .show();
                break;
            case "查看物流":

                break;
            case "取消订单":
                new SweetAlertDialog(_mActivity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("取消订单")
                        .setContentText("确定取消该订单吗？")
                        .setCancelText("取消")
                        .setConfirmText("确定")
                        .showCancelButton(true)
                        .setCancelClickListener(null)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                                CancellationOrder(list.get(position).getOrderId());
                            }
                        })
                        .show();
                break;
            case "确认收货":
                new SweetAlertDialog(_mActivity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确认收货")
                        .setContentText("确定要确认收货吗？")
                        .setCancelText("取消")
                        .setConfirmText("确定")
                        .showCancelButton(true)
                        .setCancelClickListener(null)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                            }
                        })
                        .show();
                break;
        }
    }

    @Override
    public void itemClick(int position) {
//        EventBus.getDefault().post(new StartBrotherEvent(OrderDetailFragment.newInstance(list.get(position).getOrderId())));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (subscriber != null && (!subscriber.isUnsubscribed())) {
            subscriber.unsubscribe();
        }
        if (subscriber2 != null && (!subscriber2.isUnsubscribed())) {
            subscriber2.unsubscribe();
        }
    }
}
