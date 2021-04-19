package com.appsnipp.androidproject;

import android.content.Context;
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
import com.appsnipp.androidproject.model.Product;
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
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class AddEventFragment extends Fragment {

    private int position;
    private ImageButton selectEventImage;
    private Button saveBtn;
    private Button cancelBtn;
    private EditText eventName;
    private EditText eventDate;
    private MainActivity parent;
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
//    private List<Product> productList;
    private String saveCurrentDate, saveCurrentTime,postRandomName,downloadUrl;


    String[] subject = {" ","birthday","house Party","sit In The House","party"};
    private static final int Gallery_pic =1;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parent = (MainActivity) getActivity();
    }

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

                if (ValidateEventInfo()) {
                    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                    String uid = null;
                    if(user !=null){
                        uid = user.getUid();
                    }

                    //move to the eventDetails

                    Navigation.findNavController(v).popBackStack();

                }




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

    private boolean ValidateEventInfo() {

        description = eventDescription.getText().toString();
        nameEvent = eventName.getText().toString();

        if (imageBitmap == null) {
            Toast.makeText(parent, "Please select Event image !!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(description)) {
            Toast.makeText(parent, "Please fill the description box !!", Toast.LENGTH_SHORT).show();
            eventDescription.setError("Please fill the description box");
            return false;
        }
        if (TextUtils.isEmpty(nameEvent)) {
            Toast.makeText(parent, "Please choose event name !!", Toast.LENGTH_SHORT).show();
            return false;
        }

            StoringImageToFireBaseStorage();
        return true;

    }

    private void StoringImageToFireBaseStorage() {

        Calendar forDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(forDate.getTime());

        Calendar forTime = Calendar.getInstance();
        SimpleDateFormat currenTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currenTime.format(forDate.getTime());

        postRandomName = saveCurrentDate+saveCurrentTime;

        ImageModel.uploadImage(imageBitmap, postRandomName, new ImageModel.Listener() {
            @Override
            public void onSuccess(String url) {
                SavingPostInDataBase(url);
//                        loadingBar.hide();
            }

            @Override
            public void onFail() {

            }
        });



    }

    private void SavingPostInDataBase(String url ) {

        final Event event= new Event(postRandomName+currentUserId,eventName.getText().toString(),saveCurrentDate,eventDescription.getText().toString(),url,saveCurrentTime);
        Model.instance.addEvent(event, new Model.AddEventListener() {
            @Override
            public void onComplete() {
                Toast.makeText(parent, "Event is updated successfully", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void PicApic() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,Gallery_pic);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


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
        super.onActivityResult(requestCode, resultCode, data);
    }
}