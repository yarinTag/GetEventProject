package Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.androidproject.R;
import com.appsnipp.androidproject.model.Event;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    public List<Event> data = new ArrayList<>();
    private OnItemClickListener listener;

    public EventAdapter() {

    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_row,parent,false);
        ViewHolder holder = new ViewHolder(view);
        holder.listener =  listener;
        return holder;
    }

    public void setOnClickListener(EventAdapter.OnItemClickListener listener){
        this.listener = listener;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = data.get(position);
        holder.bindData(event,position);
    }

    @Override
    public int getItemCount() {

        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private EventAdapter.OnItemClickListener listener;
        private TextView eventName,userName,eventDescription,eventDate,eventTime;
        private CircleImageView profileImg;
        private ImageView eventImg;
        //        private LinearLayout backgroundImage;
        int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.user_profile_name);
//            userName = itemView.findViewById(R.id.user_profile_name);
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
//            this.userName.setText(event.getEventName());
            this.eventName.setText(event.getEventName());
            this.eventDescription.setText(event.getEventDetails());
            this.eventDate.setText(event.getEventDate());
            this.eventTime.setText(event.getEventTime());


            Picasso.get().load(event.getEventImg()).placeholder(R.drawable.photo).into(eventImg);


        }

    }
}
