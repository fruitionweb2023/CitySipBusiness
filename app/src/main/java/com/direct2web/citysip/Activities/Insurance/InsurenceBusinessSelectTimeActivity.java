package com.direct2web.citysip.Activities.Insurance;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.direct2web.citysip.Activities.Doctor.DoctorBusinessTimingListActivity;
import com.direct2web.citysip.Adapter.RestaurentAdapters.AddSpecialTimeAdapter;
import com.direct2web.citysip.Adapter.RestaurentAdapters.SpinnerTimingTitleListAdapter;
import com.direct2web.citysip.Model.RestaurentModels.Timming.AddSpecialTime;
import com.direct2web.citysip.Model.RestaurentModels.Timming.HourTitle;
import com.direct2web.citysip.Model.RestaurentModels.Timming.ResponseHoursTitleList;
import com.direct2web.citysip.Model.RestaurentModels.Timming.ResponseSendTiming;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityDoctorBusinessSelectTimeBinding;
import com.direct2web.citysip.databinding.ActivityInsurenceBusinessSelectTimeBinding;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsurenceBusinessSelectTimeActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, AddSpecialTimeAdapter.onCanceDishTypellItem {

    ActivityInsurenceBusinessSelectTimeBinding binding;
    SessionManager sessionManager;

    String sunday = "0", monday = "0", tuesday = "0", wednesday = "0", thursday = "0", friday = "0", saturday = "0";

    Boolean bSunday = true, bMonday = true, bTuesday = true, bWednesday = true, bThursday = true, bFriday = true, bSaturday = true;

    List<HourTitle> hourTitleList = new ArrayList<>();
    ProgressDialog pd;

    String cat_id = "";

    SpinnerTimingTitleListAdapter titleListAdapter;

    String rest_open = "0", rest_close = "0", add_open = "0", add_close = "0", hoursTitle = "", hoursId;
    Calendar myCalendar = Calendar.getInstance();
    private static int flag_calander = 0;


    ArrayList<AddSpecialTime> specialTimeList = new ArrayList<>();
    AddSpecialTimeAdapter typeListAdapter;
    boolean goIn = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_insurance_select_timing_activtiy);
        sessionManager = new SessionManager(this);

        cat_id = sessionManager.getBusinessType();
        getData(cat_id);


        binding.txtMonday.setOnClickListener(this);
        binding.txtTuesday.setOnClickListener(this);
        binding.txtWednesday.setOnClickListener(this);
        binding.txtThursday.setOnClickListener(this);
        binding.txtFriday.setOnClickListener(this);
        binding.txtSaturday.setOnClickListener(this);
        binding.txtSunday.setOnClickListener(this);

        binding.cbAdd.setOnCheckedChangeListener(this);

        titleListAdapter = new SpinnerTimingTitleListAdapter(InsurenceBusinessSelectTimeActivity.this, R.layout.raw_recyclear_view_drop_down_timing, R.id.tvName, hourTitleList);
        binding.spinnerTitle.setAdapter(titleListAdapter);

        binding.spinnerTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                hoursTitle = hourTitleList.get(position).getTitle();
                hoursId = hourTitleList.get(position).getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        binding.txtSelectFromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar1 = Calendar.getInstance();
                TimePickerDialog datePickerDialog = new TimePickerDialog(InsurenceBusinessSelectTimeActivity.this, android.R.style.Theme_Holo_Light_Dialog, time, calendar1
                        .get(Calendar.HOUR_OF_DAY), calendar1.get(Calendar.MINUTE), false) {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        super.onClick(dialog, which);
                        if (which == dialog.BUTTON_NEGATIVE) {
                            rest_open = "0";
                            binding.txtSelectFromTime.setText("Select");
                        }
                    }

                };
                datePickerDialog.show();
                flag_calander = 1;

            }
        });

        binding.txtSelectToTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar1 = Calendar.getInstance();
                TimePickerDialog datePickerDialog = new TimePickerDialog(InsurenceBusinessSelectTimeActivity.this, android.R.style.Theme_Holo_Light_Dialog, time, calendar1
                        .get(Calendar.HOUR_OF_DAY), calendar1.get(Calendar.MINUTE), false) {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        super.onClick(dialog, which);
                        if (which == dialog.BUTTON_NEGATIVE) {
                            rest_close = "0";
                            binding.txtSelectToTime.setText("Select");
                        }
                    }

                };
                datePickerDialog.show();
                flag_calander = 2;

            }
        });

        binding.txtAddSelectFromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar1 = Calendar.getInstance();
                TimePickerDialog datePickerDialog = new TimePickerDialog(InsurenceBusinessSelectTimeActivity.this, android.R.style.Theme_Holo_Light_Dialog, time, calendar1
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
                flag_calander = 3;

            }
        });

        binding.txtAddSelectToTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar1 = Calendar.getInstance();

                TimePickerDialog datePickerDialog = new TimePickerDialog(InsurenceBusinessSelectTimeActivity.this, android.R.style.Theme_Holo_Light_Dialog, time, calendar1
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
                flag_calander = 4;

            }
        });

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (add_open.equals("0") && add_close.equals("0")){

                    Toast.makeText(InsurenceBusinessSelectTimeActivity.this, "Please Select Time", Toast.LENGTH_SHORT).show();

                }else {

                    AddSpecialTime dishType = new AddSpecialTime(hoursId, hoursTitle, add_open + "-" + add_close);
                    boolean flag = false;

                    if (specialTimeList.size() == 0) {
                        specialTimeList.add(dishType);
                        goIn = false;

                        typeListAdapter = new AddSpecialTimeAdapter(InsurenceBusinessSelectTimeActivity.this, specialTimeList, InsurenceBusinessSelectTimeActivity.this);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(InsurenceBusinessSelectTimeActivity.this);
                        binding.rclrTiming.setLayoutManager(layoutManager);
                        binding.rclrTiming.setAdapter(typeListAdapter);
                    } else {
                        for (AddSpecialTime d : specialTimeList) {
                            if (d.getTitle().equals(hoursTitle)) {
                                Toast.makeText(InsurenceBusinessSelectTimeActivity.this, "title already available", Toast.LENGTH_SHORT).show();
                                flag = true;
                            }
                        }
                        if (!flag) {
                            specialTimeList.add(dishType);
                            goIn = false;
                            typeListAdapter = new AddSpecialTimeAdapter(InsurenceBusinessSelectTimeActivity.this, specialTimeList, InsurenceBusinessSelectTimeActivity.this);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(InsurenceBusinessSelectTimeActivity.this);
                            binding.rclrTiming.setLayoutManager(layoutManager);
                            binding.rclrTiming.setAdapter(typeListAdapter);
                        }

                    }

                }


            }
        });

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendData();

            }
        });


    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        //SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        if (flag_calander == 1) {
            rest_open = sdf.format(myCalendar.getTime());
            binding.txtSelectFromTime.setText(sdf.format(myCalendar.getTime()));
        } else if (flag_calander == 2) {
            rest_close = sdf.format(myCalendar.getTime());
            binding.txtSelectToTime.setText(sdf.format(myCalendar.getTime()));
        } else if (flag_calander == 3) {
            add_open = sdf.format(myCalendar.getTime());
            binding.txtAddSelectFromTime.setText(sdf.format(myCalendar.getTime()));
        } else if (flag_calander == 4) {
            add_close = sdf.format(myCalendar.getTime());
            binding.txtAddSelectToTime.setText(sdf.format(myCalendar.getTime()));
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.txt_monday:
                if (bMonday) {
                    bMonday = false;
                    monday = "1";

                    binding.txtMonday.setBackground(getResources().getDrawable(R.drawable.circle_blue_doctor));
                    binding.txtMonday.setTextColor(getResources().getColor(R.color.white));
                } else {
                    bMonday = true;
                    monday = "0";

                    binding.txtMonday.setBackground(getResources().getDrawable(R.drawable.weekdays));
                    binding.txtMonday.setTextColor(getResources().getColor(R.color.clr_979592));
                }
                break;

            case R.id.txt_tuesday:
                if (bTuesday) {
                    bTuesday = false;
                    tuesday = "1";

                    binding.txtTuesday.setBackground(getResources().getDrawable(R.drawable.circle_blue_doctor));
                    binding.txtTuesday.setTextColor(getResources().getColor(R.color.white));
                } else {
                    bTuesday = true;
                    tuesday = "0";

                    binding.txtTuesday.setBackground(getResources().getDrawable(R.drawable.weekdays));
                    binding.txtTuesday.setTextColor(getResources().getColor(R.color.clr_979592));
                }
                break;

            case R.id.txt_wednesday:
                if (bWednesday) {
                    bWednesday = false;
                    wednesday = "1";

                    binding.txtWednesday.setBackground(getResources().getDrawable(R.drawable.circle_blue_doctor));
                    binding.txtWednesday.setTextColor(getResources().getColor(R.color.white));
                } else {
                    bWednesday = true;
                    wednesday = "0";

                    binding.txtWednesday.setBackground(getResources().getDrawable(R.drawable.weekdays));
                    binding.txtWednesday.setTextColor(getResources().getColor(R.color.clr_979592));
                }
                break;

            case R.id.txt_thursday:
                if (bThursday) {
                    bThursday = false;
                    thursday = "1";

                    binding.txtThursday.setBackground(getResources().getDrawable(R.drawable.circle_blue_doctor));
                    binding.txtThursday.setTextColor(getResources().getColor(R.color.white));
                } else {
                    bThursday = true;
                    thursday = "0";

                    binding.txtThursday.setBackground(getResources().getDrawable(R.drawable.weekdays));
                    binding.txtThursday.setTextColor(getResources().getColor(R.color.clr_979592));
                }
                break;

            case R.id.txt_friday:
                if (bFriday) {
                    bFriday = false;
                    friday = "1";

                    binding.txtFriday.setBackground(getResources().getDrawable(R.drawable.circle_blue_doctor));
                    binding.txtFriday.setTextColor(getResources().getColor(R.color.white));
                } else {
                    bFriday = true;
                    friday = "0";

                    binding.txtFriday.setBackground(getResources().getDrawable(R.drawable.weekdays));
                    binding.txtFriday.setTextColor(getResources().getColor(R.color.clr_979592));
                }
                break;

            case R.id.txt_saturday:
                if (bSaturday) {
                    bSaturday = false;
                    saturday = "1";

                    binding.txtSaturday.setBackground(getResources().getDrawable(R.drawable.circle_blue_doctor));
                    binding.txtSaturday.setTextColor(getResources().getColor(R.color.white));
                } else {
                    bSaturday = true;
                    saturday = "0";

                    binding.txtSaturday.setBackground(getResources().getDrawable(R.drawable.weekdays));
                    binding.txtSaturday.setTextColor(getResources().getColor(R.color.clr_979592));
                }
                break;

            case R.id.txt_sunday:
                if (bSunday) {
                    bSunday = false;
                    sunday = "1";

                    binding.txtSunday.setBackground(getResources().getDrawable(R.drawable.circle_blue_doctor));
                    binding.txtSunday.setTextColor(getResources().getColor(R.color.white));
                } else {
                    bSunday = true;
                    sunday = "0";

                    binding.txtSunday.setBackground(getResources().getDrawable(R.drawable.weekdays));
                    binding.txtSunday.setTextColor(getResources().getColor(R.color.clr_979592));
                }
                break;


        }

    }


    private void getData(String cat_id) {

        pd = new ProgressDialog(InsurenceBusinessSelectTimeActivity.this);
        pd.setMessage("Please Wait.....");
        pd.setCancelable(false);
        pd.show();

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseHoursTitleList> call = api.getInsurenceHoursTitleList("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, cat_id);
        call.enqueue(new Callback<ResponseHoursTitleList>() {
            @Override
            public void onResponse(Call<ResponseHoursTitleList> call, Response<ResponseHoursTitleList> response) {

                Log.e("responseInsurenceTitle", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(InsurenceBusinessSelectTimeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        hourTitleList = response.body().getHourTitle();
                        titleListAdapter = new SpinnerTimingTitleListAdapter(InsurenceBusinessSelectTimeActivity.this,R.layout.raw_recyclear_view_drop_down_timing,R.id.tvName,hourTitleList);
                        binding.spinnerTitle.setAdapter(titleListAdapter);

                    }

                } else {

                    Toast.makeText(InsurenceBusinessSelectTimeActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseHoursTitleList> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                t.printStackTrace();
                Log.e("error", t.getMessage());
            }
        });


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {

            binding.llAdd.setVisibility(View.VISIBLE);

        } else {

            binding.llAdd.setVisibility(View.GONE);
            if (!goIn) {
                specialTimeList.clear();
                typeListAdapter.updateDataList(specialTimeList);
            }

            add_close = "0";
            binding.txtAddSelectToTime.setText("Select");

            add_open = "0";
            binding.txtAddSelectFromTime.setText("Select");

        }

    }

    @Override
    public void onDishTypeItemCancel(AddSpecialTime data) {


        specialTimeList.remove(data);

        typeListAdapter.updateDataList(specialTimeList);


    }


    public void sendData() {


        pd = new ProgressDialog(InsurenceBusinessSelectTimeActivity.this);
        pd.setMessage("Please Wait.....");
        pd.setCancelable(false);
        pd.show();

        if (sunday.equals("0") && monday.equals("0") && tuesday.equals("0") && wednesday.equals("0") && thursday.equals("0") && friday.equals("0") && saturday.equals("0")) {


            pd.dismiss();
            Toast.makeText(InsurenceBusinessSelectTimeActivity.this, "Please Select WeekDays", Toast.LENGTH_SHORT).show();


        } else if (rest_open.equals("0")) {
            pd.dismiss();
            Toast.makeText(InsurenceBusinessSelectTimeActivity.this, "Please Select restaurant Open Time", Toast.LENGTH_SHORT).show();
        } else if (rest_close.equals("0")) {
            pd.dismiss();
            Toast.makeText(InsurenceBusinessSelectTimeActivity.this, "Please Select restaurant Close Time", Toast.LENGTH_SHORT).show();
        } else {

            String days = monday + "~~" + tuesday + "~~" + wednesday + "~~" + thursday + "~~" + friday + "~~" + saturday + "~~" + sunday;

            String defaultId = "0";
            String defaultTime = rest_open + "-" + rest_close;

            AddSpecialTime specialTime = new AddSpecialTime(defaultId, "", defaultTime);

            specialTimeList.add(0, specialTime);


            String time = "", id = "";
            StringBuilder sb = new StringBuilder();
            for (AddSpecialTime u : specialTimeList) {
                sb.append(u.getTime()).append("~~");
            }
            time = sb.toString();
            time = time.substring(0, time.length() - 2);
            Log.e("string", time);

            StringBuilder sbid = new StringBuilder();
            for (AddSpecialTime u : specialTimeList) {
                sbid.append(u.getId()).append("~~");
            }
            id = sbid.toString();
            id = id.substring(0, id.length() - 2);
            Log.e("string", id);

            Api api = RetrofitClient.getClient().create(Api.class);
            Call<ResponseSendTiming> call = api.sendInsurenceTiming("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                    WS_URL_PARAMS.access_key, sessionManager.getUserId(), id, days, time);

            Log.e("SendDataTime : ", "userId : " + sessionManager.getUserId() + "\n id : " + id + "\n Days : " + days + "\n Time : " + time );
            call.enqueue(new Callback<ResponseSendTiming>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<ResponseSendTiming> call, Response<ResponseSendTiming> response) {

                    Log.e("responseInsurenceSendTiming", new Gson().toJson(response.body()));
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                    if (response.body() != null && response.isSuccessful()) {

                        if (response.body().getError()) {

                            Toast.makeText(InsurenceBusinessSelectTimeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        } else {

                            Intent intent = new Intent(InsurenceBusinessSelectTimeActivity.this, InsurenceBusinessTimingListActivity.class);
                            finish();
                            startActivity(intent);

                        }

                    } else {
                        Toast.makeText(InsurenceBusinessSelectTimeActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onFailure(Call<ResponseSendTiming> call, Throwable t) {

                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                    t.printStackTrace();
                    Log.e("errorSendTiming", t.getMessage());
                }
            });


        }


    }

}