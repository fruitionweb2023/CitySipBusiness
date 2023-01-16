package com.direct2web.citysip.Activities.Restaurent;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.direct2web.citysip.Activities.CommonActivities.CategoryActivity;
import com.direct2web.citysip.Model.RestaurentModels.BasicDetails.ResponseGetBasicDetails;
import com.direct2web.citysip.Model.RestaurentModels.Dashboard.ResponseDashboard;
import com.direct2web.citysip.Model.RestaurentModels.MenuList.Menu;
import com.direct2web.citysip.Model.RestaurentModels.MenuList.ResponseMenuList;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.BottomButtonClickListner;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityMainBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    SessionManager sessionManager;
    BottomButtonClickListner bottomButtonClickListner;

    boolean doubleBackToExitPressedOnce = false;

    String url = "", title, emp_id, name;
    ProgressDialog pd;

    String[] allPermitions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE};

    private static final int PERMISSIONS_REQUEST_CODE = 1240;

    List<Menu> menuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        sessionManager = new SessionManager(this);
        bottomButtonClickListner = new BottomButtonClickListner(this, sessionManager);

        binding.bottomnavigation.bbImgHome.setColorFilter(getResources().getColor(R.color.clr_f54748));
        binding.bottomnavigation.bbTxtHome.setTextColor(getResources().getColor(R.color.clay));

        binding.bottomnavigation.bbHome.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMyBusiness.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbOrder.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMenu.setOnClickListener(new BottomButtonClickListner(this, sessionManager));

//        pd = new ProgressDialog(MainActivity.this);
//        pd.setMessage("Please wait Loading...");
//        pd.show();

        checkAndRequestPermissions();

        getData(sessionManager.getUserId(), "monthly_turn_over");

        getDashboardData(sessionManager.getUserId());


//        getMenuList2(sessionManager.getUserId());

        url = "http://medicaiditpark.com/city_slip/api-firebase/business_side/chart.php?business_id="+sessionManager.getUserId();

        Log.e("url", url);

        binding.webView.setWebViewClient(new MyWebViewClient());
//        binding.webView.setInitialScale(1);
//        binding.webView.getSettings().setLoadWithOverviewMode(true);
//        binding.webView.getSettings().setUseWideViewPort(true);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setSupportZoom(true);
        binding.webView.loadUrl(url);

        binding.txtOrderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,OrderListActivity.class);
                startActivity(intent);
            }
        });

        binding.txtCustomerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,CustomerActivity.class);
                startActivity(intent);
            }
        });

        binding.txtInActiveMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SetUpMenuActivity.class);
                startActivity(intent);
            }
        });

        binding.txtActiveMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SetUpMenuActivity.class);
                startActivity(intent);
            }
        });

        binding.txtActiveOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SetUpCouponsActivity.class);
                startActivity(intent);
            }
        });

        binding.txtInActiveOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SetUpCouponsActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getData(String userId, String monthly_turn_over) {


        Api api = RetrofitClient.getClient().create(Api.class);

        Call<ResponseGetBasicDetails> call = api.getBasicDetails("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId, monthly_turn_over);

        call.enqueue(new Callback<ResponseGetBasicDetails>() {
            @Override
            public void onResponse(Call<ResponseGetBasicDetails> call, Response<ResponseGetBasicDetails> response) {

                Log.e("responceMainActivity", new Gson().toJson(response.body()));

                if (pd.isShowing()){
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    } else {

                       // binding.txtRevenueCount.setText(response.body().getDetailData());

                    }

                } else {

                    Toast.makeText(MainActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(Call<ResponseGetBasicDetails> call, Throwable t) {
                if (pd.isShowing()){
                    pd.dismiss();
                }
                t.printStackTrace();
                Log.e("error", t.getMessage());

            }
        });


    }

    private void getMenuList2(String userId) {

        Log.e("userId", userId);

        Api api = RetrofitClient.getClient().create(Api.class);

        Call<ResponseMenuList> call = api.getMenuList("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId);
        call.enqueue(new Callback<ResponseMenuList>() {
            @Override
            public void onResponse(Call<ResponseMenuList> call, Response<ResponseMenuList> response) {
                Log.e("responseMenuList", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }

                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        binding.txtActiveMenu.setText("0");
                        binding.txtInActiveMenu.setText("0");
                    } else {

                        menuList = response.body().getMenuList();

                        if (menuList != null) {

                            List<Menu> tempActive = new ArrayList<>();
                            List<Menu> tempInActive = new ArrayList<>();


                            for (Menu menuItemList : menuList) {

                                if (menuItemList.getStatus().equals("1")) {
                                    tempActive.add(menuItemList);
                                } else {

                                    tempInActive.add(menuItemList);
                                }
                            }
                            binding.txtActiveMenu.setText(String.valueOf(tempActive.size()));
                            binding.txtInActiveMenu.setText(String.valueOf(tempInActive.size()));


                        } else {
                            binding.txtActiveMenu.setText("0");
                            binding.txtInActiveMenu.setText("0");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMenuList> call, Throwable t) {

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                Log.e("erro", t.getMessage());
                t.printStackTrace();
            }
        });

    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
            startActivity(intent);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
            view.loadUrl(url);

            if (!pd.isShowing()) {
                pd.show();
            }

            return true;
        }

        @Override
        public void onPageFinished(android.webkit.WebView view, String url) {
            System.out.println("on finish");
            if (pd.isShowing()) {
                pd.dismiss();
            }

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            binding.nestedScrollView.scrollTo(0, 0);
        }
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


    private void getDashboardData(String userId) {

        pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(false);
        pd.show();

        Api api = RetrofitClient.getClient().create(Api.class);

        Call<ResponseDashboard> call = api.getDashboardData("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId);

        call.enqueue(new Callback<ResponseDashboard>() {
            @Override
            public void onResponse(Call<ResponseDashboard> call, Response<ResponseDashboard> response) {

                Log.e("responceMainActivity", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getmError()) {

                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    } else {

                        binding.txtActiveMenu.setText(response.body().getmActiveMenuItem());
                        binding.txtInActiveMenu.setText(response.body().getmInactiveMenuItem());
                        binding.txtActiveOffer.setText(response.body().getmActiveOffer());
                        binding.txtInActiveOffer.setText(response.body().getmInactiveOffer());
                         binding.txtRevenueCount.setText("₹" + response.body().getMonthlyTurenOver());


                    }

                } else {

                    Toast.makeText(MainActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(Call<ResponseDashboard> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                t.printStackTrace();
                Log.e("error", t.getMessage());

            }
        });


    }


}