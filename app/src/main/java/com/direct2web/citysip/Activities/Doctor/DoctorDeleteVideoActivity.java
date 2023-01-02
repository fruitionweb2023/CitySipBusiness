package com.direct2web.citysip.Activities.Doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.direct2web.citysip.Adapter.DoctorAdapters.DeleteVideoDoctorAdapter;
import com.direct2web.citysip.Model.DoctorModels.DoctorAddImage.ImageAndVedio.Image;
import com.direct2web.citysip.Model.DoctorModels.DoctorAddImage.ImageAndVedio.ResponseDoctorAddImageAndVedio;
import com.direct2web.citysip.Model.DoctorModels.DoctorAddImage.ImageAndVedio.Video;
import com.direct2web.citysip.Model.DoctorModels.DoctorDeleteData.ResponseDoctorDelete;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityDoctorDeleteVideoBinding;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorDeleteVideoActivity extends AppCompatActivity {

    ActivityDoctorDeleteVideoBinding binding;
    SessionManager sessionManager;
    List<Video> videoList;
    ProgressDialog pd;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_delete_video);
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

        pd = new ProgressDialog(DoctorDeleteVideoActivity.this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(false);
        pd.show();

        StringBuilder sb = new StringBuilder();
        for (Video u : videoList) {
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
                WS_URL_PARAMS.access_key, userId, "business_video", id);
        call.enqueue(new Callback<ResponseDoctorDelete>() {
            @Override
            public void onResponse(Call<ResponseDoctorDelete> call, Response<ResponseDoctorDelete> response) {
                Log.e("responseDeleteImage", new Gson().toJson(response.body()));

                pd.dismiss();
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(DoctorDeleteVideoActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        onBackPressed();
                    }


                } else {

                    Toast.makeText(DoctorDeleteVideoActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(DoctorDeleteVideoActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {

                        videoList = response.body().getVideo();
                        int numberOfColumns = 3;
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(DoctorDeleteVideoActivity.this, numberOfColumns);
                        binding.rclrVideo.setLayoutManager(gridLayoutManager);
                        DeleteVideoDoctorAdapter deleteVideoAdapter = new DeleteVideoDoctorAdapter(DoctorDeleteVideoActivity.this, videoList);
                        binding.rclrVideo.setAdapter(deleteVideoAdapter);
                    }

                } else {

                    Toast.makeText(DoctorDeleteVideoActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(DoctorDeleteVideoActivity.this, DoctorAddVedioActivity.class);
        finish();
        startActivity(intent);
    }
}