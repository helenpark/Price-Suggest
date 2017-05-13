package com.td.innovate.app.Adapter;

/**
 * Created by zunairsyed on 2015-11-10.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.td.innovate.app.Fragment.ProductInfoFragment;
import com.td.innovate.app.Fragment.ReviewsFragment;

import com.td.innovate.app.R;
import com.viewpagerindicator.IconPagerAdapter;

import com.td.innovate.app.Fragment.StoreFragment;


import java.util.ArrayList;
import java.util.List;

public class MainViewPagerFragmentAdapter
        extends FragmentPagerAdapter implements IconPagerAdapter {

    protected static final int[] ICONS = new int[] {
            R.drawable.icon_select_group_offers,
            R.drawable.icon_select_group_product_info,
            R.drawable.icon_select_group_reviews,
    };

    private int mCount = ICONS.length;


    private List<Fragment> fragments;

    public MainViewPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
        this.fragments = new ArrayList<Fragment>();

        fragments.add(new StoreFragment());
        fragments.add(new ProductInfoFragment());
        fragments.add(new ReviewsFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getIconResId(int index) {
        return ICONS[index % ICONS.length];
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }

    public void update(){
        StoreFragment sf = (StoreFragment) fragments.get(0);
        sf.update();
    }

    public void productUpdate(){
        ProductInfoFragment pf = (ProductInfoFragment) fragments.get(1);
        pf.productUpdate();
    }
}
