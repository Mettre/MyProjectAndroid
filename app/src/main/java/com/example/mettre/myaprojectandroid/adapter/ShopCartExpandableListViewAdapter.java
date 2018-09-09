package com.example.mettre.myaprojectandroid.adapter;


import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mettre.myaprojectandroid.R;
import com.example.mettre.myaprojectandroid.bean.CartBean;
import com.example.mettre.myaprojectandroid.bean.CartGoodsItem;
import com.example.mettre.myaprojectandroid.utils.BigDecimalUtils;
import com.example.mettre.myaprojectandroid.utils.MyImageLoader;
import com.example.mettre.myaprojectandroid.utils.RandomURL;
import com.example.mettre.myaprojectandroid.view.SlideView;
import com.example.mettre.myaprojectandroid.view.SquareImageView;

import java.util.List;


public class ShopCartExpandableListViewAdapter extends BaseExpandableListAdapter {
    private List<CartBean> groups;
    private Context context;
    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;
    private DeleteProductInterface deleteProductInterface;
    private ChildItemInterface childItemInterface;

    /**
     * 构造函数
     *
     * @param groups  组元素列表
     * @param context
     */
    public ShopCartExpandableListViewAdapter(List<CartBean> groups, Context context) {
        super();
        this.groups = groups;
        this.context = context;
    }

    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }

    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    public DeleteProductInterface getDeleteProductInterface() {
        return deleteProductInterface;
    }

    public void setDeleteProductInterface(DeleteProductInterface deleteProductInterface) {
        this.deleteProductInterface = deleteProductInterface;
    }

    public ChildItemInterface getChildItemInterface() {
        return childItemInterface;
    }

    public void setChildItemInterface(ChildItemInterface childItemInterface) {
        this.childItemInterface = childItemInterface;
    }

    @Override
    public int getGroupCount() {
        return groups == null ? 0 : groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).getGoodsItem().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).getGoodsItem().get(childPosition);
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
            convertView = View.inflate(context, R.layout.item_shopcart_group, null);
            gholder.cb_check = (CheckBox) convertView.findViewById(R.id.determine_chekbox);
            gholder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_source_name);
            //groupMap.put(groupPosition, convertView);
            convertView.setTag(gholder);
        } else {
            // convertView = groupMap.get(groupPosition);
            gholder = (GroupHolder) convertView.getTag();
        }
        final CartBean group = (CartBean) getGroup(groupPosition);
        if (group != null) {
            gholder.tv_group_name.setText(group.getBrandName());

            gholder.cb_check.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    group.setSelected(((CheckBox) v).isChecked());
                    checkInterface.checkGroup(groupPosition, ((CheckBox) v).isChecked());// 暴露组选接口

                }
            });
            gholder.tv_group_name.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    childItemInterface.onStoreClick(groupPosition);
                }
            });
            gholder.cb_check.setChecked(group.isSelected());
            gholder.cb_check.setClickable(false);
            gholder.cb_check.setEnabled(false);
            for (CartGoodsItem c : group.getGoodsItem()) {
                if (c.getStock() >= c.getCartNumber()) {
                    gholder.cb_check.setClickable(true);
                    gholder.cb_check.setEnabled(true);
                    break;
                }
            }
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        SlideView slideView = null;
        final ChildHolder cholder;
        if (convertView == null) {
            cholder = new ChildHolder();
            View view = View.inflate(context, R.layout.item_shopcart_product, null);
            slideView = new SlideView(context, context.getResources(), view);
            convertView = slideView;
            cholder.add_reduce_LinearLayout = (LinearLayout) convertView.findViewById(R.id.add_reduce_LinearLayout);
            cholder.cb_check = (CheckBox) convertView.findViewById(R.id.check_box);
            cholder.child_main = (LinearLayout) convertView.findViewById(R.id.child_main);
            cholder.promotionType_text = (TextView) convertView.findViewById(R.id.promotionType_text);

            cholder.iv_adapter_list_pic = (SquareImageView) convertView.findViewById(R.id.iv_adapter_list_pic);
            cholder.tv_product_desc = (TextView) convertView.findViewById(R.id.tv_intro);
            cholder.sku = (TextView) convertView.findViewById(R.id.sku);
            cholder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            cholder.iv_increase = (TextView) convertView.findViewById(R.id.tv_add);
            cholder.iv_decrease = (TextView) convertView.findViewById(R.id.tv_reduce);
            cholder.tv_count = (TextView) convertView.findViewById(R.id.tv_num);

            cholder.tv_delete = (TextView) convertView.findViewById(R.id.back);
            cholder.bottom_line = (TextView) convertView.findViewById(R.id.bottom_line);
            convertView.setTag(cholder);
        } else {
            // convertView = childrenMap.get(groupPosition);
            cholder = (ChildHolder) convertView.getTag();
        }
        final CartGoodsItem product = (CartGoodsItem) getChild(groupPosition, childPosition);

        if (product != null) {

            MyImageLoader.getInstance().displayImage(context, RandomURL.getInstance().getRandomUrl(), cholder.iv_adapter_list_pic);
            cholder.tv_product_desc.setText(product.getGoodsName());
            cholder.tv_price.setText( BigDecimalUtils.wipeBigDecimalZero(product.getGoodsPrice()));
            cholder.tv_count.setText(product.getCartNumber() + "");
            cholder.cb_check.setChecked(product.isSelected());

            if (childPosition + 1 == getChildrenCount(groupPosition)) {
                cholder.bottom_line.setVisibility(View.VISIBLE);
            } else {
                cholder.bottom_line.setVisibility(View.GONE);
            }

            cholder.cb_check.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    product.setSelected(((CheckBox) v).isChecked());
                    cholder.cb_check.setChecked(((CheckBox) v).isChecked());
                    checkInterface.checkChild(groupPosition, childPosition, ((CheckBox) v).isChecked());// 暴露子选接口
                }
            });
            cholder.iv_increase.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doIncrease(groupPosition, childPosition, cholder.tv_count, cholder.cb_check.isChecked());// 暴露增加接口
                }
            });
            cholder.iv_decrease.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doDecrease(groupPosition, childPosition, cholder.tv_count, cholder.cb_check.isChecked());// 暴露删减接口
                }
            });
        }
        cholder.tv_delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                deleteProductInterface.onDelete(groupPosition, childPosition);
            }
        });
        cholder.iv_adapter_list_pic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                childItemInterface.onChildItemClick(groupPosition, childPosition);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    /**
     * 复选框接口
     */
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *
         * @param groupPosition 组元素位置
         * @param isChecked     组元素选中与否
         */
        public void checkGroup(int groupPosition, boolean isChecked);

        /**
         * 子选框状态改变时触发的事件
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param isChecked     子元素选中与否
         */
        public void checkChild(int groupPosition, int childPosition, boolean isChecked);
    }

    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        public void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        /**
         * 删减操作
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        public void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);
    }

    /**
     * 商品删除接口
     */
    public interface DeleteProductInterface {
        void onDelete(int groupPosition, int childPosition);

    }

    public interface ChildItemInterface {
        void onChildItemClick(int groupPosition, int childPosition);

        void onStoreClick(int groupPosition);
    }

    /**
     * 组元素绑定器
     */
    private class GroupHolder {
        CheckBox cb_check;
        TextView tv_group_name;
    }

    /**
     * 子元素绑定器
     */
    private class ChildHolder {
        LinearLayout add_reduce_LinearLayout;
        CheckBox cb_check;
        TextView promotionType_text;
        LinearLayout child_main;
        SquareImageView iv_adapter_list_pic;
        TextView tv_product_desc;
        TextView tv_price;
        TextView iv_increase;
        TextView tv_count;
        TextView iv_decrease;
        TextView tv_delete;
        TextView sku;
        TextView bottom_line;
    }

}
