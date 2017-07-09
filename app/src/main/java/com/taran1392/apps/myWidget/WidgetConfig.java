package com.taran1392.apps.myWidget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.taran1392.apps.myWidget.adapter.FontAdapter;
import com.taran1392.apps.myWidget.widgetproviders.MyWidgetProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;


/**
 * Created by singh_t on 2/5/2017.
 */

public class WidgetConfig extends Activity {

    EditText clockName;
    Spinner timeFormatSpinner,timeFontSpinner,timeZoneSpinner;
    View backgroundView,fontColorView,clock;
    TextView backgroundTextView,fontColortextView,ampmTextView;

    TextView clocktextview,datetextview,clockNametextview,clockTitleTextView;

    CheckBox clockTitleCheckBox;


    ArrayAdapter <CharSequence> timezoneAdapter;
    FontAdapter fontAdapter;

    //sharedpref values
    public static String TITLE="title";
    public static String TIMEZONE="timezone";
    public static String FONT="font";
    public static String TIMEFORMAT="timeformat";
    public static String BACKGROUNDCOLOR="backgroundcolor";
    public static String FONTCOLOR="fontcolor";


    String sp_title,sp_timezone,sp_font;
    int sp_fontcolor,sp_backgroundcolor,sp_timeformat;

    Date d;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_widgetconfig);
        Toolbar toolbar = (Toolbar) findViewById(R.id.wtoolbar);


        //Toolbar tb=(Toolbar)getSupportActionBar();

//toolbar.setMenu();
        findViewById(R.id.menu_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Toast.makeText(WidgetConfig.this,"lickedabout",Toast.LENGTH_SHORT).show();
               //openContextMenu(v);

                showMenu(v);
             //   openOptionsMenu();
            }
        });

        registerForContextMenu(findViewById(R.id.menu_button));


        ColorDrawable colorDrawable=new ColorDrawable(0x78080808);
        //getSupportActionBar().setBackgroundDrawable(colorDrawable);

       // getSupportActionBar().s
        d=new Date();
        //clockName=(EditText)findViewById(R.id.edit_clockName);
        timeFormatSpinner=(Spinner)findViewById(R.id.timeformatSpinner);
        timeFontSpinner=(Spinner)findViewById(R.id.timefontSpinner);
        timeZoneSpinner=(Spinner)findViewById(R.id.timexonespinner);


        backgroundView=findViewById(R.id.backgroundedit);
        fontColorView=findViewById(R.id.fontcoloredit);

        backgroundTextView=(TextView)findViewById(R.id.backgroundTextView);
        fontColortextView=(TextView)findViewById(R.id.fontColor);
        ampmTextView=(TextView)findViewById(R.id.ampm_textview);

        clockNametextview=(TextView)findViewById(R.id.clockName);
        clocktextview=(TextView)findViewById(R.id.clock_textview);
        datetextview=(TextView)findViewById(R.id.clockDate);
        clock=findViewById(R.id.clock);


        clockTitleTextView=(TextView)findViewById(R.id.clocktitletextview);

        clockTitleCheckBox =(CheckBox )findViewById(R.id.clocktitlecheckbox);

        clockTitleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    clockNametextview.setVisibility(View.VISIBLE);
                    showDialog();
                }else{


                    clockNametextview.setVisibility(View.GONE);
                }

            }
        });







        backgroundView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ColorDrawable cd = (ColorDrawable) backgroundTextView.getBackground();
                int colorCode = cd.getColor();
                changeColor(0,colorCode);
            }
        });

        fontColorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ColorDrawable cd = (ColorDrawable) fontColortextView.getBackground();
                int colorCode = cd.getColor();
                changeColor(1,colorCode);
            }
        });


        WallpaperManager wpm= WallpaperManager.getInstance(this);
        Drawable draw=wpm.getDrawable();

        findViewById(R.id.mainContainer).setBackground(draw);


        ArrayAdapter <CharSequence> adapter =
                new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapter.add("12-hour format");
        adapter.add("24-hour format");
        timeFormatSpinner.setAdapter(adapter);


        final SimpleDateFormat tf=new SimpleDateFormat("hh:mm");
        clocktextview.setText(tf.format(d));
        final SimpleDateFormat df=new SimpleDateFormat("EEE, dd MMM yyyy");


        datetextview.setText(df.format(d));



        timeFormatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                switch (position){
                    case 0:clocktextview.setText(tf.format(d));ampmTextView.setVisibility(View.VISIBLE);
                        break;
                    case 1:clocktextview.setText(tf.format(d));ampmTextView.setVisibility(View.GONE);break;


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        ArrayList<String> fonts=new ArrayList<String>();

        fonts=new ArrayList<String>();
        try {
            String files[] = this.getAssets().list("fonts");

            for(String f:files ){

                fonts.add(f);
            }

        }catch (Exception e){


        }

        fontAdapter=new FontAdapter(this,R.layout.item,fonts);


        timeFontSpinner.setAdapter(fontAdapter);

        timeFontSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Typeface font=fontAdapter.getFont(position);
                clocktextview.setTypeface(font);
                datetextview.setTypeface(font);
                ampmTextView.setTypeface(font);
                clockNametextview.setTypeface(font);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        timezoneAdapter =
                new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item );
        timezoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       timezoneAdapter.addAll(TimeZone.getAvailableIDs());

        timeZoneSpinner.setAdapter(timezoneAdapter);
        timeZoneSpinner.setSelection(timezoneAdapter.getPosition(TimeZone.getDefault().getID()));

        //Date d=new Date();


        timeZoneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String tz=timezoneAdapter.getItem(position).toString();

                TimeZone timeZone=TimeZone.getTimeZone(tz);

                tf.setTimeZone(timeZone);
                df.setTimeZone(timeZone);
                Date d=new Date();
                clocktextview.setText(tf.format(d));

                datetextview.setText(df.format(d));


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getWidgetConfig();
        loadWidgetConfig();
        if(sp_title.length()<=0)
        clockTitleCheckBox.setChecked(false);

    }




    public void changeColor(int id,int currentColor){

final int id2=id;
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose color")
                .initialColor(currentColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        //toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        //changeBackgroundColor(selectedColor);
                        switch (id2){
                            case 0: backgroundTextView.setBackgroundColor(selectedColor);
                                    clock.setBackgroundColor(selectedColor);
                                break;
                            case 1: fontColortextView.setBackgroundColor(selectedColor);
                                    clockNametextview.setTextColor(selectedColor);
                                    //clockTitleTextView.setTextColor(selectedColor);
                                    datetextview.setTextColor(selectedColor);
                                    clocktextview.setTextColor(selectedColor);
                                    ampmTextView.setTextColor(selectedColor);
                                break;

                        }



                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();



    }


    @Override
    protected void onStart() {
        super.onStart();

        //clockName.clearFocus();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //clockName.clearFocus();
    }

    @Override
    public void onBackPressed() {

       int mAppWidgetId = INVALID_APPWIDGET_ID;
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(EXTRA_APPWIDGET_ID,
                    INVALID_APPWIDGET_ID);
            Intent i = new Intent(getBaseContext(), WidgetConfig.class);

            i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);

            // Creating a pending intent, which will be invoked when the user
            // clicks on the widget
            PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Getting an instance of WidgetManager
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getBaseContext());

            // Instantiating the class RemoteViews with widget_layout
          //  RemoteViews views = new RemoteViews(getBaseContext().getPackageName(), R.layout.clock_layout);

            // Setting the background color of the widget
           // ColorDrawable d=(ColorDrawable) fontColortextView.getBackground();


           // views.setInt(R.id.clock_textview, "setTextColor",d.getColor());
           // views.setTextColor(R.id.clock_textview,d.getColor());
            ComponentName thisWidget = new ComponentName(this, MyWidgetProvider.class);
            //  Attach an on-click listener to the clock
           // views.setOnClickPendingIntent(R.id.clock, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the app widget
            //appWidgetManager.updateAppWidget(thisWidget,views);
            //appWidgetManager.updateAppWidget(mAppWidgetId, views);

            // Return RESULT_OK from this activity
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);


            SharedPreferences sp=getSharedPreferences(""+mAppWidgetId,MODE_PRIVATE);

             SharedPreferences.Editor ed= sp.edit();
            if(clockTitleCheckBox.isChecked())
            ed.putString(TITLE,clockTitleTextView.getText().toString());
            else
                ed.putString(TITLE,"");

            ed.putInt(TIMEFORMAT,timeFormatSpinner.getSelectedItemPosition());
            ed.putString(TIMEZONE,timeZoneSpinner.getSelectedItem().toString());
            ed.putInt(FONTCOLOR,((ColorDrawable)fontColortextView.getBackground()).getColor());

            ed.putInt(BACKGROUNDCOLOR,((ColorDrawable)backgroundTextView.getBackground()).getColor());

            ed.putString(FONT,timeFontSpinner.getSelectedItem().toString());

            ed.commit();



            Intent intent2=new Intent();
            intent2.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            final PendingIntent pi=PendingIntent.getBroadcast(this.getApplicationContext(),0,intent2,0);
           try {
               pi.send();
           }catch (Exception e){}

            Log.d("WIDGETDDEBUG","mAppWidgetId"+ mAppWidgetId);
            finish();


        }
        if (mAppWidgetId == INVALID_APPWIDGET_ID) {

            Log.d("onback presses","mAppWidgetId");
            finish();
        }

        finish();

    }







    public void getWidgetConfig(){
        sp_font="android_7.ttf";
        sp_backgroundcolor=0xa0161515;
        sp_fontcolor=0xFFFFFFFF;
        sp_timezone=TimeZone.getDefault().getDisplayName();
        sp_timeformat=0;
        sp_title="";

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

       int  mAppWidgetId = extras.getInt(EXTRA_APPWIDGET_ID,
                INVALID_APPWIDGET_ID);

        Log.d("WIDGETDDEBUG","Loading widget "+ mAppWidgetId);
        SharedPreferences sp= getSharedPreferences(""+mAppWidgetId, Context.MODE_PRIVATE);
        sp_font=sp.getString(WidgetConfig.FONT,"android_7.tff");
        sp_backgroundcolor=sp.getInt(WidgetConfig.BACKGROUNDCOLOR,0xa0161515);
        sp_fontcolor=sp.getInt(WidgetConfig.FONTCOLOR,0xFFFFFFFF);
        sp_timezone=sp.getString(WidgetConfig.TIMEZONE, TimeZone.getDefault().getID());
        sp_title=sp.getString(WidgetConfig.TITLE,"");
        sp_timeformat=sp.getInt(WidgetConfig.TIMEFORMAT,0);

    }



    public void loadWidgetConfig(){

        clockTitleTextView.setText(sp_title);
        clockNametextview.setText(sp_title);
        timeFormatSpinner.setSelection(sp_timeformat);
        Log.d("TIMEFORMAT ","  "+sp_font);
        timeZoneSpinner.setSelection(timezoneAdapter.getPosition(sp_timezone));
        timeFontSpinner.setSelection(fontAdapter.getPosition(sp_font));
        fontColortextView.setBackgroundColor(sp_fontcolor);
        backgroundTextView.setBackgroundColor(sp_backgroundcolor);


        backgroundTextView.setBackgroundColor(sp_backgroundcolor);
        fontColortextView.setBackgroundColor(sp_fontcolor);

        clocktextview.setTextColor(sp_fontcolor);
        datetextview.setTextColor(sp_fontcolor);
        clockNametextview.setTextColor(sp_fontcolor);
        ampmTextView.setTextColor(sp_fontcolor);
try {
    Typeface font = Typeface.createFromAsset(getAssets(), "fonts/" + sp_font);

    clocktextview.setTypeface(font);
    datetextview.setTypeface(font);
    clockNametextview.setTypeface(font);
    ampmTextView.setTypeface(font);
}catch (Exception e){

}

        findViewById(R.id.clock).setBackgroundColor(sp_backgroundcolor);

    }





    public void showDialog(){
        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        final View dview=inflater.inflate(R.layout.dialog_layout, null);
        builder.setView(dview).setTitle("Clock Title")
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                           EditText ed= (EditText) dview.findViewById(R.id.dialogClockname);
                            clockTitleTextView.setText(ed.getText());
                            clockNametextview.setText(ed.getText());
                            dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                  //      LoginDialogFragment.this.getDialog().cancel();

                        dialog.dismiss();
                    }
                });
        //return builder.create();

        dialog=builder.create();
        dialog.show();



    }


    public void showMenu (View view)
    {
        PopupMenu menu = new PopupMenu (this, view);
        menu.setOnMenuItemClickListener (new PopupMenu.OnMenuItemClickListener ()
        {
            @Override
            public boolean onMenuItemClick (MenuItem item)
            {
                int id = item.getItemId();
                switch (id)
                {
                    case R.id.action_about: startActivity(new Intent(WidgetConfig.this,MainActivity.class));   ; break;
                    case R.id.action_rate: Toast.makeText(WidgetConfig.this,"popup rate",Toast.LENGTH_SHORT    ).show();

                                String url="market://details?id="+getPackageName();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                        break;
                    case R.id.action_share:
                        String url2="http://play.google.com/store/apps/details?id="+getPackageName();
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey! Check out this Clock Widget "+url2);
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                        break;
                }
                return true;
            }
        });
        menu.inflate (R.menu.menu_main);
        menu.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

//        getMenuInflater().inflate(R.menu.menu_main, menu);

        menu.add("About");
        menu.add("Rate");

        //menu.s
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {


        if(item.getTitle().equals("Rate")){

            Toast.makeText(this,"Rate",Toast.LENGTH_SHORT).show();
            return true;
        }

        if(item.getTitle().equals("About")){

            Toast.makeText(this,"about",Toast.LENGTH_SHORT).show();
            return true;
        }





        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {

            Toast.makeText(this,"about",Toast.LENGTH_SHORT).show();
            return true;
        }if (id == R.id.action_rate) {


            Toast.makeText(this,"Rate",Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    }
