package com.example.leechanguk.blindnessmoney;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class FindinfoActivity extends AppCompatActivity {

    private EditText findID_EditPhone;
    private EditText findPW_EditID;
    private EditText findPW_EditPhone;

    private Button btnFindID;
    private Button btnFindPW;

    private final String findIDURL = "http://blindmoney.esy.es/searchid.php";
    private final String findPWURL = "http://blindmoney.esy.es/searchpw.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findinfo);

        findID_EditPhone = (EditText)findViewById(R.id.edit_phone);
        findPW_EditID = (EditText)findViewById(R.id.edit_id);
        findPW_EditPhone = (EditText)findViewById(R.id.edit_phone2);

        btnFindID = (Button)findViewById(R.id.btn_findID);
        btnFindPW = (Button)findViewById(R.id.btn_findPW);

        btnFindID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findID_EditPhone.getText().toString().length() == 11)
                    new FindIDAsyncTask().execute();
                else
                    Toast.makeText(getApplicationContext(), "잘못된 전화번호 입니다.", Toast.LENGTH_LONG).show();
            }
        });

        btnFindPW.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(findPW_EditPhone.getText().toString().length() == 11)
                    new FindPWAsyncTask().execute();
                else
                    Toast.makeText(getApplicationContext(), "잘못된 전화번호 입니다.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private class FindIDAsyncTask extends AsyncTask<Void, Void, Void> {
        private String phoneNo;
        private String duplicateResult;
        @Override
        protected void onPreExecute(){
            phoneNo = findID_EditPhone.getText().toString();
            phoneNo = "+82" + phoneNo.substring(1);
        }

        @Override
        public Void doInBackground(Void...params){
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(findIDURL);

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("phoneNumber", phoneNo));
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
            AlertDialog.Builder alert = new AlertDialog.Builder(FindinfoActivity.this);
            alert.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }});
            alert.setTitle("아이디 찾기 결과");
            alert.setMessage(duplicateResult);
            alert.show();
        }
    }

    private class FindPWAsyncTask extends AsyncTask<Void, Void, Void>{
        private String phoneNo;
        private String ID;

        private String duplicateResult;
        @Override
        protected void onPreExecute(){
            phoneNo = findPW_EditPhone.getText().toString();
            phoneNo = "+82" + phoneNo.substring(1);
            ID = findPW_EditID.getText().toString();
        }

        @Override
        public Void doInBackground(Void...params){
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(findPWURL);

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("searchPN", phoneNo));
                nameValuePairs.add(new BasicNameValuePair("searchID", ID));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                BufferedReader dataReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"utf-8"));
                duplicateResult = dataReader.readLine();
                duplicateResult = dataReader.readLine();

            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void params){
            AlertDialog.Builder alert = new AlertDialog.Builder(FindinfoActivity.this);
            alert.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }});
            alert.setTitle("비밀번호 찾기 결과");
            alert.setMessage(duplicateResult);
            alert.show();
        }
    }
}
