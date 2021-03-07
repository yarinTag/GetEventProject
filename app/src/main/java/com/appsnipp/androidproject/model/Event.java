package com.appsnipp.androidproject.model;


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
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.appsnipp.androidproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Comment;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;


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
    private String eventID;
    private String eventName;
    private String eventDetails;
    private String eventTime;
    private String eventDate;
    private String eventImg;


    public Event() {
    }

    public Event(String eventID,String eventName, String eventTime, String eventDetails,String eventImg,String eventDate) {
        this.eventName=eventName;
        this.eventID=eventID;
        this.eventTime=eventTime;
        this.eventDetails=eventDetails;
        this.eventImg = eventImg;
        this.eventDate = eventDate;
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

    private FirebaseDatabase database ;
    private DatabaseReference eventRef;
    private DatabaseReference userRef;
    private List<Event> eventList = new ArrayList<>();

    public EventFirebase() {

    }

    public void getAllEvent(final Model.GetAllEventListener listener) {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
// ...
        database = FirebaseDatabase.getInstance();
        eventRef = database.getReference("EventList");

        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s: snapshot.getChildren()) {
                    eventList.add( s.getValue(Event.class));

                }
                listener.onComplete(eventList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//
//        ChildEventListener childEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
//                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
//
//                // A new comment has been added, add it to the displayed list
//                Comment comment = dataSnapshot.getValue(Comment.class);
//
//                // ...
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
//                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
//
//                // A comment has changed, use the key to determine if we are displaying this
//                // comment and if so displayed the changed comment.
//                Comment newComment = dataSnapshot.getValue(Comment.class);
//                String commentKey = dataSnapshot.getKey();
//
//                // ...
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
//
//                // A comment has changed, use the key to determine if we are displaying this
//                // comment and if so remove it.
//                String commentKey = dataSnapshot.getKey();
//
//                // ...
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
//                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
//
//                // A comment has changed position, use the key to determine if we are
//                // displaying this comment and if so move it.
//                Comment movedComment = dataSnapshot.getValue(Comment.class);
//                String commentKey = dataSnapshot.getKey();
//
//                // ...
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
//            }
//        };
//        eventRef.addChildEventListener(childEventListener);
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


class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    public List<Event> data;
    private OnItemClickListener listener;

    public EventAdapter() {

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_row, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.listener = listener;
        return holder;
    }

    public void setOnClickListener(EventAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = data.get(position);
        holder.bindData(event, position);
    }

    @Override
    public int getItemCount() {

        if (data == null) {
            return 0;
        }

        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public EventAdapter.OnItemClickListener listener;
        private TextView EventName,userName,eventDescription,eventDate,eventTime;
        private CircleImageView profileImg;
        private ImageView eventImg;
//        private LinearLayout backgroundImage;
        int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            EventName = itemView.findViewById(R.id.eventViewName);
            userName = itemView.findViewById(R.id.user_profile_name);
            eventDescription = itemView.findViewById(R.id.event_description);
            eventDate = itemView.findViewById(R.id.event_date);
            eventTime = itemView.findViewById(R.id.event_time);
            profileImg = itemView.findViewById(R.id.event_profile_image);
            eventImg = itemView.findViewById(R.id.event_img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(position);
                }
            });
        }

        public void bindData(Event event, int position) {
            this.userName.setText(event.getEventName());
            this.eventDescription.setText(event.getEventName());
            this.eventDate.setText(event.getEventName());
            this.eventTime.setText(event.getEventName());
            //need to fill the picture
//            this.position = position;
//            this.backgroundImage.setBackgroundResource(getImageBackground(event.getPosition()));
        }

        private int getImageBackground(int position) {
            switch (position) {
                case 1:
                    return R.drawable.birthday;
                case 2:
                    return R.drawable.house_party;
                case 3:
                    return R.drawable.sit_in_the_house;
                case 4:
                    return R.drawable.party;
                default:
                    return R.color.default_active_item_color;

            }
        }

    }
}