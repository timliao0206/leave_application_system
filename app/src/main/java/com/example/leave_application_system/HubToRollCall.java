package com.example.leave_application_system;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

    static final int REQUEST_IMAGE = 1;
    Uri photo = null;

    public void takePhoto(View view){
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            return;
        }


        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent,REQUEST_IMAGE);
        }

        Intent toRollCall = new Intent(HubToRollCall.this,RollCall.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("photo",photo);
        toRollCall.putExtras(bundle);
        startActivity(toRollCall);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_IMAGE);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            photo = (Uri) extras.get(MediaStore.EXTRA_OUTPUT);
        }


    }

}
