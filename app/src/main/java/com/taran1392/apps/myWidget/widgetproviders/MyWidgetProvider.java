package com.taran1392.apps.myWidget.widgetproviders;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.CalendarContract;
import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews;

import com.taran1392.apps.myWidget.R;
import com.taran1392.apps.myWidget.WidgetConfig;
import com.taran1392.apps.myWidget.services.ClockService;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by singh_t on 2/5/2017.
 */

public class MyWidgetProvider extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Date d=new Date();
            Log.d("WIDGETDDEBUG","Onupdate called");
        //SimpleDateFormat df=new SimpleDateFormat("HH:mm:ss");

        //String time=df.format(d);
        for (int i=0; i<appWidgetIds.length; i++) {
            int appWidgetId = appWidgetIds[i];
            Log.d("WIDGETDDEBUG","Inside update mAppWidgetId"+ appWidgetId);
            // Create an Intent to launch ExampleActivity
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.clock_layout);


            SharedPreferences sp= context.getSharedPreferences(""+appWidgetId,Context.MODE_PRIVATE);
            String font=sp.getString(WidgetConfig.FONT,"android_7.ttf");
            int backgroundcolor=sp.getInt(WidgetConfig.BACKGROUNDCOLOR,0xffff);
            int fontcolor=sp.getInt(WidgetConfig.FONTCOLOR,0xffff);
            String timezone=sp.getString(WidgetConfig.TIMEZONE, TimeZone.getDefault().getID());
            String title=sp.getString(WidgetConfig.TITLE,null);
            int timeformat=sp.getInt(WidgetConfig.TIMEFORMAT,0);

                TimeZone tz=TimeZone.getTimeZone(timezone);
             SimpleDateFormat tf=new SimpleDateFormat("hh:mm");
                if(timeformat==1)
                    tf=new SimpleDateFormat("HH:mm");

            tf.setTimeZone(tz);
             SimpleDateFormat df=new SimpleDateFormat("EEE, dd MMM yyyy");
             df.setTimeZone(tz);

            Log.d("WIDGETDDEBUG","tz"+tz+" font "+font+ " color "+fontcolor+" title "+title);
            Log.d("HELLO","font:"+ font+" fnt color "+fontcolor );
            Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/" + font);
           try {

              views.setImageViewBitmap(R.id.clockImage,getFontBitmap(context,tf.format(d),fontcolor,font,80));
               views.setImageViewBitmap(R.id.dateImage,getFontBitmap(context,df.format(d),fontcolor,font,40));

               if(title!=null && title.length() >0){
                    views.setViewVisibility(R.id.clockNameImage,View.VISIBLE);
                   views.setImageViewBitmap(R.id.clockNameImage,getFontBitmap(context,title,fontcolor,font,40));

               }else    {
                   views.setViewVisibility(R.id.clockNameImage,View.GONE);


               }

               if(timeformat==0){

                      views.setViewVisibility(R.id.AMPMImage, View.VISIBLE);
                   String am="AM";
                   if(d.getHours() >=12)
                       am="PM";
                   views.setImageViewBitmap(R.id.AMPMImage,createTextBitmap(am,typeface,30,fontcolor));


               }else{
                   views.setViewVisibility(R.id.AMPMImage, View.GONE);


               }


            //   views.setInt(R.id.clockImage,"setBackgroundColor", Color.CYAN);
              // views.setInt(R.id.dateImage,"setBackgroundColor", Color.BLUE);
               //views.setInt(R.id.clockNameImage,"setBackgroundColor", Color.YELLOW);

           }catch (Exception e){

               Log.d("EXXX","Exception"+e);
           }

              // views.setInt(R.id.clock,"setBackgroundColor",backgroundcolor);

            views.setInt(R.id.bgcolor, "setColorFilter", backgroundcolor);
                int alpha=(backgroundcolor>>24)&0xFF;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            views.setInt(R.id.bgcolor, "setImageAlpha", alpha);
            else
            views.setInt(R.id.bgcolor, "setAlpha", alpha);





/*
            Intent intent=new Intent(context, WidgetConfig.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
           intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            PendingIntent pi=PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.clockImage,pi);
*/

            Intent intent=new Intent(context, MyWidgetProvider.class);
            intent.putExtra("mywidget",appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            PendingIntent pi=PendingIntent.getBroadcast(context,0,intent,0);
            views.setOnClickPendingIntent(R.id.clockImage,pi);

            views.setOnClickPendingIntent(R.id.dateImage,getCalenderIntent(context));

           // intent.setAction(Intent.APPWIDGET )

//            Log.d("CLOCK","onupdate called");

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

        Intent intent=new Intent(context, ClockService.class);
        //intent.setFlags(FLAG_ACTIVITY_CLEAR_TASK );
        context.startService(intent);
    }





    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);


      int appwidgetID=intent.getIntExtra("mywidget",-1);
        Log.d("CLOCKRECE","onreceive called "+appwidgetID);
if(appwidgetID > -1){
                Intent i=new Intent(context,WidgetConfig.class);
                i.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(FLAG_ACTIVITY_NEW_TASK);

    i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appwidgetID);

    context.startActivity(i);


}else {


    ComponentName thisWidget = new ComponentName(context,
            MyWidgetProvider.class);

    onUpdate(context, AppWidgetManager.getInstance(context), AppWidgetManager.getInstance(context).getAppWidgetIds(thisWidget));

}

    }




    public static Bitmap createTextBitmap(final String text, final Typeface typeface, final float textSizePixels, final int textColour)
    {
        final TextPaint textPaint = new TextPaint();
        textPaint.setTypeface(typeface);
        textPaint.setTextSize(textSizePixels);
        textPaint.setAntiAlias(true);
        textPaint.setSubpixelText(true);
        textPaint.setColor(textColour);
        textPaint.setTextAlign(Paint.Align.LEFT);
        Bitmap myBitmap = Bitmap.createBitmap((int) textPaint.measureText(text), (int) textSizePixels, Bitmap.Config.ARGB_8888);
        Canvas myCanvas = new Canvas(myBitmap);
        myCanvas.drawText(text, 0, myBitmap.getHeight(), textPaint);

        return myBitmap;
    }





    public static Bitmap getFontBitmap(Context context, String text, int color,String font, float fontSizeSP) {
        int fontSizePX = convertDiptoPix(context, fontSizeSP);
        int pad = (fontSizePX / 9);
        pad=0;
        Paint paint = new Paint();
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/"+font);
        paint.setAntiAlias(true);
        paint.setTypeface(typeface);
        paint.setColor(color);
        paint.setTextSize(fontSizePX);

        int textWidth = (int) (paint.measureText(text) + pad * 2);
        int height = (int) (fontSizePX / 0.75);
        Bitmap bitmap = Bitmap.createBitmap(textWidth, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        float xOriginal = pad;
        canvas.drawText(text, xOriginal, fontSizePX, paint);

      //  SaveImage(bitmap);
        return bitmap;
    }

    public static int convertDiptoPix(Context context, float dip) {
        int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
        return value;
    }





    public static void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();

        String fname = "Image-abc.jpg";
        File file = new File(myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();


            Log.d("FILEHELLO",file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public PendingIntent getCalenderIntent(Context context){


        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        // cal.add(Calendar.MONTH, 2);
        long time = cal.getTime().getTime();
        Uri.Builder builder =
                CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        builder.appendPath(Long.toString(time));
        Intent intent =
                new Intent(Intent.ACTION_VIEW, builder.build());

        PendingIntent pi= PendingIntent.getActivity(context,0,intent,0);
        return pi;
    }



}
