package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
//显示数据库中的内容
///将对Diary的操作改为对Note的操作
public class MainActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    private List<Note> noteList = new ArrayList<>();
    String author_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button edit;
       // Button query;
//获得从登录界面输入的作者名
        Intent intentget = getIntent();
        author_name =  intentget.getStringExtra("author_name");
        Log.d("MainActivity","author_name = "+author_name);

//用于构造数据库，表
        //dbHelper = new MyDatabaseHelper(this,"Book.db",null,2);
        //Button createdatabase = (Button)findViewById(R.id.create_database);

//定义对数据库操作的一个对象
        dbHelper = new MyDatabaseHelper(this,"Book.db",null,3);
        //createdatabase.setOnClickListener(new MyOnClickListener());
        edit = (Button) findViewById(R.id.edit);
        //query = (Button) findViewById(R.id.query_data);
        edit.setOnClickListener(new MyOnClickListener());
       // query.setOnClickListener(new MyOnClickListener());

//获得数据库所有信息
        initDiary();
        NoteAdapter adapter = new NoteAdapter(MainActivity.this,R.layout.diary_item,noteList);
        ListView listView = (ListView)findViewById(R.id.list_item);
        listView.setAdapter(adapter);

//内部类,设置点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note note = noteList.get(position);
                Bundle bd = new Bundle();
        //点击后跳转到修改界面
                Intent intent3 = new Intent(MainActivity.this,Update.class);
                bd.putString("author_1",note.getAuthor());
                bd.putInt("id_1", note.getId());
                intent3.putExtras(bd);
                startActivity(intent3);
            }
        });

    }

//响应函数
    class MyOnClickListener implements View.OnClickListener {
        public void onClick(View view) {
//跳转到编写日记的活动界面
            switch(view.getId()) {
//编辑日记按钮
                case R.id.edit:
                    Intent intent = new Intent(MainActivity.this,DiaryWrite.class);
                    intent.putExtra("master",author_name);
                    startActivity(intent);
                    break;
/*                case R.id.query_data:
                    SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                    editor.putString("author","l3");
                    editor.apply();
                    Toast.makeText(MainActivity.this, "you add shared", Toast.LENGTH_SHORT).show();
                    break;*/
//构造数据库和表
/*                    case R.id.create_database:
                        dbHelper.getWritableDatabase();
                        Toast.makeText(MainActivity.this, "create table succeed", Toast.LENGTH_SHORT).show();
                        break;*/
            }
        }
    }

//获取数据库中的数据，将每一行数据初始化成一个对象，装到数组中
   private void initDiary() {
        //得到已有的数据库对象
        SQLiteDatabase db =dbHelper.getWritableDatabase();
        //查询所有数据,用一个日记对象的列表获得数据库中所有已有的数据
        Cursor cursor = db.query("Note",null,"author=?", new String[]{String.valueOf(author_name)},null,null,null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String author = cursor.getString(cursor.getColumnIndex("author"));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex("time"));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") byte[] picture = cursor.getBlob(cursor.getColumnIndex("picture"));

                Log.d("MainActivity","book author is "+author);
                Log.d("MainActivity","book title is "+title);
                Log.d("MainActivity","book time is "+time);
                Log.d("MainActivity","book content is "+content);
                Log.d("MainActivity","book id is "+id);
                Log.d("MainActivity","book picture is "+picture);

                Note note = new Note(id,author,title,time,content,picture);
                noteList.add(note);
            }while (cursor.moveToNext());
        }
        cursor.close();

    }
}