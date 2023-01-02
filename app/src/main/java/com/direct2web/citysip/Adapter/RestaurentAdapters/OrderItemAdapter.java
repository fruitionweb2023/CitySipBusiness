package com.direct2web.citysip.Adapter.RestaurentAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.direct2web.citysip.Model.RestaurentModels.OrderList.OrderItem;
import com.direct2web.citysip.R;
import com.direct2web.citysip.databinding.RawOrderItemBinding;

import java.util.ArrayList;
import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.viewHolder> {


    List<OrderItem> orderList;
    Context context;

    List<OrderItem> orderItemList = new ArrayList<>();

    public OrderItemAdapter(List<OrderItem> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderItemAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RawOrderItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_order_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemAdapter.viewHolder holder, int position) {

        OrderItem order = orderList.get(position);

        holder.binding.txtName.setText(order.getTitle());
        holder.binding.txtQty.setText(" (" + order.getQty() + ")");
       // holder.binding.txtPrice.setText(order.getAmount());



    }

    @Override
    public int getItemCount() {
        return (orderList != null) ? orderList.size() : 0;
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        RawOrderItemBinding binding;

        public viewHolder(@NonNull RawOrderItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
