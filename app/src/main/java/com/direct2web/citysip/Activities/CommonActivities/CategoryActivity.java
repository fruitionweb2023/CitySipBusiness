package com.direct2web.citysip.Activities.CommonActivities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.direct2web.citysip.Activities.Doctor.DoctorAboutYouActivity;
import com.direct2web.citysip.Activities.Doctor.DoctorDeshboardActivity;
import com.direct2web.citysip.Activities.Insurance.InsuranceAboutYouActivity;
import com.direct2web.citysip.Activities.Insurance.InsuranceDashboardActivity;
import com.direct2web.citysip.Activities.Lawyer.LawyerAboutYouActivity;
import com.direct2web.citysip.Activities.Lawyer.LawyerDashboardActivity;
import com.direct2web.citysip.Activities.Restaurent.AddRestaurantDetailsActivity;
import com.direct2web.citysip.Activities.Restaurent.MainActivity;
import com.direct2web.citysip.Activities.SpaAndSalon.SapAndSalonDeshboardActivity;
import com.direct2web.citysip.Activities.SpaAndSalon.SpaAndSalonAboutYouActivity;
import com.direct2web.citysip.Adapter.RestaurentAdapters.CategoryAdapter;
import com.direct2web.citysip.Model.RestaurentModels.Category.Category;
import com.direct2web.citysip.Model.RestaurentModels.Category.ResponseCategoryList;
import com.direct2web.citysip.Model.RestaurentModels.CreateAccount.ResponseCreateAccount;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityCategoryBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity implements CategoryAdapter.onClickCategory {

    private static final int PERMISSIONS_REQUEST_CODE = 1240;
    ActivityCategoryBinding binding;
    String city_id = "1";
    List<Category> categoryList = new ArrayList<>();
    CategoryAdapter adapter;

    String[] allPermitions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE};

    boolean doubleBackToExitPressedOnce = false;

    int flag = 0;
    SessionManager sessionManager;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category);

        checkAndRequestPermissions();

        sessionManager = new SessionManager(this);

        flag = getIntent().getIntExtra("flag", 0);

        getCategory(city_id, sessionManager.getUserContact());


    }

    private void getCategory(String city_id, String mobile) {

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseCategoryList> call = api.getCategory("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, city_id, mobile);
        call.enqueue(new Callback<ResponseCategoryList>() {
            @Override
            public void onResponse(Call<ResponseCategoryList> call, Response<ResponseCategoryList> response) {

                Log.e("response_category", new Gson().toJson(response.body()));

                if (response.body() != null && response.isSuccessful()) {

                    categoryList = response.body().getCategoryList();

                    if (categoryList != null) {

                        adapter = new CategoryAdapter(CategoryActivity.this, categoryList, CategoryActivity.this);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(CategoryActivity.this, 3);
                        binding.rclrCategory.setLayoutManager(gridLayoutManager);
                        binding.rclrCategory.setAdapter(adapter);


                    } else {

                        binding.rclrCategory.setVisibility(View.GONE);

                    }


                } else {

                    binding.rclrCategory.setVisibility(View.GONE);

                }


            }

            @Override
            public void onFailure(Call<ResponseCategoryList> call, Throwable t) {

            }
        });


    }

    private void checkAndRequestPermissions() {

        List<String> listpermissionneeded = new ArrayList<>();

        for (String prm : allPermitions) {

            if (ContextCompat.checkSelfPermission(this, prm) != PackageManager.PERMISSION_GRANTED) {
                listpermissionneeded.add(prm);
            }
        }

        if (!listpermissionneeded.isEmpty()) {

            ActivityCompat.requestPermissions(this, listpermissionneeded.toArray(new String[listpermissionneeded.size()]), PERMISSIONS_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            HashMap<String, Integer> permissionresult = new HashMap<>();
            int deniedcount = 0;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionresult.put(permissions[i], grantResults[i]);
                    deniedcount++;
                }
            }

            if (deniedcount != 0) {
                for (Map.Entry<String, Integer> entry : permissionresult.entrySet()) {
                    String permname = entry.getKey();
                    int permvalue = entry.getValue();

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permname)) {
                        showdialog("", "This App needs Location and Storage permission to work without any problems.",
                                "Yes , Grant permission",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();
                                        checkAndRequestPermissions();
                                    }
                                },
                                "No, Exit App", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                }, false);
                        break;
                    } else {
                        showdialog("", "You have denied some permission, Allow all permission at [Setting] > [App] > [Permission]",
                                "Go to Setting",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.fromParts("package", getPackageName(), null));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                },
                                "No, Exit App", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                }, false);
                        break;
                    }
                }
            }

        }

    }

    public void showdialog(String title, String msg, String positivelabel, DialogInterface.OnClickListener positiveonclick,
                           String negitivelabel, DialogInterface.OnClickListener negitiveonclick,
                           boolean iscancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setCancelable(iscancelable);
        builder.setMessage(msg);
        builder.setPositiveButton(positivelabel, positiveonclick);
        builder.setNegativeButton(negitivelabel, negitiveonclick);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            moveTaskToBack(true);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void sendUserData(String strUsername, String cat_id, String launchBusiness) {


        pd = new ProgressDialog(CategoryActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();


        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseCreateAccount> call = api.sendCreateAccount("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, strUsername, cat_id);
        Log.e("Create Account Data :", "mobile : " + strUsername + "  " + "cat_id : " + cat_id );
        call.enqueue(new Callback<ResponseCreateAccount>() {
            @Override
            public void onResponse(@NonNull Call<ResponseCreateAccount> call, @NonNull Response<ResponseCreateAccount> response) {

                Log.e("responseCreateAccount", new Gson().toJson(response.body()));
                if (pd.isShowing()) {
                    pd.dismiss();
                }

                if (response.body() != null && response.isSuccessful()) {

                    if (!response.body().getError()) {

                        sessionManager.setUserId(response.body().getUserId());
                        sessionManager.setBusinesstype(response.body().getCatId());
//                        Toast.makeText(CategoryActivity.this, "UserId" + sessionManager.getUserId(), Toast.LENGTH_SHORT).show();

                        switch (response.body().getCatId()) {

                            case "1":
                                if (launchBusiness.equals("0")) {
                                    Intent lawyer_intent = new Intent(CategoryActivity.this, LawyerAboutYouActivity.class);
                                    startActivity(lawyer_intent);
                                    sessionManager.setBusinesstype("1");
                                } else {
                                    Intent lawyer_intent = new Intent(CategoryActivity.this, LawyerDashboardActivity.class);
                                    startActivity(lawyer_intent);
                                    sessionManager.setBusinesstype("1");
                                }
                                break;

                            case "4":
                                if (launchBusiness.equals("0")) {
                                    Intent doc_intent = new Intent(CategoryActivity.this, DoctorAboutYouActivity.class);
                                    startActivity(doc_intent);
                                    sessionManager.setBusinesstype("4");
                                } else {
                                    Intent doc_intent = new Intent(CategoryActivity.this, DoctorDeshboardActivity.class);
                                    startActivity(doc_intent);
                                    sessionManager.setBusinesstype("4");
                                }
                                break;

                            case "6":
                                if (launchBusiness.equals("0")) {
                                    Intent rest_intent = new Intent(CategoryActivity.this, AddRestaurantDetailsActivity.class);
                                    startActivity(rest_intent);
                                    sessionManager.setBusinesstype("6");
                                } else {
                                    Intent rest_intent = new Intent(CategoryActivity.this, MainActivity.class);
                                    startActivity(rest_intent);
                                    sessionManager.setBusinesstype("6");
                                }
                                break;

                            case "3":
                                if (launchBusiness.equals("0")) {
                                    Intent rest_intent = new Intent(CategoryActivity.this, InsuranceAboutYouActivity.class);
                                    startActivity(rest_intent);
                                    sessionManager.setBusinesstype("3");
                                } else {
                                    Intent rest_intent = new Intent(CategoryActivity.this, InsuranceDashboardActivity.class);
                                    startActivity(rest_intent);
                                    sessionManager.setBusinesstype("3");
                                }
                                break;

                            case "2":
                                if (launchBusiness.equals("0")) {
                                    Intent rest_intent = new Intent(CategoryActivity.this, SpaAndSalonAboutYouActivity.class);
                                    startActivity(rest_intent);
                                    sessionManager.setBusinesstype("2");
                                } else {
                                    Intent rest_intent = new Intent(CategoryActivity.this, SapAndSalonDeshboardActivity.class);
                                    startActivity(rest_intent);
                                    sessionManager.setBusinesstype("2");
                                }
                                break;

                            default:
                                Toast.makeText(CategoryActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                                break;

                        }


//                        sendProfileData(response.body().getUserId());


                    } else {


                        Toast.makeText(CategoryActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                } else {

                    Toast.makeText(CategoryActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(@NonNull Call<ResponseCreateAccount> call, @NonNull Throwable t) {
//                pd.dismiss();
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                t.printStackTrace();
                Log.e("errorCreateAccount", t.getMessage());


            }
        });


    }


    @Override
    public void onCategoryItemClick(int position) {

        Category category = categoryList.get(position);


        if (sessionManager.checkLogin()) {

//            if (category.getLaunchBusiness().equals("0")) {
            sendUserData(sessionManager.getUserContact(), category.getId(), category.getLaunchBusiness());
//            } else {

                /*switch (category.getId()) {
                    case "1":
                        Intent lawyer_intent = new Intent(CategoryActivity.this, LawyerDashboardActivity.class);
                        startActivity(lawyer_intent);
                        sessionManager.setBusinesstype("1");
                        break;
                    case "4":
                        Intent doc_intent = new Intent(CategoryActivity.this, DoctorDeshboardActivity.class);
                        startActivity(doc_intent);
                        sessionManager.setBusinesstype("4");
                        break;
                    case "6":
                        Intent rest_intent = new Intent(CategoryActivity.this, MainActivity.class);
                        startActivity(rest_intent);
                        sessionManager.setBusinesstype("6");
                        break;
                    default:
                        Toast.makeText(CategoryActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                        break;
                }*/
//            }
        } else {
            Intent rest_intent = new Intent(CategoryActivity.this, IntroLoginActivity.class);
            startActivity(rest_intent);
//            sessionManager.setBusinesstype("1");
        }


    }
}