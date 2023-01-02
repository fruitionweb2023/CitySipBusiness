package com.direct2web.citysip.Adapter.RestaurentAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.direct2web.citysip.Model.RestaurentModels.Cuisines.DishTitle;
import com.direct2web.citysip.R;
import com.direct2web.citysip.databinding.RawSelectedDishFromOfferBinding;

import java.util.ArrayList;
import java.util.List;

public class OfferDishNameListAdapter extends RecyclerView.Adapter<OfferDishNameListAdapter.CategoryViewHolder> {

    Context context;
    List<DishTitle> categoryList;
    onCanceDishTitleItem canceDishTitleItem;

    public OfferDishNameListAdapter(Context context, List<DishTitle> categoryList,onCanceDishTitleItem canceDishTitleItem) {
        this.context = context;
        this.categoryList = categoryList;
        this.canceDishTitleItem=canceDishTitleItem;
    }

    @NonNull
    @Override
    public OfferDishNameListAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RawSelectedDishFromOfferBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_selected_dish_from_offer, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferDishNameListAdapter.CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {

        DishTitle cl = categoryList.get(position);

        holder.binding.txtName.setText(cl.getName());

        holder.binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                canceDishTitleItem.onDishTitleItemCancel(categoryList.get(position));

            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void updateDataList(ArrayList<DishTitle> categoryList) {

        this.categoryList = categoryList;
        notifyDataSetChanged();

    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        RawSelectedDishFromOfferBinding binding;

        public CategoryViewHolder(@NonNull RawSelectedDishFromOfferBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public interface onCanceDishTitleItem {
        public void onDishTitleItemCancel(DishTitle d);
    }
}
