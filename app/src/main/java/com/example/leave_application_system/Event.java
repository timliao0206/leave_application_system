package com.example.leave_application_system;

import java.util.Calendar;

public class Event {

    public Event(){
        id = -1;
        name = "";
        start_time = null;
        end_time = null;
        count ++;
    }

    public Event(int id ,String name ,Calendar start_time ,Calendar end_time){
        if(start_time.before(end_time) || start_time.equals(end_time)) {
            //if start time is before end time
            this.id = id;
            this.name = name;
            this.start_time = start_time;
            this.end_time = end_time;
            count ++;
        }else{
            //if start time is after end time
            this.id = -1;
            this.name = "";
            this.start_time = null;
            this.end_time = null;
        }
    }

    public Event(String name ,Calendar start_time ,Calendar end_time){
        if(start_time.before(end_time) || start_time.equals(end_time)) {
            //if start time is before end time
            this.id = count;
            this.name = name;
            this.start_time = start_time;
            this.end_time = end_time;
            count ++;
        }else{
            //if start time is after end time
            this.id = -1;
            this.name = "";
            this.start_time = null;
            this.end_time = null;
        }
    }

    private static int count = 0;
    //properties
    private int id;
    private String name;
    private Calendar start_time;
    private Calendar end_time;


    //get func
    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public Calendar getStartTime(){
        return start_time;
    }

    public Calendar getEndTime(){
        return end_time;
    }
}
