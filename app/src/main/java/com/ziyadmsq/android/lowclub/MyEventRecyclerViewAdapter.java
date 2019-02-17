package com.ziyadmsq.android.lowclub;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import java.util.List;


public class MyEventRecyclerViewAdapter extends RecyclerView.Adapter<MyEventRecyclerViewAdapter.ViewHolder> implements Filterable {

    Context mContext;
    List<Event> mValues;
//    private final OnListFragmentInteractionListener mListener;

    public MyEventRecyclerViewAdapter(List<Event> items, Context context) {
        this.mContext = context;
        if (items == null) {
            Log.e("MyEventRecyViewAdapter", "sorry null value");

        } else {
            Log.e("MyEventRecyViewAdapter", "not null value" + " == " + String.valueOf(items.size()));

        }
//        for(Event event:items){
////            Log.e("MyEventRecyViewAdapter", String.valueOf(i));
//            if(event.getVols().containsKey(MainActivity.account.getFirebaseID())){
//
//            }
//        }
        this.mValues = items;
    }

    @Override // we can say its done over here..
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.fragment_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
//        Log.e("onBindViewHolder", String.valueOf(mValues.size()));
//        for(Event event:mValues){
//            Log.e("MyEventRecyViewAdapter", event.getID());
//        }
//        if(mValues.get(position).getVols().containsKey("fpQUTySrlgUD2qo2WPOaGWFNUAl1")){
        Log.e("onBindViewHolder", " == " + MainActivity.account.getFirebaseID());
        holder.aboutTextView.setText(mValues.get(position).getAbout());
        holder.titleTextView.setText(mValues.get(position).getTitle());
//            if(mValues.get(position).getDate()!=null){
        holder.dateTextView.setText(mValues.get(position).getDate().toMap().get("D") + "/" + mValues.get(position).getDate().toMap().get("M") + "/" + mValues.get(position).getDate().toMap().get("Y"));
//            }
//        }


//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.mItem);
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                return null;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView titleTextView;
        TextView aboutTextView;
        TextView dateTextView;
//        android.support.v7.widget.CardView cardView;


        public ViewHolder(View view) {
            super(view);
//            might be a good idea
//            cardView = view.findViewById(R.id.card_view);
            titleTextView = (TextView) view.findViewById(R.id.title1);
            aboutTextView = (TextView) view.findViewById(R.id.about1);
            dateTextView = view.findViewById(R.id.date1);


        }

        @Override
        public String toString() {
            return super.toString() + " '" + "" + "'";
        }
    }
}
