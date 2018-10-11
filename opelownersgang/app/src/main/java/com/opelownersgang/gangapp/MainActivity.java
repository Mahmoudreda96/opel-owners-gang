package com.opelownersgang.gangapp;
//test change


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tbruyelle.rxpermissions2.RxPermissions;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    WebView mainview;
    ProgressBar progressBar;
    String url;
    WebSettings webSettings;
    // FloatingActionButton fab;
    // private AlarmManager alarmMgr;
    // private PendingIntent pendingIntent;


    @SuppressLint({"ClickableViewAccessibility", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mainview = findViewById(R.id.webview);
        progressBar = findViewById(R.id.progressBar);
        //fab = findViewById(R.id.fab);

        url = "https://www.opelownersgang.com";

        //hide floating Button & main view
        // fab.hide();

        //display permeation
        permeation_alert();

        //check new message
        //startService(new Intent(MainActivity.this, MyService.class));

        // refresh url after .. time;
        //Intent myIntent = new Intent(MainActivity.this, MyService.class);
        // pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, 0);
        //alarmMgr = (AlarmManager) MainActivity.this.getSystemService(Context.ALARM_SERVICE);
        // Calendar calendar = Calendar.getInstance();
        // alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + AlarmManager.INTERVAL_HALF_HOUR, AlarmManager.INTERVAL_HALF_HOUR, pendingIntent);

        //improve webView performance
        mainview.setWebChromeClient(new WebChromeClient());
        webSettings = mainview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setAppCacheEnabled(true);
        webSettings.setSaveFormData(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);


        mainview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mainview.setVisibility(View.INVISIBLE);
                progressBar.getDisplay();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mainview.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (URLUtil.isNetworkUrl(url)) {
                    return false;
                } else if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    startActivity(intent);
                } else {
                    view.getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                }
                return true;
            }
        });


        //floating Button
       /* fab.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), display_notifications.class);
            startActivity(i);

        });*/
        //check internet
        checkConnection();
        mainview.loadUrl(url);

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
                .request(/*Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,*/ Manifest.permission.CALL_PHONE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE/*,
                        Manifest.permission.CAMERA*/) // ask single or multiple permission once
                .subscribe();
    }

    //check internet
    private void showSnack(boolean isConnected) {
        if (isConnected) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.main_activity), "Connected", Snackbar.LENGTH_SHORT);
            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.GREEN);
            snackbar.show();
            mainview.loadUrl(url);
            //   fab.setVisibility(View.VISIBLE);

        } else {
            Snackbar snackbar2 = Snackbar
                    .make(findViewById(R.id.main_activity), "Waiting For Network", Snackbar.LENGTH_INDEFINITE);
            View sbView2 = snackbar2.getView();
            TextView textView2 = sbView2.findViewById(android.support.design.R.id.snackbar_text);
            textView2.setTextColor(Color.RED);
            snackbar2.show();
           /* fab.hide();
            if (alarmMgr != null) {
                alarmMgr.cancel(pendingIntent);
            }*/

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    public void onDestroy() {
        super.onDestroy();
        //clear cache in kitkat
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}



