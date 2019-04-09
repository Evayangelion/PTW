package com.evayangelion.ptwdemo2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class setActivity extends AppCompatActivity {

    private String setitem[]={"密码修改","帮助"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        ArrayAdapter<String>adapter=new ArrayAdapter<String>(
                setActivity.this,android.R.layout.simple_list_item_1,setitem);
        ListView listview=(ListView)findViewById(R.id.list_view_set);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String showText = "点击第" + position + "项，id为" + id;
                Toast.makeText(setActivity.this, showText, Toast.LENGTH_LONG).show();
                if(id==0)
                {
                    Intent intent=new Intent(setActivity.this,Change_password_Activity.class);
                    startActivity(intent);
                }
                else if(id==1)
                {
                    Intent intent=new Intent(setActivity.this,helpActivity.class);
                    startActivity(intent);
                }
            }

        });

    }
}
