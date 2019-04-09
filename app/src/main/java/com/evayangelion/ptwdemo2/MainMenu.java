package com.evayangelion.ptwdemo2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainMenu extends AppCompatActivity {

    //private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText("home");
                    Intent intent_home=new Intent(MainMenu.this,MapActivity.class);
                    startActivity(intent_home);
                    return true;
                case R.id.navigation_dashboard:
                    //mTextMessage.setText("dashbord");
                    return true;
                case R.id.navigation_notifications:
                    Intent intent_my=new Intent(MainMenu.this,myActivity.class);
                    startActivity(intent_my);

                     //mTextMessage.setText("个人中心");
                    return true;
            }
            return false;
        }
        };
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);



        }

    }

