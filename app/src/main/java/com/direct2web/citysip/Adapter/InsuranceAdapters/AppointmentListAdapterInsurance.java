package com.direct2web.citysip.Adapter.InsuranceAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.direct2web.citysip.Model.InsuranceModel.InsuranceAppointment.Order;
import com.direct2web.citysip.Model.InsuranceModel.InsuranceAppointment.OrderItem;
import com.direct2web.citysip.R;
import com.direct2web.citysip.databinding.RawInsuranceAppointmentListBinding;
import com.direct2web.citysip.databinding.RawLawyerAppointmentListBinding;

import java.util.ArrayList;
import java.util.List;

public class AppointmentListAdapterInsurance extends RecyclerView.Adapter<AppointmentListAdapterInsurance.viewHolder> {


    List<Order> orderList;
    Context context;

    List<OrderItem> orderItemList = new ArrayList<>();

    public AppointmentListAdapterInsurance(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public AppointmentListAdapterInsurance.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RawInsuranceAppointmentListBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_insurance_appointment_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentListAdapterInsurance.viewHolder holder, int position) {

        Order order = orderList.get(position);

        holder.binding.txtOrderId.setText(order.getId());
        holder.binding.txtCustomerName.setText(order.getCustomerName());
        holder.binding.txtDate.setText(order.getOrderDate());
        holder.binding.txtTime.setText(order.getOrderTime());
        holder.binding.txtPrice.setText(order.getTotalAmount());
       // holder.binding.txtAppointmentTime.setText(order.getAppointmentTime());

        orderItemList = order.getOrderItem();

        ServiceItemAdapterInsurance serviceItemAdapter = new ServiceItemAdapterInsurance(orderItemList,context);
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

        RawInsuranceAppointmentListBinding binding;

        public viewHolder(@NonNull RawInsuranceAppointmentListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
