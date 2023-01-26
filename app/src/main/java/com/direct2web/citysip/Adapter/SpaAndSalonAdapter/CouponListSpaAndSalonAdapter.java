package com.direct2web.citysip.Adapter.SpaAndSalonAdapter;

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

import com.direct2web.citysip.Model.RestaurentModels.Delete.ResponseStatus;
import com.direct2web.citysip.Model.SpaAndSalon.CouponsOffer.Offer;
import com.direct2web.citysip.Model.SpaAndSalon.DeleteData.ResponseSpaAndSalonDelete;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.RawSpaSalonCouponsListItemBinding;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CouponListSpaAndSalonAdapter extends RecyclerView.Adapter<CouponListSpaAndSalonAdapter.ViewHolder> {

    List<Offer> offerList;
    Context context;
    ProgressDialog pd;
    int remove ;
    OnItemClickListner addButtonClick;

    public CouponListSpaAndSalonAdapter(List<Offer> offerList, Context context, OnItemClickListner addButtonClick ) {
        this.offerList = offerList;
        this.context = context;
        this.addButtonClick = addButtonClick;
    }

    @NonNull
    @Override
    public CouponListSpaAndSalonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RawSpaSalonCouponsListItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_spa_salon_coupons_list_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Offer offer = offerList.get(position);

        holder.binding.txtCouponsCode.setText(offer.getCoupnCode());
        holder.binding.txtOffer.setText(offer.getPercentage()+"%");
        holder.binding.txtMaxAmount.setText(offer.getMaxAmount());
        holder.binding.txtMinAmount.setText(offer.getMinAmount());
        holder.binding.txtTerms.setText(offer.getTermsCondition());

        holder.binding.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure want to delete ?");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        remove = position;
                        delete(new SessionManager(context).getUserId(), "offer", offer.getId());
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        holder.binding.switchOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!isChecked) {

//                    onClickSwitchOff.onSwitchItemClickOff(position);
                    holder.binding.llBottom.setAlpha(0.25f);
                    holder.binding.imgEdit.setClickable(false);
                    holder.binding.imgDelete.setClickable(false);
                    sendStatus(new SessionManager(context).getUserId(),"offer",offer.getId(),"0");

                } else {

                    holder.binding.llBottom.setAlpha(1.0f);
                    holder.binding.imgEdit.setClickable(true);
                    holder.binding.imgDelete.setClickable(true);
                    sendStatus(new SessionManager(context).getUserId(),"offer",offer.getId(),"1");
                }

            }
        });

        holder.binding.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addButtonClick.onAddButtonClick(position);

            }
        });

        if (offer.getStatus().equals("1")){
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
        return (offerList != null) ? offerList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RawSpaSalonCouponsListItemBinding binding;

        public ViewHolder(@NonNull RawSpaSalonCouponsListItemBinding itemView) {
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
        Call<ResponseSpaAndSalonDelete> call = api.sendSpaAndSalonDeleteData("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId, type, id);
        call.enqueue(new Callback<ResponseSpaAndSalonDelete>() {
            @Override
            public void onResponse(Call<ResponseSpaAndSalonDelete> call, Response<ResponseSpaAndSalonDelete> response) {

                Log.e("responseDelete", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        offerList.remove(remove);
                        updateList(offerList);
                    }

                } else {
                    assert response.body() != null;
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseSpaAndSalonDelete> call, Throwable t) {

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                t.printStackTrace();
                Log.e("errorDelete", t.getMessage());
            }
        });

    }

    private void updateList(List<Offer> offerList) {
        this.offerList=offerList;
        notifyDataSetChanged();
    }

    public void sendStatus(String userId,String type,String id,String status){

        /*pd = new ProgressDialog(context);
        pd.setMessage("Please Wait....");
        pd.setCancelable(false);
        pd.show();*/

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseStatus> call = api.sendSpaAndSalonStatus("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
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
