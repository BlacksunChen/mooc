package cmr.mooc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 28364 on 2017/4/21.
 */

public class MyDBHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String SQL = "create table users("+
            "id integer primary key autoincrement,"+
            "userName text,"+
            "password text)";

    /**
     * 在SQLiteOpenHelper的子类当中，必须有该构造函数
     * @param context   上下文对象
     * @param name      数据库名称
     * @param factory
     * @param version   当前数据库的版本，值必须是整数并且是递增的状态
     */
    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                      int version)
    {
        super(context,name,factory,version);
    }
    public MyDBHelper(Context context,String name,SQLiteDatabase.CursorFactory factory)
    {
        super(context,name,factory,VERSION);
    }
    public MyDBHelper(Context context,String name)
    {
        super(context,name,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        System.out.println("create a database");
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        System.out.println("upgrade a database");
    }
}
