package com.example.aarushi.myproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    private EditText etUserName, etPassword;
    private TextView tvForgotPassword, tvFacebbokLogin, tvGoogleLogin, tvSignup;

    private Button btLogin;

    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        etUserName = (EditText) findViewById(R.id.input_username);
        etPassword = (EditText) findViewById(R.id.input_password);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        btLogin = (Button) findViewById(R.id.btLogin);
        tvFacebbokLogin = (TextView) findViewById(R.id.tvFacebbokLogin);
        tvGoogleLogin = (TextView) findViewById(R.id.tvGoogleLogin);
        tvSignup = (TextView) findViewById(R.id.tvSignup);

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


        tvSignup.setOnClickListener(this);
        btLogin.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        tvFacebbokLogin.setOnClickListener(this);
        tvGoogleLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btLogin:

                login();

                break;
            case R.id.tvSignup:
                Intent signup = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(signup);
        }


    }

    private void login() {

        final String username = etUserName.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();

        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        if (username.equalsIgnoreCase(""))
            Toast.makeText(this, "Please Enter username", Toast.LENGTH_SHORT).show();
        else if (!StringUtils.isValidEmails(username) && !StringUtils.isValidPhoneNumber(username))
            Toast.makeText(this, "Please Enter Valid User name & password ", Toast.LENGTH_SHORT).show();
        else if (password.equalsIgnoreCase(""))
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        else {

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_LOGIN, new Response.Listener<String>() {

                public void onResponse(String response) {
                    Log.d(TAG, "Login Response: " + response.toString());
                    hideDialog();

                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");

                        // Check for error node in json
                        if (!error) {
                            // user successfully logged in
                            // Create login session
                            session.setLogin(true);

                            // Now store the user in SQLite
                            String uid = jObj.getString("uid");

                            JSONObject user = jObj.getJSONObject("user");
                            String name = user.getString("name");
                            String email = user.getString("email");
                            String created_at = user
                                    .getString("created_at");

                            // Inserting row in users table
                            db.addUser(name, email, uid, created_at);

                            // Launch main activity
                            Intent intent = new Intent(LoginActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Error in login. Get the error message
                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            }, new Response.ErrorListener() {


                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Login Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", username);
                    params.put("password", password);

                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
