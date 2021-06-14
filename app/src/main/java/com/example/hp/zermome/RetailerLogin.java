package com.example.hp.zermome;



import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hp.zermome.Fragement.HomeFragment_for_processor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dmax.dialog.SpotsDialog;

public class RetailerLogin extends AppCompatActivity {
    EditText email,password;
    Button login;
    FirebaseAuth firebaseAuth;
    String text_email,password_text;
    Intent home_intent;
    private SharedprefrenceConfig sharedprefrenceConfig;
    android.app.AlertDialog waiting_dialog;
    String message,occupation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        waiting_dialog=new SpotsDialog.Builder()
                .setCancelable(false)
                .setMessage("Please wait")
                .setContext(this)
                .build();


        email=(EditText)findViewById(R.id.email);
        password=(EditText) findViewById(R.id.password);
        login=(Button) findViewById(R.id.login);
        firebaseAuth=FirebaseAuth.getInstance();
        home_intent=new Intent(this,For_Processor.class);
        sharedprefrenceConfig=new SharedprefrenceConfig(this);
        if(sharedprefrenceConfig.readLoginStatus())
        {
            startActivity(home_intent);
            finish();
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waiting_dialog.show();
                userlogin();
            }
        });
    }
    void userlogin()
    {
        text_email=email.getText().toString();
        password_text=password.getText().toString();
        firebaseAuth.signInWithEmailAndPassword(text_email,password_text).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    decide();
                }
                else
                {
                    waiting_dialog.dismiss();
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void register_open(View view) {


        Intent intent = new Intent(this, ProcessorRegistration.class);
        startActivity(intent);
        this.overridePendingTransition( R.anim.trans_left_in, R.anim.trans_left_out );
        finish();

    }
    private void decide() {
        FirebaseUser currentUser=firebaseAuth.getCurrentUser();
        final String user_id=currentUser.getUid();
        DatabaseReference current_user_db=FirebaseDatabase.getInstance().getReference().child("User_Resgisration").child(user_id);
        current_user_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                occupation=dataSnapshot.child("Occupation").getValue().toString();
                if(occupation.equals("Processor"))
                {
                    waiting_dialog.dismiss();
                    sharedprefrenceConfig.writeLoginStatus(true);
                    startActivity(home_intent);
                    finish();


                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please Enter Valid email id",Toast.LENGTH_LONG).show();
                    waiting_dialog.dismiss();

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
