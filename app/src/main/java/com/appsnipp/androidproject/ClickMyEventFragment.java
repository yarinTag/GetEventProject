package com.appsnipp.androidproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ClickMyEventFragment extends Fragment {

    private ImageView eventImg;
    private TextView eventName;
    private TextView eventDescription;
    private Button updateEvent, deleteEvent;
    String eventNName;
    String eventDDescription;
    String eventIImage;

    public ClickMyEventFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_click_my_event, container, false);

        eventImg = view.findViewById(R.id.imageView);
        eventName = view.findViewById(R.id.eventNameView);
        eventDescription = view.findViewById(R.id.Description_view);
        updateEvent = view.findViewById(R.id.editPost_btn);
        deleteEvent = view.findViewById(R.id.deleteEvent_btn);

        eventNName = ClickMyEventFragmentArgs.fromBundle(getArguments()).getEventName();
        eventName.setText(eventNName);
        eventDDescription = ClickMyEventFragmentArgs.fromBundle(getArguments()).getEventDescription();
        eventDescription.setText(eventDDescription);

        return view;
    }
}