package com.direct2web.citysip.Adapter.RestaurentAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.direct2web.citysip.Model.RestaurentModels.ImageVideo.Image;
import com.direct2web.citysip.R;

import java.util.List;

public class SetImageAdapter extends RecyclerView.Adapter<SetImageAdapter.ViewHolder> {

    Context context;
    List<Image> stringArrayList;
    OnItemClickListner addButtonClick;

    public SetImageAdapter(Context context, List<Image> stringArrayList,OnItemClickListner addButtonClick ) {
        this.context = context;
        this.stringArrayList = stringArrayList;
        this.addButtonClick = addButtonClick;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == stringArrayList.size()) ? R.layout.property_add_image : R.layout.property_image_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView;
        if (viewType == R.layout.property_add_image) {
            itemView = LayoutInflater.from(context).inflate(R.layout.property_add_image,viewGroup,false);
        } else {
            itemView = LayoutInflater.from(context).inflate(R.layout.property_image_list,viewGroup,false);
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        if (position == stringArrayList.size()) {
            holder.btnAddImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addButtonClick.onAddButtonClick(position);
                }

            });
        } else {
            Glide.with(context)
                    .load(stringArrayList.get(position).getPhoto())
                    .error(R.drawable.ic_group_1755)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.image);

        }
    }


    @Override
    public int getItemCount() {
        return stringArrayList.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        ImageView btnAddImage;
        RelativeLayout relativeLayout;
        View view;

        public ViewHolder(View itemView ) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            btnAddImage = (ImageView) itemView.findViewById(R.id.btn_addItem);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);


        }


    }

    public interface OnItemClickListner{
        public void onAddButtonClick(int postion);

    }

}