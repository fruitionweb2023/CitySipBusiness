package com.direct2web.citysip.Adapter.LawyerAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.direct2web.citysip.Model.LawyerModels.LawyerAppointment.OrderItem;
import com.direct2web.citysip.R;
import com.direct2web.citysip.databinding.RawDoctorServiceItemBinding;
import com.direct2web.citysip.databinding.RawLawyerServiceItemBinding;

import java.util.ArrayList;
import java.util.List;

public class ServiceItemAdapterLawyer extends RecyclerView.Adapter<ServiceItemAdapterLawyer.viewHolder> {


    List<OrderItem> orderList;
    Context context;

    List<OrderItem> orderItemList = new ArrayList<>();

    public ServiceItemAdapterLawyer(List<OrderItem> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public ServiceItemAdapterLawyer.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RawLawyerServiceItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_lawyer_service_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceItemAdapterLawyer.viewHolder holder, int position) {

        OrderItem order = orderList.get(position);

        holder.binding.txtName.setText(order.getServiceName());
        holder.binding.txtQnty.setText(order.getQty());
        holder.binding.txtAmount.setText(order.getAmount());
        holder.binding.txtAppointmentDate.setText(order.getAppointmentDate());
        holder.binding.txtAppointmentTime.setText(order.getAppointmentTime());

    }

    @Override
    public int getItemCount() {
        return (orderList != null) ? orderList.size() : 0;
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        RawLawyerServiceItemBinding binding;

        public viewHolder(@NonNull RawLawyerServiceItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
