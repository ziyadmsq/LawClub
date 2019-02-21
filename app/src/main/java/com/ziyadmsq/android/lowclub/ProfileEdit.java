package com.ziyadmsq.android.lowclub;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileEdit extends android.support.v4.app.Fragment {

    private EditText editName;
    private EditText editPhone;
    private Button editButton;
    private Button resetPassButton;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private FirebaseAuth auth;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile_edit, container, false);

//        initiateComponents();
        editName = view.findViewById(R.id.editName);
        editPhone = view.findViewById(R.id.editPhone);
        editButton = view.findViewById(R.id.editButton);
        resetPassButton = view.findViewById(R.id.reset_password);

        if (MainActivity.account.getName().equals("")) {
            editName.setHint("يرجى ادخال الاسم");
        } else {
            editName.setHint(MainActivity.account.getName());
        }
        if (MainActivity.account.getPhone().equals("")) {
            editPhone.setHint("يرجى ادخال الرقم");
        } else {
            editPhone.setHint(MainActivity.account.getPhone());
        }


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //Get Firebase auth instance
                auth = FirebaseAuth.getInstance();
                mFirebaseDatabase = FirebaseDatabase.getInstance();
                mMessagesDatabaseReference = mFirebaseDatabase.getReference().child(MainActivity.ACCOUNT_TREE).child(MainActivity.account.getFirebaseID());
                mMessagesDatabaseReference.keepSynced(true);
                mMessagesDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String text;
                        boolean flag = false;
                        if (!editName.getText().toString().equals("")) {
                            text = editName.getText().toString();
                            mFirebaseDatabase.getReference().child(MainActivity.ACCOUNT_TREE).child(MainActivity.account.getFirebaseID()).child("name").setValue(text);
                            MainActivity.account.setName(editName.getText().toString());
                            editName.setText("");
                            editName.setHint(text);
                            flag = true;
                        }
                        if (!editPhone.getText().toString().equals("")) {
                            text = editPhone.getText().toString();
                            mFirebaseDatabase.getReference().child(MainActivity.ACCOUNT_TREE).child(MainActivity.account.getFirebaseID()).child("phone").setValue(text);
                            MainActivity.account.setPhone(editPhone.getText().toString());
                            editPhone.setText("");
                            editPhone.setHint(text);
                            flag = true;
                        }
                        if (flag) {
                            Snackbar mSnackbar = Snackbar.make(view, "تم تعديل معلوماتك \uD83C\uDF88\uD83D\uDC4C\uD83C\uDFFB", Snackbar.LENGTH_LONG);
                            View mView = mSnackbar.getView();
                            TextView mTextView = (TextView) mView.findViewById(android.support.design.R.id.snackbar_text);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            } else {
                                mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                            }
                            mSnackbar.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


        return view;
    }

   /* private ProfileEdit context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        editButton = findViewById(R.id.editButton);
        resetPassButton = findViewById(R.id.reset_password);

        if (MainActivity.account.getName().equals("")) {
            editName.setHint("يرجى ادخال الاسم");
        } else {
            editName.setHint(MainActivity.account.getName());
        }
        if (MainActivity.account.getPhone().equals("")) {
            editPhone.setHint("يرجى ادخال الرقم");
        } else {
            editPhone.setHint(MainActivity.account.getPhone());
        }


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //Get Firebase auth instance
                auth = FirebaseAuth.getInstance();
                mFirebaseDatabase = FirebaseDatabase.getInstance();
                mMessagesDatabaseReference = mFirebaseDatabase.getReference().child(MainActivity.ACCOUNT_TREE).child(MainActivity.account.getFirebaseID());
                mMessagesDatabaseReference.keepSynced(true);
                mMessagesDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String text;
                        boolean flag = false;
                        if (!editName.getText().toString().equals("")) {
                            text = editName.getText().toString();
                            mFirebaseDatabase.getReference().child(MainActivity.ACCOUNT_TREE).child(MainActivity.account.getFirebaseID()).child("name").setValue(text);
                            MainActivity.account.setName(editName.getText().toString());
                            editName.setText("");
                            editName.setHint(text);
                            flag = true;
                        }
                        if (!editPhone.getText().toString().equals("")) {
                            text = editPhone.getText().toString();
                            mFirebaseDatabase.getReference().child(MainActivity.ACCOUNT_TREE).child(MainActivity.account.getFirebaseID()).child("phone").setValue(text);
                            MainActivity.account.setPhone(editPhone.getText().toString());
                            editPhone.setText("");
                            editPhone.setHint(text);
                            flag = true;
                        }
                        if (flag) {
                            Snackbar mSnackbar = Snackbar.make(view, "تم تعديل معلوماتك \uD83C\uDF88\uD83D\uDC4C\uD83C\uDFFB", Snackbar.LENGTH_LONG);
                            View mView = mSnackbar.getView();
                            TextView mTextView = (TextView) mView.findViewById(android.support.design.R.id.snackbar_text);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            } else {
                                mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                            }
                            mSnackbar.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });*/

//        initiateComponents();
//
//        editName.setHint(MainActivity.account.getName());
//        editPhone.setHint(MainActivity.account.getPhone());
//
//        editButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mMessagesDatabaseReference = mFirebaseDatabase.getReference().child(MainActivity.ACCOUNT_TREE).child(MainActivity.account.getFirebaseID());
//                mMessagesDatabaseReference.keepSynced(true);
//                mMessagesDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        mFirebaseDatabase.getReference().child(MainActivity.ACCOUNT_TREE).child(MainActivity.account.getFirebaseID()).child("name").setValue(editName.getText());
//                        MainActivity.account.setName(editName.getText().toString());
//                        editName.setHint(editName.getText());
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//            }
//        });


//    }

    private void initiateComponents() {
//        editName = findViewById(R.id.editName);
//        editPhone = findViewById(R.id.editPhone);
//        editButton = findViewById(R.id.editButton);
//        resetPassButton = findViewById(R.id.reset_password);
    }

}
