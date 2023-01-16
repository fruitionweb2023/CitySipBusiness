package com.direct2web.citysip.Activities.Insurance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.direct2web.citysip.Activities.Lawyer.LawyerAddServicesActivity;
import com.direct2web.citysip.Activities.Lawyer.LawyerDashboardActivity;
import com.direct2web.citysip.Adapter.InsuranceAdapters.ServicesListAdapterInsurance;
import com.direct2web.citysip.Adapter.LawyerAdapters.ServicesListAdapterLawyer;
import com.direct2web.citysip.Model.InsuranceModel.InsuranceServices.ResponseInsuranceServices;
import com.direct2web.citysip.Model.InsuranceModel.InsuranceServices.Service;
import com.direct2web.citysip.Model.LawyerModels.LawyerServices.ResponseLawyerServices;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.BottomButtonClickListner;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityInsuranceServicesBinding;
import com.direct2web.citysip.databinding.ActivityLawyerServicesBinding;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsuranceServicesActivity extends AppCompatActivity implements ServicesListAdapterInsurance.OnItemClickListner {

    ActivityInsuranceServicesBinding binding;
    SessionManager sessionManager;
    ProgressDialog pd;
    BottomButtonClickListner bottomButtonClickListner;
    List<Service> serviceList;
    ServicesListAdapterInsurance adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_insurance_services);
        sessionManager = new SessionManager(this);

        bottomButtonClickListner = new BottomButtonClickListner(this, sessionManager);

        binding.bottomnavigation.bbImgMyBusiness.setColorFilter(getResources().getColor(R.color.clr_2C3564));

        binding.bottomnavigation.bbOrder.setVisibility(View.GONE);
        binding.bottomnavigation.bbAppointment.setVisibility(View.VISIBLE);

        binding.bottomnavigation.bbHome.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMyBusiness.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbAppointment.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMenu.setOnClickListener(new BottomButtonClickListner(this, sessionManager));


        pd = new ProgressDialog(InsuranceServicesActivity.this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(true);
        pd.show();

        getServiceList(sessionManager.getUserId());

        binding.addCoupons.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InsuranceServicesActivity.this, InsuranceAddServicesActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getServiceList(String userId) {
        Api api = RetrofitClient.getClient().create(Api.class);

        Call<ResponseInsuranceServices> call = api.getInsuranceServices("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key,userId);
        call.enqueue(new Callback<ResponseInsuranceServices>() {
            @Override
            public void onResponse(Call<ResponseInsuranceServices> call, Response<ResponseInsuranceServices> response) {
                Log.e("responseServiceList", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }

                if (response.body() != null && response.isSuccessful()) {
                    Log.e("userId", sessionManager.getUserId());

                    if (response.body().getError()) {

                        Toast.makeText(InsuranceServicesActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        serviceList = response.body().getServiceList();

                        adapter = new ServicesListAdapterInsurance(serviceList, InsuranceServicesActivity.this,InsuranceServicesActivity.this);
                        binding.rcleAddServices.setAdapter(adapter);
                    }

                }else {
                    Toast.makeText(InsuranceServicesActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseInsuranceServices> call, Throwable t) {

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
        Intent i = new Intent(InsuranceServicesActivity.this, InsuranceDashboardActivity.class);
        finish();
        startActivity(i);
    }

    @Override
    public void onAddButtonClick(int postion) {

        Intent i = new Intent(InsuranceServicesActivity.this, InsuranceAddServicesActivity.class);
        i.putExtra("flag", "1");
        i.putExtra("company_id", serviceList.get(postion).getCompanyId());
        i.putExtra("service_id", serviceList.get(postion).getServiceId());
        i.putExtra("id", serviceList.get(postion).getId());
        i.putExtra("serviceName", serviceList.get(postion).getServiceName());
        i.putExtra("description", serviceList.get(postion).getDescription());

        Log.e("Service send Data : ",
                "\ncompany_id : " + serviceList.get(postion).getCompanyId() +
                " \nservice_id : " + serviceList.get(postion).getServiceId() +
                "\nid : " + serviceList.get(postion).getId() +
                "\nserviceName : " + serviceList.get(postion).getServiceName() +
                "\ndescription : " + serviceList.get(postion).getDescription()
        );

        finish();
        startActivity(i);

    }
}