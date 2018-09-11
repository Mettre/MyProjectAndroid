package com.example.mettre.myaprojectandroid.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mettre.myaprojectandroid.R;
import com.example.mettre.myaprojectandroid.bean.OrderListBean;
import com.example.mettre.myaprojectandroid.utils.BigDecimalUtils;
import com.example.mettre.myaprojectandroid.utils.MyImageLoader;
import com.example.mettre.myaprojectandroid.utils.RandomURL;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by app on 2017/10/23.
 */

public class OrderAdapter extends BaseExpandableListAdapter {

    private List<OrderListBean> groups;
    private Context context;
    private OrderClickListener onOrderClick;

    /**
     * 构造函数
     *
     * @param groups  组元素列表
     * @param context
     */
    public OrderAdapter(List<OrderListBean> groups, Context context, OrderClickListener onOrderClick) {
        super();
        this.groups = groups;
        this.context = context;
        this.onOrderClick = onOrderClick;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).getOrderItem().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).getOrderItem().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder gholder;
        if (convertView == null) {
            gholder = new GroupHolder();
            convertView = View.inflate(context, R.layout.item_order_group, null);
            gholder.shop_name = (TextView) convertView.findViewById(R.id.shop_name);
            gholder.order_state = (TextView) convertView.findViewById(R.id.order_state);
            //groupMap.put(groupPosition, convertView);
            convertView.setTag(gholder);
        } else {
            // convertView = groupMap.get(groupPosition);
            gholder = (GroupHolder) convertView.getTag();
        }
        final OrderListBean group = (OrderListBean) getGroup(groupPosition);
        if (group != null) {
            gholder.shop_name.setText(group.getBrandName());
            String orderType = "";
            gholder.order_state.setTextColor(context.getResources().getColor(R.color.monsoon));
            //订单状态 0:已取消  10:未付款  20:已支付  30:已发货  40:交易成功  50:交易关闭
            switch (group.getStatus()) {
                case 0:
                    orderType = "已取消";
                    break;
                case 10:
                    orderType = "未付款";
                    break;
                case 20:
                    orderType = "已支付";
                    break;
                case 30:
                    orderType = "已发货";
                    break;
                case 40:
                    orderType = "交易成功";
                    break;
                case 50:
                    orderType = "交易关闭";
                    break;
            }
            gholder.order_state.setText(orderType);
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildHolder cholder;
        if (convertView == null) {
            cholder = new ChildHolder();
            View view = View.inflate(context, R.layout.item_order_child, null);
            convertView = view;

            cholder.product_picture = (ImageView) convertView.findViewById(R.id.product_picture);
            cholder.tv_product_desc = (TextView) convertView.findViewById(R.id.tv_product_desc);
            cholder.product_price = (TextView) convertView.findViewById(R.id.product_price);
            cholder.product_quantity = (TextView) convertView.findViewById(R.id.product_quantity);
            cholder.freight_text = (TextView) convertView.findViewById(R.id.freight_text);
            cholder.order_btn_right = (TextView) convertView.findViewById(R.id.order_btn_right);
            cholder.order_btn_left = (TextView) convertView.findViewById(R.id.order_btn_left);
            cholder.order_btn_three = (TextView) convertView.findViewById(R.id.order_btn_three);
            cholder.goods_attributeStr = (TextView) convertView.findViewById(R.id.goods_attributeStr);
            cholder.goods_main_item = (LinearLayout) convertView.findViewById(R.id.goods_main_item);
            cholder.total_quantity = (TextView) convertView.findViewById(R.id.total_quantity);
            cholder.total_price = (TextView) convertView.findViewById(R.id.total_price);
            cholder.money_linearLayout = (LinearLayout) convertView.findViewById(R.id.money_linearLayout);
            cholder.money_pay = (TextView) convertView.findViewById(R.id.money_pay);
            cholder.bottom_view = (LinearLayout) convertView.findViewById(R.id.bottom_view);
            convertView.setTag(cholder);
        } else {
            // convertView = childrenMap.get(groupPosition);
            cholder = (ChildHolder) convertView.getTag();
        }
        final OrderListBean.ListBean goods = (OrderListBean.ListBean) getChild(groupPosition, childPosition);
        final OrderListBean group = (OrderListBean) getGroup(groupPosition);

        if (goods != null) {
            cholder.tv_product_desc.setText(goods.getGoodsName());
            MyImageLoader.getInstance().displayImage(context, RandomURL.getInstance().getRandomUrl(), cholder.product_picture);

            cholder.product_price.setText(BigDecimalUtils.wipeBigDecimalZero(goods.getOrderGoodsPrice()));
            cholder.product_quantity.setText("x" + goods.getQuantity() + "");
            cholder.goods_main_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOrderClick.itemClick(groupPosition);
                }
            });
        }


        if (childPosition + 1 == getChildrenCount(groupPosition)) {
            cholder.bottom_view.setVisibility(View.VISIBLE);
            //WAIT_PAY("等待付款"), WAIT_SEND("等待发货"), SENDED("已发货"), REFUNDING("退款中"), SUCCESS("成功"), CANCLE("取消");
            //订单状态 0:已取消  10:未付款  20:已支付  30:已发货  40:交易成功  50:交易关闭
            switch (group.getStatus()) {
                case 10:
                    cholder.order_btn_left.setVisibility(View.VISIBLE);
                    cholder.order_btn_left.setText("取消订单");
                    cholder.order_btn_right.setVisibility(View.VISIBLE);
                    cholder.order_btn_three.setVisibility(View.GONE);
                    cholder.order_btn_right.setText("付款");
                    cholder.order_btn_left.setTextColor(context.getResources().getColor(R.color.monsoon));
                    cholder.order_btn_left.setBackground(context.getResources().getDrawable(R.drawable.bg_orange_line));
                    cholder.order_btn_right.setTextColor(context.getResources().getColor(R.color.white));
                    cholder.order_btn_right.setBackground(context.getResources().getDrawable(R.drawable.bg_dark_red));
                    break;
                case 20:
                    cholder.order_btn_three.setVisibility(View.GONE);
                    cholder.order_btn_left.setVisibility(View.VISIBLE);
                    cholder.order_btn_left.setText("取消订单");
                    cholder.order_btn_right.setVisibility(View.VISIBLE);
                    cholder.order_btn_right.setText("提醒发货");
                    cholder.order_btn_right.setTextColor(context.getResources().getColor(R.color.monsoon));
                    cholder.order_btn_right.setBackground(context.getResources().getDrawable(R.drawable.bg_orange_line));
                    cholder.order_btn_left.setTextColor(context.getResources().getColor(R.color.monsoon));
                    cholder.order_btn_left.setBackground(context.getResources().getDrawable(R.drawable.bg_orange_line));
                    break;
                case 30:
                    cholder.order_btn_left.setVisibility(View.VISIBLE);
                    cholder.order_btn_right.setVisibility(View.VISIBLE);
                    cholder.order_btn_three.setVisibility(View.VISIBLE);
                    cholder.order_btn_left.setText("查看物流");
                    cholder.order_btn_right.setText("申请售后");
                    cholder.order_btn_three.setText("确认收货");

                    cholder.order_btn_right.setTextColor(context.getResources().getColor(R.color.monsoon));
                    cholder.order_btn_right.setBackground(context.getResources().getDrawable(R.drawable.bg_orange_line));
                    cholder.order_btn_left.setTextColor(context.getResources().getColor(R.color.monsoon));
                    cholder.order_btn_left.setBackground(context.getResources().getDrawable(R.drawable.bg_orange_line));
                    cholder.order_btn_three.setTextColor(context.getResources().getColor(R.color.white));
                    cholder.order_btn_three.setBackground(context.getResources().getDrawable(R.drawable.bg_dark_red));
                    break;
                case 40:
                    cholder.order_btn_three.setVisibility(View.GONE);
                    cholder.order_btn_left.setVisibility(View.VISIBLE);
                    cholder.order_btn_left.setText("评价");
                    cholder.order_btn_left.setTextColor(context.getResources().getColor(R.color.white));
                    cholder.order_btn_left.setBackground(context.getResources().getDrawable(R.drawable.bg_dark_red));
                    cholder.order_btn_right.setTextColor(context.getResources().getColor(R.color.monsoon));
                    cholder.order_btn_right.setBackground(context.getResources().getDrawable(R.drawable.bg_orange_line));
                    cholder.order_btn_right.setVisibility(View.VISIBLE);
                    cholder.order_btn_right.setText("申请售后");
                    break;
                case 50:
                    cholder.order_btn_three.setVisibility(View.GONE);
                    cholder.order_btn_left.setVisibility(View.GONE);
                    cholder.order_btn_right.setVisibility(View.GONE);
                    break;
            }

            cholder.order_btn_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOrderClick.onBtnClick(groupPosition, cholder.order_btn_right.getText().toString());
                }
            });
            cholder.order_btn_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOrderClick.onBtnClick(groupPosition, cholder.order_btn_left.getText().toString());
                }
            });
            cholder.order_btn_three.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOrderClick.onBtnClick(groupPosition, cholder.order_btn_three.getText().toString());
                }
            });


            if (group.getPostage().compareTo(BigDecimal.ZERO) == 0) {
                cholder.freight_text.setVisibility(View.GONE);
            } else {
                cholder.freight_text.setText("(含运费" + BigDecimalUtils.wipeBigDecimalZero(group.getPostage()) + ")");
            }
//            cholder.total_quantity.setText("共" + group.getTotalQuantity() + "件");
            cholder.total_price.setText("￥" + group.getOrderPrice());
            cholder.money_pay.setText("￥" + group.getPayment());

        } else {
            cholder.bottom_view.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public interface OrderClickListener {
        void onBtnClick(int position, String str);

        void itemClick(int position);
    }

    /**
     * 组元素绑定器
     */
    private class GroupHolder {
        private TextView shop_name;
        private TextView order_state;
    }

    /**
     * 子元素绑定器
     */
    private class ChildHolder {

        ImageView product_picture;
        TextView tv_product_desc;
        TextView product_price;
        TextView product_quantity;
        TextView freight_text;
        TextView total_quantity;
        TextView total_price;
        LinearLayout money_linearLayout;
        TextView money_pay;
        LinearLayout bottom_view;
        TextView order_btn_right;
        TextView order_btn_left;
        TextView order_btn_three;
        TextView goods_attributeStr;
        LinearLayout goods_main_item;
    }
}
