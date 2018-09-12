package com.example.mettre.myaprojectandroid;

import android.os.Bundle;
import android.text.TextUtils;

import com.example.mettre.myaprojectandroid.fragment.HomeFragment;
import com.example.mettre.myaprojectandroid.utils.ConstantUtil;
import com.example.mettre.myaprojectandroid.utils.SharedPrefsUtil;

import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.SupportActivity;

public class MainActivity extends SupportActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getTime();
//        Fragmentation.builder()
//                // show stack view. Mode: BUBBLE, SHAKE, NONE
//                .stackViewMode(Fragmentation.NONE)
//                .debug(BuildConfig.DEBUG)
//                .install();

        if (findFragment(HomeFragment.class) == null) {
            loadRootFragment(R.id.fl_container, HomeFragment.newInstance());  //load root Fragment
        }
    }

    /**
     * 获取时间戳
     *
     * @return
     */
    public void getTime() {
        if (TextUtils.isEmpty(SharedPrefsUtil.getValue(this, ConstantUtil.TIMESTAMP, ""))) {
            long time = System.currentTimeMillis() / 1000;//获取系统时间的10位的时间戳
            String str = String.valueOf(time);
            SharedPrefsUtil.putValue(this, ConstantUtil.TIMESTAMP, str);
        }
    }
}
