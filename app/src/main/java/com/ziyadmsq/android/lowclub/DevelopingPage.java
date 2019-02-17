package com.ziyadmsq.android.lowclub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DevelopingPage extends android.support.v4.app.Fragment {
    public DevelopingPage(){
        //i'm empty in side </3
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.developing_page, container, false);
        return view;
    }
}
