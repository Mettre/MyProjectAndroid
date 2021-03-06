package com.example.mettre.myaprojectandroid.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mettre.myaprojectandroid.R;
import com.example.mettre.myaprojectandroid.bean.CategoryBean;
import com.example.mettre.myaprojectandroid.view.GridViewForScrollView;

import java.util.List;

/**
 * Created by Mettre on 2018/8/30.
 */

public class HomeAdapter extends BaseAdapter {

    private Context context;
    private List<CategoryBean> foodDatas;
    private onGridViewItemListener onGridViewItemListener;

    public HomeAdapter(Context context, List<CategoryBean> foodDatas, onGridViewItemListener onGridViewItemListener) {
        this.context = context;
        this.foodDatas = foodDatas;
        this.onGridViewItemListener = onGridViewItemListener;
    }

    @Override
    public int getCount() {
        if (foodDatas != null) {
            return foodDatas.size();
        } else {
            return 10;
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
        final CategoryBean dataBean = foodDatas.get(position);
        List<CategoryBean> dataList = dataBean.getChildCategory();
        HomeAdapter.ViewHold viewHold = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_home, null);
            viewHold = new HomeAdapter.ViewHold();
            viewHold.gridView = (GridViewForScrollView) convertView.findViewById(R.id.gridView);
            viewHold.blank = (TextView) convertView.findViewById(R.id.blank);
            convertView.setTag(viewHold);
        } else {
            viewHold = (HomeAdapter.ViewHold) convertView.getTag();
        }
        HomeItemAdapter adapter = new HomeItemAdapter(context, dataList);
        viewHold.blank.setText(dataBean.getCategoryName());
        viewHold.gridView.setAdapter(adapter);
        viewHold.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onGridViewItemListener.onItemClick(dataBean.getChildCategory().get(i));
            }
        });
        return convertView;
    }

    public interface onGridViewItemListener {
        void onItemClick(CategoryBean dataBean);
    }

    private static class ViewHold {
        private GridViewForScrollView gridView;
        private TextView blank;
    }
}
