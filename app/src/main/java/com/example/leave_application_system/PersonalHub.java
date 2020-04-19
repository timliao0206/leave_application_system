package com.example.leave_application_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class PersonalHub extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.personalhub);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    public void personal_profile(View view){
        Intent intent = new Intent(PersonalHub.this,PersonalProfile.class);
        startActivity(intent);
    }

    public void class_schedule(View view){
        Intent intent = new Intent(PersonalHub.this, HubToRollCall.class);
        startActivity(intent);
        return;
    }

    public void leave_application(View view){
        //to do
    }

    public void itinerary(View view){
        //to do
    }

    public void announcement(View view){
        //to do
    }
}
