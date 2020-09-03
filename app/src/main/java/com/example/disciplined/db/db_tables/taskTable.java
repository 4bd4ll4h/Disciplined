package com.example.disciplined.db.db_tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Entity
public class taskTable {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private  Long id ;
    @ColumnInfo(name = "title")
    private  String title="" ;
    @ColumnInfo(name = "description")
    private  String description="";
    @ColumnInfo(name = "importance")
    private  int importance;
    @ColumnInfo
    @TypeConverters(Converters.class)
    private Date date;
    @ColumnInfo
    private String Repeat="";

    @ColumnInfo(name = "status")
    private int status=0;

    public taskTable() {
    }

    public taskTable(String title, String description, int importance, Date date, String repeat, int status) {
        this.title = title;
        this.description = description;
        this.importance = importance;
        this.date = date;
        Repeat = repeat;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRepeat() {
        return Repeat;
    }

    public void setRepeat(String repeat) {
        Repeat = repeat;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class Converters {
        @TypeConverter
        public Date fromTimestamp(String value) {
            try {
                Log.i("DateFormatTo",value);
                return value == null ? null :  new SimpleDateFormat("MMM dd yyyy E HH:mm:ss z",Locale.ENGLISH).parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.i("DateFormat","failed");
            return new Date(value);
        }

        @TypeConverter
        public String dateToTimestamp(Date date) {
            if (date == null) {
                return null;
            } else {
                return new SimpleDateFormat("MMM dd yyyy E HH:mm:ss z",Locale.ENGLISH).format(date);
            }
        }
    }
    public static class CompareConverters {
        @TypeConverter
        public Date fromTimestamp(String value) {
            return value == null ? null : new Date(value);
        }

        @TypeConverter
        public static String dateToTimestamp(Date date) {
            if (date == null) {
                return null;
            } else {
                Log.i("DateFormatCompare",new SimpleDateFormat("MMM dd yyyy",Locale.ENGLISH).format(date));
                return new SimpleDateFormat("MMM dd yyyy",Locale.ENGLISH).format(date);
               // return "Thu Apr 30 22:43:52 GMT+02:00 2020";
            }
        }
    }
}
