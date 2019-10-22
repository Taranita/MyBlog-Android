package com.taranita.myblog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.taranita.myblog.auth.SignUp;
import com.taranita.myblog.profile.UserProfile;
import com.taranita.myblog.storage.SharedPrefManager;
import com.taranita.myblog.volley.VolleySingleton;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private String appURL;
    private String EMAIL, PASSWORD;
    MaterialEditText mEmail, mPassword;
    Button mButton;
    TextView mCreateAccount;
    Activity mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        appURL = "http://localhost/api/login.php";
        appURL = "http://192.168.0.100/api/login.php";

        mEmail = findViewById(R.id.text_Email);
        mPassword = findViewById(R.id.text_Password);
        mButton = findViewById(R.id.btn_login);
        mCreateAccount =findViewById(R.id.lb_CreateAccount);

        mCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });

        mButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                logIn();
            }
        });
    }

    private void logIn() {
        EMAIL = Objects.requireNonNull(mEmail.getText()).toString();
        PASSWORD = Objects.requireNonNull(mPassword.getText()).toString();
        if (EMAIL.isEmpty()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            alert.setMessage("Email cannot be empty");
            alert.setCancelable(false);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }
        else if (PASSWORD.isEmpty()){
            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            alert.setMessage("Password cannot be empty");
            alert.setCancelable(false);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }
        else if (PASSWORD.length()<8){
            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            alert.setMessage("Password length must not be less than 8 characters");
            alert.setCancelable(false);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }
        else{
            StringRequest stringRequest = new StringRequest(Request.Method.POST, appURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equals("true")){
                        Intent intent = new Intent(mContext, Dashboard.class);
//                        intent.putExtra("email", EMAIL);
                        SharedPrefManager sharedPrefManager = new SharedPrefManager();
                        sharedPrefManager.saveEmail(getApplicationContext(), EMAIL);
                        Log.d(TAG, "Email saved " + EMAIL);

                        startActivity(intent);
                    }else{
                        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                        alert.setMessage(response);
                        alert.setCancelable(false);
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alert.show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    AlertDialog.Builder alert;
                    NetworkResponse response = error.networkResponse;
                    if(response != null && response.data != null){
                        switch (response.statusCode){
                            case 400:
                                alert = new AlertDialog.Builder(mContext);
                                alert.setTitle("Error");
                                alert.setMessage("The server could not understand the request due to invalid syntax.");
                                alert.setCancelable(false);
                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                alert.show();
                                break;
                            case 404:
                                alert = new AlertDialog.Builder(mContext);
                                alert.setTitle("Error");
                                alert.setMessage("The server can not find requested resource.");
                                alert.setCancelable(false);
                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                alert.show();
                                break;
                            case 403:
                                alert = new AlertDialog.Builder(mContext);
                                alert.setTitle("Error");
                                alert.setMessage("The client does not have access rights to the content.");
                                alert.setCancelable(false);
                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                alert.show();
                                break;
                        }
                    }
                    else{
                        alert = new AlertDialog.Builder(mContext);
                        alert.setTitle("Error");
                        alert.setMessage(error.toString());
                        alert.setCancelable(false);
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alert.show();
                    }

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError{
                    HashMap<String, String> params = new HashMap<>();
                    params.put("Accept", "Application/json;charset=UTF-8");

                    return params;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError{
                    HashMap<String,String> params = new HashMap<>();
                    params.put("email", EMAIL);
                    params.put("password", PASSWORD);

                    return params;
                }
            };
            VolleySingleton.getInstance().addRequestQueue(stringRequest);
        }
    }
}
