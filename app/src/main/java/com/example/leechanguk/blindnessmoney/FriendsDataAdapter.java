package com.example.leechanguk.blindnessmoney;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Leechanguk on 2016-09-20.
 */
public class FriendsDataAdapter extends BaseAdapter {
    ArrayList<FriendsData> datas;
    LayoutInflater inflater;

    public FriendsDataAdapter(ArrayList<FriendsData> datas, LayoutInflater inflater){
        this.datas = datas;
        this.inflater = inflater;
    }

    @Override
    public int getCount(){
        return datas.size();
    }

    @Override
    public Object getItem(int position){
        return datas.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if(convertView == null)
            convertView = inflater.inflate(R.layout.list_row,null);

        
    }
}
