package com.osedok.agolauth;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

/**
 * Created by bienieka on 18/02/2016.
 */
public class ArcGISOAuth extends AppCompatActivity {


        // The redirect_uri can be either a special value of urn:ietf:wg:oauth:2.0:oob
        // or an application-specific custom URI that is handled on the device.
        private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
        // OAuth2 client_id = APPID
        //PLease set your own client ID here
        private static final String CLIENT_ID = "BHGHGYGJO";
        String username;
        String access_token;
        String refresh_token;
        private CountDownLatch latch;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setTitle("Sign in to ArcGIS Online");
            setContentView(R.layout.arcgis_oauth);
            findViewById(R.id.ProgressBar).setVisibility(View.VISIBLE);
            final WebView webView = (WebView) findViewById(R.id.webview);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    Log.e("NEW URL", "URL containing code:\n" + url);

                    final String code = url.substring(url.indexOf("?code=") + 6,
                            url.length());

                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                requestToken("https://www.arcgis.com/sharing/rest/oauth2/token?client_id="
                                        + CLIENT_ID
                                        + "&redirect_uri="
                                        + REDIRECT_URI
                                        + "&grant_type=authorization_code&code="
                                        + code);
                            } catch (Exception e) {
                                Log.e(getClass().getSimpleName(), "", e);
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_CANCELED, returnIntent);
                                finish();
                            }
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("TOKEN", access_token);
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        }
                    });
                    return true;
                }


            });


            String url = "https://www.arcgis.com/sharing/oauth2/authorize?client_id="
                    + CLIENT_ID
                    + "&response_type=code&redirect_uri="
                    + REDIRECT_URI;

            Log.e("URL", url);
            webView.loadUrl(url);

        }


        public void setLatch(CountDownLatch latch) {
            this.latch = latch;
        }

        private void requestToken(String url) throws JsonParseException,
                MalformedURLException, IOException {
            JsonFactory f = new JsonFactory();
            JsonParser json = f.createJsonParser(new URL(url));
            json.nextToken();
            while (json.nextToken() != JsonToken.END_OBJECT) {
                String name = json.getCurrentName();
                json.nextToken();
                if ("username".equals(name))
                    username = json.getText();
                else if ("access_token".equals(name))
                    access_token = json.getText();
                else if ("refresh_token".equals(name))
                    refresh_token = json.getText();
                Log.i(getClass().getSimpleName(), name + " = " + json.getText());
            }
        }





    }


