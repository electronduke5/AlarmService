package com.example.alarmservice;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    TextView txtTime;
    Button btnStartTimer;
    Button btnSelectTime;
    Calendar dateTime = Calendar.getInstance();
    int hours;
    int minutes;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTime = findViewById(R.id.txtTime);
        btnStartTimer = findViewById(R.id.btnStartTimer);
        btnSelectTime = findViewById(R.id.btnSelectTime);

        btnSelectTime.setOnClickListener(view ->{
            new TimePickerDialog(MainActivity.this, timeSetListener,
                    dateTime.get(Calendar.HOUR_OF_DAY),
                    dateTime.get(Calendar.MINUTE), true)
                    .show();
        });

        btnStartTimer.setOnClickListener(view -> {

            if(LocalDateTime.now().getHour() >= hours
                    && LocalDateTime.now().getMinute() >= minutes){
                hours +=24;
            }

            long millis = convertTimeToMilli(hours, minutes) - convertTimeToMilli(LocalDateTime.now().getHour(), LocalDateTime.now().getMinute());
            Log.d("ВРЕМЯ", "Милисек: " + millis);
            Log.d("ВРЕМЯ", "Таймер срабатывающий в: " +new Date(Instant.now().truncatedTo(ChronoUnit.MINUTES).toEpochMilli() + millis) + ", через " +  millis / 60000d    +" minutes");

            //При помощи класса Intent обращаемся к логике класса Alarm
            Intent intent = new Intent(MainActivity.this, Alarm.class);

            //Обращение к службе AlarmService осуществляется при помощи объекта AlarmManager
            //Служба Alarm Service используется для отправки пользователю разовых или повторяющихся
            //сообщений в заданное время
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            //Срабатывание AlarmManager
            alarmManager.set(AlarmManager.RTC_WAKEUP, Instant.now().truncatedTo(ChronoUnit.MINUTES).toEpochMilli() +  millis,
                    PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0));

            Toast.makeText(MainActivity.this, "Будильник сработает" +
                    new Date(Instant.now().truncatedTo(ChronoUnit.MINUTES).toEpochMilli() + millis),
                    Toast.LENGTH_LONG).show();
        });
    }

    TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

            dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            hours = hourOfDay;

            dateTime.set(Calendar.MINUTE, minute);
            minutes = minute;

            txtTime.setText(DateUtils.formatDateTime(getApplicationContext(),
                    dateTime.getTimeInMillis(),
                    DateUtils.FORMAT_SHOW_TIME));

        }
    };

    public long convertTimeToMilli(int hours, int minutes) {
        return (60000*minutes) + (3600000*hours);
    }
}