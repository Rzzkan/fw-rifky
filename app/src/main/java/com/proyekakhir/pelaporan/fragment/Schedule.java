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
import com.proyekakhir.pelaporan.adapter.AdapterSchedule;
import com.proyekakhir.pelaporan.model.ScheduleModel;
import com.proyekakhir.pelaporan.response.ScheduleResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Schedule extends Fragment {
    RecyclerView rvSchedule;
    AdapterSchedule adapter;
    ArrayList<ScheduleModel> items;

    SnackbarHandler snackbar;
    ApiInterface apiInterface;
    SPManager spManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        initialization(view);
        getData();
        if (spManager.getSpRole()==0){
            clickListener();
        }
        return view;
    }

    private void  initialization(View view){
        snackbar = new SnackbarHandler(getActivity());
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        spManager = new SPManager(getContext());
        rvSchedule = view.findViewById(R.id.rvSchedule);
        rvSchedule.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSchedule.setHasFixedSize(true);
        items = new ArrayList<>();
        adapter = new AdapterSchedule(getContext(),items);
        rvSchedule.setAdapter(adapter);
    }


    private void getData(){
        Call<ScheduleResponse> getSchedule = apiInterface.getAllSchedules();
        getSchedule.enqueue(new Callback<ScheduleResponse>() {
            @Override
            public void onResponse(Call<ScheduleResponse> call, Response<ScheduleResponse> response) {
                if (response.body().getSuccess()==1) {
                    if (response.body().getData().size()>0){
                        for (int i=0; i<response.body().getData().size();i++){
                            ScheduleModel data = response.body().getData().get(i);
                            items.add(new ScheduleModel(
                                    data.getIdSchedule(),
                                    data.getName(),
                                    data.getPhone(),
                                    data.getAddress(),
                                    data.getDay()
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
            public void onFailure(Call<ScheduleResponse> call, Throwable t) {
                snackbar.snackError("Tidak ada Konektivitas");
                snackbar.snackError(t.toString());
            }
        });
    }

    private void clickListener(){
        adapter.setOnItemClickListener(new AdapterSchedule.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ScheduleModel obj, int position) {
                Bundle data = new Bundle();
                data.putString("id_schedule", obj.getIdSchedule());
                data.putString("day", obj.getDay());
                Tools.addFragment(getActivity(), new EditSchedule(), data, "edit-schedule");
            }
        });
    }
}