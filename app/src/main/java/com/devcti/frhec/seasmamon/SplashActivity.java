package com.devcti.frhec.seasmamon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class SplashActivity extends AppCompatActivity{

    public static final String PREFS_NAME = "Estados";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Date dia = new Date();

        Button button = (Button) findViewById(R.id.aprove);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    state(getIntent().getStringExtra("Hospital"), getIntent().getStringExtra("Secret"),getIntent().getStringExtra("ID"),dia,true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                returnScreen();
            }
        });

        Button button1 = (Button) findViewById(R.id.deny);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    state(getIntent().getStringExtra("Hospital"),getIntent().getStringExtra("Secret"),getIntent().getStringExtra("ID"),dia,false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                returnScreen();
            }
        });

    }

    private void returnScreen(){
        Intent intent = new Intent(this, ItemListActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                returnScreen();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void state(String name, String secret, String id, Date dia, Boolean type) throws IOException {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(name,"{\"id\":\""+id+"\",\"name\":\""+name+"\",\"secret\":\""+secret+"\",\"dia\":"+dia.getTime()+",\"type\":\""+type.booleanValue()+"\"}");

        URL url = null;
        try {
            url = new URL("http://10.12.214.135:3000/saveRequest");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.connect();

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("secret", secret));
        params.add(new BasicNameValuePair("id", id));

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(getQuery(params));
        writer.flush();
        writer.close();
        os.close();

        // Commit the edits!
        editor.commit();
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}