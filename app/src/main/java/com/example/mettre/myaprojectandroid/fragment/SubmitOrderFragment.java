package com.example.mettre.myaprojectandroid.fragment;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.example.mettre.myaprojectandroid.R;
import com.example.mettre.myaprojectandroid.adapter.OrderExpandableListViewAdapter;
import com.example.mettre.myaprojectandroid.base.BaseMainFragment;
import com.example.mettre.myaprojectandroid.bean.AddressBean;
import com.example.mettre.myaprojectandroid.bean.OrderRequestBean;
import com.example.mettre.myaprojectandroid.bean.SubmitOrderGroup;
import com.example.mettre.myaprojectandroid.constant.CommonConstant;
import com.example.mettre.myaprojectandroid.event.StartBrotherEvent;
import com.example.mettre.myaprojectandroid.http.HttpMethods;
import com.example.mettre.myaprojectandroid.http.HttpResult3;
import com.example.mettre.myaprojectandroid.subscribers.ProgressSubscriber;
import com.example.mettre.myaprojectandroid.subscribers.SubscriberOnNextListener;
import com.example.mettre.myaprojectandroid.utils.BigDecimalUtils;
import com.example.mettre.myaprojectandroid.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by app on 2017/8/30.
 * 填写订单
 */
public class SubmitOrderFragment extends BaseMainFragment {

    private Toolbar mToolbar;
    private BigDecimal totalRMB = new BigDecimal(0);//总支付部分--现金
    private BigDecimal totalOrderPrice = new BigDecimal(0);//订单总额
    private BigDecimal totalDriver = new BigDecimal(0);//总运费
    private BigDecimal totalGoodsPrice = new BigDecimal(0);//商品总价
    private int type;//1：商品直接购买  2：购物车购买
    /**
     * 提交订单
     */
    private TextView price_total;
    private AddressBean addressBean;
    private ProgressSubscriber<HttpResult3> subscriber;
    private SubscriberOnNextListener getTopMovieOnNext;
    private List<SubmitOrderGroup> orderVMList;
    private List<OrderRequestBean> orderItems = new ArrayList<>();

    /**
     * 默认收货地址
     */
    private SubscriberOnNextListener getAddAddressListNext;

    /**
     * 订单项
     */
    private View headView;
    private View footView;
    private TextView order_total;
    private TextView price_driver;
    private TextView goods_total;
    private LinearLayout address_yes;
    private LinearLayout address_no;
    private ExpandableListView exListView;
    private OrderExpandableListViewAdapter selva;

    private TextView address_name;
    private TextView address_phone;
    private TextView address_normal;
    private TextView address_details;

    public static SubmitOrderFragment newInstance(List<SubmitOrderGroup> orderVMList, int type) {
        SubmitOrderFragment fragment = new SubmitOrderFragment();
        Bundle args = new Bundle();
        args.putSerializable("orderVMList", (Serializable) orderVMList);
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_submit_order, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        initClickListener(view, R.id.submit_order_order);

        addressBean = new AddressBean();

        type = getArguments().getInt("type");
        orderVMList = (List<SubmitOrderGroup>) getArguments().getSerializable("orderVMList");
        exListView = view.findViewById(R.id.exListView);
        price_total = view.findViewById(R.id.price_total);
        mToolbar = view.findViewById(R.id.toolbar);

        initToolbarNav(mToolbar);
        mToolbar.setTitle("提交订单");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.oil));
        subRMB(orderVMList);
        subOrderPrice(orderVMList);
        subOrderDriver(orderVMList);
        subOrderGoodsPrice();
        price_total.setText(BigDecimalUtils.wipeBigDecimalZero(totalRMB));
        initEvents();

        address_yes.setVisibility(View.GONE);
        address_no.setVisibility(View.VISIBLE);
        getDefaultAddresses();
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {//收货地址回来
            if (2000 == requestCode) {
                AddressBean addressBean = (AddressBean) data.getSerializable("addressBean");
                AddressBack(addressBean.getName(), addressBean.getPhone(), addressBean.getAddress(), addressBean.isDefaults());
            }
        }
    }

    /**
     * 多个订单总价--现金
     */
    private void subRMB(List<SubmitOrderGroup> orderVMList) {
        totalRMB = new BigDecimal(0);
        for (SubmitOrderGroup o : orderVMList) {
//            totalRMB += (o.getPayAmount());
            totalRMB = totalRMB.add(o.getUserAllPrice());
        }
    }

    /**
     * 多个订单总价--全部
     */
    private void subOrderPrice(List<SubmitOrderGroup> orderVMList) {
        totalOrderPrice = new BigDecimal(0);
        for (SubmitOrderGroup o : orderVMList) {
//            totalOrderPrice += (o.getTotalAmount());
            totalOrderPrice = totalOrderPrice.add(o.getUserAllPrice());
        }
    }

    /**
     * 多个订单总运费
     */
    private void subOrderDriver(List<SubmitOrderGroup> orderVMList) {
        totalDriver = new BigDecimal(0);
        for (SubmitOrderGroup o : orderVMList) {
//            totalDriver += (o.getPostage());
            totalDriver = totalDriver.add(o.getPostage());
        }
    }

    /**
     * 多个订单商品总价
     */
    private void subOrderGoodsPrice() {
        for (SubmitOrderGroup o : orderVMList) {
            totalGoodsPrice = totalGoodsPrice.add(o.getGoodsTotal());
        }
    }

    /**
     * 提交订单
     */
    private void placeOrder(List<OrderRequestBean> orderItems) {
        getTopMovieOnNext = new SubscriberOnNextListener<HttpResult3>() {

            @Override
            public void onNext(HttpResult3 response) {

                new SweetAlertDialog(_mActivity, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("订单提交成功!")
                        .setConfirmText("关闭")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                EventBus.getDefault().post(new StartBrotherEvent(CommonConstant.REFRESH_CART));
                                pop();
                            }
                        })
                        .show();
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
        subscriber = new ProgressSubscriber(getTopMovieOnNext, _mActivity);
        HttpMethods.getInstance().submitOrder(new ProgressSubscriber(getTopMovieOnNext, _mActivity), orderItems);
    }

    /**
     * 提交订单头尾布局
     *
     * @param exListView
     */
    private void setHeadFootView(ExpandableListView exListView) {
        headView = LayoutInflater.from(_mActivity).inflate(R.layout.place_order_headview, null);
        footView = LayoutInflater.from(_mActivity).inflate(R.layout.place_order_footview, null);

        address_yes = (LinearLayout) headView.findViewById(R.id.address_yes);
        address_no = (LinearLayout) headView.findViewById(R.id.address_no);
        address_name = (TextView) headView.findViewById(R.id.address_name);
        address_phone = (TextView) headView.findViewById(R.id.address_phone);
        address_normal = (TextView) headView.findViewById(R.id.address_normal);
        address_details = (TextView) headView.findViewById(R.id.address_details);

        order_total = (TextView) footView.findViewById(R.id.order_total);
        price_driver = (TextView) footView.findViewById(R.id.price_driver);
        goods_total = (TextView) footView.findViewById(R.id.goods_total);
        price_driver.setText(BigDecimalUtils.wipeBigDecimalZero(totalDriver));
        goods_total.setText(BigDecimalUtils.wipeBigDecimalZero(totalGoodsPrice));
        order_total.setText(BigDecimalUtils.wipeBigDecimalZero(totalOrderPrice));

        exListView.addHeaderView(headView);
        exListView.addFooterView(footView);

        address_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startForResult(AddressListFragment.newInstance(true), 2000);

            }
        });
        address_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startForResult(AddressListFragment.newInstance(true), 2000);
            }
        });
    }

    /**
     * 选择出默认的收货地址
     */
    private void getDefaultAddresses() {

        getAddAddressListNext = new SubscriberOnNextListener<List<AddressBean>>() {

            @Override
            public void onNext(List<AddressBean> response) {
                for (AddressBean listBean : response) {
                    if (listBean.isDefaults()) {
                        String name = listBean.getName();
                        String phone = listBean.getPhone();
                        String address = listBean.getProvince() + listBean.getCity() + listBean.getCounty() + listBean.getAddress();
                        boolean defaulted = listBean.isDefaults();
                        AddressBack(name, phone, address, defaulted);
                        break;
                    }
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
        HttpMethods.getInstance().AddressList(new ProgressSubscriber(getAddAddressListNext, _mActivity), 1, 100);
    }


    /**
     * 地址选择完成回调
     */
    private void AddressBack(String name, String phone, String address, boolean defaulted) {
        exListView.removeHeaderView(headView);
        address_name.setText(name);
        address_phone.setText(phone);
        address_details.setText(address);
        if (defaulted) {
            address_normal.setVisibility(View.VISIBLE);
        } else {
            address_normal.setVisibility(View.GONE);
        }
        address_yes.setVisibility(View.VISIBLE);
        address_no.setVisibility(View.GONE);
        exListView.addHeaderView(headView);
        addressBean.setName(name);
        addressBean.setPhone(phone);
        addressBean.setAddress(address);
    }

    /**
     * setAdapter
     */

    private void initEvents() {
        selva = new OrderExpandableListViewAdapter(orderVMList, _mActivity);
        exListView.setAdapter(selva);
        setHeadFootView(exListView);

        for (int i = 0; i < selva.getGroupCount(); i++) {
            exListView.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_order_order:
                if (addressBean.getAddress() == null && address_no.getVisibility() == View.VISIBLE) {
                    ToastUtils.showCenterToast("请选择收货地址", 200);
                    return;
                }

                orderVMList = selva.getOrderVMList();

                for (SubmitOrderGroup submitOrderGroup : orderVMList) {
                    OrderRequestBean orderRequestBean = new OrderRequestBean();
                    orderRequestBean.setBrandId(submitOrderGroup.getBrandId());
                    orderRequestBean.setBuyerMessage(submitOrderGroup.getBuyerMessage());
                    orderRequestBean.setGoodsTotal(submitOrderGroup.getGoodsTotal());
                    orderRequestBean.setPostage(submitOrderGroup.getPostage());
                    orderRequestBean.setUserAllPrice(submitOrderGroup.getUserAllPrice());
                    orderRequestBean.setRecipientName(addressBean.getName());
                    orderRequestBean.setRecipientPhoneNumber(addressBean.getPhone());
                    orderRequestBean.setRecipientAddress(addressBean.getAddress());
                    List<OrderRequestBean.GoodsItem> goodsItems = new ArrayList<>();
                    for (SubmitOrderGroup.GoodsItem g : submitOrderGroup.getGoodsItems()) {
                        OrderRequestBean.GoodsItem goodsItem = new OrderRequestBean.GoodsItem();
                        goodsItem.setGoodsId(g.getGoodsId());
                        goodsItem.setCartId(g.getCartId());
                        goodsItem.setGoodsNumber(g.getGoodsNumber());
                        goodsItems.add(goodsItem);
                    }
                    orderRequestBean.setGoodsItems(goodsItems);
                    orderItems.add(orderRequestBean);

                }
                placeOrder(orderItems);
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
