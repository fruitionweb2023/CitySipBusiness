package com.direct2web.citysip.Activities.Restaurent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.direct2web.citysip.Activities.CommonActivities.IntroLoginActivity;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.databinding.ActivityRestaurantSplashScreenBinding;

public class RestaurantSplashScreenActivity extends AppCompatActivity {

    ActivityRestaurantSplashScreenBinding binding;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_restaurant_splash_screen);

        sessionManager = new SessionManager(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*if (new SessionManager(RestaurantSplashScreenActivity.this).checkLogin()) {
                    startActivity(new Intent(RestaurantSplashScreenActivity.this, MainActivity.class).putExtra("flag", 1));
                    finish();
                } else {*/
                startActivity(new Intent(RestaurantSplashScreenActivity.this, IntroLoginActivity.class));
                finish();
                //}
            }
        }, 3000);


    }
}