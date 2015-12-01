package com.rock.android.booklibrary.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rock.android.booklibrary.R;
import com.rock.android.booklibrary.model.BookUser;
import com.rock.android.booklibrary.util.Constants;
import com.rock.android.booklibrary.util.Util;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class PersonModifyActivity extends BaseActivity implements View.OnClickListener{

    private android.widget.EditText nameEt;
    private android.widget.EditText emailEt;
    private android.widget.Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_person_modify);
        this.saveBtn = (Button) findViewById(R.id.saveBtn);
        this.emailEt = (EditText) findViewById(R.id.emailEt);
        this.nameEt = (EditText) findViewById(R.id.nameEt);

        saveBtn.setOnClickListener(this);
    }

    private void isUserExist(){
        String email = emailEt.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(PersonModifyActivity.this, "请填写email!", Toast.LENGTH_SHORT).show();
        }else{
            BmobQuery<BookUser> query = new BmobQuery<>();
            query.addWhereEqualTo("email",email);
            query.findObjects(this, new FindListener<BookUser>() {
                @Override
                public void onSuccess(List<BookUser> list) {
                    if(!Util.ListNotEmpty(list)){
                        createUser();
                    }
                }

                @Override
                public void onError(int i, String s) {

                }
            });
        }
    }

    private void createUser(){
        final BookUser user = new BookUser();
        user.email = emailEt.getText().toString();
        user.name = nameEt.getText().toString();
        user.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(PersonModifyActivity.this, "保存成功!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra(Constants.BOOKUSER_KEY,user);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(PersonModifyActivity.this, "保存失败!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveBtn:
                isUserExist();
                break;
        }
    }
}
