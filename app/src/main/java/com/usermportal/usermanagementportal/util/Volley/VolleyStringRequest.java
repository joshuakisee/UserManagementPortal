package com.usermportal.usermanagementportal.util.Volley;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.usermportal.usermanagementportal.activites.MainActivity;

import org.json.JSONObject;

import java.util.Map;

public class VolleyStringRequest {
    private ProgressDialog mAuthProgressDialog;

    public void postData(final Context contxt, final Map<String, String> params, String url, final VolleyCallBack callback) {

        mAuthProgressDialog = new ProgressDialog(contxt);
        mAuthProgressDialog.setMessage("loading please wait...");
        mAuthProgressDialog.setCancelable(false);
        mAuthProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, ""+url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mAuthProgressDialog.hide();

                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mAuthProgressDialog.hide();
                VolleyValidation.volleyErrorResponse(contxt, error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = params;
                return parameters;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(contxt);
        requestQueue.add(request);

    }


    public void getData(final Context contxt, final Map<String, String> params, String url, String msg, final JsonVolleyCallBack callback) {

        mAuthProgressDialog = new ProgressDialog(contxt);
        mAuthProgressDialog.setMessage(""+msg);
        mAuthProgressDialog.setCancelable(false);
        mAuthProgressDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ""+url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                        mAuthProgressDialog.hide();

                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mAuthProgressDialog.hide();
                VolleyValidation.volleyErrorResponse(contxt, error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = params;
                return parameters;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(contxt);
        requestQueue.add(request);

    }

    public void deleteData(final Context contxt, String url,String msg, final JsonVolleyCallBack callback) {

        mAuthProgressDialog = new ProgressDialog(contxt);
        mAuthProgressDialog.setMessage(""+msg);
        mAuthProgressDialog.setCancelable(false);
        mAuthProgressDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, ""+url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mAuthProgressDialog.hide();

                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mAuthProgressDialog.hide();
                MainActivity.getInstace().userList();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(contxt);
        requestQueue.add(request);

    }
}
