package com.example.leave_application_system;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ExecutionException;

public class ProposeLeaveActivity2 extends AppCompatActivity {

    String date;
    String username;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.propose_choose_class);

        date = this.getIntent().getStringExtra("date");

        GlobalVariable gv = (GlobalVariable) getApplicationContext();
        username = gv.username;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                TableLayout tl = findViewById(R.id.tb1);
                TableRow.LayoutParams tr_layout = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT);
                TableLayout.LayoutParams tl_layout = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);


                try{
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con = DriverManager.getConnection(gv.url+"roll_call_system",gv.dbuser,gv.dbpassword);



                    int id=gv.id;

                    String sql = "SELECT class.ClassName , classtime.Time , classtime.ClassTimeId from classtime inner join (class inner join studentinclass on (StudentInClassRefStudentId = ? AND class.ClassId = studentinclass.StudentInClassRefClassId )) on (class.ClassId = classtime.ClassTimeRefClassId AND classtime.Time >= " +
                            "? AND classtime.Time <= DATE_ADD( ? , INTERVAL 1 DAY))";
                    PreparedStatement stmt = con.prepareStatement(sql);
                    stmt.setInt(1,id);
                    stmt.setString(2,date);
                    stmt.setString(3,date);
                    ResultSet rs = stmt.executeQuery();

                    if(rs.next()){
                        TableRow tr = new TableRow(ProposeLeaveActivity2.this);

                        TextView tv = new TextView(ProposeLeaveActivity2.this);
                        tv.setText(R.string.class_name);
                        TextViewSetting.TableSetting(tv);
                        tv.setLayoutParams(tr_layout);
                        tr.addView(tv);

                        TextView tv2 = new TextView(ProposeLeaveActivity2.this);
                        tv2.setText(R.string.class_time);
                        TextViewSetting.TableSetting(tv2);
                        tv2.setLayoutParams(tr_layout);
                        tr.addView(tv2);

                        tr.setLayoutParams(tl_layout);
                        tl.addView(tr,tl_layout);

                        do{
                            TableRow tableRow = new TableRow(ProposeLeaveActivity2.this);
                            TextView classname = new TextView(ProposeLeaveActivity2.this);
                            TextView classtime = new TextView(ProposeLeaveActivity2.this);

                            classname.setText(rs.getString(1));
                            TextViewSetting.TableSetting(classname);
                            classname.setLayoutParams(tr_layout);

                            tableRow.addView(classname);

                            classtime.setText(rs.getString(2));
                            TextViewSetting.TableSetting(classtime);
                            classtime.setLayoutParams(tr_layout);

                            tableRow.addView(classtime);


                            int classtimeid = rs.getInt(3);
                            Button chosen = new Button(ProposeLeaveActivity2.this);
                            chosen.setOnClickListener(new Button.OnClickListener(){
                                public void onClick(View view){
                                    try{
                                        ClassChosenButtonOnClick(classtimeid);
                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            });
                            chosen.setText(R.string.choose);

                            tableRow.addView(chosen);

                            tl.addView(tableRow,tl_layout);
                        }while(rs.next());
                    }else{
                        TableRow tr = new TableRow(ProposeLeaveActivity2.this);
                        TextView tv = new TextView(ProposeLeaveActivity2.this);
                        tv.setText(R.string.no_class);
                        TextViewSetting.TableSetting(tv);
                        tv.setLayoutParams(tr_layout);

                        tr.addView(tv);

                        tr.setLayoutParams(tl_layout);

                        tl.addView(tr,tl_layout);
                    }
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
    }

    public void ClassChosenButtonOnClick(int classtimeid){
        GlobalVariable gv = (GlobalVariable) getApplicationContext();

        LayoutInflater inflater = LayoutInflater.from(ProposeLeaveActivity2.this);
        final View popView = inflater.inflate(R.layout.leave_reason,null);
        AlertDialog.Builder builder= new AlertDialog.Builder(ProposeLeaveActivity2.this);
        builder.setNegativeButton("cancel",null);
        builder.setView(popView);

        EditText et = popView.findViewById(R.id.reason);
        builder.setPositiveButton(R.string.propose, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            String sql = "insert into request (RequestRefStudentId , RequestRefClassTimeId , StudentStatement , Approval , TeacherStatement) " +
                                    "values (? , ? , ? , 0 , ?)";
                            Class.forName("com.mysql.jdbc.Driver");
                            Connection con = DriverManager.getConnection(gv.url+"roll_call_system",gv.dbuser,gv.dbpassword);
                            PreparedStatement stmt = con.prepareStatement(sql);
                            stmt.setString(1,""+gv.id);
                            stmt.setString(2,""+classtimeid);
                            stmt.setString(3,et.getText().toString());
                            stmt.setString(4,"");
                            stmt.executeUpdate();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
                try{
                    thread.join();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        AlertDialog ad = builder.create();
        ad.show();
    }
}
