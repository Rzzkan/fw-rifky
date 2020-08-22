package com.proyekakhir.pelaporan.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import com.proyekakhir.pelaporan.R;
import com.proyekakhir.pelaporan.Utils.ApiClient;
import com.proyekakhir.pelaporan.Utils.ApiInterface;
import com.proyekakhir.pelaporan.Utils.SPManager;
import com.proyekakhir.pelaporan.Utils.SnackbarHandler;
import com.proyekakhir.pelaporan.Utils.Tools;
import com.proyekakhir.pelaporan.adapter.AdapterContact;
import com.proyekakhir.pelaporan.adapter.AdapterLocation;
import com.proyekakhir.pelaporan.model.ContactModel;
import com.proyekakhir.pelaporan.model.LocationModel;
import com.proyekakhir.pelaporan.response.ContactResponse;
import com.proyekakhir.pelaporan.response.LocationResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Location extends Fragment {
    EditText etSearch;
    RecyclerView rvLocation;
    Button btnAddLocation;
    WebView webView;
    AdapterLocation adapter;
    ArrayList<LocationModel> items;

    SnackbarHandler snackbar;
    ApiInterface apiInterface;
    SPManager spManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        initialization(view);
        Tools.setToolbar(getActivity(),"Lokasi");
        getData();
        searchHandler();
        if (spManager.getSpRole()==0){
            btnClick();
        }
        clickListener();
        return view;
    }

    private void initialization(View view){
        snackbar = new SnackbarHandler(getActivity());
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        spManager = new SPManager(getContext());
        etSearch = view.findViewById(R.id.etSearch);
        rvLocation = view.findViewById(R.id.rvLocation);
        btnAddLocation = view.findViewById(R.id.btnAddLocation);
        webView = view.findViewById(R.id.webView);
        rvLocation.setLayoutManager(new LinearLayoutManager(getContext()));
        rvLocation.setHasFixedSize(true);
        items = new ArrayList<>();
        adapter = new AdapterLocation(getContext(),items);
        rvLocation.setAdapter(adapter);
        if (spManager.getSpRole()!=0){
            btnAddLocation.setVisibility(View.GONE);
        }
    }

    private void clickListener(){
        adapter.setOnItemClickListener(new AdapterLocation.OnItemClickListener() {
            @Override
            public void onItemClick(View view, LocationModel obj, int position) {
                if (spManager.getSpRole()!=0){
                    final String lati = obj.getLatitude().replaceAll("[^0-9.]", "");
                    final String longi = obj.getLongitude().replaceAll("[^0-9.]", "");
                    String url ="https://maps.google.com/maps?q=-"+lati+","+longi;
                    webView.loadUrl(url);
                }else {
                    Bundle data = new Bundle();
                    data.putString("id_location", obj.getIdLocation());
                    data.putString("name", obj.getName());
                    data.putString("address", obj.getAddress());
                    data.putString("longitude", obj.getLatitude());
                    data.putString("latitude", obj.getLongitude());
                    Tools.addFragment(getActivity(), new EditLocation(), data, "edit-location");
                }
            }
        });

    }

    private void btnClick(){
        btnAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.addFragment(getActivity(), new AddLocation(), null, "add-contact");
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
        Call<LocationResponse> getLocation = apiInterface.getAllLocations();
        getLocation.enqueue(new Callback<LocationResponse>() {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                if (response.body().getSuccess()==1) {
                    if (response.body().getData().size()>0){
                        for (int i=0; i<response.body().getData().size();i++){
                            LocationModel data = response.body().getData().get(i);
                            items.add(new LocationModel(
                                    data.getIdLocation(),
                                    data.getName(),
                                    data.getAddress(),
                                    data.getLatitude(),
                                    data.getLongitude()
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
            public void onFailure(Call<LocationResponse> call, Throwable t) {
                snackbar.snackError("Tidak ada Konektivitas");
                snackbar.snackError(t.toString());
                Log.d("cek home",t.toString());
            }
        });
    }
}