package com.example.leechanguk.blindnessmoney;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leechanguk on 2016-09-18.
 */
public class Join extends AppCompatActivity {

    String ID;
    String passWord;
    private String phoneNum;
    private boolean _checkPW;
    private final String myServerURL = "http://blindmoney.esy.es/join.php";


    EditText inputID;
    EditText inputPW;
    EditText checkPW;
    EditText inputEmail;
    //Button text;
    int stateDuplicate = 0;
    boolean isDuplicate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);

        inputID = (EditText) findViewById(R.id.inputID);
        inputPW = (EditText) findViewById(R.id.inputPW);
        checkPW = (EditText) findViewById(R.id.inputCheckPW);
        inputEmail = (EditText) findViewById(R.id.inputEMail);
        final TextView pwWarning = (TextView)findViewById(R.id.join_pw_warning);
        final TextView idWarning = (TextView)findViewById(R.id.join_id_warning);

        TelephonyManager telManager = (TelephonyManager)getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        phoneNum = telManager.getLine1Number();
        if(phoneNum != null && phoneNum.charAt(0) == '0'){
            phoneNum = "+82" + phoneNum.substring(1);
        }

        //아이디입력을 변경하면 isDuplicate = false;
        inputID.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isDuplicate = false;
                stateDuplicate = 0;
                if(s.length() < 8){
                    idWarning.setVisibility(View.VISIBLE);
                } else {
                    idWarning.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                isDuplicate = false;
            }
        });

        inputPW.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() < 8){
                    pwWarning.setVisibility(View.VISIBLE);
                } else {
                    pwWarning.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputPW.removeTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() < 8){
                    pwWarning.setVisibility(View.VISIBLE);
                } else {
                    pwWarning.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Button joinButton = (Button) findViewById(R.id.joinRequire);
        joinButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = inputID.getText().toString();
                passWord = inputPW.getText().toString();
                if (stateDuplicate == 0){ //중복확인 버튼 눌렀나 안눌렀나 (아이디관련 다 여기서 처리)
                    Toast toast = Toast.makeText(Join.this, "아이디 중복확인을 해주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if (passWord.equals("")){
                    Toast toast = Toast.makeText(Join.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if (passWord.equals(checkPW.getText().toString()))
                    _checkPW = true;
                else
                    _checkPW = false;

                if (!_checkPW) {
                    Toast toast = Toast.makeText(Join.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    new JoinAsyncTask().execute();
                }
            }
        });

        Button duplicatedID = (Button)findViewById(R.id.duplicateView); //중복확인 버튼
        duplicatedID.setOnClickListener(new View.OnClickListener(){ //아이디 관련 다 여기서 처리
            @Override
            public void onClick(View v){
                ID = inputID.getText().toString().trim();
                if (ID.equals("")){
                    Toast toast = Toast.makeText(Join.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                new DuplicateAsyncTask().execute();

            }
        });
    }

    class DuplicateAsyncTask extends AsyncTask<Void, Void, Void>{
        String duplicateResult = "";
        @Override
        protected Void doInBackground(Void... params){
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://blindmoney.esy.es/duplicate.php");
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("id", ID));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                BufferedReader dataReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"utf-8"));
                duplicateResult = dataReader.readLine();

            } catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void params){
            stateDuplicate = 1;
            if(duplicateResult.equals("impossible join")) {
                isDuplicate = true;
                Toast toast = Toast.makeText(Join.this, "아이디가 존재합니다.", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                isDuplicate = false;
                Toast toast = Toast.makeText(Join.this, "사용 가능한 아이디 입니다.", Toast.LENGTH_SHORT);
                toast.show();
            }

        }
    }

    class JoinAsyncTask extends AsyncTask<Void, Void, Void>{

        private String duplicateResult;

        @Override
        protected Void doInBackground(Void... params){
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://blindmoney.esy.es/join.php");

            try {
                String result = ID + "|" + passWord + "|" + phoneNum;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("result",result));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//                Toast.makeText(this, phoneNum, Toast.LENGTH_SHORT).show();
                HttpResponse response = httpclient.execute(httppost);

                BufferedReader dataReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"utf-8"));
                duplicateResult = dataReader.readLine();

            } catch (ClientProtocolException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void params){
            /************************ 수정했음 ********************************/
            if(duplicateResult.equals("insert success ")){
                Intent intent = new Intent(getApplicationContext(), JoinBank.class);
                startActivity(intent);
                finish();
            } else if(duplicateResult.equals("exist device ")){
                Toast.makeText(getApplicationContext(), "이미 가입된 전화번호입니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), duplicateResult, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
