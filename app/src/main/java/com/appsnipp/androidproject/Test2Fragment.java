package com.appsnipp.androidproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import de.hdodenhof.circleimageview.CircleImageView;


import static android.app.Activity.RESULT_OK;


public class Test2Fragment extends Fragment {

    final static int Gallery_pic =1;
    private String currentUserID;

    private FirebaseAuth mAuth;
    private StorageReference userProfileRef;
    private DatabaseReference userRef;


    public Test2Fragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_test2, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        CircleImageView profileImage = view.findViewById(R.id.edit_profile_img);
        userProfileRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,Gallery_pic);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==Gallery_pic && resultCode==RESULT_OK && data!=null) {

            Uri imageUri = data.getData();

            CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1).start(getContext(),this);
        }

        if (requestCode==1){
            
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

                if (requestCode!= RESULT_OK) {
                    //Basically save the img in firebase

                    Uri resultUri = result.getUri();

                    final StorageReference filePath = userProfileRef.child(currentUserID + ".jpg");
                    filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Profile Image stored successfully ", Toast.LENGTH_SHORT).show();
                                final String downloadUrl = filePath.getDownloadUrl().toString();

                                userRef.child("Profile Images").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), "Profile Image stored", Toast.LENGTH_SHORT).show();
                                        } else {
                                            String message = task.getException().getMessage();
                                            Toast.makeText(getContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        }
                    });

                }
                else {
                    Toast.makeText(getContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                }
            }

    }

}