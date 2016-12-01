package com.example.leechanguk.blindnessmoney;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Leechanguk on 2016-11-22.
 */
public class SendMsgActivity extends AppCompatActivity {
    public static ArrayList<SendMsgListViewBtnItem> receiverdata = new ArrayList<SendMsgListViewBtnItem>();
    public static int unit = 0;
    int extra=0;
    SendMsgListViewAdapter adapter;
    SendMsgListViewBtnItem item;
    Intent receiveIntent;
    String receiver;
    EditText editmsg;
    EditText price;
    ////////////////디바이드추가
    public static String devide;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_activity);

        receiveIntent = getIntent();
        if(receiveIntent.getStringExtra("HOW") == "msg"){
            price.setText(receiveIntent.getStringExtra("얼마나"));
            editmsg.setText(receiveIntent.getStringExtra("어디서"));
        }
        receiver = receiveIntent.getStringExtra("receiver");
        editmsg = (EditText)findViewById(R.id.edit_msg);

        ListView listView = (ListView)findViewById(R.id.receiverList);
        adapter = new SendMsgListViewAdapter(this, R.layout.receiver_list_item, receiverdata);
        listView.setAdapter(adapter);
////////////////////////이거 추가
        if (TabMainActivity.state == 0) {
            item = new SendMsgListViewBtnItem();
            item.setName("나");
            item.setPrice("0");
            adapter.add(item);
        }
//////////////////////////

        for(int i = 0; i < FriendsListActivity.receiver.size(); i++){
            item = new SendMsgListViewBtnItem();
            item.setName(FriendsListActivity.receiver.get(i));
            item.setPrice("0");
            adapter.add(item);
        }

        price = (EditText)findViewById(R.id.price);
        price.setInputType(InputType.TYPE_CLASS_NUMBER);
        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
                if (TabMainActivity.state == 0) {
                    if (!price.getText().toString().equals("")) {
                        item = (SendMsgListViewBtnItem) adapter.getItem(0);
                        String temp = divide();
                        int me = Integer.valueOf(temp) + extra;
                        item.setPrice(me + "");
                        adapter.notifyDataSetChanged();
                        for (int j = 1; j < adapter.getCount(); j++) {
                            item = (SendMsgListViewBtnItem) adapter.getItem(j);
                            item.setPrice(divide());
                            adapter.notifyDataSetChanged();
                        }
                    }
                } else if (TabMainActivity.state == 1) {
                    if (!price.getText().toString().equals("")) {
                        item = (SendMsgListViewBtnItem) adapter.getItem(0);
                        item.setPrice(price.getText().toString());
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ArrayList<String> spinnerList = new ArrayList<String>();
        spinnerList.add("10원");
        spinnerList.add("100원");
        spinnerList.add("500원");
        spinnerList.add("1000원");
        ArrayAdapter<String> spadpater = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerList);

        final Spinner spinner = (Spinner)findViewById(R.id.unit);
        spinner.setAdapter(spadpater);
        if (TabMainActivity.state == 1) {
            spinner.setEnabled(false);
        }
        if (TabMainActivity.state == 0) {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            unit = 10;
                            break;
                        case 1:
                            unit = 100;
                            break;
                        case 2:
                            unit = 500;
                            break;
                        case 3:
                            unit = 1000;
                            break;
                        default:
                            //error
                            break;
                    }
                    if (!price.getText().toString().equals("")) {
                        item = (SendMsgListViewBtnItem) adapter.getItem(0);
                        String temp = divide();
                        int me = Integer.valueOf(temp) + extra;
                        item.setPrice(me + "");
                        adapter.notifyDataSetChanged();
                        for (int j = 1; j < adapter.getCount(); j++) {
                            item = (SendMsgListViewBtnItem) adapter.getItem(j);
                            item.setPrice(divide());
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        item.setPrice("0");
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
    @Override
    public void onStart(){
        super.onStart();
        receiverdata = new ArrayList<SendMsgListViewBtnItem>();
    }

    private String divide() {
        double each = Integer.valueOf(price.getText().toString()) / (adapter.list.size());
        each = each/unit;
        int result =(int)Math.floor(each);
        extra = Integer.valueOf(price.getText().toString()) - result*unit*(adapter.list.size());
/////////////////////디바이드저장
        return devide = String.valueOf(result*unit);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_complete, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.add_button:
                if (editmsg.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(), "내용을 입력하세요.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (TabMainActivity.state == 0) {
                    if (!JoinBank.banksetting.getString("accountNum","Non").equals("Non")) { //////////////////////////////////////////
                        new SendNotificationAsyncTast().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "은행과 계좌를 등록하세요", Toast.LENGTH_LONG).show();
                    }
                } else if (TabMainActivity.state == 1) {
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat CurDateFormat = new SimpleDateFormat("MM/dd");
                    String strCurDate = CurDateFormat.format(date);
                    LoanListActivity.database.open();
                    SendMsgListViewBtnItem tempitem = (SendMsgListViewBtnItem) adapter.getItem(0);
                    LoanListActivity.database.add(tempitem.getName(), strCurDate, tempitem.getPrice(), editmsg.getText().toString());
                    Intent intent = new Intent(getApplicationContext(), TabMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                return true;
            default:
                return false;
        }
    }

    private class SendNotificationAsyncTast extends AsyncTask<Void, Void, Void>{
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        String message = "";
        BufferedReader dataReader;
        String result;
        String phoneNum;
        String pricemsg;

        /////////////////은행이름 수정 디바이드 수정
        @Override
        protected void onPreExecute(){
            String[] bankArr = getResources().getStringArray(R.array.bankSpinner);
            int index = JoinBank.banksetting.getInt("bank",0);
            message = editmsg.getText().toString()+ "   " + bankArr[index] + "은행 " +
                    String.valueOf(JoinBank.banksetting.getString("accountNum","null")) +"으로 " + SendMsgActivity.devide + "원 입금 바랍니다." ;
            pricemsg = SendMsgActivity.devide;
            TelephonyManager telManager = (TelephonyManager)getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
            phoneNum = telManager.getLine1Number();
        }
//////////////////////////////

        @Override
        public Void doInBackground(Void...params){
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://blindmoney.esy.es/sendmsg.php");
            //개인별로 금액이 다를 수있으니까 메시지에 나눠서 보내줘야해
            try {
                receiver += "|+|+" + message + "|+|+" + phoneNum + "|+|+" + pricemsg;
                nameValuePairs.add(new BasicNameValuePair("receiverNumber", receiver));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
                HttpResponse response = httpclient.execute(httppost);

                dataReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"utf-8"));

            } catch (ClientProtocolException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void params){
            try {
                while ((result = dataReader.readLine()) != null){
                    Log.d("result TAG :", result);
                }
            } catch (IOException e){
                e.printStackTrace();
            }
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat CurDateFormat = new SimpleDateFormat("MM/dd");
            String strCurDate = CurDateFormat.format(date);
            RepayListActivity.repayDB.open();
            for(int i = 1 ; i < adapter.getCount() ; i++){
                SendMsgListViewBtnItem item = (SendMsgListViewBtnItem) adapter.getItem(i);
                RepayListActivity.repayDB.add(item.getName(),strCurDate,item.getPrice(),editmsg.getText().toString());
                if(FriendsListActivity.nonUseradapter.contain(item.getName()) != -1){
                    String msgReceiver = FriendsListActivity.pushContactHash.get(item.getName());
                    msgReceiver = "0"+msgReceiver.substring(3);
                    sendSMS(msgReceiver,message);
                }
            }
            RepayListActivity.repayDB.close();
            Intent intent = new Intent(getApplicationContext(), TabMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    public void sendSMS(String smsNumber, String smsText){
        PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT_ACTION"),0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED_ACTION"),0);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch(getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(SendMsgActivity.this, "전송 완료", Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(SendMsgActivity.this, "전송 실패", Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(SendMsgActivity.this, "서비스 지역이 아닙니다.", Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(SendMsgActivity.this, "Radio 가 꺼져있습니다.", Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(SendMsgActivity.this, "PDU Null", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_SENT ACTION"));

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch(getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(SendMsgActivity.this, "도착 완료", Toast.LENGTH_LONG).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(SendMsgActivity.this, "도착 실패", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_DELIVERED_ACTION"));

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(smsNumber, null, smsText, sentIntent, deliveredIntent);
    }
    /**
     @Override
     protected void onStop()
     {
     super.onStop();
     try
     {
     unregisterReceiver(new BroadcastReceiver() {
     @Override
     public void onReceive(Context context, Intent intent) {
     switch(getResultCode()){
     case Activity.RESULT_OK:
     Toast.makeText(SendMsgActivity.this, "전송 완료", Toast.LENGTH_LONG).show();
     break;
     case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
     Toast.makeText(SendMsgActivity.this, "전송 실패", Toast.LENGTH_LONG).show();
     break;
     case SmsManager.RESULT_ERROR_NO_SERVICE:
     Toast.makeText(SendMsgActivity.this, "서비스 지역이 아닙니다.", Toast.LENGTH_LONG).show();
     break;
     case SmsManager.RESULT_ERROR_RADIO_OFF:
     Toast.makeText(SendMsgActivity.this, "Radio 가 꺼져있습니다.", Toast.LENGTH_LONG).show();
     break;
     case SmsManager.RESULT_ERROR_NULL_PDU:
     Toast.makeText(SendMsgActivity.this, "PDU Null", Toast.LENGTH_LONG).show();
     break;
     }
     }
     });
     unregisterReceiver(new BroadcastReceiver() {
     @Override
     public void onReceive(Context context, Intent intent) {
     switch(getResultCode()){
     case Activity.RESULT_OK:
     Toast.makeText(SendMsgActivity.this, "도착 완료", Toast.LENGTH_LONG).show();
     break;
     case Activity.RESULT_CANCELED:
     Toast.makeText(SendMsgActivity.this, "도착 실패", Toast.LENGTH_LONG).show();
     break;
     }
     }
     });
     }
     catch (Exception e)
     {
     e.printStackTrace();
     }
     }
     */
}
