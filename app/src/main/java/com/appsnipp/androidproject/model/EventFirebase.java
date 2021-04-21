package com.appsnipp.androidproject.model;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventFirebase {

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
