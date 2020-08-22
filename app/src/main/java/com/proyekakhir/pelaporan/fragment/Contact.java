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
import android.widget.Button;
import android.widget.EditText;

import com.proyekakhir.pelaporan.R;
import com.proyekakhir.pelaporan.Utils.ApiClient;
import com.proyekakhir.pelaporan.Utils.ApiInterface;
import com.proyekakhir.pelaporan.Utils.SPManager;
import com.proyekakhir.pelaporan.Utils.SnackbarHandler;
import com.proyekakhir.pelaporan.Utils.Tools;
import com.proyekakhir.pelaporan.adapter.AdapterContact;
import com.proyekakhir.pelaporan.model.ContactModel;
import com.proyekakhir.pelaporan.response.ContactResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Contact extends Fragment {
    EditText etSearch;
    RecyclerView rvContact;
    Button btnAddContact;
    AdapterContact adapter;
    ArrayList<ContactModel> items;

    SnackbarHandler snackbar;
    ApiInterface apiInterface;
    SPManager spManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        initialization(view);
        Tools.setToolbar(getActivity(),"Kontak");
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
        rvContact = view.findViewById(R.id.rvContact);
        btnAddContact = view.findViewById(R.id.btnAddContact);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvContact.setLayoutManager(layoutManager);
        rvContact.setHasFixedSize(true);
        items = new ArrayList<>();
        adapter = new AdapterContact(getContext(),items);
        rvContact.setAdapter(adapter);
        if (spManager.getSpRole()!=0){
            btnAddContact.setVisibility(View.GONE);
        }
    }

    private void clickListener(){
        adapter.setOnItemClickListener(new AdapterContact.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ContactModel obj, int position) {
                if (spManager.getSpRole()!=0){
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + obj.getPhone()));
                    startActivity(intent);
                }else  {
                    Bundle data = new Bundle();
                    data.putString("id_contact", obj.getIdContact());
                    data.putString("phone", obj.getPhone());
                    data.putString("division", obj.getDivision());
                    data.putString("address", obj.getAddress());
                    Tools.addFragment(getActivity(), new EditContact(), data, "edit-contact");
                }
            }
        });
    }

    private void btnClick(){
        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.addFragment(getActivity(), new AddContact(), null, "add-contact");
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
        Call<ContactResponse> getContact = apiInterface.getAllContacts();
        getContact.enqueue(new Callback<ContactResponse>() {
            @Override
            public void onResponse(Call<ContactResponse> call, Response<ContactResponse> response) {
                if (response.body().getSuccess()==1) {
                    if (response.body().getData().size()>0){
                        for (int i=0; i<response.body().getData().size();i++){
                            ContactModel data = response.body().getData().get(i);
                            items.add(new ContactModel(
                               data.getIdContact(),
                               data.getDivision(),
                               data.getAddress(),
                               data.getPhone()
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
            public void onFailure(Call<ContactResponse> call, Throwable t) {
                snackbar.snackError("Tidak ada Konektivitas");
                snackbar.snackError(t.toString());
                Log.d("cek home",t.toString());
            }
        });
    }
}