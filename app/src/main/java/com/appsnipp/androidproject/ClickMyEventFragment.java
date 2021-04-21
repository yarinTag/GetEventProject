package com.appsnipp.androidproject;

import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appsnipp.androidproject.model.Event;
import com.appsnipp.androidproject.model.ImageModel;
import com.appsnipp.androidproject.model.Model;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class ClickMyEventFragment extends Fragment {

    private ImageView eventImg;
    private EditText eventName;
    private EditText eventDescription;
    private Button updateEvent, deleteEvent;
    Event eventId;
    private static final int Gallery_pic =1;
    private Uri imageUri;
    private Bitmap imageBitmap;
    String saveCurrentDate;
    String saveCurrentTime;
    View view;
    ImageButton backbtn;
    MainActivity parent;
    private ProgressDialog loadingBar;

    public ClickMyEventFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parent= (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_click_my_event, container, false);

        eventImg = view.findViewById(R.id.imageView);
        eventName = view.findViewById(R.id.editName);
        eventDescription = view.findViewById(R.id.editD);
        updateEvent = view.findViewById(R.id.editPost_btn);
        deleteEvent = view.findViewById(R.id.deleteEvent_btn);
        backbtn=view.findViewById(R.id.backbtn);
        loadingBar = new ProgressDialog(parent);

        eventId = ClickMyEventFragmentArgs.fromBundle(getArguments()).getEvent();

        eventName.setText(eventId.getEventName());
        eventDescription.setText(eventId.getEventDetails());
        Picasso.get().load(eventId.getEventImg()).placeholder(R.drawable.photo).into(eventImg);
        eventImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PicApic();
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigateUp();
            }
        });
        updateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingBar.setTitle(" My Event's");
                loadingBar.setMessage("Please wait, while we updating your Event...");
                loadingBar.show();

                ValidateEventInfo(new AddEventFragment.savePostListener() {
                    @Override
                    public void onComplete(boolean ifSave) {
                        if(ifSave) {
                            //move to the eventDetails
                                loadingBar.dismiss();
                                Navigation.findNavController(view).navigateUp();
                        }
                    }
                });

            }
        });

        deleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.instance.deleteEvent(eventId, new Model.DeleteListener() {
                    @Override
                    public void onComplete() {
                        Toast.makeText(parent,"Event Deleted successfully",Toast.LENGTH_LONG).show();
                        Navigation.findNavController(view).navigateUp();

                    }
                });
            }
        });


        return view;
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
                eventImg.setImageBitmap(imageBitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            //eventImg.setImageURI(imageUri);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void ValidateEventInfo(final AddEventFragment.savePostListener listener) {

       String description = eventDescription.getText().toString();
       String  nameEvent = eventName.getText().toString();


        if (TextUtils.isEmpty(description)) {
            eventDescription.setError("Please fill the description box");
            listener.onComplete(false);
        }
        if (TextUtils.isEmpty(nameEvent)) {
            listener.onComplete(false);
        }

        StoringImageToFireBaseStorage(new AddEventFragment.savePostListener() {
            @Override
            public void onComplete(boolean ifSave) {
                listener.onComplete(true);
            }


        });


    }

    private void StoringImageToFireBaseStorage(final AddEventFragment.savePostListener listener) {

        Calendar forDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(forDate.getTime());

        Calendar forTime = Calendar.getInstance();
        SimpleDateFormat currenTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currenTime.format(forDate.getTime());

        String postRandomName = saveCurrentDate + saveCurrentTime;
        if(imageBitmap!=null){
        ImageModel.uploadImage(imageBitmap, postRandomName, new ImageModel.Listener() {
            @Override
            public void onSuccess(String url) {
                SavingPostInDataBase(url, new AddEventFragment.savePostListener() {
                    @Override
                    public void onComplete(boolean ifSave) {
                        listener.onComplete(false);

                    }

                });

//                        loadingBar.hide();
            }

            @Override
            public void onFail() {

            }
        });
    }
        else {
            SavingPostInDataBase(eventId.getEventImg(), new AddEventFragment.savePostListener() {
                @Override
                public void onComplete(boolean ifSave) {
                    listener.onComplete(false);

                }

            });

        }

    }


    private void SavingPostInDataBase(String url , final AddEventFragment.savePostListener listener ) {

        final Event event= new Event(eventId.getEventID(),eventName.getText().toString(),saveCurrentDate,eventDescription.getText().toString(),url,saveCurrentTime,eventId.getUserImg(),eventId.getUserId(),System.currentTimeMillis(),false);
        Model.instance.addEvent(event, new Model.EventListener() {
            @Override
            public void onComplete() {
                listener.onComplete(true);
            }
        });

    }

}