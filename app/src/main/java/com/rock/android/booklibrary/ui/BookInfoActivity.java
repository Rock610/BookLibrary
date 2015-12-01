package com.rock.android.booklibrary.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rock.android.booklibrary.R;
import com.rock.android.booklibrary.model.Book;
import com.squareup.picasso.Picasso;

public class BookInfoActivity extends BaseActivity implements View.OnClickListener{

    private static final int REQUEST_LEND = 100;
    public static final String BOOK_TAG = "abook";
    private android.widget.ImageView bookImageView;
    private android.widget.Button lendBtn;
    private android.widget.TextView bookNameTv;
    private android.widget.TextView bookAuthorTv;
    private android.widget.TextView bookBorrowInfoTv;
    private android.widget.TextView bookIdTv;
    private Book mBook;
    private AlertDialog mGetBackDialog;

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
            lendBtn.setVisibility(View.VISIBLE);
            bookIdTv.setText("id:"+mBook.bookId);
            bookNameTv.setText("书名:"+mBook.name);
            String image = mBook.bookPic;
            if(!TextUtils.isEmpty(image)){
                Picasso.with(this).load(image).fit().into(bookImageView);
            }

            if(mBook.isLend){
                String msg = "状态: 已借出给"+mBook.user.email;
                bookBorrowInfoTv.setText(msg);
                lendBtn.setText("收回");
            }else{
                bookBorrowInfoTv.setText("状态: 未借出");
                lendBtn.setText("出借");
            }
        }else{
            lendBtn.setVisibility(View.GONE);
        }
        lendBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lendBtn:
                if(!mBook.isLend){
                    Intent intent = new Intent(this,LendoutActivity.class);
                    intent.putExtra(BookInfoActivity.BOOK_TAG,mBook);
                    startActivityForResult(intent,REQUEST_LEND);
                }else{
                    showGetBackDialog();
                }

                break;
        }
    }

    private void showGetBackDialog(){
        if(mGetBackDialog == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("确认已收回?");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mBook.isLend = false;
                    mBook.remove("user");
                    dialog.dismiss();
                    mBook.update(BookInfoActivity.this, mBook.getObjectId(), null);
                    bookBorrowInfoTv.setText("状态: 未借出");
                    lendBtn.setText("出借");
                }
            });

            builder.setNegativeButton("取消",null);
            mGetBackDialog = builder.create();
        }
        mGetBackDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_LEND){
            if(resultCode == Activity.RESULT_OK){
                if(data != null){
                    mBook = (Book) data.getSerializableExtra(BOOK_TAG);
                    String msg = "状态: 已借出给"+mBook.user.email;
                    bookBorrowInfoTv.setText(msg);
                    lendBtn.setText("收回");
                    mBook.isLend = true;
                }

            }
        }
    }
}
