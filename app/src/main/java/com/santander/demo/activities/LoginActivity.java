package com.santander.demo.activities;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.santander.demo.R;
import com.santander.demo.model.PresenterImpl;
import com.santander.demo.presenter.LoginPresenter;
import com.santander.demo.view.LoginView;


public class LoginActivity extends Activity implements View.OnClickListener,LoginView {

    EditText etUsuario,etPassword;
    Button btPassword,btRegister;

    LoginPresenter mLoginPresenter;
    private SessionManager session;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        etUsuario= findViewById(R.id.usuario);
        etPassword= findViewById(R.id.password);

        btPassword= findViewById(R.id.btn_login);
        btRegister= findViewById(R.id.btn_register);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);


        btPassword.setOnClickListener(this);

        // Session manager
        session = new SessionManager(getApplicationContext());


        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        ActivityCompat.requestPermissions(this, new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION },
                1);
        mLoginPresenter= new PresenterImpl(LoginActivity.this);



        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegister = new Intent(getApplicationContext(), NewRegister.class);
                startActivity(intentRegister);
            }
        });

    }
    @Override
    public void onClick(View v){
     String usuario=etUsuario.getText().toString();
     String password=etPassword.getText().toString();
     mLoginPresenter.performLogin(usuario,password);
     mProgressBar.setVisibility(View.VISIBLE);

    }


    @Override
    public void loginValidacion() {
        mProgressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), "Ingresa el usuario y el password", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginExitoso() {

        Toast.makeText(getApplicationContext(), "Usuario correcto", Toast.LENGTH_SHORT).show();
        session.setLogin(true);
        session.createLoginSession(etUsuario.getText().toString(), "");
        mProgressBar.setVisibility(View.GONE);
        Intent intentLogin = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intentLogin);
    }

    @Override
    public void loginError(String error) {
        Toast.makeText(getApplicationContext(), "Error:"+error, Toast.LENGTH_SHORT).show();
        mProgressBar.setVisibility(View.GONE);
    }
}

