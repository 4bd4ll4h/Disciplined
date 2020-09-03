package com.example.disciplined;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import java.util.Objects;

@SuppressLint("ValidFragment")
public class snooze extends DialogFragment {
    View view;
    Context con;
    Switch aSwitch;
    ListView repeat,intervel;
    LinearLayout relativeLayout;
    boolean isCheck;
    ArrayAdapter intervelAdapter,mRepeatAdapter;
    String []intervels;
    String []repeatSt;
    int intervelInt,repeatInt;
    NewTaskViewModle viewModle;
    update update;


    @SuppressLint("ValidFragment")
    public snooze(Context con ,update update) {
        this.update=update;
        this.con = con;


    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        viewModle= ViewModelProviders.of((FragmentActivity) con).get(NewTaskViewModle.class);

        AlertDialog.Builder snoozeBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()),R.style.Theme_AppCompat_Yellow);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.activity_snooze, null);
        snoozeBuilder.setView(view);
        {


            aSwitch=view.findViewById(R.id.snoozeS);
            repeat=view.findViewById(R.id.repeatList11);
            intervel=view.findViewById(R.id.intervelList);
            relativeLayout=view.findViewById(R.id.IandR);


            isCheck= viewModle.selectedSitting.getSnoozeIson() == 1;
            Log.i("isCheck", String.valueOf(isCheck));
            aSwitch.setChecked(isCheck);
            if(aSwitch.isChecked()) {
                relativeLayout.setAlpha(1f);
                aSwitch.setText(R.string.on);
            }else {relativeLayout.setAlpha(.5f);
                aSwitch.setText(R.string.off);}

            repeatSt=new String[3];
            repeatSt[0]="3 times";
            repeatSt[1]="5 times";
            repeatSt[2]="Continuously";


            mRepeatAdapter=new ArrayAdapter<>(con,android.R.layout.simple_list_item_single_choice,repeatSt);
            repeat.setAdapter(mRepeatAdapter);
            repeat.setItemChecked(0,true);
            repeat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(position==2){
                        repeatInt=100;
                    }
                    if(position==1){
                        repeatInt=5;
                    }
                    if(position==0){
                        repeatInt=3;
                    }
                }
            });


            intervels=new String[4];
            intervels[0]="5 minutes";
            intervels[1]="10 minutes";
            intervels[2]="15 minutes";
            intervels[3]="30 minutes";

            intervelAdapter=new ArrayAdapter<>(con,android.R.layout.simple_list_item_single_choice,intervels);
            intervel.setAdapter(intervelAdapter);
            intervel.setItemChecked(0,true);
            intervel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(position==0){
                        intervelInt=5;
                    }
                    if(position==1){
                        intervelInt=10;
                    }
                    if(position==2){
                        intervelInt=15;
                    }
                    if(position==3){
                        intervelInt=30;
                    }
                }
            });

            aSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                     clickSnooze();

                }
            });

        }

        snoozeBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModle.selectedSitting.setSnoozeIson(aSwitch.isChecked()?1:0);
                viewModle.selectedSitting.setSnoozeRepeat(intervelInt);
                viewModle.selectedSitting.setSnoozeRepeat(repeatInt);
                update.update();


            }
        });
        snoozeBuilder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        return snoozeBuilder.show();

    }



    public void clickSnooze(){
        if(aSwitch.isChecked()) {
            relativeLayout.setAlpha(1f);
            aSwitch.setText(R.string.on);
        }else {relativeLayout.setAlpha(.5f);
            aSwitch.setText(R.string.off);}
    }
interface update{
       void update();
}
}
