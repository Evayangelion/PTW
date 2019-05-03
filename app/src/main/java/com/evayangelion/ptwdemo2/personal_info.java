package com.evayangelion.ptwdemo2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class personal_info extends AppCompatActivity {

    private String[] perinITEM1 = {"头像", "名字", "账号","二维码"};
    private String[] perinITEM2={"更多"};

    public void setListViewHeightBasedOnChildren(ListView listView)
    {
        //获取listView对应的Adapter
        ListAdapter listAdapter=listView.getAdapter();
        if(listAdapter==null)return;

        int totalHeight=0;
        for(int i=0,len=listAdapter.getCount();i<len;i++)
        {
            // listAdapter.getCount()返回数据项的数目
            View listItem=listAdapter.getView(i,null,listView);
            //计算子项view的宽高
            listItem.measure(0,0);
            //统计所有子项总高度
            totalHeight+=listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params=listView.getLayoutParams();
        params.height=totalHeight+(listView.getDividerHeight()*(listAdapter.getCount()-1));
        //整个listview要显示的高度包括每一个子项高和子项间分割符高
        listView.setLayoutParams(params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_personal_info);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                personal_info.this, android.R.layout.simple_list_item_1, perinITEM1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                personal_info.this, android.R.layout.simple_list_item_1, perinITEM2);

        ListView listView = (ListView) findViewById(R.id.list_personinfo1);
        listView.setAdapter(adapter);
        ListView listView2 = (ListView) findViewById(R.id.list_personinfo2);
        listView2.setAdapter(adapter2);


        //设置listView高度避免每个listview只显示一个Item
        setListViewHeightBasedOnChildren(listView);
        setListViewHeightBasedOnChildren(listView2);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String showText = "点击第" + position + "项，id为" + id;
                Toast.makeText(personal_info.this, showText, Toast.LENGTH_LONG).show();
                //获取每个item的id
                if (id == 0) {
                    Intent intent = new Intent(personal_info.this, personalportrait.class);
                    startActivity(intent);
                } else if (id == 1) {
                    //Intent intent = new Intent(myActivity.this, historyactivity.class);
                    //startActivity(intent);
                } else if (id == 2) {
                    //Intent intent = new Intent(myActivity.this, markActivity.class);
                    //startActivity(intent);
                }
            }
        });
    }
}
