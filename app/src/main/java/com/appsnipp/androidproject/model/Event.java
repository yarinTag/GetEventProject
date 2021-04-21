package com.appsnipp.androidproject.model;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.appsnipp.androidproject.MyApplication;
import com.appsnipp.androidproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import de.hdodenhof.circleimageview.CircleImageView;

@Dao
interface EventDao {

    @Query("select * from Event Where isDelete == 0 order by eventTime And eventDate ")
    LiveData<List<Event>> getAll();

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    void insertAll(Event...events);

    @Delete
    void delete(Event event);
    //one Event
    @Query("SELECT * FROM Event\n"+
            "WHERE Event.eventID=:eventId")
    Event getEvent(String eventId);

    @Query("select * from Event Where Event.userId=:userId And isDelete==0 order by eventTime And eventDate ")
    LiveData<List<Event>> getEventsUser(String userId);
}



@Entity(tableName = "Event")
public class Event implements Serializable {
    @PrimaryKey
    @NonNull
    private String eventID;
    private String eventName;
    private String eventDetails;
    private String eventTime;
    private String eventDate;
    private String eventImg;
    private String userImg;
    private String userId;
    private Long lastUpdate;
    private boolean isDelete;



    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Event() {
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public Event(String eventID, String eventName, String eventTime, String eventDetails, String eventImg, String eventDate,String userImg ,String userId,Long lastUpdate,boolean isDelete) {
        this.eventName=eventName;
        this.eventID=eventID;
        this.eventTime=eventTime;
        this.eventDetails=eventDetails;
        this.eventImg = eventImg;
        this.eventDate = eventDate;
        this.userImg = userImg;
        this.userId = userId;
        this.lastUpdate=lastUpdate;
        this.isDelete = isDelete;
    }

    @NonNull
    public String getEventID() {
        return eventID;
    }

    public void setEventID(@NonNull String eventID) {
        this.eventID = eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventImg() {
        return eventImg;
    }

    public void setEventImg(String eventImg) {
        this.eventImg = eventImg;
    }

}

class EventFirebase {

    public static final EventFirebase instance = new EventFirebase();

    private FirebaseDatabase database ;
    private DatabaseReference eventRef;
    private DatabaseReference eventRef2;
    private List<Event> eventList ;
    private FirebaseDatabase instance1;

    private EventFirebase() {
        database = FirebaseDatabase.getInstance();
    }

    public void getAllEvent(Long lastUpdate, final Model.GetAllEventListener listener) {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
// ...

        eventRef = database.getReference("EventList");
        final List<Event> eventList2 = new ArrayList<>();
        eventRef.orderByChild("lastUpdate").startAt(lastUpdate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s: snapshot.getChildren()) {
                    Event event = s.getValue(Event.class);
                    eventList2.add( event);
                }
                eventList = eventList2;
                listener.onComplete(eventList2);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onComplete(eventList);

            }
        });

    }


    public void addUserToEvent(String eventId,String userId){

        database.getReference("Users").child(userId).child("EventList").child(eventId).setValue(eventId);

    }


    public void addEvent(final Event event, Model.EventListener listener) {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        eventRef=database.getReference("EventList");

        eventRef.child(event.getEventID()).setValue(event);
//        database.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("EventList").child(event.getEventID()).setValue(event.getEventID());

//        addUserToEvent(event.getEventID(),mAuth.getCurrentUser().getUid());
//        database.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("EventList").child(event.getEventID()).setValue(event.getEventID());


        listener.onComplete();

    }

    public void updateEvent(Event event, final Model.EventListener listener) {
        addEvent(event, new Model.EventListener() {
            @Override
            public void onComplete() {
                listener.onComplete();
            }
        });
    }

    public void getEvent(String id, Model.GetEventListener listener) {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    }


    public void DeleteEvent(Event event, Model.DeleteListener listener) {
        database.getReference("EventList").child(event.getEventID()).child("delete").setValue(true);
        database.getReference("EventList").child(event.getEventID()).child("lastUpdate").setValue(System.currentTimeMillis());
        listener.onComplete();

    }

    public interface eventListListener{
        void onComplete(List<Event> events);
    }

    public void getUserList(final eventListListener listener) {
        eventRef = database.getReference("Users").child(UserModel.instance.getUser().getUserID()).child("EventList");
        eventRef2 = database.getReference("EventList");
        final List<Event> eventList = new ArrayList<>();

        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren()) {
                    String s1 = s.getValue(String.class);
                    EventModel.instance.getEvent(s1, new EventModel.GetEventListener() {
                        @Override
                        public void onComplete(Event event) {
                            eventList.add(event);
                        }
                    });
                }
                listener.onComplete(eventList);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

class EventModel {

    public final static EventModel instance = new EventModel();
    public List<Event> data = new ArrayList<>();
    public Event event;
    Long lastUpdate;
    SharedPreferences sp;
    EventModel(){

    }
    public interface GetAllEventListener{
        void onComplete();
    }

    public interface GetAllLiveDataListener{
        void onComplete(LiveData<List<Event>> data);
    }



    public void getAllEventsUser(String userId,final GetAllLiveDataListener listener) {
        LiveData<List<Event>> liveData = (LiveData<List<Event>>) AppLocalDb.db.eventDao().getEventsUser(userId);
        refreshMyEvents(null);
        listener.onComplete(liveData);
    }



    public void refreshMyEvents(final GetAllEventListener listener) {
        sp = MyApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
        lastUpdate= sp.getLong("lastupdated",0);
        EventFirebase.instance.getAllEvent(lastUpdate,new Model.GetAllEventListener() {
            @Override
            public void onComplete(final List<Event> result) {
                final List<Event> data =result;
                class MyAsyncTask extends AsyncTask {
                    @Override
                    protected Object doInBackground(Object[] objects) {
//                        AppLocalDb.db.eventDao().insertAll((Event) data);
//                        data.forEach( e ->AppLocalDb.db.eventDao().insertAll( (e));
                        for(Event e : data) {
                            AppLocalDb.db.eventDao().insertAll((e));
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

    public void getAllEvents(final GetAllLiveDataListener listener) {

        LiveData<List<Event>> liveData = (LiveData<List<Event>>) AppLocalDb.db.eventDao().getAll();
        refreshMyEvents(null);
        listener.onComplete(liveData);
    }


    public interface AddEventListener{
        void onComplete();
    }
    public void addEvent(final Event event, final Model.EventListener listener){
        EventFirebase.instance.addEvent(event, new Model.EventListener() {
            @Override
            public void onComplete() {

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
        });
    }


    public interface deleteEventListener{
        void onComplete();
    }
    public void deleteEvent(final Event event, final Model.DeleteListener listener){
        EventFirebase.instance.DeleteEvent(event, new Model.DeleteListener() {
            @Override
            public void onComplete() {
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
        });

    }

    public interface GetEventListener{
        void onComplete(Event event);
    }

    public void getEvent(final String eventId, final GetEventListener listener){

        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                event = AppLocalDb.db.eventDao().getEvent(eventId);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if(listener!= null){
                    listener.onComplete(event);
                }
            }
        };
        MyAsyncTask task= new MyAsyncTask();
        task.execute();
    }

    public interface UpdateListener{
        void onComplete();
    }

    public void updateEvent(final Event event, final UpdateListener listener){
        EventFirebase.instance.updateEvent(event, new Model.EventListener() {
            @Override
            public void onComplete() {
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
        });

    }

}


