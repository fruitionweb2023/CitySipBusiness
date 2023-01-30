package com.direct2web.citysip.Activities.Restaurent;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;

import com.direct2web.citysip.Model.RestaurentModels.Profile.ResponseSendProfile;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.ConnectionToInternet;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityEditLocationFetchBinding;
import com.direct2web.citysip.databinding.ActivityLocationFetchBinding;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditLocationFetchActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    ActivityEditLocationFetchBinding binding;

    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;

    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    AutocompleteSupportFragment placeAutoComplete;

    String latitude = "", longitude = "";
    String address = "";

    SessionManager sessionManager;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_location_fetch);

        binding.progressBarCyclic.setVisibility(View.VISIBLE);
        binding.llProgressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        sessionManager = new SessionManager(this);

        binding.edtRestName.setText(getIntent().getStringExtra("address1"));
        String str = getIntent().getStringExtra("address2");
        String delimiter = ",";
        String[] temp = str.split(delimiter);
        String streetAdd = temp[0];
        String roadAdd = temp[1];
        binding.edtStreet.setText(streetAdd);
        binding.edtStreet.setText(roadAdd);
        latitude = getIntent().getStringExtra("lat");
        longitude = getIntent().getStringExtra("lang");

        Log.e("Lat-Lang : ",latitude + " , " + longitude );

        configureCameraIdle();



        binding.btnVerify.setOnClickListener(v -> {

            if (address.equals("")) {

                Toast.makeText(EditLocationFetchActivity.this, "Please Wait", Toast.LENGTH_SHORT).show();


            } else if (binding.edtRestName.getText().toString().equals("")) {

                binding.edtRestName.setError("Field can not be Empty");

            }else {


                if (new ConnectionToInternet(EditLocationFetchActivity.this).isConnectingToInternet()) {
                    sendProfile(sessionManager.getUserId());
                } else {
                    new ConnectionToInternet(EditLocationFetchActivity.this).ShowDilog(EditLocationFetchActivity.this);
                }
            }

        });


    }

    private void sendProfile(String userId) {

        pd = new ProgressDialog(EditLocationFetchActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        String name = binding.edtRestName.getText().toString();
        String street = binding.edtStreet.getText().toString();

        String line1;

        if (street.equals("")) {
            line1 = name;
        } else {

            line1 = name + ", " + street;

        }


        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseSendProfile> call = api.editProfile("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId, line1, address, "", "", "",
                "", "", "",  latitude, longitude, "");
        call.enqueue(new Callback<ResponseSendProfile>() {
            @Override
            public void onResponse(Call<ResponseSendProfile> call, Response<ResponseSendProfile> response) {

                Log.e("responseEditLocation", new Gson().toJson(response.body()));
                pd.dismiss();
                if (response.body() != null && response.isSuccessful()) {

                    if (!response.body().getError()) {

                        Intent i = new Intent(EditLocationFetchActivity.this, SetUpAboutRestaurantActivity.class);
                        finish();
                        startActivity(i);

                    } else {
                        Toast.makeText(EditLocationFetchActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(EditLocationFetchActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(Call<ResponseSendProfile> call, Throwable t) {
                pd.dismiss();
                t.printStackTrace();
                Log.e("errorEditLocation", t.getMessage());
            }
        });


    }

    private void configureCameraIdle() {
        onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                LatLng latLng = mMap.getCameraPosition().target;
                Geocoder geocoder = new Geocoder(EditLocationFetchActivity.this);

                try {
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addressList != null && addressList.size() > 0) {


                        latitude = String.valueOf(addressList.get(0).getLatitude());
                        longitude = String.valueOf(addressList.get(0).getLongitude());

                        address = addressList.get(0).getAddressLine(0);

//                        String city = addressList.get(0).get();

                        binding.dragResult.setText(address);

                        Log.e("address", "" + address);

                        binding.progressBarCyclic.setVisibility(View.GONE);
                        binding.llProgressBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                mMap.setOnCameraIdleListener(onCameraIdleListener);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            mMap.setOnCameraIdleListener(onCameraIdleListener);
        }


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        //mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(EditLocationFetchActivity.this, SetUpAboutRestaurantActivity.class);
        finish();
        startActivity(i);
    }
}
