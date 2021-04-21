package com.appsnipp.androidproject.model;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class EventViewModel extends ViewModel {
    private LiveData<List<Event>> liveData;
    private LiveData<List<Event>> liveDataUser;
    public void getData(final EventModel.GetAllLiveDataListener listener) {

        if(liveData==null) {
            Model.instance.getAllEvent(new EventModel.GetAllLiveDataListener() {
                @Override
                public void onComplete(LiveData<List<Event>> data) {
                    liveData = data;
                    listener.onComplete(liveData);

                }
            });
        }else {
            listener.onComplete(liveData);
        }
    }

    public void getUserData(String userId,final EventModel.GetAllLiveDataListener listener) {

        if(liveDataUser==null) {
            Model.instance.getEventsUser(userId,new EventModel.GetAllLiveDataListener() {
                @Override
                public void onComplete(LiveData<List<Event>> data) {
                    liveDataUser = data;
                    listener.onComplete(liveDataUser);
                }
            });
        }else {
            listener.onComplete(liveDataUser);
        }
    }


    public interface RefreshEventListener{
        void onComplete();
    }

    public void refresh(final RefreshEventListener listener) {

        Model.instance.RefreshMyEvent(new Model.RefreshEventListener() {
            @Override
            public void onComplete() {
                listener.onComplete();
            }
        });
    }

}
