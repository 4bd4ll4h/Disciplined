package com.example.disciplined.db.db_tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class alarmSitting_table {

    @ColumnInfo
    @PrimaryKey
    private @NonNull String  alarmName;
    @ColumnInfo
    private int snoozeInterval;
    @ColumnInfo
    private int snoozeRepeat;
    @ColumnInfo
    private int snoozeIson;
    @ColumnInfo
    private String sound;
    @ColumnInfo
    private String soundName="";
    @ColumnInfo
    private int volume=7;


    public alarmSitting_table() {
    }

    public alarmSitting_table(@NonNull String alarmName, int snoozeInterval, int snoozeRepeat, int snoozeIson, String sound, String soundName, int volume) {
        this.alarmName = alarmName;
        this.snoozeInterval = snoozeInterval;
        this.snoozeRepeat = snoozeRepeat;
        this.snoozeIson = snoozeIson;
        this.sound = sound;
        this.soundName = soundName;
        this.volume = volume;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public int getSnoozeInterval() {
        return snoozeInterval;
    }

    public void setSnoozeInterval(int snoozeInterval) {
        this.snoozeInterval = snoozeInterval;
    }

    public int getSnoozeRepeat() {
        return snoozeRepeat;
    }

    public void setSnoozeRepeat(int snoozeRepeat) {
        this.snoozeRepeat = snoozeRepeat;
    }

    public int getSnoozeIson() {
        return snoozeIson;
    }

    public void setSnoozeIson(int snoozeIson) {
        this.snoozeIson = snoozeIson;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getSoundName() {
        return soundName;
    }

    public void setSoundName(String soundName) {
        this.soundName = soundName;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
