package com.direct2web.citysip.Activities.Lawyer;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.direct2web.citysip.Activities.Doctor.DoctorServicesActivity;
import com.direct2web.citysip.Activities.Restaurent.ImagePickerActivity;
import com.direct2web.citysip.Model.DoctorModels.DoctorServices.ResponseDoctorAddService;
import com.direct2web.citysip.Model.LawyerModels.LawyerServices.ResponseLawyerAddService;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityDoctorAddServicesBinding;
import com.direct2web.citysip.databinding.ActivityLawyerAddServicesBinding;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LawyerAddServicesActivity extends AppCompatActivity {

    ActivityLawyerAddServicesBinding binding;
    SessionManager sessionManager;
    ProgressDialog pd;
    public static final int REQUEST_IMAGE = 100;
    String img2 = "";
    Bitmap bitmap2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lawyer_add_services);
        sessionManager = new SessionManager(this);




        binding.imgDishLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProfileImageClick();
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.txtName.getText().toString().equals("")) {

                    Toast.makeText(getApplicationContext(), "Enter Dish Name", Toast.LENGTH_SHORT).show();

                } else if (binding.txtService.getText().toString().equals("")) {

                    Toast.makeText(getApplicationContext(), "Enter Dish Price", Toast.LENGTH_SHORT).show();

                } else if (binding.txtAboutService.equals("")) {

                    Toast.makeText(getApplicationContext(), "Please Select Cuisines First", Toast.LENGTH_SHORT).show();

                }else {

                    sendSetUpMenu();
                }
            }
        });
    }

    private void sendSetUpMenu() {

        pd = new ProgressDialog(LawyerAddServicesActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();


        String authHeader = "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject);
        String accesskey = WS_URL_PARAMS.access_key;
        String business_id = sessionManager.getUserId();
        String name = binding.txtName.getText().toString();
        String service = binding.txtService.getText().toString();
        String description = binding.txtAboutService.getText().toString();
        String price = "";


        RequestBody t1 = RequestBody.create(MediaType.parse("multipart/form-data"), authHeader);
        RequestBody t2 = RequestBody.create(MediaType.parse("multipart/form-data"), accesskey);
        RequestBody t3 = RequestBody.create(MediaType.parse("multipart/form-data"), business_id);
        RequestBody t4 = RequestBody.create(MediaType.parse("multipart/form-data"), name);
        RequestBody t5 = RequestBody.create(MediaType.parse("multipart/form-data"), service);
        RequestBody t6 = RequestBody.create(MediaType.parse("multipart/form-data"), description);
        RequestBody t7 = RequestBody.create(MediaType.parse("multipart/form-data"), price);
        RequestBody t8 = RequestBody.create(MediaType.parse("multipart/form-data"), "");


        File file = null;
        MultipartBody.Part body1 = null;
        if (!img2.equals("")) {
            file = new File(img2);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

            String s = name + "_" + file.getName();
            String logo = s.replaceAll(" ", "_");
            body1 = MultipartBody.Part.createFormData("image", logo, requestFile);
        }


        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseLawyerAddService> call = api.sendLawyerService(authHeader,t2,t3,t4,t5,t7,t6,t8,body1);

        call.enqueue(new Callback<ResponseLawyerAddService>() {
            @Override
            public void onResponse(Call<ResponseLawyerAddService> call, Response<ResponseLawyerAddService> response) {

                Log.e("responceAddService", new Gson().toJson(response.body()));
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(LawyerAddServicesActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        Intent intent = new Intent(LawyerAddServicesActivity.this, LawyerServicesActivity.class);
                        startActivity(intent);
                    }

                } else {
                    Toast.makeText(LawyerAddServicesActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseLawyerAddService> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                t.printStackTrace();
                Log.e("Serviceerror", t.getMessage());
            }
        });
    }



    void onProfileImageClick() {
        Dexter.withActivity(LawyerAddServicesActivity.this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(LawyerAddServicesActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                Uri selectedImage = data.getData();
                if (uri != null) {
                    try {
                        // You can update this bitmap to your server
                        bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        selectedImage = getImageUri(getApplicationContext(), bitmap2);
                        Log.e("uripath", "" + selectedImage);

                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        img2 = cursor.getString(columnIndex);
                        cursor.close();

                        Log.e("path", img2);
                        // loading profile image from local cache
                        loadProfile(uri.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    private void loadProfile(String url) {
        Log.e("TAG....", "Image cache path: " + url);

        Glide.with(this).load(url)
                .into(binding.imgDishLogo);
        binding.imgDishLogo.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(LawyerAddServicesActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LawyerAddServicesActivity.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(LawyerAddServicesActivity.this, LawyerServicesActivity.class);
        finish();
        startActivity(i);
    }
}