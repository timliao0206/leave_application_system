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
import android.widget.CheckBox;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Itinerary extends AppCompatActivity {

    int count = 0;

    ArrayList<Event> event_list;
    Map<String,Boolean> tag_map;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.itinerary_layout);
        getSupportActionBar().hide();
        tag_map = new HashMap<>();
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setActionBar(toolbar);

        java.util.Calendar now = java.util.Calendar.getInstance();
        createCalendar(now);
        addEvent(event_list);

        tag_map.put("key1",true);
        tag_map.put("key2",false);
        tag_map.put("key3",true);

        //Notify();
    }

    public void lastPage(View view){
        Intent intent = new Intent(Itinerary.this,PersonalHub.class);
        startActivity(intent);
        return;
    }

    public void tagChangeOnClick(View view){
        LayoutInflater inflater = getLayoutInflater();
        LinearLayout pop_view = (LinearLayout) inflater.inflate(R.layout.change_tag_dialog,null);

        for(Map.Entry<String,Boolean> entry : tag_map.entrySet()){
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(entry.getKey());
            checkBox.setChecked(entry.getValue());

            //set size , weight , etc
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            checkBox.setLayoutParams(params);
            checkBox.setTextSize(20);

            pop_view.addView(checkBox);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(Itinerary.this);
        builder.setNegativeButton("cancel",null);
        builder.setPositiveButton("change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(int i = 0; i<pop_view.getChildCount(); i++){
                    CheckBox checkBox = (CheckBox) pop_view.getChildAt(i);
                    String text = checkBox.getText().toString();
                    Boolean checked = checkBox.isChecked();

                    setTagVisibility(text,checked);
                }
            }
        });
        builder.setView(pop_view);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
        Calendar start_date = (Calendar) current.clone();

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

                if(current.get(Calendar.DATE) == instance.get(Calendar.DATE) && current.get(Calendar.MONTH) == instance.get(Calendar.MONTH)){
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

        Calendar end_date = (Calendar) current.clone();

        event_list = getEvent(start_date,end_date);
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

    private ArrayList<Event> getEvent(Calendar start , Calendar end){

        ArrayList<Event> event_list = new ArrayList<>();
        event_list.clear();

        //get Event from start to end
        //while getting Event , add it's tag into tag_map and set boolean in map as true


        return event_list;
    }

    private boolean setTagVisibility(String tag ,Boolean visible){
        if(!tag_map.containsKey(tag)) return false;

        if(tag_map.get(tag).equals(visible)) return true;

        tag_map.put(tag,visible);

        for(int i = 0 ;i<event_list.size() ;i ++){
            if(!event_list.get(i).containsTag(tag)) continue;

            event_list.get(i).setTagVisibility(tag,visible);
        }

        return true;
    }

    ArrayList<String> selected_tag;

    private void dateOnClick(int date){
        LayoutInflater inflater = LayoutInflater.from(Itinerary.this);
        View pop = inflater.inflate(R.layout.add_event_dialog,null);

        TextView start = pop.findViewById(R.id.start);
        TextView end = pop.findViewById(R.id.end);

        selected_tag = new ArrayList<>();
        selected_tag.clear();

        String date_in_string = "" + (date/10000) + "-" + ((date/100)%100) + "-" + (date%100);
        start.setText(date_in_string);
        end.setText(date_in_string);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            String dateTime = String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day);
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
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            String dateTime = String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day);
                            end.setText(dateTime);
                        }

                    }, year, month, day).show();
            }
        });

        Button set_tag = pop.findViewById(R.id.set_tag);
        set_tag.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                View table = inflater.inflate(R.layout.add_tag_dialog,null);
                LinearLayout linearLayout = table.findViewById(R.id.tag_selection_display);
                for(Map.Entry<String,Boolean> entry : tag_map.entrySet()){
                    CheckBox checkBox = new CheckBox(Itinerary.this);
                    checkBox.setText(entry.getKey());
                    checkBox.setChecked(false);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    checkBox.setLayoutParams(params);
                    checkBox.setTextSize(20);

                    linearLayout.addView(checkBox);
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(Itinerary.this);
                builder.setNegativeButton("cancel",null);
                builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i = 0; i<linearLayout.getChildCount() ; i++){
                            if(linearLayout.getChildAt(i) instanceof CheckBox){
                                if(((CheckBox)linearLayout.getChildAt(i)).isChecked())
                                    selected_tag.add(((CheckBox) linearLayout.getChildAt(i)).getText().toString());
                            }
                        }
                    }
                });

                Button add_tag = table.findViewById(R.id.create_new_tag);
                View dialog_view = inflater.inflate(R.layout.create_tag_dialog,null);
                add_tag.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder add_tag_builder = new AlertDialog.Builder(Itinerary.this);
                        add_tag_builder.setNegativeButton("cancel",null);
                        add_tag_builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText et = dialog_view.findViewById(R.id.new_tag_name);
                                String tag_name = et.getText().toString();
                                tag_map.put(tag_name,true);


                                //add to previous dialog
                                CheckBox checkBox = new CheckBox(Itinerary.this);
                                checkBox.setText(tag_name);
                                checkBox.setChecked(true);

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                checkBox.setLayoutParams(params);
                                checkBox.setTextSize(20);

                                linearLayout.addView(checkBox);
                            }
                        });
                        add_tag_builder.setView(dialog_view);

                        AlertDialog ad = add_tag_builder.create();
                        ad.show();
                    }
                });

                builder.setView(table);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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

                            int id = -1;
                            //need to add into somewhere in db
                            for(; start.before(end) || start.equals(end); start.add(Calendar.DATE,1)) {
                                if(id == -1) {
                                    Event event = new Event(et.getText().toString(), start);
                                    id = event.getId();
                                    for(int i=0 ; i<selected_tag.size() ; i++) event.addTag(selected_tag.get(i), tag_map.get(selected_tag.get(i)) );
                                    addEvent(event);
                                }else{
                                    Event event = new Event(id,et.getText().toString(),start);
                                    for(int i=0 ; i<selected_tag.size() ; i++) event.addTag(selected_tag.get(i), tag_map.get(selected_tag.get(i)) );
                                    addEvent(event);
                                }
                            }
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

    /*private ArrayList<String> createTagSelectionDialogAndReturnTags(){

    }*/


    private void addEvent(Event eve){

        int eventId = eve.getId();
        String name = eve.getName();
        Calendar start_date = eve.getTime();

        //insert into calendar

        TextView event = createEventTextView();

        eve.setView(event);

        //set properties
        event.setText(name);
        String color = "RGB"+(eventId%24 +1)+"_FC";
        int colorId = getResources().getIdentifier(color,"color",getPackageName());
        event.setBackgroundResource(colorId);

        Calendar date = start_date;

        int id = date.get(Calendar.DATE) + date.get(Calendar.MONTH)*100 + 100 + date.get(Calendar.YEAR)*10000;
        LinearLayout exactDay = findViewById(id);

        if(exactDay != null) {
            exactDay.addView(event);
            event_list.add(eve);
        }

    }

    private void addEvent(ArrayList<Event> eve){

        for(int i = 0 ; i<eve.size(); i++) {
            int eventId = eve.get(i).getId();
            String name = eve.get(i).getName();
            Calendar start_date = eve.get(i).getTime();

            //insert into calendar

            TextView event = createEventTextView();

            eve.get(i).setView(event);

            //set properties
            event.setText(name);
            String color = "RGB" + (eventId % 24 + 1) + "_FC";
            int colorId = getResources().getIdentifier(color, "color", getPackageName());
            event.setBackgroundResource(colorId);

            Calendar date = start_date;

            int id = date.get(Calendar.DATE) + date.get(Calendar.MONTH) * 100 + 100 + date.get(Calendar.YEAR) * 10000;
            LinearLayout exactDay = findViewById(id);

            if (exactDay != null) {
                exactDay.addView(event);
                event_list.add(eve.get(i));
            }
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
