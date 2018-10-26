package com.usermportal.usermanagementportal.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.usermportal.usermanagementportal.R;
import com.usermportal.usermanagementportal.util.Validation;
import com.usermportal.usermanagementportal.util.Volley.VolleyCallBack;
import com.usermportal.usermanagementportal.util.Volley.VolleyStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    LinearLayout login, signin;
    Button to_register, to_go_back, loginButton, registerButton;
    EditText emailField, passwordField, fnameField, lnameField, email_Field,
            password_Field, repeatPasswordField;
    String username, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login   = (LinearLayout) findViewById(R.id.login);
        signin  = (LinearLayout) findViewById(R.id.signin);


        emailField            = (EditText) findViewById(R.id.emailField);
        passwordField         = (EditText) findViewById(R.id.passwordField);
        fnameField            = (EditText) findViewById(R.id.fnameField);
        lnameField            = (EditText) findViewById(R.id.lnameField);
        email_Field           = (EditText) findViewById(R.id.email_Field);
        password_Field        = (EditText) findViewById(R.id.password_Field);
        repeatPasswordField   = (EditText) findViewById(R.id.repeatPasswordField);


        to_register     = (Button) findViewById(R.id.to_register_view);
        to_go_back      = (Button) findViewById(R.id.to_go_back);
        loginButton     = (Button) findViewById(R.id.loginButton);
        registerButton  = (Button) findViewById(R.id.registerButton);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        /** invoke methods **/
        layoutExchange();
    }




    /** what user sees **/
    private void layoutExchange()
    {
        to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setVisibility(View.GONE);
                signin.setVisibility(View.VISIBLE);
            }
        });

        to_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin.setVisibility(View.GONE);
                login.setVisibility(View.VISIBLE);
            }
        });


    }


    //login here with volley-call-back
    private void login()
    {
        /** get pref **/
        SharedPreferences editor = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        username = editor.getString("user", "null");
        email = editor.getString("email", "null");

        if (email.equals("null"))
        {
            Toast.makeText(this, "No account found please register", Toast.LENGTH_LONG).show();
            login.setVisibility(View.GONE);
            signin.setVisibility(View.VISIBLE);
            return;
        }

        //get the data && validate
        final String email      = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        /** simple validations **/

        if (Validation.emailValidation(email) == false) {
            emailField.setError("invalid email");
            return;
        }

        if (Validation.passwordValidation(password) == false) {
            passwordField.setError("invalid password");
            return;
        }

        //form hashmap
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        //send data to volley
        String url = getString(R.string.baseUrl)+"login";

        VolleyStringRequest volley = new VolleyStringRequest();
        volley.postData(LoginActivity.this, params, url, new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {

                JSONObject oprator = null;
                try {

                    oprator = new JSONObject(result);

                    String token = oprator.getString("token");
                    if (!token.trim().equals("")){

                        SharedPreferences.Editor editor = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).edit();
                        editor.putString("email", email);
                        editor.commit();

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            });
    }


    private void signup()
    {
        //get the data && validate
        final String fname        = fnameField.getText().toString().trim();
        final String lname        = lnameField.getText().toString().trim();
        final String email        = email_Field.getText().toString().trim();
        String password     = password_Field.getText().toString().trim();
        String passwordr    = repeatPasswordField.getText().toString().trim();

        /** simple validations **/

        if (Validation.passwordValidation(fname) == false) {
            fnameField.setError("invalid name");
            return;
        }

        if (Validation.passwordValidation(lname) == false) {
            lnameField.setError("invalid name");
            return;
        }

        if (Validation.emailValidation(email) == false) {
            email_Field.setError("invalid email");
            return;
        }

        if (Validation.passwordValidation(password) == false) {
            password_Field.setError("invalid password");
            return;
        }

        if (Validation.passwordValidation(passwordr) == false) {
            repeatPasswordField.setError("invalid password");
            return;
        }


        if (!password.matches(passwordr))
        {
            Toast.makeText(this, "Password don't match", Toast.LENGTH_LONG).show();
            return;
        }

        //form hashmap
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        //send data to volley
        String url = getString(R.string.baseUrl)+"login";

        VolleyStringRequest volley = new VolleyStringRequest();
        volley.postData(LoginActivity.this, params, url, new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {

                JSONObject oprator = null;
                try {

                    oprator = new JSONObject(result);

                    String token = oprator.getString("token");
                    if (!token.trim().equals("")){
                        SharedPreferences.Editor editor = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).edit();
                        editor.putString("user", fname+" "+lname);
                        editor.putString("email", email);
                        editor.commit();

                        signin.setVisibility(View.GONE);
                        login.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}
