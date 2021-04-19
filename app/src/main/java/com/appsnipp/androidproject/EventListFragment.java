package com.appsnipp.androidproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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
import com.appsnipp.androidproject.model.EventFirebase;
import com.appsnipp.androidproject.model.EventModel;
import com.appsnipp.androidproject.model.EventViewModel;
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
    EventViewModel viewModel;
    LiveData<List<Event>> liveData;


    public interface eventInterface{
        void onItemClickEvent(Event event);
    }

    public EventListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(EventViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_event_list, container, false);

        viewModel.getData(new EventModel.GetAllLiveDataListener() {
            @Override
            public void onComplete(LiveData<List<Event>> data) {
                liveData = data;
                liveData.observe(getViewLifecycleOwner(), new Observer<List<Event>>() {
                    @Override
                    public void onChanged(List<Event> events) {

                        eventList = events;
                        adapter.setList(eventList);
                    }
                });
            }
        });



//        Model.instance.getAllEvent(new Model.GetAllEventListener() {
//            @Override
//            public void onComplete(List<Event> result) {
//
//                eventList = result;
//                adapter.setList(eventList);
//            }
//        });

        rv = view.findViewById(R.id.eventListFrag);
//        rv.hasFixedSize();
        rv.setHasFixedSize(true);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);

        adapter = new EventAdapter();
//        adapter.setList(eventList);
        rv.setAdapter(adapter);
//        if (adapter.getItemCount() != 0 ){
//            reloadData();
//        }

        adapter.setOnClickListener(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                NavDirections navDirections= EventListFragmentDirections.actionEventListFragmentToEventFragment(eventList.get(position));
                Navigation.findNavController(container).navigate(navDirections);
            }
        });
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


}