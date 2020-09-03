package com.example.disciplined.db.db_operation;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.disciplined.db.db_tables.alarm_table;


import java.util.ArrayList;
import java.util.List;

@Dao
public interface alarmTable_oparetion {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlarm(alarm_table... alarms);

    @Query("SELECT * FROM alarm_table WHERE taskId==:taskId")
    LiveData<List<alarm_table>> getAlarms(Long taskId);

    @Query("SELECT * FROM alarm_table WHERE taskId==:taskId")
    List<alarm_table> getAlarm(Long taskId);

    @Query("DELETE FROM alarm_table WHERE  taskId==:id")
    void deleteAlarms(Long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlarm(List<alarm_table> alarms);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateAlarm(List<alarm_table> alarms);
}
