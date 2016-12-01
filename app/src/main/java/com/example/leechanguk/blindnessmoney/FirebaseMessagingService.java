package com.example.leechanguk.blindnessmoney;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Leechanguk on 2016-11-17.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{
    private static final String TAG = " AIzaSyCQhN1gZGkqTm_aackjl-TL-2CHDold-OU";
    private HashMap<String, String> friendsHash = new HashMap<String, String>();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        sendNotification(remoteMessage.getData().get("message"),remoteMessage.getData().get("sender"),remoteMessage.getData().get("price"));
    }

    private void sendNotification(String messageBody, String sender, String price) {
        if(friendsHash.size() ==0){
            createHash();
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("notification_click", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        if (sender != null && messageBody != null && price != null ) {
//        if(알림이 push로 설정되어있을 경우){
            SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this);
            if (mPref.getString("alarmType","").equals("0")){ //************************push알람
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle("N빵")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(0, notificationBuilder.build());

                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat CurDateFormat = new SimpleDateFormat("MM/dd");
                String strCurDate = CurDateFormat.format(date);
                LoanListActivity.database.open();
                LoanListActivity.database.add(friendsHash.get(sender), strCurDate, price, messageBody);
                LoanListActivity.database.close();

            }
            else { //***************************문자를 받는경우
                TelephonyManager telManager = (TelephonyManager)getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
                String phoneNum = telManager.getLine1Number();
                phoneNum = "0" + phoneNum.substring(3);
                sendSMS(phoneNum, messageBody);
            }
        }
    }

    public void createHash(){
        friendsHash = new HashMap<String, String>();
        Uri nameContactsUri = ContactsContract.Contacts.CONTENT_URI;
        Uri numberContactsUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] nameProjection = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
        String[] numberProjection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor clsCursor = getContentResolver().query(nameContactsUri, nameProjection, null, null, null);

        while (clsCursor != null && clsCursor.moveToNext()) {
            String id = clsCursor.getString(0);
            String name = clsCursor.getString(1);

            Cursor cursor = getContentResolver().query(numberContactsUri, numberProjection,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);

            while (cursor != null && cursor.moveToNext()) {
                String parsing = cursor.getString(0);
                if (parsing.length() > 10 && parsing.charAt(3) == '-') {
                    parsing = parsing.substring(0, 3) + parsing.substring(4, 8) + parsing.substring(9);
                }
                parsing = "+82" + parsing.substring(1);
                friendsHash.put(parsing, name);
            }
        }
    }

    public void sendSMS(String smsNumber, String smsText){
        PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT_ACTION"),0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED_ACTION"),0);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        }, new IntentFilter("SMS_SENT ACTION"));

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        }, new IntentFilter("SMS_DELIVERED_ACTION"));

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(smsNumber, null, smsText, sentIntent, deliveredIntent);
    }
}
