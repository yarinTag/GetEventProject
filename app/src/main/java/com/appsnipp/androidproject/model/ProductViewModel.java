package com.appsnipp.androidproject.model;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ProductViewModel extends ViewModel {
    private LiveData<List<Product>> liveData;
    private LiveData<List<Product>> liveDataUser;
    public void getData(String eventId,final ProductModel.GetAllLiveDataListener listener) {

        if(liveData==null) {
            Model.instance.GetAllProduct(eventId, new Model.ProductListListener() {
                @Override
                public void onComplete(LiveData<List<Product>> products) {
                    liveData = products;
                    listener.onComplete(liveData);
                }
            });

        }else {
            listener.onComplete(liveData);
        }

    }



    public interface RefreshEventListener{
        void onComplete();
    }

    public void refresh(String eventId,final RefreshEventListener listener) {

        Model.instance.RefreshMyProducts(eventId, new Model.RefreshProductsListener() {
            @Override
            public void onComplete() {
                listener.onComplete();
            }
        });
    }

}
