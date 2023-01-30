package com.direct2web.citysip.Activities.Restaurent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.direct2web.citysip.Activities.Doctor.DoctorDeleteImageActivity;
import com.direct2web.citysip.Adapter.RestaurentAdapters.DeleteImageAdapter;
import com.direct2web.citysip.Adapter.RestaurentAdapters.SetImageAdapter;
import com.direct2web.citysip.Model.DoctorModels.DoctorDeleteData.ResponseDoctorDelete;
import com.direct2web.citysip.Model.RestaurentModels.Delete.ResponseDelete;
import com.direct2web.citysip.Model.RestaurentModels.ImageVideo.Image;
import com.direct2web.citysip.Model.RestaurentModels.ImageVideo.ResponseImageVideoList;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityDeleteImageBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteImageActivity extends AppCompatActivity {

    ActivityDeleteImageBinding binding;
    List<Image> imagesEncodedList = new ArrayList<>();
    SessionManager sessionManager;
    ProgressDialog pd;
    DeleteImageAdapter deleteImageAdapter;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_delete_image);

        sessionManager = new SessionManager(DeleteImageActivity.this);

        getData(sessionManager.getUserId());

            binding.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteSelectedItems(sessionManager.getUserId());
                }
            });
    }



    private void deleteSelectedItems( String userId) {

        pd = new ProgressDialog(DeleteImageActivity.this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(false);
        pd.show();

        StringBuilder sb = new StringBuilder();
        for (Image u : imagesEncodedList) {
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
        Call<ResponseDelete> call = api.sendDelete("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId, "business_photo", id);
        call.enqueue(new Callback<ResponseDelete>() {
            @Override
            public void onResponse(Call<ResponseDelete> call, Response<ResponseDelete> response) {
                Log.e("responseDeleteImage", new Gson().toJson(response.body()));

                pd.dismiss();
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(DeleteImageActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
//                        getData(userId);
                    onBackPressed();
                    }


                } else {

                    Toast.makeText(DeleteImageActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseDelete> call, Throwable t) {

                pd.dismiss();
                t.printStackTrace();
                Log.e("deleteImageError", t.getMessage());

            }
        });

    }


    public void getData(String userId) {

        pd = new ProgressDialog(DeleteImageActivity.this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(false);
        pd.show();

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseImageVideoList> call = api.getMedia("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key,userId);
        call.enqueue(new Callback<ResponseImageVideoList>() {
            @Override
            public void onResponse(Call<ResponseImageVideoList> call, Response<ResponseImageVideoList> response) {

                Log.e("ResponceImgVidList", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {
                        Toast.makeText(DeleteImageActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {

                        imagesEncodedList = response.body().getImage();

                        int numberOfColumns = 3;
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(DeleteImageActivity.this, numberOfColumns);
                        binding.rclrImage.setLayoutManager(gridLayoutManager);
                        // mAdapter = new DeleteImageAdapter(this, getListData());
                        deleteImageAdapter = new DeleteImageAdapter(DeleteImageActivity.this,imagesEncodedList);
                        binding.rclrImage.setAdapter(deleteImageAdapter);

                    }


                } else {

                    Toast.makeText(DeleteImageActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(Call<ResponseImageVideoList> call, Throwable t) {

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                t.printStackTrace();
                Log.e("deleteImageError", t.getMessage());
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DeleteImageActivity.this, ImageListActivity.class);
        finish();
        startActivity(intent);
    }

}