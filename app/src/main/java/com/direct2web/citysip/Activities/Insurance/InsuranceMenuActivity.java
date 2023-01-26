package com.direct2web.citysip.Activities.Insurance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.direct2web.citysip.Activities.Doctor.DoctorBusinessProfileActivity;
import com.direct2web.citysip.Activities.Doctor.DoctorBusinessTimingListActivity;
import com.direct2web.citysip.Activities.Lawyer.LawyerAboutBusinessActivity;
import com.direct2web.citysip.Activities.Lawyer.LawyerAddImageActivity;
import com.direct2web.citysip.Activities.Lawyer.LawyerAddVideoActivity;
import com.direct2web.citysip.Activities.Lawyer.LawyerAppointmentActivity;
import com.direct2web.citysip.Activities.Lawyer.LawyerCustomerActivity;
import com.direct2web.citysip.Activities.Lawyer.LawyerDashboardActivity;
import com.direct2web.citysip.Activities.PrivacyPolicyActivity;
import com.direct2web.citysip.Activities.Restaurent.SettingActivity;
import com.direct2web.citysip.Activities.SpaAndSalon.SpaAndSalonBusinessProfileActivity;
import com.direct2web.citysip.Activities.TermsAndConditionsActivity;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.BottomButtonClickListner;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.databinding.ActivityInsuranceMenuBinding;
import com.direct2web.citysip.databinding.ActivityLawyerMenuBinding;

public class InsuranceMenuActivity extends AppCompatActivity {

    ActivityInsuranceMenuBinding binding;
    SessionManager sessionManager;
    String profile;
    BottomButtonClickListner bottomButtonClickListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_insurance_menu);
        sessionManager = new SessionManager(this);


        bottomButtonClickListner = new BottomButtonClickListner(this, sessionManager);

        binding.bottomnavigation.bbImgMenu.setColorFilter(getResources().getColor(R.color.clr_2C3564));
        binding.bottomnavigation.bbTxtMenu.setTextColor(getResources().getColor(R.color.clay));

        binding.bottomnavigation.bbOrder.setVisibility(View.GONE);
        binding.bottomnavigation.bbAppointment.setVisibility(View.VISIBLE);

        binding.bottomnavigation.bbHome.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMyBusiness.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbAppointment.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMenu.setOnClickListener(new BottomButtonClickListner(this, sessionManager));

        binding.txtBusinessHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(InsuranceMenuActivity.this, InsurenceBusinessTimingListActivity.class);
                startActivity(intent);
            }
        });

        binding.menuTxtDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsuranceMenuActivity.this, InsuranceAboutBusinessActivity.class);
                startActivity(intent);


            }
        });

        binding.menuTxtPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsuranceMenuActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);


            }
        });

        binding.menuTxtTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsuranceMenuActivity.this, TermsAndConditionsActivity.class);
                startActivity(intent);


            }
        });

        binding.menuTxtDocAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsuranceMenuActivity.this, InsuranceSetUpTimingListActivity.class);
                startActivity(intent);
            }
        });

        binding.menuTxtDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                binding.menuViewDashboard.setVisibility(View.VISIBLE);
//                binding.menuTxtDashboard.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.menu_view_divider_color));
                Intent intent = new Intent(InsuranceMenuActivity.this, InsuranceDashboardActivity.class);
                finish();
                startActivity(intent);


            }
        });

        binding.menuTxtInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                setTextViewDrawableColor(binding.menuTxtInvoice,getResources().getColor(R.color.menu_view_divider_color));
//                binding.menuViewInvoice.setVisibility(View.VISIBLE);
//                binding.menuTxtInvoice.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.menu_view_divider_color));

                /*Intent intent = new Intent(DoctorBusinessProfileActivity.this, OrderListActivity.class);
                startActivity(intent);*/

                Toast.makeText(InsuranceMenuActivity.this, "Work In Progress", Toast.LENGTH_SHORT).show();



            }
        });

        binding.menuTxtCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(InsuranceMenuActivity.this, InsuranceCustomerActivity.class);
                startActivity(intent);
//                Toast.makeText(LawyerMenuActivity.this, "Work In Progress", Toast.LENGTH_SHORT).show();

            }
        });

        binding.menuTxtChatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(InsuranceMenuActivity.this, "Work In Progress", Toast.LENGTH_SHORT).show();

            }
        });

        binding.menuTxtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(InsuranceMenuActivity.this, "Work In Progress", Toast.LENGTH_SHORT).show();

            }
        });

        binding.menuTxtSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(InsuranceMenuActivity.this, SettingActivity.class);
                startActivity(intent);

//                Toast.makeText(MenuActivity.this, "Work In Progress", Toast.LENGTH_SHORT).show();

            }
        });

        binding.menuTxtRestHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(InsuranceMenuActivity.this, InsuranceAppointmentActivity.class);
                startActivity(intent);
//                Toast.makeText(LawyerMenuActivity.this, "Work In Progress", Toast.LENGTH_SHORT).show();

            }
        });


        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sessionManager.LogOut();
                sessionManager.setLogin(false);

            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(InsuranceMenuActivity.this, InsuranceDashboardActivity.class);
        finish();
        startActivity(i);
    }
}