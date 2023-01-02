package com.direct2web.citysip.Adapter.RestaurentAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.direct2web.citysip.Model.RestaurentModels.ImageVideo.Image;
import com.direct2web.citysip.R;
import com.direct2web.citysip.databinding.PropertyImageListBinding;

import java.util.List;

public class DeleteImageAdapter extends RecyclerView.Adapter<DeleteImageAdapter.ViewHolder> {

    Context context;
    List<Image> stringArrayList;

    public DeleteImageAdapter(Context context, List<Image> stringArrayList) {
        this.context = context;
        this.stringArrayList = stringArrayList;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        PropertyImageListBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.property_image_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Image images = stringArrayList.get(position);

        Glide.with(context)
                .load(stringArrayList.get(position).getPhoto())
                .error(R.drawable.ic_group_1755)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.binding.image);

        holder.binding.relativeLayout.setAlpha(0.50f);
        holder.binding.imgSelect.setVisibility(View.VISIBLE);


        holder.binding.imgSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // images.setSelected(images.setSelected(true));
                holder.binding.relativeLayout.setAlpha(1f);
                Glide.with(context)
                        .load(context.getResources().getDrawable(R.drawable.ic_selected_images))
                        .error(R.drawable.ic_group_1755)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(holder.binding.imgSelect);
                images.setSelected(true);
            }
        });
        holder.binding.imgSelect.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.binding.relativeLayout.setAlpha(0.50f);
                Glide.with(context)
                        .load(context.getResources().getDrawable(R.drawable.ic_non_select))
                        .error(R.drawable.ic_group_1755)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(holder.binding.imgSelect);
                images.setSelected(false);
                return  true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return (stringArrayList != null) ? stringArrayList.size() : 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        PropertyImageListBinding binding;

        public ViewHolder(@NonNull PropertyImageListBinding itemView) {
            super(itemView.getRoot());
           this.binding = itemView;
        }
    }
}