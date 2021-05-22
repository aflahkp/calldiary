package com.muod.calldiary.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CallObject extends RealmObject {
    @PrimaryKey
    public long time;
    public String title;
    public String notes;
    public String number;
    public String dueDate;
    public boolean isDone;

    public void setId(long time){
        this.time = time;
    }

    public String getDate(){
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, hh.mm aa");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            return dateFormat.format(calendar.getTime());
        }
        catch (Exception e){
            return "";
        }
    }
}
