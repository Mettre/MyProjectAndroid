package com.example.mettre.myaprojectandroid.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mettre.myaprojectandroid.R;
import com.example.mettre.myaprojectandroid.base.BaseMainFragment;
import com.example.mettre.myaprojectandroid.constant.CommonConstant;
import com.example.mettre.myaprojectandroid.event.StartBrotherEvent;
import com.example.mettre.myaprojectandroid.utils.TabEntity;
import com.example.mettre.myaprojectandroid.utils.ToastUtils;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by Mettre on 2018/8/14.
 */

public class HomeFragment extends BaseMainFragment implements BaseMainFragment.OnFragmentOpenDrawerListener {

    private long TOUCH_TIME = 0;
    private static final long WAIT_TIME = 2000L;
    private CommonTabLayout commonTabLayout;
    private int mCurrentPosition = 0;
    private String[] mTitles = new String[]{"首页", "分类", "购物车", "我的"};
    private final SupportFragment[] mFragments = new SupportFragment[mTitles.length];
    private ArrayList<CustomTabEntity> mTabEntities;
    private int[] checkeds = new int[]{R.drawable.home_icon5_true, R.drawable.home_icon5_true, R.drawable.home_icon5_true, R.drawable.home_icon5_true};
    private int[] normals = new int[]{R.drawable.home_icon5_false, R.drawable.home_icon5_false, R.drawable.home_icon5_false, R.drawable.home_icon5_false};


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        commonTabLayout = view.findViewById(R.id.tabLayout);
        mFragments[0] = LeftFragment.newInstance();
        mFragments[1] = GoodsCategoryFragment.newInstance();
        mFragments[2] = CartFragment.newInstance();
        mFragments[3] = RightFragment.newInstance();

        mTabEntities = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], checkeds[i], normals[i]));
        }


        commonTabLayout.setTabData(mTabEntities);
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position != mCurrentPosition) {
                    showHideFragment(mFragments[position], mFragments[mCurrentPosition]);
                    mCurrentPosition = position;
                }
            }

            @Override
            public void onTabReselect(int position) {
                //TODO 重选
            }
        });
        loadMultipleRootFragment(R.id.fl_change, 0, mFragments[0], mFragments[1], mFragments[2], mFragments[3]);
    }

    /**
     * start other BrotherFragment
     */
    @Subscribe
    public void startBrother(StartBrotherEvent event) {
        if (event.targetFragment != null) {
            start(event.targetFragment);
        }

        if(event.EventType==CommonConstant.BACK_HOME){
            commonTabLayout.setCurrentTab(0);
            showHideFragment(mFragments[0], mFragments[mCurrentPosition]);
            mCurrentPosition = 0;
        }
    }


    /**
     * 类似于 Activity的 onNewIntent()
     */
    @Override
    public void onNewBundle(Bundle args) {
        super.onNewBundle(args);
        Toast.makeText(_mActivity, args.getString("from"), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOpenDrawer() {
        pop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public boolean onBackPressedSupport() {
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 1) {
            pop();
        } else {
            if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                getActivity().finish();
            } else {
                TOUCH_TIME = System.currentTimeMillis();
                ToastUtils.showShortToast("再按一次退出程序！");
            }
            return true;
        }
        return true;
    }
}