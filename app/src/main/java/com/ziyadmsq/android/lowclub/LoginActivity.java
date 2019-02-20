package com.ziyadmsq.android.lowclub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputName;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Context context = this;
    private Button btnLogin, btnReset;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        Log.e("onCreate LoginActivity", "yess so hmmmm");
        if (auth.getCurrentUser() != null) {
            Log.e("Loged in ?", "yess so hmmmm");

            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

//        auth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null){
                    Log.e("Loged in ?", "yess so hmmmm");

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
                else{
                    System.out.println("User not logged in");
                }
            }
        };

        // set the view now
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
//        inputName = (EditText) findViewById(R.id.name);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

//        btnSignup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
//            }
//        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "كلم المشرف ويزبطك", Toast.LENGTH_LONG).show();
//                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();
//                final String name = inputName.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email + "@student.ksu.edu.sa", password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(LoginActivity.this, "اما موب مسجل ك عضو او كلمة المرور فيه مشكلة", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    mMessagesDatabaseReference = mFirebaseDatabase.getReference().child(MainActivity.ACCOUNT_TREE);
                                    mMessagesDatabaseReference.keepSynced(true);
                                    mMessagesDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                Log.e("AccountPage", "down ");
                                                if (dataSnapshot1.getValue(Account.class) != null && auth.getCurrentUser().getUid() != null) {
                                                    if (dataSnapshot1.getValue(Account.class).getFirebaseID().equals(auth.getCurrentUser().getUid())) {
                                                        MainActivity.account = dataSnapshot1.getValue(Account.class);
//                                                        MainActivity.account.setName(name);
//                                                        mFirebaseDatabase.getReference().child(MainActivity.ACCOUNT_TREE).child(auth.getCurrentUser().getUid()).child("name").setValue(name);
//                                                        mFirebaseDatabase.setPersistenceEnabled(true);
                                                        Log.e(".onDataChange","");
                                                        break;
                                                    }
                                                }
                                            }
                                            if (MainActivity.account == null) {
                                                Log.e(".account == null","");
                                                FirebaseUser currentUser = auth.getCurrentUser();
                                                Account account = new Account(currentUser.getUid(), true, "$", "user", "", 0, "", Integer.parseInt(email), password, null, null);
                                                mFirebaseDatabase.getReference().child(MainActivity.ACCOUNT_TREE).child(currentUser.getUid()).setValue(account.toMap());
                                            }
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();

                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

//                                    if (MainActivity.account == null) {
//                                        Log.e(".account == null","");
//                                        FirebaseUser currentUser = auth.getCurrentUser();
//                                        Account account = new Account(currentUser.getUid(), true, "$", "user", "", 0, "", Integer.parseInt(email), password, null, null);
//                                        mFirebaseDatabase.getReference().child(MainActivity.ACCOUNT_TREE).child(currentUser.getUid()).setValue(account.toMap());
//                                    }
//                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                    startActivity(intent);
//                                    finish();
                                }
                            }
                        });
            }
        });
    }
}