package com.example.leave_application_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CenterActivityForTeacher extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        setContentView(R.layout.centerforteacher);
        Button look_propose = findViewById(R.id.btt1);
        Button unused1 = findViewById(R.id.btt2);
        Button unused2 = findViewById(R.id.btt3);

        unused1.setText(R.string.unused_button);
        unused2.setText(R.string.unused_button);

        look_propose.setText(R.string.leave_application);

        look_propose.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setClass(CenterActivityForTeacher.this,LeaveApplicationTeacher.class);
                startActivity(intent);
            }
        });
    }
}
