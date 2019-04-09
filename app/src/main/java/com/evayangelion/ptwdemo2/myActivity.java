package com.evayangelion.ptwdemo2;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
//import static com.baidu.location.g.j.R;

public class myActivity extends AppCompatActivity {


    //个人中心主要内容
    private String[] ITEMM1 = {"今日战况", "历史纪录", "标记"};
    private String[] ITEMM2={"设置"};
    //拍照当头像
    public static final int TAKE_PHOTO = 1;
    //从相册中选择头像
    public static final int CHOOSE_PHOTO = 2;
    private RoundedImageView picture;
    private Uri imageUri;
    //handler相关
   // private static final int CHANGED=0x0010;
    //private MyHandler handler=null;
    //private MyAPP mAPP=null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText("home");
                    Intent intent_home = new Intent(myActivity.this, LoginActivity.class);
                    startActivity(intent_home);
                    return true;
                case R.id.navigation_dashboard:
                    //mTextMessage.setText("dashbord");
                    return true;
                case R.id.navigation_notifications:
                    Intent intent_my = new Intent(myActivity.this, myActivity.class);
                    startActivity(intent_my);
                    // mTextMessage.setText("个人中心");
                    return true;
            }
            return false;
        }
    };

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        Button takePhoto = (Button) findViewById(R.id.take_photo);
        Button chooseFromAlbum = (Button) findViewById(R.id.choose_from_album);
        picture = (RoundedImageView) findViewById(R.id.picture);


        //android.R.layout.simple_list_item_1是listview子布局id
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                myActivity.this, android.R.layout.simple_list_item_1, ITEMM1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                myActivity.this, android.R.layout.simple_list_item_1, ITEMM2);

        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        ListView listView2 = (ListView) findViewById(R.id.list_view2);
        listView2.setAdapter(adapter2);

        //设置listView高度避免每个listview只显示一个Item
        setListViewHeightBasedOnChildren(listView);
        setListViewHeightBasedOnChildren(listView2);

        //handler相关
       // mAPP=(MyAPP)getApplication();
        //handler=new MyHandler();

        //点击头像进入个人资料页
        picture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                //设置共享变量
                //mAPP.setHandler(handler);
                //启动另一个activity
                Intent intent = new Intent(myActivity.this, personal_info.class);
                startActivity(intent);
            }

        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String showText = "点击第" + position + "项，id为" + id;
                Toast.makeText(myActivity.this, showText, Toast.LENGTH_LONG).show();
                //获取每个item的id
                if (id == 0) {
                    Intent intent = new Intent(myActivity.this, dailybattleActivity.class);
                    startActivity(intent);
                } else if (id == 1) {
                    Intent intent = new Intent(myActivity.this, historyactivity.class);
                    startActivity(intent);
                } else if (id == 2) {
                    Intent intent = new Intent(myActivity.this, markActivity.class);
                    startActivity(intent);
                }/*else {
                    Intent intent=new Intent(myActivity.this,setActivity.class);
                    startActivity(intent);
                }*/

            }
        });


        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String showText = "点击第" + position + "项，id为" + id;
                Toast.makeText(myActivity.this, showText, Toast.LENGTH_LONG).show();
                //获取每个item的id
                if (id == 0) {
                    Intent intent = new Intent(myActivity.this, setActivity.class);
                    startActivity(intent);
                }
            }
        });

        //为拍照按钮注册点击事件
        takePhoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //创建File对象，用于储存拍照后照片
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(myActivity.this, "com.example.lbstest.fileprovider", outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }
                //启动相机程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });

        //为从相册选择照片注册点击事件
       chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (ContextCompat.checkSelfPermission(myActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                   ActivityCompat.requestPermissions(myActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
               } else {
                    openAlbum();
               }
           }
       });
    }

    /*
    实现handler处理信息更新UI
     */

    /*final class MyHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            if(msg.what==CHANGED)//更新
            {
                //picture.setImageBitmap(bitmap);
            }

        }

    }*/

    //打开相册
    private void openAlbum()
    {
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[]permissions, int[] grantResults)
    {
        switch(requestCode)
        {
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    openAlbum();
                }
                else
                {
                    Toast.makeText(this,"you denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        //将拍摄的照片显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if(resultCode==RESULT_OK)
                {
                    //4.4以上 选取图片返回一个封装过的Uri
                    if(Build.VERSION.SDK_INT>=19){handleImageOnKitKat(data);}
                    else{handleImageBeforeKitKat(data);}
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //若是document类型uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                //Toast.makeText(this, "是document类型uri", Toast.LENGTH_SHORT).show();
                String id = docId.split(":")[1];//解析出数字格式id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())) {
                //若是content型uri，普通处理
                imagePath = getImagePath(uri, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                //file型uri，直接获取图片路径
                uri.getPath();
            }
            displayImage(imagePath);
        }


    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //用两个参数来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "无法获取图片", Toast.LENGTH_SHORT).show();
        }
    }
}









