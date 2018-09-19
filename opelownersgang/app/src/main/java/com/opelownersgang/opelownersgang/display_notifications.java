package com.opelownersgang.opelownersgang;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class display_notifications extends AppCompatActivity {

//    TextView msg;
//    String id, Msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_notifcation);
       // msg = findViewById(R.id.msg_view);
        MSG();
//        SharedPreferences sharedpreferences2 = getSharedPreferences("data", Context.MODE_PRIVATE);
//        String Name2 = sharedpreferences2.getString("Name", null); // getting String;
//        msg.setText(Name2);
    }

  public void MSG() {


      // URL To Fetch Data From The Server
      String GETURL = "https://gametyapp.000webhostapp.com/get.php";
      final ListView l = findViewById(R.id.list_notification);

      // Method To Get Chat The Data From DataBase

      final ArrayList<item_Dash_Board> list_Item = new ArrayList<>();

      JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, GETURL, null, response -> {

          try {

              for (int i = 0; i < response.length(); i++) {

                  JSONObject ob = response.getJSONObject(i);
                  item_Dash_Board object = new item_Dash_Board();
                  //object.setDate(ob.getString("date"));
                  object.setDesc(ob.getString("description"));
                  //object.setTime(ob.getString("time"));
                  object.setId(ob.getString("title"));
                  list_Item.add(object);

              }

              final MY_adapter adapter = new MY_adapter(getApplicationContext(), list_Item);
              l.setAdapter(adapter);

          } catch (JSONException e) {

              Toast.makeText(getApplicationContext(), "Problem in Server", Toast.LENGTH_LONG).show();
          }
      }, error -> Toast.makeText(getApplicationContext(), "No Internet Access", Toast.LENGTH_LONG).show());

      // Execute Requesting
      RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
      requestQueue.add(request);
  }
  }
//
//        // URL To Fetch Data From The Server
//
//        String GETURL = "http://opelownersgang.com/Notify/get_msg.php";
//        // Method To Get  The Data From DataBase
//        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, GETURL, null, response -> {
//            try {
//
//
//                JSONObject ob = response.getJSONObject(94);
//
////                id = (ob.getString("ID"));
////                Msg = (ob.getString("msg"));
//                msg.setText(ob.getString("msg"));
//            } catch (JSONException e) {
//
//                Toast.makeText(getApplicationContext(), "Problem in Server", Toast.LENGTH_LONG).show();
//            }
//        }, error -> Toast.makeText(getApplicationContext(), "No Internet Access", Toast.LENGTH_LONG).show());
//        // Execute Requesting
//        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//        requestQueue.add(request);
//    }

