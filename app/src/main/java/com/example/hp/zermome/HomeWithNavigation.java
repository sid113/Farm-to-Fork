package com.example.hp.zermome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hp.zermome.Fragement.FarmerCrop;
import com.example.hp.zermome.Fragement.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class HomeWithNavigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SharedprefrenceConfig sharedprefrenceConfig;
    android.app.AlertDialog waiting_dialog;
    FirebaseUser CurrentUser;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_with_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        waiting_dialog=new SpotsDialog.Builder()
                .setCancelable(false)
                .setMessage("Please wait")
                .setContext(this)
                .build();
        setSupportActionBar(toolbar);
        mAuth=FirebaseAuth.getInstance();
        CurrentUser=mAuth.getCurrentUser();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        updateNavHeader();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_with_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        }

         else if (id == R.id.nav_profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
            overridePendingTransition( R.anim.trans_left_in, R.anim.trans_left_out );

        } else if (id == R.id.Add_Crop) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new FarmerCrop()).commit();
            overridePendingTransition( R.anim.trans_left_in, R.anim.trans_left_out );


        } else if (id == R.id.nav_share) {

        } else if (id == R.id.Log_Out) {
            waiting_dialog.show();
            sharedprefrenceConfig=new SharedprefrenceConfig(this);
            sharedprefrenceConfig.writeLoginStatus(false);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent homeintent=new Intent(HomeWithNavigation.this,LoginActivity.class);
                    startActivity(homeintent);
                    waiting_dialog.dismiss();
                    finish();
                }
            },3000);




        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void updateNavHeader()
    {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View HeaderView=navigationView.getHeaderView(0);
        TextView navusername=HeaderView.findViewById(R.id.name);
        TextView navemailid=HeaderView.findViewById(R.id.email_id);
      CircleImageView   navUserPhoto=HeaderView.findViewById(R.id.user_profile_photo);
        navemailid.setText(CurrentUser.getEmail());
        navusername.setText(CurrentUser.getDisplayName());
        Glide.with(this).load(CurrentUser.getPhotoUrl()).into(navUserPhoto);


    }
}
