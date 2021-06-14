package com.example.hp.zermome;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;

public class SharedprefrenceConfig {
    private SharedPreferences sharedPreferences;
    private Context context;

    public SharedprefrenceConfig(Context context) {
        this.context=context;
        sharedPreferences=context.getSharedPreferences(context.getResources().getString(R.string.login_preference),Context.MODE_PRIVATE);
    }
    public void writeLoginStatus(boolean status)
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(context.getResources().getString(R.string.login_status),status);
        editor.commit();
    }
    public boolean readLoginStatus()
    {
        boolean status=false;
        status=sharedPreferences.getBoolean(context.getResources().getString(R.string.login_status),false);
        return  status;
    }
}
