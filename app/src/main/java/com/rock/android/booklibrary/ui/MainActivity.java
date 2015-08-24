package com.rock.android.booklibrary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.rock.android.booklibrary.R;
import com.rock.android.booklibrary.model.Book;
import com.rock.android.booklibrary.ui.adapter.BookListAdapter;
import com.rock.android.booklibrary.util.Constants;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import pullToRefreshLibrary.PullToRefreshBase;
import pullToRefreshLibrary.PullToRefreshListView;

public class MainActivity extends BaseActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2<ListView>,AdapterView.OnItemClickListener{

    private PullToRefreshListView booksListView;
    private com.getbase.floatingactionbutton.AddFloatingActionButton addBookBtn;
    private BookListAdapter mAdapter;
    private List<Book> mBookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.addBookBtn = (AddFloatingActionButton) findViewById(R.id.addBookBtn);
        this.booksListView = (PullToRefreshListView) findViewById(R.id.booksListView);

        mBookList = new ArrayList<>();
        mAdapter = new BookListAdapter(this,mBookList);
        booksListView.setAdapter(mAdapter);
        booksListView.setOnItemClickListener(this);
        booksListView.setOnRefreshListener(this);
        addBookBtn.setOnClickListener(this);

        requestBooks();

    }


    private void requestBooks(){
        BmobQuery<Book> query = new BmobQuery<>();
        query.setLimit(Constants.PAGE_NUMBER);
        query.setSkip(mAdapter.getSkip());
        query.findObjects(this, new FindListener<Book>() {
            @Override
            public void onSuccess(List<Book> list) {
                booksListView.onRefreshComplete();
                if(mAdapter.getPage() == 1){
                    mAdapter.clear();
                }
                mAdapter.addAll(list);
                if(list.size() >= Constants.PAGE_NUMBER){
                    booksListView.setMode(PullToRefreshBase.Mode.BOTH);
                }else{
                    booksListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
            }

            @Override
            public void onError(int i, String s) {
                booksListView.onRefreshComplete();
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

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        mAdapter.setPage(1);
        requestBooks();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        mAdapter.setPagePlusOne();
        requestBooks();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
