package com.example.mettre.myaprojectandroid.fragment;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mettre.myaprojectandroid.R;
import com.example.mettre.myaprojectandroid.base.BaseMainFragment;
import com.example.mettre.myaprojectandroid.bean.AdvBean;
import com.example.mettre.myaprojectandroid.bean.GoodsListBean;
import com.example.mettre.myaprojectandroid.constant.CommonConstant;
import com.example.mettre.myaprojectandroid.event.StartBrotherEvent;
import com.example.mettre.myaprojectandroid.http.HttpMethods;
import com.example.mettre.myaprojectandroid.http.HttpResult5;
import com.example.mettre.myaprojectandroid.subscribers.ProgressSubscriber;
import com.example.mettre.myaprojectandroid.subscribers.SubscriberOnNextListener;
import com.example.mettre.myaprojectandroid.utils.MyImageLoader;
import com.example.mettre.myaprojectandroid.utils.RandomURL;
import com.example.mettre.myaprojectandroid.view.BetterRecyclerView;
import com.example.mettre.myaprojectandroid.view.SquareImageView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by Mettre on 2018/8/14.
 */

public class LeftFragment extends BaseMainFragment {

    private List<AdvBean> advBeanList;
    private List<AdvBean> advRecommendList;
    private Subscriber<HttpResult5> subscriber;
    private SubscriberOnNextListener getBannerOnNext;

    private TextView title_left_top, title_right_top, title_left_bottom, title_right_bottom;
    private TextView describe_left_top, describe_right_top, describe_left_bottom, describe_right_bottom;
    private ImageView image_left_top, image_right_top, imageView_left_bottom, imageView_right_bottom;
    private LinearLayout linearLayout_left_top, linearLayout_right_top, linearLayout_left_bottom, linearLayout_right_bottom;

    private MZBannerView mMZBanner;
    private List<String> imageUrl = new ArrayList<>();

    /**
     * 直降价
     */
    private SubscriberOnNextListener getPromotionOnNext;
    private List<GoodsListBean> listInfo;
    private RecyclerView recyclerView1;
    private RefreshLayout refreshLayout;
    private MyAdapter1 myAdapter1;

    /**
     * 秒杀
     */
    private SubscriberOnNextListener getStraightDownOnNext;
    private MyAdapter2 myAdapter2;
    private List<GoodsListBean> dropListInfo;
    private BetterRecyclerView recyclerView2;

    public static LeftFragment newInstance() {
        return new LeftFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_left, container, false);
        initClickListener(view, R.id.zxing_img);
        initLoadView(false, view);
        initView(view);
        getBannerList(CommonConstant.HOME_PROMOTION);
        getBannerList(CommonConstant.HOME_RECOMMENT);
        return view;
    }

    public void initView(View view) {

        recyclerView1 = view.findViewById(R.id.recycleView);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        listInfo = new ArrayList<>();
        recyclerView1.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        myAdapter1 = new MyAdapter1(listInfo);
        recyclerView1.setAdapter(myAdapter1);

        setHeader(recyclerView1);
        setHeightWeight(image_left_top);
        setHeightWeight(image_right_top);
        setHeightWeight(imageView_left_bottom);
        setHeightWeight(imageView_right_bottom);

        mMZBanner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int i) {
                onAdvClick(advBeanList.get(i));
            }
        });

        setTopRefresh();

        myAdapterListener(myAdapter1);
        PromotionRefresh();
    }

    /**
     * 添加刷新--
     */
    private void setTopRefresh() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getBannerList(CommonConstant.HOME_PROMOTION);
            }
        });
    }

    private void myAdapterListener(MyAdapter1 myAdapter) {
        myAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EventBus.getDefault().post(new StartBrotherEvent(GoodsDetailsFragment.newInstance(listInfo.get(position).getGoodsId())));
            }
        });
    }

    private void myAdapterListener(MyAdapter2 myAdapter) {
        myAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                EventBus.getDefault().post(new StartBrotherEvent(GoodsDetailsFragment.newInstance(dropListInfo.get(position).getGoodsId())));
            }
        });
    }

    private void setHeader(RecyclerView view) {
        dropListInfo = new ArrayList<>();
        View header = LayoutInflater.from(_mActivity).inflate(R.layout.home_head, view, false);
        initClickListener(header, R.id.linearLayout_left_top, R.id.linearLayout_right_top, R.id.linearLayout_left_bottom, R.id.linearLayout_right_bottom);

        recyclerView2 = header.findViewById(R.id.recycle_seckill);
        mMZBanner = header.findViewById(R.id.banner);
        title_left_top = header.findViewById(R.id.title_left_top);
        title_right_top = header.findViewById(R.id.title_right_top);
        title_left_bottom = header.findViewById(R.id.title_left_bottom);
        title_right_bottom = header.findViewById(R.id.title_right_bottom);
        describe_left_top = header.findViewById(R.id.describe_left_top);
        describe_right_top = header.findViewById(R.id.describe_right_top);
        describe_left_bottom = header.findViewById(R.id.describe_left_bottom);
        describe_right_bottom = header.findViewById(R.id.describe_right_bottom);
        image_left_top = header.findViewById(R.id.image_left_top);
        image_right_top = header.findViewById(R.id.image_right_top);
        imageView_left_bottom = header.findViewById(R.id.imageView_left_bottom);
        imageView_right_bottom = header.findViewById(R.id.imageView_right_bottom);
        linearLayout_left_top = header.findViewById(R.id.linearLayout_left_top);
        linearLayout_right_top = header.findViewById(R.id.linearLayout_right_top);
        linearLayout_left_bottom = header.findViewById(R.id.linearLayout_left_bottom);
        linearLayout_right_bottom = header.findViewById(R.id.linearLayout_right_bottom);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView2.setLayoutManager(linearLayoutManager);
        myAdapter2 = new MyAdapter2(R.layout.item_seckill, dropListInfo);
        recyclerView2.setAdapter(myAdapter2);
        myAdapterListener(myAdapter2);
        myAdapter1.setHeaderView(header);
    }


    private void setHeightWeight(final ImageView imageView) {
        imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                layoutParams.height = (int) (imageView.getWidth() / 2);
                imageView.setLayoutParams(layoutParams);
                return true;
            }
        });
    }

    //    加载轮播图
    private void initBannerDate() {
        imageUrl.clear();
        for (AdvBean advBean : advBeanList) {
            imageUrl.add(advBean.getAdImage());
        }
        // 设置数据
        mMZBanner.setPages(imageUrl, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });
    }

    //    加载推荐图
    private void initRecommendDate() {

        title_left_top.setText(advRecommendList.get(0).getAdName());
        describe_left_top.setText(advRecommendList.get(0).getAdDescribe());
        MyImageLoader.getInstance().displayImage(_mActivity, advRecommendList.get(0).getAdImage(), image_left_top);
        linearLayout_left_top.setOnClickListener(this);
        if (advRecommendList.size() == 1) return;

        title_right_top.setText(advRecommendList.get(1).getAdName());
        describe_right_top.setText(advRecommendList.get(1).getAdDescribe());
        MyImageLoader.getInstance().displayImage(_mActivity, advRecommendList.get(1).getAdImage(), image_right_top);
        linearLayout_right_top.setOnClickListener(this);
        if (advRecommendList.size() == 2) return;

        title_left_bottom.setText(advRecommendList.get(2).getAdName());
        describe_left_bottom.setText(advRecommendList.get(2).getAdDescribe());
        MyImageLoader.getInstance().displayImage(_mActivity, advRecommendList.get(2).getAdImage(), imageView_left_bottom);
        linearLayout_left_bottom.setOnClickListener(this);
        if (advRecommendList.size() == 3) return;

        title_right_bottom.setText(advRecommendList.get(3).getAdName());
        describe_right_bottom.setText(advRecommendList.get(3).getAdDescribe());
        MyImageLoader.getInstance().displayImage(_mActivity, advRecommendList.get(3).getAdImage(), imageView_right_bottom);
        linearLayout_right_bottom.setOnClickListener(this);
    }


    /**
     * 秒杀列表
     */
    private void PromotionRefresh() {

        getStraightDownOnNext = new SubscriberOnNextListener<List<GoodsListBean>>() {
            @Override
            public void onNext(List<GoodsListBean> goodsListBeans) {
                dropListInfo = goodsListBeans;
                myAdapter2.setNewData(dropListInfo);
            }

            @Override
            public void onCompleted() {
                hasDate();
                Depreciate();
            }

            @Override
            public void onError() {
                Depreciate();
            }

            @Override
            public void onSocketTimeout() {
                refreshLayout.finishRefresh(10);
                connectionFailed(new onReconnectInface() {

                    @Override
                    public void onReconnect() {
                        LoadLoading();
                        PromotionRefresh();
                    }
                }, false);
            }

            @Override
            public void onConnectException() {
                refreshLayout.finishRefresh(10);
                connectionFailed(new onReconnectInface() {

                    @Override
                    public void onReconnect() {
                        LoadLoading();
                        PromotionRefresh();
                    }
                }, false);
            }
        };
        HttpMethods.getInstance().getPromotionInfo(new ProgressSubscriber(getStraightDownOnNext, _mActivity, false), 1, pageSize);
    }

    /**
     * 直降列表
     */
    private void Depreciate() {

        getPromotionOnNext = new SubscriberOnNextListener<List<GoodsListBean>>() {

            @Override
            public void onNext(List<GoodsListBean> goodsListBeans) {
                listInfo = goodsListBeans;

                if (page == 1) {
                    myAdapter1.setNewData(listInfo);
                } else {
                    myAdapter1.addData(listInfo);
                    page++;
                }
            }

            @Override
            public void onCompleted() {
                if (page == 1) {
                    refreshLayout.finishRefresh(10);
                } else {
                    refreshLayout.finishLoadmore(10);
                }
            }

            @Override
            public void onError() {
                if (page == 1) {
                    refreshLayout.finishRefresh(10);
                } else {
                    refreshLayout.finishLoadmore(10);
                }
            }

            @Override
            public void onSocketTimeout() {
                refreshLayout.finishRefresh(10);
                connectionFailed(new onReconnectInface() {

                    @Override
                    public void onReconnect() {
                        LoadLoading();
                        Depreciate();
                    }
                }, false);
            }

            @Override
            public void onConnectException() {
                refreshLayout.finishRefresh(10);
                connectionFailed(new onReconnectInface() {

                    @Override
                    public void onReconnect() {
                        LoadLoading();
                        Depreciate();
                    }
                }, false);
            }
        };

        HttpMethods.getInstance().getPromotionInfo(new ProgressSubscriber(getPromotionOnNext, _mActivity, false), page, pageSize);
    }


    /**
     * 获取首页轮播图
     */
    private void getBannerList(final String adPositionNo) {

        getBannerOnNext = new SubscriberOnNextListener<List<AdvBean>>() {

            @Override
            public void onNext(List<AdvBean> response) {

                switch (adPositionNo) {
                    case CommonConstant.HOME_PROMOTION:
                        advBeanList = response;
                        if (advBeanList != null && advBeanList.size() > 0) {
                            initBannerDate();
                        }
                        break;
                    case CommonConstant.HOME_RECOMMENT:
                        advRecommendList = response;
                        if (advRecommendList != null && advRecommendList.size() > 0) {
                            initRecommendDate();
                        }
                        break;
                }

            }

            @Override
            public void onCompleted() {
                refreshLayout.finishRefresh(10);
            }

            @Override
            public void onError() {
                refreshLayout.finishRefresh(10);
            }

            @Override
            public void onSocketTimeout() {
                refreshLayout.finishRefresh(10);
            }

            @Override
            public void onConnectException() {
                refreshLayout.finishRefresh(10);
            }
        };
        subscriber = new ProgressSubscriber(getBannerOnNext, _mActivity, false);
        HttpMethods.getInstance().getBannerList(subscriber, adPositionNo);
    }


    private void onAdvClick(AdvBean advBean) {
//        "广告属性---1：跳转H5  2：跳转商品  3：公告"
        switch (advBean.getAdType()) {
            case 1:
                EventBus.getDefault().post(new StartBrotherEvent(WebFragment.newInstance(advBean.getAdLink(), "详情")));
                break;
        }
    }


    /**
     * 竖向--直降
     */
    class MyAdapter1 extends BaseQuickAdapter<GoodsListBean, BaseViewHolder> {

        public MyAdapter1(List<GoodsListBean> data) {
            super(R.layout.item_left_up, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final GoodsListBean item) {

            helper.setVisible(R.id.left_linearLayout, true);
            helper.setVisible(R.id.right_linearLayout, false);
            helper.setText(R.id.product_name, item.getGoodsName());
            helper.setText(R.id.product_price, "￥" + item.getShopPrice());
            helper.setText(R.id.price_market, "￥" + item.getMarketPrice());
            ((TextView) helper.getView(R.id.price_market)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//刪除線
            MyImageLoader.getInstance().displayImage(_mActivity, RandomURL.getInstance().getRandomUrl(), (SquareImageView) helper.getView(R.id.product_img));

        }
    }

    /**
     * 横向--秒杀
     */
    class MyAdapter2 extends BaseQuickAdapter<GoodsListBean, BaseViewHolder> {

        public MyAdapter2(Integer viewId, List<GoodsListBean> data) {
            super(viewId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, GoodsListBean item) {
            helper.setText(R.id.product_name, item.getGoodsName());
            helper.setText(R.id.product_price, "￥" + item.getShopPrice());
            helper.setText(R.id.price_market, "￥" + item.getMarketPrice());
            ((TextView) helper.getView(R.id.price_market)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//刪除線
            MyImageLoader.getInstance().displayImage(_mActivity, RandomURL.getInstance().getRandomUrl(), (ImageView) helper.getView(R.id.product_img));
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.linearLayout_left_top:
                onAdvClick(advRecommendList.get(0));
                break;
            case R.id.linearLayout_right_top:
                onAdvClick(advRecommendList.get(1));
                break;
            case R.id.linearLayout_left_bottom:
                onAdvClick(advRecommendList.get(2));
                break;
            case R.id.linearLayout_right_bottom:
                onAdvClick(advRecommendList.get(3));
                break;
            case R.id.zxing_img:
                EventBus.getDefault().post(new StartBrotherEvent(NoticeFragment.newInstance()));
                break;
        }
    }

    public class BannerViewHolder implements MZViewHolder<String> {

        private ImageView mImageView;

        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
            mImageView = (ImageView) view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, String data) {
            MyImageLoader.getInstance().displayImage(_mActivity, data, mImageView);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mMZBanner.pause();//暂停轮播
    }

    @Override
    public void onResume() {
        super.onResume();
        mMZBanner.start();//开始轮播
    }

}
