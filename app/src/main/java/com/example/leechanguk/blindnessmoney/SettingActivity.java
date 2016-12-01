package com.example.leechanguk.blindnessmoney;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
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

public class SettingActivity extends PreferenceActivity {

    private static Context thisContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        thisContext = getApplicationContext();
//        thisActivity = getActivity();

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    @Override
    public void onDestroy(){
        finish();
        super.onDestroy();
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            PreferenceManager.setDefaultValues(getActivity(), R.xml.preference_activity, false);
            addPreferencesFromResource(R.xml.preference_activity);
            //final CheckBoxPreference alarm;
            final ListPreference type;
            final Preference withdraw,account;

            //alarm = (CheckBoxPreference) findPreference("useAlarm");
            type = (ListPreference) findPreference("alarmType");
            //period = (ListPreference) findPreference("alarmPeriod");
            withdraw = findPreference("withdraw");
            account=findPreference("account");

            //저장되어있던 값 써머리에 뜨게
            int index = type.findIndexOfValue(type.getValue());
            type.setSummary(index >= 0 ? type.getEntries()[index] : null);
            //index = period.findIndexOfValue(period.getValue());
            //period.setSummary(index >= 0 ? period.getEntries()[index] : null);

            PreferenceChangeListener preferencelistener = new PreferenceChangeListener();
            //alarm.setOnPreferenceChangeListener(preferencelistener);
            type.setOnPreferenceChangeListener(preferencelistener);
            //period.setOnPreferenceChangeListener(preferencelistener);
            withdraw.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                    builder.setMessage("회원탈퇴를 하시겠습니까?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //실제로 탈퇴되는지 확인해봐야함
                            new DropOutAsyncTask().execute();
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i){
                            dialogInterface.dismiss();
                        }
                    });

                    builder.show();
                    return true;
                }
            });
            account.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent i = new Intent(getActivity(),JoinBank.class);
                    startActivity(i);
                    return false;
                }
            });

        }
    }

    public static class PreferenceChangeListener implements Preference.OnPreferenceChangeListener {
        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            /*if (preference.getKey().equals("useAlarm")) {
                boolean checked = preference.getSharedPreferences()
                        .getBoolean("useAlarm", false);
                CheckBoxPreference checkPreference = (CheckBoxPreference) preference;
                checkPreference.setChecked(!checked);
            }*/
            if (preference.getKey().equals("alarmType")) {
                String stringValue = o.toString();
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                listPreference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
                listPreference.setValue(stringValue);
            }
            return false;
        }
    }

    private static class DropOutAsyncTask extends AsyncTask<Void, Void, Void>{
        private String duplicateResult;
        private String phoneNum;

        @Override
        protected void onPreExecute(){
            TelephonyManager telManager = (TelephonyManager)thisContext.getSystemService(thisContext.TELEPHONY_SERVICE);
            phoneNum = telManager.getLine1Number();
        }

        @Override
        public Void doInBackground(Void...params){
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://blindmoney.esy.es/dropout.php");

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("dropPN", phoneNum));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                BufferedReader dataReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"utf-8"));

                duplicateResult = dataReader.readLine();
//                while((duplicateResult = dataReader.readLine()) != null);

            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void params){
            if(duplicateResult.equals("success")){
                Intent intent = new Intent(thisContext, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MainActivity.editor.clear();
                MainActivity.editor.commit();
                thisContext.startActivity(intent);
            } else {
                Toast.makeText(thisContext, "잠시후 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
