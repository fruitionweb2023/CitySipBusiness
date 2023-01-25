package com.direct2web.citysip.Activities.Insurance;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.direct2web.citysip.Activities.Doctor.DoctorServicesActivity;
import com.direct2web.citysip.Activities.Restaurent.ImagePickerActivity;
import com.direct2web.citysip.Activities.Restaurent.SetUpAddMenuActivity;
import com.direct2web.citysip.Adapter.InsuranceAdapters.AddCompnyAdapterInsurance;
import com.direct2web.citysip.Adapter.InsuranceAdapters.InsuranceCustomListViewDialogWithSearchAdapter;
import com.direct2web.citysip.Adapter.InsuranceAdapters.InsuranceServiceTitleListAdapter;
import com.direct2web.citysip.Adapter.InsuranceAdapters.SelectCompanyNameAdapter;
import com.direct2web.citysip.Adapter.RestaurentAdapters.AddCouponAddMenuAdapter;
import com.direct2web.citysip.Adapter.RestaurentAdapters.SelectOfferNameAdapter;
import com.direct2web.citysip.Model.DoctorModels.DoctorServices.ResponseEditDoctorService;
import com.direct2web.citysip.Model.InsuranceModel.InsuranceServices.CompanyList;
import com.direct2web.citysip.Model.InsuranceModel.InsuranceServices.ResponseInsuranceAddService;
import com.direct2web.citysip.Model.InsuranceModel.InsuranceServices.ResponseInsuranceServiceAndCompanyList;
import com.direct2web.citysip.Model.InsuranceModel.InsuranceServices.ServiceList;
import com.direct2web.citysip.Model.RestaurentModels.offer.Offer;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.CustomListViewDialog;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityInsuranceAddServicesBinding;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsuranceAddServicesActivity extends AppCompatActivity implements InsuranceServiceTitleListAdapter.RecyclerViewItemClickListener, AddCompnyAdapterInsurance.OnCrossItemClickListner, AddCompnyAdapterInsurance.OnItemClickListner, SelectCompanyNameAdapter.RecyclerViewItemClickListener {

    public static final int REQUEST_IMAGE = 100;
    ActivityInsuranceAddServicesBinding binding;
    SessionManager sessionManager;
    ProgressDialog pd;
    String img2 = "";
    Bitmap bitmap2;

    List<ServiceList> serviceLists = new ArrayList<>();
    List<CompanyList> companyLists = new ArrayList<>();
    List<CompanyList> temp = new ArrayList<>();

    InsuranceServiceTitleListAdapter titleListAdapter;
    InsuranceCustomListViewDialogWithSearchAdapter customDialog;
    String serviceId;

    AddCompnyAdapterInsurance adapter;
    ArrayList<CompanyList> addDishType = new ArrayList<CompanyList>();
    AddCompnyAdapterInsurance typeListAdapter;
    CustomListViewDialog customDialog1;
    String couponId = "";
    String companyId = "";
    String cId = "";
    String cDesc = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_insurance_add_services);
        sessionManager = new SessionManager(this);


        getData();

        couponId = getIntent().getStringExtra("id");


        if (Objects.equals(getIntent().getStringExtra("flag"), "1")) {
            binding.loginToYo.setText("Edit Service");
            companyId = getIntent().getStringExtra("company_id");
            serviceId = getIntent().getStringExtra("service_id");
            couponId = getIntent().getStringExtra("id");
            binding.txtService.setText(getIntent().getStringExtra("serviceName"));
            binding.btnSubmit.setText("Edit");

            Log.e("Service get Data : ",
                    "\ncompany_id : " + companyId +
                            " \nservice_id : " + serviceId +
                            "\nid : " + couponId +
                            "\nserviceName : " + getIntent().getStringExtra("serviceName") +
                            "\ndescription : " + getIntent().getStringExtra("description")
            );

            String[] array = getIntent().getStringExtra("company_id").split("~~");
            String[] array2 = getIntent().getStringExtra("description").split(",");

            addDishType.clear();
            for (int i=0;i<array.length;i++) {
                addDishType.add(new CompanyList(array[i],array2[i]));
                cId = array[i];
                cDesc = array2[i];

            }

        }




        typeListAdapter = new AddCompnyAdapterInsurance(InsuranceAddServicesActivity.this, addDishType, InsuranceAddServicesActivity.this, InsuranceAddServicesActivity.this);

        GridLayoutManager layoutManager = new GridLayoutManager(InsuranceAddServicesActivity.this, 1, GridLayoutManager.HORIZONTAL, false);

        binding.rclrCouponList.setLayoutManager(layoutManager);
        binding.rclrCouponList.setAdapter(typeListAdapter);

        binding.txtService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                titleListAdapter = new InsuranceServiceTitleListAdapter(serviceLists, InsuranceAddServicesActivity.this);
                customDialog = new InsuranceCustomListViewDialogWithSearchAdapter(InsuranceAddServicesActivity.this, titleListAdapter, serviceLists);

                customDialog.show();
                customDialog.setCanceledOnTouchOutside(false);

            }
        });


        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.txtService.getText().toString().equals("")) {

                    Toast.makeText(getApplicationContext(), "Enter Dish Price", Toast.LENGTH_SHORT).show();

                } else {

                    if (Objects.equals(getIntent().getStringExtra("flag"), "1")) {

                        sendEditSetUpMenu();

                    } else {

                        sendSetUpMenu();
                    }
                }
            }
        });
    }

    private void getData() {

        pd = new ProgressDialog(InsuranceAddServicesActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseInsuranceServiceAndCompanyList> call = api.getInsuranceServiceList("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key);
        call.enqueue(new Callback<ResponseInsuranceServiceAndCompanyList>() {
            @Override
            public void onResponse(Call<ResponseInsuranceServiceAndCompanyList> call, Response<ResponseInsuranceServiceAndCompanyList> response) {

                Log.e("ResponseCompanyList", new Gson().toJson(response.body()));

                pd.dismiss();
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(InsuranceAddServicesActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        serviceLists = response.body().getServiceList();
                        companyLists = response.body().getCompanyList();

                        typeListAdapter = new AddCompnyAdapterInsurance(InsuranceAddServicesActivity.this, addDishType, InsuranceAddServicesActivity.this, InsuranceAddServicesActivity.this);

                        LinearLayoutManager layoutManager = new LinearLayoutManager(InsuranceAddServicesActivity.this,  LinearLayoutManager.VERTICAL, false);

                        binding.rclrCouponList.setLayoutManager(layoutManager);
                        binding.rclrCouponList.setAdapter(typeListAdapter);

                    }


                } else {

                    Toast.makeText(InsuranceAddServicesActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(Call<ResponseInsuranceServiceAndCompanyList> call, Throwable t) {

                pd.dismiss();
                t.printStackTrace();
                Log.e("errorCompanyList", t.getMessage());
            }
        });


    }

    private void sendSetUpMenu() {

        pd = new ProgressDialog(InsuranceAddServicesActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();


        String authHeader = "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject);
        String accesskey = WS_URL_PARAMS.access_key;
        String business_id = sessionManager.getUserId();
        String service_id = serviceId;
        String company_id = companyId;

        if (addDishType.size() > 0) {

            StringBuilder sb = new StringBuilder();
            for (CompanyList u : addDishType) {
                sb.append(u.getId()).append("~~");
            }
            company_id = sb.toString();
            company_id = company_id.substring(0, company_id.length() - 2);
            Log.e("string", company_id);

        }

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseInsuranceAddService> call = api.sendInsuranceService(authHeader, accesskey, business_id, service_id, company_id);

        call.enqueue(new Callback<ResponseInsuranceAddService>() {
            @Override
            public void onResponse(Call<ResponseInsuranceAddService> call, Response<ResponseInsuranceAddService> response) {

                Log.e("responseAddService", new Gson().toJson(response.body()));
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(InsuranceAddServicesActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        Intent intent = new Intent(InsuranceAddServicesActivity.this, InsuranceServicesActivity.class);
                        startActivity(intent);
                    }

                } else {
                    Toast.makeText(InsuranceAddServicesActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseInsuranceAddService> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                t.printStackTrace();
                Log.e("ServiceError", t.getMessage());
            }
        });
    }

    private void sendEditSetUpMenu() {

        pd = new ProgressDialog(InsuranceAddServicesActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();


        String authHeader = "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject);
        String accesskey = WS_URL_PARAMS.access_key;
        String business_id = sessionManager.getUserId();
        String service_id = serviceId;
        String company_id = companyId;
        String id = couponId;

        if (addDishType.size() > 0) {

            StringBuilder sb = new StringBuilder();
            for (CompanyList u : addDishType) {
                sb.append(u.getId()).append("~~");
            }
            company_id = sb.toString();
            company_id = company_id.substring(0, company_id.length() - 2);
            Log.e("string", company_id);

        }

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseEditDoctorService> call = api.editInsuranceService(authHeader, accesskey, business_id, company_id, service_id, id);

        call.enqueue(new Callback<ResponseEditDoctorService>() {
            @Override
            public void onResponse(Call<ResponseEditDoctorService> call, Response<ResponseEditDoctorService> response) {

                Log.e("responseEditService", new Gson().toJson(response.body()));
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(InsuranceAddServicesActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        Intent intent = new Intent(InsuranceAddServicesActivity.this, InsuranceServicesActivity.class);
                        startActivity(intent);
                    }

                } else {
                    Toast.makeText(InsuranceAddServicesActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseEditDoctorService> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                t.printStackTrace();
                Log.e("ServiceError", t.getMessage());
            }
        });
    }


    void onProfileImageClick() {
        Dexter.withActivity(InsuranceAddServicesActivity.this)
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
        Intent intent = new Intent(InsuranceAddServicesActivity.this, ImagePickerActivity.class);
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
        Intent intent = new Intent(InsuranceAddServicesActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(InsuranceAddServicesActivity.this);
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
        Intent i = new Intent(InsuranceAddServicesActivity.this, InsuranceServicesActivity.class);
        finish();
        startActivity(i);
    }

    @Override
    public void clickOnPartyListItem(String data, String id) {


        binding.txtService.setText(data);
        binding.txtService.setTextColor(getResources().getColor(R.color.black));

        serviceId = id;

//        getAccountList(id);

        if (customDialog != null) {
            customDialog.dismiss();
        }


    }

    @Override
    public void onCrossButtonClick(CompanyList offer) {

            addDishType.remove(offer);
            if(addDishType != null)
                typeListAdapter.updateDataList(addDishType);

        typeListAdapter.notifyDataSetChanged();


    }

    @Override
    public void onAddButtonClick(int postion) {

        SelectCompanyNameAdapter dataAdapter = new SelectCompanyNameAdapter(companyLists, InsuranceAddServicesActivity.this);
        customDialog1 = new CustomListViewDialog(InsuranceAddServicesActivity.this, dataAdapter);

        customDialog1.show();
        customDialog1.setCanceledOnTouchOutside(false);

    }

    @Override
    public void clickOnSelectOfferItem(String data, String id) {
        boolean flag = false;

        if (getIntent().getStringExtra("flag") == "1") {

            CompanyList dishType = new CompanyList(cId, cDesc);

            addDishType.add(dishType);
            typeListAdapter = new AddCompnyAdapterInsurance(InsuranceAddServicesActivity.this, addDishType, InsuranceAddServicesActivity.this, InsuranceAddServicesActivity.this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(InsuranceAddServicesActivity.this, LinearLayoutManager.VERTICAL, false);
            binding.rclrCouponList.setLayoutManager(layoutManager);
            binding.rclrCouponList.setAdapter(typeListAdapter);


        } else {
            CompanyList dishType = new CompanyList(id, data);


       /* if(addDishType.contains(dishType)){
            Toast.makeText(SetUpOfferActivity.this, "DishType already available", Toast.LENGTH_SHORT).show();
        }else{
            addDishType.add(dishType);
            typeListAdapter = new OfferDishTypeListAdapter(SetUpOfferActivity.this, addDishType, this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(SetUpOfferActivity.this);
            rclrDishType.setLayoutManager(layoutManager);
            rclrDishType.setAdapter(typeListAdapter);
        }*/

            if (addDishType.size() == 0) {
                addDishType.add(dishType);

                typeListAdapter = new AddCompnyAdapterInsurance(InsuranceAddServicesActivity.this, addDishType, InsuranceAddServicesActivity.this, InsuranceAddServicesActivity.this);
                LinearLayoutManager layoutManager = new LinearLayoutManager(InsuranceAddServicesActivity.this, LinearLayoutManager.VERTICAL, false);
                binding.rclrCouponList.setLayoutManager(layoutManager);
                binding.rclrCouponList.setAdapter(typeListAdapter);

            } else {
                for (CompanyList d : addDishType) {
                    if (d.getId().equals(id)) {
                        Toast.makeText(InsuranceAddServicesActivity.this, "Company already available", Toast.LENGTH_SHORT).show();
                        flag = true;
                    }
                }
                if (!flag) {
                    addDishType.add(dishType);
                    typeListAdapter = new AddCompnyAdapterInsurance(InsuranceAddServicesActivity.this, addDishType, this, InsuranceAddServicesActivity.this);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(InsuranceAddServicesActivity.this, LinearLayoutManager.VERTICAL, false);
                    binding.rclrCouponList.setLayoutManager(layoutManager);
                    binding.rclrCouponList.setAdapter(typeListAdapter);

                }

            }

            if (customDialog1 != null) {
                customDialog1.dismiss();
            }


        }
    }
}