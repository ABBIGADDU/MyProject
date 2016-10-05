package com.example.aarushi.myproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonIOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Aarushi on 30-Sep-16.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private static final String REGISTER_URL = "http://fc.myaarushi.com/api/Customer";

    private EditText etUserName, etEmail, etPhoneNo, etPassword, etCPassword, etstate, etpincode;
    private TextView tvSignup, tvSignedIn;
    private CheckBox chQuality;

    private ProgressDialog pDialog;

    private List<MemberList> memberList;
    private Context context;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_main);
        context = RegisterActivity.this;

        etUserName = (EditText) findViewById(R.id.etUserName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPhoneNo = (EditText) findViewById(R.id.etPhoneNo);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etCPassword = (EditText) findViewById(R.id.etCPassword);
        etstate = (EditText) findViewById(R.id.etLastName);
        etpincode = (EditText) findViewById(R.id.etDisplayName);
        tvSignup = (TextView) findViewById(R.id.tvSignup);
        tvSignedIn = (TextView) findViewById(R.id.tvSignedIn);
        chQuality = (CheckBox) findViewById(R.id.chQualiy);


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        tvSignedIn.setOnClickListener(this);
        tvSignup.setOnClickListener(this);

        db = new SQLiteHandler(getApplicationContext());

        session = new SessionManager(getApplicationContext());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSignup:
                registerUser();
                break;
            case R.id.tvSignedIn:
                startActivity(new Intent(this, LoginActivity.class));

                break;
        }
    }

    /* */
    private void registerUser() {

        final String firstname, mobile, addressline1, landmark, city, State, pincode;


        firstname = etUserName.getText().toString().trim();
        mobile = etPhoneNo.getText().toString().trim();

        addressline1 = etEmail.getText().toString().trim();
        landmark = etPassword.getText().toString().trim();
        city = etCPassword.getText().toString().trim();
        State = etstate.getText().toString().trim();
        pincode = etpincode.getText().toString().trim();

        if (firstname.equalsIgnoreCase(""))
            Toast.makeText(this, "Please Enter username", Toast.LENGTH_SHORT).show();


        else {


            StringRequest strReq = new StringRequest(Request.Method.POST,
                    REGISTER_URL, new Response.Listener<String>() {


                public void onResponse(String response) {
                    try{
                       /* JsonArray jsonArray = new JsonArray(response);
                        JsonObject jsonObject= jsonArray.getAsJsonObject(0);

*/
                        Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                    catch (JsonIOException e){
                        e.printStackTrace();
                    }



                    Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    // Posting params to register url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("FirstName", firstname);

                    params.put("PrimaryMobileNumber", mobile);


                    Map<Object, String> address = new HashMap<Object, String>();
                    address.put("AddressLine1", addressline1);
                    address.put("LandMark", landmark);
                    address.put("City", city);
                    address.put("State", State);
                    address.put("PinCode", pincode);

                    params.put("Address", String.valueOf(address));

                    return params;

                }
            };

            // Adding request to request queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(strReq);
            // AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }

    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.setTitle("Checking Network");
        pDialog.setMessage("Loading..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}
