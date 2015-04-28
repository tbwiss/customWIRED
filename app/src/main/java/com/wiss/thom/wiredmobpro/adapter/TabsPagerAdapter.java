package com.wiss.thom.wiredmobpro.adapter;

import android.app.Activity;
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
import com.wiss.thom.wiredmobpro.model.Categories;
import com.wiss.thom.wiredmobpro.model.PostORM;

/**
 * Created by Thomas on 15.04.2015.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    private Activity activity;


    public TabsPagerAdapter(FragmentManager fragmentManager, Activity activity) {
        super(fragmentManager);
        this.activity = activity;
    }

    @Override
    public ListFragment getItem(int index) {

        switch (index) {
            case 0:
                PostORM.deleteCertainAmountOfPostsByCategory(activity, Categories.business,21);
                PostORM.deleteCertainAmountOfPostsByCategory(activity, Categories.design,21);
                return new BusinessFragment();
            case 1:
                PostORM.deleteCertainAmountOfPostsByCategory(activity, Categories.entertainment,21);
                return new DesignFragment();
            case 2:
                PostORM.deleteCertainAmountOfPostsByCategory(activity, Categories.entertainment,21);
                PostORM.deleteCertainAmountOfPostsByCategory(activity, Categories.gear,21);
                return new EntertainmentFragment();
            case 3:
                PostORM.deleteCertainAmountOfPostsByCategory(activity, Categories.science,21);
                return new GearFragment();
            case 4:
                PostORM.deleteCertainAmountOfPostsByCategory(activity, Categories.security,21);
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
