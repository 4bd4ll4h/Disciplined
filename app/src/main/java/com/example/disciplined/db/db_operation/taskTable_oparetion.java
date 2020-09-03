package com.example.disciplined.db.db_operation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;
import com.example.disciplined.db.db_tables.taskTable;


import java.util.Date;
import java.util.List;

@Dao
public interface taskTable_oparetion {

    @Insert
    Long[] InsertTask(taskTable... task);

    @RawQuery
    List<taskTable> GetDayTask(SupportSQLiteQuery query);

    @Query("SELECT * FROM taskTable")
    List<taskTable> GetDayTask();

    @Query("DELETE  FROM taskTable WHERE id==:taskTable")
    void DeleteTask(Long taskTable);

    @Query("SELECT * FROM taskTable WHERE id ==  :id")
    taskTable getTask(Long id);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(taskTable...taskTables);







}
