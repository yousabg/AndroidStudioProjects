package com.mobile.picturefantasy;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.InputStream;

public class LoadingPage extends AppCompatActivity {

    Button connect;
    private FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_page);

        connect = findViewById(R.id.Connect);
        Auth = FirebaseAuth.getInstance();

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Auth.getCurrentUser() != null) {
                    Intent intent = new Intent(getApplicationContext(), HomePage.class);
                    intent.putExtra("name", Auth.getCurrentUser().getDisplayName());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = Auth.getCurrentUser();
    }
}