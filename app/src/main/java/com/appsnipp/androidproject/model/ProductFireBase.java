package com.appsnipp.androidproject.model;


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

    private ProductFireBase() {
        database = FirebaseDatabase.getInstance();
    }

    public  interface ProductListener{
        void onSucceed();
    }

    public interface ProductListListener{
        void onSucceed(List<Product> products);
    }

    public void NewProduct(Product p,String eventId, ProductListener listener) {
        database.getReference("EventList").child(eventId).child("ProductList").child(p.productName).setValue(p);
        listener.onSucceed();
    }

    public void GetAllProduct(String eventId, final ProductListListener listListener){
        DatabaseReference reference = database.getReference("EventList").child(eventId).child("ProductList");
        productList = new ArrayList<>();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Product product = dataSnapshot.getValue(Product.class);
                    productList.add(product);
                }
                listListener.onSucceed(productList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void RemoveProduct(String eventId, Product product, ProductListener listener){
        database.getReference("EventList").child(eventId).child("ProductList").child(product.productName).child("delete").setValue(true);
        listener.onSucceed();

    }
}


