package com.example.disciplined;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

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
        allTask=refrshTask(repository.getAllTask(date,day,order));
    }

    public void deleteTask(Long task) {
        repository.deleteTask(task);
    }

    public void updateTask(taskTable task) {
        repository.upateTask(task);
    }
    public List<insertTask> refrshTask(List<insertTask> list) {
        List<insertTask> refresdhedList = new ArrayList<>();
        if (!list.isEmpty()) {
            int last = 0, lastT = 0, lastD = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getTask().getStatus() == 1) {
                    refresdhedList.add(lastD, list.get(i));
                    lastD++;
                } else {
                    if (new Date().getHours() > list.get(i).getTask().getDate().getHours() ||
                            (new Date().getHours() == list.get(i).getTask().getDate().getHours() &&
                                    new Date().getMinutes() >= list.get(i).getTask().getDate().getMinutes())) {
                        refresdhedList.add(lastT, list.get(i));
                        lastD++;
                        lastT++;
                    } else {
                        Log.i("checkTreue", "true");
                        refresdhedList.add(last, list.get(i));
                        lastD++;
                        lastT++;
                        last++;
                    }
                }
            }

        }
        return refresdhedList;
    }
}

