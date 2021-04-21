package com.appsnipp.androidproject.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import com.appsnipp.androidproject.MyApplication;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

@Dao
interface ProductDao {

    @Query("select * from Product Where isDelete == 0 And evenId=:eventId ")
    LiveData<List<Product>> getAll(String eventId);

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    void insertAll(Product... products);

    @Delete
    void delete(Product product);

//    @Query("select * from Event Where Event.userId=:userId And isDelete==0 order by eventTime And eventDate ")
//    LiveData<List<Event>> getEventsUser(String userId);
}

@Entity(tableName = "Product")
public class Product {
    @PrimaryKey
    @NonNull
    public String productName;
    public String productQuantity;
    public boolean isDelete;
    public String userName;
    private Long lastUpdate;
    public String evenId;

    public String getEvenId() {
        return evenId;
    }

    public void setEvenId(String evenId) {
        this.evenId = evenId;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Product() {

    }
    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }


    public Product( String productName, String productQuantity, boolean delete, String userName, Long lastUpdate, String eventId) {

        this.productName = productName;
        this.productQuantity = productQuantity;
        this.isDelete = delete;
        this.userName=userName;
        this.lastUpdate=lastUpdate;
        this.evenId=eventId;
    }



    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

}





















