package com.usermportal.usermanagementportal.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.usermportal.usermanagementportal.R;
import com.usermportal.usermanagementportal.util.Model.Model;
import com.usermportal.usermanagementportal.util.Validation;
import com.usermportal.usermanagementportal.util.Volley.JsonVolleyCallBack;
import com.usermportal.usermanagementportal.util.Volley.VolleyCallBack;
import com.usermportal.usermanagementportal.util.Volley.VolleyStringRequest;
import com.usermportal.usermanagementportal.util.adapter.UserListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.usermportal.usermanagementportal.activites.LoginActivity.MyPREFERENCES;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Model> models;
    private UserListAdapter mAdapter;
    RecyclerView users_recycler;
    TextView username, email;
    EditText usernameedit, emailEdit;
    LinearLayout editedrecord;
    CardView editrecord;
    Button edit, save, cancel;

    static MainActivity instance;
    public static MainActivity getInstace(){
        if(instance == null){
            instance = new MainActivity ();
        }
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Management Portal");

        instance = this;

        users_recycler = (RecyclerView) findViewById(R.id.users_recycler);
        username        = (TextView) findViewById(R.id.username);
        email           = (TextView) findViewById(R.id.email);
        usernameedit    = (EditText) findViewById(R.id.usernameedit);
        emailEdit       = (EditText) findViewById(R.id.emailEdit);
        editedrecord    = (LinearLayout) findViewById(R.id.editedrecord);
        editrecord      = (CardView) findViewById(R.id.editrecord);
        edit            = (Button) findViewById(R.id.editButton);
        save            = (Button) findViewById(R.id.saveButton);
        cancel          = (Button) findViewById(R.id.cancelButton);


        users_recycler.setHasFixedSize(true);

        models = new ArrayList<>();
        mAdapter = new UserListAdapter(MainActivity.this, models);

        LinearLayoutManager mLayoutManager
                = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);

        users_recycler.setLayoutManager(mLayoutManager);
        users_recycler.setItemAnimator(new DefaultItemAnimator());
        users_recycler.setAdapter(mAdapter);


        /** get pref **/
        SharedPreferences editor = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String usernamepref = editor.getString("user", "null");
        final String emailpref = editor.getString("email", "null");

        username.setText("Welcome "+usernamepref);
        email.setText(emailpref);

        /** control screen views **/
        editedrecord.setVisibility(View.VISIBLE);
        editrecord.setVisibility(View.GONE);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editedrecord.setVisibility(View.GONE);
                editrecord.setVisibility(View.VISIBLE);
                usernameedit.setHint(usernamepref);
                emailEdit.setHint(emailpref);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editedrecord.setVisibility(View.VISIBLE);
                editrecord.setVisibility(View.GONE);
                usernameedit.setText("");
                emailEdit.setText("");
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the data && validate
                String email = emailEdit.getText().toString().trim();
                String name  = usernameedit.getText().toString().trim();

                /** simple validations **/
                if (Validation.passwordValidation(name) == false) {
                    usernameedit.setError("invalid name");
                    return;
                }

                if (Validation.emailValidation(email) == false) {
                    emailEdit.setError("invalid email");
                    return;
                }

                update(name, email);

            }
        });



        userList();

    }

    //make network request
    public void userList()
    {
        //form hashmap
        Map<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(1));

        //send data to volley
        String url = getString(R.string.baseUrl)+"users";
        String msg = "Refreshing list please wait...";

        VolleyStringRequest volley = new VolleyStringRequest();
        volley.getData(MainActivity.this, params, url, msg, new JsonVolleyCallBack() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray jsonArray = result.getJSONArray("data");
                    Model data;
                    models.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        data = new Model();
                        try {
                            JSONObject object = (JSONObject) jsonArray.get(i);

                            data.setId(object.getString("id"));
                            data.setFirst_name(object.getString("first_name"));
                            data.setLast_name(object.getString("last_name"));
                            data.setAvatar(object.getString("avatar"));

                            models.add(data);

                        } catch (JSONException e) {
                            // Log.e(TAG, "the error: " + e.getMessage());

                        }
                    }

                    if (jsonArray.length() < 1)
                    {
                        Toast.makeText(MainActivity.this, "no data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    public void delete(String id)
    {

        //send data to volley
        String url = getString(R.string.baseUrl)+"api/users/"+id;
        String msg = "Deleting please wait...";

        Log.d("badurl", url);
        VolleyStringRequest volley = new VolleyStringRequest();
        volley.deleteData(MainActivity.this, url, msg, new JsonVolleyCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                Toast.makeText(MainActivity.this, ""+result, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void update(String uname, String editemail)
    {
        //send data to volley
        String url = getString(R.string.baseUrl)+"users/2";

        //form hashmap
        Map<String, String> params = new HashMap<>();
        params.put("name", uname);
        params.put("job", editemail);


        VolleyStringRequest volley = new VolleyStringRequest();
        volley.postData(MainActivity.this, params, url, new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {

                JSONObject oprator = null;
                try {

                    oprator = new JSONObject(result);

                    String name = oprator.getString("name");
                    String job = oprator.getString("job");

                    if (!name.trim().equals("")){

                        SharedPreferences.Editor editor = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).edit();
                        editor.putString("user", name);
                        editor.putString("email", job);
                        editor.commit();

                        username.setText("Welcome "+name);
                        email.setText(job);

                        editedrecord.setVisibility(View.VISIBLE);
                        editrecord.setVisibility(View.GONE);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

}
