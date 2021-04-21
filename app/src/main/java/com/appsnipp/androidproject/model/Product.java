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

class ProductFireBase {
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
class ProductModel {
    public final static ProductModel instance = new ProductModel();
    public List<Product> data = new ArrayList<>();
    public Product product;
    Long lastUpdate;
    SharedPreferences sp;

    ProductModel(){

    }
    public interface GetAllProductListener{
        void onComplete();
    }

    public interface GetAllLiveDataListener{
        void onComplete(LiveData<List<Product>> data);
    }



    public void getAllProductEvent(String eventId, final ProductModel.GetAllLiveDataListener listener) {
        LiveData<List<Product>> liveData = (LiveData<List<Product>>) AppLocalDb.db.productDao().getAll(eventId);
        refreshMyProducts(eventId,null);
        listener.onComplete(liveData);
    }



    public void refreshMyProducts(String eventId,final GetAllProductListener listener) {
        sp = MyApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
        lastUpdate= sp.getLong("lastupdated",0);
        ProductFireBase.instance.GetAllProduct(eventId, lastUpdate, new ProductFireBase.ProductListListener() {
            @Override
            public void onComplete(List<Product> products) {


                final List<Product> data =products;
                class MyAsyncTask extends AsyncTask {
                    @Override
                    protected Object doInBackground(Object[] objects) {
//                        AppLocalDb.db.eventDao().insertAll((Event) data);
//                        data.forEach( e ->AppLocalDb.db.eventDao().insertAll( (e));
                        for(Product e : data) {
                            AppLocalDb.db.productDao().insertAll((e));
                            if(e.getLastUpdate()>lastUpdate){
                                lastUpdate=e.getLastUpdate();
                            }
                        }
                        SharedPreferences.Editor editor= sp.edit();
                        editor.putLong("lastupdated",lastUpdate);
                        editor.commit();


                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);

                        if(listener!=null) {
                            listener.onComplete();
                        }
                    }
                }

                MyAsyncTask task = new MyAsyncTask();
                task.execute();
            }
        });

    }


    public interface AddProductListener{
        void onComplete();
    }
    public void addProduct(final Product product,String eventId, final AddProductListener listener){
        ProductFireBase.instance.NewProduct(product,eventId, new ProductFireBase.ProductListener() {
            @Override
            public void onComplete() {

                class MyAsyncTask extends AsyncTask {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        AppLocalDb.db.productDao().insertAll(product);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        if(listener!= null){
                            listener.onComplete();
                        }
                    }
                };
                MyAsyncTask task= new MyAsyncTask();
                task.execute();
            }
        });
    }


    public interface deleteProductListener{
        void onComplete();
    }
    public void deleteProduct(final Product product,String eventId ,final deleteProductListener listener){
        ProductFireBase.instance.DeleteProduct(eventId, product, new ProductFireBase.ProductListener() {

            @Override
            public void onComplete() {
                class MyAsyncTask extends AsyncTask {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        AppLocalDb.db.productDao().delete(product);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        if(listener!= null){
                            listener.onComplete();
                        }
                    }
                };
                MyAsyncTask task= new MyAsyncTask();
                task.execute();
            }
        });

    }



}





















