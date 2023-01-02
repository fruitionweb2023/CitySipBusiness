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

import com.direct2web.citysip.Activities.Restaurent.SelectTimingActivity;
import com.direct2web.citysip.Activities.Restaurent.SetUpTimingListActivity;
import com.direct2web.citysip.Adapter.DoctorAdapters.DoctorGetTimingListAdapter;
import com.direct2web.citysip.Adapter.DoctorAdapters.DoctorSpinnerTimingTitleListAdapter;
import com.direct2web.citysip.Adapter.RestaurentAdapters.GetTimingListAdapter;
import com.direct2web.citysip.Model.DoctorModels.DoctorServices.ResponseDoctorServices;
import com.direct2web.citysip.Model.DoctorModels.DoctorServices.Service;
import com.direct2web.citysip.Model.DoctorModels.ResponseDoctorGetTiming;
import com.direct2web.citysip.Model.DoctorModels.Slot;
import com.direct2web.citysip.Model.DoctorModels.Timing;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.BottomButtonClickListner;
import com.direct2web.citysip.Utils.CustomListViewDialogWithSearch;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityDoctorSetUpTimingListBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorSetUpTimingListActivity extends AppCompatActivity implements DoctorSpinnerTimingTitleListAdapter.RecyclerViewItemClickListener {


    ActivityDoctorSetUpTimingListBinding binding;
    SessionManager sessionManager;
    BottomButtonClickListner bottomButtonClickListner;
    ProgressDialog pd;

    List<Service> serviceList = new ArrayList<>();
    DoctorSpinnerTimingTitleListAdapter titleListAdapter;
    CustomListViewDialogWithSearch customDialog;

    List<Timing> timeList = new ArrayList<>();
    DoctorGetTimingListAdapter adapter;


    List<Service> temp;
    String s_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_set_up_timing_list);

        sessionManager = new SessionManager(this);

        bottomButtonClickListner = new BottomButtonClickListner(this, sessionManager);

        binding.bottomnavigation.bbHome.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMyBusiness.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbOrder.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMenu.setOnClickListener(new BottomButtonClickListner(this, sessionManager));

        getServiceList(sessionManager.getUserId());

        binding.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DoctorSetUpTimingListActivity.this, DoctorSelectTimingActivtiy.class);
                startActivity(intent);

            }
        });

        binding.spinnerTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                titleListAdapter = new DoctorSpinnerTimingTitleListAdapter( temp,DoctorSetUpTimingListActivity.this);
                customDialog = new CustomListViewDialogWithSearch(DoctorSetUpTimingListActivity.this, titleListAdapter,temp);

                customDialog.show();
                customDialog.setCanceledOnTouchOutside(false);


            }
        });



    }

    private void getServiceList(String userId) {

        pd = new ProgressDialog(DoctorSetUpTimingListActivity.this);
        pd.setMessage("Please Wait.....");
        pd.setCancelable(false);
        pd.show();

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

                        Toast.makeText(DoctorSetUpTimingListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        serviceList = response.body().getServiceList();

                        temp = new ArrayList<>();


                        for (int i = 0; i < serviceList.size(); i++) {

                            temp.add(new Service(serviceList.get(i).getId(), serviceList.get(i).getDoctorName()+" - "+serviceList.get(i).getServiceName()));

                        }



                    }

                }else {
                    Toast.makeText(DoctorSetUpTimingListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
    public void clickOnPartyListItem(String data, String id) {

        binding.spinnerTitle.setText(data);
        binding.spinnerTitle.setTextColor(getResources().getColor(R.color.black));

        s_id = id;
        getTimeSlot(s_id);

        if (customDialog != null) {
            customDialog.dismiss();
        }

    }

    public void getTimeSlot(String id){

        pd = new ProgressDialog(DoctorSetUpTimingListActivity.this);
        pd.setMessage("Please Wait.....");
        pd.setCancelable(false);
        pd.show();

        Api api = RetrofitClient.getClient().create(Api.class);

        Call<ResponseDoctorGetTiming> call = api.getDoctorTiming("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key,id);

        call.enqueue(new Callback<ResponseDoctorGetTiming>() {
            @Override
            public void onResponse(Call<ResponseDoctorGetTiming> call, Response<ResponseDoctorGetTiming> response) {

                Log.e("responsegettiming",new Gson().toJson(response.body()));

                pd.dismiss();

                if (response.body() != null && response.isSuccessful()){

                    if (response.body().getError()){

                        Toast.makeText(DoctorSetUpTimingListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }else {

                        timeList = response.body().getTimingList();
                        adapter = new DoctorGetTimingListAdapter(timeList, DoctorSetUpTimingListActivity.this);
                        LinearLayoutManager manager = new LinearLayoutManager(DoctorSetUpTimingListActivity.this);
                        binding.rclrTimingList.setLayoutManager(manager);
                        binding.rclrTimingList.setAdapter(adapter);


                    }


                }else {
                    Toast.makeText(DoctorSetUpTimingListActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();


                }




            }

            @Override
            public void onFailure(Call<ResponseDoctorGetTiming> call, Throwable t) {

                pd.dismiss();
                t.printStackTrace();
                Log.e("error_timing",t.getMessage());

            }
        });

    }



}