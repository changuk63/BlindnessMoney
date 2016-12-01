package com.example.leechanguk.blindnessmoney;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Leechanguk on 2016-11-17.
 */
public class ShowMsgActivity extends Activity {
    private String time ="";
    private String place ="";
    private String price = "";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
//        setContentView(R.layout.activity_msg);

        Intent intent = getIntent();
        time = intent.getStringExtra("언제");
        place = intent.getStringExtra("어디서");
        price = intent.getStringExtra("얼마나");

        final AlertDialog.Builder alert = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                Intent intent = new Intent(ShowMsgActivity.this, FriendsListActivity.class);        //로딩화면으로 바꿔야함
                //로딩말고 바로 탭메인에 내역추가 탭메인 보여주기
                intent.putExtra("언제",time);
                intent.putExtra("어디서",place);
                intent.putExtra("얼마나",price);
                intent.putExtra("HOW","msg");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        alert.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alert.setCancelable(false);


        alert.setMessage(time + "\n" + place + "\n" + price + "\n등록하시겠습니까?");
        alert.show();
    }
}
