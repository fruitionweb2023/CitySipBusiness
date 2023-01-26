package com.direct2web.citysip.Activities.SpaAndSalon;

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
import com.direct2web.citysip.Activities.TermsAndConditionsActivity;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.BottomButtonClickListner;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.databinding.ActivityDoctorBusinessProfileBinding;
import com.direct2web.citysip.databinding.ActivitySpaSalonBusinessProfileBinding;

public class SpaAndSalonBusinessProfileActivity extends AppCompatActivity {

    ActivitySpaSalonBusinessProfileBinding binding;
    SessionManager sessionManager;
    String profile;
    BottomButtonClickListner bottomButtonClickListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_spa_salon_business_profile);
        sessionManager = new SessionManager(this);


        bottomButtonClickListner = new BottomButtonClickListner(this, sessionManager);

        binding.bottomnavigation.bbImgMenu.setColorFilter(getResources().getColor(R.color.clr_spa_salon));
        binding.bottomnavigation.bbTxtMenu.setTextColor(getResources().getColor(R.color.clay));

        binding.bottomnavigation.bbHome.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMyBusiness.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbOrder.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMenu.setOnClickListener(new BottomButtonClickListner(this, sessionManager));



        /*binding.toolbar.toolbarBack.setVisibility(View.VISIBLE);

        binding.toolbar.toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });*/


        binding.txtBusinessHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(SpaAndSalonBusinessProfileActivity.this, SpaAndSalonBusinessTimingListActivity.class);
                startActivity(intent);
            }
        });

        binding.menuTxtDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpaAndSalonBusinessProfileActivity.this, SpaAndSalonAboutBusinessActivity.class);
                startActivity(intent);


            }
        });

        binding.menuTxtPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpaAndSalonBusinessProfileActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);


            }
        });

        binding.menuTxtTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpaAndSalonBusinessProfileActivity.this, TermsAndConditionsActivity.class);
                startActivity(intent);


            }
        });

        binding.menuTxtDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                binding.menuViewDashboard.setVisibility(View.VISIBLE);
//                binding.menuTxtDashboard.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.menu_view_divider_color));
                Intent intent = new Intent(SpaAndSalonBusinessProfileActivity.this, SapAndSalonDeshboardActivity.class);
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

                Toast.makeText(SpaAndSalonBusinessProfileActivity.this, "Work In Progress", Toast.LENGTH_SHORT).show();



            }
        });

        binding.menuTxtCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                setTextViewDrawableColor(binding.menuTxtInvoice,getResources().getColor(R.color.menu_view_divider_color));

//                binding.menuViewCustomer.setVisibility(View.VISIBLE);
//                binding.menuTxtCustomer.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.menu_view_divider_color));

                Intent intent = new Intent(SpaAndSalonBusinessProfileActivity.this, SpaAndSalonCustomerActivity.class);
                startActivity(intent);
//                Toast.makeText(DoctorBusinessProfileActivity.this, "Work In Progress", Toast.LENGTH_SHORT).show();

            }
        });

        binding.menuTxtChatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                setTextViewDrawableColor(binding.menuTxtInvoice,getResources().getColor(R.color.menu_view_divider_color));

//                binding.menuViewCustomer.setVisibility(View.VISIBLE);
//                binding.menuTxtCustomer.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.menu_view_divider_color));

//                Intent intent = new Intent(MenuActivity.this, ChatRoomActivity.class);
//                startActivity(intent);

                Toast.makeText(SpaAndSalonBusinessProfileActivity.this, "Work In Progress", Toast.LENGTH_SHORT).show();

            }
        });

        binding.menuTxtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(SpaAndSalonBusinessProfileActivity.this, "Work In Progress", Toast.LENGTH_SHORT).show();

            }
        });

        binding.menuTxtSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(SpaAndSalonBusinessProfileActivity.this, SettingActivity.class);
                startActivity(intent);

//                Toast.makeText(MenuActivity.this, "Work In Progress", Toast.LENGTH_SHORT).show();

            }
        });

        binding.txtStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpaAndSalonBusinessProfileActivity.this, SpaAndSalonStaffActivity.class);
                startActivity(intent);


            }
        });

        /*binding.menuTxtPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                setTextViewDrawableColor(binding.menuTxtInvoice,getResources().getColor(R.color.menu_view_divider_color));

//                binding.menuViewCustomer.setVisibility(View.VISIBLE);
//                binding.menuTxtCustomer.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.menu_view_divider_color));

                Intent intent = new Intent(MenuActivity.this, PaymentActivity.class);
                startActivity(intent);

            }
        });*/

        /*binding.menuTxtMenuList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                setTextViewDrawableColor(binding.menuTxtInvoice,getResources().getColor(R.color.menu_view_divider_color));

//                binding.menuViewCustomer.setVisibility(View.VISIBLE);
//                binding.menuTxtCustomer.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.menu_view_divider_color));

                Intent intent = new Intent(MenuActivity.this, SetUpMenuActivity.class);
                intent.putExtra("flag", 1);
                startActivity(intent);

            }
        });*/

        binding.menuTxtImageList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                setTextViewDrawableColor(binding.menuTxtInvoice,getResources().getColor(R.color.menu_view_divider_color));

//                binding.menuViewCustomer.setVisibility(View.VISIBLE);
//                binding.menuTxtCustomer.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.menu_view_divider_color));

                Intent intent = new Intent(SpaAndSalonBusinessProfileActivity.this, SpaAndSalonAddImageActivity.class);
                startActivity(intent);

            }
        });

        binding.menuTxtVideoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                setTextViewDrawableColor(binding.menuTxtInvoice,getResources().getColor(R.color.menu_view_divider_color));

//                binding.menuViewCustomer.setVisibility(View.VISIBLE);
//                binding.menuTxtCustomer.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.menu_view_divider_color));

                Intent intent = new Intent(SpaAndSalonBusinessProfileActivity.this, SpaAndSalonAddVedioActivity.class);
                startActivity(intent);

            }
        });

        binding.menuTxtDocAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                setTextViewDrawableColor(binding.menuTxtInvoice,getResources().getColor(R.color.menu_view_divider_color));

//                binding.menuViewCustomer.setVisibility(View.VISIBLE);
//                binding.menuTxtCustomer.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.menu_view_divider_color));

               /* Intent intent = new Intent(SpaAndSalonBusinessProfileActivity.this, DoctorSetUpTimingListActivity.class);
                startActivity(intent);*/
                Toast.makeText(SpaAndSalonBusinessProfileActivity.this, "Work In Progress", Toast.LENGTH_SHORT).show();

            }
        });
        binding.txtAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpaAndSalonBusinessProfileActivity.this, SpaAndSalonAppointmentActivity.class);
                startActivity(intent);
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
        Intent i = new Intent(SpaAndSalonBusinessProfileActivity.this, SapAndSalonDeshboardActivity.class);
        finish();
        startActivity(i);
    }
}