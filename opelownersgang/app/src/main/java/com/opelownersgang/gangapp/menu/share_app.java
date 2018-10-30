package com.opelownersgang.gangapp.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.opelownersgang.gangapp.R;

public class share_app extends AppCompatActivity {
    Button share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_app);

        share = findViewById(R.id.button_share);

        share.setOnClickListener(v -> {
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text / plain");
            String sharebody = "\"OPEL OwNeRs GANG App\"" + "https://play.google.com/store/apps/details?id=com.opelownersgang.gangapp";
            myIntent.putExtra(Intent.EXTRA_TEXT, sharebody);
            startActivity(Intent.createChooser(myIntent, "Share OPEL OwNeRs GANG App using"));
        });
    }
}
