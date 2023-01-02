package com.direct2web.citysip.Adapter.RestaurentAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.direct2web.citysip.Model.RestaurentModels.MenuList.Menu;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.databinding.RawMenuListBinding;

import java.util.List;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.CategoryViewHolder> {

    Context context;
    List<Menu> categoryList;

    public MenuListAdapter(Context context, List<Menu> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public MenuListAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RawMenuListBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_menu_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuListAdapter.CategoryViewHolder holder, int position) {

        Menu cl = categoryList.get(position);

        holder.binding.txtDishName.setText(cl.getTitle());
        holder.binding.txtCuisines.setText(cl.getCuisines());
        holder.binding.txtDishType.setText(cl.getDishType());
        holder.binding.txtDishLimit.setText(cl.getMaxDish());
        holder.binding.txtAmount.setText(cl.getAmount());

        if (cl.getCategory().equals("Veg")){

            holder.binding.txtVeg.setBackground(context.getResources().getDrawable(R.drawable.four_dp_corner_box_green_fill));
            holder.binding.txtNonVeg.setBackground(context.getResources().getDrawable(R.drawable.four_dp_corner_box_grey));
            holder.binding.txtVeg.setTextColor(context.getResources().getColor(R.color.white));


        }else {

            holder.binding.txtVeg.setBackground(context.getResources().getDrawable(R.drawable.four_dp_corner_box_grey));
            holder.binding.txtNonVeg.setBackground(context.getResources().getDrawable(R.drawable.four_dp_corner_box_green_fill));
            holder.binding.txtNonVeg.setTextColor(context.getResources().getColor(R.color.white));

        }

        holder.binding.edtDescription.setText(cl.getDescription());

        Glide.with(context).load(Api.url + cl.getImage())
                .thumbnail(0.5f)
                .error(R.drawable.city_sip_logo)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.binding.imgDishLogo);


    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        RawMenuListBinding binding;

        public CategoryViewHolder(@NonNull RawMenuListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
