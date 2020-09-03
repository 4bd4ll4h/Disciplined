package com.example.disciplined;

import android.app.Application;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.disciplined.db.db_tables.alarmSitting_table;
import com.example.disciplined.db.db_tables.taskTable;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.example.disciplined.MainActivity.alarmList;
import static com.example.disciplined.MainActivity.setAlarmList;
import static com.example.disciplined.MainActivity.setDBalarm;
import static com.example.disciplined.MainActivity.snoozeList;
import static com.example.disciplined.alarmReceiver.mediaPlayer;

public class alamLockScreen extends AppCompatActivity {
    private TextView clock, title, details;
    private Button snooze, dismess,done;
    Handler handler;
    int currentIndex;
    Calendar time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final disciplined_repository repository=new disciplined_repository(getApplication());
        final alarmSitting_table sitting=repository.getSet(alarmList.get(currentIndex).getSitting());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alam_lock_screen);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O_MR1){
            this.setTurnScreenOn(true);
        }else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }
        currentIndex = getIntent().getIntExtra("ID", 0);
        time = Calendar.getInstance();


        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    mediaPlayer.start();
                }catch (Exception e){
                    mediaPlayer= MediaPlayer.create(getApplicationContext(), Uri.fromFile(new File(sitting.getSound())));
                    mediaPlayer.start();
                }

                clock = findViewById(R.id.clock);
                clock.setText(NewTask.changeTimeFormat(alarmList.get(currentIndex).getDate().getHours() , alarmList.get(currentIndex).getDate().getMinutes(),getApplicationContext()));
                title = findViewById(R.id.main);
                title.setText(alarmList.get(currentIndex).getTitle());
                details =findViewById(R.id.sub);
                details.setText(alarmList.get(currentIndex).getDescription());
                snooze = findViewById(R.id.snoozeB);
                dismess = findViewById(R.id.dismes);
                done=findViewById(R.id.DoneB);
                if ((snoozeList.contains("R" + alarmList.get(currentIndex).getId()) &&
                        snoozeList.getInt("R" + alarmList.get(currentIndex).getId(), -1) <= 0) ||
                        sitting.getSnoozeIson() == 0)
                    snooze.setVisibility(View.GONE);
                snooze.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        try {
                            mediaPlayer.start();
                        }catch (Exception e){
                            mediaPlayer=MediaPlayer.create(getApplicationContext(), Uri.fromFile(new File(sitting.getSound())));
                            mediaPlayer.start();
                        }
                        int m = (Integer.valueOf(new SimpleDateFormat("mm", Locale.ENGLISH).format(time.getTime())) + sitting.getSnoozeInterval()) % 60;
                        int h = (Integer.valueOf(new SimpleDateFormat("mm",Locale.ENGLISH).format(time.getTime())) + sitting.getSnoozeInterval()) / 60 > 0 ?
                                Integer.valueOf(new SimpleDateFormat("HH",Locale.ENGLISH).format(time.getTime())) + 1 : Integer.valueOf(new SimpleDateFormat("HH",Locale.ENGLISH).format(time.getTime()));
                        snoozeList.edit().putInt("H" + alarmList.get(currentIndex).getId(), h).apply();
                        snoozeList.edit().putInt("M" + alarmList.get(currentIndex).getId(), m).apply();
                        Log.i("azsxdc", h + ":" + m);
                        if (snoozeList.contains("R" + alarmList.get(currentIndex).getId())) {
                            snoozeList.edit().putInt("R" + alarmList.get(currentIndex).getId(), snoozeList.getInt("R" + alarmList.get(currentIndex).getId(), -1) - 1).apply();
                        } else {
                            snoozeList.edit().putInt("R" + alarmList.get(currentIndex).getId(), sitting.getSnoozeRepeat() - 1).apply();
                        }
                        mediaPlayer.reset();
                        setAlarmList(getApplication());
                        finish();

                    }
                });
                dismess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mediaPlayer.reset();

                        if (snoozeList.contains("R"+alarmList.get(currentIndex).getId())){
                            snoozeList.edit().remove("R"+alarmList.get(currentIndex).getId()).apply();
                            snoozeList.edit().remove("H"+alarmList.get(currentIndex).getId()).apply();
                            snoozeList.edit().remove("M"+alarmList.get(currentIndex).getId()).apply();

                        }
                        alarmList.remove(currentIndex);
                        setAlarmList(getApplication());
                        finish();
                    }
                });
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.start();
                    }
                });

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       insertTask task=repository.getTask(alarmList.get(currentIndex).getTaskId());
                       taskTable t=task.getTask();
                       t.setStatus(1);
                       repository.upateTask(t);

                        mediaPlayer.reset();

                        if (snoozeList.contains("R"+alarmList.get(currentIndex).getId())){
                            snoozeList.edit().remove("R"+alarmList.get(currentIndex).getId()).apply();
                            snoozeList.edit().remove("H"+alarmList.get(currentIndex).getId()).apply();
                            snoozeList.edit().remove("M"+alarmList.get(currentIndex).getId()).apply();

                        }
                        alarmList.remove(currentIndex);
                        setDBalarm(getApplication());
                        setAlarmList(getApplication());
                        finish();

                    }
                });

            }});
    }


}
