package com.example.mettre.myaprojectandroid.adapter;


import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mettre.myaprojectandroid.R;
import com.example.mettre.myaprojectandroid.bean.SubmitOrderGroup;
import com.example.mettre.myaprojectandroid.utils.BigDecimalUtils;
import com.example.mettre.myaprojectandroid.utils.MyImageLoader;
import com.example.mettre.myaprojectandroid.utils.RandomURL;
import com.example.mettre.myaprojectandroid.view.SquareImageView;

import java.util.List;

/**
 * 订单项adapter
 */

public class OrderExpandableListViewAdapter extends BaseExpandableListAdapter {
    private List<SubmitOrderGroup> orderVMList;
    private Context context;

    /**
     * 构造函数
     *
     * @param context
     */
    public OrderExpandableListViewAdapter(List<SubmitOrderGroup> orderVMList, Context context) {
        super();
        this.orderVMList = orderVMList;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return orderVMList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return orderVMList.get(groupPosition).getGoodsItems().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return orderVMList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return orderVMList.get(groupPosition).getGoodsItems().get(childPosition);
    }

    public int getChildSize(int groupPosition) {

        return orderVMList.get(groupPosition).getGoodsItems().size();
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
            convertView = View.inflate(context, R.layout.order_list_group, null);
            gholder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_source_name);
            convertView.setTag(gholder);
        } else {
            gholder = (GroupHolder) convertView.getTag();
        }
        SubmitOrderGroup orderVMListBean = (SubmitOrderGroup) getGroup(groupPosition);
        if (orderVMListBean != null) {
            gholder.tv_group_name.setText(orderVMListBean.getBrandName());
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildHolder cholder;
        if (convertView == null) {
            cholder = new ChildHolder();
            convertView = View.inflate(context, R.layout.item_place_order_child, null);

            cholder.tv_product_desc = (TextView) convertView.findViewById(R.id.tv_intro);
            cholder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            cholder.tv_quantity = (TextView) convertView.findViewById(R.id.tv_quantity);
            cholder.order_detail = (LinearLayout) convertView.findViewById(R.id.order_detail);
            cholder.iv_adapter_list_pic = (SquareImageView) convertView.findViewById(R.id.iv_adapter_list_pic);

            cholder.edit_remark = (EditText) convertView.findViewById(R.id.edit_remark);
            cholder.price_rmb = (TextView) convertView.findViewById(R.id.price_rmb);
            cholder.goods_price = (TextView) convertView.findViewById(R.id.goods_price);
            cholder.price_driver = (TextView) convertView.findViewById(R.id.price_driver);
            cholder.goods_size = (TextView) convertView.findViewById(R.id.goods_size);

            convertView.setTag(cholder);
        } else {

            cholder = (ChildHolder) convertView.getTag();
        }
        SubmitOrderGroup.GoodsItem orderItems = (SubmitOrderGroup.GoodsItem) getChild(groupPosition, childPosition);

        if (childPosition + 1 == getChildSize(groupPosition)) {
            cholder.order_detail.setVisibility(View.VISIBLE);

            SubmitOrderGroup orderVMListBean = (SubmitOrderGroup) getGroup(groupPosition);
            cholder.price_driver.setText(BigDecimalUtils.wipeBigDecimalZero(orderVMListBean.getPostage()));//快递费
            cholder.price_rmb.setText(BigDecimalUtils.wipeBigDecimalZero(orderVMListBean.getUserAllPrice()));//小计
            cholder.goods_price.setText(BigDecimalUtils.wipeBigDecimalZero(orderVMListBean.getGoodsTotal()));//商品总价

            cholder.goods_size.setText("共 " + orderVMListBean.getGoodsItems().size() + " 件商品  小计：");

            cholder.edit_remark.setTag(groupPosition);
            cholder.edit_remark.addTextChangedListener(new TextSwitcher(cholder));
        } else {
            cholder.order_detail.setVisibility(View.GONE);
        }

        if (orderItems != null) {
            cholder.tv_product_desc.setText(orderItems.getGoodsName());
            cholder.tv_price.setText(BigDecimalUtils.wipeBigDecimalZero(orderItems.getGoodsPrice()));
            cholder.tv_quantity.setText("X" + orderItems.getGoodsNumber());
            MyImageLoader.getInstance().displayImage(context, RandomURL.getInstance().getRandomUrl(), cholder.iv_adapter_list_pic);

        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public List<SubmitOrderGroup> getOrderVMList() {
        return orderVMList;
    }

    public void setOrderVMList(List<SubmitOrderGroup> orderVMList) {
        this.orderVMList = orderVMList;
    }

    class TextSwitcher implements TextWatcher {
        private ChildHolder mHolder;

        public TextSwitcher(ChildHolder mHolder) {
            this.mHolder = mHolder;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int position = (int) mHolder.edit_remark.getTag();
            orderVMList.get(position).setBuyerMessage(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    /**
     * 组元素绑定器
     */
    private class GroupHolder {
        TextView tv_group_name;
    }

    /**
     * 子元素绑定器
     */
    private class ChildHolder {
        TextView tv_product_desc;
        TextView tv_price;
        TextView tv_quantity;
        private SquareImageView iv_adapter_list_pic;
        private EditText edit_remark;
        private TextView goods_price;//商品总价值
        private TextView price_rmb;//实际付款
        private TextView price_driver;//运费
        private LinearLayout order_detail;
        private TextView goods_size;//订单内商品数量
    }

}
