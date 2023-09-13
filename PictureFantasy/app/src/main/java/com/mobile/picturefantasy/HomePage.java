package com.mobile.picturefantasy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {

    Button signOutButton;
    Button hostButton;
    Button joinButton;
    ListView listOfGames;
    TextView displayName;
    FirebaseAuth Auth;
    ArrayList<String> games;
    FirebaseDatabase database;
    DatabaseReference game;
    int onItemCount = 0;
    String picturePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        hostButton = findViewById(R.id.HostButton);
        joinButton = findViewById(R.id.JoinButton);
        listOfGames = findViewById(R.id.listOfGames);
        displayName = findViewById(R.id.displayName);
        signOutButton = findViewById(R.id.signOut);
        Auth = FirebaseAuth.getInstance();
        games = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        DatabaseReference gameRef = database.getReference("games");

        gameRef.orderByValue().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                games.subList(0, games.size()).clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
//                    GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
//                    List<String> playerIDs = snap.child("players").getValue(t);
                    game = snap.getRef();
                    snap.getRef().child("players").orderByValue().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<String> names = new ArrayList<>();
                            for (DataSnapshot snap2 : snapshot.getChildren()) {
                                names.add(snap2.child("name").getValue(String.class));
                            }
                            if (names.contains(Auth.getCurrentUser().getDisplayName())) {
                                games.add(snap.child("game").getValue(String.class));
                            }
                            listOfGames.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, games));

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        displayName.setText(name);

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Auth.signOut();
                if (Auth.getCurrentUser() == null) {
                    Intent intent2 = new Intent(getApplicationContext(), LoginPage.class);
                    startActivity(intent2);
                }
            }
        });

        hostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(getApplicationContext(), HostPage.class);
                startActivity(intent3);
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(getApplicationContext(), JoinPage.class);
                startActivity(intent4);
            }
        });

        listOfGames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String nameOfGame = (String)adapterView.getItemAtPosition(i);
                onItemCount = 1;
                gameRef.orderByValue().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (onItemCount == 1) {
                            for (DataSnapshot snap: snapshot.getChildren()) {
                                if (snap.child("game").getValue(String.class).equals(nameOfGame)) {
                                    snap.getRef().orderByValue().addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (onItemCount == 1) {
                                                onItemCount = 0;
                                                if (snapshot.child("gameStarted").getValue(boolean.class)) {
                                                    Intent intent3 = new Intent(getApplicationContext(), GameActivePage.class);
                                                    intent3.putExtra("game", nameOfGame);
                                                    intent3.putExtra("name", name);
                                                    intent3.putExtra("picturePicker", snap.child("picturePicker").getValue(String.class));
                                                    startActivity(intent3);
                                                } else {
                                                    Intent intent2 = new Intent(getApplicationContext(), GamePage.class);
                                                    intent2.putExtra("game", nameOfGame);
                                                    intent2.putExtra("name", name);
                                                    startActivity(intent2);
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
        });



    }
}