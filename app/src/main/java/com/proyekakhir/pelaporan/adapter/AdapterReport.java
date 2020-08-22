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
import com.proyekakhir.pelaporan.model.ReportModel;

import java.util.ArrayList;

public class AdapterReport extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private ArrayList<ReportModel> items = new ArrayList<>();
    private ArrayList<ReportModel> itemsFiltered = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(View view, ReportModel obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterReport(Context context, ArrayList<ReportModel> items) {
        this.items = items;
        this.itemsFiltered = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public View lyt_parent;
        public View lyt_ripple;
        public TextView tvDivision, tvPhone, tvAddress;
        public OriginalViewHolder(View v) {
            super(v);
            lyt_parent = v.findViewById(R.id.lyt_parent);
            lyt_ripple = v.findViewById(R.id.lyt_ripple);
            tvDivision = v.findViewById(R.id.tvDivision);
            tvPhone = v.findViewById(R.id.tvPhone);
            tvAddress = v.findViewById(R.id.tvAddress);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            ReportModel data = itemsFiltered.get(position);
            view.tvDivision.setText(data.getIdReport());
            view.tvPhone.setText(data.getReport());
            view.tvAddress.setVisibility(View.GONE);
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
                    ArrayList<ReportModel> filteredList = new ArrayList<>();
                    for (ReportModel data : items) {
                        String name = data.getReport().toLowerCase().trim();
                        if(name.contains(charString.toLowerCase().trim())){
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
                itemsFiltered = (ArrayList<ReportModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}