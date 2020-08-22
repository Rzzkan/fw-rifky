package com.proyekakhir.pelaporan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.proyekakhir.pelaporan.R;
import com.proyekakhir.pelaporan.model.ScheduleModel;

import java.util.ArrayList;

public class AdapterSchedule extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private ArrayList<ScheduleModel> items = new ArrayList<>();
    private ArrayList<ScheduleModel> itemsFiltered = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(View view, ScheduleModel obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterSchedule(Context context, ArrayList<ScheduleModel> items) {
        this.items = items;
        this.itemsFiltered = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public View lyt_parent;
        public View lyt_ripple;
        public TextView tvName, tvDay, tvPhone;
        public OriginalViewHolder(View v) {
            super(v);
            lyt_parent = v.findViewById(R.id.lyt_parent);
            lyt_ripple = v.findViewById(R.id.lyt_ripple);
            tvName = v.findViewById(R.id.tvName);
            tvDay = v.findViewById(R.id.tvDay);
            tvPhone = v.findViewById(R.id.tvPhone);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            ScheduleModel data = itemsFiltered.get(position);
            view.tvName.setText(data.getName());
            view.tvDay.setText(data.getDay());
            view.tvPhone.setText(data.getPhone());
            view.lyt_ripple.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, itemsFiltered.get(position), position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return itemsFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    itemsFiltered = items;
                } else {
                    ArrayList<ScheduleModel> filteredList = new ArrayList<>();
                    for (ScheduleModel data : items) {
                        String day = data.getDay().toLowerCase().trim();
                        if(day.contains(charString.toLowerCase().trim())){
                            filteredList.add(data);
                        }
                    }

                    itemsFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = itemsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                itemsFiltered = (ArrayList<ScheduleModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}