package com.proyekakhir.pelaporan.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.proyekakhir.pelaporan.R;
import com.proyekakhir.pelaporan.Utils.ApiClient;
import com.proyekakhir.pelaporan.Utils.Tools;

public class DetailReport extends Fragment {
    ImageView imgReport;
    TextView tvName, tvPhone, tvAddress, tvDate, tvReport;

    String img, name, phone, address, date, report;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_detail_report, container, false);
        initialization(view);
        Tools.setToolbar(getActivity(),"Detil Laporan");
        getBundle();
        return view;
    }

    private void  initialization(View view){
        imgReport = view.findViewById(R.id.imgReport);
        tvName = view.findViewById(R.id.tvName);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvDate = view.findViewById(R.id.tvDate);
        tvReport = view.findViewById(R.id.tvReport);
    }

    private void getBundle(){
        Bundle bundle = this.getArguments();
        if (bundle != null){
            img = bundle.getString("img", "null");
            name = bundle.getString("name","null");
            phone =  bundle.getString("phone","null");
            address = bundle.getString("address", "null");
            date = bundle.getString("date", "null");
            report = bundle.getString("report", "null");

            Glide.with(getContext()).load(ApiClient.BASE_URL+img).centerCrop().into(imgReport);
            tvName.setText(name);
            tvPhone.setText(phone);
            tvAddress.setText(address);
            tvDate.setText(date);
            tvReport.setText(report);
        }
    }
}