package com.example.leechanguk.blindnessmoney;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button join = (Button)findViewById(R.id.join);
        join.setOnClickListener(new View.OnclickListner(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(),Paint.Join.class);
                startActivity(intent);
            }
        });
    }
}
