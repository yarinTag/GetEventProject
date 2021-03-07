package com.appsnipp.androidproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.multidex.MultiDex;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import com.appsnipp.androidproject.model.ImageModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private BottomNavigationView bottomNavigationView;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar menuToolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    final static int Gallery_pic =1;
    private ProgressDialog loadingBar;

    private CircleImageView navProfileUserImage;
    private TextView navProfileUserName;

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    private String currentUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view_drawer);


        mAuth=FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        //Show Background with user pic
        final View navView = navigationView.inflateHeaderView(R.layout.navigation_header);
        navProfileUserImage = navView.findViewById(R.id.nav_profile_img);
        navProfileUserName = navView.findViewById(R.id.nav_user_name);

        loadingBar = new ProgressDialog(this);


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String fullName = snapshot.child("fullName").getValue().toString();
                    String image = snapshot.child("profileImage").getValue().toString();
                    navProfileUserName.setText(fullName);
                    if (!image.equals("")) {
                        Picasso.get().load(image).placeholder(R.drawable.photo).into(navProfileUserImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        navProfileUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("Tag","clicked pic drawer");

                        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent,Gallery_pic);

                if(userRef.child("profileImage").getKey().isEmpty()) {
                    userRef.child("profileImage").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            Picasso.get().load(snapshot.getValue().toString()).placeholder(R.drawable.photo).into(navProfileUserImage);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });



        menuToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(menuToolbar);
        getSupportActionBar().setTitle("home");

        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        navController = Navigation.findNavController(this, R.id.mainActivityNavHost);
        //NavigationUI.setupActionBarWithNavController(this,navController);

        navController.navigate(R.id.action_global_eventListFragment2);

        bottomNavigationView = findViewById(R.id.navigationView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                UserMenuSelector(item);
                return false;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_pic && resultCode == RESULT_OK && data != null) {
            Uri ImageUri = data.getData();

            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null && resultCode == RESULT_OK) {
            loadingBar.setTitle("Profile Image");
            loadingBar.setMessage("Please wait, while we updating your profile image...");
            loadingBar.show();
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            //loadingBar.setCanceledOnTouchOutside(true);

            Bitmap imageBitmap;
            InputStream imageStream = null;
            try {

                final Uri resultUri = result.getUri();
                imageStream = getContentResolver().openInputStream(resultUri);
                imageBitmap = BitmapFactory.decodeStream(imageStream);
                navProfileUserImage.setImageBitmap(imageBitmap);
                ImageModel.uploadImage(imageBitmap, currentUserId, new ImageModel.Listener() {
                    @Override
                    public void onSuccess(String url) {
                        userRef.child("profileImage").setValue(url);
                        loadingBar.hide();
                    }

                    @Override
                    public void onFail() {

                    }
                });


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    private void UserMenuSelector(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.nav_profile:
//                Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
//                startActivity(intent);
//                finish();
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_findFriends:
                Toast.makeText(this, "Find Friends", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                mAuth.signOut();
                SendUserToLoginPage();
                break;
        }
    }

    private void SendUserToLoginPage() {

        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}