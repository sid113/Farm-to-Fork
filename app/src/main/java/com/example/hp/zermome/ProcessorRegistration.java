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

public class ProcessorRegistration extends AppCompatActivity {





    EditText name, password, phone_no, contact_no, location;
    int request = 100;
    String text_name, text_phone, text_password, gender, location_text, DOB_text, contact_no_text;
    private FirebaseAuth firebaseAuth;
    StorageReference filepath;
    ImageView profile;
    Calendar myCalendar;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    android.app.AlertDialog waiting_dialog;
    private AwesomeValidation awesomeValidation;

    Uri uri;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processor_registration);
        name = (EditText) findViewById(R.id.txtName);
        password = (EditText) findViewById(R.id.txtPwd);
        phone_no = (EditText) findViewById(R.id.txtEmail);
        firebaseAuth = FirebaseAuth.getInstance();
        contact_no = (EditText) findViewById(R.id.Contact_No);
        location = (EditText) findViewById(R.id.location);
        profile = (ImageView) findViewById(R.id.profile);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        waiting_dialog = new SpotsDialog.Builder()
                .setCancelable(false)
                .setMessage("Please wait")
                .setContext(this)
                .build();

        radioGroup = (RadioGroup) findViewById(R.id.radio);
        awesomeValidation.addValidation(this, R.id.Contact_No, "[1-9][0-9]{9}", R.string.mobileerror);

    }

    public void Register(View view) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);
        text_name = name.getText().toString();
        text_phone = phone_no.getText().toString();
        text_password = password.getText().toString();
        gender = radioButton.getText().toString();
        location_text = location.getText().toString();
        contact_no_text=contact_no.getText().toString();
        if (text_name.length() == 0) {
            name.setError("Please Enter Name");
            name.requestFocus();
        }
        if (text_phone.length() == 0) {
            phone_no.setError("Please Email ID");
            phone_no.requestFocus();
        }

        if (text_password.length() == 0) {
            password.setError("Please Enter Password");
            password.requestFocus();
        }

        if (location_text.length() == 0) {
            location.setError("Please Enter location");
            location.requestFocus();
        }
        if (contact_no_text.length() == 0) {
            contact_no.setError("Please Enter contact No");
            contact_no.requestFocus();
        }else {

            if (awesomeValidation.validate()) {

                waiting_dialog.show();
                firebaseAuth.createUserWithEmailAndPassword(text_phone, text_password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            waiting_dialog.dismiss();

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
        remaining_info.put("contact_no",contact_no_text);
        remaining_info.put("Occupation","Processor");
        current_user_db.setValue(remaining_info);
        current_user_db.setValue(remaining_info);
        waiting_dialog.dismiss();
        Toast.makeText(getApplicationContext(), "register complete", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), Account_created.class);
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
                                                Intent intent = new Intent(getApplicationContext(), Account_created.class);;
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
