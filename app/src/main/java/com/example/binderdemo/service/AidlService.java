package com.example.binderdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.binderdemo.Book;
import com.example.binderdemo.BookController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Studio.
 *
 * @author: linruihang
 * @Date: 2021/10/25
 * @Time: 13:22
 */
public class AidlService extends Service {
    private static final String TAG = "AidlService";
    private List<Book> mBookList;


    @Override
    public void onCreate() {
        super.onCreate();
        initData();
    }

    private void initData() {
        mBookList = new ArrayList<>();
        mBookList.add(new Book("book1"));
        mBookList.add(new Book("book2"));
        mBookList.add(new Book("book3"));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new BookBinder();
    }

    class BookBinder extends BookController.Stub {

        @Override
        public List<Book> getBookList() throws RemoteException {
            Log.d(TAG, "getBookList: " + mBookList.size());
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            /**
             *void addBook(out Book book);
             * <p>
             *      参数tag为 out 的情况下，客户端不能向服务端传递数据，参数book相当于 book = new Book();
             *      所以Book必须得有无参构造函数，否则会报错；同时，客户端获取到服务端的数据（如服务端改变书名book.setName("Android开发艺术探索");
             *      服务端能够获取到新的book实例）
             * </p>
             *
             * <p>
             *      参数tag为 in 的情况下，服务端能获取到客户端传递过来的数据，但是客户端通过参数无法感知参数变化，
             *      但是能够通过返回值获取服务端相关的数据
             * </p>
             *
             * <p>
             *     参数为 inout 的情况下，服务端和客户端数据能够双向流动，服务端能够接受客户端传递过来的数据（参数），
             *     客户端也能感知服务端对数据（参数）的更改
             * </p>
             *
             */
            synchronized (this) {
                if (book != null) {
                    mBookList.add(book);
                    //book.setName("Android开发艺术探索");
                    Log.d(TAG, "addBook: " + book.toString());
                } else {
                    Log.e(TAG, "addBook: book is null");
                    Book bookAidl = new Book("aidl");
                    mBookList.add(bookAidl);
                }
            }
        }

        @Override
        public Book getBook(int pos) throws RemoteException {
            synchronized (this) {
                Log.d(TAG, "getBook: " + pos);
                if (pos < 0 || pos >= mBookList.size()) {
                    return null;
                }
                return mBookList.get(pos);
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy================");
    }
}
