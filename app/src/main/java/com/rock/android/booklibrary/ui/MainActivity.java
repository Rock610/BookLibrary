package com.rock.android.booklibrary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.rock.android.booklibrary.R;
import com.rock.android.booklibrary.model.Book;
import com.rock.android.booklibrary.ui.adapter.BookListAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private android.widget.ListView booksListView;
    private com.getbase.floatingactionbutton.AddFloatingActionButton addBookBtn;
    private BookListAdapter mAdapter;
    private List<Book> mBookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.addBookBtn = (AddFloatingActionButton) findViewById(R.id.addBookBtn);
        this.booksListView = (ListView) findViewById(R.id.booksListView);

        mBookList = new ArrayList<>();
        mAdapter = new BookListAdapter(this,mBookList);
        booksListView.setAdapter(mAdapter);
        addBookBtn.setOnClickListener(this);

        BmobQuery<Book> query = new BmobQuery<>();
        query.setLimit(20);
        query.findObjects(this, new FindListener<Book>() {
            @Override
            public void onSuccess(List<Book> list) {
                mAdapter.addAll(list);
            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.addBookBtn:
                Intent intent = new Intent(this,CreateBookActivity.class);
                startActivity(intent);
                break;
        }
    }
}
