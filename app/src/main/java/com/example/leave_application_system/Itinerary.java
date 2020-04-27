package com.example.leave_application_system;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class Itinerary extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.itinerary_layout);

        java.util.Calendar now = java.util.Calendar.getInstance();
        createCalendar(now);

        now = Calendar.getInstance();

        addEvent(0,now,"test");
    }

    private void createCalendar(java.util.Calendar current){

        LayoutInflater inflater = getLayoutInflater();

        //this push calendar to the first day we want
        boolean flag = false;
        int month = current.get(Calendar.MONTH);
        while((!flag) || (current.get(Calendar.DAY_OF_WEEK)!=1)){
            current.add(Calendar.DATE,-1);

            if(current.get(Calendar.DATE) == 1 || current.get(Calendar.MONTH) != month) flag = true;
        }

        int weekCount = 1;
        LinearLayout table = findViewById(R.id.calendar);

        while(true){

            View week = inflater.inflate(R.layout.week_layout,table,false);
            week.setId(weekCount);

            for(int i=0 ; i<7 ; i++) {
                LinearLayout linearLayout;
                switch (current.get(Calendar.DAY_OF_WEEK)) {
                    case 1:linearLayout = week.findViewById(R.id.Sunday);break;
                    case 2:linearLayout = week.findViewById(R.id.Monday);break;
                    case 3:linearLayout = week.findViewById(R.id.Tuesday);break;
                    case 4:linearLayout = week.findViewById(R.id.Wednesday);break;
                    case 5:linearLayout = week.findViewById(R.id.Thursday);break;
                    case 6:linearLayout = week.findViewById(R.id.Friday);break;
                    case 7:linearLayout = week.findViewById(R.id.Saturday);break;
                    default:linearLayout = null;
                }

                TextView tv = (TextView) linearLayout.getChildAt(0);
                tv.setText(""+current.get(Calendar.DATE));

                if(i == 6){
                    if (month == current.get(Calendar.MONTH)) {
                        tv.setTextColor(Color.BLUE);
                    } else {
                        tv.setTextColor(0x660000FF);
                    }
                }else if(i == 0){
                    if (month == current.get(Calendar.MONTH)) {
                        tv.setTextColor(Color.RED);
                    } else {
                        tv.setTextColor(0x66FF0000);
                    }
                }else{
                    if (month == current.get(Calendar.MONTH)) {
                        tv.setTextColor(Color.BLACK);
                    } else {
                        tv.setTextColor(0x66000000);
                    }
                }


                current.add(Calendar.DATE,1);
            }

            table.addView(week);
            weekCount++;

            if( ((month+1)%12 == current.get(Calendar.MONTH)) && (current.get(Calendar.DAY_OF_WEEK) == 1) ){
                break;
            }
        }

    }

    private TextView createEventTextView(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView tv = new TextView(this);
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.WHITE);

        return tv;
    }

    private void addEvent(int eventId , Calendar date , String name){

        TextView event = createEventTextView();

        //set properties
        event.setText(name);
        String color = "RGB"+(eventId%24 +1)+"_FC";
        int colorId = getResources().getIdentifier(color,"color",getPackageName());
        event.setBackgroundResource(colorId);

        //insert into calendar
        int week = date.get(Calendar.WEEK_OF_MONTH);
        String day = "";
        switch (date.get(Calendar.DAY_OF_WEEK)){
            case 1 : day = "Sunday";break;
            case 2 : day = "Monday";break;
            case 3 : day = "Tuesday";break;
            case 4 : day = "Wednesday";break;
            case 5 : day = "Thursday";break;
            case 6 : day = "Friday";break;
            case 7 : day = "Saturday";break;
        }

        //int id = getResources().getIdentifier(""+week,"id",getPackageName());
        LinearLayout weekRow = findViewById(week);

        int dayId = getResources().getIdentifier(day,"id",getPackageName());
        LinearLayout exactDay = weekRow.findViewById(dayId);

        exactDay.addView(event);
    }

}
