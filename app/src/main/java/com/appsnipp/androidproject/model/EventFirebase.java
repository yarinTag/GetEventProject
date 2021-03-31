package com.appsnipp.androidproject.model;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventFirebase {

    public static final EventFirebase instance = new EventFirebase();

    private FirebaseDatabase database ;
    private DatabaseReference eventRef;
    private List<Event> eventList ;

    private EventFirebase() {
        database = FirebaseDatabase.getInstance();
    }

    public void getAllEvent(final Model.GetAllEventListener listener) {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
// ...

        eventRef = database.getReference("EventList");
        eventList = new ArrayList<>();
        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s: snapshot.getChildren()) {
                    Event event = s.getValue(Event.class);
                    eventList.add( event);

                }
                listener.onComplete(eventList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void addUserToEvent(String eventId,String userId){

        database.getReference("Users").child(userId).child("EventList").child(eventId).setValue(eventId);

    }


    public void addEvent(final Event event, Model.AddEventListener listener) {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        eventRef=database.getReference("EventList");
        eventRef.child(event.getEventID()).setValue(event);

        addUserToEvent(event.getEventID(),mAuth.getCurrentUser().getUid());
//        database.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("EventList").child(event.getEventID()).setValue(event.getEventID());


        listener.onComplete();

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
