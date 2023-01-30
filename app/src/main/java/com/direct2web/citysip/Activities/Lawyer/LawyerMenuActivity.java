package com.direct2web.citysip.Activities.Lawyer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.direct2web.citysip.Activities.Doctor.DoctorBusinessProfileActivity;
import com.direct2web.citysip.Activities.Doctor.DoctorBusinessTimingListActivity;
import com.direct2web.citysip.Activities.PrivacyPolicyActivity;
import com.direct2web.citysip.Activities.Restaurent.SettingActivity;
import com.direct2web.citysip.Activities.SpaAndSalon.SpaAndSalonBusinessProfileActivity;
import com.direct2web.citysip.Activities.TermsAndConditionsActivity;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.BottomButtonClickListner;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.databinding.ActivityLawyerMenuBinding;

public class LawyerMenuActivity extends AppCompatActivity {

    ActivityLawyerMenuBinding binding;
    SessionManager sessionManager;
    String profile;
    BottomButtonClickListner bottomButtonClickListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lawyer_menu);
        sessionManager = new SessionManager(this);


        bottomButtonClickListner = new BottomButtonClickListner(this, sessionManager);

        binding.bottomnavigation.bbImgMenu.setColorFilter(getResources().getColor(R.color.clr_0059C8));
        binding.bottomnavigation.bbTxtMenu.setTextColor(getResources().getColor(R.color.clay));

        binding.bottomnavigation.bbOrder.setVisibility(View.GONE);
        binding.bottomnavigation.bbAppointment.setVisibility(View.VISIBLE);

        binding.bottomnavigation.bbHome.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMyBusiness.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbAppointment.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMenu.setOnClickListener(new BottomButtonClickListner(this, sessionManager));

        binding.txtBusinessHours.setOnClickListener(v -> {
            Intent intent = new Intent(LawyerMenuActivity.this, LawyerBusinessTimingListActivity.class);
            startActivity(intent);
        });

        binding.menuTxtDetails.setOnClickListener(v -> {
            Intent intent = new Intent(LawyerMenuActivity.this, LawyerAboutBusinessActivity.class);
            startActivity(intent);
        });

        binding.menuTxtPrivacyPolicy.setOnClickListener(v -> {
            Intent intent = new Intent(LawyerMenuActivity.this, PrivacyPolicyActivity.class);
            startActivity(intent);
        });

        binding.menuTxtTerms.setOnClickListener(v -> {
            Intent intent = new Intent(LawyerMenuActivity.this, TermsAndConditionsActivity.class);
            startActivity(intent);
        });

        binding.menuTxtDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(LawyerMenuActivity.this, LawyerDashboardActivity.class);
            finish();
            startActivity(intent);
        });

        binding.menuTxtInvoice.setOnClickListener(v -> Toast.makeText(LawyerMenuActivity.this, "Work In Progress", Toast.LENGTH_SHORT).show());

        binding.menuTxtCustomer.setOnClickListener(v -> {
            Intent intent = new Intent(LawyerMenuActivity.this, LawyerCustomerActivity.class);
            startActivity(intent);
        });

        binding.menuTxtChatRoom.setOnClickListener(v -> Toast.makeText(LawyerMenuActivity.this, "Work In Progress", Toast.LENGTH_SHORT).show());

        binding.menuTxtHelp.setOnClickListener(v -> Toast.makeText(LawyerMenuActivity.this, "Work In Progress", Toast.LENGTH_SHORT).show());

        binding.menuTxtSetting.setOnClickListener(v -> {
            Intent intent = new Intent(LawyerMenuActivity.this, SettingActivity.class);
            startActivity(intent);
        });

        binding.menuTxtImageList.setOnClickListener(v -> {
            Intent intent = new Intent(LawyerMenuActivity.this, LawyerAddImageActivity.class);
            startActivity(intent);
        });

        binding.menuTxtVideoList.setOnClickListener(v -> {
            Intent intent = new Intent(LawyerMenuActivity.this, LawyerAddVideoActivity.class);
            startActivity(intent);
        });

        binding.menuTxtRestHour.setOnClickListener(v -> {
            Intent intent = new Intent(LawyerMenuActivity.this, LawyerAppointmentActivity.class);
            startActivity(intent);
        });

        binding.btnLogout.setOnClickListener(v -> {
            sessionManager.LogOut();
            sessionManager.setLogin(false);
        });
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(LawyerMenuActivity.this, LawyerDashboardActivity.class);
        finish();
        startActivity(i);
    }
}