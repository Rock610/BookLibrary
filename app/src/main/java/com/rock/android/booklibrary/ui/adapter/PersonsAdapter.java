package com.rock.android.booklibrary.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rock.android.booklibrary.R;
import com.rock.android.booklibrary.model.BookUser;

import java.util.List;

/**
 * Created by rock on 15/11/30.
 */
public class PersonsAdapter extends ArrayListAdapter<BookUser>{


    public PersonsAdapter(Activity context) {
        super(context);
    }

    public PersonsAdapter(Activity context, List<BookUser> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_personlist,null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.nameTv);
            holder.email = (TextView) convertView.findViewById(R.id.emailTv);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        BookUser user = mList.get(position);
        holder.name.setText(user.name);
        holder.email.setText(user.email);

        return convertView;
    }

    class ViewHolder{
        TextView name,email;
    }
}
