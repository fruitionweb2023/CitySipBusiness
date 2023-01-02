package com.direct2web.citysip.Adapter.RestaurentAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.direct2web.citysip.Model.RestaurentModels.MenuList.Menu;
import com.direct2web.citysip.Model.RestaurentModels.MenuList.NewMenu;
import com.direct2web.citysip.R;
import com.direct2web.citysip.databinding.RawNewMenuListBinding;

import java.util.ArrayList;
import java.util.List;

public class NewMenuListAdapter extends RecyclerView.Adapter<NewMenuListAdapter.viewHolder> {


    List<NewMenu> newMenuList;
    Context context;

    List<Menu> menuList = new ArrayList<>();

    public NewMenuListAdapter(List<NewMenu> newMenuList, Context context) {
        this.newMenuList = newMenuList;
        this.context = context;
    }

    @NonNull
    @Override
    public NewMenuListAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RawNewMenuListBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_new_menu_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewMenuListAdapter.viewHolder holder, int position) {

        NewMenu newMenu = newMenuList.get(position);

        holder.binding.txtTittle.setText(newMenu.getDishTypeTitle());

        menuList = newMenu.getMenuItem();

        NewMenuListitemAdapter adapter = new NewMenuListitemAdapter(menuList,context);

        LinearLayoutManager manager = new LinearLayoutManager(context);
        holder.binding.rclrMenuItem.setLayoutManager(manager);
        holder.binding.rclrMenuItem.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return (newMenuList != null) ? newMenuList.size() : 0;
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        RawNewMenuListBinding binding;

        public viewHolder(@NonNull RawNewMenuListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
