package com.direct2web.citysip.Activities.Doctor;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import com.direct2web.citysip.Adapter.DoctorAdapters.DoctorAddSpecialTimeAdapter;
import com.direct2web.citysip.Adapter.DoctorAdapters.DoctorSpinnerTimingTitleListAdapter;
import com.direct2web.citysip.Model.DoctorModels.DoctorServices.ResponseDoctorServices;
import com.direct2web.citysip.Model.DoctorModels.DoctorServices.Service;
import com.direct2web.citysip.Model.DoctorModels.ResponseDoctorSendTiming;
import com.direct2web.citysip.Model.RestaurentModels.Timming.AddSpecialTime;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.CustomListViewDialogWithSearch;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityDoctorSelectTimingActivtiyBinding;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorSelectTimingActivtiy extends AppCompatActivity implements View.OnClickListener, DoctorAddSpecialTimeAdapter.onCanceDishTypellItem, DoctorSpinnerTimingTitleListAdapter.RecyclerViewItemClickListener {


    ActivityDoctorSelectTimingActivtiyBinding binding;
    SessionManager sessionManager;

    String sunday = "0", monday = "0", tuesday = "0", wednesday = "0", thursday = "0", friday = "0", saturday = "0";

    Boolean bSunday = true, bMonday = true, bTuesday = true, bWednesday = true, bThursday = true, bFriday = true, bSaturday = true;

    ProgressDialog pd;

    String add_open = "0", add_close = "0", hoursTitle = "", hoursId="";
    Calendar myCalendar = Calendar.getInstance();
    private static int flag_calander = 0;


    ArrayList<AddSpecialTime> specialTimeList = new ArrayList<>();
    DoctorAddSpecialTimeAdapter doctorAddSpecialTimeAdapter;
    boolean goIn = true;

    List<Service> serviceList = new ArrayList<>();
    DoctorSpinnerTimingTitleListAdapter titleListAdapter;
    CustomListViewDialogWithSearch customDialog;

    List<Service> temp;
    String s_id;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_select_timing_activtiy);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_select_timing_activtiy);
        sessionManager = new SessionManager(this);

        getServiceList(sessionManager.getUserId());

        binding.txtMonday.setOnClickListener(this);
        binding.txtTuesday.setOnClickListener(this);
        binding.txtWednesday.setOnClickListener(this);
        binding.txtThursday.setOnClickListener(this);
        binding.txtFriday.setOnClickListener(this);
        binding.txtSaturday.setOnClickListener(this);
        binding.txtSunday.setOnClickListener(this);


        binding.spinnerTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                titleListAdapter = new DoctorSpinnerTimingTitleListAdapter( temp,DoctorSelectTimingActivtiy.this);
                customDialog = new CustomListViewDialogWithSearch(DoctorSelectTimingActivtiy.this, titleListAdapter,temp);

                customDialog.show();
                customDialog.setCanceledOnTouchOutside(false);


            }
        });




        /*binding.spinnerTitle.setAdapter(titleListAdapter);
        binding.spinnerTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hoursTitle = serviceList.get(position).getDoctorName() + "-" + serviceList.get(position).getServiceName();
                hoursId = serviceList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                updateLabel();
            }
        };

        binding.txtAddSelectFromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar1 = Calendar.getInstance();
                TimePickerDialog datePickerDialog = new TimePickerDialog(DoctorSelectTimingActivtiy.this, time, calendar1
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
                flag_calander = 1;

            }
        });

        binding.txtAddSelectToTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar1 = Calendar.getInstance();
                TimePickerDialog datePickerDialog = new TimePickerDialog(DoctorSelectTimingActivtiy.this, time, calendar1
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
                flag_calander = 2;

            }
        });

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                if (add_open.equals("0") && add_close.equals("0")){

                    Toast.makeText(DoctorSelectTimingActivtiy.this, "Please Select Time", Toast.LENGTH_SHORT).show();

                }else {

                    AddSpecialTime dishType = new AddSpecialTime(hoursId, hoursTitle, add_open + "-" + add_close,add_open,add_close);
                    boolean flag = false;

                    if (specialTimeList.size() == 0) {
                        specialTimeList.add(dishType);
                        goIn = false;

                        doctorAddSpecialTimeAdapter = new DoctorAddSpecialTimeAdapter(DoctorSelectTimingActivtiy.this, specialTimeList, DoctorSelectTimingActivtiy.this);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(DoctorSelectTimingActivtiy.this);
                        binding.rclrTiming.setLayoutManager(layoutManager);
                        binding.rclrTiming.setAdapter(doctorAddSpecialTimeAdapter);
                    } else {

                        for (AddSpecialTime d : specialTimeList) {
                            if (d.getTime().equals(add_open + "-" + add_close)) {
                                Toast.makeText(DoctorSelectTimingActivtiy.this, "time already available", Toast.LENGTH_SHORT).show();
                                flag = true;
                            }else if (checkTime(d.getInTime(),d.getOutTime(),add_open)){
                                Toast.makeText(DoctorSelectTimingActivtiy.this, "between time already available", Toast.LENGTH_SHORT).show();
                                flag = true;
                            }
                        }

                        if (!flag) {

                            specialTimeList.add(dishType);
                            goIn = false;
                            doctorAddSpecialTimeAdapter = new DoctorAddSpecialTimeAdapter(DoctorSelectTimingActivtiy.this, specialTimeList, DoctorSelectTimingActivtiy.this);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(DoctorSelectTimingActivtiy.this);
                            binding.rclrTiming.setLayoutManager(layoutManager);
                            binding.rclrTiming.setAdapter(doctorAddSpecialTimeAdapter);
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

    private void getServiceList(String userId) {

        pd = new ProgressDialog(DoctorSelectTimingActivtiy.this);
        pd.setMessage("Please Wait.....");
        pd.setCancelable(false);
        pd.show();

        Api api = RetrofitClient.getClient().create(Api.class);

        Call<ResponseDoctorServices> call = api.getServices("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key,userId);
        call.enqueue(new Callback<ResponseDoctorServices>() {
            @Override
            public void onResponse(Call<ResponseDoctorServices> call, Response<ResponseDoctorServices> response) {
                Log.e("responseServiceList", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }

                if (response.body() != null && response.isSuccessful()) {
                    Log.e("userId", sessionManager.getUserId());

                    if (response.body().getError()) {

                        Toast.makeText(DoctorSelectTimingActivtiy.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        serviceList = response.body().getServiceList();

                        temp = new ArrayList<>();


                        for (int i = 0; i < serviceList.size(); i++) {

                            temp.add(new Service(serviceList.get(i).getId(), serviceList.get(i).getDoctorName()+" - "+serviceList.get(i).getServiceName()));

                        }



                    }

                }else {
                    Toast.makeText(DoctorSelectTimingActivtiy.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDoctorServices> call, Throwable t) {

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                Log.e("ServiceListError", t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        //SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        if (flag_calander == 1) {
            add_open = sdf.format(myCalendar.getTime());
            binding.txtAddSelectFromTime.setText(sdf.format(myCalendar.getTime()));
        } else if (flag_calander == 2) {
            add_close = sdf.format(myCalendar.getTime());
            binding.txtAddSelectToTime.setText(sdf.format(myCalendar.getTime()));
        }




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.txt_monday:
                if (bMonday) {
                    bMonday = false;
                    monday = "1";

                    binding.txtMonday.setBackground(getResources().getDrawable(R.drawable.select_week_days_doctor));
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

                    binding.txtTuesday.setBackground(getResources().getDrawable(R.drawable.select_week_days_doctor));
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

                    binding.txtWednesday.setBackground(getResources().getDrawable(R.drawable.select_week_days_doctor));
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

                    binding.txtThursday.setBackground(getResources().getDrawable(R.drawable.select_week_days_doctor));
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

                    binding.txtFriday.setBackground(getResources().getDrawable(R.drawable.select_week_days_doctor));
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

                    binding.txtSaturday.setBackground(getResources().getDrawable(R.drawable.select_week_days_doctor));
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

                    binding.txtSunday.setBackground(getResources().getDrawable(R.drawable.select_week_days_doctor));
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

    @Override
    public void onDishTypeItemCancel(AddSpecialTime d) {
        specialTimeList.remove(d);
        doctorAddSpecialTimeAdapter.updateDataList(specialTimeList);
    }


    public void sendData() {


        pd = new ProgressDialog(DoctorSelectTimingActivtiy.this);
        pd.setMessage("Please Wait.....");
        pd.setCancelable(false);
        pd.show();

        if (sunday.equals("0") && monday.equals("0") && tuesday.equals("0") && wednesday.equals("0") && thursday.equals("0") && friday.equals("0") && saturday.equals("0")) {


            pd.dismiss();
            Toast.makeText(DoctorSelectTimingActivtiy.this, "Please Select WeekDays", Toast.LENGTH_SHORT).show();


        } else if (add_open.equals("0")) {
            pd.dismiss();
            Toast.makeText(DoctorSelectTimingActivtiy.this, "Please Select restaurant Open Time", Toast.LENGTH_SHORT).show();
        } else if (add_close.equals("0")) {
            pd.dismiss();
            Toast.makeText(DoctorSelectTimingActivtiy.this, "Please Select restaurant Close Time", Toast.LENGTH_SHORT).show();
        } else {

            String days = monday + "~~" + tuesday + "~~" + wednesday + "~~" + thursday + "~~" + friday + "~~" + saturday + "~~" + sunday;

            /*String defaultId = "0";
            String defaultTime = add_open + "-" + add_close;

            AddSpecialTime specialTime = new AddSpecialTime(defaultId, "", defaultTime);

            specialTimeList.add(0, specialTime);*/

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
            Call<ResponseDoctorSendTiming> call = api.sendDoctorTiming("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                    WS_URL_PARAMS.access_key, id, days, time);
            call.enqueue(new Callback<ResponseDoctorSendTiming>() {
                @Override
                public void onResponse(Call<ResponseDoctorSendTiming> call, Response<ResponseDoctorSendTiming> response) {

                    Log.e("responseSendTiming", new Gson().toJson(response.body()));
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                    if (response.body() != null && response.isSuccessful()) {

                        if (response.body().getError()) {

                            Toast.makeText(DoctorSelectTimingActivtiy.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        } else {

                            Intent intent = new Intent(DoctorSelectTimingActivtiy.this, DoctorSetUpTimingListActivity.class);
                            finish();
                            startActivity(intent);

                        }

                    } else {
                        Toast.makeText(DoctorSelectTimingActivtiy.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onFailure(Call<ResponseDoctorSendTiming> call, Throwable t) {

                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                    t.printStackTrace();
                    Log.e("errorSendTiming", t.getMessage());
                }
            });


        }


    }

    @Override
    public void clickOnPartyListItem(String data, String id) {

        binding.spinnerTitle.setText(data);
        binding.spinnerTitle.setTextColor(getResources().getColor(R.color.black));

        hoursTitle = data;
        hoursId = id;

//        getAccountList(id);

        if (customDialog != null) {
            customDialog.dismiss();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static boolean checkTime(String startTime, String endTime, String checkTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault());
//        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        LocalTime startLocalTime = LocalTime.parse(startTime, formatter);
        LocalTime endLocalTime = LocalTime.parse(endTime, formatter);
        LocalTime checkLocalTime = LocalTime.parse(checkTime, formatter);

        boolean isInBetween = false;
        if (endLocalTime.isAfter(startLocalTime)) {
            if (startLocalTime.isBefore(checkLocalTime) && endLocalTime.isAfter(checkLocalTime)) {
                isInBetween = true;
            }
        } else if (checkLocalTime.isAfter(startLocalTime) || checkLocalTime.isBefore(endLocalTime)) {
            isInBetween = true;
        }

        /*if (isInBetween) {
            System.out.println("Is in between!");
        } else {
            System.out.println("Is not in between!");
        }*/
        return isInBetween;
    }

}