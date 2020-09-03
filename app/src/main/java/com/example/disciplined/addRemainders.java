package com.example.disciplined;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Date;
import java.util.Objects;

import static com.example.disciplined.Validation.validateDate;


@SuppressLint("ValidFragment")
public class addRemainders extends BottomSheetDialogFragment {

    private BottomSheetBehavior mBehavior;
    NewTaskViewModle viewModle;
    private AppCompatEditText title,details;
    private TextView time;
    private Button save;
    private Context context;

    @SuppressLint("ValidFragment")
    public addRemainders(Context context) {
        this.context=context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        final View view = View.inflate(getContext(), R.layout.activity_add_remainders, null);
        dialog.setContentView(view);
        title=view.findViewById(R.id.reminder_title);
        details=view.findViewById(R.id.reminder_detaile);
        time=view.findViewById(R.id.remainder_time);
        viewModle= ViewModelProviders.of((FragmentActivity) context).get(NewTaskViewModle.class);

        title.setText(String.format("%s%s", getString(R.string.Dontforgetto), viewModle.task.getTitle()));
        details.setText(viewModle.task.getDescription());
        if (viewModle.remainders.getDate()==null){
            if(viewModle.task.getDate()!=null){
                Date date = new Date(viewModle.task.getDate().getTime());
                date.setMinutes(date.getMinutes() - 10);
                viewModle.remainders.setDate(date);}}
        if (viewModle.remainders.getDate()!=null)
            time.setText(NewTask.changeTimeFormat(viewModle.remainders.getDate().getHours(), viewModle.remainders.getDate().getMinutes(), getContext()));


        save=view.findViewById(R.id.save_remainder2);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRemainder();

            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker();
            }
        });
        return dialog;
    }


    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }


    private TimePickerDialog.OnTimeSetListener TPL =
            new TimePickerDialog.OnTimeSetListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    Date date;
                    if (viewModle.remainders.getDate()==null)date=new Date();
                    else date = viewModle.remainders.getDate();
                    date.setHours(hourOfDay);
                    date.setMinutes(minute);
                    viewModle.remainders.setDate(date);
                    time.setText(NewTask.changeTimeFormat(hourOfDay, minute,getContext()));

                }
            };
  public void timePicker(){
      Date date;
      if (viewModle.remainders.getDate()==null){
          date=new Date();
      }else date=viewModle.remainders.getDate();

      new TimePickerDialog(getContext(), R.style.Theme_AppCompat_Yellow, TPL, date.getHours(),
              date.getMinutes(), false).show();
  }
    public void saveRemainder() {
        viewModle.remainders.setTitle(title.getText().toString());
        viewModle.remainders.setDescription(details.getText().toString());
        if (validateDate(viewModle.remainders.getTitle())) {
            if (viewModle.remainders.getDate()!=null&&
                    (viewModle.remainders.getDate().getHours() > 0
                    || viewModle.remainders.getDate().getMinutes() > 0)) {
                viewModle.addreminder(viewModle.remainders);
                viewModle.reNew();
                dismiss();

            } else {
                time.setError(getString(R.string.enteravalidTime));
            }
        } else {
            title.setError(getString(R.string.mustbeatitleintheremainder));
        }

    }

}

