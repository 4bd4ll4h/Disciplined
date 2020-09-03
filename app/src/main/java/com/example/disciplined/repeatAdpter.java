

package com.example.disciplined;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class repeatAdpter extends PagerAdapter {
    private Context c;
    private String []days=new String[5];
    public static String weekend,work;

     repeatAdpter(Context c) {
        this.c=c;
    }

    int p;
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        p=position;
        final ListView repeatList;
        final ArrayAdapter arrayAdapter;
        View view;
        work="";
        weekend="";
        LayoutInflater inflater =LayoutInflater.from(c);
        if(p==0){
            days=new String[3];
            days[0]=c.getString(R.string.AllWeekend);
            days[1]=c.getString(R.string.Friday);
            days[2]=c.getString(R.string.Saturday);

        }
        if(p == 1){
            days=new String[6];
            days[0]=c.getString(R.string.AllWorkDays);
            days[1]=c.getString(R.string.Sunday);
            days[2]=c.getString(R.string.Monday);
            days[3]=c.getString(R.string.Tuesday);
            days[4]=c.getString(R.string.Wednesday);
            days[5]=c.getString(R.string.Thursday);
        }
        view=inflater.inflate(R.layout.activity_repeat__dialog,container,false);
       if(view==null) Log.i("stage no:","null"+p);
       else Log.i("stage no:",days[1]);

        arrayAdapter=new ArrayAdapter<>(c,android.R.layout.simple_list_item_multiple_choice,days);
        repeatList=view.findViewById(R.id.repeatList);
        repeatList.setAdapter(arrayAdapter);
        repeatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(repeatList.isItemChecked(0) &&position==0){
                    for(int i =0;i<repeatList.getCount();i++) {
                        repeatList.setItemChecked(i, true);

                    }
                }
                if(!repeatList.isItemChecked(0)&&position==0){
                    for(int i =0;i<repeatList.getCount();i++) {
                        repeatList.setItemChecked(i, false);
                    }
                }
                if(!repeatList.isItemChecked(position)&&position!=0){

                        repeatList.setItemChecked(0, false);

                }
                if(position!=0&&repeatList.getCount()==repeatList.getCheckedItemCount()+1){

                    repeatList.setItemChecked(0, true);

                }

                long[] arry3=repeatList.getCheckItemIds();
                if(String.valueOf(arrayAdapter.getItem(0)).equals(c.getString(R.string.AllWorkDays))) {
                    work= "";
                    for (int n = 0; n < arry3.length; n++) {
                        if((int) arry3[n]!=0)
                        work = work+","+String.valueOf(arrayAdapter.getItem((int) arry3[n]));
                    }
                }
                else{
                    weekend= "";
                    for (int n=0;n<arry3.length;n++){
                        if((int) arry3[n]!=0)
                            weekend = weekend+","+String.valueOf(arrayAdapter.getItem((int) arry3[n]));
                    }
                }
                Log.i("aaaav",weekend+work);
            }
        });




container.addView(view);
        return view;
    }


    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==((View) object);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String re = new String();
        if(position==0){
            re=c.getString(R.string.weekend);
        }
        if(position==1){
            re=c.getString(R.string.work_day);
        }
        return re;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}