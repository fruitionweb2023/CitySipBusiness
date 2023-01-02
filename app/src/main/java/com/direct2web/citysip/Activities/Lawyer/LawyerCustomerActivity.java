package com.direct2web.citysip.Activities.Lawyer;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.direct2web.citysip.Adapter.RestaurentAdapters.CustomerListAdapter;
import com.direct2web.citysip.Model.RestaurentModels.CustomerList.Customer;
import com.direct2web.citysip.Model.RestaurentModels.CustomerList.ResponseCustomerList;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.BottomButtonClickListner;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityDoctorCustomerBinding;
import com.direct2web.citysip.databinding.ActivityLawyerCustomerBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LawyerCustomerActivity extends AppCompatActivity {

    ActivityLawyerCustomerBinding binding;
    SessionManager sessionManager;
    BottomButtonClickListner bottomButtonClickListner;
    ProgressDialog pd;

    List<Customer> customerList = new ArrayList<>();
    CustomerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lawyer_customer);

        sessionManager = new SessionManager(this);
        /*bottomButtonClickListner = new BottomButtonClickListner(this, sessionManager);

        binding.bottomnavigation.bbHome.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMyBusiness.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbOrder.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMenu.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
*/

        binding.toolbar.toolbarBack.setVisibility(View.VISIBLE);

        binding.toolbar.toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        getData(sessionManager.getUserId());

    }

    private void getData(String userId) {

        pd = new ProgressDialog(LawyerCustomerActivity.this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(false);
        pd.show();

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseCustomerList> call = api.getLawyerCustomerList("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId);

        call.enqueue(new Callback<ResponseCustomerList>() {
            @Override
            public void onResponse(Call<ResponseCustomerList> call, Response<ResponseCustomerList> response) {

                Log.e("responseCustomer", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(LawyerCustomerActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        customerList = response.body().getCustomerList();

                        adapter = new CustomerListAdapter(customerList, LawyerCustomerActivity.this);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LawyerCustomerActivity.this);

                        binding.rclrCustomer.setLayoutManager(linearLayoutManager);
                        binding.rclrCustomer.setAdapter(adapter);


                    }

                } else {

                    Toast.makeText(LawyerCustomerActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();


                }


            }

            @Override
            public void onFailure(Call<ResponseCustomerList> call, Throwable t) {

                if (pd.isShowing()) {
                    pd.dismiss();
                }

                Log.e("error", t.getMessage());
                t.printStackTrace();

            }
        });


    }
}