package com.direct2web.citysip.Activities.Doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.direct2web.citysip.Activities.Restaurent.OrderListActivity;
import com.direct2web.citysip.Adapter.DoctorAdapters.AppointmentListAdapter;
import com.direct2web.citysip.Adapter.RestaurentAdapters.OrderListAdapter;
import com.direct2web.citysip.Model.DoctorModels.DoctorAppointment.Order;
import com.direct2web.citysip.Model.DoctorModels.DoctorAppointment.ResponseDoctorAppointment;
import com.direct2web.citysip.Model.RestaurentModels.OrderList.ResponseOrderList;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.BottomButtonClickListner;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityDoctorAppointmentBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorAppointmentActivity extends AppCompatActivity {

    ActivityDoctorAppointmentBinding binding;
    SessionManager sessionManager;
    BottomButtonClickListner bottomButtonClickListner;
    ProgressDialog pd;
    AppointmentListAdapter adapter;

    List<Order> orderListDocotor = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_appointment);
        sessionManager = new SessionManager(this);

        pd = new ProgressDialog(this);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.show();


        getOrder(sessionManager.getUserId());

    }

    private void getOrder(String userId) {

        Api api = RetrofitClient.getClient().create(Api.class);

        Call<ResponseDoctorAppointment> call = api.getOrder("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId);

        call.enqueue(new Callback<ResponseDoctorAppointment>() {
            @Override
            public void onResponse(Call<ResponseDoctorAppointment> call, Response<ResponseDoctorAppointment> response) {

                Log.e("responseOrder", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(DoctorAppointmentActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        orderListDocotor = response.body().getOrderList();

                        adapter = new AppointmentListAdapter(orderListDocotor, DoctorAppointmentActivity.this);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DoctorAppointmentActivity.this);
                        binding.rclrOrder.setLayoutManager(linearLayoutManager);
                        binding.rclrOrder.setAdapter(adapter);


                    }


                } else {

                    Toast.makeText(DoctorAppointmentActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(Call<ResponseDoctorAppointment> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                Log.e("error", t.getMessage());
                t.printStackTrace();

            }
        });


    }
}