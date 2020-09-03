package com.example.disciplined.db.db_operation;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.disciplined.db.db_tables.alarmSitting_table;


import java.util.ArrayList;
import java.util.List;

@Dao
public interface alarmSitting_oparetion {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
     Long  insertSitting(alarmSitting_table sitting_table);
//
    @Query("select * from alarmSitting_table WHERE alarmName  NOT LIKE '%password is a the fourth%'")
    List<alarmSitting_table> savedSittings();


    @Query("SELECT * FROM alarmSitting_table WHERE alarmName == :name")
    alarmSitting_table getSet(String name);

}
