package com.example.leave_application_system;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Itinerary extends AppCompatActivity {

    int count = 0;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.itinerary_layout);
        getSupportActionBar().hide();
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setActionBar(toolbar);

        java.util.Calendar now = java.util.Calendar.getInstance();
        createCalendar(now);

        //Notify();
    }

    private void createCalendar(java.util.Calendar current){

        //clear calendar
        LinearLayout root = findViewById(R.id.calendar);
        for(int i=0 ; i<root.getChildCount() ; i++){
            LinearLayout child = (LinearLayout) root.getChildAt(i);
            for(int j=0 ; j<child.getChildCount() ; j++){
                if(child.getChildAt(j) instanceof LinearLayout){
                    child.removeAllViews();
                    root.removeViewAt(i);
                    i--;
                    break;
                }
            }
        }

        LayoutInflater inflater = getLayoutInflater();

        Calendar instance = Calendar.getInstance();
        TextView topic = findViewById(R.id.this_month);

        //this push calendar to the first day we want
        boolean flag = false;
        int month = current.get(Calendar.MONTH);
        topic.setText(""+(month+1)+"月");

        while((!flag) || (current.get(Calendar.DAY_OF_WEEK)!=1)){
            current.add(Calendar.DATE,-1);

            if(current.get(Calendar.DATE) == 1 || current.get(Calendar.MONTH) != month) flag = true;
        }

        int weekCount = 1;
        LinearLayout table = findViewById(R.id.calendar);

        while(true){

            View week = inflater.inflate(R.layout.week_layout,table,false);
            week.setId(weekCount);

            for(int i=0 ; i<7 ; i++) {
                LinearLayout linearLayout;
                switch (current.get(Calendar.DAY_OF_WEEK)) {
                    case 1:linearLayout = week.findViewById(R.id.Sunday);break;
                    case 2:linearLayout = week.findViewById(R.id.Monday);break;
                    case 3:linearLayout = week.findViewById(R.id.Tuesday);break;
                    case 4:linearLayout = week.findViewById(R.id.Wednesday);break;
                    case 5:linearLayout = week.findViewById(R.id.Thursday);break;
                    case 6:linearLayout = week.findViewById(R.id.Friday);break;
                    case 7:linearLayout = week.findViewById(R.id.Saturday);break;
                    default:linearLayout = null;
                }

                final int id = current.get(Calendar.DATE) + current.get(Calendar.MONTH)*100 + 100 + current.get(Calendar.YEAR)*10000;
                linearLayout.setId(id);
                linearLayout.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        dateOnClick(id);
                    }
                });

                TextView tv = (TextView) linearLayout.getChildAt(0);
                tv.setText(""+current.get(Calendar.DATE));
                if(current.equals(instance)){
                    tv.setBackgroundColor(0x6600A0C1);
                }
                if(i == 6){
                    if (month == current.get(Calendar.MONTH)) {
                        tv.setTextColor(Color.BLUE);
                    } else {
                        tv.setTextColor(0x660000FF);
                    }
                }else if(i == 0){
                    if (month == current.get(Calendar.MONTH)) {
                        tv.setTextColor(Color.RED);
                    } else {
                        tv.setTextColor(0x66FF0000);
                    }
                }else{
                    if (month == current.get(Calendar.MONTH)) {
                        tv.setTextColor(Color.BLACK);
                    } else {
                        tv.setTextColor(0x66000000);
                    }
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

    public void lastMonth(View view){
        count--;
        Calendar createdtime = Calendar.getInstance();
        createdtime.add(Calendar.MONTH,count);

        createCalendar(createdtime);
    }

    public void nextMonth(View view){
        count++;
        Calendar createdtime = Calendar.getInstance();
        createdtime.add(Calendar.MONTH,count);

        createCalendar(createdtime);
    }

    private TextView createEventTextView(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tv = new TextView(this);
        tv.setLayoutParams(params);
        tv.setMaxLines(1);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.WHITE);

        return tv;
    }

    private void dateOnClick(int date){
        LayoutInflater inflater = LayoutInflater.from(Itinerary.this);
        View pop = inflater.inflate(R.layout.add_event_dialog,null);

        TextView start = pop.findViewById(R.id.start);
        TextView end = pop.findViewById(R.id.end);

        String date_in_string = "" + (date/10000) + "-" + ((date/100)%100) + "-" + (date%100);
        start.setText(date_in_string);
        end.setText(date_in_string);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) +1;
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            String dateTime = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);
                            start.setText(dateTime);
                        }

                    }, year, month, day).show();
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) +1;
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            String dateTime = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);
                            end.setText(dateTime);
                        }

                    }, year, month, day).show();
            }
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Itinerary.this);
        alertDialogBuilder.setNegativeButton("cancel",null);
        alertDialogBuilder.setView(pop);
        alertDialogBuilder.setPositiveButton("add", null);

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Button button = ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        try {
                            EditText et = pop.findViewById(R.id.topic);

                            if(et.getText().toString().equals("")){
                                Toast.makeText(Itinerary.this,"標題不可為空",Toast.LENGTH_LONG).show();
                                return;
                            }

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                            Date start_date = sdf.parse(start.getText().toString());
                            Date end_date = sdf.parse(end.getText().toString());

                            Calendar start = Calendar.getInstance();
                            start.setTime(start_date);

                            Calendar end = Calendar.getInstance();
                            end.setTime(end_date);

                            if(start.after(end)){
                                Toast.makeText(Itinerary.this,"日期有誤",Toast.LENGTH_LONG).show();
                                return;
                            }

                            //need to add into somewhere in db
                            Event event = new Event(et.getText().toString(),start,end);
                            addEvent(event);

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        dialog.dismiss();
                    }
                });
            }
        });

        alertDialog.show();
    }


    private void addEvent(Event eve){

        int eventId = eve.getId();
        String name = eve.getName();
        Calendar start_date = eve.getStartTime();
        Calendar end_date = eve.getEndTime();

        if(start_date == null || end_date == null) return;

        //insert into calendar
        for(;start_date.before(end_date) || start_date.equals(end_date) ; start_date.add(Calendar.DATE,1)) {

            TextView event = createEventTextView();

            String tv_tag = "" + eve.getId() +" "+start_date.toString();
            event.setTag(tv_tag);
            eve.subview_tag.add(tv_tag);

            //set properties
            event.setText(name);
            String color = "RGB"+(eventId%24 +1)+"_FC";
            int colorId = getResources().getIdentifier(color,"color",getPackageName());
            event.setBackgroundResource(colorId);

            Calendar date = start_date;

            int id = date.get(Calendar.DATE) + date.get(Calendar.MONTH)*100 + 100 + date.get(Calendar.YEAR)*10000;
            LinearLayout exactDay = findViewById(id);

            if(exactDay != null)
                exactDay.addView(event);

        }
    }

    public void Notify() {
        int NOTIFICATION_ID = 234;
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "my_channel_01";
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel_01")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("title")
                .setContentText("message");

        Intent resultIntent = new Intent(this, Itinerary.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
