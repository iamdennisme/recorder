package com.dennisce.recorder.tools.IO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.dennisce.recorder.mvp.model.RecorderInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by dennis on 2018/11/20
 */
public class RecorderDbHelper extends SQLiteOpenHelper {

    private static RecorderDbHelper recorderDbHelper;

    public static RecorderDbHelper getInstance(Context context) {
        if (recorderDbHelper == null) {
            synchronized (RecorderDbHelper.class) {
                if (recorderDbHelper == null) {
                    recorderDbHelper = new RecorderDbHelper(context);
                }
            }
        }
        return recorderDbHelper;
    }

    private Context mContext;

    private static final String DATABASE_NAME = "recorder.db";

    private static final int DATABASE_VERSION = 1;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getTableSql());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private RecorderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    public List<RecorderInfo> getAllItem() {
        String[] projection = {
                RecorderDBUtil._ID,
                RecorderDBUtil.COLUMN_NAME_RECORD_INFO_NAME,
                RecorderDBUtil.COLUMN_NAME_RECORD_INFO_PATH,
                RecorderDBUtil.COLUMN_NAME_RECORD_INFO_LENGTH,
                RecorderDBUtil.COLUMN_NAME_RECORD_INFO_CREATE_TIME
        };
        Cursor cursor = getReadableDatabase().query(RecorderDBUtil.TABLE_NAME, projection, null, null, null, null, null, null);
        List<RecorderInfo> list = new ArrayList<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                RecorderInfo item = new RecorderInfo();
                item.id = (cursor.getInt(cursor.getColumnIndex(RecorderDBUtil._ID)));
                item.name = (cursor.getString(cursor.getColumnIndex(RecorderDBUtil.COLUMN_NAME_RECORD_INFO_NAME)));
                item.filePath = (cursor.getString(cursor.getColumnIndex(RecorderDBUtil.COLUMN_NAME_RECORD_INFO_PATH)));
                item.length = (cursor.getInt(cursor.getColumnIndex(RecorderDBUtil.COLUMN_NAME_RECORD_INFO_LENGTH)));
                item.time = (cursor.getLong(cursor.getColumnIndex(RecorderDBUtil.COLUMN_NAME_RECORD_INFO_CREATE_TIME)));
                cursor.close();
                list.add(item);
                //移动到下一位
                cursor.moveToNext();
            }
        }
        cursor.close();
        return list;
    }

    public void removeRecorderInfoInfoWithId(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String[] whereArgs = {String.valueOf(id)};
        db.delete(RecorderDBUtil.TABLE_NAME, "_ID=?", whereArgs);
    }

    public int getCount() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {RecorderDBUtil._ID};
        Cursor c = db.query(RecorderDBUtil.TABLE_NAME, projection, null, null, null, null, null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public long addRecorderInfo(String recordingName, String filePath, long length) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(RecorderDBUtil.COLUMN_NAME_RECORD_INFO_NAME, recordingName);
        cv.put(RecorderDBUtil.COLUMN_NAME_RECORD_INFO_PATH, filePath);
        cv.put(RecorderDBUtil.COLUMN_NAME_RECORD_INFO_LENGTH, length);
        cv.put(RecorderDBUtil.COLUMN_NAME_RECORD_INFO_PATH, System.currentTimeMillis());
        long rowId = db.insert(RecorderDBUtil.TABLE_NAME, null, cv);
        return rowId;
    }

    private String getTableSql() {
        return "CREATE TABLE " + RecorderDBUtil.TABLE_NAME + " (" +
                RecorderDBUtil._ID + " INTEGER PRIMARY KEY" + RecorderDBUtil.COMMA_SEP +
                RecorderDBUtil.COLUMN_NAME_RECORD_INFO_NAME + RecorderDBUtil.TEXT_TYPE + RecorderDBUtil.COMMA_SEP +
                RecorderDBUtil.COLUMN_NAME_RECORD_INFO_PATH + RecorderDBUtil.TEXT_TYPE + RecorderDBUtil.COMMA_SEP +
                RecorderDBUtil.COLUMN_NAME_RECORD_INFO_LENGTH + RecorderDBUtil.INTEGER_TYPE + RecorderDBUtil.COMMA_SEP +
                RecorderDBUtil.COLUMN_NAME_RECORD_INFO_CREATE_TIME + RecorderDBUtil.INTEGER_TYPE + ")";
    }
}
