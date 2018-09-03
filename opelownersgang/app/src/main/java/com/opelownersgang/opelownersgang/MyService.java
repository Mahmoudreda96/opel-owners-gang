package com.opelownersgang.opelownersgang;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mahmoud reda on 03/09/2018.
 */

public class MyService extends Service {
    String id, msg;
    Uri h;
    Ringtone y;
    NotificationManager mNM;
    Notification mNotify;
    Vibrator v;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        notification();
        MSG();

        return START_STICKY;
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
        mNotify = builder.build();
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        h = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        y = RingtoneManager.getRingtone(getApplicationContext(), h);
    }

    public void MSG() {

        // URL To Fetch Data From The Server

        String GETURL = "http://opelownersgang.com/Notify/get_msg.php";
        // Method To Get  The Data From DataBase
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, GETURL, null, response -> {

            try {


                JSONObject ob = response.getJSONObject(1);

                id = (ob.getString("ID"));
                msg = (ob.getString("msg"));
                SharedPreferences sharedpreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                String Name = sharedpreferences.getString("Name", null); // getting String;
                if (!id.equals(Name)) {

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
        }, error -> Toast.makeText(getApplicationContext(), "No Internet Access", Toast.LENGTH_LONG).show());
        mNM.notify(0, mNotify);
        v.vibrate(100000);
        y.play();

        // Execute Requesting
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }
}
