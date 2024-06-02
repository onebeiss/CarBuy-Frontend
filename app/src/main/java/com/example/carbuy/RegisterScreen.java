package com.example.carbuy;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterScreen extends AppCompatActivity {
    private EditText usernameEditText, emailEditText, passwordEditText, birthdateEditText, phoneEditText;
    private Button registerButton;
    private Context context = this;

    private RequestQueue requestQueue;
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        requestQueue = Volley.newRequestQueue(this);
        usernameEditText = findViewById(R.id.username_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.pwd_edit_text);
        birthdateEditText = findViewById(R.id.birthdate_edit_text);
        calendar = Calendar.getInstance();
        phoneEditText = findViewById(R.id.phone_edit_text);
        registerButton = findViewById(R.id.register_button);
        progressBar = findViewById(R.id.progress_bar);

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dateSelector();
            }
        };

        birthdateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDate();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void updateDate() {
        new DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void dateSelector() {
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        birthdateEditText.setText(sdf.format(calendar.getTime()));
    }

    private void registerUser() {
        Intent HomeScreenIntent = new Intent(this, HomeScreen.class);
        JSONObject requestBody = new JSONObject();
        try{
            requestBody.put("name", usernameEditText.getText().toString());
            requestBody.put("mail", emailEditText.getText().toString());
            requestBody.put("password", passwordEditText.getText().toString());
            requestBody.put("birthdate", birthdateEditText.getText().toString());
            requestBody.put("phone", phoneEditText.getText().toString());
        } catch (JSONException e){
            Toast.makeText(context, "Error on JSON body", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Server.name + "users/",
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(context, "User registered", Toast.LENGTH_SHORT).show();;
                        startActivity(HomeScreenIntent);
                        RegisterScreen.this.finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(com.android.volley.VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (error.networkResponse == null) {
                            Toast.makeText(context, "Network Response is null", Toast.LENGTH_SHORT).show();
                        } else {
                            int serverCode = error.networkResponse.statusCode;
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject data = new JSONObject(responseBody);
                                String errorMessage = data.getString("error");
                                Toast.makeText(context, "Server Error: " + serverCode + "\nMessage: " + errorMessage, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(context, "Error parsing error response: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
        );
        this.requestQueue.add(request);
    }
}