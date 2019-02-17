package com.ziyadmsq.android.lowclub;

import java.util.HashMap;
import java.util.Map;

public class Event {

    private String ID;//#
    private String title;//#
    private String about;//#
    private Date date;//#
    private boolean isOpen;//#
    private boolean needAttendence;//#
    private boolean needVolunteer;//#
    private String ownerID;// should be an FK id //#
    private Time time;//#
    private String location;//#
    private Map<String,Object> vols;//
    private Map<String,Object> atts;//#


    public Event(){
//      hi
    }

    public Event(String id,String title, String about, Date date, boolean isOpen,
                 boolean needAttendence, boolean needVolunteer, String onnerID, Time time,
                 String location, Map<String, Object> vols, Map<String, Object> atts) {
        this.ID = id;
        this.title = title;
        this.about = about;
        this.date = date;
        this.isOpen = isOpen;
        this.needAttendence = needAttendence;
        this.needVolunteer = needVolunteer;
        this.ownerID = onnerID;
        this.time = time;
        this.location = location;
        this.vols = vols;
        this.atts = atts;
    }
    public Map<String,Object> toMAp(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("ID", this.ID);
        result.put("title", this.title);
        result.put("about", this.about);
        result.put("date", this.date.toMap());
        result.put("isOpen", this.isOpen);
        result.put("needAttendence", this.needAttendence);
        result.put("needVolunteer", this.needVolunteer);
        result.put("ownerID", this.ownerID);
        result.put("time", this.time);
        result.put("location", this.location);
        result.put("vols",vols);
        result.put("atts",atts);
        return result;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isNeedAttendence() {
        return needAttendence;
    }

    public void setNeedAttendence(boolean needAttendence) {
        this.needAttendence = needAttendence;
    }

    public boolean isNeedVolunteer() {
        return needVolunteer;
    }

    public void setNeedVolunteer(boolean needVolunteer) {
        this.needVolunteer = needVolunteer;
    }

    public String getOnnerID() {
        return ownerID;
    }

    public void setOnnerID(String onnerID) {
        this.ownerID = onnerID;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Map<String, Object> getVols() {
        return vols;
    }

    public void setVols(Map<String, Object> vols) {
        this.vols = vols;
    }

    public Map<String, Object> getAtts() {
        return atts;
    }

    public void setAtts(Map<String, Object> atts) {
        this.atts = atts;
    }
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }
}
