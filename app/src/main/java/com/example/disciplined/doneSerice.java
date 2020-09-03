package com.example.disciplined;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.disciplined.db.db_tables.taskTable;

import static com.example.disciplined.MainActivity.SORT;
import static com.example.disciplined.MainActivity.remainderOfDay;
import static com.example.disciplined.MainActivity.setAlarmList;
import static com.example.disciplined.MainActivity.setDBalarm;
import static com.example.disciplined.MainActivity.setDbReminder;
import static com.example.disciplined.MainActivity.setRemainderOfDay;

public class doneSerice extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public doneSerice() {
        super("debug");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Long currentIn=intent.getLongExtra("id",0);
        disciplined_repository repository=new disciplined_repository((getApplication()));
        insertTask t=repository.getTask(currentIn);
        t.getTask().setStatus(1);
        Log.i("adddzsx", "update");
        repository.upateTask(t.getTask());
        setDbReminder(getApplication());
        setRemainderOfDay(getApplication());
        setDBalarm(getApplication());
        setAlarmList(getApplication());

    }
}
