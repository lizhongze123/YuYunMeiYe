package cn.yuyun.yymy.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class RecordsDao {

    RecordSQLiteOpenHelper recordHelper;

    SQLiteDatabase recordsDb;

    public RecordsDao(Context context) {
        recordHelper = new RecordSQLiteOpenHelper(context);
    }

    /**添加搜索记录*/
    public void addRecords(String table,String record) {
        if (!isHasRecord(table, record)) {
            recordsDb = recordHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", record);
            //添加
            recordsDb.insert(table, null, values);
            //关闭
            recordsDb.close();
        }
    }

    /**判断是否含有该搜索记录*/
    public boolean isHasRecord(String table,String record) {
        boolean isHasRecord = false;
        recordsDb = recordHelper.getReadableDatabase();
        Cursor cursor = recordsDb.query(table, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            if (record.equals(cursor.getString(cursor.getColumnIndexOrThrow("name")))) {
                isHasRecord = true;
            }
        }
        //关闭数据库
        recordsDb.close();
        return isHasRecord;
    }

    /**获取全部搜索记录*/
    public List<String> getRecordsList(String table) {
        List<String> recordsList = new ArrayList<>();
        recordsDb = recordHelper.getReadableDatabase();
        Cursor cursor = recordsDb.query(table, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            recordsList.add(name);
        }
        //关闭数据库
        recordsDb.close();
        return recordsList;
    }

    /**模糊查询*/
    public List<String> querySimlarRecord(String table,String record){
        String queryStr = "select * from " + table + " where name like '%" + record + "%' order by name ";
        List<String> similarRecords = new ArrayList<>();
        Cursor cursor= recordHelper.getReadableDatabase().rawQuery(queryStr,null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            similarRecords.add(name);
        }
        return similarRecords;
    }

    /**清空搜索记录*/
    public void deleteAllRecords(String table) {
        recordsDb = recordHelper.getWritableDatabase();
        recordsDb.execSQL("delete from " + table);

        recordsDb.close();
    }

}
