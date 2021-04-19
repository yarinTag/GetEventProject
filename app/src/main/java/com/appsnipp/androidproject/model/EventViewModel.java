package com.appsnipp.androidproject.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class EventViewModel extends ViewModel {

    private LiveData<List<Event>> liveData;

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
