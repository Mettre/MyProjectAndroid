package com.example.mettre.myaprojectandroid.fragment;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mettre.myaprojectandroid.R;
import com.example.mettre.myaprojectandroid.adapter.HomeAdapter;
import com.example.mettre.myaprojectandroid.adapter.MenuAdapter;
import com.example.mettre.myaprojectandroid.base.BaseMainFragment;
import com.example.mettre.myaprojectandroid.bean.CategoryBean;
import com.example.mettre.myaprojectandroid.http.HttpMethods3;
import com.example.mettre.myaprojectandroid.http.HttpResult5;
import com.example.mettre.myaprojectandroid.subscribers.ProgressSubscriber;
import com.example.mettre.myaprojectandroid.subscribers.SubscriberOnNextListener;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * 三级商品分类
 */
public class CategoryFragment extends BaseMainFragment {

    /**
     * 分级列表
     */
    private List<CategoryBean> categoryList = new ArrayList<>();
    private SubscriberOnNextListener getCategoryListNext;
    private Subscriber<HttpResult5> subscriber;

    private List<String> menuList = new ArrayList<>();
    private List<CategoryBean> secondList = new ArrayList<>();
    private List<List<Integer>> listList;

    private ListView lv_menu;
    private ListView lv_home;

    private MenuAdapter menuAdapter;
    private HomeAdapter homeAdapter;
    private int currentItem;

    private TextView tv_title;

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        initView(view);
        return view;
    }

    public void initView(View view) {
        lv_menu = view.findViewById(R.id.lv_menu);
        tv_title = view.findViewById(R.id.tv_titile);
        lv_home = view.findViewById(R.id.lv_home);
        menuAdapter = new MenuAdapter(_mActivity, menuList);
        lv_menu.setAdapter(menuAdapter);

        homeAdapter = new HomeAdapter(_mActivity, secondList);
        lv_home.setAdapter(homeAdapter);

        lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                menuAdapter.setSelectItem(position);
                menuAdapter.notifyDataSetInvalidated();
                tv_title.setText(categoryList.get(position).getChildCategory().get(0).getCategoryName());
                lv_home.setSelection(listList.get(position).get(0));
            }
        });

        lv_home.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int scrollState;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.scrollState = scrollState;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    return;
                }
                int current = 0;
                for (int i = 0; i < listList.size(); i++) {
                    if (listList.get(i).indexOf(firstVisibleItem) != -1) {
                        current = i;
                        break;
                    }
                }

                if (currentItem != current && current >= 0) {
                    currentItem = current;
                    menuAdapter.setSelectItem(currentItem);
                    menuAdapter.notifyDataSetInvalidated();
                }
                tv_title.setText(secondList.get(firstVisibleItem).getCategoryName());
            }
        });

        getCategoryList();
    }

    /**
     * 获取三级分类
     */
    private void getCategoryList() {

        getCategoryListNext = new SubscriberOnNextListener<List<CategoryBean>>() {

            @Override
            public void onNext(List<CategoryBean> response) {
                categoryList = response;
                loadData();
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
        subscriber = new ProgressSubscriber(getCategoryListNext, _mActivity, false);
        HttpMethods3.getInstance().getCategoryList(subscriber);
    }


    private void loadData() {
        listList = new ArrayList<>();
        int position = 0;
        int allPosition = 0;
        if (categoryList != null && categoryList.size() > 0) {
            for (int i = 0; i < categoryList.size(); i++) {
                CategoryBean dataBean = categoryList.get(i);
                menuList.add(dataBean.getCategoryName());
                List<Integer> list = new ArrayList<>();
                for (int j = position; j < (position + dataBean.getChildCategory().size()); j++) {
                    list.add(j);
                    allPosition++;
                    secondList.add(dataBean.getChildCategory().get(j - position));
                }
                position = allPosition;
                listList.add(list);
            }
        }

        Log.e("-------11111------", "" + new Gson().toJson(listList));
        tv_title.setText(categoryList.get(0).getChildCategory().get(0).getCategoryName());

        menuAdapter.notifyDataSetChanged();
        homeAdapter.notifyDataSetChanged();
    }

    /**
     * 得到json文件中的内容
     *
     * @param _mActivity
     * @param fileName
     * @return
     */
    public static String getJson(FragmentActivity _mActivity, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        //获得assets资源管理器
        AssetManager assetManager = _mActivity.getAssets();
        //使用IO流读取json文件内容
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName), "utf-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}