package com.example.databasetest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper=new MyDatabaseHelper(this,"BookStore.db",null,1); //数据库名,版本号指定
        //如果对数据库升级了就变为dbHelper=new MyDatabaseHelper(this,"BookStore.db",null,2);
        Button createDatabase=(Button)findViewById(R.id.create_database);
        Button addData=(Button)findViewById(R.id.add_data);
        //创建数据库和表
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.getWritableDatabase();
            }
        });
        //插入数据
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                ContentValues values=new ContentValues();//数组
                //开始组装第一条数据
                values.put("name","The Da Vinci Code");
                values.put("author","Dan Brown");
                values.put("pages",454);
                values.put("price","16.96");
                db.insert("Book",null,values);//插入数据
                values.clear();
                //开始组装第二条数据
                values.put("name","The Lost Symbol");
                values.put("author","Dan Brown");
                values.put("pages",510);
                values.put("price","19.95");
                db.insert("Book",null,values);
            }
        });
        //更新数据
        Button updateData=(Button)findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                ContentValues value=new ContentValues();
                value.put("price",10.99);
                db.update("Book",value,"name=?",new String[]{"The Da Vinci Code"});//value后面就相当于where语句
            }
        });
        //删除数据
        Button deleteData=(Button)findViewById(R.id.delete_data);
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                db.delete("Book","pages >?",new String[]{"500"});
            }
        });
        //查询数据
        Button queryData=(Button)findViewById(R.id.query_data);
        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                //查询Book表中所有的数据
                Cursor cursor=db.query("Book",null,null,null,null,null,null);//7个参数：1表名，2查询哪些列，
                // 3，4约束条件查询哪些行，5 group by,6 进一步过滤,7 排序方式
                if(cursor.moveToFirst()){
                    do{
                        //遍历Cursor对象，取出数据打印
                        String name=cursor.getString(cursor.getColumnIndex("name"));
                        String author=cursor.getString(cursor.getColumnIndex("author"));
                        int pages=cursor.getInt(cursor.getColumnIndex("pages"));
                        double price=cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d("MainActivity",name);
                        Log.d("MainActivity",author);
                        Log.d("MainActivity", String.valueOf(pages));//将整型转换成字符型呈现
                        Log.d("MainActivity", String.valueOf(price));//将double型转换成字符型呈现
                    }while (cursor.moveToNext());
                }
                cursor.close();
            }
        });
        //使用事务
        Button replaceData=(Button)findViewById(R.id.replace_data);
        replaceData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                db.beginTransaction();//开启事务
                try{
                    db.delete("Book",null,null);
                    if(true){
                        //在这里手动抛出一个异常，让事务失败
                        throw new NullPointerException();
                    }
                    ContentValues value=new ContentValues();
                    value.put("name","Game of Thrones");
                    value.put("author","George Marton");
                    value.put("pages",720);
                    value.put("price",20.85);
                    db.insert("Book",null,value);
                    db.setTransactionSuccessful();//事务已经执行成功
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    db.endTransaction();//结束事务
                }
            }
        });
    }
}
