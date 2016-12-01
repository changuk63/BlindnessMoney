package com.example.leechanguk.blindnessmoney;

import android.widget.CheckBox;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by Leechanguk on 2016-10-10.
 */
public class FriendsListData {
    public CheckBox checkBox;
    public String name;
    public String phoneNum;

    public static final Comparator<FriendsListData> CHAR_COMPARATOR = new Comparator<FriendsListData>() {
        private final Collator collator = Collator.getInstance();
        @Override
        public int compare(FriendsListData o1, FriendsListData o2) {
            return collator.compare(o1.name, o2.name);
        }
    };

    public void setName(String text){
        this.name = text;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPhoneNum(){
        return this.phoneNum;
    }

    public String getName(){
        return this.name;
    }



}
