package com.example.leave_application_system;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ClassSchedule extends AppCompatActivity {

    //previous code
    /*@Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classschedule);

        GlobalVariable gv = (GlobalVariable) getApplicationContext();

        String date;
        Date mydate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = sdf.format(mydate).substring(0,10);

        TableLayout tl = findViewById(R.id.classScheduleShow);


        getClassSchedule(date);


        Button changeDate = (Button) findViewById(R.id.changeDate);

        changeDate.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(ClassSchedule.this);
                final View popView = inflater.inflate(R.layout.switchdate,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(ClassSchedule.this);
                builder.setView(popView);
                builder.setPositiveButton(R.string.select, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextView text = popView.findViewById(R.id.currentSelect);
                        String selected = text.getText().toString();
                        Thread thread1 = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                getClassSchedule(selected);
                            }
                        });

                        try{
                            thread1.start();
                            thread1.join();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });

                builder.setNegativeButton("cancel",null);

                TextView textView = popView.findViewById(R.id.currentSelect);
                textView.setText(date);

                Button lastWeek = popView.findViewById(R.id.lastWeek);
                Button yesterday = popView.findViewById(R.id.yesterday);
                Button tomorrow = popView.findViewById(R.id.tomorror);
                Button nextWeek = popView.findViewById(R.id.nextWeek);

                lastWeek.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        try {
                            String curr = textView.getText().toString();
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            LocalDate ld = LocalDate.parse(curr,dtf);
                            ld = ld.minusDays(7);
                            textView.setText(ld.toString());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });

                yesterday.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        try {
                            String curr = textView.getText().toString();
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            LocalDate ld = LocalDate.parse(curr,dtf);
                            ld = ld.minusDays(1);
                            textView.setText(ld.toString());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });

                tomorrow.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        try {
                            String curr = textView.getText().toString();
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            LocalDate ld = LocalDate.parse(curr,dtf);
                            ld = ld.plusDays(1);
                            textView.setText(ld.toString());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });

                nextWeek.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        try {
                            String curr = textView.getText().toString();
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            LocalDate ld = LocalDate.parse(curr,dtf);
                            ld = ld.plusDays(7);
                            textView.setText(ld.toString());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });

                AlertDialog ad = builder.create();
                ad.show();
            }
        });
    }

    boolean empty = false;



    void getClassSchedule(String date){
        TableRow.LayoutParams tr_layout = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT);
        TableLayout.LayoutParams tl_layout = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);

        GlobalVariable gv = (GlobalVariable) getApplicationContext();

        ArrayList<String> ClassName = new ArrayList<String>();
        ArrayList<String> Time = new ArrayList<String>();
        ArrayList<Integer> ClassTimeId = new ArrayList<Integer>();

        ClassName.clear();
        Time.clear();
        ClassTimeId.clear();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con = DriverManager.getConnection(gv.url + "roll_call_system", gv.dbuser, gv.dbpassword);
                    int id = gv.id;

                    String sql = "SELECT class.ClassName , classtime.Time , classtime.ClassTimeId from classtime inner join (class inner join studentinclass on (StudentInClassRefStudentId = ? AND class.ClassId = studentinclass.StudentInClassRefClassId )) on (class.ClassId = classtime.ClassTimeRefClassId AND classtime.Time >= " +
                            "? AND classtime.Time <= DATE_ADD( ? , INTERVAL 1 DAY))";
                    PreparedStatement stmt = con.prepareStatement(sql);
                    stmt.setInt(1, id);
                    stmt.setString(2, date);
                    stmt.setString(3, date);
                    ResultSet rs = stmt.executeQuery();
                    empty = !rs.next();

                    if(empty) return;
                    do{
                        ClassName.add(rs.getString("ClassName"));
                        Time.add(rs.getString("Time"));
                        ClassTimeId.add(rs.getInt("ClassTimeId"));
                    }while(rs.next());

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try{
            thread.join();
        }catch(Exception e){
            e.printStackTrace();
        }

        TableLayout tl = findViewById(R.id.classScheduleShow);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tl.removeAllViews();
            }
        });
        if(!empty){
            TableRow tr = new TableRow(ClassSchedule.this);
            TextView tv = new TextView(ClassSchedule.this);
            tv.setText(R.string.class_name);
            TextViewSetting.TableSetting(tv);
            tv.setLayoutParams(tr_layout);
            tr.addView(tv);

            TextView tv2 = new TextView(ClassSchedule.this);
            tv2.setText(R.string.class_time);
            TextViewSetting.TableSetting(tv2);
            tv2.setLayoutParams(tr_layout);
            tr.addView(tv2);

            tr.setLayoutParams(tl_layout);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tl.addView(tr,tl_layout);
                }
            });

            for(int i=0 ; i<ClassName.size() ; i++){
                TableRow tableRow = new TableRow(ClassSchedule.this);
                TextView classname = new TextView(ClassSchedule.this);
                TextView classtime = new TextView(ClassSchedule.this);

                classname.setText(ClassName.get(i));
                TextViewSetting.TableSetting(classname);
                classname.setLayoutParams(tr_layout);

                tableRow.addView(classname);

                classtime.setText(Time.get(i));
                TextViewSetting.TableSetting(classtime);
                classtime.setLayoutParams(tr_layout);

                tableRow.addView(classtime);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tl.addView(tableRow,tl_layout);
                    }
                });
            }
        }else{
            TableRow tr = new TableRow(ClassSchedule.this);
            TextView tv = new TextView(ClassSchedule.this);
            tv.setText(R.string.no_class);
            TextViewSetting.TableSetting(tv);
            tv.setLayoutParams(tr_layout);

            tr.addView(tv);

            tr.setLayoutParams(tl_layout);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //tl.removeAllViews();
                    tl.addView(tr,tl_layout);
                }
            });
        }
    }*/

    private int gridHeight,gridWidth;
    private RelativeLayout layout;
    private RelativeLayout tmpLayout;
    private static boolean isFirst = true;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamic_class_schedule);
        tmpLayout = (RelativeLayout) findViewById(R.id.Monday);

        tmpLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tmpLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                gridHeight=tmpLayout.getHeight()/12;
                gridWidth=tmpLayout.getWidth();

                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                createSchedule(simpleDateFormat.format(date).toString());
            }
        });

        LinearLayout weekScroll = findViewById(R.id.weekSelect);
        Date date_ = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date_);
        for(int i=0 ; i<52 ; i++){
            calendar.add(Calendar.DATE,7);
            int month = calendar.get(Calendar.MONTH);
            int date = calendar.get(Calendar.DATE);

            TextView newText = new TextView(this);
            newText.setText(month+1+"/"+date);
            newText.setClickable(true);
            newText.setLayoutParams(new LinearLayout.LayoutParams(100,100));


            final String index = calendar.get(Calendar.YEAR)+"-"+month+1+"-"+date+" 00:00:00";
            newText.setOnClickListener(new TextView.OnClickListener(){
                @Override
                public void onClick(View view){
                    createSchedule(index);
                }
            });

            weekScroll.addView(newText);
        }



    }

    //call this func to create a view for this layout
    private TextView createTv(int start,int end,String text){
        TextView tv = new TextView(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridWidth,gridHeight*(end-start+1));

        tv.setY(gridHeight*(start-1));
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setText(text);
        return tv;
    }

    //i = day , start = start time , end = end time , text = the name of class , color (1~24) = the background color of the schedule
    private void addView(int i,int start,int end,String text,int color){
        TextView tv;
        switch (i){
            case 1:
                layout = (RelativeLayout) findViewById(R.id.Monday);
                break;
            case 2:
                layout = (RelativeLayout) findViewById(R.id.Tuesday);
                break;
            case 3:
                layout = (RelativeLayout) findViewById(R.id.Wednesday);
                break;
            case 4:
                layout = (RelativeLayout) findViewById(R.id.Thursday);
                break;
            case 5:
                layout = (RelativeLayout) findViewById(R.id.Friday);
                break;
            case 6:
                layout = (RelativeLayout) findViewById(R.id.Saturday);
                break;
            case 7:
                layout = (RelativeLayout) findViewById(R.id.Sunday);
                break;
        }
        tv = createTv(start,end,text);

        View topLine = new View(this);

        topLine.setLayoutParams(new RelativeLayout.LayoutParams(gridWidth,20));
        topLine.setY(gridHeight*(start-1));

        String RGBID = "RGB"+(color%24+1);
        int colorId = getResources().getIdentifier(RGBID,"color",getPackageName());

        tv.setBackgroundColor(ContextCompat.getColor(this,colorId));
        topLine.setBackgroundColor(ContextCompat.getColor(this,colorId));

        layout.addView(tv);
        layout.addView(topLine);
    }

    //index is the first day of the week
    private void createSchedule(String date) {

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
            Date thisDate = simpleDateFormat.parse(date);
            java.util.Calendar calendar = Calendar.getInstance();
            calendar.setTime(thisDate);

            for (int i = 0; i < 7; i++) {
                int month = calendar.get(Calendar.MONTH)+1;
                int date_of_month = calendar.get(Calendar.DATE);

                TextView tv;
                switch (i){
                    case 0:tv = findViewById(R.id.firstDay);break;
                    case 1:tv = findViewById(R.id.secondDay);break;
                    case 2:tv = findViewById(R.id.thirdDay);break;
                    case 3:tv = findViewById(R.id.forthDay);break;
                    case 4:tv = findViewById(R.id.fifthDay);break;
                    case 5:tv = findViewById(R.id.sixthDay);break;
                    case 6:tv = findViewById(R.id.seventhDay);break;
                    default:tv = findViewById(R.id.firstDay);
                }

                tv.setText(month+"/"+date_of_month);

                calendar.add(calendar.DATE,1);
            }
        }catch(Exception e){
            e.printStackTrace();
        }


        //schedule create here
        //get from sql

        addView(1, 3, 8, "try", 8);
        addView(5, 6, 7, "try", 17);
        addView(4, 4, 6, "try", 21);

    }

}

