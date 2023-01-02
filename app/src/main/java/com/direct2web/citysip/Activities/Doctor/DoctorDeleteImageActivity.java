package com.direct2web.citysip.Activities.Doctor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.direct2web.citysip.Adapter.DoctorAdapters.DeleteImageDoctorAdapter;
import com.direct2web.citysip.Model.DoctorModels.DoctorAddImage.ImageAndVedio.Image;
import com.direct2web.citysip.Model.DoctorModels.DoctorAddImage.ImageAndVedio.ResponseDoctorAddImageAndVedio;
import com.direct2web.citysip.Model.DoctorModels.DoctorDeleteData.ResponseDoctorDelete;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityDoctorDeleteImageBinding;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorDeleteImageActivity extends AppCompatActivity {

    ActivityDoctorDeleteImageBinding binding;
    SessionManager sessionManager;
    List<Image> imageList;
    DeleteImageDoctorAdapter deleteImageDoctorAdapter;
    String id = "";
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_delete_image);

        sessionManager = new SessionManager(this);

        getData(sessionManager.getUserId());

        binding.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteImage(sessionManager.getUserId());

            }
        });

    }

    private void deleteImage(String userId) {

        pd = new ProgressDialog(DoctorDeleteImageActivity.this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(false);
        pd.show();

        StringBuilder sb = new StringBuilder();
        for (Image u : imageList) {
            if (u.getSelected()) {
                sb.append(u.getId()).append("~~");
            }
        }
        id = sb.toString();
        if (!id.equals("")) {
            id = id.substring(0, id.length() - 2);
        }
        Log.e("string", id);

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseDoctorDelete> call = api.sendDeleteDataDoctor("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId, "business_photo", id);
        call.enqueue(new Callback<ResponseDoctorDelete>() {
            @Override
            public void onResponse(Call<ResponseDoctorDelete> call, Response<ResponseDoctorDelete> response) {
                Log.e("responseDeleteImage", new Gson().toJson(response.body()));

                pd.dismiss();
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(DoctorDeleteImageActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        onBackPressed();
                    }


                } else {

                    Toast.makeText(DoctorDeleteImageActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseDoctorDelete> call, Throwable t) {

                pd.dismiss();
                t.printStackTrace();
                Log.e("deleteImageError", t.getMessage());

            }
        });


    }

    public void getData(String userId) {

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseDoctorAddImageAndVedio> call = api.getImageAndVedioListDoctor("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId);
        call.enqueue(new Callback<ResponseDoctorAddImageAndVedio>() {
            @Override
            public void onResponse(Call<ResponseDoctorAddImageAndVedio> call, Response<ResponseDoctorAddImageAndVedio> response) {

                Log.e("ResponceImgVidList", new Gson().toJson(response.body()));

                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {
                        Toast.makeText(DoctorDeleteImageActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {

                        imageList = response.body().getImage();

                        int numberOfColumns = 3;
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(DoctorDeleteImageActivity.this, numberOfColumns);
                        binding.rclrImageDelete.setLayoutManager(gridLayoutManager);
                        deleteImageDoctorAdapter = new DeleteImageDoctorAdapter(imageList, DoctorDeleteImageActivity.this);
                        binding.rclrImageDelete.setAdapter(deleteImageDoctorAdapter);

                    }

                } else {

                    Toast.makeText(DoctorDeleteImageActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseDoctorAddImageAndVedio> call, Throwable t) {

                t.printStackTrace();
                Log.e("error", t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DoctorDeleteImageActivity.this, DoctorAddImageActivity.class);
        finish();
        startActivity(intent);
    }
}