package com.direct2web.citysip.Adapter.DoctorAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.direct2web.citysip.Model.DoctorModels.DoctorAppointment.Order;
import com.direct2web.citysip.Model.DoctorModels.DoctorAppointment.OrderItem;
import com.direct2web.citysip.Model.DoctorModels.DoctorAppointment.ResponseDoctorAppointment;
import com.direct2web.citysip.R;
import com.direct2web.citysip.databinding.RawDoctorAppointmentListBinding;

import java.util.ArrayList;
import java.util.List;

public class AppointmentListAdapter extends RecyclerView.Adapter<AppointmentListAdapter.viewHolder> {


    List<Order> orderList;
    Context context;

    List<OrderItem> orderItemList = new ArrayList<>();

    public AppointmentListAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public AppointmentListAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RawDoctorAppointmentListBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_doctor_appointment_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentListAdapter.viewHolder holder, int position) {

        Order order = orderList.get(position);

        holder.binding.txtOrderId.setText(order.getId());
        holder.binding.txtCustomerName.setText(order.getCustomerName());
        holder.binding.txtDate.setText(order.getOrderDate());
        holder.binding.txtTime.setText(order.getOrderTime());
        holder.binding.txtPrice.setText(order.getTotalAmount());
        holder.binding.txtAppointmentTime.setText(order.getAppointmentTime());

        orderItemList = order.getOrderItem();

        ServiceItemAdapter serviceItemAdapter = new ServiceItemAdapter(orderItemList,context);
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

        RawDoctorAppointmentListBinding binding;

        public viewHolder(@NonNull RawDoctorAppointmentListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
