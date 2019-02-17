package com.ziyadmsq.android.lowclub;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
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
        Log.e("EventAdapter", "getView");
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
            public void onClick(View view) {
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
                        } else{
                            MainActivity.account.getMyJoin().put(event.getID(), false);
                        }
                        mMessagesDatabaseReference.child(MainActivity.ACCOUNT_TREE).child(mFirebaseAuth.getCurrentUser().getUid()).child("myJoin").setValue(MainActivity.account.getMyJoin());
                        Toast.makeText(getContext(), "SuccessListener", Toast.LENGTH_LONG).show();
                        joinButton.setEnabled(false);
                    }
                });

            }
        });
        attButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        Toast.makeText(getContext(), "SuccessListener", Toast.LENGTH_LONG).show();

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
                    Log.e("ValueEventListener", "down ");
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
