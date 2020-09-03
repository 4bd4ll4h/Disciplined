package com.example.disciplined.AsyncTask_class;

import android.os.AsyncTask;

import com.example.disciplined.db.db_operation.taskTable_oparetion;
import com.example.disciplined.db.db_tables.taskTable;

public class deleteTask extends AsyncTask<Long,Void,Void> {
    private com.example.disciplined.db.db_operation.taskTable_oparetion taskTable_oparetion;

    public deleteTask(taskTable_oparetion taskTable_oparetion) {
        this.taskTable_oparetion = taskTable_oparetion;
    }

    @Override
    protected Void doInBackground(Long... taskTables) {

        taskTable_oparetion.DeleteTask(taskTables[0]);
        return null;
    }
}
