package com.example.leechanguk.blindnessmoney;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Leechanguk on 2016-11-21.
 */
public class TradeListViewAdapter extends BaseAdapter {
    ArrayList<TradeListItem> listViewItemList = new ArrayList<TradeListItem>();

    Context context;
    LayoutInflater inflator;
    CheckBox check;
    int layout;
    boolean[] isCheckConfirm;


    public TradeListViewAdapter(Context context, int resource, int size){
        this.context = context;
        this.inflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = resource;
        isCheckConfirm = new boolean[size];

    }

    @Override
    public int getCount(){
        return listViewItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.trade_listview_item,parent,false);
        }

        TextView targetTextView = (TextView)convertView.findViewById(R.id.tradeTarget);
        TextView priceTextView = (TextView)convertView.findViewById(R.id.tradePrice);
        TextView timeTextView = (TextView)convertView.findViewById(R.id.tradeTime);
        TextView contentTextView = (TextView)convertView.findViewById(R.id.tradeContent);

        TradeListItem listItem = listViewItemList.get(position);

        check = (CheckBox)convertView.findViewById(R.id.tradeCheck);
        check.setClickable(true);
        check.setFocusable(false);

        check.setChecked(isCheckConfirm[position]);

//        iconImageView.setImageBitmap(listItem.getIcon());
        targetTextView.setText(listItem.getTarget());
        priceTextView.setText(listItem.getPrice());
        timeTextView.setText(listItem.getTime());
        contentTextView.setText(listItem.getContent());


        return convertView;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position){
        return listViewItemList.get(position);
    }

    public void addItem(String target, String time, String price, String content){
        TradeListItem item = new TradeListItem();

        item.setTarget(target);
        item.setTime(time);
        item.setPrice(price);
        item.setContent(content);

        listViewItemList.add(item);
    }

    public void remove(int position){
        listViewItemList.remove(position);
    }

    public void removeAll(){
        for(int i =  0; i < listViewItemList.size(); i++){
            listViewItemList.remove(0);
        }
    }

    public void dataChange(int size){
        isCheckConfirm = new boolean[size];
        notifyDataSetChanged();
    }

    public void setChecked(int position){
        isCheckConfirm[position] = !isCheckConfirm[position];
    }
}
