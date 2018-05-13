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

public class SelectApplication extends AppCompatActivity {

    TextView tv;
    private String TAG = SelectApplication.class.getSimpleName();
    //private final String IP =  "192.168.0.141";
    private ListView lv;
    static String intent_serviceID, intent_description, intent_serviceName;
    private String password, username;
    private ListAdapter adapter;
    ArrayList<HashMap<String, String>> applicationList;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_application);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initNavigationDrawer();

        password = getIntent().getStringExtra("intent_psw");
        username = getIntent().getStringExtra("intent_username");


        applicationList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.lv);
        tv = (TextView) findViewById(R.id.selectBank);

        //Setting text from string values for both english and russian languages
       // tv.setText(getResources().getString(R.string.choose_station) + " " + route_name);

        tv.setText("Select Application");

        new GetApplications().execute();
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
                        Intent intent_fare = new Intent(SelectApplication.this, SeeYou.class);
                        startActivity(intent_fare);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.approved:
                        Intent intent_time = new Intent(SelectApplication.this, SeeYou.class);
                        startActivity(intent_time);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(SelectApplication.this);
                        builder.setMessage("Are you sure you want to exit?")
                                .setCancelable(true)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(SelectApplication.this, LogInCustomer.class);
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



    private class GetApplications extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {

                URL url = new URL("http://" +  IPContainer.IP + "/jrlu/getApplications.php"); // here is your URL path

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
                                String serviceName = c.getString("serviceName");
                                String description = c.getString("description");
                                String serviceID = c.getString("serviceID");

                                // tmp hash map for single contact
                                HashMap<String, String> record = new HashMap<>();

                                // adding each child node to HashMap key => value
                                record.put("serviceName",  serviceName);
                                record.put("description", description);
                                record.put("serviceID", serviceID);

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
            adapter = new SimpleAdapter(SelectApplication.this, applicationList,
                    R.layout.application_list, new String[]{"serviceName"},
                    new int[]{R.id.application});
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {

                    HashMap<String,String> map =(HashMap<String,String>)lv.getItemAtPosition(position);
                    intent_serviceID = map.get("serviceID");
                    intent_serviceName = map.get("serviceName");
                    map.get("description").replaceAll("\\n","\n");
                    intent_description = map.get("description");
                    //Toast.makeText(getApplicationContext(), selectedBank, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SelectApplication.this, ApplicationDescription.class);
                    intent.putExtra("intent_serviceID", intent_serviceID);
                    intent.putExtra("intent_serviceName", intent_serviceName);
                    intent.putExtra("intent_description", intent_description);
                    intent.putExtra("intent_psw", password);
                    intent.putExtra("intent_username", username);
                    startActivity(intent);
                }
            });
        }

    }
}
