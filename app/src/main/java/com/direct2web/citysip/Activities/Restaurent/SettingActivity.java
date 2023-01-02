package com.direct2web.citysip.Activities.Restaurent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_setting);
        sessionManager = new SessionManager(this);



    }
}