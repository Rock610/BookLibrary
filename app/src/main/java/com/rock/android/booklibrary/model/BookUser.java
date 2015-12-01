package com.rock.android.booklibrary.model;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by rock on 15/8/4.
 */
public class BookUser extends BmobObject{
    public String name;
    public String id;
    public boolean isAdmin;
    public String email;
    public List<Book> books;//拥有的书
}
