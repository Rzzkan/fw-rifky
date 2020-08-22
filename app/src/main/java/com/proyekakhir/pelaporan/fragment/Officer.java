package com.proyekakhir.pelaporan.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.proyekakhir.pelaporan.R;
import com.proyekakhir.pelaporan.Utils.ApiClient;
import com.proyekakhir.pelaporan.Utils.ApiInterface;
import com.proyekakhir.pelaporan.Utils.SPManager;
import com.proyekakhir.pelaporan.Utils.SnackbarHandler;
import com.proyekakhir.pelaporan.Utils.Tools;
import com.proyekakhir.pelaporan.adapter.AdapterOfficer;
import com.proyekakhir.pelaporan.model.UserModel;
import com.proyekakhir.pelaporan.response.UserResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Officer extends Fragment {
    EditText etSearch;
    RecyclerView rvOfficer;
    Button btnAddOfficer;
    AdapterOfficer adapter;
    ArrayList<UserModel> items;

    SnackbarHandler snackbar;
    ApiInterface apiInterface;
    SPManager spManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_officer, container, false);
        initialization(view);
        Tools.setToolbar(getActivity(),"Daftar Petugas");
        getData();
        btnClick();
        searchHandler();
        return view;
    }

    private void initialization(View view){
        snackbar = new SnackbarHandler(getActivity());
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        spManager = new SPManager(getContext());
        etSearch = view.findViewById(R.id.etSearch);
        rvOfficer = view.findViewById(R.id.rvOfficer);
        btnAddOfficer = view.findViewById(R.id.btnAddOfficer);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvOfficer.setLayoutManager(layoutManager);
        rvOfficer.setHasFixedSize(true);
        items = new ArrayList<>();
        adapter = new AdapterOfficer(getContext(),items);
        rvOfficer.setAdapter(adapter);
        if (spManager.getSpRole()!=0){
            btnAddOfficer.setVisibility(View.GONE);
        }
    }

    private void btnClick(){
        btnAddOfficer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.addFragment(getActivity(), new AddOfficer(), null, "add-officer");
            }
        });
    }

    private void searchHandler(){
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString().toLowerCase().trim());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getData(){
        Call<UserResponse> getOfficers = apiInterface.getAllOfficers();
        getOfficers.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.body().getSuccess()==1) {
                    if (response.body().getData().size()>0){
                        for (int i=0; i<response.body().getData().size();i++){
                            UserModel data = response.body().getData().get(i);
                            items.add(new UserModel(
                                    data.getIdUser(),
                                    data.getEmail(),
                                    data.getRole(),
                                    data.getName(),
                                    data.getAddress(),
                                    data.getPhone(),
                                    data.getPassword(),
                                    data.getImg()
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
            public void onFailure(Call<UserResponse> call, Throwable t) {
                snackbar.snackError("Tidak ada Konektivitas");
                snackbar.snackError(t.toString());
            }
        });
    }
}