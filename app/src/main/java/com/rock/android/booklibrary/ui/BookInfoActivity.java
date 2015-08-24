package com.rock.android.booklibrary.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rock.android.booklibrary.R;
import com.rock.android.booklibrary.model.Book;
import com.squareup.picasso.Picasso;

public class BookInfoActivity extends BaseActivity {

    public static final String BOOK_TAG = "abook";
    private android.widget.ImageView bookImageView;
    private android.widget.Button lendBtn;
    private android.widget.TextView bookNameTv;
    private android.widget.TextView bookAuthorTv;
    private android.widget.TextView bookBorrowInfoTv;
    private android.widget.TextView bookIdTv;
    private Book mBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        init();
    }

    private void init(){
        mBook = (Book) getIntent().getSerializableExtra(BOOK_TAG);
        this.bookIdTv = (TextView) findViewById(R.id.bookIdTv);
        this.bookBorrowInfoTv = (TextView) findViewById(R.id.bookBorrowInfoTv);
        this.bookAuthorTv = (TextView) findViewById(R.id.bookAuthorTv);
        this.bookNameTv = (TextView) findViewById(R.id.bookNameTv);
        this.lendBtn = (Button) findViewById(R.id.lendBtn);
        this.bookImageView = (ImageView) findViewById(R.id.bookImageView);

        if(mBook != null){
            bookIdTv.setText(mBook.bookId);
            bookNameTv.setText(mBook.name);
            String image = mBook.bookPic;
            if(!TextUtils.isEmpty(image)){
                Picasso.with(this).load(image).fit().into(bookImageView);
            }
        }
    }

}
