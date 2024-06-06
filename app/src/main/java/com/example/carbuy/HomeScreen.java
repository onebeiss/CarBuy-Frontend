package com.example.carbuy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HomeScreen extends AppCompatActivity {
    private ProgressBar progressBar;
    private RecyclerView recyclerView1, recyclerView2;
    private TextView emptyView, profileUserName, profileEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recyclerView1 = findViewById(R.id.recicler_view1);
        recyclerView2 = findViewById(R.id.recicler_view2);

        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView2.setLayoutManager(layoutManager2);

        emptyView = findViewById(R.id.emptyView);
        profileUserName = findViewById(R.id.profile_username);
        profileEmail = findViewById(R.id.profile_email);
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);

        progressBar = findViewById(R.id.progress_bar);

        Activity activity = this;

        SharedPreferences preferences = getSharedPreferences("SESSIONS_APP_PREFS", MODE_PRIVATE);
        String sessionToken = preferences.getString("VALID_TOKEN", null);
        String url1 = Server.name + "get_ads/";
        String url2 = Server.name + "get_favourites/";
        String url3 = Server.name + "get_user/";

        JSONArray jsonArray = new JSONArray();

        jsonArray.put(sessionToken);

        JsonArrayRequestWithAuthentication request = new JsonArrayRequestWithAuthentication(
                Request.Method.GET,
                url1,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        try {

                            List<AllAdsItem> adsList = new ArrayList<>();  // Lista para almacenar elementos

                            // Itera a través del JSONArray para obtener datos de cada elemento
                            for (int i = 0; i < response.length(); i++) {
                                // Obtiene un objeto JSON que representa un elemento del catálogo
                                JSONObject ad = response.getJSONObject(i);
                                AllAdsItem data = new AllAdsItem(ad);  // Crea un objeto MyItem a partir del JSONObject
                                adsList.add(data);  // Agrega el objeto a la lista
                            }
                            Collections.shuffle(adsList);
                            AllAdsAdapter adapter1 = new AllAdsAdapter(adsList, activity);
                            recyclerView1.setAdapter(adapter1);

                            adapter1.setOnItemClickListener(new AllAdsAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    try {
                                        AllAdsItem selectedItem = adsList.get(position);
                                        String adId = selectedItem.getId();
                                        Intent intent = new Intent(activity, HomeScreen.class);
                                        intent.putExtra("AD_ID", adId);
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Log.e("TAG", "Error: " + e.getMessage());
                                    }
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activity, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ,this);

        JsonArrayRequestWithAuthentication request2 = new JsonArrayRequestWithAuthentication(
                Request.Method.GET,
                url2,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<AllFavouritesItem> favsList = new ArrayList<>();  // Lista para almacenar elementos

                            // Itera a través del JSONArray para obtener datos de cada elemento
                            for (int i = 0; i < response.length(); i++) {
                                // Obtiene un objeto JSON que representa un elemento del catálogo
                                JSONObject fav = response.getJSONObject(i);
                                AllFavouritesItem data = new AllFavouritesItem(fav);  // Crea un objeto MyItem a partir del JSONObject
                                favsList.add(data);  // Agrega el objeto a la lista
                            }

                            AllFavouritesAdapter adapter2 = new AllFavouritesAdapter(favsList, activity);
                            recyclerView2.setAdapter(adapter2);
                            adapter2.setOnItemClickListener(new AllFavouritesAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    try {
                                        AllFavouritesItem selectedItem = favsList.get(position);
                                        String favId = selectedItem.getId();
                                        Intent intent = new Intent(activity, HomeScreen.class);
                                        intent.putExtra("FAV_ID", favId);
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Log.e("TAG", "Error: " + e.getMessage());
                                    }
                                }
                            });

                            if (favsList.isEmpty()) {
                                emptyView.setVisibility(View.VISIBLE);
                                recyclerView2.setVisibility(View.GONE);
                            } else {
                                emptyView.setVisibility(View.GONE);
                                recyclerView2.setVisibility(View.VISIBLE);
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("TAG", "Error: " + error.getMessage());
                    }
                }
        ,this);

        JsonObjectRequestWithAuthentication request3 = new JsonObjectRequestWithAuthentication(
                Request.Method.GET,
                url3,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String userName = response.getString("name");
                            String email = response.getString("email");

                            profileUserName.setText(userName);
                            profileEmail.setText(email);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activity, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "Error: " + error.getMessage());
                    }
                }
        ,this);

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
        queue.add(request2);
        queue.add(request3);
/*
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.search_icon) {
                    Intent intent = new Intent(activity, SearchScreen.class);
                    startActivity(intent);
                }
                if (menuItem.getItemId() == R.id.favourites_icon) {
                    Intent intent = new Intent(activity, FavouritesScreen.class);
                    startActivity(intent);
                }
                if (menuItem.getItemId() == R.id.settings_icon) {
                    Intent intent = new Intent(activity, SettingsScreen.class);
                    startActivity(intent);
                }
            }
        });
         */
    }
}