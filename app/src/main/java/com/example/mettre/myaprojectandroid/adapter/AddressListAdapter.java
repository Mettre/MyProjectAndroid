package com.example.mettre.myaprojectandroid.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mettre.myaprojectandroid.R;
import com.example.mettre.myaprojectandroid.bean.AddressBean;

import java.util.List;

/**
 * Created by app on 2017/7/21.
 * 收货地址
 */

public class AddressListAdapter extends BaseQuickAdapter<AddressBean, BaseViewHolder> {

    public AddressListAdapter(List<AddressBean> data) {
        super(R.layout.item_address_list, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final AddressBean item) {
        helper.setText(R.id.name, item.getName());
        helper.getView(R.id.address_normal).setVisibility(item.isDefaults() ? View.VISIBLE : View.INVISIBLE);
        helper.setText(R.id.phone, item.getPhone());
        helper.setText(R.id.address, item.getProvince() + item.getCity() + item.getCounty() + item.getAddress());
    }
}
