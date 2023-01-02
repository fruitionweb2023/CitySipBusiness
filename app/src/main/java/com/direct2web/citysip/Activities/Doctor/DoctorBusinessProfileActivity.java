package com.direct2web.citysip.Activities.Doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.direct2web.citysip.Activities.Restaurent.SettingActivity;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.BottomButtonClickListner;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.databinding.ActivityDoctorBusinessProfileBinding;

public class DoctorBusinessProfileActivity extends AppCompatActivity {

    ActivityDoctorBusinessProfileBinding binding;
    SessionManager sessionManager;
    String profile;
    BottomButtonClickListner bottomButtonClickListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_business_profile);
        sessionManager = new SessionManager(this);


        bottomButtonClickListner = new BottomButtonClickListner(this, sessionManager);

        binding.bottomnavigation.bbImgMenu.setColorFilter(getResources().getColor(R.color.clr_0059C8));
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

        binding.menuTxtDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorBusinessProfileActivity.this, DoctorAboutBusinessActivity.class);
                startActivity(intent);


            }
        });

        binding.menuTxtDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                binding.menuViewDashboard.setVisibility(View.VISIBLE);
//                binding.menuTxtDashboard.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.menu_view_divider_color));
                Intent intent = new Intent(DoctorBusinessProfileActivity.this, DoctorDeshboardActivity.class);
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

                Toast.makeText(DoctorBusinessProfileActivity.this, "Work In Progress", Toast.LENGTH_SHORT).show();



            }
        });

        binding.menuTxtCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                setTextViewDrawableColor(binding.menuTxtInvoice,getResources().getColor(R.color.menu_view_divider_color));

//                binding.menuViewCustomer.setVisibility(View.VISIBLE);
//                binding.menuTxtCustomer.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.menu_view_divider_color));

                Intent intent = new Intent(DoctorBusinessProfileActivity.this, DoctorCustomerActivity.class);
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

                Toast.makeText(DoctorBusinessProfileActivity.this, "Work In Progress", Toast.LENGTH_SHORT).show();

            }
        });

        binding.menuTxtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(DoctorBusinessProfileActivity.this, "Work In Progress", Toast.LENGTH_SHORT).show();

            }
        });

        binding.menuTxtSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(DoctorBusinessProfileActivity.this, SettingActivity.class);
                startActivity(intent);

//                Toast.makeText(MenuActivity.this, "Work In Progress", Toast.LENGTH_SHORT).show();

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

                Intent intent = new Intent(DoctorBusinessProfileActivity.this, DoctorAddImageActivity.class);
                startActivity(intent);

            }
        });

        binding.menuTxtVideoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                setTextViewDrawableColor(binding.menuTxtInvoice,getResources().getColor(R.color.menu_view_divider_color));

//                binding.menuViewCustomer.setVisibility(View.VISIBLE);
//                binding.menuTxtCustomer.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.menu_view_divider_color));

                Intent intent = new Intent(DoctorBusinessProfileActivity.this, DoctorAddVedioActivity.class);
                startActivity(intent);

            }
        });

        binding.menuTxtDocAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                setTextViewDrawableColor(binding.menuTxtInvoice,getResources().getColor(R.color.menu_view_divider_color));

//                binding.menuViewCustomer.setVisibility(View.VISIBLE);
//                binding.menuTxtCustomer.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.menu_view_divider_color));

                Intent intent = new Intent(DoctorBusinessProfileActivity.this, DoctorSetUpTimingListActivity.class);
                startActivity(intent);
//                Toast.makeText(DoctorBusinessProfileActivity.this, "Work In Progress", Toast.LENGTH_SHORT).show();

            }
        });
        binding.txtAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorBusinessProfileActivity.this, DoctorAppointmentActivity.class);
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
        Intent i = new Intent(DoctorBusinessProfileActivity.this, DoctorDeshboardActivity.class);
        finish();
        startActivity(i);
    }
}