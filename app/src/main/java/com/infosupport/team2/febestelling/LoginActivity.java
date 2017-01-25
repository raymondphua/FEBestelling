package com.infosupport.team2.febestelling;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.infosupport.team2.febestelling.util.AppSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.password;


/**
 * Created by paisanrietbroek on 23/01/2017.
 */

public class LoginActivity extends Activity {

    private ProgressDialog progressDialog;
    private EditText emailField, passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_layout);

        progressDialog = new ProgressDialog(this);

        emailField = (EditText) findViewById(R.id.login_email);
        passwordField = (EditText) findViewById(R.id.login_password);

        resetFields();

        Button loginBtn = (Button) findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = String.valueOf(emailField.getText());
                String password = String.valueOf(passwordField.getText());

                if (isValidInput(email, password)) {
                    authenticateApp(createAuthUrl(email, password));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetFields();
    }

    public void goToNextPage() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public Boolean isValidInput(String email, String password) {
        if (email.matches("") || password.matches("")) {
            Toast.makeText(getApplicationContext(),
                    "Vul alle velden in.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public String createAuthUrl(String email, String password) {
        String AUTHENTICATION_URL = "http://10.0.3.2:11150/oauth/token?grant_type=password&" +
                "username=" + email + "&" +
                "password=" + password + "" +
                "&client_id=kantilever&client_secret=kantiSecret";
        return AUTHENTICATION_URL;
    }

    public void authenticateApp(String url) {
        String REQUEST_TAG = "com.infosupport.team2.authenticationReq";
        progressDialog.setMessage("Laden...");
        progressDialog.show();

        JSONObject jsonObject = new JSONObject();
        final String requestBody = jsonObject.toString();

        final StringRequest putRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject responseObject = new JSONObject(response);
                            AppSingleton.getInstance(getApplicationContext()).token = responseObject.getString("access_token");
                            goToNextPage();
                            progressDialog.hide();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("Response", response);
                            progressDialog.hide();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        try {
                            if (error.networkResponse == null || error.networkResponse.statusCode == 500) {
                                toastMessage("Het is momenteel niet mogelijk om in te loggen.");
                                progressDialog.hide();
                            } else if (error.networkResponse.statusCode == 403) {
                                toastMessage("U heeft niet de juiste rechten.");
                                progressDialog.hide();
                            } else if (error.networkResponse.statusCode == 401){
                                toastMessage("Login gegevens kloppen niet.");
                                progressDialog.hide();
                            }
                            Log.d("Error.response", error.getMessage());
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.hide();
                        }
                    }

                })
        {

            @Override
            public byte[] getBody() {
                return requestBody.getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(putRequest, REQUEST_TAG);
    }

    public void resetFields() {
        this.emailField.setText("");
        this.passwordField.setText("");
    }

    public void toastMessage(String txt) {
        Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
    }

}
