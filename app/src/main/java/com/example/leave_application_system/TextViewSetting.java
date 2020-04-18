package com.example.leave_application_system;

import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

public class TextViewSetting {
    public static void TableSetting(TextView tv){
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(20);
        tv.setTextColor(Color.BLACK);
        tv.setBackgroundColor(Color.WHITE);
    }
}
