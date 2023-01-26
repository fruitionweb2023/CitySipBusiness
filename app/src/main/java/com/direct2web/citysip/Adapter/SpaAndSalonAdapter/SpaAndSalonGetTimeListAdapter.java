package com.direct2web.citysip.Adapter.SpaAndSalonAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.direct2web.citysip.Adapter.RestaurentAdapters.GetTimingItemAdapter;
import com.direct2web.citysip.Model.RestaurentModels.Timming.AddSpecialTime;
import com.direct2web.citysip.Model.RestaurentModels.Timming.Timing;
import com.direct2web.citysip.R;
import com.direct2web.citysip.databinding.RawDoctorGetTimeListBinding;
import com.direct2web.citysip.databinding.RawSpaAndSalonGetTimeListBinding;

import java.util.ArrayList;
import java.util.List;

public class SpaAndSalonGetTimeListAdapter extends RecyclerView.Adapter<SpaAndSalonGetTimeListAdapter.viewHolder> {


    List<Timing> orderList;
    Context context;

    List<AddSpecialTime> orderItemList = new ArrayList<>();
    onClickSwitch onClickSwitch;
    onClickSwitchOff onClickSwitchOff;

    public SpaAndSalonGetTimeListAdapter(List<Timing> orderList, Context context, onClickSwitch onClickSwitch, onClickSwitchOff onClickSwitchOff) {
        this.orderList = orderList;
        this.context = context;
        this.onClickSwitch=onClickSwitch;
        this.onClickSwitchOff = onClickSwitchOff;
    }

    @NonNull
    @Override
    public SpaAndSalonGetTimeListAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RawSpaAndSalonGetTimeListBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_spa_and_salon_get_time_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SpaAndSalonGetTimeListAdapter.viewHolder holder, @SuppressLint("RecyclerView") int position) {

        Timing order = orderList.get(position);

        holder.binding.txtTitle.setText(order.getDay());
        holder.binding.txtRestTime.setText(order.getDay_time());


        orderItemList = order.getAddSpecialTime();

        GetTimingItemAdapter orderItemAdapter = new GetTimingItemAdapter(orderItemList,context);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        holder.binding.rclrSpecialTime.setLayoutManager(manager);
        holder.binding.rclrSpecialTime.setAdapter(orderItemAdapter);


        holder.binding.switchTiming.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!isChecked) {

                    onClickSwitchOff.onSwitchItemClickOff(position);
                    holder.binding.llBottom.setAlpha(0.25f);
                    holder.binding.imgEdit.setClickable(false);

                } else {

                    onClickSwitch.onSwitchItemClick(position);
//                    holder.binding.llBottom.setBackground(context.getResources().getDrawable(R.drawable.eight_dp_corner_box_two_dp_thik));
                    holder.binding.llBottom.setAlpha(1.0f);
                    holder.binding.imgEdit.setClickable(true);
                }

            }
        });
        if (order.getStatus().equals("1")){

            holder.binding.switchTiming.setChecked(true);
            holder.binding.llBottom.setAlpha(1.0f);
            holder.binding.imgEdit.setClickable(true);

        }else {

            holder.binding.switchTiming.setChecked(false);
            holder.binding.llBottom.setAlpha(0.25f);
            holder.binding.imgEdit.setClickable(false);

        }
        
        holder.binding.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Edit Clicked", Toast.LENGTH_SHORT).show();
                
            }
        });



    }

    @Override
    public int getItemCount() {
        return (orderList != null) ? orderList.size() : 0;
    }

    public interface onClickSwitch {
        public void onSwitchItemClick(int position);
    }

    public interface onClickSwitchOff {
        public void onSwitchItemClickOff(int position);
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        RawSpaAndSalonGetTimeListBinding binding;

        public viewHolder(@NonNull RawSpaAndSalonGetTimeListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }



}
