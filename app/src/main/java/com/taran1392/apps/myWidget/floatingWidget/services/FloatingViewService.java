package com.taran1392.apps.myWidget.floatingWidget.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.taran1392.apps.myWidget.R;
import com.taran1392.apps.myWidget.floatingWidget.activity.FloatingMainActivity;

/**
 * Created by singh_t on 2/11/2017.
 */
public class FloatingViewService extends Service {
    private WindowManager mWindowManager;
    private View mFloatingView,mBottomView;
    WindowManager.LayoutParams params,params2;
    int height;

    public FloatingViewService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Inflate the floating view layout we created
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.floatingwidget_layout, null);
        mBottomView = LayoutInflater.from(this).inflate(R.layout.bottom, null);



        //Add the view to the window.
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        //params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;






        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);


        height=getScreenHeight(mWindowManager);

        mBottomView.setVisibility(View.GONE);

        params2 = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        params2.gravity = Gravity.BOTTOM | Gravity.CENTER;        //Initially view will be added to top-left corner
        params2.x = 0;
        //params2.y = 100;

        mWindowManager.addView(mBottomView,params2);
        mWindowManager.addView(mFloatingView, params);

        //mWindowManager.getDefaultDisplay().getHeight();




//The root element of the collapsed view layout
        final View collapsedView = mFloatingView.findViewById(R.id.collapse_view);
//The root element of the expanded view layout
        final View expandedView = mFloatingView.findViewById(R.id.expanded_view);


//Set the close button
        ImageView closeButtonCollapsed = (ImageView) mFloatingView.findViewById(R.id.close_btn);
        closeButtonCollapsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close the service and remove the from from the window
                stopSelf();
            }
        });



        mFloatingView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:


                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;


                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();


                     //

                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.


                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);




                           // collapsedView.setP
                        mBottomView.setVisibility(View.VISIBLE);

                        if(event.getRawY() > height-mBottomView.getHeight()){
                            //collapsedView.setForeground();
                            collapsedView.setBackground(new ColorDrawable( 0x79eb2727 ));

                        }else{

                            collapsedView.setBackground(new ColorDrawable(Color.TRANSPARENT));


                        }

                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingView, params );



                        //mFloatingView.findViewById(R.id.bottom_view).setVisibility(View.VISIBLE);
                        return true;

                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);



                        //mWindowManager.removeView(mBottomView);
                        mBottomView.setVisibility(View.GONE);

                        //mFloatingView.findViewById(R.id.bottom_view).setVisibility(View.GONE);
                        mWindowManager.updateViewLayout(mFloatingView,params);

                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.
                        if (Xdiff < 10 && Ydiff < 10) {


                            if(expandedView.getVisibility()==View.VISIBLE){

                                expandedView.setVisibility(View.INVISIBLE);
                            }else{
                                expandedView.setVisibility(View.VISIBLE);



                            }

                            if (isViewCollapsed()) {
                                //When user clicks on the image view of the collapsed layout,
                                //visibility of the collapsed layout will be changed to "View.GONE"
                                //and expanded view will become visible.
                                //collapsedView.setVisibility(View.GONE);
                               // expandedView.setVisibility(View.VISIBLE);
                            }





                        }





                        if(event.getRawY() > height-mBottomView.getHeight()){
                            stopSelf();




                        }
                        return true;
                }
                return false;
            }
        });
    }






    public int getScreenHeight(WindowManager wm){
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        //return "{" + width + "," + height + "}";
        return height;


    }
    private boolean isViewCollapsed() {
        return mFloatingView == null || mFloatingView.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }
}