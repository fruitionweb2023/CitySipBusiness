package com.direct2web.citysip.Adapter.InsuranceAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.direct2web.citysip.Model.RestaurentModels.Timming.AddSpecialTime;
import com.direct2web.citysip.R;
import com.direct2web.citysip.databinding.RawDoctorTimingAddBinding;
import com.direct2web.citysip.databinding.RawInsuranceTimingAddBinding;

import java.util.ArrayList;

public class InsuranceAddSpecialTimeAdapter extends RecyclerView.Adapter<InsuranceAddSpecialTimeAdapter.CategoryViewHolder> {

    Context context;
    ArrayList<AddSpecialTime> categoryList;
    int id = 1;
    int id2 = 0;
    onCanceDishTypellItem cancelItem;

    public InsuranceAddSpecialTimeAdapter(Context context, ArrayList<AddSpecialTime> categoryList, onCanceDishTypellItem cancelItem) {
        this.context = context;
        this.categoryList = categoryList;
        this.cancelItem = cancelItem;

    }

    @NonNull
    @Override
    public InsuranceAddSpecialTimeAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RawInsuranceTimingAddBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_insurance_timing_add, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull InsuranceAddSpecialTimeAdapter.CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {

        AddSpecialTime cl = categoryList.get(position);

        id2 += id;

        holder.binding.txtId.setText(String.valueOf(id2));

//        holder.binding.txtTitle.setText(cl.getTitle());

        holder.binding.txtTime.setText(cl.getTime());

        holder.binding.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id2 = 0;
                cancelItem.onDishTypeItemCancel(categoryList.get(position));

            }
        });

    }

    @Override
    public int getItemCount() {
        return (categoryList == null) ? 0 : categoryList.size();
    }

    public void updateDataList(ArrayList<AddSpecialTime> categoryList) {

        this.categoryList = categoryList;
        notifyDataSetChanged();

    }

    public interface onCanceDishTypellItem {
        public void onDishTypeItemCancel(AddSpecialTime d);
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        RawInsuranceTimingAddBinding binding;

        public CategoryViewHolder(@NonNull RawInsuranceTimingAddBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
