package com.example.disciplined;

import android.app.Application;
import android.app.IntentService;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class alarmReceiver extends IntentService {
    private View unlockView;
    private WindowManager wm;
    private TextView clock, title, details;
    private Button snooze, dismess,done;
    int currentIndex = 0;
    Calendar time;
    static MediaPlayer mediaPlayer;
    AudioManager audioManager;
    WindowManager.LayoutParams params;
    Context context = this;


    public alarmReceiver() {
        super("alarmReceiver");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        currentIndex = intent.getIntExtra("ID", 0);

        return START_NOT_STICKY;

    }

    @Override
    public void onCreate() {
        super.onCreate();

        setDBalarm((Application) context.getApplicationContext());
        if(!alarmList.isEmpty()) {
            final disciplined_repository repository = new disciplined_repository((Application) context.getApplicationContext());
            final alarmSitting_table sitting = repository.getSet(alarmList.get(currentIndex).getSitting());
            if (method.checkOverlayPremission(context)) {
                audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                if (audioManager != null) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, sitting.getVolume(), 0);
                    Log.i("dataBase9", sitting.getVolume() + "k");

                }
                mediaPlayer = new MediaPlayer();
                mediaPlayer = MediaPlayer.create(context, Uri.parse(sitting.getSound()));
                snoozeList = context.getSharedPreferences("package com.example.athefourth.sia", Context.MODE_PRIVATE);
                time = Calendar.getInstance();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                try {
                    mediaPlayer.start();
                } catch (Exception e) {
                    mediaPlayer = MediaPlayer.create(context, Uri.fromFile(new File(sitting.getSound())));
                    mediaPlayer.start();
                }
                if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH && isDeviceLock(context))) {
//            IntentFilter filter = new IntentFilter();
//            filter.addAction(Intent.ACTION_SCREEN_ON);
//            filter.addAction(ACTION_DEBUG);
//            registerReceiver(overlayReceiver, filter);
                    Intent ii = new Intent(context, alamLockScreen.class);
                    ii.putExtra("ID", currentIndex);
                    ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.getApplicationContext().startActivity(ii);
                }
                else {
                    wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        params = new WindowManager.LayoutParams(
                                WindowManager.LayoutParams.WRAP_CONTENT,
                                WindowManager.LayoutParams.WRAP_CONTENT ,
                                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                                PixelFormat.TRANSLUCENT);
                    }
                    else  params = new WindowManager.LayoutParams(
                            WindowManager.LayoutParams.WRAP_CONTENT,
                            WindowManager.LayoutParams.WRAP_CONTENT ,
                            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                            | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                            PixelFormat.TRANSLUCENT);
                    int resources = 0;
                    resources = R.layout.alarm_ring_unlock;
                    final int finalResources = resources;
                    LayoutInflater inflater = LayoutInflater.from(context);
                    unlockView = inflater.inflate(finalResources, null);
                    clock = unlockView.findViewById(R.id.clock);
                    clock.setText(NewTask.changeTimeFormat(alarmList.get(currentIndex).getDate().getHours(), alarmList.get(currentIndex).getDate().getMinutes(), context));
                    title = unlockView.findViewById(R.id.main);
                    title.setText(alarmList.get(currentIndex).getTitle());
                    details = unlockView.findViewById(R.id.sub);
                    details.setText(alarmList.get(currentIndex).getDescription());
                    snooze = unlockView.findViewById(R.id.snoozeB);
                    dismess = unlockView.findViewById(R.id.dismes);
                    done = unlockView.findViewById(R.id.DoneB);
                    Log.i("snoozeInfo", snoozeList.getInt("R" + alarmList.get(currentIndex).getId(), -1) + "");
                    if ((snoozeList.contains("R" + alarmList.get(currentIndex).getId()) &&
                            snoozeList.getInt("R" + alarmList.get(currentIndex).getId(), -1) <= 0) ||
                            sitting.getSnoozeIson() == 0)
                        snooze.setVisibility(View.GONE);
                    snooze.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            mediaPlayer.reset();
                            wm.removeView(unlockView);
                            int m = (Integer.valueOf(new SimpleDateFormat("mm", Locale.ENGLISH).format(time.getTime())) + sitting.getSnoozeInterval()) % 60;
                            int h = (Integer.valueOf(new SimpleDateFormat("mm", Locale.ENGLISH).format(time.getTime())) + sitting.getSnoozeInterval()) / 60 > 0 ?
                                    Integer.valueOf(new SimpleDateFormat("HH", Locale.ENGLISH).format(time.getTime())) + 1 : Integer.valueOf(new SimpleDateFormat("HH", Locale.ENGLISH).format(time.getTime()));
                            snoozeList.edit().putInt("H" + alarmList.get(currentIndex).getId(), h).apply();
                            snoozeList.edit().putInt("M" + alarmList.get(currentIndex).getId(), m).apply();
                            Log.i("azsxdc", h + ":" + m);
                            if (snoozeList.contains("R" + alarmList.get(currentIndex).getId())) {
                                snoozeList.edit().putInt("R" + alarmList.get(currentIndex).getId(), snoozeList.getInt("R" + alarmList.get(currentIndex).getId(), -1) - 1).apply();
                            } else {
                                snoozeList.edit().putInt("R" + alarmList.get(currentIndex).getId(), sitting.getSnoozeRepeat() - 1).apply();
                            }


                            setAlarmList(getApplication());
                            onDestroy();
                            stopSelf();


                        }
                    });
                    dismess.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("erroooor", alarmList.size() + "");
                            mediaPlayer.reset();

                            if (snoozeList.contains("R" + alarmList.get(currentIndex).getId())) {
                                snoozeList.edit().remove("R" + alarmList.get(currentIndex).getId()).apply();
                                snoozeList.edit().remove("H" + alarmList.get(currentIndex).getId()).apply();
                                snoozeList.edit().remove("M" + alarmList.get(currentIndex).getId()).apply();

                            }


                            alarmList.remove(currentIndex);
                            setAlarmList(getApplication());
                            wm.removeView(unlockView);
                            onDestroy();
                            stopSelf();


                        }
                    });

                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            insertTask task = repository.getTask(alarmList.get(currentIndex).getTaskId());
                            taskTable t = task.getTask();
                            t.setStatus(1);
                            repository.upateTask(t);


                            mediaPlayer.reset();

                            if (snoozeList.contains("R" + alarmList.get(currentIndex).getId())) {
                                snoozeList.edit().remove("R" + alarmList.get(currentIndex).getId()).apply();
                                snoozeList.edit().remove("H" + alarmList.get(currentIndex).getId()).apply();
                                snoozeList.edit().remove("M" + alarmList.get(currentIndex).getId()).apply();

                            }


                            alarmList.remove(currentIndex);
                            setDBalarm(getApplication());
                            setAlarmList(getApplication());
                            wm.removeView(unlockView);
                            onDestroy();
                            stopSelf();

                        }
                    });
                    params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
                    params.x = 0;
                    params.y = 0;
                    params.verticalMargin = 10f;
                    wm.addView(unlockView, params);

                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                }
            } else {
                Toast.makeText(context,"check overlay permission for more flexible experience",Toast.LENGTH_LONG).show();
                Intent ii = new Intent(context, alamLockScreen.class);
                ii.putExtra("ID", currentIndex);
                ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.i("dataBase9", "here");
                context.startActivity(ii);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    public boolean isDeviceLock(Context c) {
        boolean islock = false;
        KeyguardManager k = (KeyguardManager) c.getSystemService(Context.KEYGUARD_SERVICE);
        if (k.inKeyguardRestrictedInputMode()) {
            islock = true;
        } else {
            PowerManager pm = (PowerManager) c.getSystemService(POWER_SERVICE);
            islock = !pm.isInteractive();

        }
        return islock;
        }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i("dataBase9", "here2");

    }
}

