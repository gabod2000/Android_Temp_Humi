package io.terasyshub.screens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.terasyshub.R;

public class Login extends AppCompatActivity {

    EditText loginEmail, loginPassword;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //Casting
        loginEmail = (EditText)findViewById(R.id.loginEmail);
        loginPassword = (EditText)findViewById(R.id.loginPass);
        loginBtn = (Button)findViewById(R.id.loginBtn);
    }

    public void loginPressed(View view) {

    }
}
