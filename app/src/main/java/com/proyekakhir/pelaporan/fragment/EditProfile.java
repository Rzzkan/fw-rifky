package com.proyekakhir.pelaporan.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

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

public class EditProfile extends Fragment {
    EditText etName, etAddress, etPhone, etEmail, etPassword, etRePassword;
    com.google.android.material.textfield.TextInputLayout lytName, lytAddress, lytPhone, lytEmail, lytPassword, lytRePassword;
    Button btnRegister;
    RadioGroup radioGender;
    RadioButton radioMale, radioFemale;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;
    SPManager spManager;
    String selectedGender;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_register, container, false);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        snackbar = new SnackbarHandler(getActivity());
        spManager = new SPManager(getContext());
        initialization(view);
        Tools.setToolbar(getActivity(),"Ubah Profil");
        setData();
        handleClick();
        return view;
    }

    private void initialization(View view){
        etName =  view.findViewById(R.id.etName);
        etAddress =  view.findViewById(R.id.etAddress);
        etPhone =  view.findViewById(R.id.etPhone);
        etEmail =  view.findViewById(R.id.etEmail);
        etPassword =  view.findViewById(R.id.etPassword);
        etRePassword =  view.findViewById(R.id.etRePassword);
        lytAddress =  view.findViewById(R.id.lytAddress);
        lytName =  view.findViewById(R.id.lytName);
        lytPhone =  view.findViewById(R.id.lytPhone);
        lytEmail =  view.findViewById(R.id.lytEmail);
        lytPassword = view.findViewById(R.id.lytPw);
        lytRePassword = view.findViewById(R.id.lytRpw);
        btnRegister =  view.findViewById(R.id.btnSign_up);
        btnRegister.setText("Simpan");
        etPassword.setVisibility(View.GONE);
        etRePassword.setVisibility(View.GONE);
        lytPassword.setVisibility(View.GONE);
        lytRePassword.setVisibility(View.GONE);
        etEmail.setEnabled(false);
        etEmail.setTextColor(getResources().getColor(R.color.divider));
    }

    private void setData(){
        etName.setText(spManager.getSpName());
        etEmail.setText(spManager.getSpEmail());
        etAddress.setText(spManager.getSpAddress());
        etPhone.setText(spManager.getSpPhone());
    }

    private void handleClick(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private boolean validate(){
        boolean valid = false;
        if (etName.getText().toString().isEmpty()||
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

    private void saveData(){
        if (validate()){
            //Call API
            Call<BaseResponse> postEditUser = apiInterface.postEditUser(
                    spManager.getSpID(),
                    etName.getText().toString(),
                    etAddress.getText().toString(),
                    etPhone.getText().toString()
            );
            postEditUser.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.body().getSuccess()==1) {
                        snackbar.snackSuccess("Berhasil Merubah Data");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                spManager.saveSPString(spManager.SP_NAME,etName.getText().toString());
                                spManager.saveSPString(spManager.SP_ADDRESS,etAddress.getText().toString());
                                spManager.saveSPString(spManager.SP_PHONE,etPhone.getText().toString());
                                Tools.removeAllFragment(getActivity(),new Account(),"account");
                            }
                        }, 1000);

                    } else{
                        snackbar.snackError("Gagal");
                    }

                }
                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    snackbar.snackInfo("Tidak ada Konektifitas");
                }
            });
        }else {
            snackbar.snackInfo("Gagal");
        }
    }
}
