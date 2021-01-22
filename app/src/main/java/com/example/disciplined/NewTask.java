package com.example.disciplined;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import com.example.disciplined.db.db_tables.alarm_table;
import com.example.disciplined.db.db_tables.remainders_table;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.Chip;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.example.disciplined.Validation.validateDate;
import static com.example.disciplined.Validation.validateTask;
import static com.example.disciplined.Validation.validateTime;


public class NewTask extends AppCompatActivity {

    private AppCompatEditText mTitle;
    private AppCompatEditText mDetail;
    private Button mDate;
    private Button mTime;
    private TextView mImportnaceText;
    private SeekBar mImportnace;
    private Button mRepeat;
    private NachoTextView mRepeatText;
    private Switch mRemainderSwitch;
    private Switch mAllarmSwitch;
    private RecyclerView remainderList, alarm_list;
    public NewTaskViewModle viewModle;
    remainderAdapter remainderAdapter;
    alarmAdapter alramAdapter;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.title_activity_new_task));

        mTitle = findViewById(R.id.titleTask);
        mDetail = findViewById(R.id.detaileTask);
        mTime = findViewById(R.id.taskTiem_pick);
        mDate = findViewById(R.id.taskdate_pick);
        mImportnaceText = findViewById(R.id.importanceText);
        mImportnace = findViewById(R.id.importance);
        mRepeat = findViewById(R.id.repeat_pick);
        mRepeatText = findViewById(R.id.et_tag);
        mRemainderSwitch = findViewById(R.id.newRemaider);
        mAllarmSwitch = findViewById(R.id.newAlarm);


        remainderList = findViewById(R.id.remaider_list);
        remainderAdapter = new remainderAdapter(this);
        remainderList.setLayoutManager(new LinearLayoutManager(this));
        remainderList.setHasFixedSize(false);
        remainderList.setAdapter(remainderAdapter);

        alarm_list = findViewById(R.id.alarm_list);
        alramAdapter = new alarmAdapter(this);
        alarm_list.setLayoutManager(new LinearLayoutManager(this));
        alarm_list.setHasFixedSize(false);
        alarm_list.setAdapter(alramAdapter);
        context = this;


        if (getIntent().hasExtra("id")) {
            viewModle = ViewModelProviders.of(this,new factory(getApplication(), getIntent().getLongExtra("id", -1))).get(NewTaskViewModle.class);
            viewModle.id=getIntent().getLongExtra("id",-1);

        } else {
            viewModle = ViewModelProviders.of(this).get(NewTaskViewModle.class);
        }

        viewModle.getRmainder().observe(this, new Observer<List<remainders_table>>() {
            @Override
            public void onChanged(@Nullable List<remainders_table> remainders_tables) {
                ArrayList<remainders_table> clone = new ArrayList<>();
                for (remainders_table r : remainders_tables) {
                    clone.add(r);
                }
                remainderAdapter.submitList(clone);

            }
        });
        viewModle.getAlarm().observe(this, new Observer<List<alarm_table>>() {
            @Override
            public void onChanged(@Nullable List<alarm_table> alarm_tables) {
                ArrayList<alarm_table> clone = new ArrayList<>();
                for (alarm_table r : alarm_tables) {
                    clone.add(r);
                }
                alramAdapter.submitList(clone);
            }
        });

        mTitle.setText(viewModle.task.getTitle());
        mDetail.setText(viewModle.task.getDescription());
        mImportnace.setProgress(viewModle.task.getImportance());
        mImportnaceText.setText(viewModle.importance);
        setSeek(viewModle.task.getImportance());
        Date date = viewModle.task.getDate();
        if (viewModle.task.getDate() != null) {
            mTime.setText(changeTimeFormat(date.getHours(), date.getMinutes(), getApplicationContext()));
            mDate.setText(MessageFormat.format("{0}/{1}/{2}", date.getDate(), date.getMonth() + 1, date.getYear() + 1900));
        }
        setUpComponent();

    }


    private void setUpComponent() {
        mImportnace.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                viewModle.task.setImportance(progress);
                setSeek(progress);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date;
                if (viewModle.task.getDate() == null) {
                    date = new Date();
                } else date = viewModle.task.getDate();

                new DatePickerDialog(NewTask.this, R.style.Theme_AppCompat_Yellow, DPL, date.getYear() + 1900
                        , date.getMonth()
                        , date.getDate()).show();
            }
        });

        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date;
                if (viewModle.task.getDate() == null) {
                    date = new Date();
                } else date = viewModle.task.getDate();

                new TimePickerDialog(NewTask.this, R.style.Theme_AppCompat_Yellow, TPL, date.getHours(),
                        date.getMinutes(), false).show();
            }
        });

        mRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                com.example.disciplined.repeatDialog.postive postive = new repeatDialog.postive() {
                    @Override
                    public void click() {
                        viewModle.task.setRepeat(repeatAdpter.work + repeatAdpter.weekend);
                        if (viewModle.task.getRepeat().equals("")) {
                            mRepeatText.setText(getResources().getString(R.string.just_once));
                        } else {
                            mRepeatText.setText(Arrays.asList(viewModle.task.getRepeat().split(",")));
                        }
                    }
                };
                repeatDialog repeatDialog = new repeatDialog(context, postive);
                repeatDialog.show(getSupportFragmentManager(), "repeatDialog");

            }
        });

        mRepeatText.setOnChipRemoveListener(new NachoTextView.OnChipRemoveListener() {
            @Override
            public void onChipRemove(Chip chip) {
                viewModle.task.setRepeat(mRepeatText.getChipAndTokenValues().toString());
            }
        });


        mRemainderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    viewModle.task.setTitle(mTitle.getText().toString());
                    viewModle.task.setDescription(mDetail.getText().toString());
                    addRemainders remainders = new addRemainders(context);
                    remainders.show(getSupportFragmentManager(), "add Remainder");
                    mRemainderSwitch.setChecked(false);


                } else {

                }
            }
        });

        mAllarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    viewModle.task.setTitle(mTitle.getText().toString());
                    viewModle.task.setDescription(mDetail.getText().toString());
                    addAlarm alarm = new addAlarm(context);
                    alarm.show(getSupportFragmentManager(), "add Alarm");
                    mAllarmSwitch.setChecked(false);

                } else {

                }
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {


            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                viewModle.deleteRemainder(viewHolder.getAdapterPosition());

            }
        }).attachToRecyclerView(remainderList);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {


            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                viewModle.deleteAlarm(viewHolder.getAdapterPosition());

            }
        }).attachToRecyclerView(alarm_list);
    }

    // note that mImportnaceText is working but after second thoughts I decided that it's not necessary
    // you can enable it by changing the visibility attribute from GONE to VISIBLE in the xml
    private void setSeek(int progress) {
        if (progress < 2) {
            mImportnaceText.setText(getString(R.string.notImportent));
            mImportnaceText.setTextColor(getResources().getColor(R.color.Gray));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mImportnace.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.Gray)));
                mImportnace.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.Gray)));
            }
        }
        if (progress < 5 && progress >= 2) {
            mImportnaceText.setText(getString(R.string.easy));
            mImportnaceText.setTextColor(getResources().getColor(R.color.grn));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mImportnace.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.grn)));
                mImportnace.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.grn)));
            }

        }
        if (progress < 8 && progress >= 5) {
            mImportnaceText.setText(getString(R.string.notEseay));
            mImportnaceText.setTextColor(getResources().getColor(R.color.redEsay));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mImportnace.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.redEsay)));
                mImportnace.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.redEsay)));
            }

        }
        if (progress < 11 && progress >= 8) {
            mImportnaceText.setText(getString(R.string.hard));
            mImportnaceText.setTextColor(getResources().getColor(R.color.Red));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mImportnace.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.Red)));
                mImportnace.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.Red)));
            }
        }
    }

    private DatePickerDialog.OnDateSetListener DPL =
            new DatePickerDialog.OnDateSetListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Date date;
                    if (viewModle.task.getDate() == null) date = new Date();
                    else date = viewModle.task.getDate();
                    date.setYear(year-1900);
                    date.setMonth(month);
                    date.setDate(dayOfMonth);
                    viewModle.task.setDate(date);
                    mDate.setText(dayOfMonth + "/" + month + "/" + year);
                }
            };

    private TimePickerDialog.OnTimeSetListener TPL =
            new TimePickerDialog.OnTimeSetListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    Date date;
                    if (viewModle.task.getDate() == null) date = new Date();
                    else date = viewModle.task.getDate();
                    date.setHours(hourOfDay);
                    date.setMinutes(minute);
                    viewModle.task.setDate(date);
                    mTime.setText(changeTimeFormat(hourOfDay, minute, getApplicationContext()));

                }
            };

    public static String changeTimeFormat(int Hour, int minute, Context context) {
        String TimeFormatted = "";

        if (Hour < 12 && 10 > minute) {
            if (Hour == 0) Hour = Hour + 12;
            TimeFormatted = Hour + ":0" + minute + context.getString(R.string.Am);
            return TimeFormatted;
        }
        if ((Hour > 12 || Hour == 12)) {
            if (minute < 10) {
                if (Hour != 12) {
                    Hour = Hour - 12;
                }
                TimeFormatted = Hour + ":0" + minute + context.getString(R.string.pm);
                return TimeFormatted;
            }

            if (minute > 10 || minute == 10) {
                if (Hour != 12) {
                    Hour = Hour - 12;
                }
                TimeFormatted = Hour + ":" + minute + context.getString(R.string.pm);
                return TimeFormatted;
            }
        }
        if ((Hour < 12 && (minute > 10 || minute == 10))) {
            if (Hour == 0) Hour = Hour + 12;
            TimeFormatted = Hour + ":" + minute + context.getString(R.string.Am);
            return TimeFormatted;
        }
        return TimeFormatted;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void saveTask() {
        String Vtask = mTitle.getText().toString();
        String Vdate = mDate.getText().toString();
        String Vtime = mTime.getText().toString();
        int err = 0;
        mTitle.setError(null);
        mDetail.setError(null);
        mTime.setError(null);
        if (!validateTask(Vtask)) {
            err++;
            mTitle.setError(getString(R.string.tooLongtaskorEmpty));
            showSnackBarMessage(getString(R.string.interavalidinfo));

        }

        if (!validateDate(Vdate)) {
            err++;
            mDate.setError(getString(R.string.invalidDate));
            showSnackBarMessage(getString(R.string.interavalidinfo));

        }

        if (!validateTime(Vtime)) {
            err++;
            mTime.setError(getString(R.string.invalidTime));
            showSnackBarMessage(getString(R.string.interavalidinfo));
        }

        if (err == 0) {
            viewModle.task.setTitle(mTitle.getText().toString());
            viewModle.task.setDescription(mDetail.getText().toString());
            viewModle.task.setImportance(mImportnace.getProgress());
            if(viewModle.id==null||viewModle.id==-1)
            viewModle.insertTask();
            else viewModle.updateTask();
            finish();

        }
        //finishActivity.finish();
        // startActivity(new Intent(this, Task_of_the_day.class));


    }


    private void showSnackBarMessage(String message) {


        Snackbar.make(findViewById(R.id.torr), message, Snackbar.LENGTH_SHORT).show();

    }


}
