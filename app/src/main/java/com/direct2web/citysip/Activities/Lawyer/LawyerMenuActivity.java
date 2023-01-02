package com.direct2web.citysip.Activities.Lawyer;

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
                Intent intent = new Intent(LawyerMenuActivity.this, LawyerAboutBusinessActivity.class);
                startActivity(intent);


            }
        });

        binding.menuTxtDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                binding.menuViewDashboard.setVisibility(View.VISIBLE);
//                binding.menuTxtDashboard.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.menu_view_divider_color));
                Intent intent = new Intent(LawyerMenuActivity.this, LawyerDashboardActivity.class);
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

                Toast.makeText(LawyerMenuActivity.this, "Work In Progress", Toast.LENGTH_SHORT).show();



            }
        });

        binding.menuTxtCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                setTextViewDrawableColor(binding.menuTxtInvoice,getResources().getColor(R.color.menu_view_divider_color));

//                binding.menuViewCustomer.setVisibility(View.VISIBLE);
//                binding.menuTxtCustomer.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.menu_view_divider_color));

                Intent intent = new Intent(LawyerMenuActivity.this, LawyerCustomerActivity.class);
                startActivity(intent);
//                Toast.makeText(LawyerMenuActivity.this, "Work In Progress", Toast.LENGTH_SHORT).show();

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

                Toast.makeText(LawyerMenuActivity.this, "Work In Progress", Toast.LENGTH_SHORT).show();

            }
        });

        binding.menuTxtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(LawyerMenuActivity.this, "Work In Progress", Toast.LENGTH_SHORT).show();

            }
        });

        binding.menuTxtSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(LawyerMenuActivity.this, SettingActivity.class);
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

                Intent intent = new Intent(LawyerMenuActivity.this, LawyerAddImageActivity.class);
                startActivity(intent);

            }
        });

        binding.menuTxtVideoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                setTextViewDrawableColor(binding.menuTxtInvoice,getResources().getColor(R.color.menu_view_divider_color));

//                binding.menuViewCustomer.setVisibility(View.VISIBLE);
//                binding.menuTxtCustomer.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.menu_view_divider_color));

                Intent intent = new Intent(LawyerMenuActivity.this, LawyerAddVideoActivity.class);
                startActivity(intent);

            }
        });

        binding.menuTxtRestHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                setTextViewDrawableColor(binding.menuTxtInvoice,getResources().getColor(R.color.menu_view_divider_color));

//                binding.menuViewCustomer.setVisibility(View.VISIBLE);
//                binding.menuTxtCustomer.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.menu_view_divider_color));

                Intent intent = new Intent(LawyerMenuActivity.this, LawyerAppointmentActivity.class);
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
        Intent i = new Intent(LawyerMenuActivity.this, LawyerDashboardActivity.class);
        finish();
        startActivity(i);
    }
}