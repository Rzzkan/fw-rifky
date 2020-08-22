package com.proyekakhir.pelaporan.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.proyekakhir.pelaporan.Login;
import com.proyekakhir.pelaporan.R;
import com.proyekakhir.pelaporan.Register;
import com.proyekakhir.pelaporan.Utils.ApiClient;
import com.proyekakhir.pelaporan.Utils.ApiInterface;
import com.proyekakhir.pelaporan.Utils.SnackbarHandler;
import com.proyekakhir.pelaporan.Utils.Tools;
import com.proyekakhir.pelaporan.response.BaseResponse;
import com.proyekakhir.pelaporan.response.ValidateResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddOfficer extends Fragment {
    EditText etName, etAddress, etPhone, etEmail, etPassword, etRePassword;
    com.google.android.material.textfield.TextInputLayout lytName, lytAddress, lytPhone, lytEmail, lytPassword, lytRePassword;
    Button btnRegister;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_officer, container, false);
        initialization(view);
        Tools.setToolbar(getActivity(),"Tambah Petugas");
        emailWatcher();
        handleClick();
        return view;
    }

    private void  initialization(View view){
        snackbar = new SnackbarHandler(getActivity());
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        etName = view.findViewById(R.id.etName);
        etAddress = view.findViewById(R.id.etAddress);
        etPhone = view.findViewById(R.id.etPhone);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        etRePassword = view.findViewById(R.id.etRePassword);
        lytAddress = view.findViewById(R.id.lytAddress);
        lytName = view.findViewById(R.id.lytName);
        lytPhone = view.findViewById(R.id.lytPhone);
        lytEmail = view.findViewById(R.id.lytEmail);
        lytPassword = view.findViewById(R.id.lytPw);
        lytRePassword = view.findViewById(R.id.lytRpw);
        btnRegister = view.findViewById(R.id.btnRegister);
    }

    private boolean validate(){
        boolean valid = false;
        if (etName.getText().toString().isEmpty()||
                etAddress.getText().toString().isEmpty()||
                etPhone.getText().toString().isEmpty()||
                etPassword.getText().toString().isEmpty()||
                etRePassword.getText().toString().isEmpty()||
                etEmail.getText().toString().isEmpty()
        ){
            snackbar.snackInfo("Pastikan tidak ada yang kosong");
            valid = false;
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()){
            lytEmail.setError("Tolong masukkan email yang valid");
            valid = false;
        }else if (!etPassword.getText().toString().equals(etRePassword.getText().toString())){
            lytRePassword.setError("kata sandi tidak sama");
            valid = false;
        }else {
            valid = true;
        }
        return valid;
    }

    private void emailWatcher(){
        boolean check = false;
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lytEmail.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                lytEmail.setErrorEnabled(true);
                Call<ValidateResponse> postIsEmailExist = apiInterface.postIsEmailExist(
                        s.toString()
                );
                postIsEmailExist.enqueue(new Callback<ValidateResponse>() {
                    @Override
                    public void onResponse(Call<ValidateResponse> call, Response<ValidateResponse> response) {
                        if (!response.body().isData()) {
                            snackbar.snackSuccess("Email tersedia");
                            btnRegister.setClickable(true);
                        } else{
                            snackbar.snackError("Email sudah digunakan");
                            lytEmail.setError("Email sudah digunakan");
                            btnRegister.setClickable(false);
                        }
                    }
                    @Override
                    public void onFailure(Call<ValidateResponse> call, Throwable t) {
                        snackbar.snackInfo("Tidak ada konektifitas");
                    }
                });
            }
        });
    }
    private void handleClick(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    Call<BaseResponse> postRegister = apiInterface.postRegister(
                            etEmail.getText().toString(),
                            2,
                            etName.getText().toString(),
                            etAddress.getText().toString(),
                            etPhone.getText().toString(),
                            "null",
                            etRePassword.getText().toString()
                    );
                    postRegister.enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            if (response.body().getSuccess()==1) {
                                snackbar.snackSuccess("Registrasi Berhasil");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Tools.removeAllFragment(getActivity(), new Officer(), "officer");
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
                    snackbar.snackInfo("Registrasi Gagal");
                }
            }
        });
    }
}