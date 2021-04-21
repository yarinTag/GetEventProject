package com.appsnipp.androidproject.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.appsnipp.androidproject.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class EventModel {

    public final static EventModel instance = new EventModel();
    public List<Event> data = new ArrayList<>();
    public Event event;
    Long lastUpdate;
    SharedPreferences sp;
    EventModel(){

    }
    public interface GetAllEventListener{
        void onComplete();
    }

    public interface GetAllLiveDataListener{
        void onComplete(LiveData<List<Event>> data);
    }



    public void getAllEventsUser(String userId,final GetAllLiveDataListener listener) {
        LiveData<List<Event>> liveData = (LiveData<List<Event>>) AppLocalDb.db.eventDao().getEventsUser(userId);
        refreshMyEvents(null);
        listener.onComplete(liveData);
    }



    public void refreshMyEvents(final GetAllEventListener listener) {
        sp = MyApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
        lastUpdate= sp.getLong("lastupdated",0);
        EventFirebase.instance.getAllEvent(lastUpdate,new Model.GetAllEventListener() {
            @Override
            public void onComplete(final List<Event> result) {
                final List<Event> data =result;
                class MyAsyncTask extends AsyncTask {
                    @Override
                    protected Object doInBackground(Object[] objects) {
//                        AppLocalDb.db.eventDao().insertAll((Event) data);
//                        data.forEach( e ->AppLocalDb.db.eventDao().insertAll( (e));
                        for(Event e : data) {
                            AppLocalDb.db.eventDao().insertAll((e));
                            if(e.getLastUpdate()>lastUpdate){
                                lastUpdate=e.getLastUpdate();
                            }
                        }
                        SharedPreferences.Editor editor= sp.edit();
                        editor.putLong("lastupdated",lastUpdate);
                        editor.commit();


                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);

                        if(listener!=null) {
                            listener.onComplete();
                        }
                    }
                }

                MyAsyncTask task = new MyAsyncTask();
                task.execute();
            }
        });

    }

    public void getAllEvents(final GetAllLiveDataListener listener) {

        LiveData<List<Event>> liveData = (LiveData<List<Event>>) AppLocalDb.db.eventDao().getAll();
        refreshMyEvents(null);
        listener.onComplete(liveData);
    }


    public interface AddEventListener{
        void onComplete();
    }
    public void addEvent(final Event event, final Model.EventListener listener){
        EventFirebase.instance.addEvent(event, new Model.EventListener() {
            @Override
            public void onComplete() {

                class MyAsyncTask extends AsyncTask {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        AppLocalDb.db.eventDao().insertAll(event);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        if(listener!= null){
                            listener.onComplete();
                        }
                    }
                };
                MyAsyncTask task= new MyAsyncTask();
                task.execute();
            }
        });
    }


    public interface deleteEventListener{
        void onComplete();
    }
    public void deleteEvent(final Event event, final Model.DeleteListener listener){
        EventFirebase.instance.DeleteEvent(event, new Model.DeleteListener() {
            @Override
            public void onComplete() {
                class MyAsyncTask extends AsyncTask {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        AppLocalDb.db.eventDao().delete(event);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        if(listener!= null){
                            listener.onComplete();
                        }
                    }
                };
                MyAsyncTask task= new MyAsyncTask();
                task.execute();
            }
        });

    }

    public interface GetEventListener{
        void onComplete(Event event);
    }

    public void getEvent(final String eventId, final GetEventListener listener){

        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                event = AppLocalDb.db.eventDao().getEvent(eventId);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if(listener!= null){
                    listener.onComplete(event);
                }
            }
        };
        MyAsyncTask task= new MyAsyncTask();
        task.execute();
    }

    public interface UpdateListener{
        void onComplete();
    }

    public void updateEvent(final Event event, final UpdateListener listener){
        EventFirebase.instance.updateEvent(event, new Model.EventListener() {
            @Override
            public void onComplete() {
                class MyAsyncTask extends AsyncTask {

                    @Override
                    protected Object doInBackground(Object[] objects) {
                        AppLocalDb.db.eventDao().insertAll(event);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        if(listener!= null){
                            listener.onComplete();
                        }
                    }
                };
                MyAsyncTask task= new MyAsyncTask();
                task.execute();
            }
        });

    }

}
