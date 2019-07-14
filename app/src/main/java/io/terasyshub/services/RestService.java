package io.terasyshub.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.terasyshub.constants.API;
import io.terasyshub.controllers.AuthController;
import io.terasyshub.models.Device;
import io.terasyshub.screens.Login;
import io.terasyshub.utils.Mapper;
import io.terasyshub.utils.TokenRequest;

public class RestService {
    private static RestService restInstance;
    private AuthController authController;
    private Context context;

    private RestService(Context context){
        if (restInstance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        this.context = context;
        this.authController = AuthController.getInstance();

    }

    public synchronized static RestService getInstance(Context context){
        if (restInstance == null) {
            restInstance = new RestService(context);
        }
        return restInstance;
    }

    public void Authenticate(final String email, final String password, final AuthenticationListener authenticationListener){
        StringRequest request = new StringRequest(Request.Method.POST, API.login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                authenticationListener.onResponse(response);
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

    public void getDevices(final DevicesRESTCallback devicesRESTCallback) {
        TokenRequest tokenRequest = new TokenRequest(Request.Method.GET, API.devices, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<Device> deviceList = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int x=0; x<jsonArray.length(); x++){
                        JSONObject deviceJSON = jsonArray.getJSONObject(x);
                        deviceList.add(Mapper.JSONtoDevice(deviceJSON));
                    }

                    devicesRESTCallback.onResponse(deviceList);
                } catch (Exception e){
                    devicesRESTCallback.onError("Something went wrong");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    devicesRESTCallback.onError(jsonError);
                }

                if(error.networkResponse.statusCode == 401) {
                     authController.logout();
                     context.startActivity(new Intent(context, Login.class));
                     ((Activity)context).finish();
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(tokenRequest);
    }






    public interface AuthenticationListener {
        void onResponse(String token);
        void onError(String message);
    }

    public interface DevicesRESTCallback {
        void onResponse(List<Device> devices);
        void onError(String message);
    }


}
