package com.opelownersgang.gangapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class display_notifications extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    String GETURL;
    ListView l;
    ArrayList<item_Dash_Board> list_Item;
    JsonArrayRequest request;
    JSONObject ob;
    item_Dash_Board object;
    MY_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_notifcation);
        MSG();

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    public void MSG() {


        // URL To Fetch Data From The Server
        GETURL = "http://opelownersgang.com/Notify/get_msg.php";
        l = findViewById(R.id.list_notification);

        // Method To Get Chat The Data From DataBase

        list_Item = new ArrayList<>();

        request = new JsonArrayRequest(Request.Method.GET, GETURL, null, response -> {

            try {

                for (int i = 0; i < response.length(); i++) {

                    ob = response.getJSONObject(i);
                    object = new item_Dash_Board();
                    object.setDesc(ob.getString("msg"));
                    object.setTime(ob.getString("msg_date"));
                    object.setId(ob.getString("ID"));
                    list_Item.add(object);
                }
                adapter = new MY_adapter(getApplicationContext(), list_Item);
                l.setAdapter(adapter);

            } catch (JSONException e) {

                Toast.makeText(getApplicationContext(), "Problem in Server", Toast.LENGTH_LONG).show();
            }
        }, error -> Toast.makeText(getApplicationContext(), "No Internet Access", Toast.LENGTH_LONG));

        // Execute Requesting
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }


    private void showSnack(boolean isConnected) {
        if (isConnected) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.display_notification), "Connected", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.GREEN);
            snackbar.show();
            MSG();
        } else {
            Snackbar snackbar2 = Snackbar
                    .make(findViewById(R.id.display_notification), "Waiting For Network", Snackbar.LENGTH_INDEFINITE);
            View sbView2 = snackbar2.getView();
            TextView textView2 = sbView2.findViewById(android.support.design.R.id.snackbar_text);
            textView2.setTextColor(Color.RED);
            snackbar2.show();
        }


    }
}