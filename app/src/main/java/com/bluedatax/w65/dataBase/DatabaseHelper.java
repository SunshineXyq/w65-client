package com.bluedatax.w65.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bdx108 on 15/12/25.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private String create;
    private static DatabaseHelper helper;
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    //为了简化构造器的使用，我们自定义一个构造器
    private DatabaseHelper(Context context, String name,String create) {
        this(context, name, null, 1);//传入Context和数据库的名称，调用上面那个构造器
        this.create=create;
    }
    //将自定义的数据库创建类单例。
    public static  synchronized  DatabaseHelper getInstance(Context context,String create) {
        if(helper==null){
            helper = new DatabaseHelper(context, "database_name",create);//数据库名称为create_db。

        }
        return  helper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
