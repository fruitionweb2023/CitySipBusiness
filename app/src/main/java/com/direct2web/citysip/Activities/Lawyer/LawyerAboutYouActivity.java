package com.direct2web.citysip.Activities.Lawyer;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.direct2web.citysip.Activities.Doctor.DoctorAboutBusinessActivity;
import com.direct2web.citysip.Activities.Doctor.DoctorAboutHospitalActivity;
import com.direct2web.citysip.Activities.Doctor.DoctorAboutYouActivity;
import com.direct2web.citysip.Model.DoctorModels.DoctorAboutYou.ResponseDoctorAboutYou;
import com.direct2web.citysip.Model.LawyerModels.LawyerAboutYou.ResponseLawyerAboutYou;
import com.direct2web.citysip.Model.RestaurentModels.Profile.ResponseSendProfile;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.ConnectionToInternet;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityDoctorAboutYouBinding;
import com.direct2web.citysip.databinding.ActivityLawyerAboutYouBinding;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LawyerAboutYouActivity extends AppCompatActivity {

    ActivityLawyerAboutYouBinding binding;
    SessionManager sessionManager;
    ProgressDialog pd;
    Calendar calendar = Calendar.getInstance();
    DatePickerDialog picker;
    DatePickerDialog.OnDateSetListener date;

    private static int flag_calander = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lawyer_about_you);
        sessionManager = new SessionManager(this);

      /*  if (getIntent().getStringExtra("flag").equals("1")) {

            binding.edtRestName.setText(getIntent().getStringExtra("userName"));
            binding.edtRestNumber.setText(getIntent().getStringExtra("contect"));
            binding.edtRestWebsite.setText(getIntent().getStringExtra("dob"));
            binding.edtRestDescription.setText(getIntent().getStringExtra("nationality"));
            binding.btnVerify.setText("Edit");
        } else {

            binding.edtRestName.setText("");
            binding.edtRestNumber.setText("");
            binding.edtRestWebsite.setText("Select Date");
            binding.edtRestDescription.setText("");
            binding.btnVerify.setText("Continue");
        }*/

        binding.edtRestName.addTextChangedListener(tw);
        binding.edtRestNumber.addTextChangedListener(tw);
        binding.edtRestWebsite.addTextChangedListener(tw);
        binding.edtRestDescription.addTextChangedListener(tw);

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                updateLabel();

            }


        };

        binding.edtRestWebsite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar calendar1 = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(LawyerAboutYouActivity.this, date, calendar1
                        .get(Calendar.YEAR), calendar1.get(Calendar.MONTH),
                        calendar1.get(Calendar.DAY_OF_MONTH)) {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        super.onClick(dialog, which);
                        if (which == dialog.BUTTON_NEGATIVE) {
                            binding.edtRestWebsite.setText("Select Date");
                        }
                    }

                };
                datePickerDialog.show();
                flag_calander = 1;
            }
        });

        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.edtRestName.getText().toString().equals("")) {

                    binding.edtRestName.setError("Field can not be empty");

                } else if (binding.edtRestNumber.getText().toString().equals("") && binding.edtRestNumber.getText().length() == 10) {

                    binding.edtRestNumber.setError("Peese enter valid phoneNo.");

                } else if (binding.edtRestWebsite.getText().toString().equals("")) {

                    binding.edtRestWebsite.setError("Field can not be empty");

                } else if (binding.edtRestDescription.getText().toString().equals("")) {

                    binding.edtRestDescription.setError("Field can not be empty");

                } else {

                    if (new ConnectionToInternet(LawyerAboutYouActivity.this).isConnectingToInternet()) {
                        sendProfile(sessionManager.getUserId());

                       /* if (getIntent().getStringExtra("flag").equals("1")) {

                            editProfile(sessionManager.getUserId());
                        } else {
                            sendProfile(sessionManager.getUserId());
                        }*/
                    } else {
                        new ConnectionToInternet(LawyerAboutYouActivity.this).ShowDilog(LawyerAboutYouActivity.this);
                    }

                }

            }
        });
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if (flag_calander == 1) {
            binding.edtRestWebsite.setText(sdf.format(calendar.getTime()));
        }


    }

    private void sendProfile(String userId) {


        pd = new ProgressDialog(LawyerAboutYouActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        String name = binding.edtRestName.getText().toString();
        String number = binding.edtRestNumber.getText().toString();
        String dateOfBirth = binding.edtRestWebsite.getText().toString();
        String nationality = binding.edtRestDescription.getText().toString();

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseLawyerAboutYou> call = api.sendLawyerAboutDetails("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key,userId,name,number,dateOfBirth,nationality);
        call.enqueue(new Callback<ResponseLawyerAboutYou>() {
            @Override
            public void onResponse(Call<ResponseLawyerAboutYou> call, Response<ResponseLawyerAboutYou> response) {

                Log.e("responseSendProfile",new Gson().toJson(response.body()));
                pd.dismiss();
                if (response.body() != null && response.isSuccessful()){

                    if (!response.body().getError()){

                        Intent i = new Intent(LawyerAboutYouActivity.this, LawyerAboutOfficeActivity.class);
                        startActivity(i);

                    }else {
                        Toast.makeText(LawyerAboutYouActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(LawyerAboutYouActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseLawyerAboutYou> call, Throwable t) {
                pd.dismiss();
                t.printStackTrace();
                Log.e("errorSendProfile", t.getMessage());
            }
        });
    }

    private void editProfile(String userId) {


        pd = new ProgressDialog(LawyerAboutYouActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        String name = binding.edtRestName.getText().toString();
        String number = binding.edtRestNumber.getText().toString();
        String dateOfBirth = binding.edtRestWebsite.getText().toString();
        String nationality = binding.edtRestDescription.getText().toString();

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseSendProfile> call = api.editLawyerAboutyou("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key,userId,name,number,dateOfBirth,nationality);

        call.enqueue(new Callback<ResponseSendProfile>() {
            @Override
            public void onResponse(Call<ResponseSendProfile> call, Response<ResponseSendProfile> response) {

                Log.e("responseEditProfile",new Gson().toJson(response.body()));
                pd.dismiss();
                if (response.body() != null && response.isSuccessful()){

                    if (!response.body().getError()){

                        Intent i = new Intent(LawyerAboutYouActivity.this, LawyerAboutBusinessActivity.class);
                        finish();
                        startActivity(i);

                    }else {
                        Toast.makeText(LawyerAboutYouActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(LawyerAboutYouActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseSendProfile> call, Throwable t) {
                pd.dismiss();
                t.printStackTrace();
                Log.e("errorEditProfile", t.getMessage());
            }
        });
    }


    TextWatcher tw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (binding.edtRestName.getText().length() > 0 && binding.edtRestNumber.getText().length() > 0 && binding.edtRestWebsite.getText().length() > 0 && binding.edtRestDescription.getText().length() > 0) {
                binding.btnVerify.setTextColor(getResources().getColor(R.color.clr_f8f8f8));
                binding.btnVerify.setBackground(getResources().getDrawable(R.drawable.button_doctor));

            } else {

                binding.btnVerify.setTextColor(getResources().getColor(R.color.cle_979592));
                binding.btnVerify.setBackground(getResources().getDrawable(R.drawable.button_disable));
            }
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };
}