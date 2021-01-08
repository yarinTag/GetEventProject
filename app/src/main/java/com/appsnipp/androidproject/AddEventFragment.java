package com.appsnipp.androidproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.appsnipp.androidproject.model.Event;
import com.appsnipp.androidproject.model.Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class AddEventFragment extends Fragment {

    private int position;
    Button saveBtn;
    Button cancelBtn;
    EditText eventName;
    EditText eventDate;
    EditText eventDescription;
    Spinner spinnerEvent;
    private String receivedSize;

    String[] subject = {" ","birthday","house Party","sit In The House","party"};

    public AddEventFragment() {
        // Required empty public constructor
        this.position = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_add_event, container, false);
        spinnerEvent = view.findViewById(R.id.event_subject);
        spinnerEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {

                position = i;

                //Showing selected spinner item
                Toast.makeText(getContext(), "Selected: " + subject[i], Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,subject);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEvent.setAdapter(aa);
        eventName = view.findViewById(R.id.AddEventName);
        eventDate = view.findViewById(R.id.addEventDate);
        eventDescription = view.findViewById(R.id.addEventDescription);
        saveBtn = view.findViewById(R.id.SaveEventBtn);
        cancelBtn = view.findViewById(R.id.CancelEventBtn);

        Bundle bundle = this.getArguments();
        if (bundle != null){
            //Key default value
            receivedSize = bundle.getString("size","0");
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Event event = new Event();
//                event.setEventName(eventName.getText().toString());
//                event.setEventDetails(eventDescription.getText().toString());
//                event.setEventTime(eventDate.getText().toString());
//                event.setPosition(position);


                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                String uid = null;
                if(user !=null){
                    uid = user.getUid();
                }
                Event event= new Event(eventName.toString(),eventDate.toString(),eventDescription.toString());
                event.setEventId(uid+receivedSize);
                Model.instance.addEvent(event, new Model.AddEventListener() {
                    @Override
                    public void onComplete() {

                    }
                });
                //move to the eventDetails

                AddEventFragmentDirections.SaveActionAddEventFragmentToEventListFragment action = AddEventFragmentDirections.saveActionAddEventFragmentToEventListFragment(event);
                Navigation.findNavController(v).navigate(action);

            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.cancle_addEventFragment_to_eventListFragment);
            }
        });

        return view;
    }
}