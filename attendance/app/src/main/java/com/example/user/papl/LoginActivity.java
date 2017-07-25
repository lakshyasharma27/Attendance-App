package com.example.user.papl;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    Button Login;
    CheckBox showpassword;
    EditText userid,password;
    public static final String mypreference = "mypref";
    public static final String spid = "userid";
    public static final String sppass = "password";
    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        Login=(Button)findViewById(R.id.login);
        userid=(EditText)findViewById(R.id.userid);
        userid.setText("PAPL/EMP/");
        password=(EditText)findViewById(R.id.password);
        showpassword=(CheckBox)findViewById(R.id.showpassword);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(spid)) {
            Intent fp=new Intent(getApplicationContext(),DashboardActivity.class);
            startActivity(fp);
           }


        showpassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    password.setTransformationMethod(null);
                } else {
                    //not checked
                    password.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                userLogin();
            }


        });
    }
    private void userLogin(){

        final  String id=userid.getText().toString();
        final String pass=password.getText().toString();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            //  JSONObject obj = new JSONObject(response);

                            if(response.equals("success")){
                                Toast.makeText(LoginActivity.this, "LOGIN SUCCESSFULL", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(spid, id);
                                editor.putString(sppass, pass);
                                editor.commit();
                                startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
                                finish();
                            }else{
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Please check your user id and password",
                                        Toast.LENGTH_LONG
                                ).show();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                getApplicationContext(),
                                "Network Issues",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", id);
                params.put("password", pass);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }
}

