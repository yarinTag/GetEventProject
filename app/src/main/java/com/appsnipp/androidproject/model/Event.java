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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import java.sql.Timestamp;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.appsnipp.androidproject.R;


import de.hdodenhof.circleimageview.CircleImageView;

@Dao
interface EventDao {

    @Query("select * from Event ORDER BY eventTime,eventDate ")
    LiveData<List<Event>> getAll();

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    void insertAll(Event...events);

    @Delete
    void delete(Event event);
    //one Event
    @Query("SELECT * FROM Event\n"+
            "WHERE Event.eventID=:eventId")
    Event getEvent(String eventId);

    @Query("select * from Event WHERE Event.userId=:userId")
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

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public Event(String eventID, String eventName, String eventTime, String eventDetails, String eventImg, String eventDate,String userImg ,String userId,Long lastUpdate) {
        this.eventName=eventName;
        this.eventID=eventID;
        this.eventTime=eventTime;
        this.eventDetails=eventDetails;
        this.eventImg = eventImg;
        this.eventDate = eventDate;
        this.userImg = userImg;
        this.userId = userId;
        this.lastUpdate=lastUpdate;
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

    }
}