package com.appsnipp.androidproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.appsnipp.androidproject.model.Event;
import com.appsnipp.androidproject.model.ImageModel;
import com.appsnipp.androidproject.model.Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


public class AddEventFragment extends Fragment {

    private int position;
    private ImageButton selectEventImage;
    private Button saveBtn;
    private Button cancelBtn;
    private EditText eventName;
    private EditText eventDate;
    private EditText eventDescription;
    private Spinner spinnerEvent;
    private String receivedSize;
    private Uri imageUri;
    private String currentUserId;

    private String description,nameEvent;
    private Bitmap imageBitmap;
    private StorageReference eventImageReference;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef,eventRef;

    private String saveCurrentDate, saveCurrentTime,postRandomName,downloadUrl;


    String[] subject = {" ","birthday","house Party","sit In The House","party"};
    private static final int Gallery_pic =1;

    public AddEventFragment() {
        // Required empty public constructor
        this.position = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        eventImageReference = FirebaseStorage.getInstance().getReference();

        mAuth=FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        eventRef = FirebaseDatabase.getInstance().getReference().child("Events");

        View view = inflater.inflate(R.layout.fragment_add_event, container, false);
//        spinnerEvent = view.findViewById(R.id.event_subject);
//        spinnerEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
//
//                position = i;
//
//                //Showing selected spinner item
//                Toast.makeText(getContext(), "Selected: " + subject[i], Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });



        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,subject);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerEvent.setAdapter(aa);
        selectEventImage = view.findViewById(R.id.imageButton);
        eventName = view.findViewById(R.id.AddEventName);
//        eventDate = view.findViewById(R.id.addEventDate);
        eventDescription = view.findViewById(R.id.addEventDescription);
        saveBtn = view.findViewById(R.id.SaveAddEventBtn);
        cancelBtn = view.findViewById(R.id.CancelAddEventBtn);

        Bundle bundle = this.getArguments();
        if (bundle != null){
            //Key default value
            receivedSize = bundle.getString("size","0");
        }


        selectEventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PicApic();
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Event event1 = new Event();
//                event1.setEventName(eventName.getText().toString());
//                event1.setEventDetails(eventDescription.getText().toString());
//                event1.setEventTime(eventDate.getText().toString());

                ValidateEventInfo();


                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                String uid = null;
                if(user !=null){
                    uid = user.getUid();
                }
//                Event event= new Event(uid+receivedSize,eventName.getText().toString(),eventDate.getText().toString(),eventDescription.getText().toString());
//                Model.instance.addEvent(event, new Model.AddEventListener() {
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
                //move to the eventDetails

//                AddEventFragmentDirections.SaveActionAddEventFragmentToEventListFragment action = AddEventFragmentDirections.saveActionAddEventFragmentToEventListFragment(event);
//                Navigation.findNavController(v).navigate(action);

            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });

        return view;
    }

    private void ValidateEventInfo() {

        description = eventDescription.getText().toString();
        nameEvent = eventName.getText().toString();

        if (imageUri == null)
            Toast.makeText(getActivity(), "Please select Event image !!", Toast.LENGTH_SHORT).show();
        if (TextUtils.isEmpty(description))
            Toast.makeText(getActivity(), "Please fill the description box !!", Toast.LENGTH_SHORT).show();
        if (TextUtils.isEmpty(nameEvent))
            Toast.makeText(getActivity(), "Please choose event name !!", Toast.LENGTH_SHORT).show();
        else
            StoringImageToFireBaseStorage();

    }

    private void StoringImageToFireBaseStorage() {

        Calendar forDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(forDate.getTime());

        Calendar forTime = Calendar.getInstance();
        SimpleDateFormat currenTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currenTime.format(forDate.getTime());

        postRandomName = saveCurrentDate+saveCurrentTime;

        ImageModel.uploadImage(imageBitmap, currentUserId, new ImageModel.Listener() {
            @Override
            public void onSuccess(String url) {
                SavingPostInDataBase(url);
//                        loadingBar.hide();
            }

            @Override
            public void onFail() {

            }
        });

//        StorageReference eventImagePath = eventImageReference.child("Event Images").child(imageUri.getLastPathSegment() + postRandomName + "jpg");
//
//        eventImagePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                if (task.isSuccessful()) {
//
//
//                    Toast.makeText(getActivity(), "Image uploaded successfully to Storage :)", Toast.LENGTH_SHORT).show();
//
//                    SavingPostInDataBase();
//                }
//                else {
//                    String message = task.getException().getMessage();
//                    Toast.makeText(getActivity(), "Error occured: " + message, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

    }

    private void SavingPostInDataBase(String url) {

        final Event event= new Event(postRandomName+currentUserId,eventName.getText().toString(),saveCurrentDate,eventDescription.getText().toString(),url,saveCurrentTime);
        Model.instance.addEvent(event, new Model.AddEventListener() {
            @Override
            public void onComplete() {
                Toast.makeText(getContext(), "Event is updated successfully", Toast.LENGTH_SHORT).show();
            }
        });

//        userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    String userName = snapshot.child("fullName").getValue().toString();
//                    String userImage = snapshot.child("profileImage:").getValue().toString();
//
//                    HashMap eventsMap = new HashMap();
//                    eventsMap.put("uid", currentUserId);
//                    eventsMap.put("date", saveCurrentDate);
//                    eventsMap.put("time", saveCurrentTime);
//                    eventsMap.put("description", description);
//                    eventsMap.put("eventImage", downloadUrl);
//                    eventsMap.put("fullName", userName);
//                    eventsMap.put("userImage", userImage);
//
//                    eventRef.child(postRandomName).updateChildren(eventsMap).addOnCompleteListener(new OnCompleteListener() {
//                        @Override
//                        public void onComplete(@NonNull Task task) {
//                            if (task.isSuccessful()) {
//                                Toast.makeText(getContext(), "Event is updated successfully", Toast.LENGTH_SHORT).show();
//                            }else {
//                                Toast.makeText(getActivity(), "Error occurred while updating your event", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    private void PicApic() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,Gallery_pic);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==Gallery_pic && resultCode==RESULT_OK && data!=null){
            imageUri = data.getData();
            InputStream imageStream = null;
            try {

                imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                imageBitmap = BitmapFactory.decodeStream(imageStream);
                selectEventImage.setImageBitmap(imageBitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            selectEventImage.setImageURI(imageUri);

        }
    }
}