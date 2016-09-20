package com.example.leechanguk.blindnessmoney;

/**
 * Created by Leechanguk on 2016-09-20.
 */
public class FriendsData {
    private String name;
    private int imgId;

    public FriendsData(String name, int imgId){
        this.name = name;
        this.imgId = imgId;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setImgId(int imgId){
        this.imgId = imgId;
    }

    public String getName(){
        return name;
    }

    public int getImgId(){
        return imgId;
    }
}
