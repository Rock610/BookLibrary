package com.rock.android.booklibrary.ui.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rock.android.booklibrary.R;
import com.rock.android.booklibrary.model.Book;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rock on 15/8/5.
 */
public class BookListAdapter extends ArrayListAdapter<Book>{

    public BookListAdapter(Activity context, List<Book> list) {
        super(context, list);
        page = 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_booklist,null);
            holder = new ViewHolder();
            holder.bookImage = (ImageView) convertView.findViewById(R.id.bookImageView);
            holder.bookName = (TextView) convertView.findViewById(R.id.bookNameTv);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Book b = ((Book)getItem(position));
        holder.bookName.setText(b.name);
        String url = b.bookPic;
        if(TextUtils.isEmpty(url)){
            url = "empty";
        }

        Picasso.with(mContext).load(url).fit().into(holder.bookImage);
        return convertView;
    }

    private class ViewHolder{
        ImageView bookImage;
        TextView bookName;
    }


}
