package com.opelownersgang.opelownersgang;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    WebView mainview;
    ProgressBar progressBar;
    String id, msg, url;
    Uri h;
    Ringtone y;
    NotificationManager mNM;
    Notification mNotify;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainview = findViewById(R.id.webview);
        progressBar = findViewById(R.id.progressBar);
        url = "https://test.opelownersgang.com";

        permeation_alert();
        notification();
        MSG();

        mainview.setVisibility(View.VISIBLE);
        WebSettings webSettings = mainview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mainview.setWebChromeClient(new WebChromeClient());
        mainview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                mainview.setVisibility(View.VISIBLE);
                String javaScript = "javascript:(function() { var a= document.getElementsByTagName('header');a[0].hidden='true';a=document.getElementsByClassName('page_body');a[0].style.padding='0px';})()";
                mainview.loadUrl(javaScript);
            }
        });
        mainview.loadUrl(url);

        //improve webView performance
        mainview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mainview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mainview.getSettings().setAppCacheEnabled(true);
        mainview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setSaveFormData(true);
    }

    @Override
    public void onBackPressed() {
        if (mainview.canGoBack()) {
            mainview.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public void permeation_alert() {
        //permission alert to enable
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA) // ask single or multiple permission once
                .subscribe(granted -> {
                    if (granted) {
                        // All requested permissions are granted
                    } else {
                        // At least one permission is denied
                    }
                });
    }

    public void notification() {


        Intent intent1 = new Intent(this.getApplicationContext(), display_notifications.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent1, 0);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("the gang");
        builder.setContentText("touch to disable massage ");
        builder.setSmallIcon(R.drawable.logo);
        builder.setContentIntent(pIntent);
        builder.setAutoCancel(true);
        //Vibrator v;
        mNotify = builder.build();
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        h = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        y = RingtoneManager.getRingtone(getApplicationContext(), h);
        // v= (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        //v.vibrate(10000);
    }

    public void MSG() {

        // URL To Fetch Data From The Server
        String GETURL = "https://gametyapp.000webhostapp.com/signintech.php";


        // Method To Get  The Data From DataBase
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, GETURL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {


                    JSONObject ob = response.getJSONObject(1);

                    id = (ob.getString("id"));
                    msg = (ob.getString("msg"));
                    SharedPreferences sharedpreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                    String Name = sharedpreferences.getString("Name", null); // getting String;
                    if (!id.equals(Name)) {
                        mNM.notify(0, mNotify);
                        y.play();
                        //edit sharedpreferences id
                        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Name", id);
                        editor.apply();

                        //edit sharedpreferences msg
                        SharedPreferences sharedPreferences2 = getSharedPreferences("data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                        editor2.putString("Name2", id);
                        editor2.apply();
                    }
                } catch (JSONException e) {

                    Toast.makeText(getApplicationContext(), "Problem in Server", Toast.LENGTH_LONG).show();
                }
            }
        }, error -> Toast.makeText(getApplicationContext(), "No Internet Access", Toast.LENGTH_LONG).show());

        // Execute Requesting
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }
}



/*public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith("tel:")) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
            startActivity(intent);
            view.reload();
            return true;
        }

        view.loadUrl(url);
        return true;
    }*/