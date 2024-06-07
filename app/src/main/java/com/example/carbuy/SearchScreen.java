package com.example.carbuy;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchScreen extends AppCompatActivity {
    private ProgressBar progressBar;
    private EditText searchView;
    private Activity activity;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        activity = this;
        searchView = findViewById(R.id.search_bar);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        searchView.requestFocus();

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Abre el teclado automáticamente
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 100);

        // Configurar la tecla Enter para buscar
        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == KeyEvent.KEYCODE_ENTER) || ((event != null) && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))) {
                    search();
                }
                return false;
            }
        });

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.home_icon) {
                    Intent intent = new Intent(activity, HomeScreen.class);
                    startActivity(intent);
                }
                if (menuItem.getItemId() == R.id.search_icon) {
                    Intent intent = new Intent(activity, SearchScreen.class);
                    startActivity(intent);
                }
                /*
                if (menuItem.getItemId() == R.id.favourites_icon) {
                    Intent intent = new Intent(activity, FavouritesScreen.class);
                    startActivity(intent);
                }
                if (menuItem.getItemId() == R.id.settings_icon) {
                    Intent intent = new Intent(activity, SettingsScreen.class);
                    startActivity(intent);
                }
                 */
                return true;
            }
        });
    }

    private void search() {
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        try {
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    Server.name + "search?q=" + searchView.getText().toString(),
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressBar.setVisibility(View.INVISIBLE);
                            try {
                                JSONArray resultArray = response.getJSONArray("cars");
                                List<SearchResultItem> resultList = new ArrayList<>();

                                for (int i = 0; i < resultArray.length(); i++) {
                                    JSONObject result = resultArray.getJSONObject(i);
                                    SearchResultItem data = new SearchResultItem(result);
                                    resultList.add(data);
                                }
                                SearchResultAdapter adapter = new SearchResultAdapter(resultList, activity);
                                recyclerView.setAdapter(adapter);

                                // Verifica si la lista de elementos itemList tiene más de un elemento
                                if (resultList.size() > 1) {
                                    // La lista tiene más de un elemento
                                    Log.d("SearchScreen", "La lista itemList contiene más de un elemento.");
                                } else {
                                    // La lista no tiene más de un elemento
                                    Log.d("SearchScreen", "La lista itemList no contiene más de un elemento.");
                                }

                                adapter.setOnItemClickListener(new SearchResultAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        try {
                                            SearchResultItem resultClicked = resultList.get(position);
                                            String resultId = resultClicked.getId();

                                            Intent intent = new Intent(activity, AdDetailScreen.class);
                                            intent.putExtra("RESULT_ID", resultId);
                                            startActivity(intent);
                                        } catch (Exception e) {
                                            Log.e("ERROR in the item click", e.getMessage());
                                        }
                                    }
                                });
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.e("TAG", "ERROR" + volleyError.getMessage());
                        }
                    }
            );

            int customTimeoutMs = 30000; // 30 segundos
            request.setRetryPolicy(new DefaultRetryPolicy(
                    customTimeoutMs,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(request);

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

            } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }

    }
}