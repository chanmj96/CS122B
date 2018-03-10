package com.example.mattchan.cs122b;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends TimerActivity {

    public int totalitems;
    public int perpage = 10;
    public int currpage = 0;

    ArrayList<Movie> movies;
    ListView listView;
    TextView textView;
    private static CustomAdapter adapter;

    Button btn, prev, next;
    EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        btn = findViewById(R.id.button);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        input = findViewById(R.id.editText);
        listView = findViewById(R.id.listView);
        textView = findViewById(R.id.searchTitle);

        movies = new ArrayList<>();
        for(int i = 0; i < perpage; ++i){
            movies.add(new Movie("None", "None", "None", "None", "None", "None"));
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(getIntent());
    }

    //ToDo: Add onStop, onRestart movielist is still displayed, make movieList not Visibility.Gone

    public void connectToTomcat(View view){
        final Map<String, String> params = new HashMap<String, String>();


        // no user is logged in, so we must connect to the server
        RequestQueue queue = Volley.newRequestQueue(this);

        final Context context = this;
        String url = "http://10.0.2.2:9999/CS122B/MobileSearch?title=";

        url += input.getText().toString();
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        movies = new ArrayList<>();

                        try {

                            JSONArray marray = new JSONArray(response);
                            totalitems = marray.length();

                            for(int i = 0; i < marray.length(); ++i){
                                JSONObject movie = marray.getJSONObject(i);

                                movies.add(new Movie(
                                        movie.getString("id"),
                                        movie.getString("title"),
                                        movie.getString("year"),
                                        movie.getString("director"),
                                        movie.getString("cast"),
                                        movie.getString("genre")));
                            }

                            loadview(0);

                            listView.setVisibility(View.VISIBLE);
                            prev.setVisibility(View.VISIBLE);
                            next.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.GONE);
                            btn.setVisibility(View.GONE);
                            input.setVisibility(View.GONE);
                            //adapter = new CustomAdapter(movies, getApplicationContext());
                            //listView.setAdapter(adapter);

                        } catch (JSONException e){
                            Log.d("Response", response);
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("security.error", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {

                return params;
            }
        };


        // Add the request to the RequestQueue.
        queue.add(postRequest);


        return ;
    }

    public void loadview(int n){
        ArrayList<Movie> page = new ArrayList<>();
        int start = n * perpage;
        for(int i = start; i < start + perpage; ++i){
            if(i < movies.size()){
                page.add(movies.get(i));
            } else {
                break;
            }
        }
        adapter = new CustomAdapter(page, getApplicationContext());
        listView.setAdapter(adapter);

    }
    public void prevpage(View view){
        if(currpage > 0) {
            loadview(currpage - 1);
            currpage -= 1;
        }
    }
    public void nextpage(View view){
        if(currpage < totalitems / perpage) {
            loadview(currpage + 1);
            currpage += 1;
        }
    }

}
