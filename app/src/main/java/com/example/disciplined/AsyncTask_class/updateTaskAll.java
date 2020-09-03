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
import java.util.List;

public class updateTaskAll extends AsyncTask<insertTask, Void, Void> {

    private com.example.disciplined.db.db_operation.taskTable_oparetion taskTable_oparetion;
    private com.example.disciplined.db.db_operation.alarmTable_oparetion alarmTable_oparetion;
    private com.example.disciplined.db.db_operation.remainderTable_oparetion remainderTable_oparetion;

    public updateTaskAll(taskTable_oparetion taskTable_oparetion,
                         alarmTable_oparetion alarmTable_oparetion,
                         remainderTable_oparetion remainderTable_oparetion) {

        this.taskTable_oparetion = taskTable_oparetion;
        this.alarmTable_oparetion = alarmTable_oparetion;
        this.remainderTable_oparetion = remainderTable_oparetion;
    }

    @Override
    protected Void doInBackground(insertTask... tasks) {
        int i = 0;
        alarmTable_oparetion.deleteAlarms(tasks[i].getTask().getId());
        remainderTable_oparetion.deleteRemainders(tasks[i].getTask().getId());
        taskTable_oparetion.updateTask(tasks[i].getTask());
        List<alarm_table> alarms=tasks[i].getAlarms();
        List<remainders_table> remaind;
        if (alarms!=null) {
            for (int o = 0; o < alarms.size(); o++) {
                alarms.get(o).setTaskId(tasks[i].getTask().getId());
            }
            alarmTable_oparetion.insertAlarm(alarms);

        }
        remaind=tasks[i].getRemainders();
        if (remaind!=null) {
            for (int o = 0; o < remaind.size(); o++) {
                remaind.get(o).setTaskId(tasks[i].getTask().getId());
            }
            remainderTable_oparetion.insertRemainder(remaind);
        }
        i++;
        return null;
    }
}