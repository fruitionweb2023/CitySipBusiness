package com.direct2web.citysip.Adapter.RestaurentAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.direct2web.citysip.Model.RestaurentModels.CustomerList.Customer;
import com.direct2web.citysip.R;
import com.direct2web.citysip.databinding.RawCustomerListDetailsBinding;

import java.util.List;

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.viewHolder> {


    List<Customer> customerList;
    Context context;

    public CustomerListAdapter(List<Customer> customerList, Context context) {
        this.customerList = customerList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomerListAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RawCustomerListDetailsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_customer_list_details, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerListAdapter.viewHolder holder, int position) {

        Customer customer = customerList.get(position);

        holder.binding.txtName.setText(customer.getName());
        holder.binding.txtEmail.setText(customer.getEmail());
        holder.binding.txtNumber.setText(customer.getMobile());

        /*Glide.with(context).load(Api.url + customer.getProfile())
                .thumbnail(0.5f)
                .error(R.drawable.city_sip_logo)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.binding.imgProfilePic);*/


    }

    @Override
    public int getItemCount() {
        return (customerList != null) ? customerList.size() : 0;
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        RawCustomerListDetailsBinding binding;

        public viewHolder(@NonNull RawCustomerListDetailsBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
