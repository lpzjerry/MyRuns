package com.xd.demoactiontabs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import java.time.Instant;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FragmentA fragmentA;
    FragmentB fragmentB;
    FragmentC fragmentC;
    ViewPager viewPager;
    TabLayout tabLayout;
    MyFragmentPagerAdapter myFragmentPagerAdapter;
    ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Util.checkPermissions(this);

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        tabLayout = (TabLayout)findViewById(R.id.tab);

        fragmentA = new FragmentA();
        fragmentB = new FragmentB();
        fragmentC = new FragmentC();

        fragments = new ArrayList<Fragment>();
        fragments.add(fragmentA);
        fragments.add(fragmentB);
        fragments.add(fragmentC);

        myFragmentPagerAdapter = new MyFragmentPagerAdapter(this.getSupportFragmentManager(), fragments);
        viewPager.setAdapter(myFragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void onClickButtonStart(View view) {
        Spinner mySpinner = (Spinner) findViewById(R.id.spinner_input_type);
        String text = mySpinner.getSelectedItem().toString();
        // Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show(); // DEBUG
        if (text.equals("Manual Entry")) {
            Intent intent = new Intent(MainActivity.this, ManualEntryActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
        }
    }
}
