package com.example.leechanguk.blindnessmoney;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by Leechanguk on 2016-10-04.
 */
public class JoinBank extends AppCompatActivity {

    private EditText _eAccount;
    private int bank = 0;

    private Button clear;

    //public static Boolean set_BankInfo=false;//////////////////////////////////셋팅 안해놓았을 때
    public static SharedPreferences banksetting;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join2);
        editor = banksetting.edit();

        final String[] bankArr = getResources().getStringArray(R.array.bankSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, bankArr);
        Spinner spi = (Spinner) findViewById(R.id.chooseBank);
        spi.setAdapter(adapter);

        _eAccount = (EditText)findViewById(R.id.inputAccountNum);

        //등록해놓은 정보 띄우기
        _eAccount.setText(banksetting.getString("accountNum",""));
        spi.setSelection(banksetting.getInt("bank",0));

        spi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bank = Integer.valueOf(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        clear = (Button) findViewById(R.id.joinClear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(JoinBank.this, "계좌 등록 완료", Toast.LENGTH_LONG);
                toast.show();
                editor.putString("accountNum",_eAccount.getText().toString());
                editor.putInt("bank",bank);
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), TabMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


    }
}