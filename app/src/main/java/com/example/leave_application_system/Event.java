package com.example.leave_application_system;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class Event {

    public Event(){
        id = -1;
        name = "";
        time = null;
        count ++;
        tag_list.clear();
    }

    public Event(int id ,String name ,Calendar time){

        this.id = id;
        this.name = name;
        this.time = time;
        count ++;
        tag_list.clear();
    }

    public Event(String name ,Calendar time){

        this.id = count;
        this.name = name;
        this.time = time;
        count ++;
        tag_list.clear();
    }

    private static int count = 0;
    //properties
    private int id;
    private String name;
    private Calendar time;
    private View view = null;
    private ArrayList<String> tag_list = new ArrayList<>();
    private ArrayList<String> visible_tag = new ArrayList<>();

    //get func
    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public Calendar getTime(){
        return time;
    }

    //set func
    public void setView(View view){
        this.view = view;
        return;
    }

    //additional func
    public boolean addTag(String tag, Boolean visible){
        if(tag_list.contains(tag)) return false;

        tag_list.add(tag);
        if(visible) visible_tag.add(tag);
        return true;
    }

    public boolean containsTag(String tag){
        return tag_list.contains(tag);
    }

    public boolean isVisible(){
        return view.getVisibility() == View.VISIBLE;
    }

    public void setTagVisibility(String tag,boolean visibility){
        if(!containsTag(tag)) return;

        if(visibility && visible_tag.contains(tag)) return;

        if(!visibility && !visible_tag.contains(tag)) return;

        if(!visibility && visible_tag.contains(tag)) visible_tag.remove(tag);

        if(visibility && !visible_tag.contains(tag)) visible_tag.add(tag);


        if(visible_tag.size() > 0) view.setVisibility(View.VISIBLE);
        else view.setVisibility(View.GONE);

        return;
    }

}
