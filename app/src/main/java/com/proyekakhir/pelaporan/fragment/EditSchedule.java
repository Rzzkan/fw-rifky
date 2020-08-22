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
import com.proyekakhir.pelaporan.model.UserModel;
import com.proyekakhir.pelaporan.response.BaseResponse;
import com.proyekakhir.pelaporan.response.UserResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditSchedule extends Fragment {

    EditText etDay;
    com.chivorn.smartmaterialspinner.SmartMaterialSpinner spinName;
    Button btnSave;

    String id_schedule, day;
    String id_user;

    ArrayList<UserModel> userList;
    List<String> nameList;

    SnackbarHandler snackbar;
    ApiInterface apiInterface;
    SPManager spManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_edit_schedule, container, false);
        initialization(view);
        Tools.setToolbar(getActivity(),"Ubah Jadwal");
        getOfficer();
        getBundle();
        btnListener();
        return view;
    }

    private void initialization(View view){
        snackbar = new SnackbarHandler(getActivity());
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        spManager = new SPManager(getContext());
        etDay = view.findViewById(R.id.etDay);
        btnSave = view.findViewById(R.id.btnSave);
        spinName = view.findViewById(R.id.spinOfficer);
        nameList = new ArrayList<>();
        userList = new ArrayList<>();
    }

    private void getBundle(){
        Bundle bundle = this.getArguments();
        if (bundle != null){
            id_schedule=  bundle.getString("id_schedule","null");
            day =  bundle.getString("day","null");
            etDay.setText(day);
            etDay.setEnabled(false);
        }
    }

    private void btnListener(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findIDUser(spinName.getSelectedItem().toString());
                if (validate()){
                    updateSchedule();
                }
            }
        });
    }

    private void getOfficer(){
        Call<UserResponse> getOfficers = apiInterface.getAllOfficers();
        getOfficers.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.body().getSuccess()==1) {
                    if (response.body().getData().size()>0){
                        for (int i=0; i<response.body().getData().size();i++){
                            UserModel data = response.body().getData().get(i);
                            nameList.add(data.getName());
                            userList.add(new UserModel(
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
                    spinName.setItem(nameList);
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

    private void updateSchedule(){
        Call<BaseResponse> updateSchedule = apiInterface.postUpdateSchedule(
                id_schedule,
                id_user
        );
        updateSchedule.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body().getSuccess()==1) {
                    snackbar.snackSuccess("Berhasil");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Tools.removeAllFragment(getActivity(), new Dashboard(), "Dasboard");
                        }
                    }, 1000);

                }else {
                    snackbar.snackError("Gagal");
                }
            }
            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                snackbar.snackError("Tidak ada Konektivitas");
                snackbar.snackError(t.toString());
            }
        });
    }

    private UserModel findIDUser(String name) {
        for(UserModel listA : userList) {
            if (listA.getName().equalsIgnoreCase(name)) {
                id_user = listA.getIdUser();
            }
        }
        System.out.println("Not Found");
        return null;
    }

    private boolean validate(){
        boolean valid = false;
        if (id_user.equalsIgnoreCase("")){
            snackbar.snackInfo("Pastikan memilih petugas");
            valid = false;
        }else {
            valid = true;
        }
        return valid;
    }
}