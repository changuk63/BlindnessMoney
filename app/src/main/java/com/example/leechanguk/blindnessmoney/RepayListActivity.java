package com.example.leechanguk.blindnessmoney;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by Leechanguk on 2016-11-22.
 */
public class RepayListActivity extends Fragment {
    ListView listView;
    TradeListViewAdapter adapter;
    private ImageView repayRemove;
    public static RepayDBOpenHelper repayDB;

    public static RepayListActivity newInstance(){
        return new RepayListActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trade_list, container, false);

        repayRemove = (ImageView)view.findViewById(R.id.repayRemove);

        repayDB = new RepayDBOpenHelper(getActivity());
        repayDB.open();
        listView = (ListView) view.findViewById(android.R.id.list);

        adapter = new TradeListViewAdapter(getActivity(), android.R.layout.simple_list_item_1, repayDB.size()+1);
        if(adapter.getCount() != 0)
            adapter.removeAll();

        initList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setChecked(position);
                adapter.notifyDataSetChanged();
                repayRemove.setVisibility(View.VISIBLE);
            }
        });

        repayRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeList();
            }
        });
        return view;
    }
    public void removeList(){
        TradeListItem item;
        for(int i = (adapter.getCount() - 1) ; i >= 0 ; i--){
            if(adapter.isCheckConfirm[i]) {
                item = (TradeListItem) adapter.getItem(i);
                repayDB.remove(item.getTarget());
                adapter.remove(i);

            }
        }
        adapter.dataChange(adapter.getCount()+1);
        listView.setAdapter(adapter);
    }

    private void initList(){
        if(repayDB.size() > 0) {
            Cursor myDBCursor = repayDB.searchTable();
            myDBCursor.moveToLast();
            adapter.addItem(myDBCursor.getString(1), myDBCursor.getString(2), myDBCursor.getString(3), myDBCursor.getString(4));
            while (myDBCursor.moveToPrevious()) {
                adapter.addItem(myDBCursor.getString(1), myDBCursor.getString(2), myDBCursor.getString(3), myDBCursor.getString(4));
            }
            listView.setAdapter(adapter);
        }
    }
}
