package com.example.hp.zermome;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class selectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
    }

    public void farmerField(View view) {
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
       this.overridePendingTransition( R.anim.trans_left_in, R.anim.trans_left_out );
        finish();
    }

    public void customer_field(View view) {

        Intent intent=new Intent(this,ScannerActivity.class);
        startActivity(intent);
        this.overridePendingTransition( R.anim.trans_left_in, R.anim.trans_left_out );
        finish();
    }

    public void processor(View view) {
        Intent intent=new Intent(this,RetailerLogin.class);
        startActivity(intent);
        this.overridePendingTransition( R.anim.trans_left_in, R.anim.trans_left_out );

        finish();

    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                      selectionActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
