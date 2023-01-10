package com.direct2web.citysip.Activities.SpaAndSalon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.direct2web.citysip.Adapter.SpaAndSalonAdapter.DeleteVideoSpaAndSalonAdapter;
import com.direct2web.citysip.Model.SpaAndSalon.DeleteData.ResponseSpaAndSalonDelete;
import com.direct2web.citysip.Model.SpaAndSalon.MediaLibrary.ResponseSpaAndSalonAddImageAndVedio;
import com.direct2web.citysip.Model.SpaAndSalon.MediaLibrary.Video;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityDoctorDeleteVideoBinding;
import com.direct2web.citysip.databinding.ActivitySpaSalonDeleteVideoBinding;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpaAndSalonDeleteVideoActivity extends AppCompatActivity {

    ActivitySpaSalonDeleteVideoBinding binding;
    SessionManager sessionManager;
    List<Video> videoList;
    ProgressDialog pd;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_spa_salon_delete_video);
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

        pd = new ProgressDialog(SpaAndSalonDeleteVideoActivity.this);
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
        Call<ResponseSpaAndSalonDelete> call = api.sendSpaAndSalonDeleteData("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId, "business_video", id);
        call.enqueue(new Callback<ResponseSpaAndSalonDelete>() {
            @Override
            public void onResponse(Call<ResponseSpaAndSalonDelete> call, Response<ResponseSpaAndSalonDelete> response) {
                Log.e("responseDeleteImage", new Gson().toJson(response.body()));

                pd.dismiss();
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(SpaAndSalonDeleteVideoActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        onBackPressed();
                    }


                } else {

                    Toast.makeText(SpaAndSalonDeleteVideoActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseSpaAndSalonDelete> call, Throwable t) {

                pd.dismiss();
                t.printStackTrace();
                Log.e("deleteImageError", t.getMessage());

            }
        });


    }

    public void getData(String userId) {

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseSpaAndSalonAddImageAndVedio> call = api.getSpaAndSalonImageAndVedioList("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId);
        call.enqueue(new Callback<ResponseSpaAndSalonAddImageAndVedio>() {
            @Override
            public void onResponse(Call<ResponseSpaAndSalonAddImageAndVedio> call, Response<ResponseSpaAndSalonAddImageAndVedio> response) {

                Log.e("ResponceImgVidList", new Gson().toJson(response.body()));

                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {
                        Toast.makeText(SpaAndSalonDeleteVideoActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {

                        videoList = response.body().getVideo();
                        int numberOfColumns = 3;
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(SpaAndSalonDeleteVideoActivity.this, numberOfColumns);
                        binding.rclrVideo.setLayoutManager(gridLayoutManager);
                        DeleteVideoSpaAndSalonAdapter deleteVideoAdapter = new DeleteVideoSpaAndSalonAdapter(SpaAndSalonDeleteVideoActivity.this, videoList);
                        binding.rclrVideo.setAdapter(deleteVideoAdapter);
                    }

                } else {

                    Toast.makeText(SpaAndSalonDeleteVideoActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseSpaAndSalonAddImageAndVedio> call, Throwable t) {

                t.printStackTrace();
                Log.e("error", t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SpaAndSalonDeleteVideoActivity.this, SpaAndSalonAddVedioActivity.class);
        finish();
        startActivity(intent);
    }
}