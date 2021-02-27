package com.appsnipp.androidproject.model;

import android.view.View;

import com.appsnipp.androidproject.LoginActivity;

import java.util.List;

public class Model {
    public final static Model instance = new Model();
    UserFirebase userFirebase= new UserFirebase();
    EventModel eventModel= new EventModel();
    EventFirebase eventFirebase= new EventFirebase();

    public void LoginIn(String email, String password, LoginActivity activity) {

        userFirebase.UserLogIn(email,password,activity);
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

    public boolean UserIsConnected() {
        return userFirebase.UserIsConnected();
    }


    //Event

    public interface GetAllEventListener extends Listener<List<Event>>{}
    public void getAllEvent(GetAllEventListener listener) {
        eventFirebase.getAllEvent(listener);
        //add from firebase to locoldb
        eventModel.getAllEvents(listener);
    }

    public interface AddEventListener{
        void onComplete();
    }
    public void addEvent(final Event event, AddEventListener listener){
        EventFirebase eventFirebase= new EventFirebase();

        eventFirebase.addEvent(event,listener);
        listener.onComplete();
        /*eventModel.addEvent(event,listener);*/
    }

    public interface DeleteEventListener extends AddEventListener{}
    public void delete(Event event, DeleteListener listener) {
        eventFirebase.deleteEvent(event,listener);
    }

    public interface GetEventListener {
        void onComplete();
    }
    public void getEvent(Event event,GetEventListener listener) {
        eventFirebase.getEvent(event.getEventID(), listener);
        eventModel.getEvent(event,listener);
    }
//    public interface deleteEventListener {
//    }
}
