package com.appsnipp.androidproject.model;

import androidx.lifecycle.LiveData;

import com.appsnipp.androidproject.LoginActivity;

import java.util.List;

public class Model {
    public final static Model instance = new Model();
    UserFirebase userFirebase= new UserFirebase();
    EventModel eventModel= new EventModel();

    public void UpdateImg(String url,String userId){
        userFirebase.UpdateImg(url,userId);
    }


    public interface isConnectedListener{
        void onComplete(boolean flag,User user);
    }

    public User getUser(){
        return UserModel.instance.getUser();
    }

    public void LoginIn(String email, String password, LoginActivity activity, final isConnectedListener listener) {

        UserModel.login(email, password, activity, new Model.isConnectedListener() {
            @Override
            public void onComplete(boolean flag,User user) {
                listener.onComplete(flag,user);
            }
        });
    }

    public interface RefreshEventListener{
        void onComplete();
    }

    public void RefreshMyEvent(final RefreshEventListener listener) {

        eventModel.refreshMyEvents(new EventModel.GetAllEventListener() {
            @Override
            public void onComplete() {
                listener.onComplete();
            }

        });
    }
    public interface RefreshProductsListener{
        void onComplete();
    }


    //User
    public interface Listener<T>{
        void onComplete(T result);
    }
    public interface GetAllUserListener extends Listener<List<User>>{}
    public void getAllUsers(GetAllUserListener listener) {

        userFirebase.getAllUsers(listener);
    }

    public interface AddUserListener{
        void onComplete();
    }
    public void addUser(final User user, AddUserListener listener){
        userFirebase.addUser(user,listener);
    }

    public interface DeleteListener extends AddUserListener{}
    public void delete(User user, DeleteListener listener) {
        userFirebase.deleteUser(user,listener);
    }

    public void userLogOut() {
        UserModel.instance.userLogOut();
    }

    public void UserIsConnected(final isConnectedListener listener) {
         userFirebase.UserIsConnected(new Model.isConnectedListener() {
            @Override
            public void onComplete(boolean flag,User user) {
                listener.onComplete(flag,user);
            }
        });
    }


    //Event

    public interface GetAllEventListener {
        void onComplete(List<Event> data);
    }
    public void getAllEvent(final EventModel.GetAllLiveDataListener listener) {

        //add from firebase to locoldb
//        eventModel.getAllEvents(new EventModel.GetAllEventListener() {
//            @Override
//            public void onComplete(List<Event> data) {
//                listener.onComplete(data);
//            }
//        });
        eventModel.getAllEvents(new EventModel.GetAllLiveDataListener() {
            @Override
            public void onComplete(LiveData<List<Event>> data) {
                listener.onComplete(data);
            }
        });


    }

    public interface EventListener {
        void onComplete();
    }
    public void addEvent(final Event event, final EventListener listener){

        eventModel.addEvent(event, new EventListener() {
            @Override
            public void onComplete() {
                listener.onComplete();
            }
        });
    }

    public interface DeleteEventListener extends EventListener {}
    public void deleteEvent(Event event, final DeleteListener listener) {
        eventModel.instance.deleteEvent(event, new DeleteListener() {
            @Override
            public void onComplete() {
                listener.onComplete();
            }
        });
    }

    public interface GetEventListener {
        void onComplete();
    }

    public void getEventsUser(String userId,final EventModel.GetAllLiveDataListener listener) {
        eventModel.getAllEventsUser(userId,new EventModel.GetAllLiveDataListener() {
            @Override
            public void onComplete(LiveData<List<Event>> data) {
                listener.onComplete(data);
            }
        });
    }

//    public void getEvent(Event event,GetEventListener listener) {
//        EventFirebase.instance.getEvent(event.getEventID(), listener);
//        eventModel.getEvent(event,listener);
//    }
//    public interface deleteEventListener {
//    }

    public void updateEvent (Event event, final EventListener listener) {
        eventModel.updateEvent(event, new EventModel.UpdateListener() {
            @Override
            public void onComplete() {
                listener.onComplete();
            }
        });
    }


    //Product
    public interface ProductListListener{
        void onComplete(LiveData<List<Product>> products);
    }
    public void GetAllProduct(String eventId,final ProductListListener listener) {
            ProductModel.instance.getAllProductEvent(eventId, new ProductModel.GetAllLiveDataListener() {
                @Override
                public void onComplete(LiveData<List<Product>> data) {
                    listener.onComplete(data);

                }
            });
    }


    public  interface ProductListener{
        void onComplete();
    }

    public void NewProduct(Product p, String eventId,final ProductListener listener) {
        ProductModel.instance.addProduct(p, eventId, new ProductModel.AddProductListener() {
            @Override
            public void onComplete() {
                listener.onComplete();
            }
        });
    }

    public void RefreshMyProducts(String eventId,final RefreshProductsListener listener ) {

        ProductModel.instance.refreshMyProducts(eventId, new ProductModel.GetAllProductListener() {
            @Override
            public void onComplete() {
                listener.onComplete();
            }
        });
    }

    public interface DeleteProductListener {
        void onComplete();
    }
    public void deleteProduct(Product product,String eventId, final DeleteProductListener listener) {
        ProductModel.instance.deleteProduct(product, eventId, new ProductModel.deleteProductListener() {
            @Override
            public void onComplete() {
                listener.onComplete();
            }
        });
    }
}
