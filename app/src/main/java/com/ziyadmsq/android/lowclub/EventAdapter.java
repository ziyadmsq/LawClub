package com.ziyadmsq.android.lowclub;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventAdapter extends ArrayAdapter<Event> {
    boolean flag = false;
    String username = "";

    public EventAdapter(@NonNull Context context, int resource, @NonNull List<Event> objects) {
        super(context, resource, objects);
        Log.e("EventAdapter", "constructor");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_event,
                    parent,/*TODO i think you should try to make it true*/ false);
        }
        Log.e("EventAdapter", "getItem(position");
        final Event event = getItem(position);
        final FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference mMessagesDatabaseReference;
        mMessagesDatabaseReference = mFirebaseDatabase.getReference();


        TextView titleTextView = convertView.findViewById(R.id.title);
        TextView aboutTextView = convertView.findViewById(R.id.about);
        TextView ownerTextView = convertView.findViewById(R.id.owner);
        TextView locationTextView = convertView.findViewById(R.id.locationTextView);
        final Button joinButton = convertView.findViewById(R.id.joinButton);
        final Button attButton = convertView.findViewById(R.id.attendButton);
        TextView timeTextView = convertView.findViewById(R.id.timeTextView);
        TextView dateTextView = convertView.findViewById(R.id.calenderTextView);

        if(!event.isOpen()){
            joinButton.setEnabled(false);
            attButton.setEnabled(false);
        }else{
            if (!event.isNeedVolunteer() /*&& (event.getVols() != null && event.getVols().containsKey(MainActivity.account.getFirebaseID()))*/) {
                joinButton.setEnabled(false);
            }
            if (!event.isNeedAttendence() /*&& event.getAtts() != null && event.getAtts().containsKey(MainActivity.account.getFirebaseID())*/) {
                attButton.setEnabled(false);
            }
        }

//        if( event.getVols()!=null && event.getVols().containsKey(MainActivity.account.getFirebaseID()) ){
//            Log.e("yess","yesssssss1");
//            joinButton.setEnabled(false);
//        }
//        if( event.getAtts()!=null && event.getAtts().containsKey(MainActivity.account.getFirebaseID())){
//            Log.e("yess","yesssssss2");
//            attButton.setEnabled(false);
//        }


//        if (!event.isOpen()) {
//            Log.e("!event.isOpen()",event.getID());
//            Log.e("!event.isOpen()", String.valueOf(event.isOpen()));
//            joinButton.setEnabled(false);
//            attButton.setEnabled(false);
//        }
//        if (!event.isNeedVolunteer()) {
//            Log.e("!event.isNeedVolunteer",event.getID());
//            joinButton.setEnabled(false);
//        }
//        if (!event.isNeedAttendence()) {
//            Log.e("!event.isNeedAttendence",event.getID());
//            attButton.setEnabled(false);
//        }

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //TODO : add him to the vol list in the firebase
                Log.e("joinButton", "clickable");
                mMessagesDatabaseReference.child(MainActivity.EVENT_TREE);
                mMessagesDatabaseReference.keepSynced(true);
                Map<String, Object> hashMap = new HashMap<>();
                hashMap.putAll(event.getAtts());
                hashMap.put(mFirebaseAuth.getCurrentUser().getUid(), false);
                event.setVols(hashMap);
                mMessagesDatabaseReference.child(MainActivity.EVENT_TREE).child(event.getID()).child("vols").setValue(event.getVols())
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "FailureListener", Toast.LENGTH_LONG).show();
                            }
                        }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Toast.makeText(getContext(), "CanceledListener", Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (MainActivity.account.getMyJoin() == null) {
                            Map<String, Object> map = new HashMap<>();
                            map.put(event.getID(), false);
                            MainActivity.account.setMyJoin(map);
                        } else {
                            MainActivity.account.getMyJoin().put(event.getID(), false);
                        }
                        mMessagesDatabaseReference.child(MainActivity.ACCOUNT_TREE).child(mFirebaseAuth.getCurrentUser().getUid()).child("myJoin").setValue(MainActivity.account.getMyJoin());
//                        Toast.makeText(getContext(), "SuccessListener", Toast.LENGTH_LONG).show();
                        Snackbar mSnackbar = Snackbar.make(view, "تم تسجيلك في قائمة المنظمين \uD83C\uDF88\uD83D\uDC4C\uD83C\uDFFB", Snackbar.LENGTH_LONG);
                        View mView = mSnackbar.getView();
                        TextView mTextView = (TextView) mView.findViewById(android.support.design.R.id.snackbar_text);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        } else {
                            mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                        }
                        mSnackbar.show();
                        joinButton.setEnabled(false);
                    }
                });

            }
        });
        attButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
                FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference mMessagesDatabaseReference;
                mMessagesDatabaseReference = mFirebaseDatabase.getReference().child(MainActivity.EVENT_TREE);

                event.getAtts().put(mFirebaseAuth.getCurrentUser().getUid(), false);
                mMessagesDatabaseReference.child(event.getID()).child("atts").setValue(event.getAtts())
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "FailureListener", Toast.LENGTH_LONG).show();
                            }
                        }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Toast.makeText(getContext(), "CanceledListener", Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        attButton.setEnabled(false);
//                        Toast.makeText(getContext(), "SuccessListener", Toast.LENGTH_LONG).show();

                        Snackbar mSnackbar = Snackbar.make(view, "حياك الله", Snackbar.LENGTH_LONG);
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
            }
        });


//            messageTextView.setVisibility(View.VISIBLE);
//            photoImageView.setVisibility(View.GONE);


//        Query query = mMessagesDatabaseReference.child("Accounts").orderByChild("name").startAt(event.getOwnerID()).endAt(event.getOwnerID()+"\uf8ff");
//        String username = mMessagesDatabaseReference.child("Accounts").child(event.getOwnerID()).child("name");

        //TODO : IMPORTANT I HAVE FOUND THE "USERS" INSTADE OF MAINACTIVITY.ACC AND DID'N UPDATE IT
        mMessagesDatabaseReference.child(MainActivity.ACCOUNT_TREE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Log.e(dataSnapshot1.getValue(Account.class).getFirebaseID(), "==" + event.getOnnerID());
                    if (dataSnapshot1.getValue(Account.class).getFirebaseID().equals(event.getOnnerID())) {
                        username = dataSnapshot1.getValue(Account.class).getName();
                        Log.e("ValueEventListener", "getFirebaseID()==event.getOnnerID");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        titleTextView.setText(event.getTitle());
        aboutTextView.setText(event.getAbout());
        ownerTextView.setText(username);
        locationTextView.setText(event.getLocation());
        if (event.getTime() != null) {
            timeTextView.setText("from  " + event.getTime().fromTimeHH + ":" + event.getTime().fromTimeMM + "\nto  " + event.getTime().toTimeHH + ":" + event.getTime().toTimeMM);
        }
        if (event.getDate() != null) {
            dateTextView.setText(event.getDate().toMap().get("D") + "/" + event.getDate().toMap().get("M") + "/" + event.getDate().toMap().get("Y"));
        } else {
            dateTextView.setText("لم يحدد بعد");
        }
        return convertView;
    }
}
