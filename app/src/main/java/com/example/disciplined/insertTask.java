package com.example.disciplined;

import com.example.disciplined.db.db_tables.alarmSitting_table;
import com.example.disciplined.db.db_tables.alarm_table;
import com.example.disciplined.db.db_tables.remainders_table;
import com.example.disciplined.db.db_tables.taskTable;

import java.util.ArrayList;
import java.util.List;

public class insertTask {
    private taskTable task;
    private List<remainders_table> remainders;
    private List<alarm_table> alarms;
    private alarmSitting_table sitting;

    public insertTask(taskTable task, List<remainders_table> remainders, List<alarm_table> alarms, alarmSitting_table sitting) {
        this.task = task;
        this.remainders = remainders;
        this.alarms = alarms;
        this.sitting = sitting;
    }

    public taskTable getTask() {
        return task;
    }

    public void setTask(taskTable task) {
        this.task = task;
    }

    public List<remainders_table> getRemainders() {
        return remainders;
    }

    public void setRemainders(ArrayList<remainders_table> remainders) {
        this.remainders = remainders;
    }

    public List<alarm_table> getAlarms() {
        return alarms;
    }

    public void setAlarms(ArrayList<alarm_table> alarms) {
        this.alarms = alarms;
    }

    public alarmSitting_table getSitting() {
        return sitting;
    }

    public void setSitting(alarmSitting_table sitting) {
        this.sitting = sitting;
    }
}
