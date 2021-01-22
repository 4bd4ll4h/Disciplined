package com.example.disciplined;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.example.disciplined.db.db_tables.taskTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class method {


    public static float SetCounter(int Houre, int Muntie){

        float sec ;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH",Locale.ENGLISH);
        Long h= Long.valueOf(simpleDateFormat.format(new Date()));
        simpleDateFormat=new SimpleDateFormat("mm", Locale.ENGLISH);
        Long m= Long.valueOf(simpleDateFormat.format(new Date()));
        simpleDateFormat=new SimpleDateFormat("ss",Locale.ENGLISH);
        float s= Float.parseFloat(simpleDateFormat.format(new Date()));
        h=h*60;
        Houre=Houre*60;
        sec= 60 * ((Houre + Muntie) - (h + m + (s / 60)));
        return sec;
    }


    public static boolean checkOverlayPremission(Context c){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!Settings.canDrawOverlays(c)){

                return false;

            }
            else return true;
        }return false;
    }
}
