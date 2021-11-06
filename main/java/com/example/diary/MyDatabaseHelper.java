package com.example.diary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

//构造日记表
//使用系统提供的SQLiteOpenHelper类，重写自己的类，用于对数据库进行操作
public class MyDatabaseHelper extends SQLiteOpenHelper {
 //表属性
    public static final String CREATE_DIARY = "create table Diary ("
            + "id integer primary key autoincrement,"
            + "author text,"
            + "title text,"
            + "time text,"
            + "content text)";

    public static final String CREATE_NOTE = "create table Note ("
            + "id integer primary key autoincrement,"
            + "author text,"
            + "title text,"
            + "time text,"
            + "content text,"
            + "picture BLOB)";
    //BLOB二进制长对象，用于存储大文件


    private Context mContext;
//构造函数
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version) {
        super(context,name,factory,version);
        mContext = context;
    }
//重写创建和更新数据库的方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DIARY);
        db.execSQL(CREATE_NOTE);
        Toast.makeText(mContext,"Create succeed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists Diary");
        db.execSQL("drop table if exists Note");
        onCreate(db);

    }
}
