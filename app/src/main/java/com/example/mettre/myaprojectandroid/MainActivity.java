package com.example.mettre.myaprojectandroid;

import android.os.Bundle;

import com.example.mettre.myaprojectandroid.fragment.HomeFragment;

import me.yokeyword.fragmentation.SupportActivity;

public class MainActivity extends SupportActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Fragmentation.builder()
//                // show stack view. Mode: BUBBLE, SHAKE, NONE
//                .stackViewMode(Fragmentation.NONE)
//                .debug(BuildConfig.DEBUG)
//                .install();

        if (findFragment(HomeFragment.class) == null) {
            loadRootFragment(R.id.fl_container, HomeFragment.newInstance());  //load root Fragment
        }
    }
}
