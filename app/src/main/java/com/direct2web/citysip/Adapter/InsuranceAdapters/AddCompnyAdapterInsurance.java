package com.direct2web.citysip.Adapter.InsuranceAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.direct2web.citysip.Model.InsuranceModel.InsuranceServices.CompanyList;
import com.direct2web.citysip.Model.RestaurentModels.offer.Offer;
import com.direct2web.citysip.R;

import java.util.ArrayList;
import java.util.List;

public class AddCompnyAdapterInsurance extends RecyclerView.Adapter<AddCompnyAdapterInsurance.ViewHolder> {

    Context context;
    List<CompanyList> stringArrayList;
    OnItemClickListner addButtonClick;
    OnCrossItemClickListner crossItemClickListner;

    public AddCompnyAdapterInsurance(Context context, List<CompanyList> stringArrayList, OnItemClickListner addButtonClick, OnCrossItemClickListner crossItemClickListner) {
        this.context = context;
        this.stringArrayList = stringArrayList;
        this.addButtonClick = addButtonClick;
        this.crossItemClickListner=crossItemClickListner;
    }


    @Override
    public int getItemViewType(int position) {
        return (position == stringArrayList.size()) ? R.layout.raw_offer_add : R.layout.raw_offer_add_coupon;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView;
        if (viewType == R.layout.raw_offer_add) {
            itemView = LayoutInflater.from(context).inflate(R.layout.raw_offer_add,viewGroup,false);

        } else {
            itemView = LayoutInflater.from(context).inflate(R.layout.raw_offer_add_coupon,viewGroup,false);
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        /*if (stringArrayList.size() <= 1){
            holder.btnAddImage.setText("Please Add Offer First");
            holder.btnAddImage.setTextColor(context.getResources().getColor(R.color.clr_f54748));
            holder.btnAddImage.setClickable(false);
            holder.btnAddImage.setEnabled(false);
        }else {
            holder.btnAddImage.setText("Add New");
            holder.btnAddImage.setClickable(true);
            holder.btnAddImage.setEnabled(true);
        }*/

        if (position == stringArrayList.size()) {

                holder.btnAddImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                            addButtonClick.onAddButtonClick(position);

                    }
                });

        } else {

            holder.tittle.setText(stringArrayList.get(position).getName());

//            Glide.with(context)
//                    .load(stringArrayList.get(position).getPhoto())
//                    .error(R.drawable.ic_group_1755)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .into(holder.image);

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    crossItemClickListner.onCrossButtonClick(stringArrayList.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return stringArrayList.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView btnAddImage,tittle;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.img_cross);
            btnAddImage = (TextView) itemView.findViewById(R.id.txt_add_coupons);
            tittle = (TextView) itemView.findViewById(R.id.txt_title_coupons);



        }
    }

    public interface OnItemClickListner{
        public void onAddButtonClick(int postion);

    }

    public void updateDataList(ArrayList<CompanyList> categoryList) {

        this.stringArrayList = categoryList;
        notifyDataSetChanged();

    }

    public interface OnCrossItemClickListner{
        public void onCrossButtonClick(CompanyList offer);

    }
}