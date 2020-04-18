package com.example.leave_application_system;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LeaveApplicationTeacher extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_application_teacher);

        GlobalVariable gv = (GlobalVariable) getApplicationContext();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                TableRow.LayoutParams tr_layout = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT);
                TableLayout.LayoutParams tl_layout = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);

                TableLayout table = findViewById(R.id.tb2);
                try{
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con = DriverManager.getConnection(gv.url+"roll_call_system" , gv.dbuser , gv.dbpassword);


                    //select all request from student in this teacher's class
                    String sql = "select student.StudentName , student.StudentUserName , class.ClassName , request.StudentStatement , classtime.Time , student.StudentId , classtime.ClassTimeId from student inner join" +
                            " ( request inner join (classtime inner join (class inner join teacherinclass on (TeacherInClassRefTeacherId = ? AND ClassId = TeacherInClassRefClassId )) on ClassId = ClassTimeRefClassId )" +
                            " on (RequestRefClassTimeId = ClassTimeId AND Approval = 0 ) ) on (StudentId = RequestRefStudentId) ";

                    PreparedStatement stmt = con.prepareStatement(sql);
                    stmt.setString(1,""+gv.id);
                    ResultSet rs = stmt.executeQuery();
                    if(rs.next()){
                        TableRow row = new TableRow(LeaveApplicationTeacher.this);

                        int[] list = {R.string.student_name,R.string.student_username,R.string.class_name,R.string.reason_of_leaving,R.string.class_time};

                        //create table
                        for(int i=0 ; i<list.length ; i++) {
                            TextView topic = new TextView(LeaveApplicationTeacher.this);
                            topic.setText(list[i]);
                            TextViewSetting.TableSetting(topic);
                            topic.setLayoutParams(tr_layout);
                            row.addView(topic);
                        }

                        row.setLayoutParams(tl_layout);
                        table.addView(row,tl_layout);

                        do{
                            TableRow tableRow = new TableRow(LeaveApplicationTeacher.this);

                            for(int i =0 ; i<list.length ; i++) {
                                TextView tv = new TextView(LeaveApplicationTeacher.this);
                                tv.setText(rs.getString(i+1));
                                TextViewSetting.TableSetting(tv);
                                tv.setLayoutParams(tr_layout);
                                tableRow.addView(tv);
                            }

                            int id= rs.getInt("StudentId");
                            int classtimeid = rs.getInt("ClassTimeId");

                            Button bt = new Button(LeaveApplicationTeacher.this);
                            bt.setText(R.string.check);
                            bt.setOnClickListener(new Button.OnClickListener(){
                                public void onClick(View view){
                                    //when the right-hand-side button is clicked
                                    check(id,classtimeid);
                                }
                            });
                            tableRow.addView(bt);

                            tableRow.setLayoutParams(tl_layout);
                            table.addView(tableRow,tl_layout);

                        }while (rs.next());
                        rs.close();
                        stmt.close();
                        con.close();
                    }else{
                        TableRow tr = new TableRow(LeaveApplicationTeacher.this);
                        TextView tv = new TextView(LeaveApplicationTeacher.this);
                        tv.setText(R.string.no_request);
                        TextViewSetting.TableSetting(tv);
                        tv.setLayoutParams(tr_layout);
                        tr.addView(tv);
                        tr.setLayoutParams(tl_layout);
                        table.addView(tr,tl_layout);
                    }

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

    public void check(int id, int classtimeid){
        GlobalVariable gv = (GlobalVariable) getApplicationContext();
        //pop a view
        AlertDialog.Builder builder = new AlertDialog.Builder(LeaveApplicationTeacher.this);
        builder.setTitle(R.string.agree_or_no);
        builder.setNegativeButton("cancel",null);
        builder.setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //set approval to 1
                            String sql = "UPDATE request SET Approval = 1 WHERE RequestRefStudentId = ? AND RequestRefClassTimeId = ?";
                            Class.forName("com.mysql.jdbc.Driver");
                            Connection con = DriverManager.getConnection(gv.url+"roll_call_system",gv.dbuser,gv.dbpassword);
                            PreparedStatement stmt = con.prepareStatement(sql);
                            stmt.setInt(1,id);
                            stmt.setInt(2,classtimeid);
                            stmt.executeUpdate();
                        }catch (Exception e){
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
        });
        builder.setNeutralButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //pop a view and ask reason
                AlertDialog.Builder b = new AlertDialog.Builder(LeaveApplicationTeacher.this);
                b.setNegativeButton("cancel",null);
                b.setTitle(R.string.reason);
                EditText et = new EditText(LeaveApplicationTeacher.this);
                b.setView(et);
                b.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    String sql = "UPDATE request SET Approval = 2 , TeacherStatement = ? WHERE RequestRefStudentId = ? AND RequestRefclassTimeId = ?";
                                    Class.forName("com.mysql.jdbc.Driver");
                                    Connection con = DriverManager.getConnection(gv.url+"roll_call_system",gv.dbuser,gv.dbpassword);
                                    PreparedStatement stmt = con.prepareStatement(sql);
                                    String statement = et.getText().toString();
                                    stmt.setString(1,statement);
                                    stmt.setInt(2,id);
                                    stmt.setInt(3,classtimeid);
                                    stmt.executeUpdate();
                                }catch (Exception e){
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
                });
                AlertDialog alertDialog = b.create();
                alertDialog.show();
            }
        });

        AlertDialog ad = builder.create();
        ad.show();
    }
}
