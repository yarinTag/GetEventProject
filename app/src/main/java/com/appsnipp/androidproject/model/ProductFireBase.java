package com.appsnipp.androidproject.model;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductFireBase {
    public static final ProductFireBase instance = new ProductFireBase();
    private FirebaseDatabase database;
    private List<Product> productList;
    public Long lastUpdate;
    public SharedPreferences sp;

    private ProductFireBase() {
        database = FirebaseDatabase.getInstance();
    }

    public  interface ProductListener{
        void onComplete();
    }

    public interface ProductListListener{
        void onComplete(List<Product> products);
    }

    public void NewProduct(Product p,String eventId, ProductListener listener) {
        database.getReference("EventList").child(eventId).child("ProductList").child(p.productName).setValue(p);
        listener.onComplete();
    }

    public void GetAllProduct(String eventId,Long lastUpdate, final ProductListListener listListener){
        DatabaseReference reference = database.getReference("EventList").child(eventId).child("ProductList");
        productList = new ArrayList<>();

        reference.orderByChild("lastUpdate").startAt(lastUpdate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Product product = dataSnapshot.getValue(Product.class);
                    productList.add(product);
                }
                listListener.onComplete(productList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void DeleteProduct(String eventId, Product product, ProductListener listener){
        database.getReference("EventList").child(eventId).child("ProductList").child(product.productName).child("isDelete").setValue(true);
        listener.onComplete();

    }

}
