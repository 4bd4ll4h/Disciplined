package com.example.disciplined;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.support.v4.content.ContextCompat.getColor;
import static com.example.disciplined.MainActivity.SORT;
import static com.example.disciplined.MainActivity.setAlarmList;
import static com.example.disciplined.MainActivity.setDBalarm;
import static com.example.disciplined.MainActivity.setDbReminder;
import static com.example.disciplined.MainActivity.setRemainderOfDay;
import static com.example.disciplined.MainActivity.sorting;
import static com.example.disciplined.MainActivity.wayOfSorting;
import static com.example.disciplined.method.SetCounter;


public class DayFragment extends android.support.v4.app.Fragment {

    private View view;
    private String theDay;
    private viewModle viewModle;
    private RecyclerView taskList;
    private taskAdapter taskAdapter;
    private int lastPosition = -1;
    private View lastVeiw = null;

    private boolean isOpen = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_day, container, false);
        theDay = getArguments().getString("theDay");
        viewModle = ViewModelProviders.of(this).get(com.example.disciplined.viewModle.class);
        viewModle.setAllTask(new Date(theDay),
                new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date(theDay)), SORT);
        taskList = view.findViewById(R.id.Tasks);
        taskList.setLayoutManager(new LinearLayoutManager(getContext()));
        taskList.setHasFixedSize(false);
        taskAdapter = new taskAdapter(getContext(), new taskAdapter.taskAdapterInterface() {
            @Override
            public void onClik(View v, int position) {
                clicked(v, new Integer(position));
            }

            @Override
            public void onFinsh() {
                viewModle.setAllTask(new Date(theDay),
                        new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date(theDay)), SORT);
                taskAdapter.submitList1(viewModle.getAllTask());
                taskAdapter.notifyDataSetChanged();

            }

            @Override
            public void onClikRemaove(int task) {

                taskAdapter.notifyDataSetChanged();
                viewModle.deleteTask(viewModle.getAllTask().get(task).getTask().getId());
                viewModle.setAllTask(new Date(theDay),
                        new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date(theDay)), SORT);
                taskAdapter.submitList1(viewModle.getAllTask());
                taskAdapter.notifyDataSetChanged();
                lastPosition--;

            }

            @Override
            public void onClikDone(int task, com.example.disciplined.taskAdapter.taskHolder holder) {
                if (viewModle.getAllTask().get(task).getTask().getStatus() != 1) {
                    viewModle.getAllTask().get(task).getTask().setStatus(1);
                    holder.doneB.setDrawingCacheBackgroundColor(getColor(getContext(), R.color.colorPrimary));
                    holder.time.setTextColor(getColor(getContext(), R.color.colorPrimary));
                    holder.timeIcon.setImageResource(R.drawable.done);
                    holder.timeIcon.setColorFilter(getColor(getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                    holder.repeatIcon.setColorFilter(getColor(getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                    holder.alarmIcon.setColorFilter(getColor(getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                    holder.notifition.setColorFilter(getColor(getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                    viewModle.updateTask(viewModle.getAllTask().get(task).getTask());
                    viewModle.setAllTask(new Date(theDay),
                            new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date(theDay)), SORT);
                    taskAdapter.submitList1(viewModle.getAllTask());
                    taskAdapter.notifyDataSetChanged();
                }
                setDbReminder(getActivity().getApplication());
                setRemainderOfDay(getActivity().getApplication());
                setDBalarm(getActivity().getApplication());
                setAlarmList(getActivity().getApplication());
            }

            @Override
            public void onClikEdite(int task) {

                Intent i = new Intent(getContext(), NewTask.class);
                i.putExtra("id", viewModle.allTask.get(task).getTask().getId());

                startActivity(i);

            }
        }, theDay);
        taskList.setAdapter(taskAdapter);

        taskAdapter.submitList1(viewModle.getAllTask());
        taskAdapter.notifyDataSetChanged();


        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ByDate:
                SORT = "Date ASC";
                viewModle.setAllTask(new Date(theDay),
                        new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date(theDay)), SORT);
                taskAdapter.submitList1(viewModle.getAllTask());
                taskAdapter.notifyDataSetChanged();
                sorting = Objects.requireNonNull(getActivity()).getSharedPreferences("package com.example.disciplined", Context.MODE_PRIVATE);
                sorting.edit().putString(wayOfSorting, SORT).apply();

                return true;
            case R.id.ByImportance:
                SORT = "importance DESC";
                viewModle.setAllTask(new Date(theDay),
                        new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date(theDay)), SORT);

                taskAdapter.submitList1(viewModle.getAllTask());
                taskAdapter.notifyDataSetChanged();
                sorting = Objects.requireNonNull(getActivity()).getSharedPreferences("package com.example.disciplined", Context.MODE_PRIVATE);
                sorting.edit().putString(wayOfSorting, SORT).apply();
                return true;
            case R.id.ByTitle:
                SORT = "title ASC";
                viewModle.setAllTask(new Date(theDay),
                        new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date(theDay)), SORT);
                taskAdapter.submitList1(viewModle.getAllTask());
                taskAdapter.notifyDataSetChanged();
                sorting = Objects.requireNonNull(getActivity()).getSharedPreferences("package com.example.disciplined", Context.MODE_PRIVATE);
                sorting.edit().putString(wayOfSorting, SORT).apply();

                return true;

            default:
                break;
        }

        return false;
    }

    private void clicked(View view, int position) {
        if (lastPosition != -1)
            updateArray(lastPosition, lastVeiw);
        if ((lastPosition != position) || (!isOpen)) {
            view.findViewById(R.id.divider2).setVisibility(View.VISIBLE);
            view.findViewById(R.id.item).setAlpha(1f);
            TextView note = view.findViewById(R.id.NoteDE);
            note.setText(viewModle.allTask.get(position).getTask().getDescription());
            note.setVisibility(View.VISIBLE);
            if (view.findViewById(R.id.repeat).getVisibility() == View.VISIBLE) {
                TextView RepeatDE = view.findViewById(R.id.RepeatDE);
                RepeatDE.setVisibility(View.VISIBLE);
            }
            if (view.findViewById(R.id.Alert).getVisibility() == View.VISIBLE) {
                TextView AlarmDe = view.findViewById(R.id.AlarmDE);
                AlarmDe.setVisibility(View.VISIBLE);
            }
            TextView RemaiderDE = view.findViewById(R.id.RemainderDE);
            String reminderNum = (String) RemaiderDE.getText().subSequence(0, 1);
            int remdNum = Integer.valueOf(reminderNum);
            if (remdNum > 0) {
                RemaiderDE.setVisibility(View.VISIBLE);
            }


            view.findViewById(R.id.detaile).setVisibility(View.VISIBLE);
            TextView Atime = view.findViewById(R.id.Rtimes);
            if (Atime.getText().toString().equals(getString(R.string.time_is_up)))
                Atime.setText(NewTask.changeTimeFormat(viewModle.allTask.get(position).getTask().getDate().getHours(),
                        viewModle.allTask.get(position).getTask().getDate().getMinutes(), getContext()));
            lastPosition = new Integer(position);
            lastVeiw = view;
            isOpen = true;
        } else {
            isOpen = false;
        }
    }

    public void updateArray(int index, View lastVeiw) {
        View vie = null;
        if (index != -1) {
            vie = lastVeiw;
            if (vie == null) {
                return;
            } else {
                if (vie.findViewById(R.id.detaile).getVisibility() == View.VISIBLE) {

                    vie.findViewById(R.id.divider2).setVisibility(View.GONE);
                    TextView note = vie.findViewById(R.id.NoteDE);
                    note.setVisibility(View.GONE);
                    TextView RepeatDE = vie.findViewById(R.id.RepeatDE);
                    RepeatDE.setVisibility(View.GONE);
                    TextView AlarmDe = vie.findViewById(R.id.AlarmDE);
                    AlarmDe.setVisibility(View.GONE);
                    TextView RemaiderDE = vie.findViewById(R.id.RemainderDE);
                    RemaiderDE.setVisibility(View.GONE);
                    vie.findViewById(R.id.detaile).setVisibility(View.GONE);
                    TextView Atime = vie.findViewById(R.id.Rtimes);
                    Date date1 = new Date();
                    Date date2 = new Date(theDay);
                    String d1 = "", d2 = "";
                    d1 = String.valueOf(date1.getYear());
                    d2 = String.valueOf(date2.getYear());
                    d1 += String.valueOf(date1.getMonth());
                    d2 += String.valueOf(date2.getMonth());
                    d1 += String.valueOf(date1.getDate());
                    d2 += String.valueOf(date2.getDate());
                    if (viewModle.allTask.get(index).getTask().getStatus() != 1 &&
                            (Integer.valueOf(d1) > Integer.valueOf(d2) || (d1.equals(d2) &&
                                    SetCounter(viewModle.allTask.get(index).getTask().getDate().getHours(),
                                            viewModle.allTask.get(index).getTask().getDate().getMinutes()) < 0))) {
                        Atime.setText(getString(R.string.time_is_up));
                        vie.findViewById(R.id.item).setAlpha(0.5f);
                    }
                }

            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModle.setAllTask(new Date(theDay),
                new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date(theDay)), SORT);
        taskAdapter.submitList1(viewModle.getAllTask());
        taskAdapter.notifyDataSetChanged();

    }


}
