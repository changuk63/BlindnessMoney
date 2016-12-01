package com.example.leechanguk.blindnessmoney;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Leechanguk on 2016-11-22.
 */
public class SendMsgListViewAdapter extends ArrayAdapter {
    TextView targetPrice;
    SendMsgListViewBtnItem listViewBtn;
    ArrayList<SendMsgListViewBtnItem> list;


    int resId;

    SendMsgListViewAdapter(Context context, int res, ArrayList<SendMsgListViewBtnItem> list){
        super(context, res, list);
        this.list = list;
        this.resId = res;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resId, parent, false);
        }

        TextView targetName = (TextView)convertView.findViewById(R.id.tradeTarget);
        targetPrice = (TextView)convertView.findViewById(R.id.tradePrice);

        listViewBtn = (SendMsgListViewBtnItem)getItem(position);

        targetName.setText(listViewBtn.getName());
        targetPrice.setText(listViewBtn.getPrice());

        ImageButton upBtn = (ImageButton) convertView.findViewById(R.id.up_btn);
        upBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                listViewBtn.setPrice(String.valueOf(Integer.valueOf(listViewBtn.getPrice()) + SendMsgActivity.unit));
                targetPrice.setText(String.valueOf(Integer.valueOf(listViewBtn.getPrice())));
            }
        });

        ImageButton downBtn = (ImageButton) convertView.findViewById(R.id.down_btn);
        downBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                listViewBtn.setPrice(String.valueOf(Integer.valueOf(listViewBtn.getPrice()) - SendMsgActivity.unit));
                targetPrice.setText(String.valueOf(Integer.valueOf(listViewBtn.getPrice())));
            }
        });

        return convertView;
    }
}
