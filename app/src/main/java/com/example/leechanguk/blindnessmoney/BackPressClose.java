package com.example.leechanguk.blindnessmoney;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Leechanguk on 2016-11-30.
 */

public class BackPressClose {
    private long backkeyPressedTime = 0;
    private Activity activity;
    private Toast toast;

    public BackPressClose(Activity activity){
        this.activity = activity;
    }

    public void onBackPressed(){
        if(System.currentTimeMillis() > backkeyPressedTime + 2000) {
            backkeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if(System.currentTimeMillis() <= backkeyPressedTime + 2000){
            activity.finish();
            toast.cancel();
        }
    }

    public void showGuide(){
        toast = Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG);
        toast.show();
    }
}
