package com.direct2web.citysip.Adapter.RestaurentAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.direct2web.citysip.Model.RestaurentModels.Timming.HourTitle;
import com.direct2web.citysip.R;

import java.util.List;


public class SelectTimmingTitleAdapter extends RecyclerView.Adapter<SelectTimmingTitleAdapter.FruitViewHolder> {

    List<HourTitle> mDataset;
    RecyclerViewItemClickListener recyclerViewItemClickListener;

    public SelectTimmingTitleAdapter(List<HourTitle> myDataset, RecyclerViewItemClickListener listener) {
        mDataset = myDataset;
        this.recyclerViewItemClickListener = listener;
    }

    @NonNull
    @Override
    public FruitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_recycler_view, parent, false);

        FruitViewHolder vh = new FruitViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull FruitViewHolder fruitViewHolder, int i) {


        fruitViewHolder.mTextView.setText(mDataset.get(i).getTitle());

    }

    @Override
    public int getItemCount() {
        return (mDataset == null) ? 0 : mDataset.size();
    }


    public interface RecyclerViewItemClickListener {
        void clickOnSelectHoursTitleItem(String data,String id);
    }

    public class FruitViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextView, mId;

        public FruitViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.tvName);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerViewItemClickListener.clickOnSelectHoursTitleItem(mDataset.get(getAdapterPosition()).getTitle(),mDataset.get(getAdapterPosition()).getId());

        }
    }
}