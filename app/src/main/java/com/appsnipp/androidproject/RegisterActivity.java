package com.appsnipp.androidproject;

import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.appsnipp.androidproject.model.Model;
import com.appsnipp.androidproject.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.List;


public class RegisterActivity extends AppCompatActivity {
    MyAdapter adapter;
    private EditText newUserFullName;
    private EditText newUserEmail;
    private EditText newUserPhoneNumber;
    private EditText newUserPassword;
    private Button registerBtn;
    List<User> userList= new LinkedList<User>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        changeStatusBarColor();
        adapter= new MyAdapter();
        reloadData();
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
                final User user = new User(userList.size(),fullName,userEmail,userMobile,userPassword);

                Model.instance.addUser(user, new Model.AddUserListener() {
                    @Override
                    public void onComplete() {
                    }
                });

                reloadData();

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


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if(userList==null){
                return 0;
            }

            return userList.size();
        }


        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if(view==null ){
                view = getLayoutInflater().inflate(R.layout.activity_register, null);
            }


            return view;
        }
    }

    private void reloadData() {
        Model.instance.getAllUsers(new Model.GetAllUserListener() {
            @Override
            public void onComplete(List<User> data) {
                for(User u:data) {

                    userList.add(u);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}
