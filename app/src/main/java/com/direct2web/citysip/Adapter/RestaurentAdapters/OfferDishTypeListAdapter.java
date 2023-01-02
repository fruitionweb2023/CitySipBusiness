package com.direct2web.citysip.Adapter.RestaurentAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.direct2web.citysip.Model.RestaurentModels.Cuisines.DishType;
import com.direct2web.citysip.R;
import com.direct2web.citysip.databinding.RawSelectedDishTypeFromOfferBinding;

import java.util.ArrayList;

public class OfferDishTypeListAdapter extends RecyclerView.Adapter<OfferDishTypeListAdapter.CategoryViewHolder> {

    Context context;
    ArrayList<DishType> categoryList;
    String id;
    onCanceDishTypellItem cancelItem;

    public OfferDishTypeListAdapter(Context context, ArrayList<DishType> categoryList, onCanceDishTypellItem cancelItem) {
        this.context = context;
        this.categoryList = categoryList;
        this.cancelItem = cancelItem;

    }

    @NonNull
    @Override
    public OfferDishTypeListAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RawSelectedDishTypeFromOfferBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_selected_dish_type_from_offer, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferDishTypeListAdapter.CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {

        DishType cl = categoryList.get(position);

        holder.binding.txtName.setText(cl.getName());

        holder.binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancelItem.onDishTypeItemCancel(categoryList.get(position));

            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void updateDataList(ArrayList<DishType> categoryList) {

        this.categoryList = categoryList;
        notifyDataSetChanged();

    }

    public interface onCanceDishTypellItem {
        public void onDishTypeItemCancel(DishType d);
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        RawSelectedDishTypeFromOfferBinding binding;

        public CategoryViewHolder(@NonNull RawSelectedDishTypeFromOfferBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
