package com.taran1392.apps.myWidget.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.taran1392.apps.myWidget.R;

import java.util.ArrayList;

/**
 * Created by singh_t on 2/6/2017.
 */

public class FontAdapter extends ArrayAdapter<String>{

    private Context activity;
    private ArrayList<String> data;
    public Resources res;
    //SpinnerModel tempValues=null;
    LayoutInflater inflater;

    /*************  CustomAdapter Constructor *****************/
    public FontAdapter(
            Context activitySpinner,
            int textViewResourceId,
            ArrayList objects

    )
    {


        super(activitySpinner, textViewResourceId, objects);



        /********** Take passed values **********/
        activity = activitySpinner;
        data     = objects;

        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
    //    return super.getDropDownView(position, convertView, parent);

    return getView(position,convertView,parent);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // return getCustomView(position, convertView, parent);
        View row = inflater.inflate(R.layout.item, parent, false);

        TextView tx=(TextView)row.findViewById(R.id.textview);
        Typeface custom_font = Typeface.createFromAsset(activity.getAssets(), "fonts/"+(String)data.get(position));
        tx.setTypeface(custom_font);


       // Log.d("HELLO FONT",(String)data.get(position));

        return  row;

    }



    public Typeface getFont(int position){
        Typeface custom_font = Typeface.createFromAsset(activity.getAssets(), "fonts/"+(String)data.get(position));
        return custom_font;

    }

}
