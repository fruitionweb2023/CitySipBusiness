package com.direct2web.citysip.Adapter.DoctorAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.direct2web.citysip.Model.DoctorModels.DoctorAppointment.OrderItem;
import com.direct2web.citysip.R;
import com.direct2web.citysip.databinding.RawDoctorServiceItemBinding;

import java.util.ArrayList;
import java.util.List;

public class ServiceItemAdapter extends RecyclerView.Adapter<ServiceItemAdapter.viewHolder> {


    List<OrderItem> orderList;
    Context context;

    List<OrderItem> orderItemList = new ArrayList<>();

    public ServiceItemAdapter(List<OrderItem> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public ServiceItemAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RawDoctorServiceItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_doctor_service_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceItemAdapter.viewHolder holder, int position) {

        OrderItem order = orderList.get(position);

        holder.binding.txtName.setText(order.getServiceName());
        holder.binding.txtQty.setText(order.getQty());

    }

    @Override
    public int getItemCount() {
        return (orderList != null) ? orderList.size() : 0;
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        RawDoctorServiceItemBinding binding;

        public viewHolder(@NonNull RawDoctorServiceItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
