package com.proyekakhir.pelaporan.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SPManager {
    public static final String SP_LOGIN_APP = "spLoginApp";

    public static final String SP_ID = "spId";
    public static final String SP_NAME = "spName";
    public static final String SP_EMAIL = "spEmail";
    public static final String SP_ADDRESS = "spAddress";
    public static final String SP_PHONE = "spPhone";
    public static final String SP_IMG = "spImg";
    public static final String SP_ROLE = "spRole";

    public static final String SP_IS_SIGNED = "spSignedIn";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SPManager(Context context){
        sp = context.getSharedPreferences(SP_LOGIN_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.apply();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.apply();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.apply();
    }

    public void removeSP(){
        spEditor.remove(SP_ID);
        spEditor.remove(SP_EMAIL);
        spEditor.remove(SP_NAME);
        spEditor.remove(SP_ADDRESS);
        spEditor.remove(SP_PHONE);
        spEditor.remove(SP_IMG);
        spEditor.remove(SP_ROLE);
        spEditor.apply();
    }

    public String getSpID(){
        return sp.getString(SP_ID, "id");
    }

    public String getSpName(){
        return sp.getString(SP_NAME, "LightBrary");
    }

    public String getSpEmail(){
        return sp.getString(SP_EMAIL, "User");
    }

    public String getSpAddress(){
        return sp.getString(SP_ADDRESS, "null");
    }

    public String getSpPhone(){
        return sp.getString(SP_PHONE, "null");
    }

    public String getSpImg(){
        return sp.getString(SP_IMG, "null");
    }

    public int getSpRole(){
        return sp.getInt(SP_ROLE, 0);
    }

    public Boolean getSpIsSigned(){
        return sp.getBoolean(SP_IS_SIGNED, false);
    }

}
