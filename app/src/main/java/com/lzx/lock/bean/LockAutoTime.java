package com.lzx.lock.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by lzx on 2017/1/10.
 *
 *
 *
 */

public class LockAutoTime implements Parcelable {
    private String title;
    private long time;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeLong(this.time);
    }

    public LockAutoTime() {
    }

    protected LockAutoTime(Parcel in) {
        this.title = in.readString();
        this.time = in.readLong();
    }

    public static final Parcelable.Creator<LockAutoTime> CREATOR = new Parcelable.Creator<LockAutoTime>() {
        @Override
        public LockAutoTime createFromParcel(Parcel source) {
            return new LockAutoTime(source);
        }

        @Override
        public LockAutoTime[] newArray(int size) {
            return new LockAutoTime[size];
        }
    };
}