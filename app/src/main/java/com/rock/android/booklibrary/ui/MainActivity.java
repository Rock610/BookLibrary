package com.rock.android.booklibrary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
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
    private ImageButton mScreeningBtn;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.addBookBtn = (AddFloatingActionButton) findViewById(R.id.addBookBtn);
        this.booksListView = (PullToRefreshListView) findViewById(R.id.booksListView);
        mScreeningBtn  = (ImageButton) findViewById(R.id.screeningBtn);

        mBookList = new ArrayList<>();
        mAdapter = new BookListAdapter(this,mBookList);
        booksListView.setAdapter(mAdapter);
        booksListView.setOnItemClickListener(this);
        booksListView.setOnRefreshListener(this);
        addBookBtn.setOnClickListener(this);
        mScreeningBtn.setOnClickListener(this);

//        requestBooks(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                booksListView.setRefreshing();
            }
        },300);

    }

    private void requestBooks(boolean isLend){
        BmobQuery<Book> query = new BmobQuery<>();
        if(isLend){
            query.addWhereEqualTo("isLend",true);
            mAdapter.setPage(1);
        }
        query.include("user");//加这句才可以把关联关系查出来，不然没有user
        query.setLimit(Constants.PAGE_NUMBER);
        query.setSkip(mAdapter.getSkip());

        query.findObjects(this, new FindListener<Book>() {
            @Override
            public void onSuccess(List<Book> list) {
                booksListView.onRefreshComplete();
                if (mAdapter.getPage() == 1) {
                    mAdapter.clear();
                }
                mAdapter.addAll(list);
                if (list.size() >= Constants.PAGE_NUMBER) {
                    booksListView.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
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
            case R.id.screeningBtn:
                //目前只做已借出筛选
                requestBooks(true);
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        mAdapter.setPage(1);
        requestBooks(false);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        mAdapter.setPagePlusOne();
        requestBooks(false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int realPosition = position -1;
        if(realPosition <mAdapter.getCount()){
            Intent intent = new Intent(this,BookInfoActivity.class);
            Book b = (Book) mAdapter.getItem(realPosition);
            intent.putExtra(BookInfoActivity.BOOK_TAG,b);
            startActivity(intent);
        }
    }
}
