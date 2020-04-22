package com.example.leave_application_system;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.check_attendance_action_bar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();
        if(id == R.id.next_page_in_action_bar){
            //submit the selection

            ArrayList<Integer> checkedList = getCheckedStudentId();

            Intent intent = new Intent(RollCall.this,ClassSchedule.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roll_call);

        Bitmap photo = null;
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            photo = extras.getParcelable("photo");
        }

        createStudentTable(photo);
    }

    private void createStudentTable(Bitmap photo){

        //for all student , call createStudentRow
        //change the number of totalStudent

        //tmp
        createStudentRow(null, 0, "Textview",true);
        createStudentRow(null, 1, "Textview2",false);
    }

    private void createStudentRow(Bitmap image, int id, String name , boolean defaultAttendance){

        LayoutInflater inflater = getLayoutInflater();
        View horizontal = inflater.inflate(R.layout.student_row, null);
        horizontal.setId(id);

        ImageView avastar = horizontal.findViewById(R.id.image);
        avastar.setImageBitmap(image);

        TextView name_scope = (TextView)horizontal.findViewById(R.id.student_name);
        name_scope.setText(name);

        CheckBox check = horizontal.findViewById(R.id.attendance);
        check.setChecked(defaultAttendance);

        LinearLayout fatherLayout = findViewById(R.id.studentTableLayout);
        fatherLayout.addView(horizontal);

        return;
    }

    private ArrayList<Integer> getCheckedStudentId(){

        LinearLayout tableLayout = findViewById(R.id.studentTableLayout);
        int totalStudent = tableLayout.getChildCount();

        ArrayList<Integer> checkedStudent = new ArrayList<>();

        LinearLayout linearLayout;
        for(int i=0 ; i<totalStudent ; i++){
            int resId = getResources().getIdentifier(""+i,"id",getPackageName());
            linearLayout = findViewById(resId);

            if(linearLayout == null) continue;

            LinearLayout container = (LinearLayout) linearLayout.getChildAt(0);

            for(int index = 0 ; index < container.getChildCount() ; index++){
                if(container.getChildAt(index) instanceof CheckBox){
                    if(((CheckBox) container.getChildAt(index)).isChecked()){
                        checkedStudent.add(i);
                    }
                }
            }

        }

        return checkedStudent;
    }
}
