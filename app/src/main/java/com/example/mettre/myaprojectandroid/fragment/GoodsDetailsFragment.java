package com.example.mettre.myaprojectandroid.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mettre.myaprojectandroid.R;
import com.example.mettre.myaprojectandroid.base.BaseMainFragment;
import com.example.mettre.myaprojectandroid.bean.GoodsDetailsBean;
import com.example.mettre.myaprojectandroid.http.HttpMethods3;
import com.example.mettre.myaprojectandroid.http.HttpResult3;
import com.example.mettre.myaprojectandroid.subscribers.ProgressSubscriber;
import com.example.mettre.myaprojectandroid.subscribers.SubscriberOnNextListener;
import com.example.mettre.myaprojectandroid.utils.BigDecimalUtils;
import com.example.mettre.myaprojectandroid.utils.MyImageLoader;
import com.example.mettre.myaprojectandroid.utils.StatusBarUtil;
import com.example.mettre.myaprojectandroid.view.GradationScrollView;
import com.example.mettre.myaprojectandroid.view.NoScrollListView;
import com.example.mettre.myaprojectandroid.view.ScrollViewContainer;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;

/**
 * 商品详情
 */
public class GoodsDetailsFragment extends BaseMainFragment implements GradationScrollView.ScrollViewListener {


    private long goodsId;
    private TextView goods_name,goods_brief_text,shop_price_text,market_price_text;
    private GoodsDetailsBean goodsDetailsBean;
    private SubscriberOnNextListener getGoodsDetailsNext;
    private Subscriber<HttpResult3> subscriber;

    private GradationScrollView scrollView;
    private ImageView iv;
    private RelativeLayout llTitle;
    private LinearLayout llOffset;
    private ImageView ivCollectSelect;//收藏选中
    private ImageView ivCollectUnSelect;//收藏未选中
    private ScrollViewContainer container;
    private TextView tvGoodTitle;

    private NoScrollListView nlvImgs;//图片详情
    private QuickAdapter<String> imgAdapter;
    private List<String> imgsUrl;

    private int height;
    private int width;

    public static GoodsDetailsFragment newInstance(long goodsId) {
        GoodsDetailsFragment goodsListFragment = new GoodsDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("goodsId", goodsId);
        goodsListFragment.setArguments(bundle);
        return goodsListFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_details, container, false);

        goodsId = getArguments().getLong("goodsId");
        initClickListener(view,R.id.back_ImageView,R.id.tv_good_detail_buy,R.id.tv_good_detail_shop_cart,R.id.iv_good_detail_shop,R.id.iv_good_detail_share);
        initView(view);

        getGoodsDetails(goodsId);
        //透明状态栏
//        StatusBarUtil.setTranslucentForImageView(_mActivity, llOffset);
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) llOffset.getLayoutParams();
        params1.setMargins(0, -StatusBarUtil.getStatusBarHeight(_mActivity) / 4, 0, 0);
        llOffset.setLayoutParams(params1);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv.getLayoutParams();
        params.height = getScreenHeight(_mActivity) * 2 / 3;
        iv.setLayoutParams(params);

        container = new ScrollViewContainer(_mActivity);

        initImgDatas();

        initListeners();
        return view;
    }


    public void initView(View view) {

        goods_name = view.findViewById(R.id.goods_name);
        goods_brief_text = view.findViewById(R.id.goods_brief_text);
        shop_price_text = view.findViewById(R.id.shop_price_text);
        market_price_text = view.findViewById(R.id.market_price_text);

        scrollView = view.findViewById(R.id.scrollview);
        iv = view.findViewById(R.id.iv_good_detail_img);
        llTitle = view.findViewById(R.id.ll_good_detail);
        llOffset = view.findViewById(R.id.ll_offset);
        ivCollectSelect = view.findViewById(R.id.iv_good_detai_collect_select);
        ivCollectUnSelect = view.findViewById(R.id.iv_good_detai_collect_unselect);
        container = view.findViewById(R.id.sv_container);
        tvGoodTitle = view.findViewById(R.id.tv_good_detail_title_good);
        nlvImgs = view.findViewById(R.id.nlv_good_detial_imgs);
    }


    private void initGoodsDetailsDate(){
        goods_name.setText(goodsDetailsBean.getGoodsName());
        goods_brief_text.setText(goodsDetailsBean.getGoodsBrief());
        shop_price_text.setText(BigDecimalUtils.wipeBigDecimalZero(goodsDetailsBean.getShopPrice()));
        market_price_text.setText(BigDecimalUtils.wipeBigDecimalZero(goodsDetailsBean.getMarketPrice()));
    }

    /**
     * 获取商品详细信息
     */
    private void getGoodsDetails(long goodsId) {

        getGoodsDetailsNext = new SubscriberOnNextListener<GoodsDetailsBean>() {

            @Override
            public void onNext(GoodsDetailsBean response) {

                goodsDetailsBean = response;
                initGoodsDetailsDate();
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
        subscriber = new ProgressSubscriber(getGoodsDetailsNext, _mActivity);
        HttpMethods3.getInstance().getGoodsDetails(subscriber, goodsId);
        }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.back_ImageView:
                pop();
                break;
            case R.id.tv_good_detail_buy:
                break;
            case R.id.tv_good_detail_shop_cart:
                break;
            case R.id.iv_good_detail_shop:
                break;
            case R.id.iv_good_detail_share:
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscriber != null && (!subscriber.isUnsubscribed())) {
            subscriber.unsubscribe();
        }
    }

    public int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    // TODO: 16/8/21 模拟图片假数据
    private void initImgDatas() {
        width = getScreenWidth(_mActivity);
        imgsUrl = new ArrayList<>();
        imgsUrl.add("https://img.alicdn.com/imgextra/i4/714288429/TB2dLhGaVXXXXbNXXXXXXXXXXXX-714288429.jpg");
        imgsUrl.add("https://img.alicdn.com/imgextra/i3/726966853/TB2vhJ6lXXXXXbJXXXXXXXXXXXX_!!726966853.jpg");
        imgsUrl.add("https://img.alicdn.com/imgextra/i4/2081314055/TB2FoTQbVXXXXbuXpXXXXXXXXXX-2081314055.png");
        imgAdapter = new QuickAdapter<String>(_mActivity, R.layout.adapter_good_detail_imgs) {
            @Override
            protected void convert(BaseAdapterHelper helper, String item) {
                ImageView iv = helper.getView(R.id.iv_adapter_good_detail_img);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv.getLayoutParams();
                params.width = width;
                params.height = width / 2;
                iv.setLayoutParams(params);
                MyImageLoader.getInstance().displayImageCen(_mActivity, item, iv, width, width / 2);
            }
        };
        imgAdapter.addAll(imgsUrl);
        nlvImgs.setAdapter(imgAdapter);
    }

    private void initListeners() {

        ViewTreeObserver vto = iv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llTitle.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                height = iv.getHeight();

                scrollView.setScrollViewListener(GoodsDetailsFragment.this);
            }
        });
    }

    /**
     * 滑动监听
     *
     * @param scrollView
     * @param x
     * @param y
     * @param oldx
     * @param oldy
     */
    @Override
    public void onScrollChanged(GradationScrollView scrollView, int x, int y,
                                int oldx, int oldy) {
        // TODO Auto-generated method stub
        if (y <= 0) {   //设置标题的背景颜色
            llTitle.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
        } else if (y > 0 && y <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) y / height;
            float alpha = (255 * scale);
            tvGoodTitle.setTextColor(Color.argb((int) alpha, 1, 24, 28));
            llTitle.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
        } else {    //滑动到banner下面设置普通颜色
            llTitle.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
        }
    }

}
