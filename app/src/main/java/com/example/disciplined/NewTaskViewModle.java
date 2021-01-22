package com.example.disciplined;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.disciplined.db.db_tables.alarmSitting_table;
import com.example.disciplined.db.db_tables.alarm_table;
import com.example.disciplined.db.db_tables.remainders_table;
import com.example.disciplined.db.db_tables.taskTable;

public class NewTaskViewModle extends AndroidViewModel {

    taskTable task = new taskTable("", "", 2, null, "", 0);
    alarm_table alarm=new alarm_table();
    remainders_table remainders = new remainders_table();
    private ArrayList<alarmSitting_table> sitting;
    alarmSitting_table selectedSitting;
    private disciplined_repository repository;
    private MutableLiveData<List<remainders_table>> remaindersLis = new MutableLiveData<>();
    private MutableLiveData<List<alarm_table>> alarmList = new MutableLiveData<>();
    insertTask insertTask;

    Long id;
    String importance = getApplication().getString(R.string.importance_from_1_to_10);
    int count;

    public NewTaskViewModle(@NonNull Application application) {
        super(application);
        repository = new disciplined_repository(application);
        setSelectedSitting(0);
        count = 0;
        Log.i("trckedIDIN",sitting.size()+"");


    }

    public NewTaskViewModle(@NonNull Application application, Long i) {
        super(application);
        repository = new disciplined_repository(application);
         insertTask= repository.getTask(i);
        task=insertTask.getTask();
        remaindersLis.setValue(insertTask.getRemainders());
        alarmList.setValue(insertTask.getAlarms());
        setSelectedSitting(0);
        this.id = i;
        count = 0;


    }

    void reNew() {
        remainders = new remainders_table();
    }

    void addreminder(remainders_table remainders_table) {
        remainders_table.setId(count);
        count++;
        List<remainders_table> r = new ArrayList<>();
        if (remaindersLis.getValue() != null) {
            r = remaindersLis.getValue();
        }
        r.add(remainders_table);
        remaindersLis.setValue(r);
    }

    public LiveData<List<remainders_table>> getRmainder() {
        return remaindersLis;
    }

    public void deleteRemainder(int pos) {
        List<remainders_table> r = remaindersLis.getValue();
        r.remove(pos);
        remaindersLis.setValue(r);
    }

    void addAlarm(alarm_table alarm_table) {
        alarm_table.setId(count);
        count++;
        if(selectedSitting.getAlarmName().equals(""))
            // password here is a joke XD just used as unique set of character :)
        selectedSitting.setAlarmName("password is a the fourth "+Calendar.getInstance().toString());
        repository.insertSitting(selectedSitting);
        alarm_table.setSitting(selectedSitting.getAlarmName());
        List<alarm_table> r = new ArrayList<>();
        if (alarmList.getValue() != null) {
            r = alarmList.getValue();
        }
        r.add(alarm_table);
        alarmList.setValue(r);
    }
    void reNewAlarm(){
        alarm=new alarm_table();
    }

    public void deleteAlarm(int pos) {
        List<alarm_table> r = alarmList.getValue();
        r.remove(pos);
        alarmList.setValue(r);
    }


    public void insertTask(){
        repository.insetTsak(new insertTask(task,remaindersLis.getValue(),alarmList.getValue(),null));
    }


    public LiveData<List<alarm_table>> getAlarm() {
        return alarmList;
    }


    public ArrayList<alarmSitting_table> getSitting() {
        return sitting;
    }

    public alarmSitting_table getSelectedSitting() {
        return selectedSitting;
    }

    public void setSelectedSitting(int pos) {


        sitting= (ArrayList<alarmSitting_table>) repository.getSitting();

        selectedSitting = sitting.get(pos);
    }

    public void updateTask() {

        repository.upateTask(new insertTask(task,remaindersLis.getValue(),alarmList.getValue(),null));
    }
}
