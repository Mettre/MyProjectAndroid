package com.example.mettre.myaprojectandroid.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mettre.myaprojectandroid.R;
import com.example.mettre.myaprojectandroid.base.BaseMainFragment;
import com.example.mettre.myaprojectandroid.bean.GoodsListBean;
import com.example.mettre.myaprojectandroid.constant.CommonConstant;
import com.example.mettre.myaprojectandroid.http.HttpMethods3;
import com.example.mettre.myaprojectandroid.http.HttpResult5;
import com.example.mettre.myaprojectandroid.subscribers.ProgressSubscriber;
import com.example.mettre.myaprojectandroid.subscribers.SubscriberOnNextListener;
import com.example.mettre.myaprojectandroid.utils.MyImageLoader;
import com.example.mettre.myaprojectandroid.utils.RandomURL;
import com.example.mettre.myaprojectandroid.utils.ToastUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by Mettre on 2018/8/31.
 * 商品列表
 */

public class GoodsListFragment extends BaseMainFragment {

    private TextView searchText;
    private List<GoodsListBean> goodsList;
    private SubscriberOnNextListener getRefreshGoodsListNext;
    private Subscriber<HttpResult5> subscriber;


    //商品分类
    private int categoryId;

    private RefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;

    public static GoodsListFragment newInstance(int categoryId) {
        GoodsListFragment goodsListFragment = new GoodsListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("categoryId", categoryId);
        goodsListFragment.setArguments(bundle);
        return goodsListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_list, container, false);
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
                getRefreshGoods();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                getRefreshGoods();
            }
        });
    }

    private void initView(View view) {
        categoryId = getArguments().getInt("categoryId", 0);
        searchText = view.findViewById(R.id.search_text);
        recyclerView = view.findViewById(R.id.recyclerView);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        initClickListener(view, R.id.back_icon, R.id.search_LinearLayout);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        goodsList = new ArrayList<>();
        myAdapter = new MyAdapter(goodsList);
        recyclerView.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });

        setRefresh();
        getRefreshGoods();
    }


    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == CommonConstant.GOODS_REFRESH && resultCode == RESULT_OK && data != null) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_LinearLayout:
//                startForResult(HMSearchFragment.newInstance(false, true, serviceModule), CommonConstant.GOODS_REFRESH);
                break;
            case R.id.back_icon:
                pop();
                break;
        }
    }


    /**
     * 刷新商品列表
     */
    private void getRefreshGoods() {

        getRefreshGoodsListNext = new SubscriberOnNextListener<List<GoodsListBean>>() {

            @Override
            public void onNext(List<GoodsListBean> goodsListBeanList) {

                if (page == 1) {
                    goodsList = goodsListBeanList;
                    if (goodsList == null || goodsList.size() == 0) {

                    } else {
                        myAdapter.setNewData(goodsList);
                    }
                } else {
                    myAdapter.addData(goodsList);
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
                    if (goodsList == null || goodsList.size() == 0) {

                    }
                    refreshLayout.finishRefresh(100);
                } else {
                    page--;
                    refreshLayout.finishLoadmore(100);
                }
            }

            @Override
            public void onSocketTimeout() {

            }

            @Override
            public void onConnectException() {

            }
        };
        subscriber = new ProgressSubscriber(getRefreshGoodsListNext, _mActivity, false);
        HttpMethods3.getInstance().getGoodsList(subscriber, categoryId, page, pageSize);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (subscriber != null && (!subscriber.isUnsubscribed())) {
            subscriber.unsubscribe();
        }
        if (refreshLayout.isLoading()) {
            refreshLayout.finishLoadmore();
            refreshLayout.finishRefresh(10);
        }
    }

    class MyAdapter extends BaseQuickAdapter<GoodsListBean, BaseViewHolder> {

        public MyAdapter(List<GoodsListBean> data) {
            super(R.layout.item_goods_list, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final GoodsListBean item) {
            MyImageLoader.getInstance().displayImage(_mActivity, RandomURL.getInstance().getRandomUrl(), (ImageView) helper.getView(R.id.icon));
            helper.setText(R.id.product_name, item.getGoodsName());
            helper.setText(R.id.price_market, "￥" + item.getMarketPrice());
            ((TextView) helper.getView(R.id.price_market)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            helper.setText(R.id.price_hm, "￥" + item.getShopPrice());
            helper.getView(R.id.cart_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getStock() > 0) {

                    } else {
                        ToastUtils.showCenterToast("商品库存不足", 200);
                    }
                }
            });
        }
    }
}
