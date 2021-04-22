package com.appsnipp.androidproject.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import com.appsnipp.androidproject.LoginActivity;
import com.appsnipp.androidproject.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


@Dao
interface UserDao {
    @Query("select * from User")
    List<User> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User... user);

    @Delete
    void delete(User user);
}

@Entity(tableName = "User")
public class User implements Serializable {

    @PrimaryKey
        @NonNull
    private String userID;
    private String fullName;
    private String email;
    private String mobile;
    private String profileImage;
    private String password;
    public User() {
    }


    public User(String fullName, String email, String mobile, String password, String image) {
        this.fullName = fullName;
        this.email = email;
        this.mobile = mobile;
        this.profileImage = image;
        this.password=password;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String image) {
        this.profileImage = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
class UserModel {

    public final static UserModel instance = new UserModel();

    public void userLogOut() {
        UserFirebase.instance.userLogOut();
    }

    UserModel(){

    }

//    public String getUserId() {
//        String s = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        return s;
//    }

    public static void login(String email, String password, LoginActivity loginActivity, final Model.isConnectedListener listener) {
        UserFirebase.instance.UserLogIn(email, password, loginActivity, new Model.isConnectedListener() {
            @Override
            public void onComplete(boolean flag,User user) {
                listener.onComplete(flag,user);
            }
        });
    }

    public User getUser() {
        User s = UserFirebase.instance.getUser();
        return s;
    }





}

class UserFirebase {

    public final static UserFirebase instance = new UserFirebase();
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;
    public User user = new User();
    private FirebaseDatabase database ;
    private DatabaseReference eventRef;




    public UserFirebase() {
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public void UpdateImg(String url,String userId){
        database.getReference("Users").child(userId).child("profileImage").setValue(url);
    }

    FirebaseUser userFire;

    public void UserIsConnected(final Model.isConnectedListener listener){

        //If the user has already logged in to the app, doesn't ask them to reconnect
          userFire = mAuth.getCurrentUser();
        if (userFire != null) {
             //User is signed in
            eventRef = database.getReference("Users").child(userFire.getUid());
            eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    user = snapshot.getValue(User.class);
                    user.setUserID(userFire.getUid());
                    listener.onComplete(true,user);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }else
            listener.onComplete(false,user);

    }

    public void getAllUsers(Model.GetAllUserListener listener) {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    }



    public void addUser(final User user, Model.AddUserListener listener) {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();



//        loadingBar.setTitle("Creating New Account");
//        loadingBar.setMessage("Please wait, while we are creating your new Account. . . ");
//        loadingBar.show();
//        loadingBar.setCanceledOnTouchOutside(true);

        mAuth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
//                    final User user = new User(userList.size(),fullName,userEmail,userMobile);
                        FirebaseDatabase.getInstance().getReference("Users").
                                child(FirebaseAuth.getInstance().getCurrentUser()
                                        .getUid()).setValue(user);
//                        loadingBar.dismiss();


                    } else {
                        Log.d("TAG", "User has not been registered");
//                    loadingBar.dismiss();

                    }
            }

        });

    }

    public void updateUser(User user, Model.AddUserListener listener) {
        addUser(user,listener);
    }

    public void getUser(String id, Model.GetAllUserListener listener) {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    }

    public void deleteUser(User user, Model.DeleteListener listener) {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    }

    public User getUser() {

        return user;
    }

    public void userLogOut() {
        mAuth.signOut();
    }

    public void UserLogIn(String email, String password, final LoginActivity activity,final Model.isConnectedListener listener) {


        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    //That means the current logged in user and now we need to check if the user dot is email is verified

                    String userId = mAuth.getUid();
                    userFire = mAuth.getCurrentUser();

                    eventRef = database.getReference("Users").child(userId);
                    eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {



                            if (userFire.isEmailVerified())
                            {
                                user = snapshot.getValue(User.class);
                                user.setUserID(userFire.getUid());
                                  listener.onComplete(true,user);

                            }else{
                                userFire.sendEmailVerification();
                                listener.onComplete(false,user);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }else{
                   activity.UserNotMatch();
                }
            }
        });
    }
}


