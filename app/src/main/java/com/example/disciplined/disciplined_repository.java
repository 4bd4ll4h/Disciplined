package com.example.disciplined;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.disciplined.AsyncTask_class.*;
import com.example.disciplined.db.db_operation.*;
import com.example.disciplined.db.db_tables.alarmSitting_table;
import com.example.disciplined.db.db_tables.alarm_table;
import com.example.disciplined.db.db_tables.remainders_table;
import com.example.disciplined.db.db_tables.taskTable;
import com.example.disciplined.db.disciplined_db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class disciplined_repository {
    private List<alarmSitting_table> list = new ArrayList<>();
    private alarmSitting_oparetion sitting_oparetion;
    private alarmTable_oparetion alarmTable_oparetion;
    private remainderTable_oparetion remainderTable_oparetion;
    private taskTable_oparetion taskTable_oparetion;

    private LiveData<List<alarm_table>> alarm_liveData;
    LiveData<ArrayList<remainders_table>> remainder_liveData;
    LiveData<ArrayList<taskTable>> task_liveData;


    public disciplined_repository(Application application) {
        disciplined_db db = disciplined_db.getInstance(application);
        sitting_oparetion = db.doa_alarmSitting();
        alarmTable_oparetion = db.doa_alarmTable();
        remainderTable_oparetion = db.doa_remainderTable();
        taskTable_oparetion = db.doa_taskTable();


    }

    public insertTask getTask(final Long taskId) {
        getTask getTask = new getTask(taskTable_oparetion, alarmTable_oparetion, remainderTable_oparetion);
        getTask.execute(taskId);
        try {
            return getTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return new insertTask(new taskTable(), new ArrayList<remainders_table>(), new ArrayList<alarm_table>(), null);
    }

    public List<alarmSitting_table> getSitting() {

        getSitting getSit = new getSitting(sitting_oparetion);
        getSit.execute();
        try {
            list = getSit.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.i("asyncTaskListOut", "size" + list.size());

        if (getSit.isCancelled()) {
            getSit.execute();
            Log.i("asyncTaskListOutRE9999", "size" + list.size());
            return getSit.getList();
        } else

            return list;

    }


    public void insertSitting(final alarmSitting_table sittingTable) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                sitting_oparetion.insertSitting(sittingTable);

                return null;
            }
        }.execute();
    }

    public void insetTsak(insertTask insertTask) {
        saveTask saveTask = new saveTask(sitting_oparetion, alarmTable_oparetion, remainderTable_oparetion, taskTable_oparetion);
        saveTask.execute(insertTask);
    }

    public List<insertTask> getAllTask(Date date, String day, String order) {
        allTask allTask = new allTask(date, order, day, taskTable_oparetion, alarmTable_oparetion, remainderTable_oparetion);
        allTask.execute();
        try {
            Log.i("safsdaf", allTask.get().size() + "c");
            return allTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteTask(Long task) {
        deleteTask deleteTask = new deleteTask(taskTable_oparetion);
        deleteTask.execute(task);
    }

    public void upateTask(taskTable task) {
        updateTask updateTask = new updateTask(taskTable_oparetion);
        updateTask.execute(task);
    }

    public void upateTask(insertTask insertTask) {
        updateTaskAll updateTaskAll = new updateTaskAll(taskTable_oparetion, alarmTable_oparetion, remainderTable_oparetion);
        updateTaskAll.execute(insertTask);
    }

    public alarmSitting_table getSet(String sitting) {
        getSitting getSitting=new getSitting(sitting_oparetion);
        getSitting.execute(sitting);
        try {
            return getSitting.get().get(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    return new alarmSitting_table();
    }

}

