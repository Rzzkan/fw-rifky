package com.proyekakhir.pelaporan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karan.churi.PermissionManager.PermissionManager;
import com.proyekakhir.pelaporan.Utils.BottomNavigationViewHelper;
import com.proyekakhir.pelaporan.Utils.SPManager;
import com.proyekakhir.pelaporan.Utils.Tools;
import com.proyekakhir.pelaporan.fragment.Account;
import com.proyekakhir.pelaporan.fragment.Dashboard;
import com.proyekakhir.pelaporan.fragment.Officer;
import com.proyekakhir.pelaporan.fragment.Report;

public class MainActivity extends AppCompatActivity {
    private View parent_view;
    SPManager spManager;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spManager = new SPManager(this);
        parent_view = findViewById(R.id.container);
        initComponent();
        handleRole();
    }

    private void initComponent() {
        // on item list clicked
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));
//        getSupportActionBar().hide();
        Tools.removeAllFragment(MainActivity.this,new Dashboard(),"dashboard");
        navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.nav_dashboard:
                        Tools.removeAllFragment(MainActivity.this,new Dashboard(),"dashboard");
                        break;
                    case R.id.nav_report:
                        Tools.removeAllFragment(MainActivity.this, new Report(),"report");
                        break;
                    case R.id.nav_officer:
                        Tools.removeAllFragment(MainActivity.this, new Officer(),"officer");
                        break;
                    case R.id.nav_account:
                        Tools.removeAllFragment(MainActivity.this, new Account(),"account");
                        break;
                    case R.id.nav_logout:
                        logoutDialog();
                        break;
                }
                return true;
            }
        });
    }

    public void logoutDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle    ("Keluar");
        builder.setMessage("Apakah Anda Yakin untuk Keluar ?");
        builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                spManager.saveSPBoolean(spManager.SP_IS_SIGNED, false);
                spManager.removeSP();
                startActivity(new Intent(MainActivity.this, Login.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void handleRole(){
        if (spManager.getSpRole()==1){
            Log.d("cek role", String.valueOf(spManager.getSpRole()));
            navigation.getMenu().getItem(1).setVisible(false);
            navigation.getMenu().getItem(2).setVisible(false);
        }else if (spManager.getSpRole()==2){
            navigation.getMenu().getItem(1).setVisible(true);
            navigation.getMenu().getItem(2).setVisible(false);
        }else {
            navigation.getMenu().getItem(1).setVisible(true);
            navigation.getMenu().getItem(2).setVisible(true);
        }
    }
}