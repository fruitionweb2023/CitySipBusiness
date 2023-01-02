package com.direct2web.citysip.Activities.Restaurent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.direct2web.citysip.Model.RestaurentModels.offer.Offer;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.databinding.ActivitySetUpEditCouponsBinding;

import java.util.List;

public class SetUpEditCoupons extends AppCompatActivity {

    ActivitySetUpEditCouponsBinding binding;
    SessionManager sessionManager;
    List<Offer> offerList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_edit_coupons);

        sessionManager = new SessionManager(this);


    }
}