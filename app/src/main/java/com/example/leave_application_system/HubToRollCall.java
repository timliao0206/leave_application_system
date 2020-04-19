package com.example.leave_application_system;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class HubToRollCall extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.hub_to_roll_call);


    }

    public void manualRollCall(){

    }

    public void goToSchedule(){
        Intent intent = new Intent(HubToRollCall.this,ClassSchedule.class);
        startActivity(intent);
    }

    public void takePhoto(){

    }

}
