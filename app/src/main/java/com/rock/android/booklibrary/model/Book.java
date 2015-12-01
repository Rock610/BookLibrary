package com.rock.android.booklibrary.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by rock on 15/8/4.
 */
public class Book extends BmobObject{
    public String name;
    public String bookId;
    public String bookPic;
    public BookUser user;
    public boolean isLend;

}
