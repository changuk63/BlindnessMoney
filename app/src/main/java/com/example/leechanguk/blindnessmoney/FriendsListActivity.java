package com.example.leechanguk.blindnessmoney;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Leechanguk on 2016-10-10.
 */
public class FriendsListActivity extends AppCompatActivity {

    private ListView listView = null;
    private ListView nonUserListView = null;
    private FriendsListAdapter adapter =null;
    public static FriendsListAdapter nonUseradapter = null;
    ScrollView mScrollView;
    String price = "";
    String place = "";
    String time = "";
    public static HashMap<String,String> contactHash;
    public static HashMap<String,String> pushContactHash;
    ArrayList<String> contactList;
    public static ArrayList<String> receiver;
    String contact = "";

    @Override
    protected void onCreate(Bundle bundleInstanceState){
        super.onCreate(bundleInstanceState);
        setContentView(R.layout.frieds_list);


        contactList = new ArrayList<String>();
        if(contactList.size() == 0)
            makeContactHash();
        listView = (ListView)findViewById(R.id.friendsList);
        nonUserListView = (ListView)findViewById(R.id.nonUserList);
        mScrollView = (ScrollView)findViewById(R.id.scrollView);
        final ImageView clearButton = (ImageView)findViewById(R.id.searchClear);
        final EditText friendSearch = (EditText)findViewById(R.id.searchFriend);
        friendSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
                clearButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = friendSearch.getText().toString();
                adapter.filter(searchText);
                nonUseradapter.filter(searchText);
            }
        });


        receiver = new ArrayList<String>();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //체크 두번했을때 리스트에 두번 올라가는거 체크해볼것
                adapter.setChecked(position);
                adapter.notifyDataSetChanged();
            }
        });
        nonUserListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //체크 두번했을때 리스트에 두번 올라가는거 체크해볼것
                nonUseradapter.setChecked(position);
                nonUseradapter.notifyDataSetChanged();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendSearch.getText().clear();
                clearButton.setVisibility(View.GONE);
           }
        });

        new FriendsListAsyncTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_friends_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.clear_button:
                String receiverProtocol = "";
                if (receiver.size()==0){
                    Toast.makeText(getApplicationContext(), "체크해 주세요.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (TabMainActivity.state == 1 && receiver.size() != 1) {  ///////count 다시보기
                    Toast.makeText(getApplicationContext(), "한명만 체크해 주세요.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                for(int i = 0; i < receiver.size() ; i++){
                    receiverProtocol += pushContactHash.get(receiver.get(i)) + "|&|";
                }
                Intent getIntent = getIntent();
                Intent intent = new Intent(getApplicationContext(), SendMsgActivity.class);
                if(getIntent.getStringExtra("HOW") == "msg"){
                    intent.putExtra("얼마나",getIntent.getStringExtra("얼마나"));
                    intent.putExtra("어디서",getIntent.getStringExtra("어디서"));
                }
//                intent.putExtra("how much",price);
//                intent.putExtra("receiver",receiverProtocol);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
//                mDBHelper.close();
                finish();
                return true;
            default:
                return false;
        }
    }

    void makeContactHash(){
        contactHash = new HashMap<String,String>();
        pushContactHash = new HashMap<String, String>();
        Uri nameContactsUri = ContactsContract.Contacts.CONTENT_URI;
        Uri numberContactsUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] nameProjection = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
        String[] numberProjection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor clsCursor = getContentResolver().query(nameContactsUri, nameProjection, null, null, null);

        while (clsCursor != null && clsCursor.moveToNext())
        {
            String id = clsCursor.getString(0);
            String name = clsCursor.getString(1);

            Cursor cursor = getContentResolver().query(numberContactsUri, numberProjection,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);

            while(cursor != null && cursor.moveToNext()){
                String parsing = cursor.getString(0);
                if(parsing.length() > 10 && parsing.charAt(3) == '-'){
                    parsing = parsing.substring(0,3)+parsing.substring(4,8)+parsing.substring(9);
                }
                parsing = "+82"+parsing.substring(1);
                contactHash.put(parsing,name);
                pushContactHash.put(name, parsing);
                contactList.add(parsing);
                contact += parsing + "|";
            }
        }
    }

    class FriendsListAsyncTask extends AsyncTask<Void, Void, Void> {
        //        String result;
        BufferedReader dataReader;
        ArrayList<NameValuePair> nameValuePairs;
        @Override
        protected void onPreExecute(){
            nameValuePairs = new ArrayList<NameValuePair>();
//            mDBHelper.deleteTable();
        }

        @Override
        protected Void doInBackground(Void... params){
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://blindmoney.esy.es/FriendsList.php");

            try {
                nameValuePairs.add(new BasicNameValuePair("contact", contact));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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
                String result;
                adapter = new FriendsListAdapter(getApplicationContext(), contactList.size());
                nonUseradapter = new FriendsListAdapter(getApplicationContext(), contactList.size());
                while((result = dataReader.readLine()) != null){
                    adapter.addItem(contactHash.get(result));
                    contactList.remove(result);
                }
                for(int i = 0 ; i < contactList.size() ; i++){
                    nonUseradapter.addItem(contactHash.get(contactList.get(i)));
                }
                adapter.dataChange(adapter.getCount());
                nonUseradapter.dataChange(nonUseradapter.getCount());
            } catch (IOException e) {
                e.printStackTrace();
            }
            listView.setAdapter(adapter);
            nonUserListView.setAdapter(nonUseradapter);

            setListViewHeightBasedOnChildren(listView);
            setListViewHeightBasedOnChildren(nonUserListView);
            mScrollView.invalidate();
            mScrollView.requestLayout();
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView){
        ListAdapter listAdapter = listView.getAdapter();
        if(listAdapter == null){
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for(int i = 0; i < listAdapter.getCount(); i++){
            View listItem = listAdapter.getView(i, null, listView);
            //listItem.measure(0,0);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}


