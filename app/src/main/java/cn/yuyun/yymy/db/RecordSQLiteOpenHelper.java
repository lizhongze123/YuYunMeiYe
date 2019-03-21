package cn.yuyun.yymy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author
 * @desc   SQLiteOpenHelper子类用于打开数据库并进行对用户搜索历史记录进行增删减除的操作
 * @date
 */
public class RecordSQLiteOpenHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "temp.db";
    private final static int DB_VERSION = 1;
    public static final String TABLE_STORE = "table_store";
    public static final String TABLE_STAFF = "table_staff";
    public static final String TABLE_MEMBER = "table_member";

    public RecordSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlStore = "CREATE TABLE IF NOT EXISTS " + TABLE_STORE + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);";
        String sqlStaff = "CREATE TABLE IF NOT EXISTS " + TABLE_STAFF + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);";
        String sqlMember = "CREATE TABLE IF NOT EXISTS " + TABLE_MEMBER + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);";
        db.execSQL(sqlStore);
        db.execSQL(sqlStaff);
        db.execSQL(sqlMember);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
