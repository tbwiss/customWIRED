package com.wiss.thom.wiredmobpro.activities;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.wiss.thom.wiredmobpro.R;
import com.wiss.thom.wiredmobpro.adapter.TabsPagerAdapter;
import com.wiss.thom.wiredmobpro.helper.ConnectionTask;
import com.wiss.thom.wiredmobpro.model.Categories;
import com.wiss.thom.wiredmobpro.model.PostORM;

import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    private static final String TAG = "MainActivity";
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;

    // Tab titles
    public static final String[] tabs = { "Business", "Design", "Entertainment", "Gear","Science","Security" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        URL sUrl = null;
        URL gUrl = null;
        URL eUrl = null;
        URL dUrl = null;

        try{
            sUrl = new URL(Categories.scienceURL);
            gUrl = new URL(Categories.gearURL);
            eUrl = new URL(Categories.entertainmentURL);
            dUrl = new URL(Categories.designURL);
        }catch(MalformedURLException e){
            Log.i(TAG, "Failure in URL: " + e.getMessage());
        }

        // Content aus Internet laden
        new ConnectionTask(this, sUrl, Categories.science).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new ConnectionTask(this, gUrl, Categories.gear).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new ConnectionTask(this, eUrl, Categories.entertainment).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new ConnectionTask(this, dUrl, Categories.design).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(),this);

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });


    }



    @Override
    public void onStop(){
        super.onStop();

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        PostORM.deleteDatabase(this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {


    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
}
