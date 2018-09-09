package com.example.mettre.myaprojectandroid.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mettre.myaprojectandroid.R;
import com.example.mettre.myaprojectandroid.base.BaseMainFragment;
import com.example.mettre.myaprojectandroid.bean.AdvBean;
import com.example.mettre.myaprojectandroid.constant.CommonConstant;
import com.example.mettre.myaprojectandroid.event.StartBrotherEvent;
import com.example.mettre.myaprojectandroid.http.HttpMethods;
import com.example.mettre.myaprojectandroid.http.HttpResult5;
import com.example.mettre.myaprojectandroid.subscribers.ProgressSubscriber;
import com.example.mettre.myaprojectandroid.subscribers.SubscriberOnNextListener;
import com.example.mettre.myaprojectandroid.utils.MyImageLoader;
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

    public static LeftFragment newInstance() {
        return new LeftFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_left, container, false);
        initClickListener(view, R.id.linearLayout_left_top, R.id.linearLayout_right_top, R.id.linearLayout_left_bottom, R.id.linearLayout_right_bottom);
        initView(view);
        getBannerList(CommonConstant.HOME_PROMOTION);
        getBannerList(CommonConstant.HOME_RECOMMENT);
        return view;
    }

    public void initView(View view) {
        mMZBanner = view.findViewById(R.id.banner);
        title_left_top = view.findViewById(R.id.title_left_top);
        title_right_top = view.findViewById(R.id.title_right_top);
        title_left_bottom = view.findViewById(R.id.title_left_bottom);
        title_right_bottom = view.findViewById(R.id.title_right_bottom);
        describe_left_top = view.findViewById(R.id.describe_left_top);
        describe_right_top = view.findViewById(R.id.describe_right_top);
        describe_left_bottom = view.findViewById(R.id.describe_left_bottom);
        describe_right_bottom = view.findViewById(R.id.describe_right_bottom);
        image_left_top = view.findViewById(R.id.image_left_top);
        image_right_top = view.findViewById(R.id.image_right_top);
        imageView_left_bottom = view.findViewById(R.id.imageView_left_bottom);
        imageView_right_bottom = view.findViewById(R.id.imageView_right_bottom);
        linearLayout_left_top = view.findViewById(R.id.linearLayout_left_top);
        linearLayout_right_top = view.findViewById(R.id.linearLayout_right_top);
        linearLayout_left_bottom = view.findViewById(R.id.linearLayout_left_bottom);
        linearLayout_right_bottom = view.findViewById(R.id.linearLayout_right_bottom);

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
