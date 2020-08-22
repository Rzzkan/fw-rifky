package com.proyekakhir.pelaporan.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.proyekakhir.pelaporan.R;
import com.proyekakhir.pelaporan.Utils.ApiClient;
import com.proyekakhir.pelaporan.Utils.ApiInterface;
import com.proyekakhir.pelaporan.Utils.SPManager;
import com.proyekakhir.pelaporan.Utils.SnackbarHandler;
import com.proyekakhir.pelaporan.Utils.Tools;
import com.proyekakhir.pelaporan.response.BaseResponse;
import com.proyekakhir.pelaporan.response.ValidateResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends Fragment {
    Button btnSave;
    EditText etOldPw,etNewPw;
    com.google.android.material.textfield.TextInputLayout lytOldPw,lytNewPw;
    boolean hidePassHandler = true;
    boolean hidePassHandler2 = true;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;
    SPManager spManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_change_password, container, false);
        initialization(view);
        Tools.setToolbar(getActivity(),"Ubah Kata Sandi");
        snackbar = new SnackbarHandler(getActivity());
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        spManager = new SPManager(getContext());
        handleClick();
        pwWatcher();
        return view;
    }

    private void initialization(View view){
        etNewPw = view.findViewById(R.id.etNewPw);
        etOldPw = view.findViewById(R.id.etOldPw);
        btnSave = view.findViewById(R.id.btnSave);
        btnSave.setClickable(false);
    }

    private  void handleClick(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               changePw();
            }
        });
    }

    private void changePw(){
        Call<BaseResponse> postChangePw = apiInterface.postChangePw(
                spManager.getSpID(),
                etNewPw.getText().toString()
        );
        postChangePw.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body().getSuccess()==1) {
                    snackbar.snackSuccess("Berhasil merubah kata sandi");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Tools.removeAllFragment(getActivity(), new Account(),"account");
                        }
                    }, 1000);
                } else{
                    snackbar.snackError("Gagal merubah sandi");
                }

            }
            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                snackbar.snackInfo("Tidak ada konektifitas");
            }
        });
    }

    private void checkOldPw(){
        Call<ValidateResponse> postCheckPw = apiInterface.postCheckPw(
                spManager.getSpID(),
                etOldPw.getText().toString()
        );
        postCheckPw.enqueue(new Callback<ValidateResponse>() {
            @Override
            public void onResponse(Call<ValidateResponse> call, Response<ValidateResponse> response) {
                if (response.body().isData()) {
                    snackbar.snackSuccess("Sandi lama sesuai");
                    btnSave.setClickable(true);
                } else{
                    snackbar.snackError("Sandi Lama tidak sesuai");
                    btnSave.setClickable(false);
                }

            }
            @Override
            public void onFailure(Call<ValidateResponse> call, Throwable t) {
                snackbar.snackInfo("Tidak ada Konektifitas");
            }
        });
    }

    private void pwWatcher(){
        etOldPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkOldPw();
            }
        });
    }
}
