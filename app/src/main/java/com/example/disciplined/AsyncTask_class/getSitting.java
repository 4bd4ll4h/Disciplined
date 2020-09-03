package com.example.disciplined.AsyncTask_class;

import android.os.AsyncTask;
import android.util.Log;
import com.example.disciplined.db.db_operation.alarmSitting_oparetion;
import com.example.disciplined.db.db_tables.alarmSitting_table;
import java.util.ArrayList;
import java.util.List;

public class getSitting extends AsyncTask<String ,Void,  List<alarmSitting_table>> {
    private alarmSitting_oparetion sitting_oparetion;
     private List<alarmSitting_table> list=new ArrayList<>();

    public getSitting(alarmSitting_oparetion sitting_oparetion) {
        this.sitting_oparetion=sitting_oparetion;
    }

    public List<alarmSitting_table> getList() {
        return list;
    }

    public void setList(List<alarmSitting_table> list) {
        this.list = list;
    }

    @Override
    protected List<alarmSitting_table> doInBackground(String ... voids) {
        Log.i("asyncTaskExex","size"+sitting_oparetion.savedSittings().size());
        if (voids.length!=0){
            List<alarmSitting_table> resule= new ArrayList<alarmSitting_table>();
                    resule.add(sitting_oparetion.getSet(voids[0]));
            return resule;
        }
        return sitting_oparetion.savedSittings();
    }
    @Override
    protected void onPostExecute(List<alarmSitting_table> alarmSitting_tables) {
        list=alarmSitting_tables;
        super.onPostExecute(alarmSitting_tables);
    }


}
