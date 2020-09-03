package com.example.disciplined;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.disciplined.db.db_tables.alarm_table;
import com.example.disciplined.db.db_tables.remainders_table;

public class alarmAdapter extends ListAdapter<alarm_table, alarmAdapter.remainderHolder> {
    Context context;

    public alarmAdapter(Context context) {
        super(diffCallback);
        this.context=context;
    }

    private static DiffUtil.ItemCallback<alarm_table> diffCallback= new DiffUtil.ItemCallback<alarm_table>() {
        @Override
        public boolean areItemsTheSame(alarm_table oldItem, alarm_table newItem) {
            return oldItem.getId()==newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(alarm_table oldItem, alarm_table newItem) {
            return oldItem.getDate().toString().equals(newItem.getDate().toString())
                    &&oldItem.getTitle().equals(newItem.getTitle())
                    &&oldItem.getDescription().equals(newItem.getDescription());
        }
    };

    @NonNull
    @Override
    public remainderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_remainder_alarm,parent,false);
        Log.i("onRes", getItemCount() + "size adapter");

        return new remainderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull remainderHolder holder, int position) {
        alarm_table remainders_table=getItem(position);
        holder.time.setText(NewTask.changeTimeFormat(
                remainders_table.getDate().getHours(),
                remainders_table.getDate().getMinutes(),
                context));
        holder.detale.setText(remainders_table.getDescription());
        holder.title.setText(remainders_table.getTitle());
    }

    class remainderHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView detale,time;
        public remainderHolder(View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.from);
            detale=itemView.findViewById(R.id.message);
            time=itemView.findViewById(R.id.date);
        }
    }
}
