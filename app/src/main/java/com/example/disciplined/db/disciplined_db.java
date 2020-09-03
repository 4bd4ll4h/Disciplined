package com.example.disciplined.db;

import android.annotation.SuppressLint;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.support.annotation.NonNull;

import com.example.disciplined.backGround_job;
import com.example.disciplined.db.db_tables.taskTable;
import com.example.disciplined.db.db_tables.remainders_table;
import com.example.disciplined.db.db_tables.alarm_table;
import com.example.disciplined.db.db_tables.alarmSitting_table;
import com.example.disciplined.db.db_operation.alarmSitting_oparetion;
import com.example.disciplined.db.db_operation.alarmTable_oparetion;
import com.example.disciplined.db.db_operation.taskTable_oparetion;
import com.example.disciplined.db.db_operation.remainderTable_oparetion;

import java.util.List;


@Database(entities =
        { taskTable.class,remainders_table.class,alarm_table.class,alarmSitting_table.class}
        ,version = 6,exportSchema = true)
public abstract class disciplined_db extends RoomDatabase {

    @SuppressLint("StaticFieldLeak")
    private static disciplined_db Instance;
    @SuppressLint("StaticFieldLeak")
    private static Context contex;

    public abstract alarmSitting_oparetion doa_alarmSitting();
    public abstract alarmTable_oparetion doa_alarmTable();
    public abstract remainderTable_oparetion doa_remainderTable();
    public abstract taskTable_oparetion doa_taskTable();



    public static synchronized disciplined_db getInstance(Context context) {
        contex=context;
        if (Instance == null) {
            Instance = Room.databaseBuilder(context.getApplicationContext(),
                    disciplined_db.class,
                    "disciplined_db").fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();

        }
        return Instance;

    }


    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            RingtoneManager ringtoneManager=new RingtoneManager(contex);
            ringtoneManager.setType(RingtoneManager.TYPE_ALARM);
            final Cursor cursor2=ringtoneManager.getCursor();
            @SuppressLint("StaticFieldLeak") final backGround_job back = new backGround_job(Instance) {
                @Override
                public void setRe(List<alarmSitting_table> re) {

                }

                @Override
                public List<alarmSitting_table> getRe() {
                    return null;
                }

                @Override
                protected List<alarmSitting_table> doInBackground(Void...voids) {

                    setSitting.insertSitting(
                            new alarmSitting_table(
                                    "Good Luck",
                                    5,3,1,
                                    cursor2.getString(RingtoneManager.URI_COLUMN_INDEX)+"/"+cursor2.getString(RingtoneManager.ID_COLUMN_INDEX)
                                    ,cursor2.getString(RingtoneManager.TITLE_COLUMN_INDEX),
                                    10));


                    return null;
                }
            };
            back.execute();
        }
    };

}
