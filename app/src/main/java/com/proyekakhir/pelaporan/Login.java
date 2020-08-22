package com.proyekakhir.pelaporan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.karan.churi.PermissionManager.PermissionManager;
import com.proyekakhir.pelaporan.Utils.ApiClient;
import com.proyekakhir.pelaporan.Utils.ApiInterface;
import com.proyekakhir.pelaporan.Utils.SPManager;
import com.proyekakhir.pelaporan.Utils.SnackbarHandler;
import com.proyekakhir.pelaporan.model.UserModel;
import com.proyekakhir.pelaporan.response.LogInResponse;
import com.proyekakhir.pelaporan.response.ValidateResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity {
    EditText etEmail,etPassword;
    com.google.android.material.textfield.TextInputLayout lytEmail,lytPassword;
    Button btnLogin;
    com.balysv.materialripple.MaterialRippleLayout btnRegister;

    SPManager spManager;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;
    PermissionManager permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialization();
        spManager = new SPManager(this);
        if (spManager.getSpIsSigned()){
            startActivity(new Intent(this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
        snackbar = new SnackbarHandler(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        permission=new PermissionManager() {};
        permission.checkAndRequestPermissions(this);
        buttonHandler();
    }

    private void initialization(){
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        lytEmail = (com.google.android.material.textfield.TextInputLayout) findViewById(R.id.lytEmail);
        lytPassword = (com.google.android.material.textfield.TextInputLayout) findViewById(R.id.lytPassword);
        btnLogin = (Button) findViewById(R.id.btnSign_in);
        btnRegister = (com.balysv.materialripple.MaterialRippleLayout) findViewById(R.id.btnSign_up);
    }

    private void buttonHandler(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    isEmailExists();
                }
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }

    private boolean validate(){
        boolean valid = false;
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
            valid = false;
            lytEmail.setError("Masukkan email yang Valid");
        } else {
            valid = true;
            lytEmail.setError(null);
        }

        if (etPassword.getText().toString().isEmpty()) {
            valid = false;
            lytPassword.setError("Masukkan Kata sandi yang Valid");
        } else {
            if (etPassword.length() > 2) {
                valid = true;
                lytPassword.setError(null);
            } else {
                valid = false;
                lytPassword.setError("Kata sandi terlalu pendek");
            }
        }
        return valid;
    }

    private void isEmailExists(){
        Call<ValidateResponse> postIsEmailExist = apiInterface.postIsEmailExist(
                etEmail.getText().toString()
        );
        postIsEmailExist.enqueue(new Callback<ValidateResponse>() {
            @Override
            public void onResponse(Call<ValidateResponse> call, Response<ValidateResponse> response) {
                if (response.body().isData()) {
                    auth();
                } else{
                    snackbar.snackError("Email Belum terdaftar");
                    lytEmail.setError("Email Belum Terdaftar");
                }
            }
            @Override
            public void onFailure(Call<ValidateResponse> call, Throwable t) {
                snackbar.snackInfo("Tidak ada Konektivitas");
            }
        });
    }

    private void auth(){
        Call<LogInResponse> postSingnin = apiInterface.postSignin(
                etEmail.getText().toString(),
                etPassword.getText().toString()
        );

        postSingnin.enqueue(new Callback<LogInResponse>() {
            @Override
            public void onResponse(Call<LogInResponse> call, Response<LogInResponse> response) {
                if (response.body().getSuccess()==1) {
                    UserModel currentUser = response.body().getData();
                    spManager.saveSPString(spManager.SP_ID, currentUser.getIdUser());
                    spManager.saveSPString(spManager.SP_NAME, currentUser.getName());
                    spManager.saveSPString(spManager.SP_EMAIL, currentUser.getEmail());
                    spManager.saveSPString(spManager.SP_ADDRESS, currentUser.getAddress());
                    spManager.saveSPString(spManager.SP_PHONE, currentUser.getPhone());
                    spManager.saveSPString(spManager.SP_IMG, currentUser.getImg());
                    spManager.saveSPInt(spManager.SP_ROLE, currentUser.getRole());
                    spManager.saveSPBoolean(spManager.SP_IS_SIGNED, true);
                    snackbar.snackSuccess("Berhasil Masuk");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent=new Intent(Login.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 1000);
                } else{
                    snackbar.snackError("Kata Sandi Salah");
                }
            }
            @Override
            public void onFailure(Call<LogInResponse> call, Throwable t) {
                snackbar.snackInfo("Tidak ada Konektivitas");
            }
        });
    }

}
