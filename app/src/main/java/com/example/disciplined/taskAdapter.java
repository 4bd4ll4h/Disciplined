package com.example.disciplined;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.CountDownTimer;
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

import com.example.disciplined.db.db_tables.taskTable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.support.v4.content.ContextCompat.getColor;
import static android.support.v4.content.ContextCompat.getDrawable;


public class taskAdapter extends ListAdapter<insertTask, taskAdapter.taskHolder> {
    private static DiffUtil.ItemCallback<insertTask> diffCallback = new DiffUtil.ItemCallback<insertTask>() {
        @Override
        public boolean areItemsTheSame(insertTask oldItem, insertTask newItem) {
            return oldItem.getTask().getId().equals(newItem.getTask().getId());
        }

        @Override
        public boolean areContentsTheSame(insertTask oldItem, insertTask newItem) {
            return oldItem.getTask().getId().equals(newItem.getTask().getId()) &&
                    oldItem.getTask().getDate().toString().equals(newItem.getTask().getDate().toString()) &&
                    oldItem.getTask().getTitle().equals(newItem.getTask().getTitle()) &&
                    oldItem.getTask().getRepeat().equals(newItem.getTask().getRepeat()) &&
                    oldItem.getTask().getDescription().equals(newItem.getTask().getDescription()) &&
                    oldItem.getTask().getImportance() == newItem.getTask().getImportance() &&
                    oldItem.getTask().getStatus() == newItem.getTask().getStatus() &&
                    oldItem.getAlarms().size() == newItem.getAlarms().size() &&
                    oldItem.getRemainders().size() == newItem.getRemainders().size();
        }
    };
    private Context context;
    private taskAdapterInterface taskFace;
    private String theDay;
    private insertTask task1;
    private taskHolder taskHolder;

    protected taskAdapter(Context context, taskAdapterInterface taskFace, String theDay) {

        super(diffCallback);
        this.taskFace = taskFace;
        this.context = context;
        this.theDay = theDay;
        Log.i("safsda2",getItemCount()+"i");
    }

    @NonNull
    @Override
    public taskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);

        return new taskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  taskHolder holder, int position) {
        Log.i("safsdaf",getItemCount()+"i");

        taskTable task = getItem(position).getTask();
         task1=getItem(position);
         taskHolder=holder;
        holder.title.setText(task.getTitle());
        holder.detail.setText(task.getDescription());
        holder.remainderText.setText(String.format("%d%s", getItem(position).getRemainders().size(), context.getString(R.string.Reminders)));
        holder.alarmText.setText(String.format("%d%s", getItem(position).getAlarms().size(), context.getString(R.string.Alarms)));
        holder.repeatText.setText(task.getRepeat());

        float sec = SetCounter(task.getDate().getHours(), task.getDate().getMinutes());
        if (task.getStatus()!=1&&sec > 0 && position == 0 && (new Date().getYear()==new Date(theDay).getYear()&&new Date().getMonth()==new Date(theDay).getMonth()&&
                new Date().getDate()==new Date(theDay).getDate())) {
            final int[] CH = new int[1];
            final int[] Cm = new int[1];
            final int[] Csecand = new int[1];
            final taskTable finalMtask = task;
            CountDownTimer countDownTimer = new CountDownTimer((long) (sec * 1000), 900) {
                @Override
                public void onTick(long millisUntilFinished) {
                    millisUntilFinished = millisUntilFinished / 1000;
                    CH[0] = (int) millisUntilFinished / (60 * 60);
                    Cm[0] = (int) (millisUntilFinished % (60 * 60)) / (60);
                    Csecand[0] = (int) (millisUntilFinished % (60 * 60)) - (Cm[0] * 60);
                    getHolder().time.setText(CH[0] + ":" + Cm[0] + ":" + Csecand[0]);
                }

                @Override
                public void onFinish() {
                    getHolder().item.setAlpha(0.5f);
                    getHolder().time.setText(R.string.time_is_up);
                    taskFace.onFinsh();
                }
            };
            countDownTimer.start();
        } else {
            Date date1=new Date();
            Date date2=new Date(theDay);
            String d1="",d2="";
            d1= String.valueOf(date1.getYear());
            d2= String.valueOf(date2.getYear());
            d1+= String.valueOf(date1.getMonth());
            d2+= String.valueOf(date2.getMonth());
            d1+= String.valueOf(date1.getDate());
            d2+= String.valueOf(date2.getDate());
            Log.i("theDay2",d1+" : "+d2);
            if (task.getStatus()!=1&& (Integer.valueOf(d1)>Integer.valueOf(d2)||(d1.equals(d2) &&sec<0)))
            {holder.item.setAlpha(0.5f);
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
                holder.importantcIcon.setBackground(getDrawable(context,R.drawable.icon_shap));
                holder.timeIcon.setColorFilter(getColor(context, R.color.Red), PorterDuff.Mode.SRC_IN);
                holder.repeatIcon.setColorFilter(getColor(context, R.color.Red), PorterDuff.Mode.SRC_IN);
                holder.alarmIcon.setColorFilter(getColor(context, R.color.Red), PorterDuff.Mode.SRC_IN);
                holder.notifition.setColorFilter(getColor(context, R.color.Red), PorterDuff.Mode.SRC_IN);

                break;
            case 5:
            case 6:
            case 7:
                holder.time.setTextColor(getColor(context, R.color.redEsay));
                holder.importantcIcon.setBackground(getDrawable(context,R.drawable.icon_shape_red_esy));
                holder.timeIcon.setColorFilter(getColor(context, R.color.redEsay), PorterDuff.Mode.SRC_IN);
                holder.repeatIcon.setColorFilter(getColor(context, R.color.redEsay), PorterDuff.Mode.SRC_IN);
                holder.alarmIcon.setColorFilter(getColor(context, R.color.redEsay), PorterDuff.Mode.SRC_IN);
                holder.notifition.setColorFilter(getColor(context, R.color.redEsay), PorterDuff.Mode.SRC_IN);

                break;
            case 2:
            case 3:
            case 4:
                holder.time.setTextColor(getColor(context, R.color.grn));
                holder.importantcIcon.setBackground(getDrawable(context,R.drawable.ic_shap_esy));
                holder.timeIcon.setColorFilter(getColor(context, R.color.grn), PorterDuff.Mode.SRC_IN);
                holder.repeatIcon.setColorFilter(getColor(context, R.color.grn), PorterDuff.Mode.SRC_IN);
                holder.alarmIcon.setColorFilter(getColor(context, R.color.grn), PorterDuff.Mode.SRC_IN);
                holder.notifition.setColorFilter(getColor(context, R.color.grn), PorterDuff.Mode.SRC_IN);

                break;
            default:
                holder.time.setTextColor(getColor(context, R.color.Gray));
                holder.importantcIcon.setBackground(getDrawable(context,R.drawable.ic_chap_none));
                holder.timeIcon.setColorFilter(getColor(context, R.color.Gray), PorterDuff.Mode.SRC_IN);
                holder.repeatIcon.setColorFilter(getColor(context, R.color.Gray), PorterDuff.Mode.SRC_IN);
                holder.alarmIcon.setColorFilter(getColor(context, R.color.Gray), PorterDuff.Mode.SRC_IN);
                holder.notifition.setColorFilter(getColor(context, R.color.Gray), PorterDuff.Mode.SRC_IN);

                break;


        }
        if(task.getStatus()==1){

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
        }


        holder.removeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);

                builder.setTitle(R.string.Confirm);
                builder.setMessage(R.string.deletTask);
                builder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     taskFace.onClikRemaove(getItem());

                    }
                });
                builder.setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }
        });



        holder.doneB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               taskFace.onClikDone(getItem(),getHolder());
            }
        });
        holder.editeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               taskFace.onClikEdite(getItem());

            }
        });



    }

    private taskHolder getHolder() {
        return taskHolder;
    }

    private insertTask getItem() {
        return task1;
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
                    taskFace.onClik(v,getAdapterPosition());
                }
            });
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

    interface taskAdapterInterface {
        public void onClik(View v,int position);

        public void onFinsh();

        void onClikRemaove(insertTask task);

        void onClikDone(insertTask task, taskHolder holder);

        void onClikEdite(insertTask task);
    }
}
