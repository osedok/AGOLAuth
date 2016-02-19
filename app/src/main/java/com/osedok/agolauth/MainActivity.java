package com.osedok.agolauth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;

import java.util.concurrent.CountDownLatch;

public class MainActivity extends AppCompatActivity {

    public static final int AGOL_TOKEN_REQUEST = 1;
    private String AGOLToken = "";
    private View fabView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

             if(resultCode == Activity.RESULT_OK){

                Bundle b = data.getExtras();

                switch (requestCode) {

                    case AGOL_TOKEN_REQUEST:

                        if(b.containsKey("TOKEN") && !TextUtils.isEmpty(b.getString("TOKEN"))) {
                            AGOLToken = b.getString("TOKEN");
                            Log.e("AGOL Token", AGOLToken);
                            Snackbar.make(fabView, "Access Token: "+ AGOLToken, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                        break;

                }


            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }

    }
}
