package com.appsnipp.androidproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.appsnipp.androidproject.model.Event;
import com.appsnipp.androidproject.model.Model;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.LinkedList;
import java.util.List;

import Adapter.EventAdapter;


public class EventListFragment extends Fragment {


    private RecyclerView rv;
    private EventAdapter adapter;
    List<Event> eventList = new LinkedList<>();
    private eventInterface callback;



    public interface eventInterface{
        void onItemClickEvent(Event event);
    }

    public EventListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_event_list, container, false);

        try {
            if(getArguments()!= null){
                //try to get data from addEventFragment
//                EventListFragmentArgs args = EventListFragmentArgs.fromBundle(getArguments());
                //add the event into the model
//                ModelDemo.instance.addEvent(args.getEvent());
                //refresh the adapter
                adapter.notifyDataSetChanged();
            }
        }catch (Exception e){}

        rv = view.findViewById(R.id.eventListFrag);
        rv.hasFixedSize();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);

        adapter = new EventAdapter();
        adapter.data = eventList;
        rv.setAdapter(adapter);
        if (adapter.getItemCount() != 0 ){
            reloadData();
        }

        FloatingActionButton fab = view.findViewById(R.id.addEventBtn);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String size=""+eventList.size();
                NavDirections navDirections= EventListFragmentDirections.actionEventListFragmentToAddEventFragment(size);
                Navigation.findNavController(v).navigate(navDirections);
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
    private void reloadData() {
        Model.instance.getAllEvent(new Model.GetAllEventListener() {
            @Override
            public void onComplete(List<Event> result) {
                eventList = result;
                if (eventList != null){
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}