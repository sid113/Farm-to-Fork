package com.example.hp.zermome;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {
    TextView name,DOB,address,email_address,location,gender,another_location;
    CircleImageView profile;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        name=(TextView) findViewById(R.id.name);
        DOB=(TextView) findViewById(R.id.dob);
        address=(TextView) findViewById(R.id.location);
        profile=(CircleImageView) findViewById(R.id.profile);
        email_address=(TextView) findViewById(R.id.email);
        location=(TextView) findViewById(R.id.location);
        gender=(TextView) findViewById(R.id.gender);
       another_location=(TextView) findViewById(R.id.marriage);
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        final String user_id=mAuth.getCurrentUser().getUid();
        email_address.setText(currentUser.getEmail());
        name.setText(currentUser.getDisplayName());
        Glide.with(this).load(currentUser.getPhotoUrl()).into(profile);
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference(mAuth.getUid());
        DatabaseReference current_user_db=FirebaseDatabase.getInstance().getReference().child("farmer_reg").child(user_id);
        current_user_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("name").getValue().toString());
               email_address.setText(dataSnapshot.child("phone_no").getValue().toString());
                location.setText(dataSnapshot.child("location").getValue().toString());
                gender.setText(dataSnapshot.child("gender").getValue().toString());
                DOB.setText(dataSnapshot.child("DOB").getValue().toString());
                another_location.setText(dataSnapshot.child("location").getValue().toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}
