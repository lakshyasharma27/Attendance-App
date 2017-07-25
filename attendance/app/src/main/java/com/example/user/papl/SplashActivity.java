package com.example.user.papl;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;

public class SplashActivity extends Activity {
    SharedPreferences sharedpref;
    public static final String myprefs = "myprefs";
    public static final String shown = "0";
    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        sharedpref = getSharedPreferences(myprefs,
                Context.MODE_PRIVATE);
        if (sharedpref.contains(shown)) {
            Intent fp=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(fp);
        }

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                SharedPreferences.Editor editor = sharedpref.edit();
                editor.putString(shown,"1");
                editor.commit();
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}

