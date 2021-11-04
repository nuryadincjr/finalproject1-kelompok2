package com.nuryadincjr.todolist.data;

import android.os.*;
import androidx.room.*;

@Entity
public class ToDo implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "task_title")
    private String title;

    @ColumnInfo(name = "task_details")
    private String details;

    @ColumnInfo(name = "is_pin")
    private boolean isPin;

    @ColumnInfo(name = "is_arcip")
    private boolean isArcip;

    @ColumnInfo(name = "is_delete")
    private boolean isDelete;

    @ColumnInfo(name = "latest_edited")
    private String latestEdited;

    public ToDo(int uid, String title, String details,
                boolean isPin, boolean isArcip, boolean isDelete, String latestEdited) {
        this.uid = uid;
        this.title = title;
        this.details = details;
        this.isPin = isPin;
        this.isArcip = isArcip;
        this.isDelete = isDelete;
        this.latestEdited = latestEdited;
    }

    public ToDo() {
    }

    protected ToDo(Parcel in) {
        uid = in.readInt();
        title = in.readString();
        details = in.readString();
        isPin = in.readInt() != 0;
        isArcip = in.readInt() != 0;
        isDelete = in.readInt() != 0;
        latestEdited = in.readString();
    }

    public static final Creator<ToDo> CREATOR = new Creator<ToDo>() {
        @Override
        public ToDo createFromParcel(Parcel in) {
            return new ToDo(in);
        }

        @Override
        public ToDo[] newArray(int size) {
            return new ToDo[size];
        }
    };

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isPin() {
        return isPin;
    }

    public void setPin(boolean pin) {
        isPin = pin;
    }

    public boolean isArcip() {
        return isArcip;
    }

    public void setArcip(boolean arcip) {
        isArcip = arcip;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public String getLatestEdited() {
        return latestEdited;
    }

    public void setLatestEdited(String latestEdited) {
        this.latestEdited = latestEdited;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.uid);
        parcel.writeString(this.title);
        parcel.writeString(this.details);
        parcel.writeInt(this.isPin ? 1:0);
        parcel.writeInt(this.isArcip ? 1:0);
        parcel.writeInt(this.isDelete ? 1:0);
        parcel.writeInt(this.isPin ? 1:0);
        parcel.writeString(this.latestEdited);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ToDo that = (ToDo) obj;
        return this.getUid() == that.getUid() &&
                this.getTitle().equals(that.getTitle()) &&
                this.getDetails().equals(that.getDetails()) &&
                this.isPin() == that.isPin() &&
                this.isArcip() == that.isArcip() &&
                this.isDelete() == that.isDelete();
    }
}