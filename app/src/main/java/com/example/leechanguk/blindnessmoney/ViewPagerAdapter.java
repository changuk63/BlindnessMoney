package com.example.leechanguk.blindnessmoney;

import android.app.PendingIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Leechanguk on 2016-11-21.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private static final int FIRSTPAGE = 0;
    private static final int SECONDPAGE = 1;

    public ViewPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position){
        switch (position){
            case FIRSTPAGE:
                return RepayListActivity.newInstance();
            case SECONDPAGE:
                return LoanListActivity.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount(){
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position){
        if(position == 0)
            return "받을 돈";
        else
            return "갚을 돈";
    }
}
