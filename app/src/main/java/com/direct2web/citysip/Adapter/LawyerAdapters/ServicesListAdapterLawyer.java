package com.direct2web.citysip.Adapter.LawyerAdapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.direct2web.citysip.Adapter.DoctorAdapters.ServicesListAdapter;
import com.direct2web.citysip.Model.LawyerModels.LawyerDeleteData.ResponseLawyerDelete;
import com.direct2web.citysip.Model.LawyerModels.LawyerServices.Service;
import com.direct2web.citysip.Model.RestaurentModels.Delete.ResponseStatus;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.RawDoctorAddServicesBinding;
import com.direct2web.citysip.databinding.RawLawyerAddServicesBinding;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicesListAdapterLawyer extends RecyclerView.Adapter<ServicesListAdapterLawyer.ViewHolder> {

    List<Service> serviceList;
    Context context;
    ProgressDialog pd;
    int remove;
    OnItemClickListner addButtonClick;



    public ServicesListAdapterLawyer(List<Service> serviceList, Context context,OnItemClickListner addButtonClick) {
        this.serviceList = serviceList;
        this.context = context;
        this.addButtonClick = addButtonClick;

    }

    @NonNull
    @Override
    public ServicesListAdapterLawyer.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RawLawyerAddServicesBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_lawyer_add_services, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Service service = serviceList.get(position);

        holder.binding.txtServiceName.setText(service.getServiceName());
        holder.binding.txtDescription.setText(service.getDescription());
        holder.binding.txtName.setText(service.getDoctorName());
        holder.binding.txtPrice.setText(service.getAmount());

        Glide.with(context).load(Api.imageUrl + service.getImage())
                .thumbnail(0.5f)
                .error(R.drawable.city_sip_logo)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.binding.imgServiceLogo);

        holder.binding.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure want to delete ?");
                builder.setPositiveButton("yes", (dialog, which) -> {
                    remove = position;
                    delete(new SessionManager(context).getUserId(), "hospital_services", service.getId());
                });
                builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        holder.binding.switchOnOff.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (!isChecked) {

//                    onClickSwitchOff.onSwitchItemClickOff(position);
                holder.binding.llBottom.setAlpha(0.25f);
                holder.binding.imgEdit.setClickable(false);
                holder.binding.imgDelete.setClickable(false);
                sendStatus(new SessionManager(context).getUserId(),"hospital_services",service.getId(),"0");

            } else {

//                    onClickSwitch.onSwitchItemClick(position);
//                    holder.binding.llBottom.setBackground(context.getResources().getDrawable(R.drawable.eight_dp_corner_box_two_dp_thik));
                holder.binding.llBottom.setAlpha(1.0f);
                holder.binding.imgEdit.setClickable(true);
                holder.binding.imgDelete.setClickable(true);
                sendStatus(new SessionManager(context).getUserId(),"hospital_services",service.getId(),"1");
            }

        });

        holder.binding.imgEdit.setOnClickListener(v -> {

            addButtonClick.onAddButtonClick(position);
           // Toast.makeText(context, "Edit Clicked", Toast.LENGTH_SHORT).show();

        });

        if (service.getStatus().equals("1")){
            holder.binding.switchOnOff.setChecked(true);
            holder.binding.llBottom.setAlpha(1.0f);
            holder.binding.imgEdit.setClickable(true);
            holder.binding.imgDelete.setClickable(true);
        }else {
            holder.binding.switchOnOff.setChecked(false);
            holder.binding.llBottom.setAlpha(0.25f);
            holder.binding.imgEdit.setClickable(false);
            holder.binding.imgDelete.setClickable(false);
        }
    }

    @Override
    public int getItemCount() {
        return (serviceList != null) ? serviceList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RawLawyerAddServicesBinding binding;

        public ViewHolder(@NonNull RawLawyerAddServicesBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    private void delete(String userId, String type, String id) {

        pd = new ProgressDialog(context);
        pd.setMessage("Please Wait....");
        pd.setCancelable(false);
        pd.show();

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseLawyerDelete> call = api.sendLawyerDeleteDataDoctor("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId, type, id);
        call.enqueue(new Callback<ResponseLawyerDelete>() {
            @Override
            public void onResponse(Call<ResponseLawyerDelete> call, Response<ResponseLawyerDelete> response) {

                Log.e("responseDelete", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        serviceList.remove(remove);
                        updateList(serviceList);
                    }

                } else {
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseLawyerDelete> call, Throwable t) {

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                t.printStackTrace();
                Log.e("errorDelete", t.getMessage());
            }
        });

    }

    private void updateList(List<Service> serviceList) {
        this.serviceList=serviceList;
        notifyDataSetChanged();
    }

    public void sendStatus(String userId,String type,String id,String status){

        /*pd = new ProgressDialog(context);
        pd.setMessage("Please Wait....");
        pd.setCancelable(false);
        pd.show();*/

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseStatus> call = api.sendLawyerStatus("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId, type, id,status);
        call.enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {

                Log.e("responseStatus", new Gson().toJson(response.body()));

                /*if (pd.isShowing()) {
                    pd.dismiss();
                }*/
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } /*else {
                        //Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }*/

                } else {
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();


                }

            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {

                /*if (pd.isShowing()) {
                    pd.dismiss();
                }*/
                t.printStackTrace();
                Log.e("errorStatus", t.getMessage());
            }
        });
    }

    public interface OnItemClickListner{
        public void onAddButtonClick(int postion);
    }
}
