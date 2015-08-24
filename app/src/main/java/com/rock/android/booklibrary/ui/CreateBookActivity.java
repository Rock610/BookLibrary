package com.rock.android.booklibrary.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bmob.BTPFileResponse;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.rock.android.booklibrary.R;
import com.rock.android.booklibrary.model.Book;
import com.squareup.picasso.Picasso;

import cn.bmob.v3.datatype.BmobFile;

public class CreateBookActivity extends BaseActivity implements View.OnClickListener{

    public static int REQUEST_LOAD_IMG = 100;
    private android.widget.Button choosePicBtn;
    private android.widget.ImageView bookFrontImageView;
    private android.widget.EditText bookNameEditText;
    private Button submitBtn;
    private String mImageUrl;
    private AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_book);
        this.submitBtn = (Button) findViewById(R.id.submitBtn);
        this.bookNameEditText = (EditText) findViewById(R.id.bookNameEditText);
        this.bookFrontImageView = (ImageView) findViewById(R.id.bookFrontImageView);
        this.choosePicBtn = (Button) findViewById(R.id.choosePicBtn);
        choosePicBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.choosePicBtn:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_LOAD_IMG);
                break;
            case R.id.submitBtn:
                submit();
                break;
        }
    }

    private void submit(){
        if(mDialog == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("请稍后...");
            builder.setMessage("正在努力加载...");
            mDialog = builder.create();
        }
        mDialog.show();

        uploadImage(mImageUrl);

    }

    private void uploadImage(String filePath){
        BTPFileResponse response = BmobProFile.getInstance(this).upload(filePath, new UploadListener() {

            @Override
            public void onSuccess(String fileName,String url,BmobFile file) {
                // TODO Auto-generated method stub
                Log.i("bmob", "文件上传成功：" + fileName + ",可访问的文件地址：" + file.getUrl());
                Book b = new Book();
                b.name = bookNameEditText.getText().toString();
                b.bookPic = file.getUrl();
                b.save(CreateBookActivity.this);
                Toast.makeText(CreateBookActivity.this,"上传成功!",Toast.LENGTH_SHORT).show();
                reset();
            }

            @Override
            public void onProgress(int progress) {
                // TODO Auto-generated method stub
                Log.i("bmob","onProgress :"+progress);
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                // TODO Auto-generated method stub
                Log.i("bmob","文件上传失败："+errormsg);
                mDialog.dismiss();
            }
        });
    }

    private void reset(){
        bookNameEditText.setText("");
        mImageUrl = "";
        Picasso.with(this).load("file://"+mImageUrl).fit().into(bookFrontImageView);
        mDialog.dismiss();
    }

    public void getImageFromResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_LOAD_IMG && resultCode == RESULT_OK
                && null != data) {
            // Get the Image from data
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            // Get the cursor
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgDecodableString = cursor.getString(columnIndex);
            cursor.close();
            System.out.println("uri==>" + imgDecodableString);

            mImageUrl = imgDecodableString;

            Picasso.with(this).load("file://"+mImageUrl).fit().into(bookFrontImageView);
            // Set the Image in ImageView after decoding the String
            //BitmapFactory.decodeFile(imgDecodableString)
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getImageFromResult(requestCode,resultCode,data);
    }
}
