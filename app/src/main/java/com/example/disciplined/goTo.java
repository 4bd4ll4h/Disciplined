package com.example.disciplined;

import android.app.Application;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class goTo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_to);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DayFragment dayFragment=new DayFragment();
        Bundle bundle=new Bundle();
        bundle.putString("theDay",getIntent().getStringExtra("theDay"));
        dayFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.theDayone,dayFragment).commit();
    }
}
