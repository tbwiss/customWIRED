package com.wiss.thom.wiredmobpro.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;

import com.wiss.thom.wiredmobpro.fragments.BusinessFragment;
import com.wiss.thom.wiredmobpro.fragments.DesignFragment;
import com.wiss.thom.wiredmobpro.fragments.EntertainmentFragment;
import com.wiss.thom.wiredmobpro.fragments.GearFragment;
import com.wiss.thom.wiredmobpro.fragments.ScienceFragment;
import com.wiss.thom.wiredmobpro.fragments.SecurityFragment;

/**
 * Created by Thomas on 15.04.2015.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {


    public TabsPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public ListFragment getItem(int index) {

        switch (index) {
            case 0:
                return new BusinessFragment();
            case 1:
                return new DesignFragment();
            case 2:
                return new EntertainmentFragment();
            case 3:
                return new GearFragment();
            case 4:
                return new ScienceFragment();
            case 5:
                return new SecurityFragment();

        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 6;
    }


}
