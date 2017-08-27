package com.devcti.frhec.seasmamon;

import android.view.View;

import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by frhec on 8/26/17.
 */

public class MyAddListener implements View.OnClickListener{

    @Override
    public void onClick(View v) {
        FirebaseMessaging.getInstance().subscribeToTopic("news");
    }
}