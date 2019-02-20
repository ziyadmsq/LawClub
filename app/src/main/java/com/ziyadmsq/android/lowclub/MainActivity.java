package com.ziyadmsq.android.lowclub;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static final int RC_SIGN_IN = 1;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private com.ziyadmsq.android.lowclub.EventAdapter mEventAdapter;
    private ListView mEventListView;
    private ChildEventListener mChildEventListener;
    MainActivity contextNavigationView;
    private LinearLayout navHeader;
    private String username;
    private Context context = this;
    public static Account account;
    public static String EVENT_TREE = "Events";
    public static String ACCOUNT_TREE = "Accounts";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        findViewById(R.id.fab).setVisibility(View.GONE);

        //TODO : when you need to add the admin sign in
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "the adding option will be added later on", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                Intent intent = new Intent(MainActivity.this,Events.class);
//                startActivity(intent);
//            }
//        });
        navHeader = findViewById(R.id.nav_header);

        // Initialize Firebase components
        initializeFirebaseComponents();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
            Log.e("MainActivity", "no currentUser");
        }
        final Context context = this;
        Log.e("onCreate()", "above mAuthStateListener");
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    onSignedInInitialize(user.getDisplayName());

                    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                    navigationView.setNavigationItemSelectedListener(contextNavigationView);

                    View headerView = navigationView.getHeaderView(0);
                    LinearLayout profileHolder = headerView.findViewById(R.id.profile_holder);
                    TextView navName = profileHolder.findViewById(R.id.nav_username);
                    TextView navEmail = profileHolder.findViewById(R.id.nav_email);
                    if (account != null) {
                        navName.setText(account.getName());
                        navEmail.setText(account.getKsuId() + "@student.ksu.edu.sa");
                    }

//                    Toast.makeText(context,user.getDisplayName(),Toast.LENGTH_LONG).show();

//                    findViewById(R.id.fab).setVisibility(View.VISIBLE);
//                    Toast.makeText(MainActivity.this,"User is signed in\n" +
//                            "                    Toast ",Toast.LENGTH_LONG).show();
//                    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//                    fab.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Snackbar.make(view, "the adding option will be added later on", Snackbar.LENGTH_LONG)
//                                    .setAction("Action", null).show();
//                        }
//                    });

                } else {
                    // User is signed out
//                    findViewById(R.id.fab).setVisibility(View.VISIBLE);

                    onSignedOutCleanup();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                    startActivityForResult(intent,RC_SIGN_IN);
                    startActivity(intent);
                    finish();
                }

            }
        };

        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child(EVENT_TREE);
        mMessagesDatabaseReference.keepSynced(true);

        mMessagesDatabaseReference.getParent().child(ACCOUNT_TREE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                    Log.e("AccountPage", "down ");
//                    Log.e(dataSnapshot1.getValue(Account.class).getFirebaseID(), " == " + mFirebaseAuth.getCurrentUser().getUid());
                    if (dataSnapshot1.getValue(Account.class) != null && dataSnapshot1.getValue(Account.class).getFirebaseID() != null && mFirebaseAuth.getCurrentUser().getUid() != null) {
                        if (dataSnapshot1.getValue(Account.class).getFirebaseID().equals(mFirebaseAuth.getCurrentUser().getUid())) {
                            account = dataSnapshot1.getValue(Account.class);
                            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                            navigationView.setNavigationItemSelectedListener(contextNavigationView);

                            View headerView = navigationView.getHeaderView(0);
                            LinearLayout profileHolder = headerView.findViewById(R.id.profile_holder);
                            TextView navName = profileHolder.findViewById(R.id.nav_username);
                            TextView navEmail = profileHolder.findViewById(R.id.nav_email);
                            if (account != null) {
                                navName.setText(account.getName());
                                navEmail.setText(account.getKsuId() + "@student.ksu.edu.sa");
                            }
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mEventListView = (ListView) findViewById(R.id.EventListView);
        List<Event> eventList = new ArrayList<>();
//        TODO : change me .. changed
        mEventAdapter = new com.ziyadmsq.android.lowclub.EventAdapter(this, R.layout.item_event, eventList);
//        mEventAdapter = new MyEventRecyclerViewAdapter(eventList,this);

        mEventListView.setAdapter(mEventAdapter);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        ImageView ftcImageView = findViewById(R.id.ftc_web);

        ftcImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri webpage = Uri.parse("https://www.ftcksu.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(contextNavigationView = this);

        View headerView = navigationView.getHeaderView(0);
        LinearLayout profileHolder = headerView.findViewById(R.id.profile_holder);
        profileHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("mainActivity", "profileHolder.setOnClickListener");
                Intent intent = new Intent(MainActivity.this, AccountPage.class);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);
            }
        });


    }

    /**
     * on here ill develop all about firebase authintication
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            } else if (false /** for now cuz i think that's only for photos */) {
                /**} else if (requestCode == RC_RECORD && resultCode == RESULT_OK) {*/
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        mEventAdapter.clear();
        detachDatabaseReadListener();
    }

    private void onSignedOutCleanup() {
        username = "anonymous";
        mEventAdapter.clear();
        detachDatabaseReadListener();
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mMessagesDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    private void onSignedInInitialize(String username) {
        this.username = username;
        attachDatabaseReadListener();

    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
//                    final Event friendlyMessage = dataSnapshot.getValue(Event.class);
//                    mEventAdapter.add(friendlyMessage);

                    mEventListView.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
//                                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
//                                    Log.e("datasnapshot","=====" + dataSnapshot.getValue(Event.class).getOnnerID());
//                                }
//                                Log.e("datasnapshot","=====" );

                                Event event = dataSnapshot.getValue(Event.class);
                                Log.e("dataSnapshot.getValue", event.getID());
                                mEventAdapter.add(event);
//                            TODO change me
//                            mEventAdapter.mValues.add(friendlyMessage);
//                                mEventAdapter.notifyDataSetChanged();
//                                mEventListView.smoothScrollToPosition(0);
                            } catch (Exception e) {
                                Log.e("dataSnapshot.getValue", "hmmmm problem");
//                                Toast.makeText(context,"Error Uploading some Events",Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mEventAdapter.notifyDataSetChanged();
            mEventListView.smoothScrollToPosition(0);
            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void initializeFirebaseComponents() {
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        mFirebaseStorage = FirebaseStorage.getInstance();
//        mFunctions = FirebaseFunctions.getInstance();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            AuthUI.getInstance().signOut(this);
////            Event event = new Event("im an id","im a title","my name is about",new Date(3,4,5),true,true,true,"3eisyUn0NJMyfaDOMElH73xHoUj1",new Time(3,3,3,4),"student would like to know me",null,null);
//////            Event(String id, String title, String about, Date date, boolean isOpen, boolean needAttendence,
//////            boolean needVolunteer, String onnerID, Time time, String location,
//////                    ArrayList<String> volunteers, ArrayList<String> attendence)
////            mMessagesDatabaseReference.push().setValue(event);
//            return true;
//        } /*else if (id == R.id.add_dummy_data) {
//
//            Map<String, Object> hashMap = new HashMap<>();
//            hashMap.put("3eisyUn0NJMyf73xHoUj1", false);
//            hashMap.put("ldskjfh;ldskjadf", true);
//            hashMap.put("fjdhaiuhfdkljh", false);
//            Map<String, Object> mapVol = new HashMap<>();
//            mapVol.put("3eisyUn0NJMyf73xHoUj1", false);
//            mapVol.put("ldskjfh;ldskjadf", true);
//            mapVol.put("fjdhaiuhfdkljh", false);
//            String eventID = mMessagesDatabaseReference.push().getKey();
//            Event event = new Event(eventID, eventID, "اللقاء العلمي الطلاب والطالبات من منسوبي الجامعة بجميع المراحل الجامعية؛ مشجعاً على العمل خارج المتطلبات الأكاديمية ومحفزاً لإبداعاتهم.. كما أعطى اللقاء العلمي فرصة لاكتشاف قدرات الطلاب البحثية والفنية والابتكارية", new Date(3, "4", 2019).toMap(), true, true, true, mFirebaseAuth.getCurrentUser().getUid(), new Time(12, 00, 1, 00), "جامعة الملك سعود،كلية القانون ،البهو الرئيسي", mapVol, hashMap);
//            Log.e("event", mFirebaseAuth.getCurrentUser().getUid());
//
////            Event(String title, String about, Date date, boolean isOpen,
////                 boolean needAttendence, boolean needVolunteer, String onnerID, Time time,
////                 String location, Map<String, Boolean> vols, Map<String, Boolean> atts)
//            FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
//            Account account = new Account(currentUser.getUid(), true, "admin", "admin", "ziyad", 24, 4.79, 43700000, "notpassword", mapVol, hashMap);
//            mMessagesDatabaseReference.child(event.getID()).setValue(event.toMAp());
////            mMessagesDatabaseReference.child("users").child(currentUser.getUid()).setValue(account);
//            mFirebaseDatabase.getReference().child(ACCOUNT_TREE).child(currentUser.getUid()).setValue(account.toMap());
//            Log.e("account", currentUser.getUid());
////            mMessagesDatabaseReference.push().setValue(event.getId(),event.isOpen());
//        }*/
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.events) {
            // Handle the camera action
        } /*else if (id == R.id.articles) {

        } else if (id == R.id.both) {

        }*/ else if (id == R.id.web) {
            Toast.makeText(this, "coming soon", Toast.LENGTH_SHORT).show();
//            Uri webpage = Uri.parse("https://www.google.com");
//            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
//            if (intent.resolveActivity(getPackageManager()) != null) {
//                startActivity(intent);
//            }

        } else if (id == R.id.email) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, "email@LawClub.com");
            intent.putExtra(Intent.EXTRA_SUBJECT, "about LawClub app");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        } else if (id == R.id.phone) {
//            mMessagesDatabaseReference.child(MainActivity.ACCOUNT_TREE).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                        Log.e("findNumber", "down ");
//                        Log.e(dataSnapshot1.getValue(Account.class).getFirebaseID(), "==" + dataSnapshot1.getValue(Account.class).getAccType() );
//
//                        if (dataSnapshot1.getValue(Account.class).getAccType().equals("Supervisor")) {
//                            String number = dataSnapshot.getValue(Account.class).getPhone().split("0",2)[1];
//                            Uri uri = Uri.parse("https://wa.me/966"+number);
//                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                            if (intent.resolveActivity(getPackageManager()) != null) {
//                                startActivity(intent);
//                                break;
//                            }
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });

            Uri uri = Uri.parse("https://wa.me/966541544541");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
        if (id == R.id.ftc_web) {
            Toast.makeText(this, "coming soon", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }



}
