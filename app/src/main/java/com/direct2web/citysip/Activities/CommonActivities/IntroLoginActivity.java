package com.direct2web.citysip.Activities.CommonActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.direct2web.citysip.R;
import com.direct2web.citysip.databinding.ActivityIntroLoginBinding;

import java.util.ArrayList;
import java.util.List;

import app.futured.donut.DonutSection;

public class IntroLoginActivity extends AppCompatActivity {

    ActivityIntroLoginBinding binding;

    DonutSection section1,section2,section3;
    List<DonutSection> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_intro_login);

        section1 = new DonutSection("section_1",Color.parseColor("#124A00"),3f);
        section2 = new DonutSection("section_2",Color.parseColor("#B0BD00"),1f);
        section3 = new DonutSection("section_3",Color.parseColor("#FF3D00"),1f);



        list.add(section1);
        list.add(section2);
        list.add(section3);


//        binding.donutView.submitData(list);

        binding.btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(IntroLoginActivity.this,NumberVerificationActivity.class);
                startActivity(intent);

            }
        });

    }
}