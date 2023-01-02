package com.direct2web.citysip.Adapter.RestaurentAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.direct2web.citysip.Model.RestaurentModels.Timming.HourTitle;
import com.direct2web.citysip.R;

import java.util.List;


public class SpinnerTimingTitleListAdapter extends ArrayAdapter<HourTitle> {

    LayoutInflater layoutInflater;

    public SpinnerTimingTitleListAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<HourTitle> cuisines) {
        super(context, resource, textViewResourceId, cuisines);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return rowview(convertView, position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return rowview(convertView, position);
    }

    private View rowview(View convertView, int position) {

        HourTitle rowItem = getItem(position);

        viewHolder holder;
        View rowview = convertView;
        if (rowview == null) {

            holder = new viewHolder();
            layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = layoutInflater.inflate(R.layout.raw_recyclear_view_drop_down_timing, null, false);

            holder.txtTitle = (TextView) rowview.findViewById(R.id.tvName);
            rowview.setTag(holder);
        } else {
            holder = (viewHolder) rowview.getTag();
        }
        holder.txtTitle.setText(rowItem.getTitle());

        return rowview;
    }

    private class viewHolder {
        TextView txtTitle ;

    }
}