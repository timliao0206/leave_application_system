package com.example.leave_application_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class HubToRollCall extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.hub_to_roll_call);


    }

    public void manualRollCall(View view){
        Intent intent = new Intent(HubToRollCall.this,RollCall.class);
        startActivity(intent);
    }

    public void goToSchedule(View view){
        Intent intent = new Intent(HubToRollCall.this,ClassSchedule.class);
        startActivity(intent);
    }

    public void takePhoto(View view){

    }

}
