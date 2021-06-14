package com.example.hp.zermome.Fragement;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import com.example.hp.zermome.Batch_no;
import com.example.hp.zermome.R;
import com.example.hp.zermome.SplashScreeenActivity;
import com.example.hp.zermome.WelcomeActivity;

import dmax.dialog.SpotsDialog;


public class FarmerCrop extends Fragment {

    EditText e11, e22, e33, e44, e55, e66, e77;
    private static final String CHARS = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ234567890";
    android.app.AlertDialog waiting_dialog;
    Button post;
    StringBuilder token;
    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    List<String> imagesEncodedList;
    private LinearLayout parentLinearLayout;
    Button add;
    View view;
    ArrayList<String> list;


    public FarmerCrop() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_farmer_crop, container, false);

        e11 = view.findViewById(R.id.error1);
        e22 = view.findViewById(R.id.e2);
        e55 = view.findViewById(R.id.e5);
        e66 = view.findViewById(R.id.e6);
        e77 = view.findViewById(R.id.e7);
        add = view.findViewById(R.id.add_field_button);
        post = view.findViewById(R.id.button3);
        waiting_dialog = new SpotsDialog.Builder()
                .setCancelable(false)
                .setMessage("Please wait")
                .setContext(getContext())
                .build();


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
        parentLinearLayout = view.findViewById(R.id.parent_linear_layout);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.field, null);
                //Toast.makeText(getContext(),get.getText().toString(),Toast.LENGTH_LONG).show();


                // Add the new row before the add field button.
                Button delete = rowView.findViewById(R.id.delete_button);
                final EditText textOut = (EditText) rowView.findViewById(R.id.number_edit_text);


                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        parentLinearLayout.removeView((View) view.getParent());

                    }
                });
                parentLinearLayout.addView(rowView, 0);
                String text = textOut.getText().toString();
                Toast.makeText(getContext(),text,Toast.LENGTH_LONG).show();
               // parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);


            }
        });
        // add.callOnClick();
        return view;

    }

    public void submit() {

        if (e11.getText().toString().length() == 0) {
            e11.setError("Field can not be left blank");
            e11.requestFocus();
        }
        if (e22.getText().length() == 0) {
            e22.setError("Field can not be left blank");
            e22.requestFocus();
        }

        if (e55.getText().length() == 0) {
            e55.setError("Field can not be left blank");
            e55.requestFocus();
        }
        if (e66.getText().length() == 0) {
            e66.setError("Field can not be left blank");
            e66.requestFocus();
        }
        if (e77.getText().length() == 0) {
            e77.setError("Field can not be left blank");
            e77.requestFocus();
        } else {
            String crop_name_text = e11.getText().toString();
            String crop_quantity_text = e22.getText().toString();
            String chemical_used = e55.getText().toString();
            String chemical_quantity = e66.getText().toString();
            String sold_to = e77.getText().toString();
            getEditTextList();
           // Toast.makeText(getContext(), list.get(0 ), Toast.LENGTH_LONG).show();
            getToken();
            waiting_dialog.show();


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), token.toString(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), Batch_no.class);
                    intent.putExtra("message", token.toString());
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                }
            }, 3000);


        }

    }


    public void getToken() {
        Random r = new Random();
        token = new StringBuilder(7);
        for (int i = 0; i < 7; i++) {
            token.append(CHARS.charAt(r.nextInt(CHARS.length())));
        }

    }


    public void getimages(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selectl" +
                " Picture"), 1);
    }

    void getEditTextList() {

        int childCount = parentLinearLayout.getChildCount();

        String childTextViewText = null;
        for(int c=0; c<childCount; c++){
            View childView = parentLinearLayout.getChildAt(c);
            EditText childTextView = (EditText) (childView.findViewById(R.id.number_edit_text));
          childTextViewText = (childTextView.getText().toString());
        }
        Toast.makeText(getContext(),childTextViewText,Toast.LENGTH_LONG).show();
        }


    }

