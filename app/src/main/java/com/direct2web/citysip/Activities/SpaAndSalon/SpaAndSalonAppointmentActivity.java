package com.direct2web.citysip.Activities.SpaAndSalon;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.direct2web.citysip.Adapter.SpaAndSalonAdapter.AppointmentListSpaAndSalonAdapter;
import com.direct2web.citysip.Model.SpaAndSalon.Appointment.Order;
import com.direct2web.citysip.Model.SpaAndSalon.Appointment.ResponseSpaAndSalonAppointment;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.BottomButtonClickListner;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityDoctorAppointmentBinding;
import com.direct2web.citysip.databinding.ActivitySpaSalonAppointmentBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpaAndSalonAppointmentActivity extends AppCompatActivity {

    ActivitySpaSalonAppointmentBinding binding;
    SessionManager sessionManager;
    BottomButtonClickListner bottomButtonClickListner;
    ProgressDialog pd;
    AppointmentListSpaAndSalonAdapter adapter;

    List<Order> orderListDocotor = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_spa_salon_appointment);
        sessionManager = new SessionManager(this);

        pd = new ProgressDialog(this);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.show();


        getOrder(sessionManager.getUserId());

    }

    private void getOrder(String userId) {

        Api api = RetrofitClient.getClient().create(Api.class);

        Call<ResponseSpaAndSalonAppointment> call = api.getSpaAndSalonOrder("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId);

        call.enqueue(new Callback<ResponseSpaAndSalonAppointment>() {
            @Override
            public void onResponse(Call<ResponseSpaAndSalonAppointment> call, Response<ResponseSpaAndSalonAppointment> response) {

                Log.e("responseOrder", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(SpaAndSalonAppointmentActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        orderListDocotor = response.body().getOrderList();

                        adapter = new AppointmentListSpaAndSalonAdapter(orderListDocotor, SpaAndSalonAppointmentActivity.this);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SpaAndSalonAppointmentActivity.this);
                        binding.rclrOrder.setLayoutManager(linearLayoutManager);
                        binding.rclrOrder.setAdapter(adapter);


                    }


                } else {

                    Toast.makeText(SpaAndSalonAppointmentActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(Call<ResponseSpaAndSalonAppointment> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                Log.e("error", t.getMessage());
                t.printStackTrace();

            }
        });


    }
}