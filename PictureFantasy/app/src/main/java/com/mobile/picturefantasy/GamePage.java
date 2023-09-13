package com.mobile.picturefantasy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.Random;

public class GamePage extends AppCompatActivity {

    TextView name;
    TextView gameName;
    ListView listOfPlayers;
    FirebaseDatabase database;
    List<String> names;
    Button readyButton;
    FirebaseAuth Auth;
    int count = 0;
    int countTwo = 0;
    Button home;
    Button start;
    int startCalled = 0;
    ArrayList<String> names2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);

        name = findViewById(R.id.displayNameInGame);
        gameName = findViewById(R.id.nameOfGame);
        listOfPlayers = findViewById(R.id.listOfPlayers);
        readyButton = findViewById(R.id.getReady);
        names = new ArrayList<>();
        Auth = FirebaseAuth.getInstance();
        home = findViewById(R.id.backToHome);
        start = findViewById(R.id.startGame);
        names2 = new ArrayList<>();

        Intent intent = getIntent();
        String nameS = intent.getStringExtra("name");
        name.setText(nameS);
        String gameNameS = intent.getStringExtra("game");
        gameName.setText(gameNameS);

        database = FirebaseDatabase.getInstance();
        DatabaseReference users = database.getReference("users");
        DatabaseReference games = database.getReference("games");
        games.orderByValue().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    if (snap.child("game").getValue(String.class).equals(gameNameS)) {
                        snap.getRef().child("players").orderByValue().addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot snap2 : snapshot.getChildren()) {
                                    names.add(snap2.child("name").getValue(String.class));
                                    if ((snap2.child("name").getValue(String.class).equals(Auth.getCurrentUser().getDisplayName())) && (count == 0)) {
                                        if (!snap.child("host").getValue(String.class).equals(Auth.getCurrentUser().getDisplayName())) {
                                            start.setVisibility(View.GONE);
                                        }
                                        if (snap2.child("ready").getValue(Boolean.class)) {
                                            readyButton.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(android.R.color.holo_green_light));
                                            readyButton.setText("Ready");
                                        }
                                        if (snap.child("gameStarted").getValue(Boolean.class)) {
                                            readyButton.setVisibility(View.GONE);
                                        }
                                    }
                                }
                                if (count == 0) {
                                    count++;
                                    listOfPlayers.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, names));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countTwo = 0;
                DatabaseReference game = database.getReference("games");
                game.orderByValue().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            if (snap.child("game").getValue(String.class).equals(gameNameS)) {
                                snap.getRef().orderByValue().addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        snap.getRef().child("players").orderByValue().addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot snap : snapshot.getChildren()) {
                                                    if ((snap.child("name").getValue(String.class).equals(Auth.getCurrentUser().getDisplayName()))&& (countTwo == 0)) {
                                                        if (Boolean.TRUE.equals(snap.child("ready").getValue(boolean.class))) {
                                                            snap.getRef().child("ready").setValue(false);
                                                            readyButton.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(android.R.color.holo_red_dark));
                                                            readyButton.setText("Not Ready");
                                                            countTwo++;
                                                        } else {
                                                            snap.getRef().child("ready").setValue(true);
                                                            readyButton.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(android.R.color.holo_green_light));
                                                            readyButton.setText("Ready");
                                                            countTwo++;
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(), HomePage.class);
                intent2.putExtra("name", nameS);
                startActivity(intent2);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCalled = 1;
                DatabaseReference game = database.getReference("games");
                game.orderByValue().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapG : snapshot.getChildren()) {
                            if ((snapG.child("game").getValue(String.class).equals(gameNameS)) && (startCalled == 1)) {
                                snapG.getRef().orderByValue().addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        snapG.getRef().child("players").orderByValue().addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (startCalled == 1) {
                                                    startCalled = 0;
                                                for (DataSnapshot snap : snapshot.getChildren()) {
                                                    if (!snap.child("ready").getValue(Boolean.class)) {
                                                        Toast.makeText(GamePage.this, "Not all players are ready!", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }
                                                    names2.add(snap.child("name").getValue(String.class));
                                                }
                                                if (snapshot.getChildrenCount() < 3) {
                                                    Toast.makeText(GamePage.this, "Not enough players. Make sure there are 3 or more.", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    snapG.getRef().child("gameStarted").setValue(true);
                                                    snapG.getRef().child("currentRound").setValue(1);
                                                    snapG.getRef().child("maxRounds").setValue(snapshot.getChildrenCount());
                                                    Random random = new Random();
                                                    int randomNumber = random.nextInt((int)snapshot.getChildrenCount());
                                                    snapG.getRef().child("picturePicker").setValue(names2.get(randomNumber));
                                                    Intent intent2 = new Intent(getApplicationContext(), GameActivePage.class);
                                                    intent2.putExtra("name", Auth.getCurrentUser().getDisplayName());
                                                    intent2.putExtra("game", gameNameS);
                                                    intent2.putExtra("picturePicker", names2.get(randomNumber));
                                                    startActivity(intent2);
                                                }
                                            } }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });




    }


}