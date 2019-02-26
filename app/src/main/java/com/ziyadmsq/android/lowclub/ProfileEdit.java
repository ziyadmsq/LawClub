package com.ziyadmsq.android.lowclub;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileEdit extends android.support.v4.app.Fragment {

    private EditText editName, editPhone;
    private Button editButton, resetPassButton;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private FirebaseAuth auth;
    private Context context;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile_edit, container, false);
        context = getContext();

//        initiateComponents();
        editName = view.findViewById(R.id.editName);
        editPhone = view.findViewById(R.id.editPhone);
        editButton = view.findViewById(R.id.editButton);
        resetPassButton = view.findViewById(R.id.reset_password);
        editName.setFocusableInTouchMode(true);
        editPhone.setFocusableInTouchMode(true);

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

        resetPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (context != null) {
                    editPhone.setFocusable(false);
                    editName.setFocusable(false);
                    final EditText oldpass = new EditText(context);
                    final EditText newPass = new EditText(context);
                    final EditText sure = new EditText(context);
                    oldpass.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);
                    oldpass.setHint("كلمة المرور الحالية");
                    newPass.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);
                    newPass.setHint("كلمة المرور الجديدة");
                    sure.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    sure.setHint("تأكيد كلمة المرور الجديدة");
                    final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(context);
                    alertDialog2.setTitle("تغيير كلمة المرور");
//                    alertDialog2.setMessage("Are you sure you want delete this file?");
                    LinearLayout container = new LinearLayout(context);
                    container.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    oldpass.setLayoutParams(lp);
                    newPass.setLayoutParams(lp);
                    sure.setLayoutParams(lp);
                    container.setPadding(50, 0, 50, 0);
                    container.addView(oldpass, lp);
                    container.addView(newPass, lp);
                    container.addView(sure, lp);
                    alertDialog2.setView(container);
                    alertDialog2.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog
                                    editPhone.setFocusableInTouchMode(true);
                                    editPhone.setFocusable(true);
                                    editName.setFocusable(true);
                                    editName.setFocusableInTouchMode(true);
                                    if (newPass.getText().toString().equals(sure.getText().toString())) {
                                        final ProgressDialog progressDialog = new ProgressDialog(context);
                                        progressDialog.setMessage("جاري العمل .. ");
//                                        progressDialog.setIcon(R.mipmap.ic_launcher);
                                        progressDialog.show();
                                        Log.e("not null?", MainActivity.account.getKsuId() + "@student.ksu.edu.sa");
                                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        AuthCredential credential = EmailAuthProvider
                                                .getCredential(String.valueOf(MainActivity.account.getKsuId()) + "@student.ksu.edu.sa", oldpass.getText().toString());
                                        user.reauthenticate(credential)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            user.updatePassword(newPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
//                                                                        mFirebaseAuth = FirebaseAuth.getInstance();
                                                                        mFirebaseDatabase = FirebaseDatabase.getInstance();
                                                                        mMessagesDatabaseReference = mFirebaseDatabase.getReference();
                                                                        mMessagesDatabaseReference.child(MainActivity.ACCOUNT_TREE).child(MainActivity.account.getFirebaseID()).child("pass").setValue(newPass.getText().toString())
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {
                                                                                        MainActivity.account.setPassWord(newPass.getText().toString());
                                                                                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                                                        //Find the currently focused view, so we can grab the correct window token from it.
                                                                                        View view = getActivity().getCurrentFocus();
                                                                                        //If no view currently has focus, create a new one, just so we can grab a window token from it
                                                                                        if (view == null) {
                                                                                            view = new View(getActivity());
                                                                                        }
                                                                                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                                                                        Snackbar mSnackbar = Snackbar.make(view, "زبطناك يا وحش \uD83D\uDDDD", Snackbar.LENGTH_LONG);
                                                                                        View mView = mSnackbar.getView();
                                                                                        TextView mTextView = (TextView) mView.findViewById(android.support.design.R.id.snackbar_text);
                                                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                                                                            mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                                                                        } else {
                                                                                            mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                                                                                        }
                                                                                        mSnackbar.show();

                                                                                    }
                                                                                });
                                                                        mMessagesDatabaseReference.keepSynced(true);
                                                                        progressDialog.dismiss();
                                                                    } else {
                                                                        progressDialog.dismiss();
                                                                    }
                                                                }
                                                            });
                                                        } else {
                                                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                            //Find the currently focused view, so we can grab the correct window token from it.
                                                            View view = getActivity().getCurrentFocus();
                                                            //If no view currently has focus, create a new one, just so we can grab a window token from it
                                                            if (view == null) {
                                                                view = new View(getActivity());
                                                            }
                                                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                                            Snackbar mSnackbar = Snackbar.make(view, "مشكلة في كلمة المرور الحالية .. اعد المحاولة", Snackbar.LENGTH_LONG);
                                                            View mView = mSnackbar.getView();
                                                            TextView mTextView = (TextView) mView.findViewById(android.support.design.R.id.snackbar_text);
                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                                                mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                                            } else {
                                                                mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                                                            }
                                                            mSnackbar.show();
                                                            progressDialog.dismiss();
                                                        }
                                                    }
                                                });
                                    } else {
                                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        //Find the currently focused view, so we can grab the correct window token from it.
                                        View view = getActivity().getCurrentFocus();
                                        //If no view currently has focus, create a new one, just so we can grab a window token from it
                                        if (view == null) {
                                            view = new View(getActivity());
                                        }
                                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                        Snackbar mSnackbar = Snackbar.make(view, "كلمة المرور لا تتشابه", Snackbar.LENGTH_LONG);
//                                        Snackbar mSnackbar = Snackbar.make(view, "شوية تركيز .. كلمة المرور ما تتشابه الله يصلحك\uD83E\uDD26\uD83C\uDFFB\u200D♂️\uD83C\uDF88", Snackbar.LENGTH_LONG);
                                        View mView = mSnackbar.getView();
                                        TextView mTextView = (TextView) mView.findViewById(android.support.design.R.id.snackbar_text);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                            mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        } else {
                                            mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                                        }
                                        editPhone.setFocusableInTouchMode(true);
                                        editPhone.setFocusable(true);
                                        editName.setFocusable(true);
                                        editName.setFocusableInTouchMode(true);
                                        mSnackbar.show();

                                    }
                                }
                            });
                    alertDialog2.setNegativeButton("NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Write your code here to execute after dialog
                                    InputMethodManager imm1 = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                                    //Find the currently focused view, so we can grab the correct window token from it.
                                    View view1 = getActivity().getCurrentFocus();
                                    //If no view currently has focus, create a new one, just so we can grab a window token from it
                                    if (view1 == null) {
                                        view1 = new View(getActivity());
                                    }
                                    imm1.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                                    dialog.cancel();
                                    CountDownTimer countDownTimer = new CountDownTimer(500, 1000) {

                                        public void onTick(long millisUntilFinished) {

                                        }

                                        public void onFinish() {
                                            editPhone.setFocusableInTouchMode(true);
                                            editPhone.setFocusable(true);
                                            editName.setFocusable(true);
                                            editName.setFocusableInTouchMode(true);
                                        }

                                    };
                                    countDownTimer.start();
                                }
                            });
                    alertDialog2.show();
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //Get Firebase auth instance
                final ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("جاري العمل .. ");
                progressDialog.show();
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
                        } else {
                            Snackbar mSnackbar = Snackbar.make(view, "المعلومات معدلة مسبقاً", Snackbar.LENGTH_LONG);
                            View mView = mSnackbar.getView();
                            TextView mTextView = (TextView) mView.findViewById(android.support.design.R.id.snackbar_text);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            } else {
                                mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                            }
                            mSnackbar.show();
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressDialog.dismiss();
                    }
                });
            }
        });

        return view;
    }
}
