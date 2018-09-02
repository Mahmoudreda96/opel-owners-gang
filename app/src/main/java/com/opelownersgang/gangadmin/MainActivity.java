package com.opelownersgang.gangadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText Massage;
    Button send;
    String massage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Massage = findViewById(R.id.message);
        send = findViewById(R.id.send);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                massage = Massage.getText().toString().trim();

                if (massage.isEmpty())
                    Massage.setError("Massage Cannot be Empty");
                else {
                    Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_LONG).show();
                    sent();
                }
            }
        });
    }

    private void sent() {

        // URL To Fetch Data From The Server
        String temp = "http://opelownersgang.com/Notify/add_ann.php?msg=" + massage;
        temp = temp.replaceAll(" ", "%20");
        String LOGIN_URL = temp;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("post successful")) {

                    Toast.makeText(getApplicationContext(), "insert Successful", Toast.LENGTH_LONG).show();
                } else if (response.trim().equals("oops! Please try again!")) {
                    Toast.makeText(getApplicationContext(), "Error Send", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "No Internet Access", Toast.LENGTH_LONG).show();

            }
        })
                ;

        // Execute Requesting
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
