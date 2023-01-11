package com.direct2web.citysip.Activities.SpaAndSalon;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.direct2web.citysip.Activities.Doctor.DoctorSelectTimingActivtiy;
import com.direct2web.citysip.Activities.Restaurent.ImagePickerActivity;
import com.direct2web.citysip.Model.SpaAndSalon.Services.ResponseSpaAndSalonAddService;
import com.direct2web.citysip.Model.SpaAndSalon.Staff.ResponseAddStaff;
import com.direct2web.citysip.Model.SpaAndSalon.Staff.ResponseEditStaff;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivitySpaSalonAddServicesBinding;
import com.direct2web.citysip.databinding.ActivitySpaSalonAddStaffBinding;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpaAndSalonAddStaffActivity extends AppCompatActivity {

    ActivitySpaSalonAddStaffBinding binding;
    SessionManager sessionManager;
    ProgressDialog pd;
    public static final int REQUEST_IMAGE = 100;
    String img2 = "";
    Bitmap bitmap2;
    Calendar myCalendar = Calendar.getInstance();
    private static int flag_calander = 0;
    String add_open = "0", add_close = "0";
    String couponId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_spa_salon_add_staff);
        sessionManager = new SessionManager(this);


        couponId = getIntent().getStringExtra("staffId");

        if (Objects.equals(getIntent().getStringExtra("flag"), "1")) {
            binding.loginToYo.setText("Edit Staff");
            binding.txtName.setText(getIntent().getStringExtra("staffName"));
            binding.txtAddSelectFromTime.setText(getIntent().getStringExtra("timeFrom"));
            binding.txtAddSelectToTime.setText(getIntent().getStringExtra("timeTo"));
            binding.txtAboutService.setText(getIntent().getStringExtra("description"));
            binding.btnSubmit.setText("Edit");
            couponId = getIntent().getStringExtra("staffId");
            img2 = getIntent().getStringExtra("image");
            Glide.with(this).load(Api.imageUrl + img2)
                    .thumbnail(0.5f)
                    .error(R.drawable.city_sip_logo)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(binding.imgDishLogo);
        }



        binding.imgDishLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProfileImageClick();
            }
        });

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                updateLabel();
            }
        };

        binding.txtAddSelectFromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar1 = Calendar.getInstance();
                TimePickerDialog datePickerDialog = new TimePickerDialog(SpaAndSalonAddStaffActivity.this, time, calendar1
                        .get(Calendar.HOUR_OF_DAY), calendar1.get(Calendar.MINUTE), false) {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        super.onClick(dialog, which);
                        if (which == dialog.BUTTON_NEGATIVE) {
                            add_open = "0";
                            binding.txtAddSelectFromTime.setText("Select");
                        }
                    }

                };
                datePickerDialog.show();
                flag_calander = 1;

            }
        });

        binding.txtAddSelectToTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar1 = Calendar.getInstance();
                TimePickerDialog datePickerDialog = new TimePickerDialog(SpaAndSalonAddStaffActivity.this, time, calendar1
                        .get(Calendar.HOUR_OF_DAY), calendar1.get(Calendar.MINUTE), false) {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        super.onClick(dialog, which);
                        if (which == dialog.BUTTON_NEGATIVE) {
                            add_close = "0";
                            binding.txtAddSelectToTime.setText("Select");
                        }
                    }

                };
                datePickerDialog.show();
                flag_calander = 2;

            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.txtName.getText().toString().equals("")) {

                    Toast.makeText(getApplicationContext(), "Enter Staff Name", Toast.LENGTH_SHORT).show();

                } else if (binding.txtAddSelectFromTime.getText().toString().equals("Select")) {

                    Toast.makeText(getApplicationContext(), "Enter Time-from", Toast.LENGTH_SHORT).show();

                } else if (binding.txtAddSelectToTime.getText().toString().equals("Select")) {

                    Toast.makeText(getApplicationContext(), "Enter Time-to", Toast.LENGTH_SHORT).show();

                } else if (binding.txtAboutService.equals("")) {

                    Toast.makeText(getApplicationContext(), "Enter About", Toast.LENGTH_SHORT).show();

                }else {

                    if (Objects.equals(getIntent().getStringExtra("flag"), "1")) {
                        editStaff();
                    } else {
                        sendSetUpMenu();
                    }
                }
            }
        });
    }

    private void sendSetUpMenu() {

        pd = new ProgressDialog(SpaAndSalonAddStaffActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();


        String authHeader = "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject);
        String accesskey = WS_URL_PARAMS.access_key;
        String business_id = sessionManager.getUserId();
        String name = binding.txtName.getText().toString();
        String from = binding.txtAddSelectFromTime.getText().toString();
        String to = binding.txtAddSelectToTime.getText().toString();
        String about = binding.txtAboutService.getText().toString();


        RequestBody t1 = RequestBody.create(MediaType.parse("multipart/form-data"), authHeader);
        RequestBody t2 = RequestBody.create(MediaType.parse("multipart/form-data"), accesskey);
        RequestBody t3 = RequestBody.create(MediaType.parse("multipart/form-data"), business_id);
        RequestBody t4 = RequestBody.create(MediaType.parse("multipart/form-data"), name);
        RequestBody t5 = RequestBody.create(MediaType.parse("multipart/form-data"), from);
        RequestBody t6 = RequestBody.create(MediaType.parse("multipart/form-data"), to);
        RequestBody t7 = RequestBody.create(MediaType.parse("multipart/form-data"), about);


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
        Call<ResponseAddStaff> call = api.sendSpaAndSalonStaff(authHeader,t2,t3,t4,t7,t5,t6,body1);

        call.enqueue(new Callback<ResponseAddStaff>() {
            @Override
            public void onResponse(Call<ResponseAddStaff> call, Response<ResponseAddStaff> response) {

                Log.e("responceAddStaff", new Gson().toJson(response.body()));
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(SpaAndSalonAddStaffActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        Intent intent = new Intent(SpaAndSalonAddStaffActivity.this, SpaAndSalonStaffActivity.class);
                        startActivity(intent);
                    }

                } else {
                    Toast.makeText(SpaAndSalonAddStaffActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseAddStaff> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                t.printStackTrace();
                Log.e("Stafferror", t.getMessage());
            }
        });
    }

    private void editStaff() {

        pd = new ProgressDialog(SpaAndSalonAddStaffActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();


        String authHeader = "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject);
        String accesskey = WS_URL_PARAMS.access_key;
        String business_id = sessionManager.getUserId();
        String name = binding.txtName.getText().toString();
        String about = binding.txtAboutService.getText().toString();
        String from = binding.txtAddSelectFromTime.getText().toString();
        String to = binding.txtAddSelectToTime.getText().toString();
        String serviceId = couponId;


        RequestBody t1 = RequestBody.create(MediaType.parse("multipart/form-data"), authHeader);
        RequestBody t2 = RequestBody.create(MediaType.parse("multipart/form-data"), accesskey);
        RequestBody t3 = RequestBody.create(MediaType.parse("multipart/form-data"), business_id);
        RequestBody t4 = RequestBody.create(MediaType.parse("multipart/form-data"), name);
        RequestBody t5 = RequestBody.create(MediaType.parse("multipart/form-data"), about);
        RequestBody t6 = RequestBody.create(MediaType.parse("multipart/form-data"), from);
        RequestBody t7 = RequestBody.create(MediaType.parse("multipart/form-data"), to);
        RequestBody t8 = RequestBody.create(MediaType.parse("multipart/form-data"), serviceId);


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
        Call<ResponseEditStaff> call = api.editSpaAndSalonStaff(authHeader,t2,t3,t4,t5,t6,t7,t8,body1);

        call.enqueue(new Callback<ResponseEditStaff>() {
            @Override
            public void onResponse(Call<ResponseEditStaff> call, Response<ResponseEditStaff> response) {

                Log.e("ResponceEditStaff", new Gson().toJson(response.body()));
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(SpaAndSalonAddStaffActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        Intent intent = new Intent(SpaAndSalonAddStaffActivity.this, SpaAndSalonStaffActivity.class);
                        startActivity(intent);
                    }

                } else {
                    Toast.makeText(SpaAndSalonAddStaffActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseEditStaff> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                t.printStackTrace();
                Log.e("StaffEditError", t.getMessage());
            }
        });
    }



    void onProfileImageClick() {
        Dexter.withActivity(SpaAndSalonAddStaffActivity.this)
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
        Intent intent = new Intent(SpaAndSalonAddStaffActivity.this, ImagePickerActivity.class);
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
        Intent intent = new Intent(SpaAndSalonAddStaffActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SpaAndSalonAddStaffActivity.this);
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
        Intent i = new Intent(SpaAndSalonAddStaffActivity.this, SpaAndSalonStaffActivity.class);
        finish();
        startActivity(i);
    }
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        //SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        if (flag_calander == 1) {
            add_open = sdf.format(myCalendar.getTime());
            binding.txtAddSelectFromTime.setText(sdf.format(myCalendar.getTime()));
        } else if (flag_calander == 2) {
            add_close = sdf.format(myCalendar.getTime());
            binding.txtAddSelectToTime.setText(sdf.format(myCalendar.getTime())
            );
        }

    }

}