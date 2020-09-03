package com.example.disciplined;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.devlomi.record_view.OnBasketAnimationEnd;
import com.devlomi.record_view.OnRecordClickListener;
import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.content.Context.AUDIO_SERVICE;

@SuppressLint("ValidFragment")

public class sound_volume extends BottomSheetDialogFragment {
    private Context context;
    private NewTaskViewModle viewModle;
    private BottomSheetBehavior mBehavior;
    private AppBarLayout app_bar_layout;

    SeekBar volume;
    ImageView soundImage;
    ListView soundsList;
    ArrayAdapter<? extends String> soundAdapter;
    ArrayList<String[]> arrayList = new ArrayList<>();
    ArrayList<String> soundName = new ArrayList<>();
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    private MediaRecorder voiceNote;
    private String outputFile = "";
    private res resume;

    @SuppressLint("ValidFragment")
    public sound_volume(Context context, res resume) {
        this.context = context;
        viewModle = ViewModelProviders.of((FragmentActivity) context).get(NewTaskViewModle.class);
        this.resume = resume;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        final View view = View.inflate(getContext(), R.layout.activity_sound_volume, null);
        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.recordNote);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startRecording();
            }
        });


        volume = view.findViewById(R.id.volume);
        audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        final int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume.setMax(max);
        volume.setProgress(viewModle.getSelectedSitting().getVolume());
        soundImage = view.findViewById(R.id.soundImg);
        soundsList = view.findViewById(R.id.SoundList);
        app_bar_layout = (AppBarLayout) view.findViewById(R.id.app_bar_layout);

        if (volume.getProgress() == 0) {
            soundImage.setImageResource(R.drawable.ic_volume_off);
        } else soundImage.setImageResource(R.drawable.ic_volume_up);

        mediaPlayer = new MediaPlayer();
        setupList();


        soundAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_single_choice, soundName);
        soundsList.setAdapter(soundAdapter);
        soundsList.setItemChecked(getListId(), true);
        soundsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mediaPlayer.reset();
                Log.i("voiceNote", arrayList.get(position)[1]);
                mediaPlayer = MediaPlayer.create(context, Uri.parse(arrayList.get(position)[1]));

                try {
                    mediaPlayer.start();
                } catch (Exception e) {
                    mediaPlayer = MediaPlayer.create(context, Uri.fromFile(new File(arrayList.get(position)[1])));
                    mediaPlayer.start();
                }
                Log.i("checkingSize", arrayList.size() + " --- " + soundsList.getCount());
            }
        });

        volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    soundImage.setImageResource(R.drawable.ic_volume_off);

                } else soundImage.setImageResource(R.drawable.ic_volume_up);
                mediaPlayer.reset();
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                mediaPlayer = MediaPlayer.create(context,
                        Uri.parse(arrayList.get(soundsList.getCheckedItemPosition())[1]));
                Log.i("dataBase9", arrayList.get(soundsList.getCheckedItemPosition())[1]);

                try {
                    mediaPlayer.start();
                } catch (Exception e) {
                    mediaPlayer = MediaPlayer.create(context, Uri.fromFile(new File(arrayList.get(soundsList.getCheckedItemPosition())[1])));
                    mediaPlayer.start();
                    Log.i("checkingSize", arrayList.size() + " --- m" + soundsList.getCheckedItemPosition());
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });


        Log.i("checkingSize", arrayList.size() + " --- " + soundsList.getCount());
        hideView(app_bar_layout);

        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    showView(app_bar_layout, getActionBarSize());
                    Log.i("checkingSize", arrayList.size() + " -d-- " + soundsList.getCheckedItemPosition());
                }
                if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    hideView(app_bar_layout);
                    Log.i("checkingSize", arrayList.size() + " -s-- " + soundsList.getCheckedItemPosition());

                }

                if (BottomSheetBehavior.STATE_HIDDEN == newState) {
                    dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        return dialog;
    }

    private int getListId() {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i)[0].contains(viewModle.getSelectedSitting().getSoundName()))
                return i;
        }

        return 0;
    }

    private void hideView(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = 0;
        view.setLayoutParams(params);
    }

    private void showView(View view, int size) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = size;
        view.setLayoutParams(params);
    }

    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        Log.i("checkingSize", "--onStart");

    }

    private void setupList() {
        soundName = new ArrayList<>();
        File getFiles = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.example.athefourth.sia");
        File[] listF;
        listF = getFiles.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(pathname.getAbsolutePath());
                String isAudio = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO);
                Log.i("voiceNote", isAudio);
                return isAudio != null && isAudio.equals("yes");
            }
        });

        if (listF != null)
            for (File file : listF) {
                String[] ring = new String[2];
                ring[0] = file.getName();
                ring[1] = file.getAbsolutePath();
                soundName.add(ring[0]);
                arrayList.add(ring);

            }
        Log.i("listRecord", soundName.size() + "");
        RingtoneManager ringtoneManager = new RingtoneManager(context);
        ringtoneManager.setType(RingtoneManager.TYPE_ALARM);
        Cursor cursor = ringtoneManager.getCursor();

        while (cursor.moveToNext()) {
            String id = cursor.getString(RingtoneManager.ID_COLUMN_INDEX);
            String uri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX);
            String title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            String[] ring = new String[2];
            ring[0] = title;
            soundName.add(title);
            ring[1] = uri + "/" + id;
            arrayList.add(ring);
        }
    }

    public void startRecording() {
        Log.i("checkingSize", arrayList.size() + " --- " + soundsList.getCheckedItemPosition());
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 44);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v = View.inflate(context, R.layout.note_recorder, null);

        RecordView recordView = v.findViewById(R.id.record_view);
        final RecordButton recordButton = v.findViewById(R.id.record_button);
        recordButton.setRecordView(recordView);
        builder.setView(v);
        recordButton.setOnRecordClickListener(new OnRecordClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "RECORD BUTTON CLICKED", Toast.LENGTH_SHORT).show();
                Log.d("RecordButton", "RECORD BUTTON CLICKED");
            }
        });


        //Cancel Bounds is when the Slide To Cancel text gets before the timer . default is 8
        recordView.setCancelBounds(8);


        recordView.setSmallMicColor(Color.parseColor("#c2185b"));

        //prevent recording under one Second
        recordView.setLessThanSecondAllowed(false);


        recordView.setSlideToCancelText("Slide To Cancel");


        recordView.setCustomSounds(R.raw.record_start, R.raw.record_finished, 0);


        recordView.setOnRecordListener(new OnRecordListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onStart() {
                Log.d("RecordView", "onStart");
                mediaPlayer.reset();
                Toast.makeText(context, "Started", Toast.LENGTH_SHORT).show();
                File fo = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.example.athefourth.sia");
                fo.mkdir();
                outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.example.athefourth.sia/" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".wav";
                voiceNote = new MediaRecorder();
                voiceNote.setAudioSource(MediaRecorder.AudioSource.MIC);
                voiceNote.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                voiceNote.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                voiceNote.setOutputFile(outputFile);
                try {
                    voiceNote.prepare();
                    voiceNote.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onCancel() {
                Toast.makeText(context, "onCancel", Toast.LENGTH_SHORT).show();
                File removeFile = new File(outputFile);
                removeFile.delete();
                voiceNote.reset();
                Log.d("RecordView", "onCancel");

            }

            @Override
            public void onFinish(long recordTime) {

                voiceNote.stop();
                voiceNote.release();
                String time = getHumanTimeText(recordTime);
                Toast.makeText(context, "onFinishRecord - Recorded Time is: " + time, Toast.LENGTH_SHORT).show();
                Log.d("RecordView", "onFinish");

                Log.d("RecordTime", time);
            }

            @Override
            public void onLessThanSecond() {
                voiceNote.reset();
                Toast.makeText(context, "OnLessThanSecond", Toast.LENGTH_SHORT).show();
                Log.d("RecordView", "onLessThanSecond");
            }
        });


        recordView.setOnBasketAnimationEndListener(new OnBasketAnimationEnd() {
            @Override
            public void onAnimationEnd() {
                Log.d("RecordView", "Basket Animation Finished");
            }
        });
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                arrayList = new ArrayList<>();
                setupList();
                soundAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_single_choice, soundName);
                soundsList.setAdapter(soundAdapter);
                soundsList.setItemChecked(getListId(), true);
            }
        });
        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!outputFile.equals("")) {
                    File removeFile = new File(outputFile);
                    removeFile.delete();
                }
            }
        });
        builder.show();


    }

    @SuppressLint("DefaultLocale")
    private String getHumanTimeText(long milliseconds) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.reset();
        Log.i("checkingSize", arrayList.size() + " --- " + soundsList.getCheckedItemPosition());
        viewModle.selectedSitting.setSound(arrayList.get(soundsList.getCheckedItemPosition())[1]);
        viewModle.selectedSitting.setSoundName(arrayList.get(soundsList.getCheckedItemPosition())[0]);
        viewModle.selectedSitting.setVolume(volume.getProgress());
        resume.onResume1();
        dismiss();
    }

    public interface res {
        public void onResume1();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private int getActionBarSize() {
        final TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int size = (int) styledAttributes.getDimension(0, 0);
        return size;
    }
}
