package com.example.leave_application_system;

import android.app.ActionBar;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;

public class ProposeLeaveActivity extends AppCompatActivity {

    String date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.propose_choose_date);

        CalendarView cv = findViewById(R.id.calendarView2);


        Date mydate = new Date();//hen important , calendar will not get date if you did not change the button
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = sdf.format(mydate).substring(0,10);

        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                date = i + "-" + (i1 + 1) + "-" + i2;
                if (i1 < 9) {
                    StringBuffer buffdate = new StringBuffer();
                    buffdate.append(date);
                    buffdate.insert(7, "0");
                    date = buffdate.toString();
                }

                if (i2 < 10) {
                    StringBuffer buffdate = new StringBuffer();
                    buffdate.append(date);
                    buffdate.insert(12, "0");
                    date = buffdate.toString();
                }
            }
        });

        Button bt = findViewById(R.id.button7);
        bt.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(ProposeLeaveActivity.this,ProposeLeaveActivity2.class);
                intent.putExtra("date",date);
                startActivity(intent);
            }
        });
    }
}
