package com.example.mettre.myaprojectandroid.fragment;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mettre.myaprojectandroid.R;
import com.example.mettre.myaprojectandroid.adapter.ShopCartExpandableListViewAdapter;
import com.example.mettre.myaprojectandroid.app.MyApplication;
import com.example.mettre.myaprojectandroid.base.BaseMainFragment;
import com.example.mettre.myaprojectandroid.bean.CartBean;
import com.example.mettre.myaprojectandroid.bean.CartGoodsItem;
import com.example.mettre.myaprojectandroid.bean.OrderRequestBean;
import com.example.mettre.myaprojectandroid.bean.SubmitOrderGroup;
import com.example.mettre.myaprojectandroid.constant.CommonConstant;
import com.example.mettre.myaprojectandroid.event.StartBrotherEvent;
import com.example.mettre.myaprojectandroid.http.HttpMethods;
import com.example.mettre.myaprojectandroid.http.HttpResult3;
import com.example.mettre.myaprojectandroid.http.HttpResult5;
import com.example.mettre.myaprojectandroid.subscribers.ProgressSubscriber;
import com.example.mettre.myaprojectandroid.subscribers.SubscriberOnNextListener;
import com.example.mettre.myaprojectandroid.utils.BigDecimalUtils;
import com.example.mettre.myaprojectandroid.utils.ConstantUtil;
import com.example.mettre.myaprojectandroid.utils.SharedPrefsUtil;
import com.example.mettre.myaprojectandroid.utils.Utils;
import com.example.mettre.myaprojectandroid.view.SuperExpandableListView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rx.Subscriber;

public class CartFragment extends BaseMainFragment implements ShopCartExpandableListViewAdapter.CheckInterface, ShopCartExpandableListViewAdapter.ModifyCountInterface, ShopCartExpandableListViewAdapter.DeleteProductInterface, ShopCartExpandableListViewAdapter.ChildItemInterface {

    private CheckBox cb_check_all;
    private TextView tv_total_price;
    private TextView tv_go_to_pay;
    /**
     * 购物车列表
     */
    private SuperExpandableListView exListView;
    private List<CartBean> cartList = new ArrayList<>();
    private List<SubmitOrderGroup> orderVMList = new ArrayList<>();//购物车提交数据给订单
    private SubscriberOnNextListener getCartListNext;
    private Subscriber<HttpResult5> subscriber;
    private ShopCartExpandableListViewAdapter selva;
    private RefreshLayout refreshLayout;

    /**
     * 删除购物车
     */
    private SubscriberOnNextListener deleteCartInfoNext;

    /**
     * 编辑购物车数量
     */
    private SubscriberOnNextListener editQuantityInfoNext;

    private BigDecimal totalPrice = new BigDecimal(0.00);// 购买的商品总价
    private int totalCount = 0;// 购买的不同商品总数量

    /**
     * 购物车空布局
     */
    private LinearLayout bottomView;


    public static CartFragment newInstance() {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        EventBus.getDefault().register(this);
        initView(view);
        getAddCartInfo();
        setRefresh();
        return view;
    }

    private void initView(View view) {

        refreshLayout = view.findViewById(R.id.refreshLayout);
        exListView = view.findViewById(R.id.exListView);
        cb_check_all = view.findViewById(R.id.all_chekbox);
        tv_total_price = view.findViewById(R.id.price_total);
        tv_go_to_pay = view.findViewById(R.id.tv_go_to_pay);
        bottomView = view.findViewById(R.id.bottom_view);
        initLoadView(true, view);

        initClickListener(view, R.id.all_chekbox, R.id.tv_go_to_pay, R.id.price_total, R.id.go_home);
    }

    /**
     * 购物车列表
     */
    private void getAddCartInfo() {

        getCartListNext = new SubscriberOnNextListener<List<CartBean>>() {

            @Override
            public void onNext(List<CartBean> response) {
                cartList = response;
                if (cartList != null && cartList.size() > 0) {
                    bottomView.setVisibility(View.VISIBLE);
                    hasDate();
                    initEvents();
                } else {
                    LoadEmpty("购物车还是空的", "去添加你喜欢的商品吧");
                    bottomView.setVisibility(View.GONE);
                }
                calculate();
            }

            @Override
            public void onCompleted() {
                refreshLayout.finishRefresh(10);
            }

            @Override
            public void onError() {
                LoadError();
                bottomView.setVisibility(View.GONE);
                refreshLayout.finishRefresh(10);
            }

            @Override
            public void onSocketTimeout() {
                bottomView.setVisibility(View.GONE);
                refreshLayout.finishRefresh(10);
                connectionFailed(new onReconnectInface() {

                    @Override
                    public void onReconnect() {
                        getAddCartInfo();
                    }
                }, true);
            }

            @Override
            public void onConnectException() {
                bottomView.setVisibility(View.GONE);
                refreshLayout.finishRefresh(10);
                connectionFailed(new onReconnectInface() {

                    @Override
                    public void onReconnect() {
                        getAddCartInfo();
                    }
                }, true);
            }
        };
        Long timestamp = Long.parseLong(SharedPrefsUtil.getValue(Utils.getContext(), ConstantUtil.TIMESTAMP, String.valueOf(new Random().nextInt(10000))));
        subscriber = new ProgressSubscriber(getCartListNext, _mActivity, false);
        HttpMethods.getInstance().getCartListInfo(subscriber, timestamp);
    }

    /**
     * 删除购物车请求
     */
    private void deleteCartGoods(final int groupPosition, final int childPosition) {
        deleteCartInfoNext = new SubscriberOnNextListener<HttpResult3>() {

            @Override
            public void onNext(HttpResult3 response) {

                cartList.get(groupPosition).getGoodsItem().remove(childPosition);
                if (cartList.get(groupPosition).getGoodsItem().size() == 0) {//child没有了，group也就没有了
                    cartList.remove(groupPosition);
                } else {
                    for (CartGoodsItem c : cartList.get(groupPosition).getGoodsItem()) {
                        if (cartList.get(groupPosition).isSelected() && !c.isSelected()) {
                            cartList.get(groupPosition).setSelected(false);
                            break;
                        }
                    }
                }

                if (cartList.size() == 0) {
                    LoadEmpty("购物车还是空的", "去添加你喜欢的商品吧");
                    selva.notifyDataSetChanged();
                    bottomView.setVisibility(View.GONE);
                    calculate();
                    return;
                }

                calculate();
                selva.notifyDataSetChanged();
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
        List<Long> cartIds = new ArrayList<>();
        cartIds.add(cartList.get(groupPosition).getGoodsItem().get(childPosition).getCartId());
        HttpMethods.getInstance().deleteCart(new ProgressSubscriber(deleteCartInfoNext, _mActivity), cartIds);
    }

    /**
     * 编辑购物车指定商品数量
     */
    private void EditQuantityCartGoods(final int groupPosition, final int childPosition, final View showCountView, final int currentCount, final Long goodsId) {
        editQuantityInfoNext = new SubscriberOnNextListener<HttpResult3>() {

            @Override
            public void onNext(HttpResult3 response) {
                CartGoodsItem product = (CartGoodsItem) selva.getChild(groupPosition, childPosition);
                product.setCartNumber(currentCount);
                ((TextView) showCountView).setText(currentCount + "");
                selva.notifyDataSetChanged();
                calculate();

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
        Long timestamp = Long.parseLong(SharedPrefsUtil.getValue(Utils.getContext(), ConstantUtil.TIMESTAMP, String.valueOf(new Random().nextInt(10000))));
        List<Long> cartItemIds = new ArrayList<>();
        cartItemIds.add(cartList.get(groupPosition).getGoodsItem().get(childPosition).getCartId());
        HttpMethods.getInstance().editCartNum(new ProgressSubscriber(editQuantityInfoNext, _mActivity), timestamp, goodsId, currentCount);
    }


    /**
     * 添加刷新 --购物车
     */
    private void setRefresh() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getAddCartInfo();
                cb_check_all.setChecked(false);
                tv_total_price.setText(BigDecimalUtils.wipeBigDecimalZero(totalPrice));
                tv_go_to_pay.setText("去结算(0)");
                if (cartList == null || cartList.size() <= 0) {
                    return;
                }
                for (int i = 0; i < cartList.size(); i++) {
                    cartList.get(i).setSelected(false);
                    for (int j = 0; j < cartList.get(i).getGoodsItem().size(); j++) {
                        cartList.get(i).getGoodsItem().get(j).setSelected(false);
                    }
                }
            }
        });
    }

    private void initEvents() {
        selva = new ShopCartExpandableListViewAdapter(cartList, _mActivity);
        selva.setCheckInterface(this);// 关键步骤1,设置复选框接口
        selva.setModifyCountInterface(this);// 关键步骤2,设置数量增减接口
        selva.setDeleteProductInterface(this);// 关键步骤3,购物车删除接口
        selva.setChildItemInterface(this);// 关键步骤4,购物车商品点击接口
        exListView.setAdapter(selva);

        for (int i = 0; i < selva.getGroupCount(); i++) {
            exListView.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
        }

        cb_check_all.setOnClickListener(this);
        tv_go_to_pay.setOnClickListener(this);
    }

    @Override
    public void checkGroup(int groupPosition, boolean isChecked) {
        CartBean group = cartList.get(groupPosition);
        List<CartGoodsItem> childs = group.getGoodsItem();
        for (int i = 0; i < childs.size(); i++) {
            if (childs.get(i).getStock() >= childs.get(i).getCartNumber()) {
                childs.get(i).setSelected(isChecked);
            }
        }
        if (isAllCheck())
            cb_check_all.setChecked(true);
        else
            cb_check_all.setChecked(false);
        selva.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void checkChild(int groupPosition, int childPosition, boolean isChecked) {
        boolean allChildSameState = true;// 判断改组下面的所有子元素是否是同一种状态
        CartBean group = cartList.get(groupPosition);
        List<CartGoodsItem> childs = group.getGoodsItem();
        for (int i = 0; i < childs.size(); i++) {
            if (childs.get(i).getStock() >= childs.get(i).getCartNumber() && childs.get(i).isSelected() != isChecked) {
                allChildSameState = false;
                break;
            }
        }
        if (allChildSameState) {
            group.setSelected(isChecked);// 如果所有子元素状态相同，那么对应的组元素被设为这种统一状态
        } else {
            group.setSelected(false);// 否则，组元素一律设置为未选中状态
        }

        if (isAllCheck())
            cb_check_all.setChecked(true);
        else
            cb_check_all.setChecked(false);
        selva.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        CartGoodsItem product = (CartGoodsItem) selva.getChild(groupPosition, childPosition);
        int currentCount = product.getCartNumber();
        currentCount++;
        EditQuantityCartGoods(groupPosition, childPosition, showCountView, currentCount, product.getGoodsId());
    }

    @Override
    public void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        CartGoodsItem product = (CartGoodsItem) selva.getChild(groupPosition, childPosition);
        int currentCount = product.getCartNumber();
        if (currentCount == 1)
            return;

        currentCount--;
        EditQuantityCartGoods(groupPosition, childPosition, showCountView, currentCount, product.getGoodsId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_chekbox:
                doCheckAll();
                break;
            case R.id.tv_go_to_pay:
                if (TextUtils.isEmpty(MyApplication.getInstances().getToken())) {
                    EventBus.getDefault().post(new StartBrotherEvent(LoginFragment.newInstance()));
                } else {
                    if (totalCount == 0) {
                        Toast.makeText(_mActivity, "请选择要支付的商品", Toast.LENGTH_LONG).show();
                        return;
                    }
                    createOrder(cartList);
                }
                break;
            case R.id.go_home:
                EventBus.getDefault().post(new StartBrotherEvent(CommonConstant.BACK_HOME));
                break;
            case R.id.price_total:

                break;

        }
    }

    /**
     * start other BrotherFragment
     */
    @Subscribe
    public void startBrother(StartBrotherEvent event) {
        if (event.EventType == CommonConstant.REFRESH_CART) {
            refreshLayout.autoRefresh();
        }
    }

    /**
     * 购物车创建订单
     */
    private void createOrder(List<CartBean> cartList) {
        EventBus.getDefault().post(new StartBrotherEvent(SubmitOrderFragment.newInstance(setOrderVMList(cartList), 2)));

    }

    /**
     * 填写订单数据结构
     */
    private SubmitOrderGroup.GoodsItem setOrderListDate(CartGoodsItem cartItemsBean) {
        SubmitOrderGroup.GoodsItem orderItemsBean = new SubmitOrderGroup.GoodsItem();
        orderItemsBean.setGoodsId(cartItemsBean.getGoodsId());
        orderItemsBean.setCartId(cartItemsBean.getCartId());
        orderItemsBean.setGoodsName(cartItemsBean.getGoodsName());
        orderItemsBean.setGoodsPrice(cartItemsBean.getGoodsPrice());
        orderItemsBean.setGoodsNumber(cartItemsBean.getCartNumber());//具体数量
        return orderItemsBean;
    }


    /**
     * 订单项目
     */
    private List<SubmitOrderGroup> setOrderVMList(List<CartBean> cartList) {
        orderVMList = new ArrayList<>();
        for (CartBean n : cartList) {
            SubmitOrderGroup orderVMListBean = new SubmitOrderGroup();
            if (getItemHasChoice(n)) {
                orderVMListBean.setGoodsTotal(n.getSelectPrice());//商品总价
                orderVMListBean.setUserAllPrice(n.getSelectPrice());
                orderVMListBean.setBrandId(n.getBrandId());//店铺名称
                orderVMListBean.setBrandName(n.getBrandName());//店铺名称
                List<SubmitOrderGroup.GoodsItem> orderItems = new ArrayList<>();
                for (CartGoodsItem c : n.getGoodsItem()) {
                    if (c.isSelected()) {
                        orderItems.add(setOrderListDate(c));
                    }
                }
                orderVMListBean.setGoodsItems(orderItems);
                orderVMList.add(orderVMListBean);
            }

        }
        return orderVMList;
    }


    /**
     * 判断该店铺是否被选择
     *
     * @param CartBean
     * @return
     */
    private Boolean getItemHasChoice(CartBean CartBean) {
        Boolean type = false;
        for (CartGoodsItem c : CartBean.getGoodsItem()) {
            if (c.isSelected()) {
                type = true;
                break;
            }
        }
        return type;
    }

    /**
     * 全选与反选
     */
    private void doCheckAll() {
        if (cartList == null || cartList.size() == 0) {
            return;
        }
        for (int i = 0; i < cartList.size(); i++) {
//            cartList.get(i).setChoosed(cb_check_all.isChecked());
            CartBean group = cartList.get(i);
            List<CartGoodsItem> childs = group.getGoodsItem();
            for (int j = 0; j < childs.size(); j++) {
                if (childs.get(j).getStock() >= childs.get(j).getCartNumber()) {
                    childs.get(j).setSelected(cb_check_all.isChecked());
                    cartList.get(i).setSelected(cb_check_all.isChecked());
                }
            }
        }
        selva.notifyDataSetChanged();
        calculate();
    }

    /**
     * 统计操作<br>
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作<br>
     * 3.给底部的textView进行数据填充
     */
    private void calculate() {
        totalCount = 0;
        totalPrice = new BigDecimal(0.00);//购物车内总共选中商品总价
        for (int i = 0; i < cartList.size(); i++) {
            CartBean group = cartList.get(i);
            List<CartGoodsItem> childs = group.getGoodsItem();
            BigDecimal totalPriceSmall = new BigDecimal(0.00);//购物车内一个店铺内选中商品总价
            for (int j = 0; j < childs.size(); j++) {
                CartGoodsItem product = childs.get(j);

                if (product.isSelected()) {
                    totalCount++;
                    BigDecimal goodPrice = product.getGoodsPrice().multiply(new BigDecimal(product.getCartNumber()));
                    totalPrice = totalPrice.add(goodPrice);
                    totalPriceSmall = totalPriceSmall.add(product.getGoodsPrice().multiply(new BigDecimal(product.getCartNumber())));
                }
            }

            group.setSelectPrice(totalPriceSmall);
        }

        tv_total_price.setText(BigDecimalUtils.wipeBigDecimalZero(totalPrice));
        tv_go_to_pay.setText("去结算(" + totalCount + ")");
    }

    private boolean isAllCheck() {
        for (CartBean group : cartList) {
            if (!group.isSelected())
                return false;
        }
        return true;
    }

    @Override
    public void onDelete(int groupPosition, int childPosition) {
        deleteCartGoods(groupPosition, childPosition);
    }

    @Override
    public void onChildItemClick(int groupPosition, int childPosition) {

        EventBus.getDefault().post(new StartBrotherEvent(GoodsDetailsFragment.newInstance(cartList.get(groupPosition).getGoodsItem().get(childPosition).getGoodsId())));

    }

    @Override
    public void onStoreClick(int groupPosition) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (subscriber != null && (!subscriber.isUnsubscribed())) {
            subscriber.unsubscribe();
        }
    }
}
