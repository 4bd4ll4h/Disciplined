package com.example.disciplined;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import java.util.Objects;


@SuppressLint("ValidFragment")
public class repeatDialog extends DialogFragment {
    ViewPager viewPager;
    TabLayout tabLayout;
    View view;
    Context con;
    postive postive;

    public repeatDialog(Context c,postive postive) {
        this.postive=postive;
        this.con = c;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder repeatBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.repeatdialo, null);
        repeatBuilder.setView(view);

        viewPager = view.findViewById(R.id.pagerRepeat);
        repeatAdpter repeatAdpter = new repeatAdpter(con);
        viewPager.setAdapter(repeatAdpter);
        tabLayout = view.findViewById(R.id.dayBar);
        tabLayout.setupWithViewPager(viewPager);
        repeatBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                postive.click();

            }
        });
        repeatBuilder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return repeatBuilder.create();
    }


    interface postive{
         void click();

    }
}
