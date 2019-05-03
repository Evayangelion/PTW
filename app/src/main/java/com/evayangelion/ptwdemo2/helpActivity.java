package com.evayangelion.ptwdemo2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class helpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_help);
        TextView ques1=(TextView)findViewById(R.id.ques1);
        TextView ques2=(TextView)findViewById(R.id.ques2);

        ques1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(helpActivity.this,ques1Activity.class);
                startActivity(intent);
            }
        });

        ques2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(helpActivity.this,ques2Activity.class);
                startActivity(intent);
            }
        });

    }
}
