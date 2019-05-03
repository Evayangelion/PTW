package com.evayangelion.ptwdemo2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class historyactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_historyactivity);
    }
}
