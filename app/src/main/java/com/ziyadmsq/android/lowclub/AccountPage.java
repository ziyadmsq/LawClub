package com.ziyadmsq.android.lowclub;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itsronald.widget.ViewPagerIndicator;


public class AccountPage extends AppCompatActivity implements MyJoinedEventsFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener {


    private FirebaseAuth mFirebaseAuth;
    public static final int RC_SIGN_IN = 1;
    private MyPagerAdapter myPagerAdapter;
    private Context context = this;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_account);

//        try{
//            if(mFirebaseAuth.getCurrentUser()==null){
//                Intent intent = new Intent(this,LoginActivity.class);
//                startActivity(intent);
//                Log.e("AccountPage","no cuttentUser");
//            }
//        }catch (Exception e ){
//            Intent intent = new Intent(this,LoginActivity.class);
//            startActivity(intent);
//            Log.e("AccountPage","no cuttentUser");
//        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_account);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);


        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference mMessagesDatabaseReference;
        mMessagesDatabaseReference = mFirebaseDatabase.getReference();
        //dont touch
        mFirebaseAuth = FirebaseAuth.getInstance();

        TextView profileName = findViewById(R.id.profile_name);
        profileName.setText(MainActivity.account.getName());
        TextView myPoints = findViewById(R.id.points);
        TextView myJoin = findViewById(R.id.number_events);

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        LinearLayout profileHolder = headerView.findViewById(R.id.profile_holder);
        TextView navName = profileHolder.findViewById(R.id.nav_username);
        TextView navEmail = profileHolder.findViewById(R.id.nav_email);
        if (MainActivity.account != null) {
            navName.setText(MainActivity.account.getName());
            navEmail.setText(MainActivity.account.getKsuId() + "@student.ksu.edu.sa");
        }
//        if(MainActivity.account==null){
//            Log.e("account == null", "sorry");
//        } else {
//            Log.e("account != null", "hmmm so what is wrong with my code..?");
//            Log.e("account != null", String.valueOf(MainActivity.account.getMyJoin().size()));
//        }
        if (MainActivity.account.getMyJoin() != null) {
            myJoin.setText(String.valueOf(MainActivity.account.getMyJoin().size()));
        }

//        mMessagesDatabaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
//                    Log.e("AccountPage","down ");
//                    Log.e(dataSnapshot1.getValue(Account.class).getFirebaseID()," == "+mFirebaseAuth.getCurrentUser().getUid());
//                    if(dataSnapshot1.getValue(Account.class).getFirebaseID().equals(mFirebaseAuth.getCurrentUser().getUid())){
//                        account = dataSnapshot1.getValue(Account.class);
//                        if(account==null){
//                            Log.e("account == null","sorry");
//                        }else{
//                            Log.e("account != null","hmmm so what is wrong with my code..?");
//                            Log.e("account != null", String.valueOf(account.getPoints()));
//                        }
//                        Log.e("ValueEventListener","getFirebaseID()==event.getOnnerID");
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


        final ViewPager.LayoutParams layoutParams = new ViewPager.LayoutParams();
        layoutParams.width = LayoutParams.MATCH_PARENT;
        layoutParams.height = LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.TOP;


        final ViewPager viewPager = findViewById(R.id.viewpager);
        final ViewPagerIndicator viewPagerIndicator = new ViewPagerIndicator(this);
        viewPager.addView(viewPagerIndicator, layoutParams);

        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myPagerAdapter);

        if (MainActivity.account != null) {
            myPoints.setText(String.valueOf(MainActivity.account.getNumOfHours()));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.events:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.web:
                Toast.makeText(this, "قريبا", Toast.LENGTH_SHORT).show();
                break;

            case R.id.email:
//                Intent intent1 = new Intent(Intent.ACTION_SENDTO);
//                intent1.setData(Uri.parse("mailto:")); // only email apps should handle this
//                intent1.putExtra(Intent.EXTRA_EMAIL, "email@LawClub.com");
//                intent1.putExtra(Intent.EXTRA_SUBJECT, "about LawClub app");
//                if (intent1.resolveActivity(getPackageManager()) != null) {
//                    startActivity(intent1);
//                }
                break;

            case R.id.phone:
                Uri uri = Uri.parse("https://wa.me/966541544541");
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivity(i);
                }
                break;

            case R.id.ftc_web:
                Toast.makeText(this, "coming soon", Toast.LENGTH_SHORT).show();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_account);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e("hi changing password", "onOptionsItemSelected.");
        int id = item.getItemId();

        if (id == R.id.sign_out) {

            AuthUI.getInstance().signOut(this);

            FirebaseUser user = mFirebaseAuth.getCurrentUser();
//                Toast.makeText(context,"firebaseAuth.getCurrentUser",Toast.LENGTH_LONG).show();
            Log.e("onAuthStateChanged", "firebaseAuth.getCurrentUser");
//                    Toast.makeText(context,"user == null",Toast.LENGTH_LONG).show();
            Log.e("onAuthStateChanged", "usser == null");
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

//            Intent intent = new Intent(this,LoginActivity.class);
//            startActivity(intent);
            finish();

            return true;
        } //else if (id == R.id.change_pass) {
//            //TODO for changing password
//            Log.e("hi changing password", "if(id == R.id.change_pass)");
//
//            mFirebaseAuth.sendPasswordResetEmail("ziyadmsq@gmial.com")
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(context, "failed to help you :(", Toast.LENGTH_LONG).show();
//                        }
//                    }).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()) {
//                        Log.e("hi changing password", "Email sent.");
//                    }
//                }
//            });
//
//        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
