package com.example.hp.zermome;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;

public class Registration extends AppCompatActivity {

    EditText  name,password,phone_no,DOB,location;
    int request=100;
    String  text_name,text_phone,text_password,gender,location_text,DOB_text;
    private FirebaseAuth firebaseAuth;
    StorageReference filepath;
    ImageView profile;





    Calendar myCalendar;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    android.app.AlertDialog waiting_dialog;
    private AwesomeValidation awesomeValidation;

    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        name=(EditText) findViewById(R.id.txtName);
        password=(EditText) findViewById(R.id.txtPwd);
        phone_no=(EditText) findViewById(R.id.txtEmail);
        firebaseAuth=FirebaseAuth.getInstance();
        DOB=(EditText) findViewById(R.id.signup_input_dob) ;
        location=(EditText) findViewById(R.id.location) ;
        profile=(ImageView) findViewById(R.id.profile);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.name, "[A-Z][a-zA-Z]*", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.txtEmail, Patterns.EMAIL_ADDRESS, R.string.emailerror);

        waiting_dialog=new SpotsDialog.Builder()
                .setCancelable(false)
                .setMessage("Please wait")
                .setContext(this)
                .build();


        radioGroup = (RadioGroup) findViewById(R.id.radio);
         myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
       DOB.setKeyListener(null);
        DOB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                   DOB.callOnClick();
                }
            }
        });
       DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Registration.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }

        });

    }

    public void Register(View view) {


        //if this becomes true that means validation is successfull



            //process the data further

            int selectedId = radioGroup.getCheckedRadioButtonId();
            radioButton = (RadioButton) findViewById(selectedId);
            text_name = name.getText().toString();
            text_phone = phone_no.getText().toString();
            text_password = password.getText().toString();
            gender = radioButton.getText().toString();
            location_text = location.getText().toString();
            DOB_text=DOB.getText().toString();
            if(text_name.length()==0){
                name.setError("Please Enter Name");
                name.requestFocus();
            }
            if(text_phone.length()==0){
                phone_no.setError("Please Email ID");
                phone_no.requestFocus();
            }

            if(text_password.length()==0){
               password.setError("Please Enter Password");
                password.requestFocus();
            }
        if(DOB_text.length()==0){
            DOB.setError("Please Enter DOB");

        }
            if(location_text.length()==0){
                location.setError("Please Enter location");
                location.requestFocus();
            }

            else {

                if (awesomeValidation.validate()) {
                waiting_dialog.show();


                firebaseAuth.createUserWithEmailAndPassword(text_phone, text_password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Account created", Toast.LENGTH_LONG);
                            updateuserInfo();
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }


                });
            }
        }



    }
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        DOB.setText(sdf.format(myCalendar.getTime()));
    }
    private void updateuserInfo() {
        final String user_id=firebaseAuth.getCurrentUser().getUid();
        final FirebaseUser current_user=firebaseAuth.getCurrentUser();
        DatabaseReference current_user_db=FirebaseDatabase.getInstance().getReference().child("User_Resgisration").child(user_id);
        Map remaining_info=new HashMap();
        remaining_info.put("name",text_name);
        remaining_info.put("phone_no",text_phone);
        remaining_info.put("password",text_password);
        remaining_info.put("gender",gender);
        remaining_info.put("location",location_text);
        remaining_info.put("DOB",DOB_text);
        remaining_info.put("Occupation","Farmer");
        current_user_db.setValue(remaining_info);
        waiting_dialog.dismiss();
        Toast.makeText(getApplicationContext(), "register complete", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), AccountCreated.class);
        intent.putExtra("message", text_name);
        startActivity(intent);
        if(uri!=null) {
            StorageReference mstorage = FirebaseStorage.getInstance().getReference().child("user_profile");
            filepath = mstorage.child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(text_name).setPhotoUri(uri).build();
                            current_user.updateProfile(profileUpdate)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                waiting_dialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "register complete", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(getApplicationContext(), AccountCreated.class);
                                                intent.putExtra("message", text_name);
                                                startActivity(intent);
                                                overridePendingTransition( R.anim.trans_left_in, R.anim.trans_left_out );
                                                finish();

                                            }
                                        }
                                    });

                        }
                    });
                }
            });
        }


    }

    public void pick_image(View view) {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,request);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==request && resultCode==RESULT_OK)
        {
            uri=data.getData();
            profile.setImageURI(uri);


        }
    }


}
