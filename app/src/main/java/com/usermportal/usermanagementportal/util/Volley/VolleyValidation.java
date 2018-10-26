package com.usermportal.usermanagementportal.util.Volley;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class VolleyValidation {
    public static void volleyErrorResponse(Context mContext, VolleyError error)
    {
        NetworkResponse response = error.networkResponse;

        if (response != null && response.data != null) {

            String json = "";
            JSONObject obj;

            switch (response.statusCode) {

                case 400:

                    json = new String(response.data);//string
                        Toast.makeText(mContext, ""+json, Toast.LENGTH_SHORT).show();
                    break;
                case 204:

                    Toast.makeText(mContext, "record deleted", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(mContext, "system error occurred! please try again", Toast.LENGTH_LONG).show();
                    break;

            }
        } else {
            volleyErrors(mContext, error);
        }
    }


    //volley validations
    public static void volleyErrors(Context mContext, VolleyError error)
    {

        String message = null;
        if (error instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
        } else if (error instanceof AuthFailureError) {
            message = "Cannot connect to Server...AuthFailureError!";
        } else if (error instanceof ParseError) {
            message = "Parsing error! Please try again after some time!!";
        } else if (error instanceof NoConnectionError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
        }else {message = "unknown error!";}

        Toast.makeText(mContext, "error "+message, Toast.LENGTH_LONG).show();

    }
}
