package com.appsnipp.androidproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.appsnipp.androidproject.model.Event;
import com.appsnipp.androidproject.model.EventModel;
import com.appsnipp.androidproject.model.EventViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.LinkedList;
import java.util.List;

import Adapter.EventAdapter;


public class EventListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private RecyclerView rv;
    private EventAdapter adapter;
    List<Event> eventList = new LinkedList<>();
    private eventInterface callback;
    EventViewModel viewModel;
    LiveData<List<Event>> liveData;
    SwipeRefreshLayout refreshLayout ;
    MainActivity parent;
    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        viewModel.refresh(new EventViewModel.RefreshEventListener() {
            @Override
            public void onComplete() {
                refreshLayout.setRefreshing(false);
            }
        });
    }


    public interface eventInterface{
        void onItemClickEvent(Event event);
    }

    public EventListFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(EventViewModel.class);
        parent= (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_event_list, container, false);
        parent.getSupportActionBar().setTitle("Home");

        refreshLayout = view.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);

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



        rv = view.findViewById(R.id.eventListFrag);
        rv.setHasFixedSize(true);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);

        adapter = new EventAdapter();
        rv.setAdapter(adapter);


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
                Navigation.findNavController(v).navigate(R.id.action_global_addEventFragment);

            }
        });

        return view;
    }


}