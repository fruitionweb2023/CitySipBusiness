package com.direct2web.citysip.Activities.Restaurent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.direct2web.citysip.Activities.PrivacyPolicyActivity;
import com.direct2web.citysip.Activities.SpaAndSalon.SpaAndSalonBusinessProfileActivity;
import com.direct2web.citysip.Activities.TermsAndConditionsActivity;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.BottomButtonClickListner;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.databinding.ActivityMenuBinding;

public class MenuActivity extends AppCompatActivity {
    ActivityMenuBinding binding;
    SessionManager sessionManager;
    String profile;
    BottomButtonClickListner bottomButtonClickListner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu);
        sessionManager = new SessionManager(this);


        bottomButtonClickListner = new BottomButtonClickListner(this, sessionManager);

        binding.bottomnavigation.bbImgMenu.setColorFilter(getResources().getColor(R.color.clr_f54748));
        binding.bottomnavigation.bbTxtMenu.setTextColor(getResources().getColor(R.color.clay));

        binding.bottomnavigation.bbHome.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMyBusiness.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbOrder.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMenu.setOnClickListener(new BottomButtonClickListner(this, sessionManager));

        binding.toolbar.toolbarBack.setVisibility(View.VISIBLE);

        binding.toolbar.toolbarBack.setOnClickListener(v -> onBackPressed());

        binding.menuTxtDetails.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, SetUpAboutRestaurantActivity.class);
            startActivity(intent);
        });

        binding.menuTxtPrivacyPolicy.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, PrivacyPolicyActivity.class);
            startActivity(intent);
        });

        binding.menuTxtTerms.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, TermsAndConditionsActivity.class);
            startActivity(intent);
        });

        binding.menuTxtDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
        });

        binding.menuTxtInvoice.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, OrderListActivity.class);
            startActivity(intent);
        });

        binding.menuTxtCustomer.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, CustomerActivity.class);
            startActivity(intent);
        });

        binding.menuTxtChatRoom.setOnClickListener(v -> Toast.makeText(MenuActivity.this, "Work In Progress", Toast.LENGTH_SHORT).show());

        binding.menuTxtHelp.setOnClickListener(v -> Toast.makeText(MenuActivity.this, "Work In Progress", Toast.LENGTH_SHORT).show());

        binding.menuTxtSetting.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, SettingActivity.class);
            startActivity(intent);
        });

        binding.menuTxtImageList.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, ImageListActivity.class);
            startActivity(intent);
        });

        binding.menuTxtVideoList.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, VideoListActivity.class);
            startActivity(intent);
        });

        binding.menuTxtRestHour.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, SetUpTimingListActivity.class);
            startActivity(intent);
        });


        binding.btnLogout.setOnClickListener(v -> {
            sessionManager.LogOut();
            sessionManager.setLogin(false);
        });
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(MenuActivity.this, MainActivity.class);
        finish();
        startActivity(i);
    }
}
