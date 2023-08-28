package com.mobile.tickerwatchlistmanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.tickerwatchlistmanager.model.Ticker;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private TickerViewModel model;
    String ticker;
    public static final String PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        model = new ViewModelProvider(this).get(TickerViewModel.class);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            String[] perms = new String[]{Manifest.permission.RECEIVE_SMS};
            ActivityCompat.requestPermissions(this, perms, 101);
            requestPermissions(perms, 101);
        }



        prefs = getSharedPreferences(PREFS_NAME, 0);

        String maybe = prefs.getString("ticker", "");
        if (!maybe.equals("")) {
            //model.setTickerData(maybe);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString("ticker", model.history.get(model.history.size()-1).toString());
//        editor.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString("ticker", model.history.get(model.history.size()-1).toString());
//        editor.commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setMessage(intent.getStringExtra("sms"));
    }

    public void setMessage(String message) {
        if (message != null && message.contains("Ticker<<") && message.contains(">>")) {
            ticker = message.substring(message.indexOf("<") + 2, message.indexOf(">"));
            ticker = ticker.toUpperCase();
            if (ticker.matches("[a-zA-Z]+")) {
//                Toast.makeText(getApplicationContext(), t.size(), Toast.LENGTH_LONG).show();
                model.setTickerData(ticker);
            } else {
                Toast.makeText(getApplicationContext(), "Not corect ticket format.", Toast.LENGTH_LONG).show();
            }


        } else if (message != null) {
            Toast.makeText(getApplicationContext(), "No valid watchlist entry was found.", Toast.LENGTH_LONG).show();
        }

    }

}