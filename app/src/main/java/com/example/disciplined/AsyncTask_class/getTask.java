package com.example.disciplined.AsyncTask_class;

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

public class getTask extends AsyncTask<Long, Void, insertTask> {

    private taskTable_oparetion taskTable_oparetion;
    private alarmTable_oparetion alarmTable_oparetion;
    private remainderTable_oparetion remainderTable_oparetion;

    public getTask(taskTable_oparetion taskTable_oparetion,
                   alarmTable_oparetion alarmTable_oparetion,
                   remainderTable_oparetion remainderTable_oparetion) {

        this.taskTable_oparetion = taskTable_oparetion;
        this.alarmTable_oparetion = alarmTable_oparetion;
        this.remainderTable_oparetion = remainderTable_oparetion;
    }

    @Override
    protected insertTask doInBackground(Long... id) {
        insertTask list;
        taskTable table = taskTable_oparetion.getTask(id[0]);
        List reminder = new ArrayList<remainders_table>();
        List alaarms = new ArrayList<alarm_table>();
        reminder = remainderTable_oparetion.getRemainder(table.getId());
        alaarms = alarmTable_oparetion.getAlarm(table.getId());
        list = new insertTask(table, reminder, alaarms, null);

        return list;
    }
}