package com.evayangelion.ptwdemo2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class dailybattleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //隐藏标题栏
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailybattle);
    }
}
