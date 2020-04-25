package com.example.leave_application_system;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Itinerary extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.itinerary_layout);

        java.util.Calendar now = java.util.Calendar.getInstance();
        createCalendar(now);
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

            View week = inflater.inflate(R.layout.week_layout,null);
            week.setId(weekCount);

           /* LinearLayout.LayoutParams params =(LinearLayout.LayoutParams) week.getLayoutParams();
            params.weight = 1f;
            week.setLayoutParams(params);*/

            for(int i=0 ; i<7 ; i++) {
                FrameLayout frameLayout;
                switch (current.get(Calendar.DAY_OF_WEEK)) {
                    case 1:frameLayout = week.findViewById(R.id.Sunday);break;
                    case 2:frameLayout = week.findViewById(R.id.Monday);break;
                    case 3:frameLayout = week.findViewById(R.id.Tuesday);break;
                    case 4:frameLayout = week.findViewById(R.id.Wednesday);break;
                    case 5:frameLayout = week.findViewById(R.id.Thursday);break;
                    case 6:frameLayout = week.findViewById(R.id.Friday);break;
                    case 7:frameLayout = week.findViewById(R.id.Saturday);break;
                    default:frameLayout = null;
                }

                TextView tv = (TextView) frameLayout.getChildAt(0);
                if (month == current.get(Calendar.MONTH)) {
                    tv.setText(""+current.get(Calendar.DATE));
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setText(""+current.get(Calendar.DATE));
                    tv.setTextColor(0xFF333333);
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

}
