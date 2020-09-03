package com.example.disciplined;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.disciplined.db.db_tables.alarm_table;
import com.example.disciplined.db.db_tables.remainders_table;
import com.example.disciplined.db.db_tables.taskTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class viewModle extends AndroidViewModel {

    private disciplined_repository repository;
    LiveData<ArrayList<alarm_table>> alarm_liveData;
    LiveData<ArrayList<remainders_table>> remainder_liveData;
    LiveData<ArrayList<taskTable>> task_liveData;
    MutableLiveData<List<insertTask>> allTask=new MutableLiveData<>();


    public viewModle(@NonNull Application application) {
        super(application);
        repository = new disciplined_repository(application);

    }

    public void getTask(Long TaskId){
        repository.getTask(TaskId);
    }

    public LiveData getAllTask(){
        return allTask;
    }
    public void setAllTask(Date date,String day,String order){


        allTask.setValue(repository.getAllTask(date,day,order));

    }

    public void deleteTask(Long task) {
        repository.deleteTask(task);
    }

    public void updateTask(taskTable task) {
        repository.upateTask(task);
    }
}
