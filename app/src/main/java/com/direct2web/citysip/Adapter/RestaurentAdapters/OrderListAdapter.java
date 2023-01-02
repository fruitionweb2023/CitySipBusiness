package com.direct2web.citysip.Adapter.RestaurentAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.direct2web.citysip.Model.RestaurentModels.Delete.ResponseStatus;
import com.direct2web.citysip.Model.RestaurentModels.OrderList.Order;
import com.direct2web.citysip.Model.RestaurentModels.OrderList.OrderItem;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.RawOrderListDetailsBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.viewHolder> {


    List<Order> orderList;
    Context context;
    onClickAccept onClickAccept;
    onClickReject onClickReject;

    List<OrderItem> orderItemList = new ArrayList<>();

    public OrderListAdapter(List<Order> orderList, Context context, onClickAccept onClickAccept,onClickReject onClickReject) {
        this.orderList = orderList;
        this.context = context;
        this.onClickAccept=onClickAccept;
        this.onClickReject = onClickReject;
    }

    @NonNull
    @Override
    public OrderListAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RawOrderListDetailsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_order_list_details, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListAdapter.viewHolder holder, @SuppressLint("RecyclerView") int position) {

        Order order = orderList.get(position);

        holder.binding.txtOrderId.setText(order.getId());
        holder.binding.txtCustomerName.setText(order.getCustomerName());
        holder.binding.txtDate.setText(order.getOrderDate());
        holder.binding.txtTime.setText(order.getOrderTime());
        holder.binding.txtPrice.setText(order.getTotalAmount());

        orderItemList = order.getOrderItem();

        OrderItemAdapter orderItemAdapter = new OrderItemAdapter(orderItemList, context);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        holder.binding.rclrOrderItem.setLayoutManager(manager);
        holder.binding.rclrOrderItem.setAdapter(orderItemAdapter);

        if (order.getStatus().equals("0")) {

            holder.binding.btnAccept.setVisibility(View.VISIBLE);
            holder.binding.imgDelete.setVisibility(View.VISIBLE);

        } else if (order.getStatus().equals("1")) {

            holder.binding.btnAccept.setVisibility(View.GONE);
            holder.binding.imgDelete.setVisibility(View.VISIBLE);

        } else {

            holder.binding.btnAccept.setVisibility(View.VISIBLE);
            holder.binding.imgDelete.setVisibility(View.GONE);

        }

        holder.binding.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*
                sendStatus("", "order_list", order.getId(), "2");
*/

                onClickReject.onClickRejectButton(position);
            }
        });

        holder.binding.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                sendStatus("", "order_list", order.getId(), "1");
*/

                onClickAccept.onClickAcceptButton(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return (orderList != null) ? orderList.size() : 0;
    }


    public void updatelist(List<Order> temp) {
        orderList = temp;
        notifyDataSetChanged();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        RawOrderListDetailsBinding binding;

        public viewHolder(@NonNull RawOrderListDetailsBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public void sendStatus(String userId, String type, String id, String status) {

       /* pd = new ProgressDialog(SetUpCouponsActivity.this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(false);
        pd.show();
*/
        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseStatus> call = api.sendStatus("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId, type, id, status);
        call.enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {

                Log.e("responseDelete", new Gson().toJson(response.body()));

               /* if (pd.isShowing()) {
                    pd.dismiss();
                }*/

                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(context, "Successfully Update", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();

                    }

                } else {
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {

               /* if (pd.isShowing()) {
                    pd.dismiss();
                }*/
                t.printStackTrace();
                Log.e("errorDelete", t.getMessage());
            }
        });
    }

    public interface onClickAccept {

        public void onClickAcceptButton(int position);

    }

    public interface onClickReject {

        public void onClickRejectButton(int position);

    }

}
