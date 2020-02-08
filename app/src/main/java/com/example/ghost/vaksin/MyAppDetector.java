package com.example.ghost.vaksin;

import android.app.Application;

/**
 * Created by ghost on 02/08/16.
 */
public class MyAppDetector extends Application {
    private static MyAppDetector mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized MyAppDetector getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectionDetector.ConnectionDetectorListener listener) {
        ConnectionDetector.connectionDetectorListener = listener;
    }
}
