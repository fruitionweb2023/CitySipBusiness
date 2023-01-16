package com.direct2web.citysip.Activities.Doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.direct2web.citysip.Activities.Restaurent.MainActivity;
import com.direct2web.citysip.Activities.Restaurent.MenuActivity;
import com.direct2web.citysip.Activities.Restaurent.SetUpAddCouponsActivity;
import com.direct2web.citysip.Activities.Restaurent.SetUpCouponsActivity;
import com.direct2web.citysip.Activities.SpaAndSalon.SpaAndSalonAddServicesActivity;
import com.direct2web.citysip.Activities.SpaAndSalon.SpaAndSalonServicesActivity;
import com.direct2web.citysip.Adapter.DoctorAdapters.AppointmentListAdapter;
import com.direct2web.citysip.Adapter.DoctorAdapters.ServicesListAdapter;
import com.direct2web.citysip.Adapter.RestaurentAdapters.NewCouponsListItemAdapter;
import com.direct2web.citysip.Model.DoctorModels.DoctorServices.ResponseDoctorServices;
import com.direct2web.citysip.Model.DoctorModels.DoctorServices.Service;
import com.direct2web.citysip.Model.RestaurentModels.offer.Offer;
import com.direct2web.citysip.Model.RestaurentModels.offer.ResponseOfferList;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.BottomButtonClickListner;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityDoctorServicesBinding;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorServicesActivity extends AppCompatActivity implements ServicesListAdapter.OnItemClickListner {

    ActivityDoctorServicesBinding binding;
    SessionManager sessionManager;
    ProgressDialog pd;
    BottomButtonClickListner bottomButtonClickListner;
    List<Service> serviceList;
    ServicesListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_services);
        sessionManager = new SessionManager(this);

        bottomButtonClickListner = new BottomButtonClickListner(this, sessionManager);

        binding.bottomnavigation.bbImgMyBusiness.setColorFilter(getResources().getColor(R.color.clr_0059C8));

        binding.bottomnavigation.bbHome.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMyBusiness.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbOrder.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMenu.setOnClickListener(new BottomButtonClickListner(this, sessionManager));


        pd = new ProgressDialog(DoctorServicesActivity.this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(true);
        pd.show();

        getServiceList(sessionManager.getUserId());

        binding.addCoupons.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorServicesActivity.this, DoctorAddServicesActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getServiceList(String userId) {
        Api api = RetrofitClient.getClient().create(Api.class);

        Call<ResponseDoctorServices> call = api.getServices("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key,userId);
        call.enqueue(new Callback<ResponseDoctorServices>() {
            @Override
            public void onResponse(Call<ResponseDoctorServices> call, Response<ResponseDoctorServices> response) {
                Log.e("responseServiceList", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }

                if (response.body() != null && response.isSuccessful()) {
                    Log.e("userId", sessionManager.getUserId());

                    if (response.body().getError()) {

                        Toast.makeText(DoctorServicesActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        serviceList = response.body().getServiceList();

                        adapter = new ServicesListAdapter(serviceList, DoctorServicesActivity.this,DoctorServicesActivity.this);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DoctorServicesActivity.this);
                        binding.rcleAddServices.setLayoutManager(linearLayoutManager);
                        binding.rcleAddServices.setAdapter(adapter);
                    }

                }else {
                    Toast.makeText(DoctorServicesActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDoctorServices> call, Throwable t) {

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                Log.e("ServiceListError", t.getMessage());
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(DoctorServicesActivity.this, DoctorDeshboardActivity.class);
        finish();
        startActivity(i);
    }

    @Override
    public void onAddButtonClick(int postion) {
        Intent i = new Intent(DoctorServicesActivity.this, DoctorAddServicesActivity.class);
        i.putExtra("flag","1");
        i.putExtra("serviceName", serviceList.get(postion).getServiceName());
        i.putExtra("doctorName", serviceList.get(postion).getDoctorName());
        i.putExtra("amount", serviceList.get(postion).getAmount());
        i.putExtra("description", serviceList.get(postion).getDescription());
        i.putExtra("offer", serviceList.get(postion).getOffer());
        // i.putExtra("name", serviceList.get(postion).getName());
        i.putExtra("image", serviceList.get(postion).getImage());
        i.putExtra("serviceId", serviceList.get(postion).getId());
        finish();
        startActivity(i);
    }
}