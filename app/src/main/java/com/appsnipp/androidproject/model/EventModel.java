package com.appsnipp.androidproject.model;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EventModel {

    public final static EventModel instance = new EventModel();
    public List<Event> data = new ArrayList<>();

    EventModel(){

    }
    public interface GetAllEventListener{
        void onComplete(List<Event> data);
    }
    public void getAllEvents(final GetAllEventListener listener) {
        EventFirebase.instance.getAllEvent(new Model.GetAllEventListener() {
            @Override
            public void onComplete(final List<Event> result) {
                final List<Event> data =result;
                class MyAsyncTask extends AsyncTask {
                    @Override
                    protected Object doInBackground(Object[] objects) {
//                        AppLocalDb.db.eventDao().insertAll((Event) data);
//                        data.forEach( e ->AppLocalDb.db.eventDao().insertAll( (e));
//                        for(Event e : data) {
//                            AppLocalDb.db.eventDao().insertAll((e));
//                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);

                        listener.onComplete( data);
                    }
                }

                MyAsyncTask task = new MyAsyncTask();
                task.execute();
            }
        });

    }


    public interface AddEventListener{
        void onComplete();
    }
    public void addEvent(final Event event, final Model.AddEventListener listener){
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


    public interface deleteEventListener{
        void onComplete();
    }
    public void deleteEvent(final Event event, final Model.DeleteListener listener){
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

    public interface GetEventListener{
        void onComplete();
    }
    public void getEvent(final Event event, final Model.GetEventListener listener){
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDb.db.eventDao().getEvent(event.getEventID());
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

}
