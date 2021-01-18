package com.example.disciplined;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.example.disciplined.db.db_tables.taskTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class viewModle extends AndroidViewModel {

    private disciplined_repository repository;
    List<insertTask> allTask=new ArrayList<>();


    public viewModle(@NonNull Application application) {
        super(application);
        repository = new disciplined_repository(application);

    }

    public void getTask(Long TaskId){
        repository.getTask(TaskId);
    }

    public List<insertTask> getAllTask(){
        return allTask;
    }
    public void setAllTask(Date date,String day,String order){
        allTask=repository.getAllTask(date,day,order);
    }

    public void deleteTask(Long task) {
        repository.deleteTask(task);
    }

    public void updateTask(taskTable task) {
        repository.upateTask(task);
    }
}
