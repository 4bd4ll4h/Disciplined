package com.example.disciplined.AsyncTask_class;

import android.os.AsyncTask;

import com.example.disciplined.db.db_operation.taskTable_oparetion;
import com.example.disciplined.db.db_tables.taskTable;

public class updateTask extends AsyncTask<taskTable,Void,Void> {
    private com.example.disciplined.db.db_operation.taskTable_oparetion taskTable_oparetion;

    public updateTask(com.example.disciplined.db.db_operation.taskTable_oparetion taskTable_oparetion) {
        this.taskTable_oparetion = taskTable_oparetion;
    }

    @Override
    protected Void doInBackground(taskTable... taskTables) {

        taskTable_oparetion.updateTask(taskTables);
        return null;
    }
}

