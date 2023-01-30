package com.direct2web.citysip.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.webkit.WebViewClient;

import com.direct2web.citysip.Activities.Restaurent.MainActivity;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.databinding.ActivityMainBinding;
import com.direct2web.citysip.databinding.ActivityTermsAndConditionsBinding;

import java.util.Objects;

public class TermsAndConditionsActivity extends AppCompatActivity {

    ActivityTermsAndConditionsBinding binding;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_terms_and_conditions);
        sessionManager = new SessionManager(this);



        if (Objects.equals(sessionManager.getBusinessType(), "1")) {
            binding.toolbarLogo.setBackgroundResource(R.drawable.ic_lawyer_logo);
        }else if (Objects.equals(sessionManager.getBusinessType(), "2")){
            binding.toolbarLogo.setBackgroundResource(R.drawable.spa_salon_logo);
        }else if (Objects.equals(sessionManager.getBusinessType(), "3")){
            binding.toolbarLogo.setBackgroundResource(R.drawable.ic_logo_insurance);
        }else if (Objects.equals(sessionManager.getBusinessType(), "4")){
            binding.toolbarLogo.setBackgroundResource(R.drawable.ic_doctor_logo);
        }else if (Objects.equals(sessionManager.getBusinessType(), "5")){
            binding.toolbarLogo.setBackgroundResource(R.drawable.ic_lawyer_logo);
        }else if (Objects.equals(sessionManager.getBusinessType(), "6")){
            binding.toolbarLogo.setBackgroundResource(R.drawable.restaurant_logo_new);
        }

        binding.webView.setWebViewClient(new WebViewClient());
//        binding.webView.setInitialScale(1);
//        binding.webView.getSettings().setLoadWithOverviewMode(true);
//        binding.webView.getSettings().setUseWideViewPort(true);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setSupportZoom(true);
        binding.webView.loadUrl(Api.terms);
    }
}