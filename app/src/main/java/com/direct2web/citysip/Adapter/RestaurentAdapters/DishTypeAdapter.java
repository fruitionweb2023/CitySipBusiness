package com.direct2web.citysip.Adapter.RestaurentAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.direct2web.citysip.Activities.Restaurent.SetUpMenuActivity;
import com.direct2web.citysip.Model.RestaurentModels.Cuisines.DishType;
import com.direct2web.citysip.R;
import com.direct2web.citysip.databinding.RawDishTypeRclrBinding;

import java.util.List;

public class DishTypeAdapter extends RecyclerView.Adapter<DishTypeAdapter.ViewHolder> {

    Context context;
    List<DishType> clist;
    boolean first = true;
    int index = 0;

    public DishTypeAdapter(Context context, List<DishType> clist) {
        this.context = context;
        this.clist = clist;
    }


    @NonNull
    @Override
    public DishTypeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        RawDishTypeRclrBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_dish_type_rclr, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DishTypeAdapter.ViewHolder viewHolder, int i) {


        DishType c = clist.get(i);

        viewHolder.mbinding.txtId.setText(c.getId());
        viewHolder.mbinding.txtName.setText(c.getName());

        index = viewHolder.getAbsoluteAdapterPosition();


        if (index == Integer.parseInt(SetUpMenuActivity.catpos)) {

            viewHolder.mbinding.txtName.setBackground(context.getResources().getDrawable(R.drawable.thirteen_round_box_red_fill));
            viewHolder.mbinding.txtName.setTextColor(context.getResources().getColor(R.color.white));

        } else {

            viewHolder.mbinding.txtName.setBackground(context.getResources().getDrawable(R.drawable.thirteen_round_box_white_fill_with_border));
            viewHolder.mbinding.txtName.setTextColor(context.getResources().getColor(R.color.clr_124a00));

        }


    }

    @Override
    public int getItemCount() {
        return clist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final RawDishTypeRclrBinding mbinding;

        public ViewHolder(RawDishTypeRclrBinding itemView) {
            super(itemView.getRoot());
            this.mbinding = itemView;
        }
    }

}
