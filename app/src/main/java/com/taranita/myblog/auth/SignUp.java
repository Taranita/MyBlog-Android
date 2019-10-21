package com.taranita.myblog.auth;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.taranita.myblog.R;
import com.taranita.myblog.volley.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    private String appURL;
    private String NAME, EMAIL, PASSWORD, CONFIRM_PASSWORD;
    MaterialEditText mName, mEmail, mPassword, mConfirmPassword;
    CheckBox mTerms;
    Activity mContext = this;
    Button mSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
//        appURL = "http://localhost/api/login.php";
        appURL = "http://192.168.0.100/api/signUp.php";
        mName = findViewById(R.id.text_Name);
        mEmail = findViewById(R.id.text_Email);
        mPassword = findViewById(R.id.text_Password);
        mConfirmPassword = findViewById(R.id.text_ConfirmPassword);
        mTerms = findViewById(R.id.cbTerms);

        mSignUp = findViewById(R.id.btn_signUp);
        mSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                signUp();
            }
        });
    }

    private void signUp(){
        NAME = mName.getText().toString();
        EMAIL = mEmail.getText().toString();
        PASSWORD = mPassword.getText().toString();
        CONFIRM_PASSWORD = mConfirmPassword.getText().toString();
        if (NAME.isEmpty()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            alert.setMessage("Name cannot be empty");
            alert.setCancelable(false);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }
        else if (NAME.length()<4){
            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            alert.setMessage("Name length must not be less than 4 characters");
            alert.setCancelable(false);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }
        else if (EMAIL.isEmpty()) {
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
        else if(!PASSWORD.equals(CONFIRM_PASSWORD)){
            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            alert.setMessage("Password and confirm password do not match");
            alert.setCancelable(false);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }
        else if(!mTerms.isChecked()){
            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            alert.setMessage("You must accept the terms and conditions");
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
            StringRequest stringRequest = new StringRequest(Request.Method.POST, appURL, new Response.Listener<String>(){
                @Override
                public void onResponse(String response){
                    if(response.equals("true")){
                        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                        alert.setTitle("Success");
                        alert.setMessage("You have successfully created an account\nYou can login now");
                        alert.setCancelable(false);
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alert.show();
                    }else{
                        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                        alert.setTitle("Error");
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
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error){
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
                        alert.setMessage("There is an error, please try again");
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
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("Accept", "Application/json;charset=UTF-8");
                    return super.getHeaders();
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("name", NAME);
                    params.put("email", EMAIL);
                    params.put("password", PASSWORD);

                    return params;
                }
            };
            VolleySingleton.getInstance().addRequestQueue(stringRequest);
        }
    }
}
