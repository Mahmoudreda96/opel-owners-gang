package com.opelownersgang.opelownersgang;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class display_notifications extends AppCompatActivity {

    TextView msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_notifcation);
        msg = findViewById(R.id.msg_view);

        SharedPreferences sharedpreferences2 = getSharedPreferences("data", Context.MODE_PRIVATE);
        String Name2 = sharedpreferences2.getString("Name", null); // getting String;
        msg.setText(Name2);
    }
}
