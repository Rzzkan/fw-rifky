package com.proyekakhir.pelaporan.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.proyekakhir.pelaporan.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Tools {
    public static int dpToPx(Context c, int dp) {
        Resources r = c.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static void addFragment(FragmentActivity activity, Fragment fragment, Bundle bundle , String tag) {
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.addToBackStack(tag);
        ft.replace(R.id.main_content, fragment, tag);
        ft.commitAllowingStateLoss();
        fragment.setArguments(bundle);
    }

    public static void removeAllFragment(FragmentActivity activity, Fragment replaceFragment, String tag) {
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ft.replace(R.id.main_content, replaceFragment);
        ft.commitAllowingStateLoss();
    }

    public static void setToolbar(Activity activity, String title){
        ((AppCompatActivity)activity).getSupportActionBar().setTitle(title);

    }

    public static void displayImageOriginal(Context ctx, ImageView img, String url) {
        try {
            Glide.with(ctx).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(img);
        } catch (Exception e) {
        }
    }


    public static String getFormattedDate(Long dateTime) {
        SimpleDateFormat newFormat = new SimpleDateFormat("EEE, d MMM yyyy");
        return newFormat.format(new Date(dateTime));
    }

    public static String simpledateParser(String currentDate){
        SimpleDateFormat currentFormat,newFormat;
        String newDate = "";
        currentFormat = new SimpleDateFormat("E, d MMM yyyy");
        newFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date getDate = currentFormat.parse(currentDate);
            newDate = newFormat.format(getDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    public static String dateParser(String currentDate){
        SimpleDateFormat currentFormat,newFormat;
        String newDate = "";
        currentFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        newFormat = new SimpleDateFormat("E, d MMM yyyy HH:mm:ss");
        try {
            Date getDate = currentFormat.parse(currentDate);
            newDate = newFormat.format(getDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }
}
