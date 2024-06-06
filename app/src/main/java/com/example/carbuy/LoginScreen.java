package com.example.carbuy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginScreen extends AppCompatActivity {

    private Context context = this;
    private RequestQueue queueForRequests;
    private Button loginButton;
    private EditText emailEditText;
    private EditText passwordEditText;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        queueForRequests = Volley.newRequestQueue(this);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.pwd_edit_text);
        loginButton = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.progress_bar);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("email", email);
            requestBody.put("password", password);
        } catch (Exception e) {
            Toast.makeText(context, "Error on JSON object: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Server.name + "sessions/",
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressBar.setVisibility(View.INVISIBLE);
                        String receivedToken;
                        try {
                            receivedToken = response.getString("sessionToken");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        SharedPreferences preferences = context.getSharedPreferences("SESSIONS_APP_PREFS", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("VALID_USERNAME", emailEditText.getText().toString());
                        editor.putString("VALID_TOKEN", receivedToken);

                        editor.apply();

                        Intent homeScreenIntent = new Intent(LoginScreen.this, HomeScreen.class);
                        startActivity(homeScreenIntent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);

                        if (error.networkResponse != null) {
                            int serverCodeError = error.networkResponse.statusCode;
                            String errorMessage = new String(error.networkResponse.data);
                            Toast.makeText(context, "Server Error: " + serverCodeError + "\nMessage: " + errorMessage, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "error: error.networkResponse is null", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );
        queueForRequests.add(request);
    }
}