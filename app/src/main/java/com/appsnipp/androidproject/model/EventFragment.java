package com.appsnipp.androidproject.model;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appsnipp.androidproject.MainActivity;
import com.appsnipp.androidproject.R;

public class EventFragment extends Fragment {


    private MainActivity mainActivity;

    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        Event event = EventFragmentArgs.fromBundle(getArguments()).getEvent();

        mainActivity.currentEventId = event.getEventID();






        return view;
    }
}

