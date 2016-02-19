package com.osedok.agolauth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.fasterxml.jackson.core.JsonParseException;
import com.osedok.agolauth.adapters.FeatureServiceAdapter;
import com.osedok.agolauth.models.FeatureService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends AppCompatActivity {

    public static final int AGOL_TOKEN_REQUEST = 1;
    private Context c;
    private String AGOLToken = "";
    private String AGOLUsername = "";
    private View fabView;
    private CountDownLatch latch;
    private ArrayList<FeatureService> fsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c = this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this, ArcGISOAuth.class);
                startActivityForResult(i, AGOL_TOKEN_REQUEST);
                fabView = view;


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            Bundle b = data.getExtras();

            switch (requestCode) {

                case AGOL_TOKEN_REQUEST:

                    if (b.containsKey("TOKEN") && !TextUtils.isEmpty(b.getString("TOKEN")) && !TextUtils.isEmpty(b.getString("USERNAME"))) {


                        AGOLToken = b.getString("TOKEN");
                        AGOLUsername = b.getString("USERNAME");
                        Log.e("AGOL Token and Username", "Token: " + AGOLToken + "\nUsername: " + AGOLUsername);
                        Snackbar.make(fabView, "Access Token: " + AGOLToken, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                        TextView tv = (TextView) findViewById(R.id.infoText);
                        tv.setVisibility(View.GONE);
                        setTitle(getString(R.string.welcome) + " " + AGOLUsername + "!");
                        latch = new CountDownLatch(1);

                        try {

                            getAGOLContent(getString(R.string.agol_content_url), AGOLUsername, AGOLToken);
                            latch.await();
                            if (fsList.size() > 0) {
                                ListView lv = (ListView) findViewById(R.id.listview);
                                FeatureServiceAdapter customAdapter = new FeatureServiceAdapter(c, R.layout.list_item, fsList);
                                lv.setAdapter(customAdapter);
                                lv.setVisibility(View.VISIBLE);

                            }
                        } catch (InterruptedException e) {
                            Log.e(getClass().getSimpleName(), "", e);
                        }

                    }
                    break;

            }


        }
        if (resultCode == Activity.RESULT_CANCELED) {
            //there's no result
        }

    }

    private void getAGOLContent(String url, String username, String token) {

        String cURL = getString(R.string.agol_content_url);
        final String updatedCURL = cURL.replace("{USER_NAME}", username).replace("{VALID_TOKEN}", token);
        Log.i("AGOL CONTENT URL", updatedCURL);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    fsList = getFeatureServices(updatedCURL);
                    latch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private ArrayList<FeatureService> getFeatureServices(String urlString) throws JsonParseException,
            MalformedURLException, IOException, JSONException {

        ArrayList<FeatureService> featureServices = new ArrayList<>();


        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setChunkedStreamingMode(0);

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer r = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            r.append(inputLine);
        }
        in.close();

        String json = r.toString();
        JSONObject o = new JSONObject(json);
        JSONArray items = o.getJSONArray("items");

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = (JSONObject) items.get(i);

            FeatureService.Builder b = new FeatureService.Builder();

            if (item.getString("type").equals("Feature Service") && !TextUtils.isEmpty(item.getString("title"))) {
                b.id(item.getString("id"));
                b.name(item.getString("name"));
                b.title(item.getString("title"));
                b.description(item.getString("description"));
                b.url(item.getString("url"));
                b.thumbnail(item.getString("thumbnail"));
                b.type(item.getString("type"));

                featureServices.add(b.build());
            }

        }

        for (int i = 0; i < featureServices.size(); i++) {
            Log.i("FEATURE SERVICE", featureServices.get(i).getTitle() + "\n" + featureServices.get(i).getUrl());
        }


        return featureServices;

    }


}


