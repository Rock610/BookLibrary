package com.rock.android.booklibrary.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.rock.android.booklibrary.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 *@author rock
 */
public abstract class ArrayListAdapter<T> extends BaseAdapter {
	
	protected List<T> mList;
	protected Activity mContext;
	protected ListView mListView;
	protected LayoutInflater mInflater;
	
	public ArrayListAdapter(Activity context){
		this(context, null);
	}
	
	public ArrayListAdapter(Activity context,List<T> list){
		this.mContext = context;
		this.mList = list;
		mInflater = context.getLayoutInflater();
	}

	@Override
	public int getCount() {
		if(mList != null)
			return mList.size();
		else
			return 0;
	}
	
	public void addAll(List<T> list){
		if(list == null) return;
		if(mList == null){
			mList = list;
		}else{
			mList.addAll(list);
		}
		
		notifyDataSetChanged();
	}
	
	public void remove(int position){
		if(mList != null && mList.size() > position){
			mList.remove(position);
		}
		notifyDataSetChanged();
	}

	public void clear(){
		if(Util.ListNotEmpty(mList)){
			mList.clear();
			notifyDataSetChanged();
		}
	}
	@Override
	public Object getItem(int position) {
		return mList == null ? null : mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	abstract public View getView(int position, View convertView, ViewGroup parent);
	
	public void setList(List<T> list){
		this.mList = list;
		notifyDataSetChanged();
	}
	
	public List<T> getList(){
		return mList;
	}
	
	public void setList(T[] list){
		ArrayList<T> arrayList = new ArrayList<T>(list.length);  
		for (T t : list) {  
			arrayList.add(t);  
		}  
		setList(arrayList);
	}
	
	public ListView getListView(){
		return mListView;
	}
	
	public void setListView(ListView listView){
		mListView = listView;
	}

}
