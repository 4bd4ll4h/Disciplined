package com.example.disciplined.db.db_tables;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

@Entity(foreignKeys =
        {@ForeignKey(entity = alarmSitting_table.class
        , parentColumns = "alarmName", childColumns = "sitting"),
        @ForeignKey(entity = taskTable.class
                , parentColumns = "id", childColumns = "taskId"
                , onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)})
public class alarm_table {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(index = true)
    private Long taskId;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "description")
    private String description;
    @TypeConverters(taskTable.Converters.class)
    private Date date;
    @ColumnInfo(name = "sitting",index = true)
    private String sitting;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSitting() {
        return sitting;
    }

    public void setSitting(String sitting) {
        this.sitting = sitting;
    }
}
