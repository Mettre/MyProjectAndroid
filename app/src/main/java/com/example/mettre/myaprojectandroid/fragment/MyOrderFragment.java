package com.example.mettre.myaprojectandroid.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mettre.myaprojectandroid.R;
import com.example.mettre.myaprojectandroid.base.BaseMainFragment;
import com.example.mettre.myaprojectandroid.constant.CommonConstant;
import com.example.mettre.myaprojectandroid.event.StartBrotherEvent;
import com.example.mettre.myaprojectandroid.utils.TabEntity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by app on 2017/7/18.
 * 我的订单
 */

public class MyOrderFragment extends BaseMainFragment {

    private Toolbar mToolbar;
    private ArrayList<Fragment> mFragments2;
    private CommonTabLayout tableLayout;
    private ViewPager mViewPager;
    private String[] mTitles = new String[]{"全部", "待付款", "待发货", "待收货", "待评价"};
    private ArrayList<CustomTabEntity> mTabEntities;
    private int CurrentItem;

    public static MyOrderFragment newInstance(int CurrentItem) {
        MyOrderFragment fragment = new MyOrderFragment();
        Bundle args = new Bundle();
        args.putInt("CurrentItem", CurrentItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_my_order, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        CurrentItem = getArguments().getInt("CurrentItem", 0);
        mToolbar = view.findViewById(R.id.toolbar);
        tableLayout = view.findViewById(R.id.tabLayout);
        mViewPager = view.findViewById(R.id.viewPager);

        mToolbar.setTitleTextColor(getResources().getColor(R.color.oil));
        initToolbarNav(mToolbar);
        mToolbar.setTitle("我的订单");

        mFragments2 = new ArrayList<>();
        mFragments2.add(AllOrderFragment.newInstance());
        mFragments2.add(AllOrderFragment.newInstance());
        mFragments2.add(AllOrderFragment.newInstance());
        mFragments2.add(AllOrderFragment.newInstance());
        mFragments2.add(AllOrderFragment.newInstance());

        mTabEntities = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i]));
        }
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
        tl_2();
    }

    private void tl_2() {
        tableLayout.setTabData(mTabEntities);
        tableLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tableLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(CurrentItem);
    }

    @Subscribe
    public void startBrother(StartBrotherEvent event) {
        if (event.targetFragment != null && event.EventType == CommonConstant.ORDER_LIST) {
            start(event.targetFragment);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments2.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments2.get(position);
        }
    }
}
