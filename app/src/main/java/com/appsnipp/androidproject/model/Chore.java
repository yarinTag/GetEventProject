package com.appsnipp.androidproject.model;

public class Chore {

    public int choreId;
    public String choreName;
    public boolean choreStatus;
    public String choreDetails;
    public int position;

    public Chore(){}
    public Chore(String name,String choreDetails){
        this.choreName=name;
        this.choreDetails=choreDetails;
    }

    public int getChoreId() {
        return choreId;
    }

    public void setChoreId(int choreId) {
        this.choreId = choreId;
    }

    public String getChoreName() {
        return choreName;
    }

    public void setChoreName(String choreName) {
        this.choreName = choreName;
    }

    public boolean isChoreStatus() {
        return choreStatus;
    }

    public void setChoreStatus(boolean choreStatus) {
        this.choreStatus = choreStatus;
    }

    public String getChoreDetails() {
        return choreDetails;
    }

    public void setChoreDetails(String choreDetails) {
        this.choreDetails = choreDetails;
    }
}
