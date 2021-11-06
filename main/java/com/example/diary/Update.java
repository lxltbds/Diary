package com.example.diary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

//更新日记的界面
public class Update extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    private TextView clock;
    private EditText title;
    private EditText content;
    private ImageView show_picture;
    public static final int CHOOSE_PHOTO = 2;
    String author_2;
    int id_2;

//重写菜单的创建函数，创建菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_update,menu);
        return true;
    }
//菜单中选项的响应函数
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
//更新数据
            case R.id.update_item:
                dbHelper = new MyDatabaseHelper(this,"Book.db",null,3);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
//获取更新的数据
                values.put("author",author_2);
                values.put("title",title.getText().toString());
                values.put("content",content.getText().toString());
//查看ImageView框中是否有图片，有图片则将图片转换为字节数组存放,无图片则存放null
                try{
                    final ByteArrayOutputStream os = new ByteArrayOutputStream();
                    Bitmap bitmap = ((BitmapDrawable)show_picture.getDrawable()).getBitmap();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,os);
                    values.put("picture",os.toByteArray());
                } catch (NullPointerException e) {
                    values.put("picture", (byte[]) null);
                }
 //插入后跳转回主界面
                db.update("Note",values,"id=?",new String[]{String.valueOf(id_2)});
                Intent intent = new Intent(Update.this,MainActivity.class);
                intent.putExtra("author_name",author_2);
                startActivity(intent);
                finish();
                break;
//删除数据
            case R.id.remove_item:
                dbHelper = new MyDatabaseHelper(this,"Book.db",null,3);
                SQLiteDatabase delete = dbHelper.getWritableDatabase();
                delete.delete("Note","id=?",new String[]{String.valueOf(id_2)});
                Intent intent7 = new Intent(Update.this,MainActivity.class);
                intent7.putExtra("author_name",author_2);
                startActivity(intent7);
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
        setContentView(R.layout.activity_update);

        Button choose;
        choose = (Button)findViewById(R.id.choose);
        show_picture = (ImageView)findViewById(R.id.show_picture);
        title=(EditText)findViewById(R.id.title2);
        content=(EditText)findViewById(R.id.Content2);
        clock=(TextView)findViewById(R.id.clock2);

 //内部类实现选择图片的功能
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(Update.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Update.this,new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE },1);
                } else {
                    openAlbum();
                }
            }
        });

        Intent intent4 = getIntent();
        Bundle bd = intent4.getExtras();

        author_2 = bd.getString("author_1");
        id_2 = bd.getInt("id_1");

        Log.d("Update","author= "+author_2);
        Log.d("Update","id= "+id_2);

 //在数据库中选出id等于主界面中点击相应ListView选项的id的内容显示在相应的控件中
        dbHelper = new MyDatabaseHelper(this,"Book.db",null,3);
        SQLiteDatabase db2 = dbHelper.getWritableDatabase();
        Cursor cursor=db2.query("Note",null,"id=?",
                new String[]{String.valueOf(id_2)},null,null,null);
        if(cursor.moveToFirst()){
            @SuppressLint("Range") String select_title=cursor.getString(cursor.getColumnIndex("title"));
            @SuppressLint("Range") String select_content=cursor.getString(cursor.getColumnIndex("content"));
            @SuppressLint("Range") String select_time=cursor.getString(cursor.getColumnIndex("time"));
            title.setText(select_title);
            clock.setText(select_time);
            content.setText(select_content);
    //如果该项在表中有图片则得到并输出，没有则不做处理
            try{
                @SuppressLint("Range") byte[] in = cursor.getBlob(cursor.getColumnIndex("picture"));
                Bitmap bitmap=BitmapFactory.decodeByteArray(in,0,in.length);
                show_picture.setImageBitmap(bitmap);
            } catch (RuntimeException r) {
                r.fillInStackTrace();
            }

        }

    }

//打开相册
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
//选择完图片后可以回到onActivityResult方法进行处理
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

//重写访问的方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
     //如果有访问权限则打开相册，无访问权限则输出“deny”
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(Update.this, "you denied the permission", Toast.LENGTH_SHORT).show();
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
        if(DocumentsContract.isDocumentUri(Update.this,uri)) {
            //如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的ID
                String selection = MediaStore.Images.Media._ID + "=" + id;
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
            show_picture.setImageBitmap(bitmap);
        } else {
            Toast.makeText(Update.this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

}