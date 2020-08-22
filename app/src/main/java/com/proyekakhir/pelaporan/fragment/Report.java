package com.proyekakhir.pelaporan.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.proyekakhir.pelaporan.R;
import com.proyekakhir.pelaporan.Utils.ApiClient;
import com.proyekakhir.pelaporan.Utils.ApiInterface;
import com.proyekakhir.pelaporan.Utils.SPManager;
import com.proyekakhir.pelaporan.Utils.SnackbarHandler;
import com.proyekakhir.pelaporan.Utils.Tools;
import com.proyekakhir.pelaporan.adapter.AdapterReport;
import com.proyekakhir.pelaporan.model.ReportModel;
import com.proyekakhir.pelaporan.response.ReportResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Report extends Fragment {
    RecyclerView rvReport;
    AdapterReport adapter;
    ArrayList<ReportModel> items;

    SnackbarHandler snackbar;
    ApiInterface apiInterface;
    SPManager spManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        initialization(view);
        Tools.setToolbar(getActivity(),"Laporan");
        getData();
        clickListener();
        return view;
    }

    private void  initialization(View view){
        snackbar = new SnackbarHandler(getActivity());
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        spManager = new SPManager(getContext());
        rvReport = view.findViewById(R.id.rvReport);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvReport.setLayoutManager(layoutManager);
        rvReport.setHasFixedSize(true);
        items = new ArrayList<>();
        adapter = new AdapterReport(getContext(),items);
        rvReport.setAdapter(adapter);
    }

    private void getData(){
        Call<ReportResponse> getReport = apiInterface.getAllReports();
        getReport.enqueue(new Callback<ReportResponse>() {
            @Override
            public void onResponse(Call<ReportResponse> call, Response<ReportResponse> response) {
                if (response.body().getSuccess()==1) {
                    if (response.body().getData().size()>0){
                        for (int i=0; i<response.body().getData().size();i++){
                            ReportModel data = response.body().getData().get(i);
                            items.add(new ReportModel(
                                    data.getIdReport(),
                                    data.getName(),
                                    data.getPhone(),
                                    data.getAddress(),
                                    data.getDate(),
                                    data.getImg(),
                                    data.getReport()
                            ));
                        }
                    }
                    adapter.notifyDataSetChanged();
                    snackbar.snackSuccess("Berhasil Memuat Data");
                } else{
                    snackbar.snackError("Gagal Memuat Data");
                }
            }
            @Override
            public void onFailure(Call<ReportResponse> call, Throwable t) {
                snackbar.snackError("Tidak ada Konektivitas");
                snackbar.snackError(t.toString());
            }
        });
    }

    private void clickListener(){
        adapter.setOnItemClickListener(new AdapterReport.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ReportModel obj, int position) {
                Bundle data = new Bundle();
                data.putString("name", obj.getName());
                data.putString("phone", obj.getPhone());
                data.putString("address",obj.getAddress());
                data.putString("date", Tools.dateParser(obj.getDate()));
                data.putString("report",obj.getReport());
                data.putString("img",obj.getImg());
                Tools.addFragment(getActivity(), new DetailReport(), data, "detail-report");
            }
        });
    }
}