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
import com.proyekakhir.pelaporan.Utils.SPManager;
import com.proyekakhir.pelaporan.Utils.SnackbarHandler;
import com.proyekakhir.pelaporan.Utils.Tools;
import com.proyekakhir.pelaporan.response.BaseResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditContact extends Fragment {
    EditText etDivision, etAddress, etPhone;
    com.google.android.material.textfield.TextInputLayout lytDivision, lytAddress, lytPhone;
    Button btnAddContact;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;

    String id_contact, division, phone, address;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_contact, container, false);
        initialization(view);
        Tools.setToolbar(getActivity(),"Ubah Kontak");
        getBundle();
        handleClick();
        return view;
    }

    private void initialization(View view){
        snackbar = new SnackbarHandler(getActivity());
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        etDivision = view.findViewById(R.id.etDivision);
        etAddress = view.findViewById(R.id.etAddress);
        etPhone = view.findViewById(R.id.etPhone);
        lytAddress = view.findViewById(R.id.lytAddress);
        lytDivision = view.findViewById(R.id.lytDivision);
        lytPhone = view.findViewById(R.id.lytPhone);
        btnAddContact = view.findViewById(R.id.btnAddContact);
        btnAddContact.setText("Simpan");
    }

    private void getBundle(){
        Bundle bundle = this.getArguments();
        if (bundle != null){
           id_contact =  bundle.getString("id_contact","null");
           phone =  bundle.getString("phone","null");
           division = bundle.getString("division","null");
           address = bundle.getString("address", "null");
           etAddress.setText(address);
           etDivision.setText(division);
           etPhone.setText(phone);
        }
    }

    private boolean validate(){
        boolean valid = false;
        if (etDivision.getText().toString().isEmpty()||
                etAddress.getText().toString().isEmpty()||
                etPhone.getText().toString().isEmpty()
        ){
            snackbar.snackInfo("Pastikan tidak ada yang kosong");
            valid = false;
        }else {
            valid = true;
        }
        return valid;
    }
    private void handleClick(){
        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    Call<BaseResponse> postRegister = apiInterface.postUpdateContact(
                            id_contact,
                            etPhone.getText().toString(),
                            etDivision.getText().toString(),
                            etAddress.getText().toString()
                    );
                    postRegister.enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            if (response.body().getSuccess()==1) {
                                snackbar.snackSuccess("Berhasil Menambah Kontak");
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