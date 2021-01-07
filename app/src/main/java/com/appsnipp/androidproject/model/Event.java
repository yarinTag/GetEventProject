package com.appsnipp.androidproject.model;


import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;


@Dao
interface EventDao {

    @Query("select * from Event")
    List<Event> getAll();

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    void insertAll(Event...events);

    @Delete
    void delete(Event event);
    //one Event
    @Query("SELECT * FROM Event\n"+
            "WHERE Event.eventID=:eventId")
    Event getEvent(String eventId);
}



@Entity(tableName = "Event")
public class Event implements Serializable {
    @PrimaryKey
    @NonNull
    public String eventID;
    public String EventName;
    public String EventDetails;
    public String eventTime;
    public String EventImg;
    public int position;


    public Event() {
    }

    public Event(String eventName, String eventDate, String eventDescription) {
        this.EventName=eventName;
        this.eventTime=eventDate;
        this.EventDetails=eventDescription;

    }

    public String getEventId() {
        return eventID;
    }

    public void setEventId(@NonNull String eventId) {
        eventId = eventID;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getEventDetails() {
        return EventDetails;
    }

    public void setEventDetails(String eventDetails) {
        EventDetails = eventDetails;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventImg() {
        return EventImg;
    }

    public void setEventImg(String eventImg) {
        EventImg = eventImg;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


}


class EventModel {

    public final static EventModel instance = new EventModel();
    public  List<Event> data = new LinkedList<>();

    EventModel(){

    }
    public interface GetAllEventListener{
        void onComplete(List<Event> data);
    }
    public void getAllEvents(final Model.GetAllEventListener listener) {
        class MyAsyncTask extends AsyncTask {
            List<Event> data=new LinkedList<>();

            @Override
            public Object doInBackground(Object[] objects) {
                data = AppLocalDb.db.eventDao().getAll();
                return data;
            }

            @Override
            public void onPostExecute(Object o) {
                super.onPostExecute(o);
                listener.onComplete(data);
            }
        }

        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }


    public interface AddEventListener{
        void onComplete();
    }
    public void addEvent(final Event event, final Model.AddEventListener listener){
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDb.db.eventDao().insertAll(event);
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


    public interface deleteEventListener{
        void onComplete();
    }
    public void deleteEvent(final Event event, final Model.DeleteListener listener){
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDb.db.eventDao().delete(event);
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

    public interface GetEventListener{
        void onComplete();
    }
    public void getEvent(final Event event, final Model.GetEventListener listener){
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDb.db.eventDao().getEvent(event.getEventId());
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

}

class EventFirebase {


    public void getAllEvent(Model.GetAllEventListener listener) {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    }



    public void addEvent(final Event event, Model.AddEventListener listener) {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    }

    public void updateEvent(Event event, Model.AddEventListener listener) {
        addEvent(event,listener);
    }

    public void getEvent(String id, Model.GetEventListener listener) {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    }

    public void deleteEvent(Event event, Model.DeleteListener listener) {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    }
}


