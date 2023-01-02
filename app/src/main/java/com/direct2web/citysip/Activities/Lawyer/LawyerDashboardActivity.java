package com.direct2web.citysip.Activities.Lawyer;

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
import com.direct2web.citysip.Activities.Doctor.DoctorDeshboardActivity;
import com.direct2web.citysip.Activities.Restaurent.CustomerActivity;
import com.direct2web.citysip.Model.LawyerModels.LawyerDeshboard.ResponseLawyerDeshboard;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.BottomButtonClickListner;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityLawyerDeshboardBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LawyerDashboardActivity extends AppCompatActivity {

    ActivityLawyerDeshboardBinding binding;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lawyer_deshboard);
        sessionManager = new SessionManager(this);

        bottomButtonClickListner = new BottomButtonClickListner(this, sessionManager);

        binding.bottomnavigation.bbImgHome.setColorFilter(getResources().getColor(R.color.clr_0059C8));
        binding.bottomnavigation.bbTxtHome.setTextColor(getResources().getColor(R.color.clay));

        binding.bottomnavigation.bbOrder.setVisibility(View.GONE);
        binding.bottomnavigation.bbAppointment.setVisibility(View.VISIBLE);

        binding.bottomnavigation.bbHome.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMyBusiness.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbAppointment.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMenu.setOnClickListener(new BottomButtonClickListner(this, sessionManager));

        checkAndRequestPermissions();

        getDashboardData(sessionManager.getUserId());

        url = "http://direct2web.in/city_slip/api-firebase/business_side/chart.php?business_id="+sessionManager.getUserId();

        Log.e("url", url);

        binding.webView.setWebViewClient(new MyWebViewClient());
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setSupportZoom(true);
        binding.webView.loadUrl(url);

        binding.txtOrderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LawyerDashboardActivity.this, LawyerAppointmentActivity.class);
                startActivity(intent);
            }
        });

        binding.txtCustomerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LawyerDashboardActivity.this, LawyerCustomerActivity.class);
                startActivity(intent);
            }
        });

        binding.txtInActiveMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LawyerDashboardActivity.this, LawyerServicesActivity.class);
                startActivity(intent);
            }
        });

        binding.txtActiveMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LawyerDashboardActivity.this,LawyerServicesActivity.class);
                startActivity(intent);
            }
        });

        /*binding.txtActiveOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LawyerDashboardActivity.this, DoctorCouponsActivity.class);
                startActivity(intent);
            }
        });

        binding.txtInActiveOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LawyerDashboardActivity.this,DoctorCouponsActivity.class);
                startActivity(intent);
            }
        });*/
    }

    private void getDashboardData(String userId) {

        pd = new ProgressDialog(LawyerDashboardActivity.this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(false);
        pd.show();

        Api api = RetrofitClient.getClient().create(Api.class);

        Call<ResponseLawyerDeshboard> call = api.getLawyerDashboardDetails("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId);

        call.enqueue(new Callback<ResponseLawyerDeshboard>() {
            @Override
            public void onResponse(Call<ResponseLawyerDeshboard> call, Response<ResponseLawyerDeshboard> response) {

                Log.e("responceDeshboard", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(LawyerDashboardActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    } else {

                        binding.txtActiveMenu.setText(""+response.body().getActiveMenuItem());
                        binding.txtInActiveMenu.setText(""+response.body().getInactiveMenuItem());
//                        binding.txtActiveOffer.setText(""+response.body().getActiveOffer());
//                        binding.txtInActiveOffer.setText(""+response.body().getInactiveOffer());
                        binding.txtRevenueCount.setText("₹" + response.body().getMonthlyTurnOver());

                    }

                } else {

                    Toast.makeText(LawyerDashboardActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseLawyerDeshboard> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                t.printStackTrace();
                Log.e("DoctorDeshboarderror", t.getMessage());

            }
        });


    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(LawyerDashboardActivity.this, CategoryActivity.class);
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
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            if (!pd.isShowing()) {
                pd.show();
            }

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
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
}