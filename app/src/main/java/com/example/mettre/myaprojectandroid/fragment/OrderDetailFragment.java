package com.example.mettre.myaprojectandroid.fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.example.mettre.myaprojectandroid.R;
import com.example.mettre.myaprojectandroid.base.BaseMainFragment;
import com.example.mettre.myaprojectandroid.bean.OrderBean;
import com.example.mettre.myaprojectandroid.http.HttpMethods;
import com.example.mettre.myaprojectandroid.subscribers.ProgressSubscriber;
import com.example.mettre.myaprojectandroid.subscribers.SubscriberOnNextListener;
import com.example.mettre.myaprojectandroid.utils.BigDecimalUtils;
import com.example.mettre.myaprojectandroid.utils.MyImageLoader;
import com.example.mettre.myaprojectandroid.utils.RandomURL;
import com.example.mettre.myaprojectandroid.utils.ToastUtils;
import com.example.mettre.myaprojectandroid.view.SquareImageView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by app on 2017/8/30.
 * 订单详情
 */

public class OrderDetailFragment extends BaseMainFragment {

    private Toolbar mToolbar;
    private Long orderId;
    private BigDecimal moneyAmount;
    private RefreshLayout refreshLayout;

    /**
     * 收货人信息
     */
    private TextView address_name;
    private TextView address_phone;
    private TextView address_details;

    /**
     * 订单内商品列表
     */
    private LinearLayout order_goods_linearLayout;
    /**
     * 订单详情
     */
    private RelativeLayout address_RelativeLayout;
    private TextView order_type;
    private TextView order_shop_name;
    private TextView order_total_pay;
    private TextView goods_price;
    private TextView cash_price;
    private TextView order_postage;
    private TextView order_number;
    private TextView order_create_time;
    private LinearLayout bottom_LinearLayout;
    private TextView order_btn_left;
    private TextView order_btn_right;
    private TextView order_btn_three;
    private Subscriber subscriber;
    private SubscriberOnNextListener getOrderDetailNext;

    public static OrderDetailFragment newInstance(Long orderId) {
        OrderDetailFragment fragment = new OrderDetailFragment();
        Bundle args = new Bundle();
        args.putLong("orderId", orderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        orderId = getArguments().getLong("orderId");
        mToolbar = view.findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.oil));
        initToolbarNav(mToolbar);
        initLoadView(false, view);
        mToolbar.setTitle("订单详情");

        refreshLayout = view.findViewById(R.id.refreshLayout);
        address_RelativeLayout = view.findViewById(R.id.address_RelativeLayout);
        address_name = view.findViewById(R.id.address_name);
        address_phone = view.findViewById(R.id.address_phone);
        address_details = view.findViewById(R.id.address_details);
        order_type = view.findViewById(R.id.order_type);
        order_total_pay = view.findViewById(R.id.order_total_pay);
        order_shop_name = view.findViewById(R.id.order_shop_name);
        goods_price = view.findViewById(R.id.goods_price);
        cash_price = view.findViewById(R.id.cash_price);
        order_postage = view.findViewById(R.id.order_postage);
        order_number = view.findViewById(R.id.order_number);
        order_create_time = view.findViewById(R.id.order_create_time);
        order_btn_left = view.findViewById(R.id.order_btn_left);
        bottom_LinearLayout = view.findViewById(R.id.bottom_LinearLayout);
        order_btn_right = view.findViewById(R.id.order_btn_right);
        order_btn_three = view.findViewById(R.id.order_btn_three);
        order_goods_linearLayout = view.findViewById(R.id.order_goods_linearLayout);
        initClickListener(view, R.id.order_btn_left, R.id.order_btn_right, R.id.order_btn_three, R.id.copy_number);

        getOrderDetails(orderId);
        setRefresh();
    }

    /**
     * 添加刷新
     */
    private void setRefresh() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getOrderDetails(orderId);
            }
        });
    }

    /**
     * 订单详情
     */
    private void getOrderDetails(final Long orderId) {
        getOrderDetailNext = new SubscriberOnNextListener<OrderBean>() {
            @Override
            public void onNext(OrderBean response) {
                initOrderView(response);
            }

            @Override
            public void onCompleted() {
                hasDate();
                refreshLayout.finishRefresh(10);
            }

            @Override
            public void onError() {
                refreshLayout.finishRefresh(10);
            }

            @Override
            public void onSocketTimeout() {
                refreshLayout.finishRefresh(10);
                connectionFailed(new onReconnectInface() {

                    @Override
                    public void onReconnect() {
                        getOrderDetails(orderId);
                    }
                }, false);
            }

            @Override
            public void onConnectException() {
                refreshLayout.finishRefresh(10);
                connectionFailed(new onReconnectInface() {

                    @Override
                    public void onReconnect() {
                        getOrderDetails(orderId);
                    }
                }, false);
            }
        };
        subscriber = new ProgressSubscriber(getOrderDetailNext, _mActivity);
        HttpMethods.getInstance().getOrderDetail(new ProgressSubscriber(getOrderDetailNext, _mActivity), orderId);
    }


    /**
     * 订单内商品动态布局
     */
    private void DynamicLayoutView(List<OrderBean.ListBean> orderItems) {
        order_goods_linearLayout.removeAllViews();
        for (OrderBean.ListBean orderItem : orderItems) {
            order_goods_linearLayout.addView(addView(orderItem));
        }
    }

    /**
     * 动态添加商品view
     *
     * @return
     */
    private View addView(final OrderBean.ListBean orderItem) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LayoutInflater inflater = LayoutInflater.from(_mActivity);
        View view = inflater.inflate(R.layout.order_good_item, null);
        SquareImageView order_good_img = (SquareImageView) view.findViewById(R.id.product_picture);
        TextView tv_product_desc = (TextView) view.findViewById(R.id.tv_product_desc);
        TextView product_price = (TextView) view.findViewById(R.id.product_price);
        TextView product_quantity = (TextView) view.findViewById(R.id.product_quantity);
        TextView product_mark_price = (TextView) view.findViewById(R.id.product_mark_price);

        product_mark_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        MyImageLoader.getInstance().displayImage(_mActivity, RandomURL.getInstance().getRandomUrl(), order_good_img);
        tv_product_desc.setText(orderItem.getGoodsName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(GoodsDetailsFragment.newInstance(orderItem.getGoodsId()));
            }
        });

        product_price.setText("￥" + orderItem.getOrderGoodsPrice());
        product_quantity.setText("x " + orderItem.getQuantity());
        view.setLayoutParams(lp);
        return view;

    }

    private void initOrderView(OrderBean response) {

        DynamicLayoutView(response.getOrderItem());
        String orderType = "";
        //订单状态 0:已取消  10:未付款  20:已支付  30:已发货  40:交易成功  50:交易关闭
        switch (response.getStatus()) {
            case 10:
                orderType = "等待付款";
                order_btn_left.setVisibility(View.VISIBLE);
                order_btn_left.setText("取消订单");
                order_btn_right.setVisibility(View.GONE);
                order_btn_three.setVisibility(View.VISIBLE);
                order_btn_three.setText("付款");
                break;
            case 20:
                orderType = "等待发货";
                order_btn_left.setVisibility(View.VISIBLE);
                order_btn_left.setText("取消订单");
                order_btn_right.setVisibility(View.VISIBLE);
                order_btn_right.setText("提醒发货");
                order_btn_right.setTextColor(getResources().getColor(R.color.monsoon));
                order_btn_right.setBackground(getResources().getDrawable(R.drawable.bg_orange_line));
                break;
            case 30:
                orderType = "卖家已发货";
                order_btn_left.setVisibility(View.VISIBLE);
                order_btn_right.setText("申请售后");
                order_btn_left.setText("查看物流");
                order_btn_right.setVisibility(View.VISIBLE);
                order_btn_three.setVisibility(View.VISIBLE);
                order_btn_three.setText("确认收货");
                break;
            case 40:
                orderType = "交易成功";
                order_btn_left.setVisibility(View.VISIBLE);
                order_btn_left.setText("评价");
                order_btn_right.setVisibility(View.VISIBLE);
                order_btn_right.setText("申请售后");
                order_btn_left.setTextColor(getResources().getColor(R.color.white));
                order_btn_left.setBackground(getResources().getDrawable(R.drawable.bg_dark_red));
                break;
            case 0:
                bottom_LinearLayout.setVisibility(View.GONE);
                order_btn_left.setVisibility(View.GONE);
                order_btn_right.setVisibility(View.GONE);
                orderType = "交易取消";
                break;
            case 50:
                bottom_LinearLayout.setVisibility(View.GONE);
                order_btn_left.setVisibility(View.GONE);
                order_btn_right.setVisibility(View.GONE);
                orderType = "交易关闭";
                break;
        }

        address_RelativeLayout.setVisibility(View.VISIBLE);

        moneyAmount = response.getPayment();
        address_name.setText(response.getRecipientName());
        address_phone.setText(response.getRecipientPhoneNumber());
        address_details.setText(response.getRecipientAddress());

        order_type.setText(orderType);
        order_shop_name.setText(response.getBrandName());
        order_total_pay.setText(BigDecimalUtils.wipeBigDecimalZero(response.getPayment()));
        cash_price.setText(BigDecimalUtils.wipeBigDecimalZero(response.getPayment()));
        goods_price.setText(BigDecimalUtils.wipeBigDecimalZero(response.getOrderPrice()));
        order_postage.setText(BigDecimalUtils.wipeBigDecimalZero(response.getPostage()));
        order_number.setText("订单编号：" + response.getOrderNo());
        order_create_time.setText("创建时间：" + response.getCreationTime());

    }


    /**
     * 订单下部btn的监听
     */
    private void onBtnClick(String str) {
        switch (str) {
            case "付款":
                ArrayList<Long> order = new ArrayList<>();
                order.add(orderId);
//                WXPayEntryActivity.startActivity(_mActivity, moneyAmount, 1, order, 5);
                break;
            case "提醒发货":
                new SweetAlertDialog(_mActivity, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("")
                        .setContentText("已向买家提醒发货，卖家会尽快发货")
                        .setConfirmText("关闭")
                        .show();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_btn_left:
            case R.id.order_btn_right:
                onBtnClick(((TextView) v).getText().toString());
                break;
            case R.id.order_btn_three:
                onBtnClick(((TextView) v).getText().toString());
                break;
            case R.id.copy_number:
                ClipboardManager cm = (ClipboardManager) _mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(order_number.getText());
                ToastUtils.showCenterToast("复制成功", 200);
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

}
