package com.localdelivery.driver.model;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

/**
 * Created by vijay on 16/11/16.
 */

public class Date_Time {
  private Context context;
    static int year,month, day,hour,minute,second;
    private EventBus bus = EventBus.getDefault();
    String  returndate,returntime;



    public Date_Time(Context applicationContext) {

        this.context = applicationContext;
    }




    public  void dateDialog(){

         Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {



                EventBus.getDefault().post(new Event(Constants.DATEEVENT, dayOfMonth +" -" +(month+1) + "-" +year));


            }
        },year,month,day);
        datePickerDialog.show();


    }
    public  String daqteDialog(){

        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {



            EventBus.getDefault().post(new Event(Constants.DATEEVENT, dayOfMonth +" -" +(month+1) + "-" +year));

            returndate = String.valueOf(dayOfMonth)+"-"+(String.valueOf(month+1))+"-"+String.valueOf(year);

                Log.e("date",returndate);

            }
        },year,month,day);
        datePickerDialog.show();

    return returndate;
    }
    public  void timedialog(){
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute =c.get(Calendar.MINUTE);
        second =c.get(Calendar.SECOND);
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String AM_PM ;
                if(hourOfDay < 12) {
                    AM_PM = "AM";
                } else {
                    AM_PM = "PM";
                }


                EventBus.getDefault().post(new Event(Constants.TIMEEVENT,hourOfDay+":" +minute +" "+AM_PM));

            }



        },hour,minute,false);
        timePickerDialog.show();
    }

    public  String timeedialog(){
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute =c.get(Calendar.MINUTE);
        second =c.get(Calendar.SECOND);
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String AM_PM ;
                if(hourOfDay < 12) {
                    AM_PM = "AM";
                } else {
                    AM_PM = "PM";
                }


                EventBus.getDefault().post(new Event(Constants.TIMEEVENT,hourOfDay+":" +minute +" "+AM_PM));
                returntime = String.valueOf(hourOfDay)+":"+String.valueOf(minute)+" "+String.valueOf(AM_PM);

            }



        },hour,minute,false);
        timePickerDialog.show();
        return returntime;
    }



}
