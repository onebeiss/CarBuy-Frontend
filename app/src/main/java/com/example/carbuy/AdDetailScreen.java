package com.example.carbuy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class AdDetailScreen extends AppCompatActivity {
private ImageView imageView;
private TextView carBrand, carModel, carYear, carPrice, userName, userPhone, carDescription;
private ProgressBar progressBar;
private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_detail_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageView = findViewById(R.id.carImage);
        carBrand = findViewById(R.id.carBrand);
        carModel = findViewById(R.id.carModel);
        carYear = findViewById(R.id.carYear);
        carPrice = findViewById(R.id.carPrice);
        userName = findViewById(R.id.userName);
        userPhone = findViewById(R.id.userPhone);
        carDescription = findViewById(R.id.carDescription);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        queue = Volley.newRequestQueue(this);
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        Activity activity = this;

        String adId = getIntent().getStringExtra("AD_ID");

        if (adId == null) {
            Toast.makeText(this, "Invalid ad ID", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = Server.name + "ad/" + adId + "/";

        JsonObjectRequestWithAuthentication request = new JsonObjectRequestWithAuthentication(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (response != null) {
                            try {

                                JSONObject userData = response.getJSONObject("user");

                                // Obtener datos del carro
                                String brand = response.getString("brand");
                                String model = response.getString("model");
                                int year = response.getInt("year");
                                String price = response.getString("price");
                                String description = response.getString("description");
                                String imageUrl = response.getString("image_url");

                                // Obtener datos del usuario
                                String username = userData.getString("name");
                                String phone = userData.getString("phone");

                                // Actualizar la UI con los detalles del carro y del usuario
                                carBrand.setText(brand);
                                carModel.setText(model);
                                carYear.setText(String.valueOf(year));
                                userName.setText(username);
                                userPhone.setText(phone);
                                carDescription.setText(description);

                                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                                symbols.setGroupingSeparator('.');
                                DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
                                String formattedPrice = decimalFormat.format(Double.parseDouble(price)) + "€";

                                carPrice.setText(formattedPrice);

                                // Cargar la imagen utilizando Picasso
                                Picasso.get().load(imageUrl).into(imageView);

                                Log.d("Response", response.toString());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e("AdDetailScreen", "Response is null");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(AdDetailScreen.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "Error: " + error.getMessage());
                    }
                }
                ,this);
        queue.add(request);

        FloatingActionButton favButton = findViewById(R.id.favButton);
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFavorites();
                Toast.makeText(AdDetailScreen.this, "Added to favorites", Toast.LENGTH_SHORT).show();
            }
            });

        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.home_icon) {
                    Intent intent = new Intent(AdDetailScreen.this, HomeScreen.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    private void addToFavorites() {
        progressBar.setVisibility(View.VISIBLE);
        String adId = getIntent().getStringExtra("AD_ID");

        if (adId == null) {
            Toast.makeText(this, "Invalid ad ID", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences preferences = getSharedPreferences("SESSIONS_APP_PREFS", MODE_PRIVATE);
        String sessionToken = preferences.getString("VALID_TOKEN", null);

        if (sessionToken == null) {
            Toast.makeText(this, "Session token not found", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("addToFavorites", "Session Token: " + sessionToken);
        Log.d("addToFavorites", "Car ID: " + adId);

        String url = Server.name + "favourite_management/";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("car_id", adId);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("TAG", "Error: " + e.getMessage());
            Toast.makeText(this, "Error adding to favourites: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequestWithAuthentication request = new JsonObjectRequestWithAuthentication(
                Request.Method.PUT,
                url,
                jsonBody,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.INVISIBLE);

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.e("TAG", "Error: " + error.getMessage());
                        Toast.makeText(AdDetailScreen.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                , this);
        queue.add(request);
    }

}