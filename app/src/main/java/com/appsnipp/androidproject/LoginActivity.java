package com.appsnipp.androidproject;

import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appsnipp.androidproject.model.Model;



public class LoginActivity extends AppCompatActivity {


    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button signInBtn;
    private TextView resetPassword;
    private RelativeLayout relativeLayout;
    private LoginActivity loginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for changing status bar icon colors
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        setContentView(R.layout.activity_login);

        loginActivity = this;
        //If the user has already logged in to the app, doesn't ask them to reconnect
         Model.instance.UserIsConnected(new Model.isConnectedListener() {
            @Override
            public void onComplete(boolean flag) {
                if(flag) {
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });





        relativeLayout=findViewById(R.id.includeComponenet);

        signInBtn=findViewById(R.id.cirLoginButton);
        editTextEmail=findViewById(R.id.editTextEmail);
        editTextPassword=findViewById(R.id.editTextPassword);
        resetPassword = findViewById(R.id.forgot_password);


        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (email.isEmpty()) {
                    editTextEmail.setError("Email is required!");
                    editTextEmail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editTextEmail.setError("Please enter a valid email!");
                    editTextEmail.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    editTextPassword.setError("Password is required!");
                    editTextPassword.requestFocus();
                    return;
                }
                if (password.length() < 8) {
                    editTextPassword.setError("Min password length is 8 characters!");
                    editTextPassword.requestFocus();
                    return;
                }

                //That means the current logged in user and now we need to check if the user dot is email is verified
                Model.instance.LoginIn(email, password,loginActivity);

            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.GONE);
                FragmentManager fragmentManager = getSupportFragmentManager();
                ForgotPasswordFragment passwordFragment = new ForgotPasswordFragment();
                fragmentManager.beginTransaction().replace(R.id.login_container,passwordFragment).commit();
            }
        });

    }


    public void UserIsConfig(){
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        Toast.makeText(LoginActivity.this, "Login in Successfully", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    public void UserIsNotConfig(){
        Toast.makeText(this, "Check your email to verify your account!", Toast.LENGTH_SHORT).show();
        Log.d("Tag","Check your email to verify your account!");
    }

    public void UserNotMatch(){
        Toast.makeText(this,"Failed to login! Please check your credentials", Toast.LENGTH_SHORT).show();
    }

    public void onLoginClick(View View){
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

    }

}


