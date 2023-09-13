package com.mobile.picturefantasy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class LookThroughPicturesPage extends AppCompatActivity {

    FirebaseStorage storage;
    FirebaseDatabase database;
    DatabaseReference gamesRef;
    TextView name;
    ImageView picture;
    Button valid;
    Button retake;
    int gamesRefCalled = 0;
    ArrayList<String> players;
    int currentRound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_through_pictures_page);

        name = findViewById(R.id.nameOfPictureViewed);
        picture = findViewById(R.id.eachPicture);
        valid = findViewById(R.id.valid);
        retake = findViewById(R.id.retake);
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        gamesRef = database.getReference("games");
        players = new ArrayList<>();

        Intent intent = getIntent();
        String gameName = intent.getStringExtra("game");

        gamesRef.orderByValue().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (gamesRefCalled == 0) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        if (snap.child("game").getValue(String.class).equals(gameName)) {
                            snap.getRef().child("players").orderByValue().addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (gamesRefCalled == 0) {
                                            gamesRefCalled = 1;
                                            currentRound = snap.child("currentRound").getValue(Integer.class);
                                            for (DataSnapshot snap: snapshot.getChildren()) {
                                                players.add(snap.child("name").getValue(String.class));
                                            }

                                            StorageReference ref = storage.getReference();
                                            StorageReference imageRef = ref.child("images/");
                                            StorageReference gamesRef = imageRef.child(gameName);
                                            String r = "Round " + (currentRound -1);
                                            StorageReference roundRef = gamesRef.child(r);
                                            StorageReference playerRef = roundRef.child(players.get(0));
                                            name.setText(players.get(0));

                                            try {
                                                File localfile = File.createTempFile("tempfile", ".jpeg");
                                                playerRef.getFile(localfile);
                                                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                                picture.setImageBitmap(bitmap);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}