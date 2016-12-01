package com.example.leechanguk.blindnessmoney;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

/**
 * Created by Leechanguk on 2016-11-22.
 */
public class FriendsListAdapter extends BaseAdapter {
    private ArrayList<FriendsListData> listData = new ArrayList<FriendsListData>();
    private ArrayList<FriendsListData> arrayList = new ArrayList<FriendsListData>();
    CheckBox check;
    FriendsListData data;
    private boolean[] isCheckConfirm;

    Context ctx;

    public FriendsListAdapter(Context context, int size)
    {
        this.ctx = context;
        isCheckConfirm = new boolean[size];
    }

    @Override
    public int getCount(){
        return listData.size();
    }

    @Override
    public Object getItem(int position){
        return listData.get(position);
    }

    @Override
    public long getItemId(int poistion){
        return poistion;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.friends_list_item, null);

        }
        TextView textView = (TextView)convertView.findViewById(R.id.friendsName);

        data = listData.get(position);

        textView.setText(data.getName());

        check = (CheckBox)convertView.findViewById(R.id.friendsCheck);
        check.setClickable(false);
        check.setFocusable(false);

        check.setChecked(isCheckConfirm[position]);

        return convertView;

    }

    public void setChecked(int position){
        if(!check.isChecked()){
            FriendsListActivity.receiver.add(listData.get(position).getName());
//            listData.get(position).checkBox.setChecked(true);
        } else {
            FriendsListActivity.receiver.remove(listData.get(position).getName());
//            check.setChecked(false);
        }
        isCheckConfirm[position] = !isCheckConfirm[position];
    }

    public void addItem(String name){
        FriendsListData data = new FriendsListData();
        data.name = name;

        listData.add(data);
        arrayList.add(data);
    }

    public int contain(String name){
        for(int i = 0 ; i < listData.size(); i++){
            if(listData.get(i).getName().equals(name))
                return i;
        }
        return -1;
    }

    public void remove(int position){
        listData.remove(position);
        dataChange(listData.size());
    }

    public void sort(){
        Collections.sort(listData, FriendsListData.CHAR_COMPARATOR);
        dataChange(listData.size());
    }

    public void dataChange(int size){
        isCheckConfirm = new boolean[size];
        notifyDataSetChanged();
    }

    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        listData.clear();
        if(charText.length() == 0){
            listData.addAll(arrayList);
        } else {
            for (FriendsListData item : arrayList){
                if(item.getName().contains(charText)){
                    listData.add(item);
                }
            }
        }
        dataChange(listData.size()); //로 바꾸기
        //notifyDataSetChanged();
    }
}
