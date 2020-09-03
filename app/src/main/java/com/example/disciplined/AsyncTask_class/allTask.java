package com.example.disciplined.AsyncTask_class;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.TypeConverters;
import android.os.AsyncTask;
import android.util.Log;

import com.example.disciplined.db.db_operation.alarmTable_oparetion;
import com.example.disciplined.db.db_operation.remainderTable_oparetion;
import com.example.disciplined.db.db_operation.taskTable_oparetion;
import com.example.disciplined.db.db_tables.alarm_table;
import com.example.disciplined.db.db_tables.remainders_table;
import com.example.disciplined.db.db_tables.taskTable;
import com.example.disciplined.insertTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class allTask extends AsyncTask<Void,Void, List<insertTask>> {
    private Date date;
    private String order,day;
    private taskTable_oparetion taskTable_oparetion;
    private alarmTable_oparetion alarmTable_oparetion;
    private remainderTable_oparetion remainderTable_oparetion;

    public allTask(Date date, String order, String day,
                   taskTable_oparetion taskTable_oparetion,
                   alarmTable_oparetion alarmTable_oparetion,
                   remainderTable_oparetion remainderTable_oparetion) {
        this.date = date;
        this.order = order;
        this.day = day;
        this.taskTable_oparetion = taskTable_oparetion;
        this.alarmTable_oparetion = alarmTable_oparetion;
        this.remainderTable_oparetion = remainderTable_oparetion;
    }

    @Override
    protected List<insertTask> doInBackground(Void... voids) {
        List<insertTask> list=new ArrayList<>();
        String value = "SELECT * FROM taskTable WHERE taskTable.date LIKE '%"+
                taskTable.CompareConverters.dateToTimestamp(date)
                +"%'OR Repeat LIKE '%"+day+"%' ORDER BY "+order;
        SupportSQLiteQuery sqLiteQuery=new SimpleSQLiteQuery(value);

        if(taskTable_oparetion.GetDayTask(sqLiteQuery)!=null){
            Log.i("safsdaf",taskTable_oparetion.GetDayTask(sqLiteQuery).size()+"a");

            for (taskTable table:taskTable_oparetion.GetDayTask(sqLiteQuery)){
            List reminder=new ArrayList<remainders_table>();
            List alaarms=new ArrayList<alarm_table>();
            reminder=  remainderTable_oparetion.getRemainder((long) table.getId());
            alaarms=  alarmTable_oparetion.getAlarm((long) table.getId());
            list.add(new insertTask(table,reminder,alaarms,null));
        }}
        return list;
    }
}
