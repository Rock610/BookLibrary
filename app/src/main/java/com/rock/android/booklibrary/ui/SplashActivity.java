package com.rock.android.booklibrary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.rock.android.booklibrary.R;
import com.rock.android.booklibrary.util.Constants;

import cn.bmob.v3.Bmob;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Bmob.initialize(this.getApplicationContext(), Constants.BMOB_KEY);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }


}
