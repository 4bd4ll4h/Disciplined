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

   /* public static ArrayList<taskTable> SortTasks(int WaysOfSorting, ArrayList<taskTable> TargetArray){
        if(TargetArray.size()==0)
            return TargetArray;
        else {
            ArrayList<taskTable> sortArray=new ArrayList<>();
            switch (WaysOfSorting){
                case 1:
                    sortArray.add(TargetArray.get(0));
                    for(int Tp=1;Tp<TargetArray.size();Tp++)
                    {
                        for (int p=0;p<sortArray.size();p++){

                            if (TargetArray.get(Tp).getTIME_h()<sortArray.get(p).getTIME_h())
                            {
                                sortArray.add(p,TargetArray.get(Tp));
                                break; }
                            else
                            {
                                if (TargetArray.get(Tp).getTIME_h()==sortArray.get(p).getTIME_h())
                                {
                                    if (TargetArray.get(Tp).getTIME_m()<sortArray.get(p).getTIME_m() || TargetArray.get(Tp).getTIME_m()==sortArray.get(p).getTIME_m())
                                    {sortArray.add(p,TargetArray.get(Tp));
                                        break;}else {if (p+1==sortArray.size()){sortArray.add(TargetArray.get(Tp));
                                        break;}}
                                }else {

                                    if (p+1==sortArray.size()){sortArray.add(TargetArray.get(Tp));
                                        break;}
                                }
                            }


                        }
                    }
                    break;
                case 2:
                    sortArray.add(TargetArray.get(0));
                    for(int Tp=1;Tp<TargetArray.size();Tp++)
                    {
                        for (int p=0;p<sortArray.size();p++){

                            if (( TargetArray.get(Tp).getImportance()>sortArray.get(p).getImportance() ||TargetArray.get(Tp).getImportance()==sortArray.get(p).getImportance())  )
                            {
                                sortArray.add(p,TargetArray.get(Tp));
                                break; }
                            else
                            {
                                if (p+1==sortArray.size()){
                                    sortArray.add(TargetArray.get(Tp));
                                    break;}
                            }
                        }

                    }

                    break;
                case 3:

                    break;

            }
            List<TASK> temp = new ArrayList<>();
            for (int p=0;p<sortArray.size();p++){
                float secend;
                secend=SetCounter(sortArray.get(p).getTIME_h(),sortArray.get(p).getTIME_m());
                if(secend<0||secend==0){
                    temp.add(sortArray.get(p));
                }

            }
            sortArray.removeAll(temp);
            sortArray.addAll(temp);
            return sortArray;

        }
    }

*/

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
        Log.i("asdw",s+" <>"+s/60);
        Log.i("asdw", String.valueOf(sec));
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
