package com.example.disciplined;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.disciplined.db.db_tables.alarmSitting_table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.example.disciplined.Validation.validateDate;

@SuppressLint("ValidFragment")
public class addAlarm extends BottomSheetDialogFragment {

    private BottomSheetBehavior mBehavior;
    NewTaskViewModle viewModle;
    private AppCompatEditText title,details;
    private TextView time,alarmName,alarmNemeEditable,snooze,snoozeEditable,sound,soundEditable;
    private Switch snoozeSwitch;
    private Spinner spinner;
    private Button save;
    private Context context;
    private ArrayAdapter spinnerA;

    @SuppressLint("ValidFragment")
    public addAlarm(Context context) {
        this.context=context;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        final View view = View.inflate(getContext(), R.layout.activity_add_alarm, null);
        dialog.setContentView(view);

        title=view.findViewById(R.id.alarm_title);
        details=view.findViewById(R.id.alarm_details);
        time=view.findViewById(R.id.alarm_time);
        alarmName=view.findViewById(R.id.alarmName1);
        alarmNemeEditable=view.findViewById(R.id.alarmName2);
        snooze=view.findViewById(R.id.snooze1);
        snoozeEditable=view.findViewById(R.id.snooze2);
        snoozeSwitch=view.findViewById(R.id.switch1);
        sound=view.findViewById(R.id.sound1);
        soundEditable=view.findViewById(R.id.sound2);
        spinner=view.findViewById(R.id.spinner);
            save=view.findViewById(R.id.save_alarm);


            //sasinged values
            viewModle = ViewModelProviders.of((FragmentActivity) context).get(NewTaskViewModle.class);

            mBehavior = BottomSheetBehavior.from((View) view.getParent());
            mBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
            List<String> nammm = new ArrayList<String>();
            viewModle.getSitting();
            for (alarmSitting_table sitting_table:viewModle.getSitting()) {
                nammm.add(sitting_table.getAlarmName());
            }
            spinnerA = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, nammm);
            spinnerA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerA);



        title.setText(String.format("%s%s", getString(R.string.Dontforgetto), viewModle.task.getTitle()));
        details.setText(viewModle.task.getDescription());
        if (viewModle.alarm.getDate()==null){
            if(viewModle.task.getDate()!=null){
        Date date = new Date(viewModle.task.getDate().getTime());
        date.setMinutes(date.getMinutes() - 10);
        viewModle.alarm.setDate(date);}}
        if (viewModle.alarm.getDate()!=null)
            time.setText(NewTask.changeTimeFormat(viewModle.alarm.getDate().getHours(), viewModle.alarm.getDate().getMinutes(), getContext()));


        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker();
            }
        });
        setSitting();


        alarmName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAlarmNameDialog();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModle.setSelectedSitting(position);
                setSitting();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        snoozeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewModle.selectedSitting.setSnoozeIson(isChecked?1:0);

            }
        });
        view.findViewById(R.id.goTosnooze).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snooze snoozeDialog = new snooze(context, new snooze.update() {
                    @Override
                    public void update() {
                        onResume();
                    }
                });
                snoozeDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),"snoozeDialog");
                onResume();

            }
        });
        view.findViewById(R.id.goTosound).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound_volume sound_volume=new sound_volume(context, new sound_volume.res() {
                    @Override
                    public void onResume1() {
                        onResume();
                    }
                });
                getChildFragmentManager().beginTransaction().add(sound_volume,"sound").commit();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAlarm();
            }
        });
        return dialog;

    }

    @Override
    public void onResume() {
        Log.i("onDesT","onr1");

        super.onResume();
        setSitting();
        Log.i("onDesT","onr2");


    }

    private void showAlarmNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.Theme_AppCompat_Yellow);
        builder.setTitle(getString(R.string.alarm_name));
        final EditText AlamNameText;
        AlamNameText = new EditText(context);
        AlamNameText.setWidth(10);
        AlamNameText.setText(viewModle.getSelectedSitting().getAlarmName());
        builder.setView(AlamNameText);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (AlamNameText.getText().toString().equals("")) {
                    alarmNemeEditable.setText(getString(R.string.none));
                    viewModle.selectedSitting.setAlarmName("");
                } else {
                    viewModle.selectedSitting.setAlarmName(AlamNameText.getText().toString());
                    alarmNemeEditable.setText(viewModle.getSelectedSitting().getAlarmName());
                    Toast.makeText(context, viewModle.getSelectedSitting().getAlarmName(), Toast.LENGTH_SHORT).show();

                }
            }
        });
        builder.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();

    }


    @Override
    public void onStart() {
        super.onStart();
        setSitting();
        Log.i("onDesT","onStart");
        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
    private TimePickerDialog.OnTimeSetListener TPL =
            new TimePickerDialog.OnTimeSetListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    Date date;
                    if (viewModle.alarm.getDate()==null)date=new Date();
                    else date = viewModle.alarm.getDate();
                    date.setHours(hourOfDay);
                    date.setMinutes(minute);
                    viewModle.alarm.setDate(date);
                    time.setText(NewTask.changeTimeFormat(hourOfDay, minute,getContext()));

                }
            };
    public void timePicker(){
        Date date;
        if (viewModle.alarm.getDate()==null){
            date=new Date();
        }else date=viewModle.alarm.getDate();

        new TimePickerDialog(getContext(), R.style.Theme_AppCompat_Yellow, TPL, date.getHours(),
                date.getMinutes(), false).show();
    }
    private void setSitting() {

        alarmSitting_table names1 =  viewModle.getSelectedSitting();
        if(names1!=null) {
            alarmNemeEditable.setText(names1.getAlarmName());
            if (names1.getSnoozeIson() == 1) {
                snoozeSwitch.setChecked(true);
                if (names1.getSnoozeRepeat() == 100) {
                    snoozeEditable.setText(String.format("%s%s", names1.getSnoozeInterval(), getString(R.string.minuteConti)));
                } else {
                    snoozeEditable.setText(String.format("%s%s%s%s", names1.getSnoozeInterval(), getString(R.string.minute), names1.getSnoozeRepeat(), getString(R.string.time)));

                }
            } else snoozeSwitch.setChecked(false);

            soundEditable.setText(names1.getSoundName());

        }
    }

    public void saveAlarm() {
        viewModle.alarm.setTitle(title.getText().toString());
        viewModle.alarm.setDescription(details.getText().toString());
        if (validateDate(viewModle.alarm.getTitle())) {
            if (viewModle.alarm.getDate()!=null) {
                viewModle.addAlarm(viewModle.alarm);
                dismiss();

            } else {
                time.setError(getString(R.string.enteravalidTime));
            }
        } else {
            title.setError(getString(R.string.mustbeatitleintheremainder));
        }

    }


}
