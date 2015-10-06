package com.akhadidja.android.flashnews;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

public class FlashNewsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, getString(R.string.Application_ID), getString(R.string.Client_Key));
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
