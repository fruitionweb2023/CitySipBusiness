package com.direct2web.citysip.Adapter.SpaAndSalonAdapter;

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
import com.direct2web.citysip.Activities.SpaAndSalon.SpaAndSalonDeleteImageActivity;
import com.direct2web.citysip.Model.SpaAndSalon.MediaLibrary.Image;
import com.direct2web.citysip.R;
import com.direct2web.citysip.databinding.PropertyImageListBinding;

import java.util.List;

public class DeleteImageSpaAndSalonAdapter extends RecyclerView.Adapter<DeleteImageSpaAndSalonAdapter.ViewHolder> {

    List<Image> imageList;
    Context context;

    public DeleteImageSpaAndSalonAdapter(List<Image> imageList, Context context) {
       /* this.imageList = imageList;
        this.context = context;*/
    }

    public DeleteImageSpaAndSalonAdapter(List<Image> imageList, SpaAndSalonDeleteImageActivity context) {
        this.imageList = imageList;
        this.context = context;
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
        Image images = imageList.get(position);

        Glide.with(context)
                .load(imageList.get(position).getPhoto())
                .error(R.drawable.ic_group_1755)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.binding.image);

        holder.binding.relativeLayout.setAlpha(0.50f);
        holder.binding.imgSelect.setVisibility(View.VISIBLE);


        holder.binding.imgSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        return (imageList != null) ? imageList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        PropertyImageListBinding binding;

        public ViewHolder(@NonNull PropertyImageListBinding itemView) {
            super(itemView.getRoot());
           this.binding = itemView;
        }
    }
}