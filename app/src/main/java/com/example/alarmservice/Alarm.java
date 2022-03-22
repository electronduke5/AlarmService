package com.example.alarmservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;
import android.webkit.HttpAuthHandler;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.security.KeyStore;
import java.time.LocalDateTime;
import java.util.Date;

public class Alarm extends BroadcastReceiver {

    /*
    Метод срабатывает при окончании работы таймера
    Выводит сообщение и срабатывает вибриация (1000 = 1 сек)
     */

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ВРЕМЯ", "Будильник сработал в " + LocalDateTime.now());
        Toast.makeText(context, "Будильник сработал!", Toast.LENGTH_LONG).show();
        Vibrator vibrator = (Vibrator) context. getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);
    }
}
