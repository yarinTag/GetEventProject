package com.appsnipp.androidproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ForgotPasswordFragment extends Fragment {

   private EditText email;
   private Button resetPassBtn;

   private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        email= view.findViewById(R.id.email_ver);
        resetPassBtn = view.findViewById(R.id.ver_btn);

        auth = FirebaseAuth.getInstance();

        resetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _email = email.getText().toString().trim();
                if (_email.isEmpty()){
                    email.setError("Email is required");
                    email.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(_email).matches()){
                    email.setError("Please provide valid email");
                    email.requestFocus();
                    return;
                }

                auth.sendPasswordResetEmail(_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText( getContext(), "Check your email to reset your password!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                             getActivity().startActivity(intent);
                        }else{
                            Toast.makeText( getContext(), "Try again! Something wrong happened!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}