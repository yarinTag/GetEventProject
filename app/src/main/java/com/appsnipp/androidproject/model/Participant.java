package com.appsnipp.androidproject.model;

import java.util.List;

public class Participant {


    public int participantId;
    public int position;
    public List<User> comingUserList;
    public List<User> notComingUserList;
    public List<User> maybeComingUserList;

    public Participant() {
    }

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public List<User> getComingUserList() {
        return comingUserList;
    }

    public void setComingUserList(List<User> comingUserList) {
        this.comingUserList = comingUserList;
    }

    public List<User> getNotComingUserList() {
        return notComingUserList;
    }

    public void setNotComingUserList(List<User> notComingUserList) {
        this.notComingUserList = notComingUserList;
    }

    public List<User> getMaybeComingUserList() {
        return maybeComingUserList;
    }

    public void setMaybeComingUserList(List<User> maybeComingUserList) {
        this.maybeComingUserList = maybeComingUserList;
    }
}
