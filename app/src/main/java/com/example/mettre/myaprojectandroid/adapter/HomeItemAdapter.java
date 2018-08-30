package com.example.mettre.myaprojectandroid.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mettre.myaprojectandroid.R;
import com.example.mettre.myaprojectandroid.bean.CategoryBean;
import com.example.mettre.myaprojectandroid.utils.MyImageLoader;
import com.example.mettre.myaprojectandroid.view.SquareImageView;

import java.util.List;

public class HomeItemAdapter extends BaseAdapter {

    private Context context;
    private List<CategoryBean> foodDatas;

    public HomeItemAdapter(Context context, List<CategoryBean> foodDatas) {
        this.context = context;
        this.foodDatas = foodDatas;
    }


    @Override
    public int getCount() {
        if (foodDatas != null) {
            return foodDatas.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return foodDatas.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CategoryBean subcategory = foodDatas.get(position);
        ViewHold viewHold = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_home_category, null);
            viewHold = new ViewHold();
            viewHold.tv_name = (TextView) convertView.findViewById(R.id.item_home_name);
            viewHold.iv_icon = (SquareImageView) convertView.findViewById(R.id.item_album);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        viewHold.tv_name.setText(subcategory.getCategoryName());
        MyImageLoader.getInstance().displayImage(context, "https://wicdn.xiaohongchun.com/xhc-plat/1498710514471_4WzijshZNF.png", viewHold.iv_icon);
        return convertView;


    }

    private static class ViewHold {
        private TextView tv_name;
        private SquareImageView iv_icon;
    }

}
