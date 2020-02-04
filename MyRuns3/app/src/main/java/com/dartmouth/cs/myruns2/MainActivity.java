package com.dartmouth.cs.myruns2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private ActionTabsViewPagerAdaper myViewPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        fragments = new ArrayList<Fragment>();
        fragments.add(new StartFragment());
        fragments.add(new HistoryFragment());
        fragments.add(new SettingFragment());

        myViewPageAdapter = new ActionTabsViewPagerAdaper(getSupportFragmentManager(),
                fragments);

        viewPager.setAdapter(myViewPageAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }


}
