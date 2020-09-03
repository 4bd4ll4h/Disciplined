package com.example.disciplined;


import android.os.AsyncTask;

import com.example.disciplined.db.db_operation.alarmSitting_oparetion;
import com.example.disciplined.db.db_tables.alarmSitting_table;
import com.example.disciplined.db.db_tables.taskTable;
import com.example.disciplined.db.disciplined_db;

import java.util.List;

public abstract class backGround_job extends AsyncTask<Void,Void,List<alarmSitting_table>> {
    public alarmSitting_oparetion setSitting;
    public List<alarmSitting_table> re;

    public backGround_job(disciplined_db instance) {
        setSitting = instance.doa_alarmSitting();

    }

    public backGround_job() {

    }

    public abstract void setRe(List<alarmSitting_table> re);

    public abstract List<alarmSitting_table> getRe() ;
    @Override
    protected List<alarmSitting_table> doInBackground(Void... voids) {
        return null;
    }
}

