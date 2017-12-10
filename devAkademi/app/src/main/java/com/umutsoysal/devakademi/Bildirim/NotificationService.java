package com.umutsoysal.devakademi.Bildirim;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by MrRobot on 10.12.2017.
 */

public class NotificationService extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent ıntent) {

        context.startService(new Intent(context,BildirimIntent.class));
    }
}

