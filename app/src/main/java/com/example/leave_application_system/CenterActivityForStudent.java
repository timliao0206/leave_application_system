package com.example.leave_application_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CenterActivityForStudent extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        setContentView(R.layout.centerforstudent);

        Button classSchedule = findViewById(R.id.bts1);
        Button calendar = findViewById(R.id.bts2);
        Button askLeaving = findViewById(R.id.bts3);
        Button announcement = findViewById(R.id.bts4);

        classSchedule.setText(R.string.personal_class_schedule);
        calendar.setText(R.string.important_event);
        askLeaving.setText(R.string.ask_for_leaving);
        announcement.setText(R.string.announcement);

        classSchedule.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(CenterActivityForStudent.this,ClassSchedule.class);
                startActivity(intent);
            }
        });

        calendar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(CenterActivityForStudent.this, Calendar.class);
                startActivity(intent);
            }
        });

        askLeaving.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(CenterActivityForStudent.this, ProposeLeaveActivity.class);
                startActivity(intent);
            }
        });

        announcement.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(CenterActivityForStudent.this,Announcement.class);
                startActivity(intent);
            }
        });
    }
}
