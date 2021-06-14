package com.example.hp.zermome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AccountCreated extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_created);
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");
        message="Hii "+message;
        TextView txtView = (TextView) findViewById(R.id.name);
        txtView.setText(message);
    }

    public void click_login(View view) {

        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);

    }
}
