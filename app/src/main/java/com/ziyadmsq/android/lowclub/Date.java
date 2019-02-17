package com.ziyadmsq.android.lowclub;

import java.util.HashMap;
import java.util.Map;

public class Date {
    public long D;
    public String M;
    public long Y;

    public Date(){

    }
    public Date(long d, String m, long y){
        this.D = d;
        this.M = m;
        this.Y = y;
    }

    public Map<String,Object> toMap(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("D",D);
        map.put("M",M);
        map.put("Y",Y);
        return map;
    }
}