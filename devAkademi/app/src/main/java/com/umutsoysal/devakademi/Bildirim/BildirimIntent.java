package com.umutsoysal.devakademi.Bildirim;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;

import com.umutsoysal.devakademi.MainActivity;
import com.umutsoysal.devakademi.R;

import java.util.Timer;
import java.util.TimerTask;


public class BildirimIntent extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent ıntent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();



    }

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private Timer mTimer;

    TimerTask timerTask=new TimerTask() {
        @Override
        public void run() {
            Log.e("log","running");
            bildirimAt();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void bildirimAt()
    {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(BildirimIntent.this)
                        .setSmallIcon(R.drawable.coin)
                        .setContentTitle(Html.fromHtml("<b>SCOINS Turkey</b>"))
                        .setContentText("bu bir denemedir");

        Intent notificationIntent = new Intent(BildirimIntent.this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(BildirimIntent.this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) BildirimIntent.this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

    }
}