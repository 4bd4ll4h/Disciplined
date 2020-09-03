package com.example.disciplined;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class fragmentAdapter extends FragmentPagerAdapter {
private Context context;

SimpleDateFormat s;
int dayOfWeek;
    public fragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {
        position=position+1;
        Calendar  theDays=Calendar.getInstance();
        int temp=theDays.get(Calendar.DAY_OF_WEEK);
        dayOfWeek=position-temp;
        theDays.add(Calendar.DAY_OF_MONTH,dayOfWeek);
        DayFragment dayfragment= new DayFragment();
        Bundle bundle=new Bundle();
        bundle.putString("theDay",theDays.getTime().toString());
        bundle.putInt("dayNum",position);
        dayfragment.setArguments(bundle);
        return  dayfragment;
    }

    @Override
    public int getCount() {
        return 7;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String day;
        switch (position+1){
            case 1:
                day=context.getString(R.string.SUN);
                break;
            case 2:
                day=context.getString(R.string.MON);
                break;
            case 3:
                day=context.getString(R.string.TUE);
                break;
            case 4:
                day=context.getString(R.string.WED);
                break;
            case 5:
                day=context.getString(R.string.THU);
                break;
            case 6:
                day=context.getString(R.string.FRI);
                break;
            case 7:
                day=context.getString(R.string.SAT);
                break;
                default:
                    return "Day: "+position+1;
        }
        return day;
    }
}
