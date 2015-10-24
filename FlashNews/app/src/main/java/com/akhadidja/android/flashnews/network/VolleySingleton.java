package com.akhadidja.android.flashnews.network;

import com.akhadidja.android.flashnews.FlashNewsApplication;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {

    private static VolleySingleton sInstance = null;
    private RequestQueue mRequestQueue;

    private VolleySingleton(){
        mRequestQueue= Volley.newRequestQueue(FlashNewsApplication.getAppContext());
    }

    public static VolleySingleton getInstance(){
        if(sInstance == null) {
            sInstance = new VolleySingleton();
        }
        return sInstance;
    }
    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }
}
