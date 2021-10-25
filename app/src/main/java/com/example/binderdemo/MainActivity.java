package com.example.binderdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.metrics.LogSessionId;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.binderdemo.service.AidlService;

import java.util.List;

/**
 * @author linruihang
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private Button mBtnGet;
    private Button mBtnAdd;
    private Button mBtnGetAll;
    private BookController mBookController;
    private ServiceConnection mServiceConnection;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnGet = findViewById(R.id.btn_get);
        mBtnAdd = findViewById(R.id.btn_add);
        mBtnGetAll = findViewById(R.id.btn_get_all);
        mBtnAdd.setOnClickListener(this::onClick);
        mBtnGet.setOnClickListener(this::onClick);
        mBtnGetAll.setOnClickListener(this::onClick);

        bind();
    }

    private void bind() {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Log.d(TAG, "onServiceConnected======");

                mBookController = BookController.Stub.asInterface(iBinder);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.d(TAG, "onServiceDisconnected======");
                mBookController = null;

            }
        };
        mIntent = new Intent(MainActivity.this, AidlService.class);
        bindService(mIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_add) {
            if (mBookController != null) {
                try {
                    Book testBook = new Book("testBook");
                    mBookController.addBook(testBook);
                    Log.d(TAG, "add book result:" + testBook.toString());
                } catch (RemoteException e) {
                    Log.e(TAG, "add book error:" + e.getMessage());
                }
            }
        } else if (id == R.id.btn_get_all) {
            if (mBookController != null) {
                try {
                    List<Book> bookList = mBookController.getBookList();
                    if (bookList != null) {
                        for (Book book : bookList) {
                            Log.d(TAG, book.toString());
                        }
                        Log.d(TAG, "book nun = " + bookList.size());
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "get book list error:" + e.getMessage());
                }
            }
        } else if (id == R.id.btn_get) {
            try {
                Book book = mBookController.getBook(2);
                if (book != null) {
                    Log.d(TAG, "get book:" + book.toString());
                } else {
                    Log.d(TAG, "get book: null");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //解绑定
        if (mServiceConnection != null) {
            unbindService(mServiceConnection);
        }
        if (mIntent != null) {
            stopService(mIntent);
        }
    }
}