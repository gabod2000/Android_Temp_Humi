package io.terasyshub.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import io.terasyshub.constants.API;

public class RestService {
    private static RestService restInstance;
    private Context context;

    private RestService(Context context){
        if (restInstance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        this.context = context;

    }

    public synchronized static RestService getInstance(Context context){
        if (restInstance == null) {
            restInstance = new RestService(context);
        }
        return restInstance;
    }

    public void Authenticate(final String email, final String password, final AuthenticationListener authenticationListener){
        Log.e("TerasysLogTe", email+password);
        StringRequest request = new StringRequest(Request.Method.POST, API.login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TerasysLog", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    authenticationListener.onError(jsonError);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("email", email);
                params.put("pass", password);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }






    public interface AuthenticationListener {
        void onResponse(String token);
        void onError(String message);
    }


}
