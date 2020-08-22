package com.proyekakhir.pelaporan.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.proyekakhir.pelaporan.R;
import com.proyekakhir.pelaporan.Utils.ApiClient;
import com.proyekakhir.pelaporan.Utils.ApiInterface;
import com.proyekakhir.pelaporan.Utils.SnackbarHandler;
import com.proyekakhir.pelaporan.Utils.Tools;
import com.proyekakhir.pelaporan.response.BaseResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddLocation extends Fragment {
    EditText etName, etAddress, etLatitude, etLongitude;
    com.google.android.material.textfield.TextInputLayout lytName, lytAddress, lytLatitude, lytLongitude;
    Button btnAddLocation;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_location, container, false);
        initialization(view);
        handleClick();
        return view;
    }

    private void initialization(View view){
        snackbar = new SnackbarHandler(getActivity());
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        etName = view.findViewById(R.id.etName);
        etAddress = view.findViewById(R.id.etAddress);
        etLatitude = view.findViewById(R.id.etLatitude);
        etLongitude = view.findViewById(R.id.etLongitude);
        lytName= view.findViewById(R.id.lytName);
        lytAddress = view.findViewById(R.id.lytAddress);
        lytLatitude = view.findViewById(R.id.lytLatitude);
        lytLongitude = view.findViewById(R.id.lytLongitude);
        btnAddLocation = view.findViewById(R.id.btnAddLocation);
    }
    private boolean validate(){
        boolean valid = false;
        if (etName.getText().toString().isEmpty()||
                etAddress.getText().toString().isEmpty()||
                etLatitude.getText().toString().isEmpty()||
                etLongitude.getText().toString().isEmpty()
        ){
            snackbar.snackInfo("Pastikan tidak ada yang kosong");
            valid = false;
        }else {
            valid = true;
        }
        return valid;
    }
    private void handleClick(){
        btnAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    Call<BaseResponse> postRegister = apiInterface.postAddLocation(
                            etName.getText().toString(),
                            etAddress.getText().toString(),
                            etLatitude.getText().toString(),
                            etLongitude.getText().toString()
                    );
                    postRegister.enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            if (response.body().getSuccess()==1) {
                                snackbar.snackSuccess("Berhasil Menambah Lokasi");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Tools.removeAllFragment(getActivity(), new Dashboard(), "dasboard");
                                    }
                                }, 1000);

                            } else{
                                snackbar.snackError("Gagal");
                            }

                        }
                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                            snackbar.snackInfo("Tidak ada Konektivitas");
                        }
                    });
                }else {
                    snackbar.snackInfo(" Gagal");
                }
            }
        });
    }
}