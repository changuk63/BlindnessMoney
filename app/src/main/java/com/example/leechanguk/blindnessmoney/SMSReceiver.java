package com.example.leechanguk.blindnessmoney;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.StringTokenizer;

/**
 * Created by Leechanguk on 2016-11-16.
 */
public class SMSReceiver extends BroadcastReceiver {
    public String str;

    String SMSDAY = "";
    String SMSTime = "";
    String SMSPrice = "";
    String SMSPlace = "";

    Context thisContext;
    @Override
    public void onReceive(Context context, Intent intent){
        Bundle bundle = intent.getExtras();
        thisContext = context;
        str = "";
        if (bundle != null){
            Object[] pdus = (Object[])bundle.get("pdus");

            SmsMessage[] msgs = new SmsMessage[pdus.length];
            for(int i = 0 ; i < msgs.length ; i++){
                msgs[0]= SmsMessage.createFromPdu((byte[]) pdus[0]);
                str += msgs[0].getOriginatingAddress() +"에게 문자왔음, " +
                        msgs[0].getMessageBody().toString()+"\n";

            }

            Toast.makeText(context, str, Toast.LENGTH_LONG).show(); //이 부분에서 파싱해서 푸쉬메시지
            if(parsingSMS(msgs)){
                Intent intents = new Intent(context, ShowMsgActivity.class);
                intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intents.putExtra("언제",SMSTime);
                intents.putExtra("어디서",SMSPlace);
                intents.putExtra("얼마나",SMSPrice);
                context.startActivity(intents);
            }
        }
    }

    boolean parsingSMS(SmsMessage[] msgs){
        String sender = msgs[0].getOriginatingAddress();
        switch(sender){
            case "15881688":
                if(KBparsing(msgs[0].getMessageBody().toString())){
                    return true;
                }
        }
        return false;
    }

    boolean KBparsing(String msgsBody){
        StringTokenizer tokens = new StringTokenizer(msgsBody, "\n");
        String in = tokens.nextToken();
        in = tokens.nextToken();
        in = tokens.nextToken();

        SMSTime = tokens.nextToken();
        SMSPrice = tokens.nextToken();
        SMSPlace = tokens.nextToken();

        tokens = new StringTokenizer(SMSPlace, " ");
        SMSPlace = tokens.nextToken();

        if(SMSPlace == "" || SMSPrice == "" || SMSTime == ""){
            return false;
        } return true;
    }
}
