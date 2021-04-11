package com.newsolutions.newsyapp.ui.utilis;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.Toast;
import java.net.URL;
import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;


public class NetworkConnectivity extends BroadcastReceiver {
    private  AppExecutors appExecutors;
    private  Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        checkInternetConnection((isConnected) -> Toast.makeText(context.getApplicationContext(),
                "Connection :" + " "+isConnected, Toast.LENGTH_SHORT).show());
    }
    @Inject
    public NetworkConnectivity(AppExecutors appExecutors, Context context) {
        this.appExecutors = appExecutors;
        this.context = context;
    }
    public NetworkConnectivity() {
    }

    // Android framework does not allow network operations on the main thread,this run on another thread.
    public synchronized void checkInternetConnection(ConnectivityCallback callback) {
        appExecutors.getNetworkIO().execute(() -> {
            if (isNetworkAvailable()) {
                HttpsURLConnection connection = null;
                try {
                    //in some mobile devices for specific brands, the framework does not allow performing this process in Runtime then use HTTP connection request instead pingGoogle
                    connection = (HttpsURLConnection) new URL("https://clients3.google.com/generate_204").openConnection();
                    //set headers
                    connection.setRequestProperty("User-Agent", "Android");
                    connection.setRequestProperty("Connection", "close");
                    connection.setConnectTimeout(1000);
                    connection.connect();

                    boolean isConnected = connection.getResponseCode() == 204 && connection.getContentLength() == 0;
                    postCallback(callback, isConnected);
                    connection.disconnect();
                } catch (Exception e) {
                    postCallback(callback, false);
                    if (connection != null) connection.disconnect();
                }
            } else {
                postCallback(callback, false);
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            NetworkCapabilities cap = cm.getNetworkCapabilities(cm.getActiveNetwork());
            if (cap == null) return false;
            return cap.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = cm.getAllNetworks();
            for (Network n : networks) {
                NetworkInfo nInfo = cm.getNetworkInfo(n);
                if (nInfo != null && nInfo.isConnected()) return true;
            }
        } else {
            NetworkInfo[] networks = cm.getAllNetworkInfo();
            for (NetworkInfo nInfo : networks) {
                if (nInfo != null && nInfo.isConnected()) return true;
            }
        }
        return false;
    }

    //post the result on the main thread
    private void postCallback(ConnectivityCallback callBack, boolean isConnected) {
        appExecutors.mainThread().execute(() -> callBack.onDetected(isConnected));
    }

    //use to communicate with our UI classes when the server’s response comes back
    public interface ConnectivityCallback {
        void onDetected(boolean isConnected);
    }
    /**
     * boolean isConnected;
     * Process process = Runtime.getRuntime().exec("/system/bin/ping -c 1 8.8.8.8");
     * if (process.waitFor() == 0) isConnected = true;
     * else isConnected = false;
     */

}

