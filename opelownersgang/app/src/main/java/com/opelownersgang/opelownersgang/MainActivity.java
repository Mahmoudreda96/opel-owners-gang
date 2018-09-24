package com.opelownersgang.opelownersgang;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    WebView mainview;
    ProgressBar progressBar;
    String url;
    Thread t;
    FloatingActionButton fab;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"SetJavaScriptEnabled", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainview = findViewById(R.id.webview);
        progressBar = findViewById(R.id.progressBar);
        fab = findViewById(R.id.fab);

        url = "https://test.opelownersgang.com";

        //hide floating Button & main view
        mainview.setVisibility(View.INVISIBLE);
        fab.hide();
        //display permeation
        permeation_alert();

        //check new message
        startService(new Intent(MainActivity.this, MyService.class));

        // refresh url after .. time;
        t = new Thread() {

            @Override
            public void run() {

                while (!isInterrupted()) {

                    try {
                        Thread.sleep(3000);  //30*60*1000 = 30 m
                        runOnUiThread(() -> startService(new Intent(MainActivity.this, MyService.class)));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        t.start();

        mainview.addJavascriptInterface(new WebAppInterface(this), "Android");
        mainview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebSettings webSettings = mainview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mainview.setWebChromeClient(new WebChromeClient());
        mainview.setWebViewClient(new MyWebViewClient());
        webSettings.setGeolocationEnabled(true);
        webSettings.setSupportMultipleWindows(true);

        //improve webView performance
        mainview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mainview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mainview.getSettings().setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setSaveFormData(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mainview.loadUrl(url);
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

        //check internet
        checkConnection();
        //floating Button
        fab.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), display_notifications.class);
            startActivity(i);

        });
    }

    @Override
    public void onBackPressed() {
        if (mainview.canGoBack()) {
            mainview.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals("https://test.opelownersgang.com")) {
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }

    public class WebAppInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /**
         * Show a toast from the web page
         */
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
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
            fab.setVisibility(View.VISIBLE);
        } else {
            Snackbar snackbar2 = Snackbar
                    .make(findViewById(R.id.main_activity), "Waiting For Network", Snackbar.LENGTH_INDEFINITE);
            View sbView2 = snackbar2.getView();
            TextView textView2 = sbView2.findViewById(android.support.design.R.id.snackbar_text);
            textView2.setTextColor(Color.RED);
            snackbar2.show();
            fab.hide();

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
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
//    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//        if (url.startsWith("tel:")) {
//            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
//            startActivity(intent);
//            view.reload();
//            return true;
//        }
//
//        view.loadUrl(url);
//        return true;
//    }

}



