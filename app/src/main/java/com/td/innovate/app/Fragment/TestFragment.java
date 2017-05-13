package com.td.innovate.app.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.td.innovate.app.R;

/**
 * Created by zunairsyed on 2015-11-10.
 */public final class TestFragment extends Fragment {
    private static final String KEY_CONTENT = "TestFragment:Content";

    int imageSource;

//
//    public TestFragment(int imageSource) {
//        this.imageSource = imageSource;
//
//    }
    public TestFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            imageSource = savedInstanceState.getInt(KEY_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_product_info, null);
        setRetainInstance(true);
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       // outState.putInt(KEY_CONTENT, imageSource);
    }
}















/*






package com.td.innovate.app.Activity;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.td.innovate.app.Adapter.MainViewPagerFragmentAdapter;
import com.td.innovate.app.Adapter.TestFragmentAdapter;
import com.td.innovate.app.R;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

public class MainViewPagerActivity extends FragmentActivity {

    TestFragmentAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view_pager);

        mAdapter = new TestFragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.myViewPager);
        mPager.setAdapter(mAdapter);

        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator = indicator;
        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;
       // indicator.setBackgroundColor(0xFFCCCCCC);
        indicator.setRadius(15 * density);
        indicator.setPageColor(0x33888888);
        indicator.setFillColor(0x8878AB46);
       // indicator.setStrokeColor(0xFF000000);
      //  indicator.setStrokeWidth(2 * density);
        indicator.isHorizontalFadingEdgeEnabled();
       // indicator.set
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}







 */