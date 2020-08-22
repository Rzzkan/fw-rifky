package com.proyekakhir.pelaporan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.proyekakhir.pelaporan.Utils.ApiClient;
import com.proyekakhir.pelaporan.Utils.ApiInterface;
import com.proyekakhir.pelaporan.Utils.SnackbarHandler;
import com.proyekakhir.pelaporan.response.BaseResponse;
import com.proyekakhir.pelaporan.response.ValidateResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Register extends AppCompatActivity {
    EditText etName, etAddress, etPhone, etEmail, etPassword, etRePassword;
    com.google.android.material.textfield.TextInputLayout lytName, lytAddress, lytPhone, lytEmail, lytPassword, lytRePassword;
    Button btnRegister;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialization();
        snackbar = new SnackbarHandler(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        emailWatcher();
        handleClick();
    }

    private void initialization() {
        etName = (EditText) findViewById(R.id.etName);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etRePassword = (EditText) findViewById(R.id.etRePassword);
        lytAddress = (com.google.android.material.textfield.TextInputLayout) findViewById(R.id.lytAddress);
        lytName = (com.google.android.material.textfield.TextInputLayout) findViewById(R.id.lytName);
        lytPhone = (com.google.android.material.textfield.TextInputLayout) findViewById(R.id.lytPhone);
        lytEmail = (com.google.android.material.textfield.TextInputLayout) findViewById(R.id.lytEmail);
        lytPassword = (com.google.android.material.textfield.TextInputLayout) findViewById(R.id.lytPw);
        lytRePassword = (com.google.android.material.textfield.TextInputLayout) findViewById(R.id.lytRpw);
        btnRegister = (Button) findViewById(R.id.btnSign_up);
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
                    //Call API
                    Call<BaseResponse> postRegister = apiInterface.postRegister(
                            etEmail.getText().toString(),
                            1,
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
                                        Intent intent=new Intent(Register.this,Login.class);
                                        startActivity(intent);
                                        finish();
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
