package com.proyekakhir.pelaporan.Utils;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.proyekakhir.pelaporan.R;

public class SnackbarHandler {
    Activity mActivity;

    public SnackbarHandler(Activity activity){
        mActivity = activity;
    }

    public void snackError(String message ) {
        final Snackbar snackbar = Snackbar.make(mActivity.findViewById(android.R.id.content), "", Snackbar.LENGTH_SHORT);
        //inflate view
        View custom_view = mActivity.getLayoutInflater().inflate(R.layout.snackbar_icon_error, null);

        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarView.setPadding(0, 0, 0, 0);

        ((TextView) custom_view.findViewById(R.id.message)).setText(message);
        ImageView icon =  ((ImageView) custom_view.findViewById(R.id.icon));
        (custom_view.findViewById(R.id.parent_view)).setBackgroundColor(mActivity.getResources().getColor(R.color.color_error));
        snackBarView.addView(custom_view, 0);
        snackbar.show();
    }

    public void snackSuccess(String message) {
        final Snackbar snackbar = Snackbar.make(mActivity.findViewById(android.R.id.content), "", Snackbar.LENGTH_SHORT);
        //inflate view
        View custom_view = mActivity.getLayoutInflater().inflate(R.layout.snackbar_icon_success, null);

        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarView.setPadding(0, 0, 0, 0);

        ((TextView) custom_view.findViewById(R.id.message)).setText(message);
        ImageView icon =  ((ImageView) custom_view.findViewById(R.id.icon));
        (custom_view.findViewById(R.id.parent_view)).setBackgroundColor(mActivity.getResources().getColor(R.color.color_success));
        snackBarView.addView(custom_view, 0);
        snackbar.show();
    }

    public void snackInfo( String message) {
        final Snackbar snackbar = Snackbar.make(mActivity.findViewById(android.R.id.content), "", Snackbar.LENGTH_SHORT);
        //inflate view
        View custom_view = mActivity.getLayoutInflater().inflate(R.layout.snackbar_icon_info, null);

        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarView.setPadding(0, 0, 0, 0);

        ((TextView) custom_view.findViewById(R.id.message)).setText(message);
        (custom_view.findViewById(R.id.parent_view)).setBackgroundColor(mActivity.getResources().getColor(R.color.color_info));
        snackBarView.addView(custom_view, 0);
        snackbar.show();
    }

}
