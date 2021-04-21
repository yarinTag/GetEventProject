package com.appsnipp.androidproject.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.appsnipp.androidproject.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class ProductModel {
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



    public void getAllProductEvent(String eventId, final GetAllLiveDataListener listener) {
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
