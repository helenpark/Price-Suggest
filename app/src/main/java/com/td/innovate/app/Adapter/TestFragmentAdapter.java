package com.td.innovate.app.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.td.innovate.app.Fragment.TestFragment;
import com.td.innovate.app.R;

/**
 * Created by zunairsyed on 2015-11-10.
 */
public class TestFragmentAdapter  extends FragmentPagerAdapter {
    private int[] offerImages = {
            R.drawable.test_image,
            R.drawable.test_image,
            R.drawable.test_image,
            R.drawable.test_image
    };

    private int mCount = offerImages.length;

    public TestFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new TestFragment();
    }

    @Override
    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}
