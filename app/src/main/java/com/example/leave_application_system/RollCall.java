package com.example.leave_application_system;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RollCall extends AppCompatActivity {

    ArrayList<Integer> studentId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roll_call);

        createStudentRow(null,"name",true);
    }

    private void createStudentTable(){

        //for all student , call createStudentRow
    }

    private void createStudentRow(Bitmap image , String name , boolean defaultAttendance){

        LinearLayout horizontal = new LinearLayout(this);
        ImageView avastar = new ImageView(this);
        TextView textView = new TextView(this);
        CheckBox checkBox = new CheckBox(this);

        LinearLayout.LayoutParams imageParam = new LinearLayout.LayoutParams(20,20);
        LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams(100,100);
        LinearLayout.LayoutParams checkParam = new LinearLayout.LayoutParams(20,20);

        LinearLayout fatherView = findViewById(R.id.studentTableLayout);
        //set LL attribute
        fatherView.setGravity(Gravity.CENTER);
        fatherView.addView(horizontal);

        //get image and set attribute
        avastar.setImageBitmap(image);
        avastar.setLayoutParams(imageParam);

        //get name and set attribute
        textView.setText(name);
        textView.setLayoutParams(textParam);
        textView.setTextSize(18);
        textView.setGravity(Gravity.CENTER);

        //get checkbox and set attribute
        checkBox.setChecked(defaultAttendance);
        checkBox.setLayoutParams(checkParam);

        horizontal.addView(avastar);
        horizontal.addView(textView);
        horizontal.addView(checkBox);

        return;

    }
}
