package com.example.diary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

//编写日记的界面
public class DiaryWrite extends AppCompatActivity {

   // public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private MyDatabaseHelper dbHelper = new MyDatabaseHelper(this,"Book.db",null,3);
    private TextView clock;
    private EditText title;
    private EditText content;
    private ImageView picture;
    String master;
    //private Uri imageUri;//用于拍照

//重写菜单的创建函数，创建菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

//对菜单中的选项重写响应函数
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
      //添加数据
            case R.id.add_item:
                clock=(TextView)findViewById(R.id.clock);
                title=(EditText)findViewById(R.id.title);
                content=(EditText)findViewById(R.id.Content);
        //获得已有的数据库
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
       //输入数据,插入数据库
                values.put("author",master);
                values.put("time",clock.getText().toString());
                values.put("title",title.getText().toString());
                values.put("content",content.getText().toString());
                //获得图片信息
                //picture.setDrawingCacheEnabled(true);
               // Bitmap bitmap = picture.getDrawingCache();
        //如果有图片则转换为字节数组存放
                try{
                final ByteArrayOutputStream os = new ByteArrayOutputStream();
                Bitmap bitmap = ((BitmapDrawable)picture.getDrawable()).getBitmap();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,os);
                values.put("picture",os.toByteArray());
            } catch (NullPointerException e) {
                values.put("picture", (byte[]) null);
            }
                db.insert("Note",null,values);
               // picture.setDrawingCacheEnabled(false);
                Toast.makeText(this, "you click add", Toast.LENGTH_SHORT).show();
        //转到主活动界面
                Intent intent = new Intent(this,MainActivity.class);
                intent.putExtra("author_name",master);
                startActivity(intent);
                finish();
                break;
        //返回主页面
                case R.id.back_item:
                Intent back =  new Intent(this,MainActivity.class);
                back.putExtra("author_name",master);
                startActivity(back);
                finish();
                break;
            default:
                break;
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_write);
//获得登录的作者名
        Intent intentget2 = getIntent();
        master = intentget2.getStringExtra("master");

//定义相应的组件
        TextView clock;
        Button choseRromAlbum;
        String year,month,day,weekday;
        Calendar calendar = Calendar.getInstance();

        clock=(TextView)findViewById(R.id.clock);
        picture = (ImageView)findViewById(R.id.picture);
        choseRromAlbum = (Button) findViewById(R.id.choose_from_album);
//获取年，月，日，星期几，并且显示
        year = String.valueOf(calendar.get(Calendar.YEAR));
        month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        weekday = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
        clock.setText(year+"年  "+month+"月  "+day+"日 "+" 星期"+weekday);
//设置事件监听器
        choseRromAlbum.setOnClickListener(new myOnClickListener());
    }

//响应函数
    class myOnClickListener implements View.OnClickListener {

        public void onClick(View view) {
            switch (view.getId()) {
/*//拍照
                case R.id.take_photo:
                //创建对象用于获取拍摄的照片，getExternalCacheDir() 函数用于获取缓存目录
                    File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
                    try {
                        if(outputImage.exists()) {
                            outputImage.delete();
                        }
                        outputImage.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                //判断系统版本，不同版本使用不同方法
                    if (Build.VERSION.SDK_INT >= 24) {
                        //FileProvider提高安全性
                        imageUri = FileProvider.getUriForFile(DiaryWrite.this,"com.example.diary.FileProvider",outputImage);
                    } else {
                        imageUri = Uri.fromFile(outputImage);
                    }
                //启动相机程序
                    Intent intent2 = new Intent("android.media.action.IMAGE_CAPTURE");
                //指定图片输出地址
                    intent2.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                    startActivityForResult(intent2, TAKE_PHOTO);
                    break;*/
//图库中选择照片
                case R.id.choose_from_album:
                    //为了读取SD卡中的图片，要动态申请WRITE_EXTERNAL_STORAGE者个权限，同时获得读写权限
                    if (ContextCompat.checkSelfPermission(DiaryWrite.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(DiaryWrite.this,new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE },1);
                    } else {
                        openAlbum();
                    }
/*                    Intent intent3 = new Intent(Intent.ACTION_GET_CONTENT);
                    intent3.setType("image/*");
                    startActivityForResult(intent3, 1);*/
                    //break;
            }
        }
    }

//打开相册
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
//选择完图片后可以回到onActivityResult方法进行处理
//传入choose_photo参数，回到onActivityResult的时候直接进入选择图片的case
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

//重写相册函数
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
        //权限判断，如果有访问权限则打开相册，无权限返回提示
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(DiaryWrite.this, "you denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

//重写函数,如果拍照成功就可以用decodeStream函数将图片进行解析然后输出在ImageView框中
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
/*            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        //将拍摄的照片显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;*/
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK) {
               //判断手机系统版本号
                    if(Build.VERSION.SDK_INT >= 19) {
                //4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                //4.4及以下的版本使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

//兼顾旧版本和新版本手机,获得图片的真实路径然后显示
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(DiaryWrite.this,uri)) {
       //如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            //如果权限为media格式需要对id再次解析
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的ID
                String selection = MediaStore.Images.Media._ID + "=" + id;
                //解析之后用id构建新的Uri和条件语句通过getImagePath获取图片真实路径
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
        //如果是file类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri,null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        //根据图片路径显示图片
        displayImage(imagePath);
    }

//旧版本手机
    private void handleImageBeforeKitKat(Intent data) {
        //不需要进行解析
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

//获得图片的真实路径
    @SuppressLint("Range")
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null) {
            if(cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

//显示图片
    private void displayImage(String imagePath) {
        if(imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        } else {
            Toast.makeText(DiaryWrite.this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

}