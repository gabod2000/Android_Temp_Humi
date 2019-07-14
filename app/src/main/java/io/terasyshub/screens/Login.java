package io.terasyshub.screens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.terasyshub.R;
import io.terasyshub.controllers.AuthController;
import io.terasyshub.services.RestService;
import io.terasyshub.services.ValidationService;
import io.terasyshub.utils.TerasysAlert;

public class Login extends AppCompatActivity {
    private EditText loginEmail,loginPass;
    private Button loginBtn;

    private ValidationService validationService;
    private RestService restService;

    private AuthController authController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //Casting
        loginBtn = (Button)findViewById(R.id.loginBtn);
        loginEmail = (EditText)findViewById(R.id.loginEmail);
        loginPass = (EditText)findViewById(R.id.loginPass);


        //Intialize
        validationService = ValidationService.getInstance().setActivity(this);
        restService = RestService.getInstance(this);
        authController = AuthController.getInstance();
    }

    public void onLoginPressed(View view) {
        final String mail = loginEmail.getText().toString().trim();
        final String pass = loginPass.getText().toString().trim();

        if(validationService.ValidateAuthentication(mail, pass)) {
            loginBtn.setEnabled(false);
            loginBtn.setText("Loading...");
            restService.Authenticate(mail, pass, new RestService.AuthenticationListener() {
                @Override
                public void onResponse(String token) {
                    authController.setToken(token);
                    loginBtn.setEnabled(true);
                    loginBtn.setText("Login");
                    startActivity(new Intent(Login.this, Home.class));
                    finish();
                }

                @Override
                public void onError(String message) {
                    loginBtn.setEnabled(true);
                    TerasysAlert.show(message, false, Login.this);
                    loginBtn.setText("Login");
                }
            });
        }
    }
}
