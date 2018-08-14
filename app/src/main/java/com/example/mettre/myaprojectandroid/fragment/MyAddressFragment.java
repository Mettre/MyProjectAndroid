package com.example.mettre.myaprojectandroid.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.mettre.myaprojectandroid.R;
import com.example.mettre.myaprojectandroid.adapter.AddressListAdapter;
import com.example.mettre.myaprojectandroid.base.BaseMainFragment;
import com.example.mettre.myaprojectandroid.bean.AddressBean;
import com.example.mettre.myaprojectandroid.http.HttpMethods3;
import com.example.mettre.myaprojectandroid.http.HttpResult3;
import com.example.mettre.myaprojectandroid.response.PageResponse;
import com.example.mettre.myaprojectandroid.subscribers.ProgressSubscriber;
import com.example.mettre.myaprojectandroid.subscribers.SubscriberOnNextListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;

/**
 * Created by Mettre on 2018/8/14.
 * 我的收货地址
 */

public class MyAddressFragment extends BaseMainFragment {

    /**
     * 收货地址列表
     */
    private Toolbar mToolbar;
    private TextView add_btn;
    private SubscriberOnNextListener getAddAddressListNext;
    private Subscriber<HttpResult3> subscriber2;

    private RefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private AddressListAdapter myAdapter;
    private List<AddressBean> addressList;

    public static MyAddressFragment newInstance() {
        return new MyAddressFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address, container, false);
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
        mToolbar = view.findViewById(R.id.toolbar);
        recyclerView = view.findViewById(R.id.recyclerView);
        add_btn = view.findViewById(R.id.add_btn);
        refreshLayout = view.findViewById(R.id.refreshLayout);

        mToolbar.setTitleTextColor(getResources().getColor(R.color.oil));
        initToolbarNav(mToolbar);
        mToolbar.setTitle("我的收货地址");
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        addressList = new ArrayList<>();
        myAdapter = new AddressListAdapter(addressList);
        recyclerView.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                startForResult(AddAddressFragment.newInstance(), 1000);

            }
        });

        myAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                new SweetAlertDialog(_mActivity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("提示")
                        .setContentText("确定要删除该收货地址吗？")
                        .setCancelText("取消")
                        .setConfirmText("确定")
                        .showCancelButton(true)
                        .setCancelClickListener(null)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
//                                deleteAddress(String.valueOf(addressList.get(position).getId()), position);

                            }
                        })
                        .show();

                return false;
            }
        });
        setRefresh();
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start(AddAddressFragment.newInstance());
            }
        });
    }

    /**
     * 收货地址列表
     */
    private void getAddressList() {

        getAddAddressListNext = new SubscriberOnNextListener<PageResponse<AddressBean>>() {

            @Override
            public void onNext(PageResponse<AddressBean> response) {
                if (page == 1) {
                    addressList = response.getList();
                    if (addressList == null || addressList.size() == 0) {
                    } else {
                        myAdapter.setNewData(addressList);
                    }
                } else {
                    myAdapter.addData(response.getList());
                    page++;
                }
                if (response.isHasNextPage()) {
                    refreshLayout.setEnableLoadmore(true);
                } else {
                    refreshLayout.setEnableLoadmore(false);
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
            }

            @Override
            public void onConnectException() {
                if (page == 1) {
                    refreshLayout.finishRefresh(100);
                } else {
                    refreshLayout.finishLoadmore(100);
                }
            }
        };
        subscriber2 = new ProgressSubscriber(getAddAddressListNext, _mActivity, false);
        HttpMethods3.getInstance().AddressList(subscriber2, page, pageSize);
    }

}
