package com.rock.android.booklibrary.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rock.android.booklibrary.R;
import com.rock.android.booklibrary.model.Book;
import com.rock.android.booklibrary.model.BookUser;
import com.rock.android.booklibrary.ui.adapter.PersonsAdapter;
import com.rock.android.booklibrary.util.Constants;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class LendoutActivity extends BaseActivity implements View.OnClickListener{

    private android.widget.ImageButton addAPerson;
    private android.widget.RelativeLayout titleLayout;
    private android.widget.ListView personsLV;
    private PersonsAdapter mAdapter;
    private TextView userNameTv,userEmailTv;
    private Button submitBtn;
    private BmobQuery<BookUser> query = new BmobQuery<>();
    private BookUser mCurrentUser;
    private Book mBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_lendout);
        init();
    }

    private void init(){
        mBook = (Book) getIntent().getSerializableExtra(BookInfoActivity.BOOK_TAG);
        this.personsLV = (ListView) findViewById(R.id.personsLV);
        this.titleLayout = (RelativeLayout) findViewById(R.id.titleLayout);
        this.addAPerson = (ImageButton) findViewById(R.id.addAPerson);
        userNameTv = (TextView) findViewById(R.id.nameTv);
        userEmailTv = (TextView) findViewById(R.id.emailTv);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(this);
        addAPerson.setOnClickListener(this);
        mAdapter = new PersonsAdapter(this);
        personsLV.setAdapter(mAdapter);
        personsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookUser user = (BookUser) mAdapter.getItem(position);
                mCurrentUser = user;
                setUserInfoToView(user);
            }
        });

        queryUser();

    }

    private void queryUser(){

        query.setLimit(50);
        query.setSkip(mAdapter.getSkip());
        query.findObjects(this, new FindListener<BookUser>() {
            @Override
            public void onSuccess(List<BookUser> list) {
                mAdapter.clear();
                mAdapter.addAll(list);
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.addAPerson:
                Intent intent = new Intent(this,PersonModifyActivity.class);
                startActivityForResult(intent,100);
                break;
            case R.id.submitBtn:
                if(mCurrentUser != null){
                    mBook.user = mCurrentUser;
                    mBook.isLend = true;
                    mBook.update(this, mBook.getObjectId(),new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(LendoutActivity.this, "提交成功!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.putExtra(BookInfoActivity.BOOK_TAG,mBook);
                            setResult(Activity.RESULT_OK,intent);
                            finish();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            System.out.println("failed==>code==>" + i + "===msg===>" + s);
                        }
                    });
                }
                break;
        }
    }

    private void setUserInfoToView(BookUser user){
        userNameTv.setText(user.name);
        userEmailTv.setText(user.email);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            if(resultCode == Activity.RESULT_OK){
                if(data != null){
                    mCurrentUser = (BookUser) data.getSerializableExtra(Constants.BOOKUSER_KEY);
                    setUserInfoToView(mCurrentUser);
                }
                queryUser();
            }
        }
    }
}
