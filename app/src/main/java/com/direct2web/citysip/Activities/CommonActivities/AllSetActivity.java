package com.direct2web.citysip.Activities.CommonActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.direct2web.citysip.R;
import com.direct2web.citysip.databinding.ActivityAllSetBinding;

public class AllSetActivity extends AppCompatActivity {

    ActivityAllSetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_all_set);

        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(AllSetActivity.this, CategoryActivity.class);
                intent.putExtra("flag", 1);
                startActivity(intent);


            }
        });

    }
}