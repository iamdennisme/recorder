package com.dennisce.recorder.tools.io;

import android.provider.BaseColumns;

public class RecorderDBUtil implements BaseColumns {//DB表信息
    public static final String TABLE_NAME = "record_info_saved";
    public static final String COLUMN_NAME_RECORD_INFO_NAME = "name";
    public static final String COLUMN_NAME_RECORD_INFO_PATH = "path";
    public static final String COLUMN_NAME_RECORD_INFO_LENGTH = "length";
    public static final String COLUMN_NAME_RECORD_INFO_CREATE_TIME = "create_time";
    public static final String TEXT_TYPE = " TEXT";
    public static final String INTEGER_TYPE = " TEXT";
    public static final String COMMA_SEP = ",";
}