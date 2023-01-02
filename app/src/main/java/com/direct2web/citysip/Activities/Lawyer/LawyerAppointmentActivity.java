package com.direct2web.citysip.Activities.Lawyer;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.direct2web.citysip.Adapter.DoctorAdapters.AppointmentListAdapter;
import com.direct2web.citysip.Adapter.LawyerAdapters.AppointmentListAdapterLawyer;
import com.direct2web.citysip.Model.DoctorModels.DoctorAppointment.ResponseDoctorAppointment;
import com.direct2web.citysip.Model.LawyerModels.LawyerAppointment.Order;
import com.direct2web.citysip.Model.LawyerModels.LawyerAppointment.ResponseLawyerAppointment;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.BottomButtonClickListner;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityDoctorAppointmentBinding;
import com.direct2web.citysip.databinding.ActivityLawyerAppointmentBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LawyerAppointmentActivity extends AppCompatActivity {

    ActivityLawyerAppointmentBinding binding;
    SessionManager sessionManager;
    BottomButtonClickListner bottomButtonClickListner;
    ProgressDialog pd;
    AppointmentListAdapterLawyer adapter;

    List<Order> orderListDocotor = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lawyer_appointment);
        sessionManager = new SessionManager(this);

        bottomButtonClickListner = new BottomButtonClickListner(this,sessionManager);

        binding.bottomnavigation.bbOrder.setVisibility(View.GONE);
        binding.bottomnavigation.bbAppointment.setVisibility(View.VISIBLE);

        binding.bottomnavigation.bbImgAppointment.setColorFilter(getResources().getColor(R.color.clr_0059C8));
        binding.bottomnavigation.bbTxtAppointment.setTextColor(getResources().getColor(R.color.clay));

        binding.bottomnavigation.bbHome.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMyBusiness.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbAppointment.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMenu.setOnClickListener(new BottomButtonClickListner(this, sessionManager));


        pd = new ProgressDialog(this);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.show();


        getOrder(sessionManager.getUserId());

    }

    private void getOrder(String userId) {

        Api api = RetrofitClient.getClient().create(Api.class);

        Call<ResponseLawyerAppointment> call = api.geLawyerOrder("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId);

        call.enqueue(new Callback<ResponseLawyerAppointment>() {
            @Override
            public void onResponse(Call<ResponseLawyerAppointment> call, Response<ResponseLawyerAppointment> response) {

                Log.e("responseOrder", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(LawyerAppointmentActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        orderListDocotor = response.body().getOrderList();

                        adapter = new AppointmentListAdapterLawyer(orderListDocotor, LawyerAppointmentActivity.this);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LawyerAppointmentActivity.this);
                        binding.rclrOrder.setLayoutManager(linearLayoutManager);
                        binding.rclrOrder.setAdapter(adapter);


                    }


                } else {

                    Toast.makeText(LawyerAppointmentActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(Call<ResponseLawyerAppointment> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                Log.e("error", t.getMessage());
                t.printStackTrace();

            }
        });


    }
}