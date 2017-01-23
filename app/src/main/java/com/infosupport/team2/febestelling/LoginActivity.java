package com.infosupport.team2.febestelling;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by paisanrietbroek on 23/01/2017.
 */

public class LoginActivity extends Activity {

    private EditText emailField, passwordField;
    private String serverEmail, serverPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        emailField = (EditText) findViewById(R.id.login_email);
        passwordField = (EditText) findViewById(R.id.login_password);
        Button loginBtn = (Button) findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = String.valueOf(emailField.getText());
                String password = String.valueOf(passwordField.getText());

                boolean isCorrect = validateIsCorrect(email, password);

                if (isCorrect) {
                    Toast.makeText(v.getContext(), "Inloggen is gelukt.", Toast.LENGTH_SHORT);
                    goToNextPage(email, password, v);
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                    alertDialogBuilder.setTitle("Inloggen mislukt");
                    alertDialogBuilder.setPositiveButton("Opnieuw", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialogBuilder.show();
                }
            }
        });
    }

    public Boolean validateIsCorrect(String inputEmail, String inputPassword) {

        // TODO: get data from server

        if (!inputEmail.matches("") && !inputPassword.matches("")) {
            return (serverEmail.equals(inputEmail)) &&
                    (serverPassword.equals(inputPassword));
        } else {
            System.out.println("empty fields!");
            return false;
        }
    }

    public void goToNextPage(String email, String password, View view) {
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        startActivity(intent);
    }

    public void setServerData(String serverEmail, String serverPassword) {
        this.serverEmail = serverEmail;
        this.serverPassword = serverPassword;
    }

}
