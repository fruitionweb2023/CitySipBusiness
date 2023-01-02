package com.direct2web.citysip.Adapter.RestaurentAdapters;

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
import com.direct2web.citysip.Activities.CommonActivities.VerificationActivity;
import com.direct2web.citysip.Model.RestaurentModels.Delete.ResponseDelete;
import com.direct2web.citysip.Model.RestaurentModels.Delete.ResponseStatus;
import com.direct2web.citysip.Model.RestaurentModels.MenuList.Menu;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.RawNewMenuItemBinding;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewMenuListitemAdapter extends RecyclerView.Adapter<NewMenuListitemAdapter.viewHolder> {


    List<Menu> menuList;
    Context context;
    ProgressDialog pd;
    int remove ;

    onClickSwitch onClickSwitch;
    onClickSwitchOff onClickSwitchOff;


    public NewMenuListitemAdapter(List<Menu> menuList, Context context,onClickSwitch onClickSwitch,onClickSwitchOff onClickSwitchOff) {
        this.menuList = menuList;
        this.context = context;
        this.onClickSwitch=onClickSwitch;
        this.onClickSwitchOff=onClickSwitchOff;
    }

    public NewMenuListitemAdapter(List<Menu> menuList, Context context) {
        this.menuList = menuList;
        this.context = context;

        /*pd = new ProgressDialog(context);
        pd.setMessage("Please Wait....");
        pd.setCancelable(false);
        pd.show();*/
    }

    @NonNull
    @Override
    public NewMenuListitemAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RawNewMenuItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_new_menu_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewMenuListitemAdapter.viewHolder holder, @SuppressLint("RecyclerView") int position) {

        Menu menu = menuList.get(position);

        holder.binding.txtDishName.setText(menu.getTitle());
        holder.binding.txtPrice.setText(menu.getAmount());
        holder.binding.txtDiscreption.setText(menu.getDescription());
        holder.binding.txtCuisines.setText(menu.getCuisines());
        holder.binding.txtMaxDish.setText(menu.getMaxDish());
        holder.binding.txtOffer.setText(menu.getOffer());

        if (menu.getOffer().equals("No")) {
            holder.binding.txtOffer.setTextColor(context.getResources().getColor(R.color.red));
        }

        if (menu.getCategory() != null) {

            if (menu.getCategory().equals("Veg")) {

                Glide.with(context).load(context.getResources().getDrawable(R.drawable.ic_group_1723)).into(holder.binding.imgVegNonVeg);

            } else {

                Glide.with(context).load(context.getResources().getDrawable(R.drawable.ic_group_1724)).into(holder.binding.imgVegNonVeg);

            }
        }



        Glide.with(context).load(Api.imageUrl+menu.getImage())
                .error(context.getDrawable(R.drawable.city_sip_logo))
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate()
                .into(holder.binding.imgDishLogo);

        holder.binding.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure want to delete ?");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        remove = position;
                        delete(new SessionManager(context).getUserId(), "menu", menu.getId());
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
                    sendStatus(new SessionManager(context).getUserId(),"menu",menu.getId(),"0");

                } else {

//                    onClickSwitch.onSwitchItemClick(position);
//                    holder.binding.llBottom.setBackground(context.getResources().getDrawable(R.drawable.eight_dp_corner_box_two_dp_thik));
                    holder.binding.llBottom.setAlpha(1.0f);
                    holder.binding.imgEdit.setClickable(true);
                    holder.binding.imgDelete.setClickable(true);
                    sendStatus(new SessionManager(context).getUserId(),"menu",menu.getId(),"1");
                }

            }
        });

        holder.binding.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Edit Clicked", Toast.LENGTH_SHORT).show();

            }
        });

        if (menu.getStatus().equals("1")){

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

    private void delete(String userId, String type, String id) {
        pd = new ProgressDialog(context);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();


        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseDelete> call = api.sendDelete("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, "", type, id);
        call.enqueue(new Callback<ResponseDelete>() {
            @Override
            public void onResponse(Call<ResponseDelete> call, Response<ResponseDelete> response) {

                Log.e("responseDelete", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        menuList.remove(remove);
                        updateList(menuList);

                    }

                } else {
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();


                }

            }

            @Override
            public void onFailure(Call<ResponseDelete> call, Throwable t) {

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                t.printStackTrace();
                Log.e("errorDelete", t.getMessage());
            }
        });

    }

    @Override
    public int getItemCount() {
        return (menuList != null) ? menuList.size() : 0;
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        RawNewMenuItemBinding binding;

        public viewHolder(@NonNull RawNewMenuItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public void updateList(List<Menu> menuList){

        this.menuList=menuList;
        notifyDataSetChanged();
    }

    public interface onClickSwitch {
        public void onSwitchItemClick(int position);
    }

    public interface onClickSwitchOff {
        public void onSwitchItemClickOff(int position);
    }

    public void sendStatus(String userId,String type,String id,String status){

        /*pd = new ProgressDialog(context);
        pd.setMessage("Please Wait....");
        pd.setCancelable(false);
        pd.show();*/

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseStatus> call = api.sendStatus("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
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
                Log.e("errorDelete", t.getMessage());
            }
        });
    }
}