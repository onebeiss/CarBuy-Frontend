package com.example.carbuy;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class JsonArrayRequestWithAuthentication extends JsonArrayRequest {
    private Context context;

    public JsonArrayRequestWithAuthentication(int method,
                                              String url,
                                              @Nullable JSONArray jsonArrayRequest,
                                              Response.Listener<JSONArray> listener,
                                              @Nullable Response.ErrorListener errorListener,
                                              Context context
    ) {
        super(method, url, jsonArrayRequest, listener, errorListener);
        this.context = context;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        SharedPreferences preferences = context.getSharedPreferences("SESSIONS_APP_PREFS", Context.MODE_PRIVATE);
        String sessionToken = preferences.getString("VALID_TOKEN", null);
        if (sessionToken == null) {
            throw new AuthFailureError();
        }
        HashMap<String, String> myHeaders = new HashMap<>();
        myHeaders.put("sessionToken", sessionToken);
        return myHeaders;
    }
}
