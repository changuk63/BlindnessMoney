package com.example.leechanguk.blindnessmoney;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Created by Leechanguk on 2016-09-20.
 */
public class FriendsList extends AppCompatActivity {

    private ArrayList<FriendsData> m_List;

    public FriendsList() {
        m_List = new ArrayList<FriendsData>();
    }

    @Override
    public

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_list);


    }
}
