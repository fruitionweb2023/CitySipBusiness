package com.direct2web.citysip.Adapter.SpaAndSalonAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.direct2web.citysip.Model.SpaAndSalon.Appointment.Order;
import com.direct2web.citysip.Model.SpaAndSalon.Appointment.OrderItem;
import com.direct2web.citysip.R;
import com.direct2web.citysip.databinding.RawDoctorAppointmentListBinding;
import com.direct2web.citysip.databinding.RawSpaSalonAppointmentListBinding;

import java.util.ArrayList;
import java.util.List;

public class AppointmentListSpaAndSalonAdapter extends RecyclerView.Adapter<AppointmentListSpaAndSalonAdapter.viewHolder> {


    List<Order> orderList;
    Context context;

    List<OrderItem> orderItemList = new ArrayList<>();

    public AppointmentListSpaAndSalonAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public AppointmentListSpaAndSalonAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RawSpaSalonAppointmentListBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_spa_salon_appointment_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentListSpaAndSalonAdapter.viewHolder holder, int position) {

        Order order = orderList.get(position);

        holder.binding.txtOrderId.setText(order.getId());
        holder.binding.txtCustomerName.setText(order.getCustomerName());
        holder.binding.txtDate.setText(order.getOrderDate());
        holder.binding.txtTime.setText(order.getOrderTime());
        holder.binding.txtPrice.setText(order.getTotalAmount());
        holder.binding.txtAppointmentTime.setText(order.getAppointmentTime());

        orderItemList = order.getOrderItem();

        ServiceItemSpaAndSalonAdapter serviceItemAdapter = new ServiceItemSpaAndSalonAdapter(orderItemList,context);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        holder.binding.rclrServiceItem.setLayoutManager(manager);
        holder.binding.rclrServiceItem.setAdapter(serviceItemAdapter);

    }

    @Override
    public int getItemCount() {
        return (orderList != null) ? orderList.size() : 0;
    }


    public void updatelist(List<Order> temp) {
        notifyDataSetChanged();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        RawSpaSalonAppointmentListBinding binding;

        public viewHolder(@NonNull RawSpaSalonAppointmentListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
