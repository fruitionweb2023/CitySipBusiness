package com.direct2web.citysip.Utils;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.direct2web.citysip.Activities.Doctor.DoctorBusinessProfileActivity;
import com.direct2web.citysip.Activities.Doctor.DoctorCouponsActivity;
import com.direct2web.citysip.Activities.Doctor.DoctorDeshboardActivity;
import com.direct2web.citysip.Activities.Doctor.DoctorServicesActivity;
import com.direct2web.citysip.Activities.Insurance.InsuranceAppointmentActivity;
import com.direct2web.citysip.Activities.Insurance.InsuranceDashboardActivity;
import com.direct2web.citysip.Activities.Insurance.InsuranceMenuActivity;
import com.direct2web.citysip.Activities.Insurance.InsuranceServicesActivity;
import com.direct2web.citysip.Activities.Lawyer.LawyerAppointmentActivity;
import com.direct2web.citysip.Activities.Lawyer.LawyerDashboardActivity;
import com.direct2web.citysip.Activities.Lawyer.LawyerMenuActivity;
import com.direct2web.citysip.Activities.Lawyer.LawyerServicesActivity;
import com.direct2web.citysip.Activities.Restaurent.MainActivity;
import com.direct2web.citysip.Activities.Restaurent.MenuActivity;
import com.direct2web.citysip.Activities.Restaurent.SetUpCouponsActivity;
import com.direct2web.citysip.Activities.Restaurent.SetUpMenuActivity;
import com.direct2web.citysip.Activities.SpaAndSalon.SapAndSalonDeshboardActivity;
import com.direct2web.citysip.Activities.SpaAndSalon.SpaAndSalonAppointmentActivity;
import com.direct2web.citysip.Activities.SpaAndSalon.SpaAndSalonBusinessProfileActivity;
import com.direct2web.citysip.Activities.SpaAndSalon.SpaAndSalonCouponsActivity;
import com.direct2web.citysip.Activities.SpaAndSalon.SpaAndSalonServicesActivity;
import com.direct2web.citysip.R;


public class BottomButtonClickListner implements View.OnClickListener {


    Context context;
    SessionManager sessionManager;

    public BottomButtonClickListner(Context context, SessionManager sessionManager) {
        this.context = context;
        this.sessionManager = sessionManager;



    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.bb_home) {

            switch (sessionManager.getBusinessType()) {
                case "6":
                    context.startActivity(new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
                case "4":
                    context.startActivity(new Intent(context, DoctorDeshboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
                case "1":
                    context.startActivity(new Intent(context, LawyerDashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
                case "3":
                    context.startActivity(new Intent(context, InsuranceDashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
                case "2":
                    context.startActivity(new Intent(context, SapAndSalonDeshboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
            }

        } else if (id == R.id.bb_my_business) {

            switch (sessionManager.getBusinessType()) {
                case "6":
                    context.startActivity(new Intent(context, SetUpMenuActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
                case "4":
                    context.startActivity(new Intent(context, DoctorServicesActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
                case "1":
                    context.startActivity(new Intent(context, LawyerServicesActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
                case "3":
//                    Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, InsuranceServicesActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
                case "2":
//                    Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, SpaAndSalonServicesActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
            }

        } else if (id == R.id.bb_order) {

            switch (sessionManager.getBusinessType()) {
                case "6":
                    context.startActivity(new Intent(context, SetUpCouponsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
                case "4":
                    context.startActivity(new Intent(context, DoctorCouponsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
                case "2":
                    context.startActivity(new Intent(context, SpaAndSalonCouponsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;

            }

        } else if (id == R.id.bb_menu) {

            switch (sessionManager.getBusinessType()) {
                case "6":
                    context.startActivity(new Intent(context, MenuActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
                case "4":
                    context.startActivity(new Intent(context, DoctorBusinessProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
                case "1":
                    context.startActivity(new Intent(context, LawyerMenuActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
                case "3":
//                    Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, InsuranceMenuActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
                case "2":
//                    Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, SpaAndSalonBusinessProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
            }
            //((Activity) context).finish();
        } else if (id == R.id.bb_appointment) {

            switch (sessionManager.getBusinessType()) {

                case "1":
                    context.startActivity(new Intent(context, LawyerAppointmentActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
                case "3":
//                    Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, InsuranceAppointmentActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
               /* case "2":
//                    Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, SpaAndSalonAppointmentActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;*/

            }
        }
    }
}
