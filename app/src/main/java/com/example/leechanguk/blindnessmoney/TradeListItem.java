package com.example.leechanguk.blindnessmoney;

import android.widget.CheckBox;

/**
 * Created by Leechanguk on 2016-11-21.
 */
public class TradeListItem {
    private String target;
    private String time;
    private String price;
    private String content;
    private CheckBox check;

    public void setTarget(String target){
        this.target = target;
    }

    public void setTime(String time){
        this.time = time;
    }

    public void setPrice(String price){
        this.price = price;
    }

    public void setContent(String content) {this.content = content;}

    public void setCheck(CheckBox check){
        this.check = check;
    }

    public String getTarget(){
        return this.target;
    }

    public String getTime(){
        return this.time;
    }

    public String getPrice(){
        return this.price;
    }

    public String getContent() {
        return this.content;
    }

    public CheckBox getCheck(){
        return this.check;
    }
}
