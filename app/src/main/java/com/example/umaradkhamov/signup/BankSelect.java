package com.example.umaradkhamov.signup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class BankSelect extends AppCompatActivity {

    TextView tv;
    private String TAG = BankSelect.class.getSimpleName();
    private ListView lv;
    static String intent_bankID, intent_bankName;
    private String username, password;
    private ListAdapter adapter;
    ArrayList<HashMap<String, String>> applicationList;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_select);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initNavigationDrawer();

        password = getIntent().getStringExtra("intent_psw");
        username = getIntent().getStringExtra("intent_username");




        applicationList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.lv_bank);
        //tv = (TextView) findViewById(R.id.selectBankID);

        //Setting text from string values for both english and russian languages
        // tv.setText(getResources().getString(R.string.choose_station) + " " + route_name);

        //tv.setText("Select Bank");

        new BankSelect.GetBanks().execute();
    }


    public void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id){
                    case R.id.home:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.pending:
                        Intent intent = new Intent(BankSelect.this, AppointmentList.class);
                        intent.putExtra("intent_username", username);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.approved:
                        Intent intent_time = new Intent(BankSelect.this, SeeYou.class);
                        startActivity(intent_time);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(BankSelect.this);
                        builder.setMessage("Are you sure you want to exit?")
                                .setCancelable(true)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(BankSelect.this, LogInCustomer.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                }
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);
        TextView tv_email = (TextView)header.findViewById(R.id.tv_email);
        tv_email.setText("Rupini Chandran");
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }


    private class GetBanks extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {

                URL url = new URL("http://" +  IPContainer.IP + "/jrlu/getBanks.php"); // here is your URL path

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    // return sb.toString();
                    String answer = sb.toString();

                    Log.e(TAG, "Response from url: " + answer);

                    if (answer != null) {
                        try {
                            JSONObject jsonObj = new JSONObject(answer);

                            // Getting JSON Array node
                            JSONArray contacts = jsonObj.getJSONArray("manageRecord");

                            // looping through All Contacts
                            for (int i = 0; i < contacts.length(); i++) {
                                JSONObject c = contacts.getJSONObject(i);
                                String bankID = c.getString("bankID");
                                String bankName = c.getString("bankName");

                                // tmp hash map for single contact
                                HashMap<String, String> record = new HashMap<>();

                                // adding each child node to HashMap key => value
                                record.put("bankID",  bankID);
                                record.put("bankName", bankName);

                                // adding contact to contact list
                                applicationList.add(record);
                            }
                        } catch (final JSONException e) {
                            Log.e(TAG, "Json parsing error: " + e.getMessage());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),
                                            "Json parsing error: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            });

                        }

                    } else {
                        Log.e(TAG, "Couldn't get json from server.");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Couldn't get json from server. Check LogCat for possible errors!",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } else {
                    // return new String("false : "+responseCode);
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "Cannot connect to php file",
                        Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            adapter = new SimpleAdapter(BankSelect.this, applicationList,
                    R.layout.bank_list, new String[]{"bankName"},
                    new int[]{R.id.bank});
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {

                    HashMap<String,String> map =(HashMap<String,String>)lv.getItemAtPosition(position);
                    intent_bankID = map.get("bankID");
                    intent_bankName = map.get("bankName");
                    Intent intent = new Intent(BankSelect.this, SelectApplication.class);
                    intent.putExtra("intent_bankID", intent_bankID);
                    intent.putExtra("intent_bankName", intent_bankName);
                    intent.putExtra("intent_psw", password);
                    intent.putExtra("intent_username", username);
                    startActivity(intent);
                }
            });
        }

    }
}

