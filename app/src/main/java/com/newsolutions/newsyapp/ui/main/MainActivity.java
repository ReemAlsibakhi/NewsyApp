package com.newsolutions.newsyapp.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;
import com.newsolutions.newsyapp.R;
import com.newsolutions.newsyapp.ui.utilis.NetworkConnectivity;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    @Inject
    NetworkConnectivity networkConnectivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    // This method is called whenever a certain button from the UI is clicked
    public void internetCheckBtnClicked() {
        networkConnectivity.
                checkInternetConnection((isConnected) -> Toast.makeText(this, isConnected + "", Toast.LENGTH_SHORT).show());
    }
    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkConnectivity, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkConnectivity);
        super.onStop();
    }
}