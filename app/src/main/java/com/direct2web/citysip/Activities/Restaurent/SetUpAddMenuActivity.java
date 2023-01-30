package com.direct2web.citysip.Activities.Restaurent;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.direct2web.citysip.Activities.Insurance.InsuranceAddServicesActivity;
import com.direct2web.citysip.Adapter.InsuranceAdapters.AddCompnyAdapterInsurance;
import com.direct2web.citysip.Adapter.RestaurentAdapters.AddCouponAddMenuAdapter;
import com.direct2web.citysip.Adapter.RestaurentAdapters.SelectOfferNameAdapter;
import com.direct2web.citysip.Adapter.RestaurentAdapters.SpinnerCusinesListAdapter;
import com.direct2web.citysip.Adapter.RestaurentAdapters.SpinnerDishTypeListAdapter;
import com.direct2web.citysip.Adapter.RestaurentAdapters.SpinnerFoodTypeTypeListAdapter;
import com.direct2web.citysip.Adapter.RestaurentAdapters.SpinnerOfferTypeListAdapter;
import com.direct2web.citysip.Model.DoctorModels.DoctorServices.ResponseEditDoctorService;
import com.direct2web.citysip.Model.InsuranceModel.InsuranceServices.CompanyList;
import com.direct2web.citysip.Model.RestaurentModels.Cuisines.Cuisine;
import com.direct2web.citysip.Model.RestaurentModels.Cuisines.DishType;
import com.direct2web.citysip.Model.RestaurentModels.Cuisines.FoodType;
import com.direct2web.citysip.Model.RestaurentModels.Cuisines.OfferType;
import com.direct2web.citysip.Model.RestaurentModels.Cuisines.ResponseCuisinesList;
import com.direct2web.citysip.Model.RestaurentModels.MenuList.Menu;
import com.direct2web.citysip.Model.RestaurentModels.SetUpMenu.ResponseSetMenu;
import com.direct2web.citysip.Model.RestaurentModels.offer.Offer;
import com.direct2web.citysip.Model.RestaurentModels.offer.ResponseOfferList;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.CustomListViewDialog;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityAddMenuBinding;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetUpAddMenuActivity extends AppCompatActivity implements AddCouponAddMenuAdapter.OnItemClickListner, SelectOfferNameAdapter.RecyclerViewItemClickListener, AddCouponAddMenuAdapter.OnCrossItemClickListner {

    ActivityAddMenuBinding binding;
    SessionManager sessionManager;
    public static final int REQUEST_IMAGE = 100;
    String img2 = "";
    String menuId = "";
    int flag_image;
    Bitmap bitmap2;
    String category = "", strDishType, strDishTypeId, strCuisine, strCuisineId, strFoodType, srtFoodTypeId, strOfferAppliedOrNot, StrOfferAppliedOrNotId;

    List<Cuisine> cuisineList = new ArrayList<>();
    List<DishType> dishTypeList = new ArrayList<>();
    ProgressDialog pd;
    List<Menu> menuList;
    SpinnerCusinesListAdapter spinnerCusinesListAdapter;
    SpinnerDishTypeListAdapter spinnerDishTypeListAdapter;
    SpinnerOfferTypeListAdapter spinnerOfferTypeListAdapter;
    SpinnerFoodTypeTypeListAdapter spinnerFoodTypeListAdapter;
    List<Offer> offerList = new ArrayList<>();
    AddCouponAddMenuAdapter adapter;
    CustomListViewDialog customDialog;
    List<Offer> addDishType = new ArrayList<Offer>();
    List<Offer> selectedOfferList = new ArrayList<Offer>();
    AddCouponAddMenuAdapter typeListAdapter;
    Boolean flag = true;
    Boolean flag2 = true;
    Boolean flag3 = true;
    Boolean flag4 = true;
    ArrayList<OfferType> arrayList = new ArrayList<>();
    ArrayList<FoodType> arrayList2 = new ArrayList<>();
    String[] array;
    String[] array2;
    String cId;
    String cDesc;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_menu);
        sessionManager = new SessionManager(this);

        binding.toolbar.toolbarBack.setVisibility(View.VISIBLE);

        arrayList.add(new OfferType("0","Select"));
        arrayList.add(new OfferType("1","YES"));
        arrayList.add(new OfferType("2","NO"));

        arrayList2.add(new FoodType("0","Select"));
        arrayList2.add(new FoodType("1","Veg"));
        arrayList2.add(new FoodType("2","NonVeg"));

        getOfferList(sessionManager.getUserId());
        getdata();

        binding.toolbar.toolbarBack.setOnClickListener(v -> onBackPressed());

        menuId = getIntent().getStringExtra("menuId");
        if (getIntent().getStringExtra("flag").equals("1")) {

            binding.loginToYo.setText("Edit Dish");
            binding.edtRestName.setText( getIntent().getStringExtra("dishName"));
            binding.edtPrice.setText( getIntent().getStringExtra("amount"));
            binding.edtDescription.setText(getIntent().getStringExtra("description"));
            binding.txtOrderLimit.setText( getIntent().getStringExtra("maxDish"));
            binding.btnSubmit.setText("Edit");
            menuId = getIntent().getStringExtra("menuId");
            Glide.with(this).load(Api.imageUrl + getIntent().getStringExtra("image"))
                    .thumbnail(0.5f)
                    .error(R.drawable.city_sip_logo)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(binding.imgDishLogo);


            Log.e("TypeListAdapterSize : ", String.valueOf(addDishType.size()));
            typeListAdapter = new AddCouponAddMenuAdapter(SetUpAddMenuActivity.this, addDishType, SetUpAddMenuActivity.this,SetUpAddMenuActivity.this);

            GridLayoutManager layoutManager = new GridLayoutManager(SetUpAddMenuActivity.this, 1, GridLayoutManager.HORIZONTAL, false);

            binding.rclrCouponList.setLayoutManager(layoutManager);
            binding.rclrCouponList.setAdapter(typeListAdapter);

        } else  {

            binding.loginToYo.setText("Add Dish");
            binding.edtRestName.setText("");
            binding.edtPrice.setText("");
            binding.edtDescription.setText("");
            binding.txtOrderLimit.setText("");
            binding.btnSubmit.setText("Add");

        }

        pd = new ProgressDialog(SetUpAddMenuActivity.this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(true);

        typeListAdapter = new AddCouponAddMenuAdapter(SetUpAddMenuActivity.this, addDishType, SetUpAddMenuActivity.this,SetUpAddMenuActivity.this);

        GridLayoutManager layoutManager = new GridLayoutManager(SetUpAddMenuActivity.this, 1, GridLayoutManager.HORIZONTAL, false);

        binding.rclrCouponList.setLayoutManager(layoutManager);
        binding.rclrCouponList.setAdapter(typeListAdapter);

        binding.imgDishLogo.setOnClickListener(v -> {
            flag_image = 2;
            onProfileImageClick();
        });

        spinnerCusinesListAdapter = new SpinnerCusinesListAdapter(SetUpAddMenuActivity.this,R.layout.raw_recyclear_view_drop_down,R.id.tvName,cuisineList);
        binding.txtCuisines.setAdapter(spinnerCusinesListAdapter);
        binding.txtCuisines.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                strCuisine = adapterView.getItemAtPosition(i).toString();
                strCuisineId = cuisineList.get(i).getId();
                String strCuisineName = cuisineList.get(i).getName();
                binding.txtCuisines.setSelection(i);

                if (flag) {
                    String c = getIntent().getStringExtra("cuisine");
                    if (c != null) {
                        binding.txtCuisines.setSelection(spinnerCusinesListAdapter.getItemIndexByValue(c));
                        Log.e("Spinner Value : ", String.valueOf(spinnerCusinesListAdapter.getItemIndexByValue(c)));
                    }
                    flag = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerDishTypeListAdapter = new SpinnerDishTypeListAdapter(SetUpAddMenuActivity.this,R.layout.raw_recyclear_view_drop_down,R.id.tvName,dishTypeList);
        binding.txtDishType.setAdapter(spinnerDishTypeListAdapter);
        binding.txtDishType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strDishType = adapterView.getItemAtPosition(i).toString();
                strDishTypeId = dishTypeList.get(i).getId();
                binding.txtDishType.setSelection(i);

                if (flag2) {
                    String c = getIntent().getStringExtra("dishType");
                    if (c != null) {
                        binding.txtDishType.setSelection(spinnerDishTypeListAdapter.getItemIndexByValue(c));
                    }
                    flag2 = false;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        spinnerOfferTypeListAdapter = new SpinnerOfferTypeListAdapter(SetUpAddMenuActivity.this,R.layout.raw_recyclear_view_drop_down,R.id.tvName,arrayList);
        binding.txtOffer.setAdapter(spinnerOfferTypeListAdapter);
        binding.txtOffer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String id = adapterView.getItemAtPosition(i).toString();
                StrOfferAppliedOrNotId = arrayList.get(i).getId();
                strOfferAppliedOrNot = arrayList.get(i).getName();
                binding.txtOffer.setSelection(i);

                if (strOfferAppliedOrNot.equals("NO")) {
                    binding.llAddOffer.setVisibility(View.GONE);
                } else if(strOfferAppliedOrNot.equals("Select")){
                    binding.llAddOffer.setVisibility(View.GONE);
                } else  {
                    binding.llAddOffer.setVisibility(View.VISIBLE);
                }

                if (flag4) {
                    String c = getIntent().getStringExtra("offerType");
                    if (c != null) {
                        binding.txtOffer.setSelection(spinnerOfferTypeListAdapter.getItemIndexByValue(c));
                    }
                    flag4 = false;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinnerFoodTypeListAdapter = new SpinnerFoodTypeTypeListAdapter(SetUpAddMenuActivity.this,R.layout.raw_recyclear_view_drop_down,R.id.tvName,arrayList2);
        binding.txtFoodType.setAdapter(spinnerFoodTypeListAdapter);
        binding.txtFoodType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String position = adapterView.getItemAtPosition(i).toString();
                String id = arrayList2.get(i).getId();
                strFoodType = arrayList2.get(i).getName();
                binding.txtFoodType.setSelection(i);

                if (flag3) {
                    String c = getIntent().getStringExtra("category");
                    if (c != null) {
                        binding.txtFoodType.setSelection(spinnerFoodTypeListAdapter.getItemIndexByValue(c));
                    }
                    flag3 = false;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

       binding.btnSubmit.setOnClickListener(view -> {
           if (binding.edtRestName.getText().toString().equals("")) {

               Toast.makeText(getApplicationContext(), "Enter Dish Name", Toast.LENGTH_SHORT).show();

           } else if (binding.edtPrice.getText().toString().equals("")) {

               Toast.makeText(getApplicationContext(), "Enter Dish Price", Toast.LENGTH_SHORT).show();

           } else if (binding.edtDescription.getText().toString().equals("")) {

               Toast.makeText(getApplicationContext(), "Enter Dish Description", Toast.LENGTH_SHORT).show();

           } else if (binding.txtCuisines.equals("Cuisines")) {

               Toast.makeText(getApplicationContext(), "Please Select Cuisines First", Toast.LENGTH_SHORT).show();

           } else if (binding.txtDishType.equals("Dish Type")) {

               Toast.makeText(getApplicationContext(), "Please Select Dish Type First", Toast.LENGTH_SHORT).show();

           } else if (binding.txtFoodType.getSelectedItem().toString().equals("Select")) {

               Toast.makeText(getApplicationContext(), "Please select categoty Veg or NonVeg", Toast.LENGTH_SHORT).show();

           }else if (binding.txtOffer.getSelectedItem().toString().equals("Select")) {

               Toast.makeText(getApplicationContext(), "Please select offer appied or not", Toast.LENGTH_SHORT).show();

           } else if (binding.txtOrderLimit.getText().toString().equals("")) {

               Toast.makeText(getApplicationContext(), "Please Select Order Limit First", Toast.LENGTH_SHORT).show();

           } else {

               if (Objects.equals(getIntent().getStringExtra("flag"), "1")) {
                   editSetUpMenu();
               } else  {
                   sendSetUpMenu();
               }
           }
       });
    }

    private void getOfferList(String id) {

        Api api = RetrofitClient.getClient().create(Api.class);

        Call<ResponseOfferList> call = api.getOffers("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, id);
        call.enqueue(new Callback<ResponseOfferList>() {
            @Override
            public void onResponse(Call<ResponseOfferList> call, Response<ResponseOfferList> response) {
                Log.e("responseOfferList", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }

                if (response.body() != null && response.isSuccessful()) {
                    offerList = response.body().getOfferList();
                    Log.e("userId", sessionManager.getUserId());

                    if (response.body().getError()) {

                        Toast.makeText(SetUpAddMenuActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        typeListAdapter = new AddCouponAddMenuAdapter(SetUpAddMenuActivity.this, addDishType, SetUpAddMenuActivity.this,SetUpAddMenuActivity.this);

                        GridLayoutManager layoutManager = new GridLayoutManager(SetUpAddMenuActivity.this, 1, GridLayoutManager.HORIZONTAL, false);

                        binding.rclrCouponList.setLayoutManager(layoutManager);
                        binding.rclrCouponList.setAdapter(typeListAdapter);

                        if (getIntent().getStringExtra("flag").equals("1")){
                            defaultSelectValue();
                        }
                    }

                } else {
                    Toast.makeText(SetUpAddMenuActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseOfferList> call, Throwable t) {

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                Log.e("error", t.getMessage());
                t.printStackTrace();
            }
        });
    }


    private void sendSetUpMenu() {

        pd = new ProgressDialog(SetUpAddMenuActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();


        String authHeader = "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject);
        String accesskey = WS_URL_PARAMS.access_key;
        String business_id = sessionManager.getUserId();
        String title = binding.edtRestName.getText().toString();
        String amount = binding.edtPrice.getText().toString();
        String cuisines = strCuisineId;
        String dish_type = strDishTypeId;
        String max_dish = binding.txtOrderLimit.getText().toString();
        String description = binding.edtDescription.getText().toString();
        String category = strFoodType;
        String offer = strOfferAppliedOrNot;

        if (addDishType.size() > 0){

            StringBuilder sb = new StringBuilder();
            for (Offer u : addDishType) {
                sb.append(u.getId()).append("~~");
            }
            offer = sb.toString();
            offer = offer.substring(0, offer.length() - 2);
            Log.e("string", offer);

        }

        RequestBody t1 = RequestBody.create(MediaType.parse("multipart/form-data"), authHeader);
        RequestBody t2 = RequestBody.create(MediaType.parse("multipart/form-data"), accesskey);
        RequestBody t3 = RequestBody.create(MediaType.parse("multipart/form-data"), business_id);
        RequestBody t4 = RequestBody.create(MediaType.parse("multipart/form-data"), title);
        RequestBody t5 = RequestBody.create(MediaType.parse("multipart/form-data"), cuisines);
        RequestBody t6 = RequestBody.create(MediaType.parse("multipart/form-data"), dish_type);
        RequestBody t7 = RequestBody.create(MediaType.parse("multipart/form-data"), amount);
        RequestBody t8 = RequestBody.create(MediaType.parse("multipart/form-data"), max_dish);
        RequestBody t9 = RequestBody.create(MediaType.parse("multipart/form-data"), description);
        RequestBody t10 = RequestBody.create(MediaType.parse("multipart/form-data"), category);
        RequestBody t11 = RequestBody.create(MediaType.parse("multipart/form-data"), offer);

        Log.e("MenuItemsAdd : ", "business_id : " + business_id + "\ndishName : " + title + "\namount : " + amount +
                "\ncuisine : " +   cuisines + "\ndishType : " + dish_type + "\ndescription : " +
                description + "\noffer : " + offer + "\nimage : " + img2 + "\nmaxDish : " +
                max_dish + "\ncategory : " + category + "\nmenuId : " + menuId);


        File file = null;
        MultipartBody.Part body1 = null;
        if (!img2.equals("")) {
            file = new File(img2);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

            String s = title + "_" + file.getName();
            String logo = s.replaceAll(" ", "_");
            body1 = MultipartBody.Part.createFormData("image", logo, requestFile);
        }


        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseSetMenu> call = api.sendSetUpMenu(authHeader, t2, t3, t4,t5,t6,t7,t8, t9, t10,t11, body1);

        call.enqueue(new Callback<ResponseSetMenu>() {
            @Override
            public void onResponse(Call<ResponseSetMenu> call, Response<ResponseSetMenu> response) {

                Log.e("responceSetupMenu", new Gson().toJson(response.body()));
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(SetUpAddMenuActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        Intent intent = new Intent(SetUpAddMenuActivity.this, SetUpMenuActivity.class);
                        startActivity(intent);
                    }

                } else {
                    Toast.makeText(SetUpAddMenuActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseSetMenu> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                t.printStackTrace();
                Log.e("error", t.getMessage());
            }
        });
    }

    private void editSetUpMenu() {

        pd = new ProgressDialog(SetUpAddMenuActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        String authHeader = "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject);
        String accesskey = WS_URL_PARAMS.access_key;
        String business_id = sessionManager.getUserId();
        String title = binding.edtRestName.getText().toString();
        String cuisines = strCuisineId;
        String dish_type = strDishTypeId;
        String amount = binding.edtPrice.getText().toString();
        String max_dish = binding.txtOrderLimit.getText().toString();
        String description = binding.edtDescription.getText().toString();
        String category = strFoodType;
        String offer = strOfferAppliedOrNot;

        if (addDishType.size() > 0){

            StringBuilder sb = new StringBuilder();
            for (Offer u : addDishType) {
                sb.append(u.getId()).append("~~");
            }
            offer = sb.toString();
            offer = offer.substring(0, offer.length() - 2);
            Log.e("string", offer);

        }

        RequestBody t1 = RequestBody.create(MediaType.parse("multipart/form-data"), authHeader);
        RequestBody t2 = RequestBody.create(MediaType.parse("multipart/form-data"), accesskey);
        RequestBody t3 = RequestBody.create(MediaType.parse("multipart/form-data"), business_id);
        RequestBody t4 = RequestBody.create(MediaType.parse("multipart/form-data"), title);
        RequestBody t5 = RequestBody.create(MediaType.parse("multipart/form-data"), cuisines);
        RequestBody t6 = RequestBody.create(MediaType.parse("multipart/form-data"), dish_type);
        RequestBody t7 = RequestBody.create(MediaType.parse("multipart/form-data"), amount);
        RequestBody t8 = RequestBody.create(MediaType.parse("multipart/form-data"), max_dish);
        RequestBody t9 = RequestBody.create(MediaType.parse("multipart/form-data"), description);
        RequestBody t10 = RequestBody.create(MediaType.parse("multipart/form-data"), category);
        RequestBody t11 = RequestBody.create(MediaType.parse("multipart/form-data"), offer);
        RequestBody t12 = RequestBody.create(MediaType.parse("multipart/form-data"), menuId);


        File file = null;
        MultipartBody.Part body1 = null;
        if (!img2.equals("")) {
            file = new File(img2);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

            String s = title + "_" + file.getName();
            String logo = s.replaceAll(" ", "_");
            body1 = MultipartBody.Part.createFormData("image", logo, requestFile);
        }

        Log.e("Menu Items Edit Send : ", "business_id : " + business_id + "\ndishName : " + title + "\namount : " + amount +
                "\ncuisine : " +   cuisines + "\ndishType : " + dish_type + "\ndescription : " +
                description + "\noffer : " + offer + "\nimage : " + img2 + "\nmaxDish : " +
                max_dish + "\ncategory : " + category + "\nmenuId : " + menuId);

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseEditDoctorService> call = api.editSetUpMenu(authHeader, t2, t3, t4,t5,t6,t7,t8, t9, t10,t11,t12, body1);

        call.enqueue(new Callback<ResponseEditDoctorService>() {
            @Override
            public void onResponse(Call<ResponseEditDoctorService> call, Response<ResponseEditDoctorService> response) {

                Log.e("ResponceEditSetupMenu", new Gson().toJson(response.body()));
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(SetUpAddMenuActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        Intent intent = new Intent(SetUpAddMenuActivity.this, SetUpMenuActivity.class);
                        startActivity(intent);
                    }

                } else {

                    Toast.makeText(SetUpAddMenuActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseEditDoctorService> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                t.printStackTrace();
                Log.e("error", t.getMessage());
            }
        });
    }

    void onProfileImageClick() {
        Dexter.withActivity(SetUpAddMenuActivity.this)
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
        Intent intent = new Intent(SetUpAddMenuActivity.this, ImagePickerActivity.class);
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
        Intent intent = new Intent(SetUpAddMenuActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SetUpAddMenuActivity.this);
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

    private void getdata() {

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseCuisinesList> call = api.getCuisine("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject), WS_URL_PARAMS.access_key);
        call.enqueue(new Callback<ResponseCuisinesList>() {
            @Override
            public void onResponse(Call<ResponseCuisinesList> call, Response<ResponseCuisinesList> response) {

                Log.e("responseCuisin", new Gson().toJson(response.body()));

                if (response.body() != null && response.isSuccessful()) {

                    cuisineList = response.body().getCuisines();
                    dishTypeList = response.body().getDishType();

                    Cuisine cuisine = new Cuisine("0","Select");
                    cuisineList.add(0,cuisine);

                    DishType dishType = new DishType("0","Select");
                    dishTypeList.add(0,dishType);

                    spinnerCusinesListAdapter = new SpinnerCusinesListAdapter(SetUpAddMenuActivity.this,R.layout.raw_recyclear_view_drop_down,R.id.tvName,cuisineList);
                    binding.txtCuisines.setAdapter(spinnerCusinesListAdapter);

                    spinnerDishTypeListAdapter = new SpinnerDishTypeListAdapter(SetUpAddMenuActivity.this,R.layout.raw_recyclear_view_drop_down,R.id.tvName,dishTypeList);
                    binding.txtDishType.setAdapter(spinnerDishTypeListAdapter);

                }
            }

            @Override
            public void onFailure(Call<ResponseCuisinesList> call, Throwable t) {

                t.printStackTrace();
                Log.e("error", t.getMessage());

            }
        });
    }

    @Override
    public void onAddButtonClick(int postion) {

        SelectOfferNameAdapter dataAdapter = new SelectOfferNameAdapter(offerList, SetUpAddMenuActivity.this);
        customDialog = new CustomListViewDialog(SetUpAddMenuActivity.this, dataAdapter);
        customDialog.show();
        customDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void clickOnSelectOfferItem(String data, String id) {

        boolean flag = false;

        if (getIntent().getStringExtra("flag") == "1") {

            Offer dishType = new Offer(cId, cDesc);
            addDishType.add(dishType);
            typeListAdapter = new AddCouponAddMenuAdapter(SetUpAddMenuActivity.this, addDishType, SetUpAddMenuActivity.this, SetUpAddMenuActivity.this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(SetUpAddMenuActivity.this, LinearLayoutManager.VERTICAL, false);
            binding.rclrCouponList.setLayoutManager(layoutManager);
            binding.rclrCouponList.setAdapter(typeListAdapter);

        } else {
            Offer dishType = new Offer(id, data);

            if (addDishType.size() == 0) {
                addDishType.add(dishType);

                typeListAdapter = new AddCouponAddMenuAdapter(SetUpAddMenuActivity.this, addDishType, SetUpAddMenuActivity.this, SetUpAddMenuActivity.this);
                GridLayoutManager layoutManager = new GridLayoutManager(SetUpAddMenuActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
                binding.rclrCouponList.setLayoutManager(layoutManager);
                binding.rclrCouponList.setAdapter(typeListAdapter);
            } else {

                for (Offer d : addDishType) {
                    if (d.getId().equals(id)) {
                        Toast.makeText(SetUpAddMenuActivity.this, "Offer already available", Toast.LENGTH_SHORT).show();
                        flag = true;
                    }
                }
                if (!flag) {

                    addDishType.add(dishType);
                    typeListAdapter = new AddCouponAddMenuAdapter(SetUpAddMenuActivity.this, addDishType, this, SetUpAddMenuActivity.this);
                    GridLayoutManager layoutManager = new GridLayoutManager(SetUpAddMenuActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
                    binding.rclrCouponList.setLayoutManager(layoutManager);
                    binding.rclrCouponList.setAdapter(typeListAdapter);
                }
            }

            if (customDialog != null) {
                customDialog.dismiss();
            }
        }
    }

    @Override
    public void onCrossButtonClick(Offer offer) {

        addDishType.remove(offer);
        if(addDishType != null)
            typeListAdapter.updateDataList(addDishType);
        typeListAdapter.notifyDataSetChanged();

    }

    @SuppressLint("LongLogTag")
    private void  defaultSelectValue() {
        String c = getIntent().getStringExtra("offer");
        String[] offerIdArray = c.split("~~");

        if (c.equals("No") && c.equals("Select")) {
            Log.e("getOfferDetails : ", "NO or Select");
        } else {
            Log.e("getOfferDetails : ", Arrays.toString(offerIdArray));
            addDishType.clear();

            for (int i = 0; i<offerIdArray.length; i++) {
                for (Offer item : offerList) {
                    if (offerIdArray[i].equals( item.getId())){
                        cId = item.getId();
                        cDesc = item.getCoupnCode();
                        addDishType.add(new Offer(cId,cDesc));
                        break;
                    }
                }
            }
        }
        typeListAdapter.updateDataList(addDishType);
    }
}