package com.taran1392.apps.myWidget;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class MainActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);




        addSlide(AppIntroFragment.newInstance("Using Digital Clock Widget",getString(R.string.slide1),R.drawable.single_clock,Color.rgb(183, 28, 28)));

        addSlide(AppIntroFragment.newInstance("Using Digital Clock Widget",getString(R.string.slide2),R.drawable.multiclocks, Color.rgb(0, 150, 136)));

        addSlide(AppIntroFragment.newInstance("Let's Get Started",getString(R.string.slide3),R.drawable.ic_launcher, Color.rgb(27, 94, 32)));

        //setBarColor(Color.parseColor("#3F51B5"));
        setSeparatorColor(Color.parseColor("#2196F3"));
        setFadeAnimation();
        showSkipButton(false);




    }



    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);

        // Do something when users tap on Skip button.

        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            return true;
        }
        if (id == R.id.action_rate) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
