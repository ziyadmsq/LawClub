package com.ziyadmsq.android.lowclub;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
//private ChildEventListener mChildEventListener;


public class EventFragment extends android.support.v4.app.Fragment {

    //me!
    private MyEventRecyclerViewAdapter myEventRecyclerViewAdapter;
    private android.support.v7.widget.RecyclerView recyclerView;
    private List<Event> eventList;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("onCreate", "mEventAdapter");
        eventList = new ArrayList<>();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child(MainActivity.EVENT_TREE);
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            //TODO i think its cuz the names are diffrent
//                            Log.e("EventFragment",dataSnapshot.getValue(Account.class).getPassWord());
                            try {
                                Event event = dataSnapshot.getValue(Event.class);
                                Log.e("dataSnapshot.getValue", event.getID());
//                            Event event1 = new Event(null,null,null,null,false,true,true,null,null,dataSnapshot.getValue(Event.class).getLocation(),null,null);
                                if (event.getVols() != null && event.getVols().containsKey(MainActivity.account.getFirebaseID())) {

                                    eventList.add(event);
                                    Log.e("eventList onChildAdded", "==" + String.valueOf(eventList.size()));
                                }

//                            Event(String id, String title, String about, Date date, boolean isOpen, boolean needAttendence,
//                            boolean needVolunteer, String onnerID, Time time, String location,
//                                    ArrayList<String> volunteers, ArrayList<String> attendence)

                                myEventRecyclerViewAdapter.notifyDataSetChanged();
                                recyclerView.smoothScrollToPosition(0);
                            } catch (Exception e) {
                                Log.e("dataSnapshot.getValue", "hmmmm problem");

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
            }

            ;
            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
//        TextView emptyCell= view.findViewById(R.id.empty_cell);
//        if(eventList.size()==0){
//            emptyCell.setVisibility(View.INVISIBLE);
//        }else{
//            emptyCell.setVisibility(View.VISIBLE);
//        }
        myEventRecyclerViewAdapter = new MyEventRecyclerViewAdapter(eventList/*MainActivity.mEventListView*/, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(myEventRecyclerViewAdapter);

        return view;
    }

}
