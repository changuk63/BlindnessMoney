package com.example.leechanguk.blindnessmoney;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText enterID;
    EditText enterPW;
    String inputID;
    String inputPW;
    CheckBox autoLogin;
    CheckBox rememberPW;

    SharedPreferences setting;
    public static SharedPreferences.Editor editor;
    private BackPressClose backPressClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backPressClose = new BackPressClose(this);
        setting = getSharedPreferences("loginsetting",0);

        editor = setting.edit();

        enterID = (EditText)findViewById(R.id.enterID);
        enterPW = (EditText)findViewById(R.id.enterPW);
        autoLogin = (CheckBox)findViewById(R.id.autoLogin);
        rememberPW = (CheckBox)findViewById(R.id.rememberPW);

        //자동로그인
        if(setting.getBoolean("Auto_Login_enabled",false)) {
            inputID = setting.getString("ID","");
            inputPW =setting.getString("PW","");
            autoLogin.setChecked(true);
            new LoginAsyncTask().execute();
        }

        //remember_info = id/pw기억 정보
        if(setting.getBoolean("Remember_info",false)){
            enterID.setText(setting.getString("ID",""));
            enterPW.setText(setting.getString("PW",""));
            rememberPW.setChecked(true);
        }
        Button findButton = (Button)findViewById(R.id.find);
        findButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FindinfoActivity.class);
                startActivity(intent);
            }
        });

        Button joinButton = (Button)findViewById(R.id.join);
        joinButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), Join.class);
                startActivity(intent);
            }
        });

        Button loginButton = (Button)findViewById(R.id.login);
        loginButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                inputID = enterID.getText().toString();
                inputPW = enterPW.getText().toString();

                if(autoLogin.isChecked()) {
                    editor.putBoolean("Auto_Login_enabled", true);
                    editor.commit();
                } else { //필요없는거 같음
                    editor.remove("Auto_Login_enabled");
                    editor.clear();
                    editor.commit();
                }
                if(rememberPW.isChecked()){
                    editor.putString("ID", inputID);
                    editor.putString("PW", inputPW);
                    editor.putBoolean("Remember_info",true);
                    editor.commit();
                } else {
                    editor.remove("ID");
                    editor.remove("PW");
                    editor.remove("Remember_info");
                    editor.commit();
                }
                new LoginAsyncTask().execute();

            }
        });
    }
    @Override
    public void onBackPressed(){
        backPressClose.onBackPressed();
    }
	
    class LoginAsyncTask extends AsyncTask<Void, Void, Void> {
        String duplicateResult = "";
        @Override
        protected Void doInBackground(Void... params){
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://blindmoney.esy.es/login.php");

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("id", inputID));
                nameValuePairs.add(new BasicNameValuePair("pw", inputPW));
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
            if(duplicateResult.equals("possible Login")){
                Intent intent = new Intent(getApplicationContext(), TabMainActivity.class);
//                if(loadIntent.getBooleanExtra("notification_click",false)){
//                    intent.putExtra("notification_click",true);
//                }
                startActivity(intent);
                finish();
            } else if(duplicateResult.equals("impossible Login")) {
                Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 다시 확인하세요.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), duplicateResult + "." , Toast.LENGTH_LONG).show();      //나중에 지워
            }
        }
    }
}
