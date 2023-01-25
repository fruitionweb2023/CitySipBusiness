package com.direct2web.citysip.Activities.Restaurent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.direct2web.citysip.Model.RestaurentModels.Delete.ResponseStatus;
import com.direct2web.citysip.Model.RestaurentModels.aboutrestaurant.AboutResturant;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivitySetUpAboutRestaurantBinding;
import com.google.gson.Gson;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetUpAboutRestaurantActivity extends AppCompatActivity {

    ActivitySetUpAboutRestaurantBinding binding;
    SessionManager sessionManager;
    ProgressDialog pd;
   // String businessName,phoneNo,website,description,add1,add2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_up_about_restaurant);
        sessionManager = new SessionManager(this);
        pd = new ProgressDialog(SetUpAboutRestaurantActivity.this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(true);
        pd.show();

        getRestaurantDetails(sessionManager.getUserId());
        Toast.makeText(this, "BusinessId" + sessionManager.getUserId(), Toast.LENGTH_SHORT).show();


        binding.switchOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    binding.restauraneLayout.setAlpha(0.25f);
                    binding.locationLayout.setAlpha(0.25f);
                    binding.imgEdit.setClickable(false);
                    binding.imgEditLocation.setClickable(false);
                    sendStatus(sessionManager.getUserId(),"business_detail",sessionManager.getUserId(),"0");

                } else {

                    binding.restauraneLayout.setAlpha(1.0f);
                    binding.locationLayout.setAlpha(1.0f);
                    binding.imgEdit.setClickable(true);
                    binding.imgEditLocation.setClickable(true);
                    sendStatus(sessionManager.getUserId(),"business_detail",sessionManager.getUserId(),"1");


                }
            }
        });

        binding.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SetUpAboutRestaurantActivity.this, EditRestaurantDetailsActivity.class);
                i.putExtra("contect",binding.txtPhoneNo.getText().toString());
                i.putExtra("website",binding.txtWebsite.getText().toString());
                i.putExtra("about",binding.txtDescription.getText().toString());
                i.putExtra("businessName",binding.txtBusignessName.getText().toString());
                startActivity(i);
            }
        });




    }

    private void getRestaurantDetails(String userId) {
        Log.e("AboutBusiness : ", userId );

        Api api = RetrofitClient.getClient().create(Api.class);

        Call<AboutResturant> call = api.getBusignessDetails("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId);
        call.enqueue(new Callback<AboutResturant>() {
            @Override
            public void onResponse(Call<AboutResturant> call, Response<AboutResturant> response) {
                Log.e("ResponseAboutRestaurant", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(SetUpAboutRestaurantActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Log.e("userId", sessionManager.getUserId());

                        String address = response.body().getAddressLine1() + "\n" + response.body().getAddressLine2();

                        binding.txtBusignessName.setText(response.body().getBusinessName());
                        binding.txtWebsite.setText(response.body().getWebsite());
                        binding.txtBusignessName2.setText(response.body().getBusinessName());
                        binding.txtDescription.setText(response.body().getDescription());
                        binding.txtPhoneNo.setText(response.body().getPhoneNo());
                        binding.txtAddress.setText(address);

                        if (response.body().getStatus().equals("1")){

                            binding.switchOnOff.setChecked(true);
                            binding.restauraneLayout.setAlpha(1.0f);
                            binding.imgEdit.setClickable(true);
                            binding.imgEditLocation.setClickable(true);
                            binding.locationLayout.setAlpha(1.0f);


                        }else {
                            binding.switchOnOff.setChecked(false);
                            binding.restauraneLayout.setAlpha(0.25f);
                            binding.imgEdit.setClickable(false);
                            binding.imgEditLocation.setClickable(false);
                            binding.locationLayout.setAlpha(0.25f);


                        }

                        binding.imgEditLocation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent i = new Intent(SetUpAboutRestaurantActivity.this, EditLocationFetchActivity.class);
                                i.putExtra("lat",response.body().getLatitude());
                                i.putExtra("lang",response.body().getLongitude());
                                i.putExtra("address1",response.body().getAddressLine1());
                                i.putExtra("address2",response.body().getAddressLine2());
                                startActivity(i);
                            }
                        });

                    }

                }else {
                    //Toast.makeText(SetUpAboutRestaurantActivity.this, Objects.requireNonNull(response.body()).getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<AboutResturant> call, Throwable t) {

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                Log.e("error", t.getMessage());
                t.printStackTrace();
            }
        });
    }

    public void sendStatus(String userId,String type,String id,String status){

       /* pd = new ProgressDialog(SetUpCouponsActivity.this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(false);
        pd.show();
*/
        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseStatus> call = api.sendStatus("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId, type, id,status);
        call.enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {

                Log.e("responseDelete", new Gson().toJson(response.body()));

               /* if (pd.isShowing()) {
                    pd.dismiss();
                }*/

                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(SetUpAboutRestaurantActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } /*else {

                        Toast.makeText(SetUpCouponsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();


                    }*/

                } else {
                    Toast.makeText(SetUpAboutRestaurantActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {

               /* if (pd.isShowing()) {
                    pd.dismiss();
                }*/
                t.printStackTrace();
                Log.e("errorDelete", t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(SetUpAboutRestaurantActivity.this, MenuActivity.class);
        finish();
        startActivity(i);
    }

}