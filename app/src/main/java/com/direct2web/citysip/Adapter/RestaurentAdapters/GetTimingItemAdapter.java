package com.direct2web.citysip.Adapter.RestaurentAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.direct2web.citysip.Model.RestaurentModels.OrderList.OrderItem;
import com.direct2web.citysip.Model.RestaurentModels.Timming.AddSpecialTime;
import com.direct2web.citysip.R;
import com.direct2web.citysip.databinding.RawTimingAddBinding;

import java.util.ArrayList;
import java.util.List;

public class GetTimingItemAdapter extends RecyclerView.Adapter<GetTimingItemAdapter.viewHolder> {


    List<AddSpecialTime> orderList;
    Context context;

    List<OrderItem> orderItemList = new ArrayList<>();

    int id = 1;
    int id2 = 0;

    public GetTimingItemAdapter(List<AddSpecialTime> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public GetTimingItemAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RawTimingAddBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_timing_add, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GetTimingItemAdapter.viewHolder holder, int position) {

        AddSpecialTime order = orderList.get(position);

        holder.binding.imgDelete.setVisibility(View.GONE);

        /*id2 += id;

        holder.binding.txtId.setText(String.valueOf(id2));*/
        holder.binding.llMain .setVisibility(View.GONE);

        holder.binding.txtTime.setText(order.getTime());
        holder.binding.txtTitle.setText(order.getTitle());



    }

    @Override
    public int getItemCount() {
        return (orderList != null) ? orderList.size() : 0;
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        RawTimingAddBinding binding;

        public viewHolder(@NonNull RawTimingAddBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
