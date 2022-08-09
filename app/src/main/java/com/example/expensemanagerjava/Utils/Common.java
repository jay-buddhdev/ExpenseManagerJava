package com.example.expensemanagerjava.Utils;

import android.widget.DatePicker;

import com.example.expensemanagerjava.Model.User;

import java.util.Calendar;

public class Common {
    public static User currentUser;

    public static java.util.Date getDateFromDatePicker(DatePicker datePicker){
        // YYYY-MM-DD/HH-MM-SS
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

}

