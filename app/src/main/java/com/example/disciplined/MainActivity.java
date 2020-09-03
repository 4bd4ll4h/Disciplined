package com.example.disciplined;

import android.app.AlarmManager;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import com.example.disciplined.db.db_tables.alarm_table;
import com.example.disciplined.db.db_tables.remainders_table;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private fragmentAdapter fragmentAdapter;
    private TabLayout daysTab;
    public static   SharedPreferences sorting;
    public static  String SORT;
    public static final String wayOfSorting="wayOfSorting";
    protected static SharedPreferences snoozeList;
    protected static ArrayList<remainders_table> remainderOfDay;
    protected static ArrayList<alarm_table>alarmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sorting = Objects.requireNonNull(this).getSharedPreferences("package com.example.disciplined", Context.MODE_PRIVATE);

        SORT=sorting.getString(wayOfSorting,"Date ASC");


        if(!method.checkOverlayPremission(this)){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle(R.string.Screenoverlaydetected);
            builder.setMessage(R.string.toMake);
            builder.setPositiveButton(R.string.OPENSETTINGS, new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent =new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+getPackageName()));
                    startActivity(intent);
                }
            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
        }


        viewPager=findViewById(R.id.pager);
        fragmentAdapter=new fragmentAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(fragmentAdapter);

        daysTab=findViewById(R.id.tabs);
        daysTab.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1);




        snoozeList=getSharedPreferences("package com.example.athefourth.sia", Context.MODE_PRIVATE);

        am12check(getApplication());
        setDbReminder(getApplication());
        setRemainderOfDay(getApplication());
        setDBalarm(getApplication());
        setAlarmList(getApplication());
    }


    public static void am12check(Context context){
        Calendar am=Calendar.getInstance();
        am.set(am.get(Calendar.YEAR),
                am.get(Calendar.MONTH),
                am.get(Calendar.DAY_OF_MONTH)+1,
                0,0,0);
        AlarmManager am12=(AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent=new Intent(context,am12.class);
        PendingIntent pendIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (am12 != null) {
            am12.setInexactRepeating(AlarmManager.RTC_WAKEUP, am.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendIntent);
        }

    }


    public static void setDbReminder(Application context){
        remainderOfDay=new ArrayList<>();
        disciplined_repository repository=new disciplined_repository(context);

        ArrayList<insertTask>list= (ArrayList<insertTask>) repository.getAllTask(new Date(),
                new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date()),SORT);
        Log.i("test@1",list.size()+" current ");
        Date date=new Date();
        for(Iterator<insertTask> tasks = list.iterator(); tasks.hasNext();){
            insertTask Task=tasks.next();

            if(Task.getTask().getStatus()!=1)
            remainderOfDay.addAll(Task.getRemainders());
        }
        for (Iterator<remainders_table>remainder=remainderOfDay.iterator();remainder.hasNext();){
            remainders_table remainders_table=remainder.next();
            if(remainders_table.getDate().getTime()<Calendar.getInstance().getTimeInMillis()){

                remainder.remove();
            }
        }
        Log.i("test@1",remainderOfDay.size()+" DEL current ");

    }

    public static void setRemainderOfDay(Application context){

        Log.i("test@1",remainderOfDay.size()+" remainders");
        int index=0;
        for(int u=0;u<remainderOfDay.size();u++){
            if(u!=0&&(remainderOfDay.get(u).getDate().getTime()<remainderOfDay.get(index).getDate().getTime()) )
                index=u;
        }
        Log.i("test@1",index+" current index");
        if(!remainderOfDay.isEmpty()) {
            AlarmManager remainderAlarm = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            calendar.set(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    remainderOfDay.get(index).getDate().getHours(),
                    remainderOfDay.get(index).getDate().getMinutes(),
                    0);
            Intent intent = new Intent(context, remainderList.class);
            intent.putExtra("ID", index);
            PendingIntent pendingIntent = PendingIntent.getService(context,  remainderOfDay.get(index).getId(), intent, 0);
            if (remainderAlarm != null) {
                remainderAlarm.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }

    public static void setDBalarm(Application context) {
        alarmList=new ArrayList<>();

        disciplined_repository repository=new disciplined_repository(context);

        ArrayList<insertTask>list= (ArrayList<insertTask>) repository.getAllTask(new Date(),
                new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date()),SORT);
        Log.i("test@1",list.size()+" current ");
        Date date=new Date();
        for(Iterator<insertTask> tasks = list.iterator(); tasks.hasNext();){
            insertTask Task=tasks.next();

            if(Task.getTask().getStatus()!=1)
                alarmList.addAll(Task.getAlarms());
        }

        snoozeList=context.getSharedPreferences("package com.example.athefourth.sia", Context.MODE_PRIVATE);
        for(Iterator<alarm_table>alarmey=alarmList.iterator();alarmey.hasNext();){
            alarm_table alarmy2=alarmey.next();
            if(!snoozeList.contains("R"+alarmy2.getId()))
                if(alarmy2.getDate().getTime()<Calendar.getInstance().getTimeInMillis())
                {alarmey.remove();}
        }

    }

    public static void setAlarmList(Application context){
        int index=0;
        snoozeList=context.getSharedPreferences("package com.example.athefourth.sia", Context.MODE_PRIVATE);

        for(int u=0;u<alarmList.size();u++) {
            int snoozeH,snoozeM,snoozeR;
            snoozeH =  snoozeList.getInt("H"+alarmList.get(u).getId(), 0);
            snoozeM =  snoozeList.getInt("M"+alarmList.get(u).getId(), 0);
            snoozeR =  snoozeList.getInt("R"+alarmList.get(u).getId(), -1);
            if (snoozeR == -1) {
                if (u != 0 && alarmList.get(u).getDate().getTime() < alarmList.get(index).getDate().getTime() )
                    index = u;
            }

            else {

                Log.i("asdfv",snoozeH+":"+snoozeM+":"+snoozeR);
                if(snoozeR>=0){
                    if (u != 0 && (snoozeH < alarmList.get(index).getDate().getHours() ||
                            (snoozeH == alarmList.get(index).getDate().getHours()
                                    && snoozeM < alarmList.get(index).getDate().getHours())))
                        index = u;

                }
                else {
                    snoozeList.edit().remove("H"+alarmList.get(u).getId()).apply();
                    snoozeList.edit().remove("M"+alarmList.get(u).getId()).apply();
                    snoozeList.edit().remove("R"+alarmList.get(u).getId()).apply();

                }
            }
        }

        if(!alarmList.isEmpty()) {
            AlarmManager remainderAlarm = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            calendar.set(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    snoozeList.contains("H"+alarmList.get(index).getId())?
                            snoozeList.getInt("H"+alarmList.get(index).getId(),0):
                            alarmList.get(index).getDate().getHours(),
                    snoozeList.contains("M"+alarmList.get(index).getId())?
                            snoozeList.getInt("M"+alarmList.get(index).getId(),0):
                            alarmList.get(index).getDate().getMinutes(),
                    0);
            Log.i("asdfv",calendar.get(Calendar.HOUR)+":"+calendar.get(Calendar.MINUTE));
            Intent intent = new Intent(context, alarmReceiver.class);
            intent.putExtra("ID", index);

            PendingIntent pendingIntent = PendingIntent.getService(context, (int) alarmList.get(index).getId(), intent, 0);
            remainderAlarm.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }else Log.i("asdfv","empty "+alarmList.size());
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.New_task) {
            DatePickerDialog pickerDialog =new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                   Date date= new Date(year-1900,month,dayOfMonth);
                    startActivity(new Intent(MainActivity.this,goTo.class).putExtra("theDay",date.toString()));

                }
            },Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DATE));
            pickerDialog.show();

            return true;
        }

        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        am12check(getApplication());
        setDbReminder(getApplication());
        setRemainderOfDay(getApplication());
        setDBalarm(getApplication());
        setAlarmList(getApplication());
    }

    public void addnewTask(View view) {
        Intent i=new Intent(MainActivity.this,NewTask.class);
        startActivity(i);
    }
}
