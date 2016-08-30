package com.example.databasetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by zwq on 2016/8/9.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper{
    public static final  String CREATE_BOOK="create table Book("
            +"id integer primary key autoincrement,"
            +"author text,"
            +"price real,"
            +"pages integer,"
            +"name text)";
//    public static final String CREATE_CATEGORY="create table Category ("
//            +"id integer primary key autoincrement,"
//            +"category_name text"
//            +"category_code integer)";                  //创建某表的sql语句
    private Context mContext;
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
//        db.execSQL(CREATE_CATEGORY);                  //执行相关的sql语句
        Toast.makeText(mContext, "create succceeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Book");
//        db.execSQL("drop table if exists Category");
        onCreate(db);

        //升级数据库的最优写法
//        switch (oldVersion){
//            case 1:
//                db.execSQL(CREATE_BOOK);
//                db.execSQL(CREATE_CATEGORY);
//            case 2:
//                db.execSQL("alter table Book add column category_id integer");
//            default:
//        }
    }
}
