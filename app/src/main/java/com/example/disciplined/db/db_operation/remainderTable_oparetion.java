package com.example.disciplined.db.db_operation;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.disciplined.db.db_tables.remainders_table;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface remainderTable_oparetion {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRemainder(remainders_table... remainders);

    @Query("SELECT * FROM remainders_table WHERE taskId==:taskId")
    LiveData<List<remainders_table>>getRemainders(Long taskId);

    @Query("SELECT * FROM remainders_table WHERE taskId==:taskId")
    List<remainders_table>getRemainder(Long taskId);

    @Query("DELETE FROM remainders_table WHERE taskId ==:id")
    void deleteRemainders(Long id);

    @Insert()
    void insertRemainder(List<remainders_table> remaind);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRemainder(List<remainders_table> remainders_tables);
}
