package com.example.samples.viewpager;


import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.PagerAdapter;

import android.view.Menu;
import com.example.samples.R;

public class ViewPagerActivity extends Activity {

    // Declare Variables
    ViewPager viewPager;
    PagerAdapter adapter;
    String[] rank;
    String[] country;
    String[] population;
    int[] flag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from viewpager_main.xml
        setContentView(R.layout.viewpager_main);

        // Generate sample data
        rank = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        country = new String[]{"China", "India", "United States",
                "Indonesia", "Brazil", "Pakistan", "Nigeria", "Bangladesh",
                "Russia", "Japan"};

        population = new String[]{"1,354,040,000", "1,210,193,422",
                "315,761,000", "237,641,326", "193,946,886", "182,912,000",
                "170,901,000", "152,518,015", "143,369,806", "127,360,000"};

        flag = new int[]{R.drawable.china, R.drawable.india,
                R.drawable.unitedstates, R.drawable.indonesia,
                R.drawable.brazil, R.drawable.pakistan, R.drawable.nigeria,
                R.drawable.bangladesh, R.drawable.russia, R.drawable.japan};

        // Locate the ViewPager in viewpager_main.xml
        viewPager = (ViewPager) findViewById(R.id.pager);


//        viewPager.setPageMargin(-130);
//        viewPager.setHorizontalFadingEdgeEnabled(true);
//        viewPager.setFadingEdgeLength(30);

        // Pass results to ViewPagerAdapter Class
        adapter = new ViewPagerAdapter(this, rank, country, population, flag);


        // Binds the Adapter to the ViewPager
        viewPager.setAdapter(adapter);

    }

    // Not using options menu in this tutorial
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}