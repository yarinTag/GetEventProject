package com.appsnipp.androidproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.appsnipp.androidproject.model.Event;
import com.appsnipp.androidproject.model.Model;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.LinkedList;
import java.util.List;

import Adapter.EventAdapter;


public class EventListFragment extends Fragment {
    MyAdapter adapter;
    List<Event> eventList = new LinkedList<Event>();

    public EventListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        RecyclerView list = view.findViewById(R.id.eventListFrag);
        list.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);
        EventAdapter adapter = new EventAdapter();
        list.setAdapter(adapter);
        adapter.data = eventList;
        if(adapter.getItemCount()!=0){
            reloadData();
        }
        FloatingActionButton Fab=  view.findViewById(R.id.addEventBtn);

   Fab.setOnClickListener(new View.OnClickListener() {
        @Override
         public void onClick(View view) {
            String size=""+eventList.size();
            NavDirections navDirections= EventListFragmentDirections.actionEventListFragmentToAddEventFragment5(size);
            Navigation.findNavController(view).navigate(navDirections);

        }
     });
        return view;
    }
    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if(eventList==null){
                return 0;
            }

            return eventList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if(view==null ){
                view = getLayoutInflater().inflate(R.layout.event_row, null);
            }

            return view;
        }
    }
    void reloadData(){
        Model.instance.getAllEvent( new Model.GetAllEventListener(){
            @Override
            public void onComplete(List<Event> data) {
                eventList=data;
                if(eventList!=null) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
