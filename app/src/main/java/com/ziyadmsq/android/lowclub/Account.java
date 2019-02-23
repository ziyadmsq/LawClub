package com.ziyadmsq.android.lowclub;

import java.util.HashMap;
import java.util.Map;

public class Account {
    private String firebaseID;
    private boolean permit;
    private String position;
    private String accType;//
    private String name;//
    private int numOfHours;
    private String phone;
    private int KSUID;
    private String pass;//
    private Map<String, Object> myEve;
    private Map<String, Object> myJoin;

    public Account() {
//        this.permit = false;
//                this.position = "&";
//        this.accType = ac
//        this.name = name;
//        this.numOfHours =
//                this.phone = phon
//        this.ksuId = ksuI
//        this.pass = passW
//        this.myEvents = m
//        this.myJoin = myJ
//        this.firebaseID =
    }

    public Account(String firebaseID, boolean permit, String position, String accType, String name, int numOfHours,
                   String phone, int KSUID, String pass, Map<String, Object> myEve, Map<String, Object> myJoin) {
        this.permit = permit;
        this.position = position;
        this.accType = accType;
        this.name = name;
        this.numOfHours = numOfHours;
        this.phone = phone;
        this.KSUID = KSUID;
        this.pass = pass;
        this.myEve = myEve;
        this.myJoin = myJoin;
        this.firebaseID = firebaseID;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("accType", this.accType);//
        result.put("firebaseID", this.firebaseID);//
        result.put("phone", this.phone);//
        result.put("KSUID", this.KSUID);
        result.put("name", this.name);//
        result.put("numOfHours", this.numOfHours);//
        result.put("pass", this.pass);//
        result.put("permit", this.permit);//
        result.put("position", this.position);//
        result.put("myJoin", myJoin);//
        result.put("myEve", myEve);//

        return result;
    }

    public boolean isPermit() {
        return permit;
    }

    public void setPermit(boolean permit) {
        this.permit = permit;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumOfHours() {
        return numOfHours;
    }

    public void setNumOfHours(int numOfHours) {
        this.numOfHours = numOfHours;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getKsuId() {
        return KSUID;
    }

    public void setKsuId(int ksuId) {
        this.KSUID = ksuId;
    }

    public String getPassWord() {
        return pass;
    }

    public void setPassWord(String passWord) {
        this.pass = passWord;
    }

    public Map<String, Object> getMyEvents() {
        return myEve;
    }

    public void setMyEvents(Map<String, Object> myEvents) {
        this.myEve = myEvents;
    }

    public Map<String, Object> getMyJoin() {
        return myJoin;
    }

    public void setMyJoin(Map<String, Object> myJoin) {
        this.myJoin = myJoin;
    }

    public String getFirebaseID() {
        return firebaseID;
    }

}
