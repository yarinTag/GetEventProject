package com.appsnipp.androidproject;

import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.appsnipp.androidproject.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {

    private EditText newUserFullName;
    private EditText newUserEmail;
    private EditText newUserPhoneNumber;
    private EditText newUserPassword;
    private Button registerBtn;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        changeStatusBarColor();

        mAuth = FirebaseAuth.getInstance();

        newUserFullName=findViewById(R.id.editTextName);
        newUserEmail=findViewById(R.id.editTextEmail);
        newUserPhoneNumber=findViewById(R.id.editTextMobile);
        newUserPassword=findViewById(R.id.editTextPassword);
        registerBtn=findViewById(R.id.cirRegisterButton);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String fullName = newUserFullName.getText().toString().trim();
                final String userEmail = newUserEmail.getText().toString().trim();
                final String userMobile = newUserPhoneNumber.getText().toString().trim();
                String userPassword = newUserPassword.getText().toString().trim();

                //Validate these inputs
                if (fullName.isEmpty()){
                    newUserFullName.setError("Full name is required");
                    newUserFullName.requestFocus();
                    return;
                }
                if (userEmail.isEmpty()){
                    newUserEmail.setError("Email is required");
                    newUserEmail.requestFocus();
                    return;
                }
                //This statement check if the email have a @ / .com
                if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                    newUserEmail.setError("Please provide valid email");
                    newUserEmail.requestFocus();
                    return;
                }
                if (userMobile.isEmpty()){
                    newUserPhoneNumber.setError("Phone Number is required");
                    newUserPhoneNumber.requestFocus();
                    return;
                }
                if (userPassword.isEmpty()){
                    newUserPassword.setError("Password is required");
                    newUserPassword.requestFocus();
                    return;
                }
                //This statement check if the password less then 8 characters
                if (userPassword.length() < 8){
                    newUserPassword.setError("Min password length should be 8 characters");
                    newUserPassword.requestFocus();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            User user = new User(fullName,userEmail,userMobile);

                            FirebaseDatabase.getInstance().getReference("Users").
                                    child(FirebaseAuth.getInstance().getCurrentUser()
                                            .getUid()).setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        Log.d("TAG","User has been registered successfully");
                                        Toast.makeText(RegisterActivity.this, "User has been registered successfully", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(RegisterActivity.this, "Failed to register!! Try again!", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        }else{
                            Toast.makeText(RegisterActivity.this, "Failed to register!! Try again!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }

    public void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);

    }



}
