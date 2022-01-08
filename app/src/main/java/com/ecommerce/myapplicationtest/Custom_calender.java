package com.ecommerce.myapplicationtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
import org.naishadhparmar.zcustomcalendar.Property;

import java.util.Calendar;
import java.util.HashMap;

public class Custom_calender extends AppCompatActivity {
    CustomCalendar customCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_calender);
        customCalendar=findViewById(R.id.calender_id);

        //initialization description hash map
        HashMap<Object, Property> deshhashmap=new HashMap<>();

        //initialization default property
        Property defaultproperty=new Property();

        defaultproperty.layoutResource=R.layout.default_view;

        //initial and assign variable
        defaultproperty.dateTextViewResource=R.id.text_view_id;
        //put object and property
        deshhashmap.put("default",defaultproperty);

        //for current date
        Property current_Property=new Property();
        current_Property.layoutResource=R.layout.current_view;
        current_Property.dateTextViewResource=R.id.text_view_id;
        deshhashmap.put("current",current_Property);

        //for present date
        Property present_property=new Property();
        present_property.layoutResource=R.layout.present_view;
        present_property.dateTextViewResource=R.id.text_view_id;
        deshhashmap.put("present",present_property);

        //for absent date
        Property absent_property=new Property();
        absent_property.layoutResource=R.layout.absent_view;
        absent_property.dateTextViewResource=R.id.text_view_id;
        deshhashmap.put("absent",absent_property);

        //set desh hash map on custom calender
        customCalendar.setMapDescToProp(deshhashmap);

        //initial date hash map
        HashMap<Integer,Object> hashMap=new HashMap<>();

        //initial calender
        Calendar calendar=Calendar.getInstance();
        //put values
        hashMap.put(calendar.get(Calendar.DAY_OF_MONTH),"current");
        hashMap.put(1,"present");
        hashMap.put(2,"absent");
        hashMap.put(3,"present");
        hashMap.put(4,"absent");
        hashMap.put(20,"present");
        hashMap.put(30,"absent");

        //set date
        customCalendar.setDate(calendar,hashMap);
        customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
                String date=selectedDate.get(Calendar.DAY_OF_MONTH)
                        +"/"+(selectedDate.get(Calendar.MONTH)+1)
                        +"/"+selectedDate.get(Calendar.YEAR);
                Toast.makeText(Custom_calender.this, "Date: "+date, Toast.LENGTH_SHORT).show();
            }
        });

    }
}