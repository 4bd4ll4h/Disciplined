package com.example.disciplined;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.disciplined.db.db_tables.taskTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.support.v4.content.ContextCompat.getColor;
import static android.support.v4.content.ContextCompat.getDrawable;


public class taskAdapter extends ListAdapter<insertTask, taskAdapter.taskHolder> {

    private static final DiffUtil.ItemCallback<insertTask> diffCallback = new DiffUtil.ItemCallback<insertTask>() {
        @Override
        public boolean areItemsTheSame(insertTask oldItem, insertTask newItem) {
            return oldItem.getTask().getId().equals(newItem.getTask().getId());
        }


        @Override
        public boolean areContentsTheSame(insertTask oldItem, insertTask newItem) {
            return false;

            // due to some bugs we cancel this check but you free to modify
            /*
                    oldItem.getTask().getId().equals(newItem.getTask().getId()) &&
                    oldItem.getTask().getDate().toString().equals(newItem.getTask().getDate().toString()) &&
                    oldItem.getTask().getTitle().equals(newItem.getTask().getTitle()) &&
                    oldItem.getTask().getRepeat().equals(newItem.getTask().getRepeat()) &&
                    oldItem.getTask().getDescription().equals(newItem.getTask().getDescription()) &&
                    oldItem.getTask().getImportance() == newItem.getTask().getImportance() &&
                    oldItem.getTask().getStatus() == newItem.getTask().getStatus() &&
                    oldItem.getAlarms().size() == newItem.getAlarms().size() &&
                    oldItem.getRemainders().size() == newItem.getRemainders().size();
                    */
        }
    };

    private final Context context;
    private final taskAdapterInterface taskFace;
    private final String theDay;
    private insertTask task1;
    private taskHolder taskHolder;
    taskTable task;
    List<insertTask> taskList;

    protected taskAdapter(Context context, taskAdapterInterface taskFace, String theDay) {

        super(diffCallback);
        this.taskFace = taskFace;
        this.context = context;
        this.theDay = theDay;
    }

    @NonNull
    @Override
    public taskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);

        return new taskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final taskHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.setIsRecyclable(false);
        task = getItem(position).getTask();
        task1 = getItem(position);
        taskHolder = holder;
        holder.title.setText(task.getTitle());
        holder.detail.setText(task.getDescription());
        holder.remainderText.setText(String.format("%d%s", getItem(position).getRemainders().size(), context.getString(R.string.Reminders)));
        holder.alarmText.setText(String.format("%d%s", getItem(position).getAlarms().size(), context.getString(R.string.Alarms)));
        holder.repeatText.setText(task.getRepeat());


        if (holder.getCountDownTimer() != null) {
            holder.getCountDownTimer().cancel();
            holder.setCountDownTimer(null);
            holder.time.setText(NewTask.changeTimeFormat(task.getDate().getHours(), task.getDate().getMinutes(), context));
        }


        float sec = SetCounter(getItem(holder.getAdapterPosition()).getTask().getDate().getHours(),
                getItem(holder.getAdapterPosition()).getTask().getDate().getMinutes());
        if (getItem(holder.getAdapterPosition()).getTask().getStatus() != 1 &&
                sec > 0 && holder.getAdapterPosition() == 0 &&
                (new Date().getYear() == new Date(theDay).getYear() &&
                        new Date().getMonth() == new Date(theDay).getMonth() &&
                        new Date().getDate() == new Date(theDay).getDate())) {
            final int[] CH = new int[1];
            final int[] Cm = new int[1];
            final int[] Csecand = new int[1];
            holder.setCountDownTimer(new CountDownTimer((long) (sec * 1000), 900) {
                @Override
                public void onTick(long millisUntilFinished) {
                    millisUntilFinished = millisUntilFinished / 1000;
                    CH[0] = (int) millisUntilFinished / (60 * 60);
                    Cm[0] = (int) (millisUntilFinished % (60 * 60)) / (60);
                    Csecand[0] = (int) (millisUntilFinished % (60 * 60)) - (Cm[0] * 60);
                    holder.time.setText(CH[0] + ":" + Cm[0] + ":" + Csecand[0]);
                }


                @Override
                public void onFinish() {
                    holder.item.setAlpha(0.5f);
                    holder.time.setText(R.string.time_is_up);
                    taskFace.onFinsh();

                }

            });
            holder.getCountDownTimer().start();

        } else {
            Date date1 = new Date();
            Date date2 = new Date(theDay);
            String d1 = "", d2 = "";
            d1 = String.valueOf(date1.getYear());
            d2 = String.valueOf(date2.getYear());
            d1 += String.valueOf(date1.getMonth());
            d2 += String.valueOf(date2.getMonth());
            d1 += String.valueOf(date1.getDate());
            d2 += String.valueOf(date2.getDate());
            if (task.getStatus() != 1 && (Integer.valueOf(d1) > Integer.valueOf(d2) || (d1.equals(d2) && sec < 0))) {
                holder.item.setAlpha(0.5f);
                holder.time.setText(R.string.time_is_up);

            } else {
                if (task.getStatus() == 1) holder.time.setText(R.string.done);
                else
                    holder.time.setText(NewTask.changeTimeFormat(task.getDate().getHours(), task.getDate().getMinutes(), context));
            }
        }


        switch (task.getImportance()) {
            case 8:
            case 9:
            case 10:
                holder.time.setTextColor(getColor(context, R.color.Red));
                holder.importantcIcon.setBackground(getDrawable(context, R.drawable.icon_shap));
                holder.timeIcon.setColorFilter(getColor(context, R.color.Red), PorterDuff.Mode.SRC_IN);
                holder.repeatIcon.setColorFilter(getColor(context, R.color.Red), PorterDuff.Mode.SRC_IN);
                holder.alarmIcon.setColorFilter(getColor(context, R.color.Red), PorterDuff.Mode.SRC_IN);
                holder.notifition.setColorFilter(getColor(context, R.color.Red), PorterDuff.Mode.SRC_IN);

                break;
            case 5:
            case 6:
            case 7:
                holder.time.setTextColor(getColor(context, R.color.redEsay));
                holder.importantcIcon.setBackground(getDrawable(context, R.drawable.icon_shape_red_esy));
                holder.timeIcon.setColorFilter(getColor(context, R.color.redEsay), PorterDuff.Mode.SRC_IN);
                holder.repeatIcon.setColorFilter(getColor(context, R.color.redEsay), PorterDuff.Mode.SRC_IN);
                holder.alarmIcon.setColorFilter(getColor(context, R.color.redEsay), PorterDuff.Mode.SRC_IN);
                holder.notifition.setColorFilter(getColor(context, R.color.redEsay), PorterDuff.Mode.SRC_IN);

                break;
            case 2:
            case 3:
            case 4:
                holder.time.setTextColor(getColor(context, R.color.grn));
                holder.importantcIcon.setBackground(getDrawable(context, R.drawable.ic_shap_esy));
                holder.timeIcon.setColorFilter(getColor(context, R.color.grn), PorterDuff.Mode.SRC_IN);
                holder.repeatIcon.setColorFilter(getColor(context, R.color.grn), PorterDuff.Mode.SRC_IN);
                holder.alarmIcon.setColorFilter(getColor(context, R.color.grn), PorterDuff.Mode.SRC_IN);
                holder.notifition.setColorFilter(getColor(context, R.color.grn), PorterDuff.Mode.SRC_IN);

                break;
            default:
                holder.time.setTextColor(getColor(context, R.color.Gray));
                holder.importantcIcon.setBackground(getDrawable(context, R.drawable.ic_chap_none));
                holder.timeIcon.setColorFilter(getColor(context, R.color.Gray), PorterDuff.Mode.SRC_IN);
                holder.repeatIcon.setColorFilter(getColor(context, R.color.Gray), PorterDuff.Mode.SRC_IN);
                holder.alarmIcon.setColorFilter(getColor(context, R.color.Gray), PorterDuff.Mode.SRC_IN);
                holder.notifition.setColorFilter(getColor(context, R.color.Gray), PorterDuff.Mode.SRC_IN);

                break;


        }
        if (task.getStatus() == 1) {

            holder.doneB.setDrawingCacheBackgroundColor(getColor(context, R.color.colorPrimary));
            holder.time.setTextColor(getColor(context, R.color.colorPrimary));
            holder.timeIcon.setImageResource(R.drawable.done);
            holder.timeIcon.setColorFilter(getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            holder.repeatIcon.setColorFilter(getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            holder.alarmIcon.setColorFilter(getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            holder.notifition.setColorFilter(getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }
        if (getItem(position).getRemainders().size() > 0) {
            holder.notifition.setVisibility(View.VISIBLE);
        }
        if (getItem(position).getAlarms().size() > 0) {
            holder.alarmIcon.setVisibility(View.VISIBLE);
        }
        if (!task.getRepeat().equals(context.getString(R.string.just_once)) &&
                !task.getRepeat().equals("مرة واحدة") && !task.getRepeat().equals("")) {
            holder.repeatIcon.setVisibility(View.VISIBLE);
            holder.doneB.setAlpha(0.5f);
        }



    }






    public class taskHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView time;
        ImageView timeIcon;
        ImageView repeatIcon;
        ImageView alarmIcon;
        ImageView importantcIcon;
        ImageView notifition;
        RelativeLayout item;
        Button removeB;
        Button doneB;
        Button editeB;
        TextView remainderText;
        TextView alarmText;
        TextView repeatText;
        TextView detail;
        CountDownTimer countDownTimer=null;

        public taskHolder(final View view) {
            super(view);
            title = view.findViewById(R.id.Title);
            time = view.findViewById(R.id.Rtimes);
            timeIcon = view.findViewById(R.id.TimeIcon);
            repeatIcon = view.findViewById(R.id.repeat);
            alarmIcon = view.findViewById(R.id.Alert);
            detail = view.findViewById(R.id.NoteDE);
            importantcIcon = view.findViewById(R.id.IMportent);
            notifition = view.findViewById(R.id.noto);
            this.item = view.findViewById(R.id.item);
            this.removeB = view.findViewById(R.id.Remove);
            this.doneB = view.findViewById(R.id.Done);
            this.editeB = view.findViewById(R.id.Edite);
            remainderText = view.findViewById(R.id.RemainderDE);
            alarmText = view.findViewById(R.id.AlarmDE);
            this.repeatText = view.findViewById(R.id.RepeatDE);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskFace.onClik(v, getAdapterPosition());
                }
            });
            editeB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskFace.onClikEdite(getAdapterPosition());

                }
            });
            removeB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle(R.string.Confirm);
                    builder.setMessage(R.string.deletTask);
                    builder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            taskFace.onClikRemaove(getAdapterPosition());

                        }
                    });
                    builder.setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });


            doneB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(taskAdapter.taskHolder.this.doneB.getAlpha()==0.5f)
                        Toast.makeText(context,"Done is unable when Repeating is enable",Toast.LENGTH_LONG).show();
                    else
                    taskFace.onClikDone(getAdapterPosition(), taskAdapter.taskHolder.this);
                }
            });


        }

        public CountDownTimer getCountDownTimer() {
            return countDownTimer;
        }

        public void setCountDownTimer(CountDownTimer countDownTimer) {
            if (this.countDownTimer != null) this.countDownTimer.cancel();
            this.countDownTimer = null;
            this.countDownTimer = countDownTimer;

        }


    }


    public float SetCounter(int Houre, int Muntie) {

        float sec;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH", Locale.ENGLISH);
        Long h = Long.valueOf(simpleDateFormat.format(new Date()));
        simpleDateFormat = new SimpleDateFormat("mm", Locale.ENGLISH);
        Long m = Long.valueOf(simpleDateFormat.format(new Date()));
        simpleDateFormat = new SimpleDateFormat("ss", Locale.ENGLISH);
        float s = Float.parseFloat(simpleDateFormat.format(new Date()));
        h = h * 60;
        Houre = Houre * 60;
        sec = 60 * ((Houre + Muntie) - (h + m + (s / 60)));
        Log.i("asdw", s + " <>" + s / 60);
        Log.i("asdw", String.valueOf(sec));
        return sec;
    }

    public void submitList1(List<insertTask> list) {

        if(list!=null)taskList =new ArrayList<>(list);
        super.submitList(taskList);
        taskAdapter.this.submitList(list);

    }

    interface taskAdapterInterface {
        void onClik(View v, int position);

        void onFinsh();

        void onClikRemaove(int task);

        void onClikDone(int task, taskHolder holder);

        void onClikEdite(int task);
    }

}
