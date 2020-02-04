package com.dartmouth.cs.myruns2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private ActionTabsViewPagerAdaper myViewPageAdapter;

    public static RecordDataSource recordDataSource;

    private static SharedPreferences sharedPreferences;
    private static String[] unit_type_arr;
    public static final int UNIT_MILES = 0;
    public static final int UNIT_KM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        unit_type_arr = getResources().getStringArray(R.array.unit_preference_array);
        recordDataSource = new RecordDataSource(this);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        fragments = new ArrayList<Fragment>();
        fragments.add(new StartFragment());
        fragments.add(new HistoryFragment());
        fragments.add(new SettingFragment());

        myViewPageAdapter = new ActionTabsViewPagerAdaper(getSupportFragmentManager(), fragments);

        viewPager.setAdapter(myViewPageAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            public void onPageSelected(int position) {
                if (position == 1) {
                    HistoryFragment historyFragment = (HistoryFragment) fragments.get(1);
                    historyFragment.updateList();
                }
            }
        });
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    public static int getUnit() {
        String unit_type_str = sharedPreferences.getString("list_preference", unit_type_arr[1]);
        if (unit_type_str.equals(unit_type_arr[0]))
            return UNIT_KM;
        return UNIT_MILES;
    }
}
