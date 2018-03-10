package com.example.mattchan.cs122b;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            String msg = bundle.getString("timeout");

            if(msg != null && !"".equals(msg)) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
            bundle.clear();
        }
        queue = Volley.newRequestQueue(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(this);
        }
    }

    @Override
    public void onBackPressed() {
        // disable back button
        //super.onBackPressed();
        return;
    }


    public void loginRequest(View view) {
        final Map<String, String> params = new HashMap<>();

        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();

        if (!validate(email, password)) {
            return;
        }
        params.put("email", email);
        params.put("password", password);

        String url = "http://10.0.2.2:9999/CS122B/MobileLogin";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject JSON = new JSONObject(response);
                            String msg = JSON.getString("message");
                            if(JSON.getString("status").equals("success"))
                                onSuccess();
                            else
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                        catch(Exception ex)
                        {
                            Log.e("CS122B", "Could not parse malformed JSON: \"" + response + "\"");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("security.error", error.toString());
                        System.out.println("Something went wrong!");
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        queue.add(postRequest);


        return;
    }


    public void onSuccess(){
        Intent goToIntent = new Intent(this, SearchActivity.class);
        startActivity(goToIntent);

    }

    public boolean validate(String email, String password) {
        boolean valid = true;
        String message = "";
        if (email == null || email.equals("")) {
            message = "Enter a valid email address!";
            valid = false;
        } else if (password == null || password.equals("")) {
            message = "Enter a valid password!";
            valid = false;
        }
        if (!valid)
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        return valid;
    }
}
