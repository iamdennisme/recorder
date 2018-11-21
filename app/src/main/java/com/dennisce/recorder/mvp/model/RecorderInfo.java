package com.dennisce.recorder.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Create by dennis on 2018/11/20
 */

//android中字段搜寻比方法效率快3-7倍。非必要不封装
public class RecorderInfo implements Parcelable {
    public String name;
    public String filePath;
    public int id;//主键
    public int length;
    public long time;

    public RecorderInfo() {

    }

    protected RecorderInfo(Parcel in) {
        name = in.readString();
        filePath = in.readString();
        id = in.readInt();
        length = in.readInt();
        time = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(filePath);
        dest.writeInt(id);
        dest.writeInt(length);
        dest.writeLong(time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RecorderInfo> CREATOR = new Creator<RecorderInfo>() {
        @Override
        public RecorderInfo createFromParcel(Parcel in) {
            return new RecorderInfo(in);
        }

        @Override
        public RecorderInfo[] newArray(int size) {
            return new RecorderInfo[size];
        }
    };
}