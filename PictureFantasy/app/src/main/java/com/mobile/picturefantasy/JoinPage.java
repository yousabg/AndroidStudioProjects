package com.mobile.picturefantasy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class JoinPage extends AppCompatActivity {

    EditText gameID;
    Button joinButton;
    DatabaseReference gameRef;
    DatabaseReference gameIDRef;
    DatabaseReference playerRef;
    FirebaseDatabase database;
    ArrayList<String> gameIDs;
    FirebaseAuth Auth;
    ArrayList<String> playerNames;
    DatabaseReference game;
    Button backToHomeFromJoin;
    int playerJoinReq = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_page);

        gameID = findViewById(R.id.gameJoinField);
        joinButton = findViewById(R.id.joinButton);

        database = FirebaseDatabase.getInstance();
        gameRef = database.getReference("games");
        gameIDs = new ArrayList<>();
        Auth = FirebaseAuth.getInstance();
        playerNames = new ArrayList<>();
        backToHomeFromJoin = findViewById(R.id.backToHomeFromJoin);

        gameRef.orderByValue().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String s = snap.child("gameID").getValue(String.class);
                    gameIDs.add(s);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerJoinReq = 1;
                String gameIDentered = gameID.getText().toString();
                gameRef.orderByValue().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (playerJoinReq == 1) {
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                DataSnapshot currentRef = snap.child("gameID");
                                if (gameIDentered.equals(currentRef.getValue(String.class))) {
                                    game = snap.getRef();
                                    DataSnapshot gameStartedRef = snap.child("gameStarted");
                                    if (gameStartedRef.getValue(Boolean.class)) {
                                        Toast.makeText(JoinPage.this, "Game already started", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
//                                    GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
//                                    List<String> playerIDs = snap.child("players").getValue(t);
                                    game.child("players").orderByValue().addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            while (playerJoinReq == 1) {
                                                for (DataSnapshot snap : snapshot.getChildren()) {
                                                    playerNames.add(snap.child("name").getValue(String.class));
                                                }
                                                if (playerNames.contains(Auth.getCurrentUser().getDisplayName())) {
                                                    Toast.makeText(JoinPage.this, "Already in game!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    ArrayList<Player> players = new ArrayList<>();
                                                    for (String name : playerNames) {
                                                        players.add(new Player(name, false, 0, false));
                                                    }
                                                    players.add(new Player(Auth.getCurrentUser().getDisplayName(), false, 0, false));
                                                    game.child("players").setValue(players);
                                                    Toast.makeText(JoinPage.this, "Joined", Toast.LENGTH_SHORT).show();
                                                }
                                                playerJoinReq = 0;
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
//                                    if (!playerIDs.contains(Auth.getCurrentUser().getEmail())) {
//                                        playerIDs.add(Auth.getCurrentUser().getEmail(), false, 0);
//                                        snap.getRef().child("players").setValue(playerIDs);
//                                    } else {
//                                        Toast.makeText(JoinPage.this, "Already in this game!", Toast.LENGTH_SHORT).show();
//                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        backToHomeFromJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                intent.putExtra("name", Auth.getCurrentUser().getDisplayName());
                startActivity(intent);
            }
        });
    }
}
