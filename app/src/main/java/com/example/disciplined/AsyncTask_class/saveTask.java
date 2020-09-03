package com.example.disciplined.AsyncTask_class;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.disciplined.db.db_operation.alarmSitting_oparetion;
import com.example.disciplined.db.db_operation.alarmTable_oparetion;
import com.example.disciplined.db.db_operation.remainderTable_oparetion;
import com.example.disciplined.db.db_operation.taskTable_oparetion;
import com.example.disciplined.db.db_tables.alarmSitting_table;
import com.example.disciplined.db.db_tables.alarm_table;
import com.example.disciplined.db.db_tables.remainders_table;
import com.example.disciplined.insertTask;

import java.util.ArrayList;
import java.util.List;

public class saveTask extends AsyncTask<insertTask,Void,Long> {

    private alarmSitting_oparetion sitting_oparetion;
    private alarmTable_oparetion alarmTable_oparetion;
    private remainderTable_oparetion remainderTable_oparetion;
    private taskTable_oparetion taskTable_oparetion;

    public saveTask(alarmSitting_oparetion sitting_oparetion,
                    com.example.disciplined.db.db_operation.alarmTable_oparetion alarmTable_oparetion,
                    com.example.disciplined.db.db_operation.remainderTable_oparetion remainderTable_oparetion,
                    com.example.disciplined.db.db_operation.taskTable_oparetion taskTable_oparetion) {
        this.sitting_oparetion = sitting_oparetion;
        this.alarmTable_oparetion = alarmTable_oparetion;
        this.remainderTable_oparetion = remainderTable_oparetion;
        this.taskTable_oparetion = taskTable_oparetion;
    }

    @Override
    protected Long doInBackground(insertTask... insertTasks) {
        int i=0;
        Long[] id=taskTable_oparetion.InsertTask(insertTasks[i].getTask());
        Log.i("getMothod",id[i]+"--");
         List<alarm_table> alarms;
         List<remainders_table> remaind;
         alarms=insertTasks[i].getAlarms();
         if (alarms!=null) {
             for (int o = 0; o < alarms.size(); o++) {
                 alarms.get(o).setTaskId(id[i]);
             }
             alarmTable_oparetion.insertAlarm(alarms);

         }
         remaind=insertTasks[i].getRemainders();
         if (remaind!=null) {
             for (int o = 0; o < remaind.size(); o++) {
                 remaind.get(o).setTaskId(id[i]);
             }
             remainderTable_oparetion.insertRemainder(remaind);
         }
         i++;
         return null;
    }
}
