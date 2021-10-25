package com.example.binderdemo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * Created by Android Studio.
 *
 * @author: linruihang
 * @Date: 2021/10/25
 * @Time: 11:23
 */
public class Book implements Parcelable {
    private String name;

    public Book() {
    }

    public Book(String name) {
        this.name = name;
    }

    protected Book(Parcel in) {
        name = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
    }

    public void readFromParcel(Parcel parcel) {
        name = parcel.readString();
    }

    @NonNull
    @Override
    public String toString() {
        return "book name :" + name;
    }
}
