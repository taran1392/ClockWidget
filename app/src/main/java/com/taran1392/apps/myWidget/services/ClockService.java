package com.taran1392.apps.myWidget.services;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by singh_t on 2/5/2017.
 */

public class ClockService extends Service {


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        //Log.d("SSS","Service started");


        int widgetIDs[]=  intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        Intent intent2=new Intent();
        intent2.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        final PendingIntent pi=PendingIntent.getBroadcast(this.getApplicationContext(),0,intent2,0);



        try {

            //Thread.sleep(2000);

        Runnable r=new Runnable() {
            @Override
            public void run() {
                try {
                    pi.send();
                    stopSelf();


                }catch (Exception e){


                }
            }
        };

           Handler h=new Handler();

            h.postDelayed(r,60000);

        }catch (Exception e){


            //Log.d("SSS","bradcast failed"+e);


        }

        //AppWidgetManager.getInstance(this.getApplicationContext()).updateAppWidget();



    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
