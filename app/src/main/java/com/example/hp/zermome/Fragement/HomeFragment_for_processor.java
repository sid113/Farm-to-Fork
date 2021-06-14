package com.example.hp.zermome.Fragement;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hp.zermome.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment_for_processor extends Fragment {

    TextView name,DOB,address,email_address,location,gender,another_location;
    CircleImageView profile;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;




    public HomeFragment_for_processor(){

    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        name=(TextView) view.findViewById(R.id.name);
        DOB=(TextView) view.findViewById(R.id.dob);
        address=(TextView) view.findViewById(R.id.location);
        profile=(CircleImageView) view.findViewById(R.id.profile);
        email_address=(TextView)view.findViewById(R.id.email);
        location=(TextView)view.findViewById(R.id.location);
        gender=(TextView)view.findViewById(R.id.gender);
        another_location=(TextView)view.findViewById(R.id.marriage);
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        final String user_id=mAuth.getCurrentUser().getUid();
        email_address.setText(currentUser.getEmail());
        name.setText(currentUser.getDisplayName());
        Glide.with(this).load(currentUser.getPhotoUrl()).into(profile);
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference(mAuth.getUid());
        DatabaseReference current_user_db=FirebaseDatabase.getInstance().getReference().child("processor_reg").child(user_id);
        current_user_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("name").getValue().toString());
                email_address.setText(dataSnapshot.child("phone_no").getValue().toString());
                location.setText(dataSnapshot.child("location").getValue().toString());
                gender.setText(dataSnapshot.child("gender").getValue().toString());
                DOB.setText(dataSnapshot.child("contact_no").getValue().toString());
                another_location.setText(dataSnapshot.child("location").getValue().toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return view;
    }




}
